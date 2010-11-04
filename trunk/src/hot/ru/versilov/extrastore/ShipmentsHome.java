package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;
import ru.versilov.extrastore.model.Product;
import ru.versilov.extrastore.model.Resource;
import ru.versilov.extrastore.model.Shipment;
import ru.versilov.extrastore.model.ShipmentLine;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import java.awt.image.renderable.RenderContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 30.10.2010
 * Time: 2:47:39
 * To change this template use File | Settings | File Templates.
 */

@Name("shipmentsHome")
public class ShipmentsHome extends EntityList<Shipment> {

    @RequestParameter("fromResourceId")
    Long fromResourceId;

    List<ShipmentLine> shipmentLines = new ArrayList<ShipmentLine>();

    public void setShipmentId(Long id) {
        this.setId(id);
    }

    public Long getShipmentId() {
        return (Long)getId();
    }

    @Override
    protected Shipment createInstance() {
        Shipment shipment = new Shipment();
        shipment.setDate(new Date());
        ShipmentLine sl = new ShipmentLine();
        sl.setShipment(shipment);
        sl.setProduct(getProductByASIN("kz72"));
        sl.setQuantity(10);
        shipment.getLines().add(sl);
        if (fromResourceId != null) {
            shipment.setFrom(getEntityManager().find(Resource.class, fromResourceId));
        }
        return shipment;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    @Override
    protected long getItemId(Shipment item) {
        return item.getId();
    }

    @Override
    public String getEJBQLQuery() {
        return "select sh from Shipment sh";
    }

    public List<Shipment> shipmentsForResource(Resource r) {
        return getEntityManager().createQuery("select s from Shipment s where s.from=:resource or s.to=:resource").setParameter("resource", r).getResultList();
    }

    public void addShipmentLine() {
        ShipmentLine sl = new ShipmentLine();
        sl.setProduct(getProductByASIN("kz72"));
        sl.setQuantity(1);
        this.getShipmentLines().add(sl);
        //getInstance().getLines().add(sl);
//        FacesContext.getCurrentInstance().renderResponse();
    }

    public void removeShipmentLine(ShipmentLine line) {
        getInstance().getLines().remove(line);
//        FacesContext.getCurrentInstance().renderResponse();
    }

    public void saveShipmentLine() {
//        FacesContext.getCurrentInstance().renderResponse();
    }

    public boolean isEditingNewLine() {
        if (getInstance().getLines() == null || getInstance().getLines().size() == 0) {
            return false;
        }
        return (getInstance().getLines().get(getInstance().getLines().size()-1).getQuantity() == 0);
    }


     protected Product getProductByASIN(String asin) {
        EntityManager em = this.getEntityManager();
        Product product = (Product)em.createQuery("select p from Product p where p.ASIN = :asin ")
                .setParameter("asin", asin)
                .getSingleResult();
        return product;
    }

    public List<ShipmentLine> getShipmentLines() {
        return shipmentLines;
    }

    public void setShipmentLines(List<ShipmentLine> shipmentLines) {
        this.shipmentLines = shipmentLines;
    }

}
