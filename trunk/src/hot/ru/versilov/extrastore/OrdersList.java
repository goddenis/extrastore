package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.framework.EntityQuery;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.ScrollableTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.el.ValueExpression;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 25.06.2010
 * Time: 0:30:45
 * To change this template use File | Settings | File Templates.
 */
@Stateful
@Name("ordersList")
@Scope(ScopeType.SESSION)
public class OrdersList implements OrdersListI {
    private static final String EJBQL = "select o from Order o";

    private Order order = new Order();

    private Selection selection = new SimpleSelection();
    private List<Order> selectedOrders = new ArrayList<Order>();

    @PersistenceContext(type=PersistenceContextType.EXTENDED)
    private EntityManager em;

    private List<Order> resultList = null;

    private boolean incompleteOnly = true;

    private ExtendedTableDataModel<Order> ordersDataModel;

    public OrdersList() {
    }



    public List<Order> getResultList() {
        if (resultList == null) {
            String query = EJBQL;
            if (incompleteOnly) {
                query += " where o.status <> 3";
            }
            resultList = em.createQuery(query).getResultList();
        }
        return resultList;
    }

    // Make the next getResultList() call re-render list;
    private void invalidateResultList() {
        resultList = null;
        ordersDataModel = null;
    }

    public ExtendedTableDataModel<Order> getOrdersDataModel() {
     		if (ordersDataModel == null) {
			ordersDataModel = new ExtendedTableDataModel<Order>(new DataProvider<Order>(){

				private static final long serialVersionUID = 505408782103164847L;

				public Order getItemByKey(Object key) {
					for(Order c : getResultList()){
						if (key.equals(getKey(c))){
							return c;
						}
					}
					return null;
				}

				public List<Order> getItemsByRange(int firstRow, int endRow) {
					return getResultList().subList(firstRow, endRow);
				}

				public Object getKey(Order item) {
					return item.getOrderId();
				}

				public int getRowCount() {
					return getResultList().size();
				}
				
			});
		}
		return ordersDataModel;
    }

    public Order getCurOrder() {
        return order;
    }

    public boolean isIncompleteOnly() {
        return incompleteOnly;
    }

    public void setIncompleteOnly(boolean incompleteOnly) {
        if (incompleteOnly != this.incompleteOnly)  {
            this.incompleteOnly = incompleteOnly;
            invalidateResultList();
        }
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public void sent(Order order) {
        // Change status and send e-mail notification here.
        order.setStatus(Order.Status.SHIPPED);
        System.out.println("Order No." + order.getOrderId() + " was sent.");
    }

    public void accept(Order order) {
        // Change status and send e-mail notification here.
        order.setStatus(Order.Status.PROCESSING);
        System.out.println("Order No." + order.getOrderId() + " was accepted.");
    }

    public void openSelection() {
        int result = updateSelectedOrdersStatus(Order.Status.OPEN);
        System.out.println(result + " orders were opened.");
    }

    public void acceptSelection() {
        int result = updateSelectedOrdersStatus(Order.Status.PROCESSING);
        System.out.println(result + " orders were accepted.");
    }

    public void sendSelection() {
        int result = updateSelectedOrdersStatus(Order.Status.SHIPPED);
        invalidateResultList();
        System.out.println(result + " orders were shipped.");
    }

    protected void takeSelection() {
    	selectedOrders.clear();
		Iterator<Object> iterator = getSelection().getKeys();
		while (iterator.hasNext()) {
			Object key = iterator.next();
            ordersDataModel.setRowKey(key);
            if (ordersDataModel.isRowAvailable()) {
                Order o = ordersDataModel.getRowData();
			    selectedOrders.add(o);
            }
		}    
    }

    protected int updateSelectedOrdersStatus(Order.Status newStatus) {
        takeSelection();
        int result = 0;
        for (Order selectedOrder : selectedOrders) {
            selectedOrder.setStatus(newStatus);
            System.out.println("Order #" + selectedOrder.getOrderId() + " was changed.");
            result++;
        }

        return result;
    }

    @Remove
    public void remove() {
        
    }

    
}
