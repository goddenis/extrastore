package ru.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;
import ru.extrastore.model.Order;
import ru.extrastore.model.Store;

import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Ксюха on 22.02.11 at 21:39
 */
@Name("newOrderNotifier")
public class NewOrderNotifier implements Serializable {
    @Logger
    Log log;

    @In
    Renderer renderer;

    @Observer("newOrder")
    public void onNewOrder(Order newOrder, Store store) {
        log.info("#0.onNewOrder()", this.hashCode());
        Contexts.getEventContext().set("currentOrder", newOrder);
        Contexts.getEventContext().set("store", store);
        try {
            renderer.render("/order/email_notification.xhtml");
        } catch (Exception e) {
            log.error("Error sending notification email.", e);
        }
    }
}
