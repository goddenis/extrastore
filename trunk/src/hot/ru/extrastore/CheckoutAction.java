package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import ru.extrastore.model.Order;
import ru.extrastore.model.Store;
import ru.extrastore.model.User;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * File profile
 * Creator: Catalyst
 * Date: 21.02.11
 * Time: 17:47
 */
@Name("checkout")
@Scope(ScopeType.CONVERSATION)
public class CheckoutAction implements Serializable {

    @In("entityManager")
    EntityManager em;

    @Logger
    Log log;

    @In
    Renderer renderer;

    @In
    Events events;

    @In
    IdentityManager identityManager;

    @In
    Cart cart;

    @In
    Store store;

    @In("user")
    User customer;


    @Out(scope = ScopeType.CONVERSATION, required = false)
    Order currentOrder;

    @Create
    public void create() {
        log.info("CheckoutAction(#0).create()", this.hashCode());
    }

    @Destroy
    public void destroy() {
        log.info("CheckoutAction(#0).destroy()", this.hashCode());
    }

    @Conversational
    public String createOrder() {
        log.info("CheckoutAction(#0).createOrder()", this.hashCode());

        cart.update();
        currentOrder = cart.getOrder();
        currentOrder.setTotalCost(cart.getTotalCost());

        currentOrder.setCustomer(customer);

        currentOrder.setDeliveryType(store.getDeliveryTypes().iterator().next());
        currentOrder.setPaymentType(store.getPaymentTypes().iterator().next());


        return "success";
    }

    @Conversational
    public String submitDelivery() {
        log.info("CheckoutAction(#0).submitDelivery()", this.hashCode());
        return "success";
    }

    @Conversational
    public String submitPay() {
        log.info("CheckoutAction(#0).submitPay()", this.hashCode());
        return "success";
    }


    @Conversational
    public String confirm() {
        log.info("CheckoutAction(#0).confirm()", this.hashCode());

        if (currentOrder.getCustomer().getId() == 0) {
            em.persist(currentOrder.getCustomer());
        } else {
            em.merge(currentOrder.getCustomer());
        }


        if (currentOrder.getCustomer().getEmail() != null) {

            new RunAsOperation(true) {
                public void execute() {
                    IdentityManager.instance().changePassword(currentOrder.getCustomer().getEmail(), "password");
                }
            }.run();

            Identity id = Identity.instance();
            id.getCredentials().setUsername(currentOrder.getCustomer().getEmail());
            id.getCredentials().setPassword("password");
            id.login();
        }


        em.persist(currentOrder);
        events.raiseAsynchronousEvent("newOrder", currentOrder, store);    // Makes this slow operation asynchronous

        cart.reset();

        return "success";
    }

    @End
    public void thanks() {  // Method is just for ending conversation. It's called from pages.xml <page view-id="thanks.xhtml"/>
        log.info("CheckoutAction(#0).thanks()", this.hashCode());
    }
}
