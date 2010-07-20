package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.web.RequestParameter;
import ru.versilov.extrastore.model.Order;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 21.05.2010
 * Time: 19:22:47
 * To change this template use File | Settings | File Templates.
 */

@Stateful
@Name("adminshoworders")
public class AdminShowOrdersAction implements AdminShowOrders {

    @PersistenceContext
    EntityManager em;

    @DataModel
    List<Order> adminOrders;


    @DataModelSelection
    @Out(value="myorder", required=false, scope= ScopeType.CONVERSATION)
    Order order;

    @RequestParameter
    String filter = null;


    @Factory("adminOrders")
    public String findOrders() {
        if (filter != null)    {
            // Apply filter

            Order.Status status = Order.Status.valueOf(filter);
            adminOrders = em.createQuery("select o from Order o where o.status = :status")
                    .setParameter("status", status)
                    .getResultList();

        } else {
            // Show all orders

            adminOrders = em.createQuery("select o from Order o")
                    .getResultList();
        }



        return "orders";

    }

    public String detailOrder() {
        em.refresh(order);
        return "orders";
    }

    public String cancelOrder() {
        em.refresh(order);

        if ( order.getStatus() != Order.Status.OPEN ) {
            return null;
        }

        order.setStatus(Order.Status.CANCELLED);

        return findOrders();
    }

    public String acceptOrder() {
        em.refresh(order);

        if ( order.getStatus() != Order.Status.OPEN ) {
            return null;
        }

        order.setStatus(Order.Status.PROCESSING);

        return findOrders();
    }

    public String shipOrder() {
        em.refresh(order);

        if ( order.getStatus() != Order.Status.PROCESSING ) {
            return null;
        }

        order.setStatus(Order.Status.SHIPPED);

        return findOrders();
    }

                                                                                  


    @Remove
    public void destroy() {

    }
}
