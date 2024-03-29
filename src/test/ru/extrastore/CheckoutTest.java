package ru.extrastore;

import org.jboss.seam.core.Manager;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Order;
import ru.extrastore.model.Store;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertEquals;

/**
 * http://extrastore.ru
 * Created by Catalyst on 22.02.11 at 19:50
 */
public class CheckoutTest extends SeamTest {
    private static final String PRODUCT1_URL_ALIAS = "mus3";
    private static final String PRODUCT2_URL_ALIAS = "dispenser";


    @Test
    public void testOrderCreation() throws Exception {

        // Add first product
        new FacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("alias", PRODUCT1_URL_ALIAS);
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{cart.addProduct(product, 1)}");
                invokeAction("#{cart.addProduct(product, 1)}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertEquals("items count", 2, ((Long) getValue("#{cart.itemsCount}")).longValue());
                Cart cart = (Cart)getValue("#{cart}");
                assertNotNull(cart);
                assertEquals("product 1 alias", PRODUCT1_URL_ALIAS, cart.getOrder().getLines().get(0).getProduct().getUrlAlias());
            }

        }.run();

        // Add second product
        new FacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("alias", PRODUCT2_URL_ALIAS);
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{cart.addProduct(product, 1)}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertEquals("items count", 3, ((Long) getValue("#{cart.itemsCount}")).longValue());
                Cart cart = (Cart)getValue("#{cart}");
                assertNotNull(cart);
                assertEquals("product 2 alias", PRODUCT2_URL_ALIAS, cart.getOrder().getLines().get(1).getProduct().getUrlAlias());
            }

        }.run();




        String id = new FacesRequest("/cart.xhtml") {
            @Override
            protected void renderResponse() throws Exception {
                assertEquals("items count", 3, ((Long) getValue("#{cart.itemsCount}")).longValue());
                assert !Manager.instance().isLongRunningConversation();
            }

        }.run();

        id = new FacesRequest("/order/auth.xhtml", id) {
        }.run();


        id = new FacesRequest("/order/auth.xhtml", id) {

            @Override
            protected void applyRequestValues() throws Exception {
                setValue("#{credentials.username}", "bart@nightmail.ru");
                setValue("#{authenticator.authType}", 1);
            }

            @Override
            protected void invokeApplication() throws Exception {
                assertEquals("success", invokeAction("#{authenticator.authenticate()}"));
                assertNotNull(getValue("#{user}"));
                assertTrue(Manager.instance().isLongRunningConversation());
            }
        }.run();




        id = new FacesRequest("/order/delivery.xhtml", id) {

            @Override
            protected void invokeApplication() {
                assertEquals("success", invokeAction("#{checkout.createOrder()}"));
                Order currentOrder = (Order)getValue("#{currentOrder}");
                assertNotNull(currentOrder);
                assertNotNull(currentOrder.getCustomer());
                assertNotNull(currentOrder.getCustomer().getAddress());

                Store store = (Store)getValue("#{store}");

                assertEquals("delivery type", store.getDeliveryTypes().iterator().next(), currentOrder.getDeliveryType());
            }

            @Override
            protected void renderResponse() throws Exception {
                assert Manager.instance().isLongRunningConversation();


                Order currentOrder = (Order)getValue("#{currentOrder}");
                assertNotNull(currentOrder);
                assertNotNull(currentOrder.getCustomer());
                assertNotNull(currentOrder.getCustomer().getAddress());

                Store store = (Store)getValue("#{store}");

                assertEquals("delivery type", store.getDeliveryTypes().iterator().next(), currentOrder.getDeliveryType());
            }

            @Override
            protected void afterRequest() {
//               assert isInvokeApplicationComplete();
            }
        }.run();



        id = new FacesRequest("/order/delivery.xhtml", id) {
            @Override
            protected void applyRequestValues() throws Exception {
                Order currentOrder = (Order)getValue("#{currentOrder}");
                assertNotNull(currentOrder);
                assertNotNull(currentOrder.getCustomer());
                assertNotNull(currentOrder.getCustomer().getAddress());


                Store store = (Store)getValue("#{store}");

                assertEquals("delivery type", store.getDeliveryTypes().iterator().next(), currentOrder.getDeliveryType());

                setValue("#{currentOrder.customer.firstName}", "Пися");
                setValue("#{currentOrder.customer.firstName}", "Камушкин");
                setValue("#{currentOrder.customer.firstName}", "Петрович");

                setValue("#{currentOrder.customer.address.zip}", 443110);
                setValue("#{currentOrder.customer.address.region}", "Самара");
                setValue("#{currentOrder.customer.address.area}", null);
                setValue("#{currentOrder.customer.address.town}", "Самара");
                setValue("#{currentOrder.customer.address.address}", "ул. Волгина, д. 19, кв. 20");
            }

            @Override
            protected void invokeApplication() {
                assertEquals("success", invokeAction("#{checkout.submitDelivery()}"));
                assert Manager.instance().isLongRunningConversation();
            }

        }.run();

        id = new FacesRequest("/order/pay.xhtml", id) {
        }.run();

        id = new FacesRequest("/order/pay.xhtml", id) {
            @Override
            protected void applyRequestValues() throws Exception {
                Order currentOrder = (Order)getValue("#{currentOrder}");
                Store store = (Store)getValue("#{store}");
                assertEquals(store.getPaymentTypes().iterator().next(), currentOrder.getPaymentType());
            }

            @Override
            protected void invokeApplication() throws Exception {
                assertEquals("success", invokeAction("#{checkout.submitPay()}"));
            }
        }.run();



        id = new FacesRequest("/order/confirm.xhtml", id) {
        }.run();

        id = new FacesRequest("/order/confirm.xhtml", id) {
            @Override
            protected void invokeApplication() {
                assertEquals("success", invokeAction("#{checkout.confirm()}"));
                assert Manager.instance().isLongRunningConversation();
                assertNotNull(getValue("#{currentOrder}"));

                Cart cart = (Cart)getValue("#{cart}");
                assertNotNull(cart);
                assertTrue(cart.isEmpty());
            }

        }.run();


        new FacesRequest("/order/thanks.xhtml", id) {
            @Override
            protected void renderResponse() throws Exception {
                assertFalse(Manager.instance().isLongRunningConversation());
            }
        }.run();
    }
}
