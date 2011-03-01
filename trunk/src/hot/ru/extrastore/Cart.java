package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import ru.extrastore.model.Order;
import ru.extrastore.model.OrderLine;
import ru.extrastore.model.Product;
import ru.extrastore.model.Store;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
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

    @In
    Store store;

    Order order = new Order();

    Map<Product, Boolean> selection = new HashMap<Product, Boolean>();

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void addProduct(Product p, long quantity) {
        order.addProduct(p, quantity);
        selection.put(p, true);
    }

    public Map<Product, Boolean> getSelection() {
        return selection;
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

    /**
     * @return products total cost, without delivery.
     */
    public long getSubTotal() {
        return order.getSubTotal();
    }

    public long getDeliveryCost() {
        return store.getDeliveryTypes().iterator().next().getCost(this.order);
    }

    public long getTotalCost() {
        return getSubTotal() + getDeliveryCost();
    }


    public void updateTotals() {
        // empty, because totals are calculated inline.
    }

    public void update() {
        Iterator<OrderLine> i = order.getLines().iterator();
        while (i.hasNext()) {
            OrderLine l = i.next();
            if (selection.get(l.getProduct()) == false) {
                selection.remove(l.getProduct());
                i.remove();
            }
        }
    }

    /**
     * @param itemsCount
     * @return russian language ending for the count
     */
    public static String russianEnding(long itemsCount) {
        final String RUSSIAN_ENDING1 = "а";
        final String RUSSIAN_ENDING2 = "ов";

        if (itemsCount >= 11 && itemsCount <= 19) {
            return RUSSIAN_ENDING2;
        } else {
            long remain = itemsCount % 10;
            if (remain == 1) {
                return "";
            } else if (remain > 1 && remain <= 4) {
                return RUSSIAN_ENDING1;
            } else {
                return RUSSIAN_ENDING2;
            }
        }
    }
}
