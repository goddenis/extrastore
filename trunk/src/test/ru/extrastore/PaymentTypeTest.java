package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Store;
import ru.extrastore.model.payment.PaymentType;
import ru.extrastore.model.payment.PaymentTypeRobokassa;

import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * http://extrastore.ru
 * Created by Catalyst on 06.03.11 at 20:04
 */
public class PaymentTypeTest extends SeamTest {

    @Test
    public void testPaymentTypes() throws Exception{
        new FacesRequest("/index.xhtml") {
            @Override
            protected void renderResponse(){
                // #{store} should be created at StoreDomainFilter on invoke application
                Store s = (Store)getValue("#{store}");

                assertNotNull("store from session context", s);

                assertEquals("IP store defaults to localost", "localhost", s.getDomain());

                assertTrue("store has payment types", s.getPaymentTypes().size() > 0);

                PaymentType pt = s.getPaymentType("robokassa");
                assertTrue(pt instanceof PaymentTypeRobokassa);

                Object pts = getValue("#{paymentTypes}");
                assertTrue(pts instanceof List);
                assertTrue(((List)pts).size() > 0);
                assertTrue(((List)pts).get(0) instanceof PaymentType);
            }
        }.run();
    }


}
