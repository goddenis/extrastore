package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Product;
import ru.extrastore.model.ProductProperty;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.testng.AssertJUnit.*;

/**
 * File profile
 * Creator: Catalyst
 * Date: 13.02.11
 * Time: 11:17
 */
public class ProductTest extends SeamTest {
    private static final String PRODUCT_NAME = "Тестовый Продукт";
    private static final Long PRODUCT_ID = 777999L;
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal(395L);
    private static final String PRODUCT_URL_ALIAS = "skovoroda";
    private static final String PRODUCT_ASIN = "skovoroda23";
    private static final String PRODUCT_IMAGE_URL = "http://extrastore.org/img/skovoroda_small.jpg";

    @Test
    public void testProductAttributes() throws Exception {
        Product product = new Product();

        product.setId(PRODUCT_ID);
        assertEquals("Product id", PRODUCT_ID, product.getId());

        product.setASIN(PRODUCT_ASIN);
        assertEquals("Product ASIN", PRODUCT_ASIN, product.getASIN());

        product.setName(PRODUCT_NAME);
        assertEquals("Product name", PRODUCT_NAME, product.getName());

        product.setPrice(PRODUCT_PRICE);
        assertEquals("Product price", PRODUCT_PRICE, product.getPrice());

        product.setUrlAlias(PRODUCT_URL_ALIAS);
        assertEquals("Product URL alias", PRODUCT_URL_ALIAS, product.getUrlAlias());

        product.setUrlImageSmall(PRODUCT_IMAGE_URL);
        assertEquals("Product URL of small image", PRODUCT_IMAGE_URL , product.getUrlImageSmall());
    }


    @Test
    public void testRequiredAttributes()
        throws Exception
    {
        new ComponentTest() {

            @Override
            protected void testComponents()
                throws Exception
            {
                Product p = new Product();

                EntityManager em = (EntityManager) getValue("#{entityManager}");
                try {
                    em.joinTransaction();  // This line is just to avoid irritating 'Unable to mark for rollback on PersistenceException'
                    em.persist(p);
                    fail("empty product persisted");
                } catch (PersistenceException e) {
                    // good
                }
            }
        }.run();
    }

    @Test
    public void testCreateDelete() throws Exception {
        final Product p = new Product();
        p.setName(PRODUCT_NAME);

        p.setProperties(new ArrayList<ProductProperty>());
        ProductProperty pp = new ProductProperty();
        pp.setName("Size");
        pp.setValue("XXL");
        pp.setProduct(p);
        p.getProperties().add(pp);

        new FacesRequest() {
            protected void invokeApplication() {
                EntityManager em = (EntityManager) getValue("#{entityManager}");
                em.persist(p);
            }

        }.run();

        new FacesRequest() {
            protected void invokeApplication() {
                EntityManager em = (EntityManager) getValue("#{entityManager}");
                Product found = em.find(Product.class, p.getId());
                assertNotNull("find by id", found);
                assertEquals("id", p.getId(), found.getId());
                assertEquals("name", PRODUCT_NAME, found.getName());

                assertNotNull("product properties", found.getProperties());
                assertEquals("product properties size", 1, found.getProperties().size());
                assertTrue("property name", "Size".equals(found.getProperties().get(0).getName()));
                assertTrue("property value", "XXL".equals(found.getProperties().get(0).getValue()));

                em.remove(found);
            }
        }.run();

        new FacesRequest() {
            protected void invokeApplication() {
                EntityManager em = (EntityManager)getValue("#{entityManager}");
                Product found = em.find(Product.class, p.getId());

                assertNull("deleted product", found);
            }

        }.run();

    }

    @Test
    public void testProductQuery() throws Exception {
        new NonFacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("alias", "wifishirt");
                setParameter("cat", "office");
            }

            @Override
            protected void renderResponse() {
                String alias = (String)getValue("#{alias}");
                assertEquals("alias param", "wifishirt", alias);

                Product p = (Product)getValue("#{product}");
                assertNotNull("product from xml entity query", p);
                assertEquals("product url alias","wifishirt", p.getUrlAlias());
            }

        }.run();

    }

}
