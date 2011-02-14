package ru.extrastore.model;

import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * File profile
 * Creator: Catalyst
 * Date: 30.01.11
 * Time: 3:14
 */
@Entity
@Name("store")
public class Store {
    @Id
    @GeneratedValue
    Long id;

    @NotNull
    String name;

    @ManyToOne
    @NotNull
    Company company;

    @NotNull
    String domain;

    String templatePath;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="Product2Store",
               joinColumns=@JoinColumn(name="storeId"),
               inverseJoinColumns=@JoinColumn(name="productId"))
    @IndexedEmbedded
    List<Product> products;

    // Contains all categories of all products of this store.
    @Transient
    Set<Category> categories;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Set<Category> getCategories() {
        if (this.categories == null) {
            this.categories = new HashSet<Category>();
            for (Product p: this.products) {
                categories.addAll(p.categories);
            }
        }
        return this.categories;
    }
}
