package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 22.06.2010
 * Time: 18:12:23
 * To change this template use File | Settings | File Templates.
 */
@Name("productsHome")
public class ProductsHome extends EntityHome<Product> {

    public void setProductId(Long id) {
        setId(id);
    }

    public Long getProductId() {
        return (Long) getId();
    }

    @Override
    protected Product createInstance() {
        Product product = new Product();
        return product;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    @Override
    public String remove() {
        // Remove dependent objects before hand.
        if (getInstance().getInventory() != null) {
            getEntityManager().remove(getInstance().getInventory());
        }
        return super.remove();
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Product getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }



}

