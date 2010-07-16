package ru.versilov.extrastore;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 15.07.2010
 * Time: 0:08:20
 * Converter for shuttle list edit in the orderEdit.xhtml view.
 */

public class OrderLineConverter implements javax.faces.convert.Converter{


	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		int index = value.indexOf(':');
        String asin = value.substring(0, index);
        int quantity = Integer.parseInt(value.substring(index+1));

        EntityManager em = (EntityManager)Component.getInstance("entityManager");

        Product product = (Product)em.createQuery("select p from Product p where p.ASIN = :asin").setParameter("asin", asin).getSingleResult();
        if (product!= null)   {
    		return new OrderLine(product, quantity);
        } else {
            return null;
        }
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		OrderLine line = (OrderLine) value;
		return line.getProduct().getASIN() + ":" + line.getQuantity();
	}


}
