package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Category;
import ru.extrastore.model.Product;
import ru.extrastore.model.Store;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;

import static org.testng.AssertJUnit.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ксюха
 * Date: 15.02.11
 * Time: 2:18
 * To change this template use File | Settings | File Templates.
 */
public class StoreTest extends SeamTest {
    @Test
    public void testStoreCategories() throws Exception {
        Category c1 = new Category();
        c1.setId("a");
        c1.setName("CatA");
        Category c2 = new Category();
        c2.setId("b");
        c2.setName("CatB");

        Product p = new Product();
        p.setCategories(new HashSet<Category>());
        p.getCategories().add(c1);
        p.getCategories().add(c2);

        Store s = new Store();
        s.setProducts(new ArrayList<Product>());
        s.getProducts().add(p);

        assertTrue("store contains product categories", s.getCategories().containsAll(p.getCategories()));
    }


    @Test
    public void testStoreCreation() throws Exception {
        new FacesRequest("/index.xhtml") {
            @Override
            protected void renderResponse(){
                // #{store} should be created at StoreDomainFilter on invoke application
                Store s = (Store)getValue("#{store}");

                assertEquals("IP store defaults to localost", "localhost", s.getDomain());

                assertNotNull("store from session context", s);

                s = ((EntityManager)getInstance("entityManager")).merge(s);
                assertTrue("store has categories", s.getCategories().size() > 0);

                assertNotNull("store has a parent Company", s.getCompany());
                assertEquals("store company is Extra", "Extra", s.getCompany().getName());

                assertTrue("store is in company's stores list", s.getCompany().getStores().contains(s));
            }
        }.run();
    }

    @Test
    public void testDefaultTemplatePath() {
        Store s = new Store();

        assertEquals("store default template path", Store.DEFAULT_TEMPLATE_PATH, s.getTemplatePath());
    }
}
