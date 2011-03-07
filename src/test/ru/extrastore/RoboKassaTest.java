package ru.extrastore;

import org.jboss.seam.mock.EnhancedMockHttpServletRequest;
import org.jboss.seam.mock.EnhancedMockHttpServletResponse;
import org.jboss.seam.mock.ResourceRequestEnvironment;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.jboss.seam.mock.ResourceRequestEnvironment.Method;
import static org.jboss.seam.mock.ResourceRequestEnvironment.ResourceRequest;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * http://extrastore.ru
 * Created by Catalyst on 07.03.11 at 5:00
 */
public class RoboKassaTest extends SeamTest {
    ResourceRequestEnvironment requestEnv;

    @BeforeClass
    public void prepareEnv() throws Exception
    {
       requestEnv = new ResourceRequestEnvironment(this)
       {
          @Override
          public Map<String, Object> getDefaultHeaders()
          {
             return new HashMap<String, Object>()
             {{
                   put("Accept", "text/plain");
                }};
          }

           @Override
           public String getServletPath() {
               return "/r";
           }
       };
    }

    @Test
    public void testRoboKassa() throws Exception {

        // /robokassa/result?OutSum=620&InvId=63&SignatureValue=FCBD3E2DFC63FA8A423B60656742AD9C
        new ResourceRequest(requestEnv, Method.GET, "/rest/robokassa/result") {
            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                request.setQueryString("OutSum=620");
                request.addQueryParameter("InvId", "63");
                request.addQueryParameter("SignatureValue", "FCBD3E2DFC63FA8A423B60656742AD9C");
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                assertEquals(200, response.getStatus());
                assertEquals("OK63", response.getContentAsString());
            }
        }.run();



        new NonFacesRequest("/pay/robokassa/success.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("InvId", "63");
                setParameter("OutSum", "620");
                setParameter("SignatureValue", "b9dd5609176486153432972b773628f6");
                setParameter("Culture", "ru");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertTrue(((RoboKassaWebService)getValue("#{robokassa}")).checkResponse());
            }
        }.run();



        new NonFacesRequest("/pay/robokassa/fail.xhtml") {
            @Override
            protected void beforeRequest() {
                setParameter("InvId", "63");
                setParameter("OutSum", "620");
                setParameter("SignatureValue", "b9dd5609176486153432972b773628f6");
                setParameter("Culture", "ru");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertTrue(((RoboKassaWebService)getValue("#{robokassa}")).checkResponse());
            }
        }.run();
    }
}
