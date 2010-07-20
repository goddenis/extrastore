package ru.versilov.extrastore;

import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import ru.versilov.extrastore.model.Order;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 13.07.2010
 * Time: 11:43:36
 * To change this template use File | Settings | File Templates.
 */
public interface OrdersListI {
    List<Order> getResultList();

    ExtendedTableDataModel<Order> getOrdersDataModel();

    Order getCurOrder();

    boolean isIncompleteOnly();

    void setIncompleteOnly(boolean incompleteOnly);

    void invalidateResultList();

    Selection getSelection();

    void setSelection(Selection selection);


    void sent(Order order);
    void accept(Order order);
    void remove(Order order);
    void cancel(Order order);

    void openSelection();
    void acceptSelection();
    void sendSelection();
    void removeSelection();
    void cancelSelection();

    void remove();
    
}
