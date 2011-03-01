package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Catalyst on 01.03.11 at 16:12
 */
@Entity
public class Article implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @JoinColumn(name = "storeId", nullable = false)
    Store store;

    String urlAlias;

    String name;

    @Column(length = 4096)
    String html;

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

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
