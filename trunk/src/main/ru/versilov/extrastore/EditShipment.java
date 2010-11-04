package ru.versilov.extrastore;

import ru.versilov.extrastore.model.Shipment;
import ru.versilov.extrastore.model.ShipmentLine;

import javax.ejb.Remove;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 04.11.2010
 * Time: 2:12:35
 * To change this template use File | Settings | File Templates.
 */
public interface EditShipment {
    
    Shipment getShipment();
    void setShipment(Shipment shipment);
    void create();
    void remove();
    
    void addShipmentLine();
    void removeShipmentLine(ShipmentLine line);
    void saveShipmentLine();
    boolean isEditingNewLine();

    Long getFromResourceId();
    void setFromResourceId(Long fromResourceId);
    

    boolean isWired();
    void persist();
}
