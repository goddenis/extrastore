package ru.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManagementException;
import org.jboss.seam.security.management.IdentityManager;

/**
 * http://extrastore.ru
 * Created by Catalyst on 10.03.11 at 11:44
 */
@Name("register")
public class RegisterAction {
    String email;
    String password;

    @In
    IdentityManager identityManager;

    @In
    FacesMessages facesMessages;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String register() {

        try {
            new RunAsOperation(true) {
                public void execute() {
                    identityManager.createUser(email, password);
                }
            }.run();

            facesMessages.add("Вы были успешно зарегистрированы.");
            return "success";

        } catch (IdentityManagementException e) {
            facesMessages.add(e.getMessage());
            return "fail";
        }
    }
}
