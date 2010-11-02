package ru.versilov.extrastore.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 28.10.2010
 * Time: 2:50:47
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="ORGANIZATIONS")
public class Organization {
    private int id;
    private Organization parent;
    private Account account;
    private String name;

    @Id
    @GeneratedValue
    @Column(name="ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="parent_id")
    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    @ManyToOne
    @JoinColumn(name="account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
