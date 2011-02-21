package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Product;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: wilddalex
 * Date: 19.02.11
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */

public class CartTest extends SeamTest {

    private static final String POURAGE_NAME = "Каша";
    private static final long POURAGE_PRICE = 115;
    private static final long POURAGE_QUANTITY = 3;

    private static final String BREADLETS_NAME = "Хлебцы";
    private static final long BREADLETS_PRICE = 35;
    private static final long BREADLETS_QUANTITY = 2;

    private static final String PRODUCT_NAME = "orphograph";


    @Test
    public void testCart() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName(POURAGE_NAME);
        p1.setPrice(POURAGE_PRICE);

        Cart cart = new Cart();
        cart.addProduct(p1, POURAGE_QUANTITY);

        assertEquals("items count", POURAGE_QUANTITY, cart.getItemsCount());
        assertEquals("total price", POURAGE_QUANTITY * POURAGE_PRICE, cart.getTotalCost());
        assertEquals("product count", 1, cart.getProductsCount());

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName(BREADLETS_NAME);
        p2.setPrice(BREADLETS_PRICE);

        cart.addProduct(p2, BREADLETS_QUANTITY);

        assertEquals("items count", POURAGE_QUANTITY + BREADLETS_QUANTITY, cart.getItemsCount());
        assertEquals("total price", POURAGE_QUANTITY * POURAGE_PRICE + BREADLETS_QUANTITY * BREADLETS_PRICE, cart.getTotalCost());
        assertEquals("product count", 2, cart.getProductsCount());

        assertEquals("product one", cart.getOrder().getLines().get(0).getProduct(), p1);
        assertEquals("product two", cart.getOrder().getLines().get(1).getProduct(), p2);

        assertEquals("product one quantity", cart.getOrder().getLines().get(0).getQuantity(), POURAGE_QUANTITY);
        assertEquals("product two quantity", cart.getOrder().getLines().get(1).getQuantity(), BREADLETS_QUANTITY);





        cart.getSelection().put(p1, false);
        cart.update();

        assertEquals("product count", 1, cart.getProductsCount());
        assertEquals("product 2 remained", p2, cart.getOrder().getLines().get(0).getProduct());




        cart.reset();

        assertEquals("items count", 0, cart.getItemsCount());
        assertEquals("total price", 0, cart.getTotalCost());
        assertEquals("product count", 0, cart.getProductsCount());

    }

    @Test
    public void testOrderToCart() throws Exception {
        new NonFacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("alias", PRODUCT_NAME);
            }

            @Override
            protected void renderResponse() throws Exception {
                Product p = (Product) getValue("#{product}");
                assertEquals("product alias", PRODUCT_NAME, p.getUrlAlias());
            }


        }.run();

        new FacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("alias", PRODUCT_NAME);
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{cart.addProduct(product, 3)}");
                invokeAction("#{cart.addProduct(product, 1)}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertEquals("items count", 4, ((Long) getValue("#{cart.itemsCount}")).longValue());
            }

        }.run();

        new FacesRequest("/cart.xhtml") {

            @Override
            protected void renderResponse() throws Exception {
                assertEquals("items count", 4, ((Long) getValue("#{cart.itemsCount}")).longValue());
            }

        }.run();

        new FacesRequest("/cart.xhtml") {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{cart.reset()}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertEquals("items count", 0, ((Long) getValue("#{cart.itemsCount}")).longValue());
            }

        }.run();
    }

    @Test
    public void testRussianEnding() throws Exception {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                Cart c = (Cart) this.getValue("#{cart}");

                assertEquals("ов", c.russianEnding(0));
                assertEquals("", c.russianEnding(1));
                assertEquals("а", c.russianEnding(2));
                assertEquals("а", c.russianEnding(3));
                assertEquals("а", c.russianEnding(4));
                assertEquals("ов", c.russianEnding(5));
                assertEquals("ов", c.russianEnding(6));
                assertEquals("ов", c.russianEnding(7));
                assertEquals("ов", c.russianEnding(8));
                assertEquals("ов", c.russianEnding(9));
                assertEquals("ов", c.russianEnding(10));
                assertEquals("ов", c.russianEnding(11));
                assertEquals("ов", c.russianEnding(12));
                assertEquals("ов", c.russianEnding(13));
                assertEquals("ов", c.russianEnding(14));
                assertEquals("ов", c.russianEnding(15));
                assertEquals("ов", c.russianEnding(16));
                assertEquals("ов", c.russianEnding(17));
                assertEquals("ов", c.russianEnding(18));
                assertEquals("ов", c.russianEnding(19));
                assertEquals("ов", c.russianEnding(20));
                assertEquals("", c.russianEnding(21));
                assertEquals("а", c.russianEnding(22));
                assertEquals("а", c.russianEnding(23));
                assertEquals("а", c.russianEnding(24));
                assertEquals("ов", c.russianEnding(25));

                assertEquals("ов", c.russianEnding(100));
                assertEquals("", c.russianEnding(101));
                assertEquals("а", c.russianEnding(102));
                assertEquals("а", c.russianEnding(103));
                assertEquals("а", c.russianEnding(104));
                assertEquals("ов", c.russianEnding(105));
            }
        }.run();
    }

}
