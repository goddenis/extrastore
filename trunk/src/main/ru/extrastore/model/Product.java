package ru.extrastore.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="Product2Category",
               joinColumns=@JoinColumn(name="productId"),
               inverseJoinColumns=@JoinColumn(name="categoryId"))
    @IndexedEmbedded
    Set<Category> categories;

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

    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

}
