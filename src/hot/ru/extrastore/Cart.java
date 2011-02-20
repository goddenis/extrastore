package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import ru.extrastore.model.Order;
import ru.extrastore.model.OrderLine;
import ru.extrastore.model.Product;

import java.io.Serializable;
import java.util.Map;

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

    @In
    Map<String, String> messages;

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
        return (order.getLines().size() == 0);
    }

    public void reset() {
        order = new Order();
    }

    public long getProductsCount() {
        return order.getLines().size();
    }

    public long getItemsCount() {
        int itemsCount = 0;
        for (OrderLine l : order.getLines()) {
            itemsCount += l.getQuantity();
        }
        return itemsCount;
    }

    public long getTotalCost() {
        return order.getTotalCost();
    }

    /**
     * @param itemsCount
     * @return russian language ending for the count
     */
    public String russianEnding(long itemsCount) {
        if (itemsCount >= 11 && itemsCount <= 19) {
            return messages.get("russianEnding2");
        } else {
            long remain = itemsCount % 10;
            if (remain == 1) {
                return "";
            } else if (remain > 1 && remain <= 4) {
                return messages.get("russianEnding1");
            } else {
                return messages.get("russianEnding2");
            }
        }
    }
}
