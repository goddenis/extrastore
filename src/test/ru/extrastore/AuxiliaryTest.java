package ru.extrastore;

import org.jboss.remoting.InvalidConfigurationException;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Ксюха
 * Date: 20.02.11
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class AuxiliaryTest extends SeamTest {
    @Test
    public void testAssertionsEnabled() throws Exception {
        try {
            assert false;
            throw new Exception("Assertions are not enabled. Add '-enableassertions' to the JVM parameters.");
        } catch(AssertionError e) {
            // OK. Assertions enabled.
        }
    }
}
