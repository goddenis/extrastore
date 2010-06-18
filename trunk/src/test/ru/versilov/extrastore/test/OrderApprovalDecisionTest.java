package ru.versilov.extrastore.test;

import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.contexts.Contexts;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import ru.versilov.extrastore.OrderApprovalDecision;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 15.10.2009
 * Time: 0:07:01
 * To change this template use File | Settings | File Templates.
 */
public class OrderApprovalDecisionTest extends SeamTest {
    @Test
    public void testDecision() throws Exception {

        new ComponentTest() {
            
            OrderApprovalDecision decision;

            @Override
            protected void testComponents() throws Exception {
                decision = (OrderApprovalDecision) getInstance("orderApproval");
                
                Contexts.getEventContext().set("amount", new BigDecimal(100));
                assert decision.getHowLargeIsOrder().equalsIgnoreCase("large order");

                Contexts.getEventContext().set("amount", new BigDecimal(20));
                assert decision.getHowLargeIsOrder().equalsIgnoreCase("small order");
            }
        }.run();
    }
}

