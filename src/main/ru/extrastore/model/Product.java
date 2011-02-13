package ru.extrastore.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.jboss.seam.annotations.Name;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * File profile
 * Creator: Catalyst
 * Date: 25.01.11
 * Time: 11:43
 */
@Entity
@Name("product")
@Indexed
public class Product implements Serializable {

    @Id
    @GeneratedValue
    @DocumentId
    long id;

    @Column(length = 32)
    String asin;

    @Column(nullable = false, length = 128)
    String name;

    @Column(precision = 12, scale = 2)
    BigDecimal price;

    @Column(length = 64)
    String urlAlias;

    @Column(length = 128)
    String urlImageSmall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getASIN() {
        return asin;
    }

    public void setASIN(String asin) {
        this.asin = asin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUrlImageSmall() {
        return urlImageSmall;
    }

    public void setUrlImageSmall(String urlImageSmall) {
        this.urlImageSmall = urlImageSmall;
    }

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }
}

