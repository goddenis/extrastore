package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.web.RequestParameter;
import ru.versilov.extrastore.model.Product;
import ru.versilov.extrastore.model.Resource;
import ru.versilov.extrastore.model.Shipment;
import ru.versilov.extrastore.model.ShipmentLine;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 04.11.2010
 * Time: 2:12:14
 * To change this template use File | Settings | File Templates.
 */

@Stateful
@Scope(ScopeType.CONVERSATION)
@Name("editShipment")
public class EditShipmentAction implements EditShipment {
    @PersistenceContext
    EntityManager em;

    @RequestParameter("fromResourceId")
    Long fromResourceId;

    @RequestParameter("id")
    Long shipmentId;


    private Shipment shipment;

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Long getFromResourceId() {
        return fromResourceId;
    }

    public void setFromResourceId(Long fromResourceId) {
        this.fromResourceId = fromResourceId;
    }

    @Begin
    public void create() {
        if (shipmentId == null) {
            shipment = new Shipment();
            shipment.setDate(new Date());
        }

        if (fromResourceId != null) {
                shipment.setFrom(em.find(Resource.class, fromResourceId));
        }
    }

    @Remove
    public void remove() {
    }


    public void addShipmentLine() {
        ShipmentLine sl = new ShipmentLine();
        sl.setProduct(getProductByASIN("kz72"));
        sl.setShipment(shipment);
        shipment.getLines().add(sl);

    }

    public void removeShipmentLine(ShipmentLine line) {
        shipment.getLines().remove(line);
    }

    public void saveShipmentLine() {
    }

     protected Product getProductByASIN(String asin) {
        Product product = (Product)em.createQuery("select p from Product p where p.ASIN = :asin ")
                .setParameter("asin", asin)
                .getSingleResult();
        return product;
    }


    public boolean isWired() {
        return (shipment != null && shipment.getId() != null);
    }

    @End
    public void persist() {
        em.persist(shipment);
    }
    
    public boolean isEditingNewLine() {
        if (shipment.getLines() == null || shipment.getLines().size() == 0) {
            return false;
        }
        return (shipment.getLines().get(shipment.getLines().size()-1).getQuantity() == 0);
    }
    
}
