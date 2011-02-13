package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Product;

import static org.testng.AssertJUnit.assertEquals;

/**
 * File profile
 * Creator: Catalyst
 * Date: 13.02.11
 * Time: 11:17
 */
public class ProductTest extends SeamTest {
    private static final String PRODUCT_NAME = "Тестовый Продукт";

    @Test
    public void testProduct() throws Exception {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        assertEquals("Product name", PRODUCT_NAME, product.getName());
    }
}
