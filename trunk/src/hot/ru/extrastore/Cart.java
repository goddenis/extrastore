package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import ru.extrastore.model.Order;
import ru.extrastore.model.Product;

import java.io.Serializable;

/**
 * File profile
 * Creator: Catalyst
 * Date: 18.02.11
 * Time: 17:25
 */
@Name("cart")
@Scope(ScopeType.SESSION)
public class Cart implements Serializable {
    Order order = new Order();

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void addProduct(Product p, long quantity) {
        order.addProduct(p, quantity);
    }

    public boolean isEmpty() {
        return order.getLines().size() == 0;
    }

    public long getProductsCount() {
        return order.getLines().size();
    }
}
