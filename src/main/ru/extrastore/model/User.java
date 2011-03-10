package ru.extrastore.model;

import org.jboss.seam.annotations.security.management.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * http://extrastore.ru
 * Created by Catalyst on 22.02.11 at 16:09
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements Serializable{
    @Id
    @GeneratedValue
    long id;

    @UserPrincipal
    String email;

    @UserPassword(hash = "md5")
    String password;

    @UserRoles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "User2Role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name="roleId"))
    List<Role> roles;

    @UserFirstName
    String firstName;

    @UserLastName
    String lastName;

    String fatherName;

    String phone;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    Set<Order> orders;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    // Customer specific fields

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

}
