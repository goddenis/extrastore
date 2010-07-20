/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Identity;
import ru.versilov.extrastore.model.Customer;

@Stateful
@Name("editCustomer")
public class EditCustomerAction
    implements EditCustomer
{
    @PersistenceContext
    EntityManager em;
    
    @Resource
    SessionContext ctx;

    @In
    Context sessionContext;

    @In(create=true)
    @Out
    Customer customer;
    
    @In
    FacesMessages facesMessages;
    
    @In Identity identity;

    String password = null;    

    public void setPasswordVerify(String password) {
        this.password = password;
    }
    public String getPasswordVerify() {
        return password;
    }


    @Begin(nested=true, pageflow="newuser") 
    public void startEdit() {
    }

    public boolean isValidNamePassword() {
        boolean ok = true;
        if (!isUniqueName()) {
            facesMessages.add("email", "This name is already in use");
            ok = false;
        }
        if (!isPasswordsMatch()) {
            facesMessages.add("passwordVerify", "Must match password field");
            ok = false;
        }
        return ok;
    }

    @SuppressWarnings("unchecked")
    private boolean isUniqueName() {
        String email = customer.getEmail();
        if (email == null) return true;

        List<Customer> results = em.createQuery("select c from Customer c where c.email = :email")
            .setParameter("email", email)
            .getResultList();

        return results.size() == 0;
    }

    private boolean isPasswordsMatch() {
        String customerpass = customer.getPassword();

        return (password != null)
            && (customerpass != null) 
            && (customerpass.equals(password));
    }

    public String saveUser() {
        if (!isValidNamePassword()) {
            facesMessages.add("Email #{customer.email} is already registered");
            return null;
        }

        try {
            em.persist(customer);
            sessionContext.set("currentUser", customer);
            Actor.instance().setId(customer.getEmail());
            
            identity.setUsername(customer.getEmail());
            identity.setPassword(customer.getPassword());
            identity.login();
            
            facesMessages.addFromResourceBundle("createCustomerSuccess");
            return "success";
        } catch (InvalidStateException e) {
            InvalidValue[] vals = e.getInvalidValues();
            for (InvalidValue val: vals) {
                facesMessages.add(val);
            }

            return null;
        } catch (RuntimeException e) {
            ctx.setRollbackOnly();

            facesMessages.addFromResourceBundle("createCustomerError");

            return null;
        }
    }

    public Map<String,Integer> getCreditCardTypes() {
        Map<String,Integer> map = new TreeMap<String,Integer>();
        for (int i=1; i<=5; i++) {
            map.put(Customer.cctypes[i-1], i);
        }
        return map;
    }

    @Remove
    public void destroy() {}
}
