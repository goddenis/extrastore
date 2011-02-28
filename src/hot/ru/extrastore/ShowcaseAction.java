package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import ru.extrastore.model.Category;
import ru.extrastore.model.Product;
import ru.extrastore.model.Store;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * http://extrastore.ru
 * Created by Catalyst on 01.03.11 at 1:11
 */
@Name("showcase")
@Scope(ScopeType.CONVERSATION)
public class ShowcaseAction {

    @In("entityManager")
    EntityManager em;

    @In
    Store store;

    @In
    Category category;

    @Out(required = false)
    List<Product> products;

    @Out(required = false)
    Set<Category> allCategories;

    @Factory("products")
    public void loadAllProducts() {
        store = em.merge(store);

        if (category.getId() == null) {
            products = store.getProducts();
        } else {
            products = new ArrayList<Product>(category.getProducts());
            products.retainAll(store.getProducts());
        }
    }

    @Factory("allCategories")
    public void loadAllCategories() {
        store = em.merge(store);

        this.allCategories = new HashSet<Category>();
        for (Product p: store.getProducts()) {
            allCategories.addAll(p.getCategories());
        }
    }
}
