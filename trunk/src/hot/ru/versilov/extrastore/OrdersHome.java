package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Events;
import org.jboss.seam.framework.EntityHome;
import ru.versilov.extrastore.model.Order;
import ru.versilov.extrastore.model.OrderLine;
import ru.versilov.extrastore.model.Product;

import javax.persistence.EntityManager;
import java.util.*;

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

    @Override
    public String update() {
        Order o = this.getInstance();
        Iterator<OrderLine> i = o.getOrderLines().iterator();
        while (i.hasNext()) {
            OrderLine line = i.next();
            if (line.getQuantity() == 0) {
                // Clean out empty lines
                i.remove();
            } else {
                line.setOrder(o);
            }
        }
        // Recalculate net amount etc. according to the new order lines.
        o.calculateTotals();

        Events.instance().raiseTransactionSuccessEvent("orderChanged", o.getOrderId());
        
        return super.update();
    }

    @Override
    public String remove() {
        Events.instance().raiseTransactionSuccessEvent("orderRemoved", this.getInstance().getOrderId());
        
        return super.remove();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Order getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    protected Product getProductByASIN(String asin) {
        EntityManager em = this.getEntityManager();
        Product product = (Product)em.createQuery("select p from Product p where p.ASIN = :asin ")
                .setParameter("asin", asin)
                .getSingleResult();
        return product;
    }

    public List<OrderLine> getOtherLines() {
        List<OrderLine> list = new ArrayList<OrderLine>();
        List<Product> otherProducts;

        if (this.getInstance().getOrderLines().size() > 0) {
            List<Long> prods = new ArrayList<Long>();

            for (OrderLine line: this.getInstance().getOrderLines()) {
                prods.add(line.getProduct().getProductId());
            }

            otherProducts = this.getEntityManager().createQuery("select p from Product p where p.productId not in(:prods)").setParameter("prods", prods).getResultList();

        } else {
            otherProducts = this.getEntityManager().createQuery("select p from Product p").getResultList();
        }
        

        for (Product p: otherProducts) {
            list.add(new OrderLine(p, 0));
        }
        return list;
    }

    public void setOtherLines(List<OrderLine> lines) {
        
    }

    public List<OrderLine> getTargetLines() {
        return this.getInstance().getOrderLines();
    }

    public void setTargetLines(List<OrderLine> orderLines) {
        for (OrderLine line: this.getInstance().getOrderLines()) {
            this.getEntityManager().remove(line);
        }
        this.getInstance().getOrderLines().clear();
        this.getEntityManager().merge(this.getInstance());
        this.getInstance().setOrderLines(orderLines);
    }

}
