package ru.extrastore;

import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;
import ru.extrastore.model.Store;
import ru.extrastore.model.delivery.DeliveryType;

import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * http://extrastore.ru
 * Created by Catalyst on 07.03.11 at 4:58
 */
public class DeliveryTypeTest extends SeamTest {
    @Test
    public void testStoreDeliveryTypes() throws Exception {
        new FacesRequest("/index.xhtml") {
            @Override
            protected void renderResponse(){
                // #{store} should be created at StoreDomainFilter on invoke application
                Store s = (Store)getValue("#{store}");

                assertNotNull("store from session context", s);

                assertEquals("IP store defaults to localost", "localhost", s.getDomain());

                assertTrue("store has delivery types", s.getDeliveryTypes().size() > 0);

                Object pts = getValue("#{deliveryTypes}");
                assertTrue(pts instanceof List);
                assertTrue(((List)pts).size() > 0);
                assertTrue(((List)pts).get(0) instanceof DeliveryType);
            }
        }.run();
        
    }
}
