package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.testng.AssertJUnit.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ксюха
 * Date: 13.02.11
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class CategoryTest extends SeamTest {

    @Test
    public void testRequiredAttributes() throws Exception {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                Category c = new Category();

                EntityManager em = (EntityManager)getValue("#{entityManager}");
                try {
                    em.joinTransaction(); // Only to remove runtime exception from log.
                    em.persist(c);
                    fail("Empty category persisted.");
                } catch (PersistenceException e){
                    // right
                }
            }

        }.run();
    }


    @Test
    public void testCategoryAliasAndQuery() throws Exception {
        new NonFacesRequest("/browse.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("cat", "office");
            }

            @Override
            protected void renderResponse() throws Exception {
                Category c = (Category)getValue("#{category}");
                assertNotNull("category by alias", c);
                assertEquals("category alias", "office", c.getId());
                assertTrue("has products", c.getProducts().size() > 0);

                EntityManager em = (EntityManager)getValue("#{entityManager}");
                Category c2 = em.find(Category.class, "office");
                assertTrue("categories are equal", c.equals(c2));
            }
        }.run();

    }
}
