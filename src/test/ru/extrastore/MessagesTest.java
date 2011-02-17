package ru.extrastore;

import org.jboss.seam.international.Messages;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

/**
 * File profile
 * Creator: Catalyst
 * Date: 17.02.11
 * Time: 12:24
 */
public class MessagesTest extends SeamTest {
    @Test
    public void testGetMessageFromBundle() throws Exception {
        new ComponentTest() {
            @Override
            protected void testComponents() throws Exception {
                assertTrue("message from bundle", "Search".equals(Messages.instance().get("searchButton")));
            }
        }.run();
    }
}
