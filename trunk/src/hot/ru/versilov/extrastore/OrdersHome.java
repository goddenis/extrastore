package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 26.06.2010
 * Time: 1:00:33
 * To change this template use File | Settings | File Templates.
 */

@Name("ordersHome")
public class OrdersHome extends EntityHome<Order> {

    public void setOrderId(Long id) {
        setId(id);
    }

    public Long getOrderId() {
        return (Long) getId();
    }

    @Override
    protected Order createInstance() {
        Order order = new Order();
        return order;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Order getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }


}
