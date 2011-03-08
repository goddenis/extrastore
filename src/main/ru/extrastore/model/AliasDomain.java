package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;

/**
* http://extrastore.ru
* Created by Catalyst on 08.03.11 at 13:48
*/
@Entity
public class AliasDomain implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @JoinColumn(name = "storeId", nullable = false)
    Store store;

    @Column(length = 32, nullable = false)
    String domain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
