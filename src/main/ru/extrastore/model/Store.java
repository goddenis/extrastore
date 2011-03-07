package ru.extrastore.model;

import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import ru.extrastore.model.delivery.DeliveryType;
import ru.extrastore.model.payment.PaymentType;

import javax.persistence.*;
import java.io.Serializable;
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
public class Store implements Serializable {
    public static final String DEFAULT_TEMPLATE_PATH = "/templates/default";        // Made public for test purposes.

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    String name;

    @ManyToOne
    @NotNull
    Company company;

    @NotNull
    @Column(unique = true)
    String domain;

    String templatePath;

    @Column(nullable = true)
    String gaAccount;

    @Column(nullable = true)
    String yaMetrika;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="Product2Store",
               joinColumns=@JoinColumn(name="storeId"),
               inverseJoinColumns=@JoinColumn(name="productId"))
    @IndexedEmbedded
    List<Product> products;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    List<Article> articles;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    Set<DeliveryType> deliveryTypes;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    Set<PaymentType> paymentTypes;


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
        if (templatePath == null) {
            templatePath = DEFAULT_TEMPLATE_PATH;
        }
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getGaAccount() {
        return gaAccount;
    }

    public void setGaAccount(String gaAccount) {
        this.gaAccount = gaAccount;
    }

    public String getYaMetrika() {
        return yaMetrika;
    }

    public void setYaMetrika(String yaMetrika) {
        this.yaMetrika = yaMetrika;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Set<DeliveryType> getDeliveryTypes() {
        return deliveryTypes;
    }

    public void setDeliveryTypes(Set<DeliveryType> deliveryTypes) {
        this.deliveryTypes = deliveryTypes;
    }

    public Set<PaymentType> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(Set<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    @Transient
    public PaymentType getPaymentType(String alias) {
        for (PaymentType pt: this.paymentTypes) {
            if (pt.getAlias().equals(alias)) {
                return pt;
            }
        }

        return null;
    }

    @Transient
    public Set<Category> getCategories() {
        if (this.categories == null) {
            this.categories = new HashSet<Category>();
            for (Product p: this.products) {
                categories.addAll(p.categories);
            }
        }
        return this.categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        assert this.id != 0;
        assert store.id != 0;

        if (!this.id.equals(store.id)) return false;

        assert this.getName().equals(store.getName()) : this.getName();
        assert this.getDomain().equals(store.getDomain()) : this.getDomain();

        return true;
    }

}
