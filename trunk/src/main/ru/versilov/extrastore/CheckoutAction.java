/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.bpm.CreateProcess;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.bpm.Actor;
import ru.versilov.extrastore.model.*;


import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateful
@Name("checkout")
public class CheckoutAction
    implements Checkout,
               Serializable
{
    private static final long serialVersionUID = -4651884454184474207L;

    public static final int AUTH_TYPE_NEW_USER = 1;
    public static final int AUTH_TYPE_USER = 2;
    public static final int AUTH_TYPE_NO_EMAIL = 3;


    @PersistenceContext(type=PersistenceContextType.EXTENDED)
    EntityManager em;

    @Logger
    private Log log;

    @In(value="currentUser", required=false)
    Customer customer;

    @In
    Identity identity;

    @In
    private FacesMessages facesMessages;

    @In
    Renderer renderer;

    

    int authType = AUTH_TYPE_NEW_USER;

    @In(create=true)
    @Out        
    ShoppingCart cart;

    @Out(scope=ScopeType.CONVERSATION,required=false)
    Order currentOrder;
    @Out(scope=ScopeType.CONVERSATION,required=false)
    Order completedOrder;

    @Out(scope=ScopeType.BUSINESS_PROCESS, required=false)
    long orderId;
    @Out(scope=ScopeType.BUSINESS_PROCESS, required=false)
    BigDecimal amount = BigDecimal.ZERO;
    @Out(value="customer",scope=ScopeType.BUSINESS_PROCESS, required=false)
    String customerName;    

    
    @Begin(nested=true, pageflow="checkout")
    public void createOrder() {
        currentOrder = new Order();

        for (OrderLine line: cart.getCart()) {
            currentOrder.addProduct(em.find(Product.class, line.getProduct().getProductId()),
                                    line.getQuantity());
        }

        currentOrder.calculateTotals();
        if (customer == null) {
            currentOrder.setCustomer(new Customer());
        } else {
            currentOrder.setCustomer(customer);
            setAuthType(AUTH_TYPE_USER);
        }
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    @Conversational
    public String auth() {
        System.out.println("Authorization.");

        if (identity.getCredentials().getUsername().length() == 0 && authType != AUTH_TYPE_NO_EMAIL) {
            facesMessages.add("Введите электронную почту");
            return null;
        }

        switch (authType) {
            case AUTH_TYPE_NEW_USER:

                // Check, if user already exists
                if (em.createQuery("select u from User u where u.email = #{identity.username}")
                                 .getResultList().size() > 0) {
                    facesMessages.add("Эта электронная почта уже используется.");
                    return null;
                }


                this.customer = new Customer();
                this.customer.setEmail(identity.getCredentials().getUsername());
                this.customer.setPassword("password");    // If it's less, than 6 symbols weird exception occurs: null id in Customer entyr (don't flush the session, after an exception occurs).

                this.customer.setZip("443110");

                em.persist(customer);

                identity.getCredentials().setPassword(this.customer.getPassword());

                // This is our fast injection!
                Contexts.getSessionContext().set("currentUser", this.customer);
//                break;
                // No go on and login the new user!

            case AUTH_TYPE_USER:

                if (identity.login() == null) {
                    System.out.println("User did not found.");
                    return null;
                } else {
                    System.out.println("User successfully logged in.");
                    this.customer = (Customer)Contexts.getSessionContext().get("currentUser");
                    Actor.instance().setId(customer.getEmail());

                    log.debug("this.customer == #0, currentUser = #{currentUser}");

                }
                break;

            case AUTH_TYPE_NO_EMAIL:
                this.customer = new Customer();
                // Fast injection!
                Contexts.getSessionContext().set("currentUser", this.customer);
                break;

            default:
                throw new IllegalArgumentException("Unkonown auth type.");
        }
        this.currentOrder.setCustomer(this.customer);
        return "auth";
    }


    public String fillRegionAndAreaByIndex() {
        System.out.println("Checkout: Filling region and city.");
        int zip = Integer.parseInt(customer.getZip());
        OPS ops;
        try {
            ops = (OPS)em.createQuery("select o from OPS o where o.index=" + zip).getSingleResult();
        } catch (NoResultException e) {
            facesMessages.addToControl("zip", "Такого индекса не существует.");
            return null;
        }
        customer.setRegion(ops.getRegion());
        customer.setArea(ops.getArea());
        customer.setCity(ops.getCity());

        return null;
    }

    @Conversational
    public void address() {
        Events.instance().raiseAsynchronousEvent("submitAddress", currentOrder.getCustomer().getAddress1());

    }

    @Conversational
    public void pay() {
        System.out.println("User payed!");
    }


    @End
    @CreateProcess(definition="OrderManagement", processKey="#{completedOrder.orderId}")
//    @Restrict("#{identity.loggedIn}")
    public Order submitOrder() {
        try {
            completedOrder = purchase(customer, currentOrder);

            orderId      = completedOrder.getOrderId();
            amount       = completedOrder.getNetAmount();
            customerName = completedOrder.getCustomer().getEmail();
    
        } catch (InsufficientQuantityException e) {
            for (Product product: e.getProducts()) {
                Contexts.getEventContext().set("prod", product);
                FacesMessages.instance().addFromResourceBundle("checkoutInsufficientQuantity");
            }
            return null;
        }
        
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Send order notification here
        sendEmailNotification(completedOrder);

        cart.resetCart();
        return completedOrder;
    }

    private Order purchase(Customer customer, Order order)
        throws InsufficientQuantityException, Exception
    {
        order.setCustomer(customer);
        order.setOrderDate(new Date());

        List<Product> errorProducts = new ArrayList<Product>();
        for (OrderLine line: order.getOrderLines()) {
            Inventory inv = line.getProduct().getInventory();
            if (!inv.order(line.getQuantity())) {
                errorProducts.add(line.getProduct());
            }
        }

        if (errorProducts.size()>0) {
            throw new InsufficientQuantityException(errorProducts);
        }

        order.calculateTotals();

        try {
            if (customer.getId() == 0) {
                em.persist(customer);
            } else {
                em.merge(customer);
            }
            em.persist(order);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return order;
    }

    protected void sendEmailNotification(Order order) {
        try {
            renderer.render("/order/email_notification.xhtml");
            renderer.render("/email/order_received.xhtml");
        } catch (Exception e) {
            log.error("Error saving new order", e);
            facesMessages.add("Ошибка при отправке уведомления о заказе: " + e.getMessage());
        }
    }

    @End
    public void cancel() {

    }

    @Remove
    public void destroy() {}
    
}
