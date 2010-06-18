package ru.versilov.extrastore;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 23.11.2009
 * Time: 1:20:12
 * To change this template use File | Settings | File Templates.
 */
public interface ProcessOrder {
    Integer getOrderNum();
    void setOrderNum(Integer orderNum);
    ProcessOrderAction.OrderState getState();
    void accept();
    void assemble();
    void export();
    void send();
    void notifyReceiver();
    void getPaid();
    void archive();
    void remove();
    void setOrderWeight(Integer orderWeight);
    Integer getOrderWeight();
}
