package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * http://extrastore.ru
 * Created by Catalyst on 01.03.11 at 2:04
 */
public class ShowcaseTest extends SeamTest {
    @Test
    public void testProductsOutjection() throws Exception {
        new NonFacesRequest("/index.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                List products = (List)getValue("#{products}");
                assertNotNull(products);
                assertTrue(products.size() > 0);
            }
        }.run ();
    }

    @Test
    public void testCategoriesOutjection() throws Exception {
        new NonFacesRequest("/index.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                Set allCategories = (Set)getValue("#{allCategories}");
                assertNotNull(allCategories);
                assertTrue(allCategories.size() > 0);
            }
        }.run ();
    }
}
