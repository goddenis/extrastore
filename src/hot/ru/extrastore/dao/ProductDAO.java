package ru.extrastore.dao;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import ru.extrastore.model.Product;

import javax.persistence.EntityManager;

/**
 * http://extrastore.ru
 * Created by Catalyst on 27.02.11 at 13:37
 */
@Name("productDAO")
@AutoCreate
public class ProductDAO {
    @In("entityManager")
    EntityManager em;

    public Product findProductByAlias(String alias) {
        return (Product)em.createQuery("from Product p where p.urlAlias like :alias").setParameter("alias", alias).getSingleResult();
    }
}
