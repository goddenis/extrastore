package ru.extrastore;

import org.jboss.seam.international.Messages;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;
import org.testng.annotations.Test;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Iterator;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * http://extrastore.ru
 * Created by Catalyst on 10.03.11 at 20:28
 */
public class AuthenticationTest extends SeamTest {

    private static final String EXISTING_USER_EMAIL = "ivanov@mail.ru";
    private static final String NEW_USER_EMAIL = "sidorov@mail.ru";

    @Test
    public void testUnsuccessfulLogin() throws Exception {
        new FacesRequest("/login.xhtml") {
        }.run();

        new FacesRequest("/login.xhtml") {
            @Override
            protected void applyRequestValues() throws Exception {
                setValue("#{credentials.username}", "bart@nightmail.ru");
                setValue("#{credentials.password}", "password");
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{identity.login()}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertFalse((Boolean) getValue("#{identity.loggedIn}"));

                Iterator messages = FacesContext.getCurrentInstance().getMessages();
                assert messages.hasNext();
                assert ((FacesMessage)messages.next()).getSummary().equals(Messages.instance().get("org.jboss.seam.loginFailed"));
                assert !messages.hasNext();
            }
        }.run();

    }



    @Test
    public void testSuccessfulLogin() throws Exception {

        new FacesRequest("/login.xhtml") {
        }.run();

        new FacesRequest("/login.xhtml") {
            @Override
            protected void applyRequestValues() throws Exception {
                setValue("#{credentials.username}", EXISTING_USER_EMAIL);
                setValue("#{credentials.password}", "password");
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{identity.login()}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assertTrue((Boolean) getValue("#{identity.loggedIn}"));

                Iterator messages = FacesContext.getCurrentInstance().getMessages();
                assert messages.hasNext();
                assert ((FacesMessage)messages.next()).getSummary().equals(Messages.instance().get("org.jboss.seam.loginSuccessful"));
                assert !messages.hasNext();

            }
        }.run();
    }





    @Test
    public void testRegistrationWithExistingLogin() throws Exception {
        new FacesRequest("/register.xhtml") {
        }.run();

        new FacesRequest("/register.xthml") {
            @Override
            protected void applyRequestValues() throws Exception {
                setValue("#{register.email}", EXISTING_USER_EMAIL);
                setValue("#{register.password}", "123456");

                new RunAsOperation(true) {
                    @Override
                    public void execute() {
                        assertTrue(IdentityManager.instance().userExists(EXISTING_USER_EMAIL));
                    }
                }.run();
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{register.register()}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assert getOutcome().equals("fail");
            }
        }.run();
    }




    @Test
    public void testSuccessfulRegistration() throws Exception {
        new FacesRequest("/register.xthml") {
        }.run();

        new FacesRequest("/register.xhtml") {
            @Override
            protected void applyRequestValues() throws Exception {
                setValue("#{register.email}", NEW_USER_EMAIL);
                setValue("#{register.password}", "123456");
            }

            @Override
            protected void invokeApplication() throws Exception {
                invokeAction("#{register.register()}");
            }

            @Override
            protected void renderResponse() throws Exception {
                assert getOutcome().equals("success");

                new RunAsOperation(true) {
                    @Override
                    public void execute() {
                        assertTrue(IdentityManager.instance().userExists(NEW_USER_EMAIL));
                    }
                }.run();
            }
        }.run();
    }
}
