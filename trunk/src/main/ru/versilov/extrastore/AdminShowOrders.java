package ru.versilov.extrastore;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 21.05.2010
 * Time: 19:22:13
 * To change this template use File | Settings | File Templates.
 */
public interface AdminShowOrders {
    String findOrders();

    String detailOrder();
    String cancelOrder();
    String acceptOrder();
    String shipOrder();


    void destroy();
}
