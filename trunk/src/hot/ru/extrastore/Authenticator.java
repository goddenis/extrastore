package ru.extrastore;

import org.jboss.seam.annotations.*;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;
import ru.extrastore.auxiliary.BooleanWrapper;
import ru.extrastore.model.Address;
import ru.extrastore.model.Store;
import ru.extrastore.model.User;

import javax.persistence.EntityManager;

/**
 * http://extrastore.ru
 * Created by Catalyst on 10.03.11 at 1:32
 */

@Name("authenticator")
public class Authenticator {
    public static final int AUTH_TYPE_NEW_USER = 1;
    public static final int AUTH_TYPE_EXISTING_USER = 2;
    public static final int AUTH_TYPE_NO_EMAIL = 3;

    int authType = AUTH_TYPE_EXISTING_USER;


    @In("entityManager")
    EntityManager em;

    @Logger
    Log log;

    @In
    FacesMessages facesMessages;

    @In
    Credentials credentials;

    @In
    Identity identity;

    @In
    IdentityManager identityManager;

    @In
    Store store;


    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    private boolean userExists(final String principal) {
        final BooleanWrapper result = new BooleanWrapper(true);
        new RunAsOperation(true) {
            public void execute() {
                if (!identityManager.userExists(principal)) {
                    result.setValue(false);
                }
            }
        }.run();

        return result.getValue();
    }

    @Begin
    public String forwardToDelivery() {
        return "success";
    }

    @Begin
    public String authenticate() {
        log.info("Authenticator(#0).authenticate()", this.hashCode());
        switch (authType) {
            case AUTH_TYPE_NEW_USER:
            case AUTH_TYPE_NO_EMAIL:
                User customer = new User();
                customer.setAddress(new Address());
                customer.getAddress().setCustomer(customer);

                if (authType != AUTH_TYPE_NO_EMAIL) {
                    if (!userExists(identity.getCredentials().getUsername())) {
                        customer.setEmail(identity.getCredentials().getUsername());
                    } else {
                        facesMessages.add("Этот адрес эл. почты уже зарегистрирован.");
                        return null;
                    }
                }


                // For testing purposes only
                if ("localhost".equals(store.getDomain())) {
                    customer.setLastName("Тестовый");
                    customer.setFirstName("Пользователь");
                    customer.setFatherName("Не надо этот заказ принимать");

                    customer.setPhone("9053042007");

                    customer.getAddress().setZip(443000);
                    customer.getAddress().setRegion("Несуществующая обл.");
                    customer.getAddress().setArea("Неизвестный р-он");
                    customer.getAddress().setTown("с. Забытое");
                    customer.getAddress().setAddress("ул. Покинутая, д. 23, корп. 9, кв. 19");
                }

                Contexts.getSessionContext().set("user", customer);
                break;


            case AUTH_TYPE_EXISTING_USER:
                if (identity.login() == null) {
                    log.info("Could not login with credentials: #0 and #1",
                            identity.getCredentials().getUsername(),
                            identity.getCredentials().getPassword());
                    return null;
                }
                break;


            default:
                throw new IllegalArgumentException("Invalid authentication switch value.");
        }

        return "success";
    }

    @Observer(JpaIdentityStore.EVENT_USER_AUTHENTICATED)
    public void onUserAuthenticated(User user) {
        log.info("authenticator.onUserAuthenticated(#0)", this.hashCode());
        Contexts.getSessionContext().set("user", user);
    }
}
