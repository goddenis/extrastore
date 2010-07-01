package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.richfaces.model.selection.Selection;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 25.06.2010
 * Time: 0:30:45
 * To change this template use File | Settings | File Templates.
 */
@Name("ordersList")
public class OrdersList extends EntityQuery<Order> {
    private static final String EJBQL = "select o from Order o";

    private static final String[] RESTRICTIONS = {
            "lower(o.customer.firstName) like lower(concat(#{ordersList.curOrder.customer.firstName},'%'))",
            "lower(o.customer.city) like lower(concat(#{ordersList.curOrder.customer.city},'%'))"};

    private Order order = new Order();

    private Selection selection;

    public OrdersList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(125);
    }

    public Order getCurOrder() {
        return order;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public void sent(Order order) {
        // Change status and send e-mail notification here.
        order.setStatus(Order.Status.SHIPPED);
        System.out.println("Order No." + order.getOrderId() + " was sent.");
    }

    protected Order getOrderById(Integer id) {
        return null;
    }

    public void send() {
        String keys = "(";
        Iterator ik = selection.getKeys();
        while (ik.hasNext()) {
            Integer key = (Integer)ik.next();
            if (keys.length() == 1) {
                keys += key;
            } else {
                keys += (", " + key);
            }
        }
        keys += ")";
        String ejbql = "update Order o set o.status = 3 where o.orderId in " + keys;
        int result = getPersistenceContext().createQuery(ejbql).executeUpdate();
        System.out.println(result + " orders were updated.");

    }

    
}
