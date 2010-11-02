package ru.versilov.extrastore.model;

import javax.persistence.*;
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
    int id;
    Date date;
    Resource from;
    Resource to;

    List<ShipmentLine> lines;


    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return lines;
    }

    public void setLines(List<ShipmentLine> lines) {
        this.lines = lines;
    }
}
