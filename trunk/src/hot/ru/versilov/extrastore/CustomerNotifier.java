package ru.versilov.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;
import ru.versilov.extrastore.model.Order;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 19.07.2010
 * Time: 21:40:36
 * To change this template use File | Settings | File Templates.
 */

@Name("customerNotifier")
public class CustomerNotifier {
    @Logger
    private Log log;

    @In
    Renderer renderer;

    @Observer("orderStatusChanged")
    public void onOrderStatusChanged(Order order) {
        Contexts.getEventContext().set("order", order);
        switch (order.getStatus()) {
            case PROCESSING:
                sendEmailNotification("/email/order_accepted.xhtml");
                break;

            case SHIPPED:
                sendEmailNotification("/email/order_shipped.xhtml");
                break;
        }
    }

    protected void sendEmailNotification(String view) {
        try {
            renderer.render(view);
        } catch (Exception e) {
            log.error("Error sending email: " + view, e);
        }
    }

}
