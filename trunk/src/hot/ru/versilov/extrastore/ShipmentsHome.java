package ru.versilov.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;
import ru.versilov.extrastore.model.Resource;
import ru.versilov.extrastore.model.Shipment;

import javax.persistence.EntityManager;
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
}
