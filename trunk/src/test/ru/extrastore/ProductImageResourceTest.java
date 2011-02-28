package ru.extrastore;

import org.jboss.seam.mock.EnhancedMockHttpServletResponse;
import org.jboss.seam.mock.ResourceRequestEnvironment;
import org.jboss.seam.mock.ResourceRequestEnvironment.Method;
import org.jboss.seam.mock.ResourceRequestEnvironment.ResourceRequest;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * http://extrastore.ru
 * Created by Catalyst on 27.02.11 at 16:58
 */
public class ProductImageResourceTest extends SeamTest {

    static final String IMAGE_PATH = "/r/pi/noproduct-s.jpg";

    @Test
    public void testGetProductImage() throws Exception {
        new ResourceRequest(new ResourceRequestEnvironment(this), Method.GET, IMAGE_PATH) {
            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                assertEquals("image not found", 404, response.getStatus());
            }
        }.run();
    }
}
