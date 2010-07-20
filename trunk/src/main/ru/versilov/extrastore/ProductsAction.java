package ru.versilov.extrastore;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import ru.versilov.extrastore.model.Product;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 17.06.2010
 * Time: 0:10:12
 * To change this template use File | Settings | File Templates.
 */

@Stateful
@Name("showproducts")
public class ProductsAction implements Products, Serializable {
    private static final long serialVersionUID = 6262804276456538412L;
    
    @PersistenceContext
    EntityManager em;


    @DataModel
    List<Product> products;
    

    @DataModelSelection
    Product product;

    @Factory("products")
    public String findProducts() {
        products = em.createQuery("select p from Product p")
            .getResultList();

        product = null;

        return "products";
    }

    public String deleteProduct() {
        if (product != null) {
            // TODO: remove image files
            if (product.getInventory() != null) {
                em.remove(product.getInventory());
            }
            em.remove(product);
        }

        return findProducts();
    }
    
    @Remove
    public void remove() {

    }
}
