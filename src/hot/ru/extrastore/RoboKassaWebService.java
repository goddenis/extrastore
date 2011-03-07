package ru.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import ru.extrastore.model.Store;
import ru.extrastore.model.payment.PaymentTypeRobokassa;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Catalyst on 06.03.11 at 4:23
 */
@Name("robokassa")
@Path("/robokassa")
public class RoboKassaWebService implements Serializable {

    @In
    Store store;

    @Logger
    Log log;

    // These are query parameters, passed to vars in pages.xml
    @In(value = "InvId", required = false)
    String InvId;

    @In(value = "OutSum", required = false)
    String OutSum;

    @In(value = "SignatureValue", required = false)
    String SignatureValue;

    @GET
    @Path("/result")
    @Produces("text/plain")
    public String result(@QueryParam("OutSum") long outSum, @QueryParam("InvId") long orderId, @QueryParam("SignatureValue") String crc) {
        log.info("ROBOKASSA: /robokassa/result?OutSum=#0&InvId=#1&SignatureValue=#2", outSum,  orderId, crc);

        String myCRC = ((PaymentTypeRobokassa)store.getPaymentType("robokassa")).getResultCRC(outSum, orderId).toUpperCase();

        if (crc.equalsIgnoreCase(myCRC)) {
            // Mark order as paid
            return "OK"+orderId;
        }

        return null;
    }

    public boolean isSuccess() {
        PaymentTypeRobokassa pt = (PaymentTypeRobokassa)store.getPaymentType("robokassa");
        assert pt != null;

        long orderId = Long.valueOf(InvId);
        long outSum = Long.valueOf(OutSum);

        String myCRC = pt.getSuccessCRC(outSum, orderId);

        return SignatureValue.equalsIgnoreCase(myCRC);
    }

}
