package ru.versilov.extrastore;

import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.web.RequestParameter;
import ru.versilov.extrastore.model.Product;


import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.ejb.Stateless;
import java.util.List;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 05.12.2009
 * Time: 0:40:38
 * To change this template use File | Settings | File Templates.
 */


@Stateless
@Name("browse")
public class BrowseAction implements Browse, Serializable {

    @RequestParameter("cat")
    Integer categoryId;

    @RequestParameter("name")
    String categoryName;

    @PersistenceContext
    EntityManager entityManager;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Product> getProducts() {

        // If no category specified — show the first n products
        if (this.categoryId == null && this.categoryName == null) {
            return (List<Product>)entityManager.createQuery("SELECT p FROM Product p")
                 .setMaxResults(27)
                .getResultList();

        } else {
            // Category was specified through an alias like http://server.com/browse/cartoons
            if (this.categoryName != null) {
                return (List<Product>)entityManager.createQuery("SELECT p FROM Product p, IN(p.categories) c WHERE c.alias = :catName")
                 .setParameter("catName", this.categoryName)
                .getResultList();
            }

            // Categroy was specified through and id, like http://server.com/browse/63 
            return (List<Product>)entityManager.createQuery("SELECT p FROM Product p, IN(p.categories) c WHERE c.id = :catId")
             .setParameter("catId", this.categoryId)
            .getResultList();
        }
    }
}
