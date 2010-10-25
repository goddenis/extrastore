package ru.versilov.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;
import ru.versilov.extrastore.model.Batch;
import ru.versilov.extrastore.model.Order;
import ru.versilov.extrastore.model.User;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 21.07.2010
 * Time: 0:26:30
 * To change this template use File | Settings | File Templates.
 */
@Name("batchesList")
public class BatchesList extends EntityList<Batch> {

    @Override
    protected long getItemId(Batch batch) {
        return batch.getId();
    }

    @Override
    public String getEJBQLQuery() {
        return "select b from Batch b";
    }

    @Override
    public void removeSelection() {
        takeSelection();
        // Unlink orders before removal,
        // so they will not be batched at all
        for (Batch b:getSelectedEntities()) {
            for (Order o: b.getOrders()) {
                o.setBatch(null);
                getEntityManager().merge(o);
            }
        }
        getEntityManager().flush();
        super.removeSelection();
    }

    public void preparePDF(Batch batch) {
        Contexts.getEventContext().set("batch", batch);
    }

    public String printBatch(Batch batch) {
        Contexts.getConversationContext().set("batch", batch);
        return "/admin/batchPDF.xhtml";
    }

    public void exportToCSV(Batch batch) {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType("text/csv");
        response.setCharacterEncoding("windows-1251");
		response.addHeader("Content-disposition", "attachment; filename=\"" + batch.getShipmentDate() + ".csv\"");

		try {
            PrintWriter w = response.getWriter();
            // Print header
            w.println("NUM;INDEXTO;REGION;AREA;CITY;ADRES;ADRESAT;MASS;VALUE;PAYMENT;COMMENT");

            int num = 0;
            for (Order o: batch.getOrders()) {
                num++;
                w.println(num+";'"+o.getCustomer().getZip()+"';'"+o.getCustomer().getRegion()+"';'"
                        +o.getCustomer().getArea()+"';'"+o.getCustomer().getCity()+"';'"
                        +o.getCustomer().getAddress1()+"';'"+o.getCustomer().getFirstName()
                        +"';1.0;"+o.getTotalAmount().toBigInteger()+";"+o.getTotalAmount().toBigInteger()+";"+o.getOrderId());
            }

			w.flush();
			w.close();
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
    }
}
