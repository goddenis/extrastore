package ru.versilov.extrastore.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 29.10.2010
 * Time: 1:48:54
 * Web store class
 * Stores belong to orgaizations.
 * Every organization can have multiple stores.
 */



@Entity
@Table(name="STORES")
public class Store {
    int id;
    Organization owner;
    String name;
    String url;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="organization_id")
    public Organization getOwner() {
        return owner;
    }

    public void setOwner(Organization owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
