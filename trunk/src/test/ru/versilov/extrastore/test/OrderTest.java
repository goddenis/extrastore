package ru.versilov.extrastore.test;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.el.ELException;

import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.security.NotLoggedInException;
import org.jboss.seam.log.Logging;
import org.jboss.seam.log.LogProvider;
import org.testng.annotations.Test;

import ru.versilov.extrastore.*;
import ru.versilov.extrastore.model.Order.Status;
import ru.versilov.extrastore.model.Order;
import ru.versilov.extrastore.model.Product;
import ru.versilov.extrastore.model.User;

public class OrderTest
    extends SeamTest
{

    private static LogProvider log = Logging.getLogProvider(OrderTest.class);

    @Test 
    public void selectDvd() 
        throws Exception 
    {
        new FacesRequest("/product.xhtml") {
            @Override
            protected void  beforeRequest() {
                setParameter("id", "41");
            }

            @Override
            protected void renderResponse() throws Exception {
                Product dvd = (Product) getValue("#{product}");
                assert dvd != null;
                assert dvd.getProductId() == 41;                               
            }
        }.run();
    }
        
    @Test
    public void addToCart() 
        throws Exception 
    {
        String id = new FacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("id", "41");
            }
        }.run();
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{search.addToCart}");
            }
            
            @Override
            protected void renderResponse() throws Exception {
                ShoppingCart cart = (ShoppingCart) getValue("#{cart}");
                assert cart != null;
                assert cart.getCart().size() == 1;
            }
        }.run();
        
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void beforeRequest() {
                setParameter("id", "42");
            }          
        }.run();
        
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{search.addToCart}");
            }
            
            @Override
            protected void renderResponse() throws Exception {
                ShoppingCart cart = (ShoppingCart) getValue("#{cart}");
                assert cart != null;
                assert cart.getCart().size() == 2;
            }
        }.run();
        
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void beforeRequest() {
                setParameter("id", "41");
            }         
        }.run();
        
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{search.addToCart}");
            }
            
            @Override
            protected void renderResponse() throws Exception {
                ShoppingCart cart = (ShoppingCart) getValue("#{cart}");
                assert cart == null;
                assert cart.getCart().size() == 2;
            }
        }.run();
        
        
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void beforeRequest() {
                setParameter("id", "43");
            }           
        }.run();
        
        new FacesRequest("/product.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{search.addToCart}");
            }
            
            @Override
            protected void renderResponse() throws Exception {
                ShoppingCart cart = (ShoppingCart) getValue("#{cart}");
                assert cart != null;
                assert cart.getCart().size() == 3;
            }
        }.run();
        
    }
    
//    @Test
    public void checkoutNotLoggedIn() throws Exception {
        String id = new FacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("id", "41");
            }
        }.run();
        
        id = new FacesRequest("/product.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{search.addToCart}");
            }
        }.run();
        
        id = new FacesRequest("/checkout.xhtml", id) {
        }.run();
             
        id = new FacesRequest("/checkout.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{checkout.createOrder}");
            }
            @Override
            protected void renderResponse() throws Exception {
                Order order = (Order) getValue("#{currentOrder}");
                assert order != null;
                
            }
        }.run();    
        
        id = new FacesRequest("/checkout.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                try {
                    invokeAction("#{checkout.submitOrder}");
                    assert false; // should fail
                } catch (ELException e) {
                    assert e.getCause() instanceof NotLoggedInException;
                }
            }
            @Override
            protected void renderResponse() throws Exception {
                Order order = (Order) getValue("#{order}");
                assert order != null;
            }
        }.run();
        
        new FacesRequest("/checkout.xhtml", id) {
            @Override
            protected void applyRequestValues() throws Exception {
               setValue("#{identity.username}", "user1");
               setValue("#{identity.password}", "password");
            }
            protected void invokeApplication() throws Exception {
                invokeAction("#{identity.login}");
            }
            @Override
            protected void renderResponse() throws Exception {
                assert getValue("#{identity.loggedIn}").equals(Boolean.TRUE);
                User currentUser = (User) getValue("#{currentUser}");
                assert currentUser.getEmail().equals("user1");
            }
        }.run();
        
    }
    
    public long makeOrder() throws Exception {
        String id = new FacesRequest("/product.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("id", "41");
            }
        }.run();

        id = new FacesRequest("/product.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{search.addToCart}");
            }
        }.run();
        
        id = new NonFacesRequest("/checkout.xhtml", id) {
        }.run();
        
//        id = new FacesRequest("/checkout.xhtml", id) {
//            @Override
//            protected void applyRequestValues() throws Exception {
//                setValue("#{identity.username}", "h.potter@hogwarts.edu");
//               setValue("#{identity.password}", "password");
//            }
//            protected void invokeApplication() throws Exception {
//                invokeAction("#{identity.login}");
//            }
//            @Override
//            protected void renderResponse() throws Exception {
//                assert getValue("#{identity.loggedIn}").equals(Boolean.TRUE);
//                User currentUser = (User) getValue("#{currentUser}");
//                assert currentUser.getEmail().equals("h.potter@hogwarts.edu");
//            }
//        }.run();

        id = new FacesRequest("/checkout.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {             
                invokeAction("#{checkout.createOrder}");
                Order order = (Order) getValue("#{currentOrder}");
                assert order!=null;
            }         
            @Override
            protected void renderResponse()   {
                log.info("checkout.xhtml.renderResponse()");
                assert Manager.instance().isLongRunningConversation();
            }
        }.run();


        // The auth page will ruin the conv. id in showOrders() test
        // Because it is never shown for user already logged in, according to pageflow checkout.jpdl.xml
        // But don't know how tho check this out of FacesRequest. So, just save the conv. id
        // for the case, when it is destroyed to null.
        String prevId = id;

        id = new NonFacesRequest("/auth.xhtml", id) {
        }.run();

        id = new FacesRequest("/auth.xhtml", id) {
            @Override
            protected void applyRequestValues() throws Exception {
               setValue("#{checkout.authType}", CheckoutAction.AUTH_TYPE_USER);
               setValue("#{identity.username}", "h.potter@hogwarts.edu");
               setValue("#{identity.password}", "password");
            }

            @Override
            protected void invokeApplication() throws Exception {
                if (!getValue("#{identity.loggedIn}").equals(Boolean.TRUE)) {
                    invokeAction("#{checkout.auth}");
                    assert Manager.instance().isLongRunningConversation();
              }
            }
            
            @Override
            protected void renderResponse() throws Exception {
                assert getValue("#{identity.loggedIn}").equals(Boolean.TRUE);
                User currentUser = (User) getValue("#{currentUser}");
                assert currentUser.getEmail().equals("h.potter@hogwarts.edu");
            }
        }.run();


        // Restore conv. id if it was destroyed by an unappropriate auth.xhtml page call.
        if (id == null) {
            id = prevId;
        }


        id = new NonFacesRequest("/address.xhtml", id) {
        }.run();

        id = new FacesRequest("/address.xhtml", id) {
            @Override
            protected void applyRequestValues() throws Exception {
               setValue("#{currentOrder.customer.address1}", "Malholland drive, 35-40");
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("address");
                assert Manager.instance().isLongRunningConversation();
            }

            // !!! WARNING !!! renderResponse() is not called here
            // Possibly, because of redirect in checkout.jpdl
            // Neither it is called in other page tests
            @Override
            protected void renderResponse()   {
                log.info("address.xhtml.renderResponse()");
            }
        }.run();


// Pay step is not ready yet.        
//        id = new NonFacesRequest("/pay.xhtml", id) { }.run();
//        id = new FacesRequest("/pay.xhtml", id) {
//            @Override
//            protected void invokeApplication() {
//                invokeAction("pay");
//            }
//
//        }.run();


        id = new NonFacesRequest("/confirm.xhtml", id) {
        }.run();

        final Wrapper<Long> orderId = new Wrapper<Long>();
        
        new FacesRequest("/confirm.xhtml", id) {
            @Override
            protected void applyRequestValues() throws Exception {
                setValue("#{currentOrder.customer.address2}", "Address 2");
            }
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("purchase");
            }
            @Override
            protected void renderResponse() throws Exception {
                Order order = (Order) getValue("#{completedOrder}");
                assert order!=null;
                assert order.getCustomer().getEmail().equals("h.potter@hogwarts.edu");
                assert order.getStatus().equals(Status.OPEN);

                orderId.setValue(order.getOrderId());
            }
        }.run();
        
        return orderId.getValue();
    }


    @Test
    public void checkout() throws Exception {
        makeOrder();
    }



    @Test
    public void showOrders() throws Exception {
        final long order1 = makeOrder();
        final long order2 = makeOrder();
        final long order3 = makeOrder();
        
        new NonFacesRequest("/showOrders.xhtml") {
            @SuppressWarnings("unchecked")
            @Override
            protected void renderResponse() throws Exception {
                DataModel model = (DataModel) getValue("#{orders}");

                List<Long> orders = new ArrayList<Long>();
                for (Order order: (List<Order>) model.getWrappedData()) {
                    orders.add(order.getOrderId());
                }

                assert orders.contains(order1);
                assert orders.contains(order2);
                assert orders.contains(order3);
            }
            
        }.run();        
    }
    
// Commented out till ordering with authorization will work    
//    @Test
    public void cancelOrder() throws Exception {
        final long order1 = makeOrder();      
        
        String id = new NonFacesRequest("/showorders.xhtml") {
            @SuppressWarnings("unchecked")
            @Override
            protected void renderResponse() throws Exception {
                DataModel model = (DataModel) getValue("#{orders}");
                assert model!=null;
                
                assert Conversation.instance().isLongRunning();
            }
            
        }.run();  
        
        id = new FacesRequest("/showorders.xhtml",id) {
            @SuppressWarnings("unchecked")
            @Override
            protected void applyRequestValues() throws Exception {
               DataModel model = (DataModel) getValue("#{orders}");
            
               int index =0;
               for (Order order: (List<Order>) model.getWrappedData()) {
                   if (order.getOrderId() == order1) {
                       model.setRowIndex(index);
                       break;
                   }
                   index++;
               }
            }
         
            @Override
            protected void invokeApplication() throws Exception {
               invokeAction("#{showorders.detailOrder}");
            }
            
            @Override
            protected void renderResponse() throws Exception {
                assert false;
            }
        }.run();
        
        id = new FacesRequest("/showorders.xhtml",id) {           
            @Override
            protected void renderResponse() throws Exception {
                Order order = (Order) getValue("#{myorder}");                
                assert order.getOrderId() == order1;
                assert order.getStatus() == Status.OPEN;

            }
        }.run();
        
        id = new FacesRequest("/showorders.xhtml",id) {  
            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{showorders.cancelOrder}");
            }
            @Override
            protected void renderResponse() throws Exception {
                Order order = (Order) getValue("#{myorder}");                
                assert order.getOrderId() == order1;
                assert order.getStatus() == Status.CANCELLED;
                assert false;
            }
        }.run();
    }
    
    
    static class Wrapper<T> {
        T value;
        
        public void setValue(T value) {
            this.value = value;
        }
        
        public T getValue() {
            return value;
        }
    }
}
