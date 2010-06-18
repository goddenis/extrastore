package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;

import javax.ejb.Stateful;
import javax.ejb.Remove;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 23.11.2009
 * Time: 0:59:27
 * To change this template use File | Settings | File Templates.
 */

@Stateful
@Name("processOrder")
public class ProcessOrderAction implements ProcessOrder, Serializable {

    public enum OrderState {NONEXISTENT, ACCEPTED, ASSEMBLED, EXPORTED, SENT, NOTIFIED, PAID, ARCHIVED };

    
    private OrderState state = OrderState.NONEXISTENT;
    private Integer orderNum = null;
    private Integer orderWeight = null;


    public Integer getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(Integer orderWeight) {
        this.orderWeight = orderWeight;
    }

    public OrderState getState() {
        return state;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Begin(nested=true, pageflow="processorder")
    public void accept() {
        this.state = OrderState.ACCEPTED;
        System.out.println("Accepted.");
    }

    public void assemble() {
        this.state = OrderState.ASSEMBLED;
        System.out.println("Assembled.");
    }

    public void export() {
        this.state = OrderState.EXPORTED;
        System.out.println("Exported.");
    }
    public void send() {
        this.state = OrderState.SENT;
        System.out.println("Sent.");
    }
    public void notifyReceiver() {
        this.state = OrderState.NOTIFIED;
        System.out.println("Notified.");
    }
    public void getPaid() {
        this.state = OrderState.PAID;
        System.out.println("Paid.");
    }

    @End
    public void archive() {
        this.state = OrderState.ARCHIVED;        
        System.out.println("Archived.");
    }


    @Remove
    public void remove() {
    }
}
