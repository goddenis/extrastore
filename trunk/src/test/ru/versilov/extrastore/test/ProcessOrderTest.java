package ru.versilov.extrastore.test;

import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;
import org.testng.annotations.Test;
import ru.versilov.extrastore.ProcessOrderAction;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 23.11.2009
 * Time: 2:51:40
 * To change this template use File | Settings | File Templates.
 */
public class ProcessOrderTest extends SeamTest {
    private static LogProvider log = Logging.getLogProvider(ProcessOrderTest.class);

    @Test
    public void testProcessOrder() throws Exception {
        String id = new NonFacesRequest("/order/accept.xhtml") {        }.run();
        id = new FacesRequest("/order/accept.xhtml", id) {
            @Override
            protected void applyRequestValues() {
                setValue("#{processOrder.orderNum}", new Integer(777));
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{processOrder.accept}");
                assert isLongRunningConversation();
                System.out.println("Order num after accept == " + getValue("#{processOrder.orderNum}"));
            }
            
            @Override
            protected void afterRequest() {
                System.out.println("Order state in afterRequest() == " + getValue("#{processOrder.state}"));
            }

            
        }.run();


        id = new NonFacesRequest("/order/assemble.xhtml", id) {

        }.run();
        id = new FacesRequest("/order/assemble.xhtml", id) {
            @Override
            protected void applyRequestValues() {
                setValue("#{processOrder.orderWeight}", new Integer(999));
            }

            @Override
            protected void invokeApplication() throws Exception {
                assert getValue("#{processOrder.state}").equals(ProcessOrderAction.OrderState.ACCEPTED);
                System.out.println("Order state before assemble == " + getValue("#{processOrder.state}"));
                Object result = invokeAction("assemble");
                System.out.println("Order state after assemble == " + getValue("#{processOrder.state}"));
                System.out.println("Order num after assemble == " + getValue("#{processOrder.orderNum}"));
                System.out.println("Order weight after assemble == " + getValue("#{processOrder.orderWeight}"));
            }

            @Override
            protected void renderResponse() throws Exception {
                System.out.println("Order state in renderResponse() == " + getValue("#{processOrder.state}"));                
            }
        }.run();


        id = new NonFacesRequest("/order/export.xhtml", id) {        }.run();
        id = new FacesRequest("/order/export.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                assert getValue("#{processOrder.state}").equals(ProcessOrderAction.OrderState.ASSEMBLED);
                System.out.println("Order state before export == " + getValue("#{processOrder.state}"));
                Object result = invokeAction("export");
                System.out.println("Order state after export == " + getValue("#{processOrder.state}"));
                System.out.println("Order num after export == " + getValue("#{processOrder.orderNum}"));
                System.out.println("Order weight after export == " + getValue("#{processOrder.orderWeight}"));
            }

            // This is never called
            @Override
            protected void renderResponse() throws Exception {
                System.out.println("Order state in renderResponse() == " + getValue("#{processOrder.state}"));
            }

            @Override
            protected void afterRequest() {
                System.out.println("We are afterRequest()!!!!");
            }
        }.run();

        id = new NonFacesRequest("/order/send.xhtml", id) {        }.run();
        id = new FacesRequest("/order/send.xhtml", id) {
            @Override
            protected void invokeApplication() throws Exception {
                assert getValue("#{processOrder.state}").equals(ProcessOrderAction.OrderState.EXPORTED);
                System.out.println("Order state before send == " + getValue("#{processOrder.state}"));
                Object result = invokeAction("send");
                System.out.println("Order state after send == " + getValue("#{processOrder.state}"));
                System.out.println("Order weight after send == " + getValue("#{processOrder.orderWeight}"));
            }

            @Override
            protected void renderResponse() throws Exception {
                System.out.println("Order state in renderResponse() == " + getValue("#{processOrder.state}"));
            }
        }.run();


    }

}
