/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;

import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Stateful
@Name("cart")
@Scope(SESSION)
public class ShoppingCartBean
    implements ShoppingCart,
               Serializable
{
    static final long serialVersionUID = 8722576722482084467L;

    @PersistenceContext(type=PersistenceContextType.EXTENDED)
    EntityManager em;
    
    Order cartOrder = new Order();
    Map<Product,Boolean> cartSelection  = new HashMap<Product,Boolean>();

    public List<OrderLine> getCart() {
        return cartOrder.getOrderLines();
    }
    public boolean getIsEmpty() {
        return cartOrder.isEmpty();
    }

    public void addProduct(Product product, int quantity) {
        cartOrder.addProduct(product,quantity);
        cartOrder.calculateTotals();
    }

    @SuppressWarnings("unchecked")
    public Map getCartSelection() {
        return cartSelection;
    }

    public BigDecimal getSubtotal() {
        return cartOrder.getNetAmount();
    }

    public BigDecimal getTax() {
        return cartOrder.getTax();
    }

    public BigDecimal getTotal() {
        return cartOrder.getTotalAmount();
    }

    public int getItemsQuantity() {
        return cartOrder.getItemsQuantity();
    }

    /**
     *
     * @return russian ending for the word 'товар', depending on the basket goods quantity.
     */
    public String getEnding() {
        if (this.getItemsQuantity() == 1)
            return "";
        else if (this.getItemsQuantity() < 5)
            return "а";
        else if (this.getItemsQuantity() < 21)
            return "ов";
        else if (this.getItemsQuantity() % 10 == 1)
            return "";
        else if (this.getItemsQuantity() % 10 == 0)
            return "ов";
        else if (this.getItemsQuantity() % 10 < 5)
            return "а";
        else
            return "ов";
    }

    public void updateCart() {
        List<OrderLine> newLines =  new ArrayList<OrderLine>();

        for (OrderLine line: cartOrder.getOrderLines()) {
            if (line.getQuantity() > 0) {
                Boolean selected = cartSelection.get(line);
                if (selected==null || !selected) {
                    newLines.add(line);
                }
            }
        }        
        cartOrder.setOrderLines(newLines);
        cartOrder.calculateTotals();

        cartSelection = new HashMap<Product,Boolean>();
    }

    public void resetCart() {
        cartOrder = new Order();
    }

    @Remove
    public void destroy() {}

}
