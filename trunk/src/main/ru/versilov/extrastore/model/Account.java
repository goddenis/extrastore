package ru.versilov.extrastore.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 29.10.2010
 * Time: 1:03:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="ACCOUNTS")
public class Account {
    private int id;
    private String name;
    private Admin admin;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name="admin_id")
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
