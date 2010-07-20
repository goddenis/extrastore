/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore.model;

import java.io.Serializable;

import javax.persistence.*;
import org.hibernate.validator.*;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;

@Entity
@Table(name="USERS")
public abstract class User
    implements Serializable
{
    long    id;

    String email;       // Used like login also
    String  password;

    String  firstName;
    String  lastName;

    @Id @GeneratedValue
    @Column(name="USERID")
    public long getId() {
        return id;
    }                    
    public void setId(long id) {
        this.id = id;
    }     

    @Column(name="EMAIL",unique=false,nullable=true,length=50)
    @Length(min=6,max=64)
    @UserPrincipal
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="PASSWORD",nullable=true,length=50)
    @Length(min=6,max=50)
    @UserPassword(hash="none")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="FIRSTNAME",length=50)
    @UserFirstName
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @Column(name="LASTNAME",length=50)
    @UserLastName
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @Transient
    public boolean isAdmin() {
       return false;
    }
}
