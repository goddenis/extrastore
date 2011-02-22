package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;
import ru.extrastore.model.Address;
import ru.extrastore.model.Customer;
import ru.extrastore.model.Order;

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
    private FacesMessages facesMessages;


    @In
    Cart cart;

    @Out(scope = ScopeType.CONVERSATION)
    Order currentOrder;

    @Create
    public void create() {
        log.info("CheckoutAction(#0).create()", this.hashCode());
    }

    @Destroy
    public void destroy() {
        log.info("CheckoutAction(#0).destroy()", this.hashCode());
    }

    @Begin(join = true)
    public String createOrder() {
        log.info("CheckoutAction(#0).createOrder()", this.hashCode());

        cart.update();
        this.currentOrder = cart.getOrder();

        Customer customer = new Customer();
        customer.setAddress(new Address());
        currentOrder.setCustomer(customer);

        return "success";
    }

    @Conversational
    public String submitDelivery() {
        log.info("CheckoutAction(#0).submitDelivery()", this.hashCode());
        return "success";
    }


    @Conversational
    public String confirm() {
        log.info("CheckoutAction(#0).confirm()", this.hashCode());

        if (currentOrder.getCustomer().getAddress().getId() == 0) {
            em.persist(currentOrder.getCustomer().getAddress());
        }

        if (currentOrder.getCustomer().getId() == 0) {
            em.persist(currentOrder.getCustomer());
        }

        em.persist(currentOrder);
        events.raiseAsynchronousEvent("newOrder", currentOrder);    // Makes this slow operation asynchronous

        cart.reset();

        return "success";
    }

    @End
    public void thanks() {  // Method is just for ending conversation. It's called from pages.xml-><page thanks.xhtml/>
        log.info("CheckoutAction(#0).thanks()", this.hashCode());
    }
}
