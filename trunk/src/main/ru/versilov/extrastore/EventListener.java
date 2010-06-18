package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.log.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 27.10.2009
 * Time: 1:09:44
 * To change this template use File | Settings | File Templates.
 */
@Name("eventListener")
public class EventListener {

    @Logger
    private Log log;

    @Observer("submitAddress")
    public void onSubmitAddress(String address1) {
        log.info("Address was submitted: " + address1);
    }
}
