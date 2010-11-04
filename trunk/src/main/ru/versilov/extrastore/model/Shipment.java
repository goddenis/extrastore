package ru.versilov.extrastore.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 29.10.2010
 * Time: 1:36:06
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="SHIPMENTS")
public class Shipment {
    Long id;
    Date date;
    Resource from;
    Resource to;

    List<ShipmentLine> lines;


    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    @JoinColumn(name="from_id", nullable = true)
    public Resource getFrom() {
        return from;
    }

    public void setFrom(Resource from) {
        this.from = from;
    }

    @ManyToOne
    @JoinColumn(name="to_id", nullable = true)
    public Resource getTo() {
        return to;
    }

    public void setTo(Resource to) {
        this.to = to;
    }

    @OneToMany(mappedBy="shipment", cascade=CascadeType.ALL)
    public List<ShipmentLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<ShipmentLine>();
        }
        return lines;
    }

    public void setLines(List<ShipmentLine> lines) {
        this.lines = lines;
    }

    @Transient
    public int getTotal() {
        int total = 0;
        for (ShipmentLine sl: lines) {
            total += sl.getQuantity();            
        }
        return total;
    }

}
