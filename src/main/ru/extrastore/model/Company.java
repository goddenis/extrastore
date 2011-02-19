package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * File profile
 * Creator: Catalyst
 * Date: 30.01.11
 * Time: 3:13
 */
@Entity
public class Company implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    List<Store> stores;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
}
