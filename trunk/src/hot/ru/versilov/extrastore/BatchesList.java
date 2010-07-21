package ru.versilov.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;
import ru.versilov.extrastore.model.Batch;
import ru.versilov.extrastore.model.Order;
import ru.versilov.extrastore.model.User;

import javax.persistence.EntityManager;
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
}
