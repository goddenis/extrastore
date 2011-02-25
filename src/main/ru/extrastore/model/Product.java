package ru.extrastore.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.jboss.seam.contexts.Contexts;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * File profile
 * Creator: Catalyst
 * Date: 25.01.11
 * Time: 11:43
 */
@Entity
@Indexed
public class Product implements Serializable {

    @Id
    @GeneratedValue
    @DocumentId
    long id;

    @Column(length = 32, unique = true)
    String asin;

    @Column(nullable = false, length = 64)
    String name;

    @Column(length = 100)
    String description;

    @Column(length = 512)
    String longDescription; // HTML is accepted

    @Column(precision = 12, scale = 2)
    long price;

    @Column(length = 32, unique = true)
    String urlAlias;

    @Column(length = 128)
    String urlImageSmall;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="Product2Category",
               joinColumns=@JoinColumn(name="productId"),
               inverseJoinColumns=@JoinColumn(name="categoryId"))
    @IndexedEmbedded
    Set<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductProperty> properties;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Price> prices;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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

    public List<ProductProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ProductProperty> properties) {
        this.properties = properties;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

    @Transient
    public long getMainPrice() {
        Store s = (Store)Contexts.getSessionContext().get("store");
        for (Price p: prices) {
            if ((p.getStore() == null || p.getStore().equals(s)) && p.isMain()) {
                return p.getValue();
            }
        }
        return price;
    }

    @Transient
    public long getOldPrice() {
        Store s = (Store)Contexts.getSessionContext().get("store");
        for (Price p: prices) {
            if ((p.getStore() == null || p.getStore().equals(s)) && p.isOld()) {
                return p.getValue();
            }
        }
        return 0;
    }

    @Transient
    public Collection<Price> getExtraPrices() {
        List<Price> extraPrices = new ArrayList<Price>();
        Store s = (Store)Contexts.getSessionContext().get("store");
        for (Price p: prices) {
            if ((p.getStore() == null || p.getStore().equals(s)) && !p.isOld() && !p.isMain()) {
                extraPrices.add(p);
            }
        }
        return extraPrices;
    }

    @Transient
    public final Price[] getPricesByStore(Store store) {
        Set<Price> r =  filterByStore(prices, store);
        Price result[] = new Price[r.size()];
        return r.toArray(result);
    }


    public static <E extends StoreConstraint> Set<E> filterByStore(Set<E> source, Store store)  {
        return filterByStore(source, store, true);
    }

    public static <E extends StoreConstraint> Set<E> filterByStore(Set<E> source, Store store, boolean includeOrphans) {
        Set<E> result = new HashSet<E>();
        for (E sc: source) {
            if (
                    (sc.getStore() == null && includeOrphans)
                    || sc.getStore().equals(store)
                ) {
                result.add(sc);
            }
        }
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        assert this.id != 0;
        assert product.id != 0;

        if (this.id != product.id) return false;

        assert this.getName().equals(product.getName()) : this.getName();

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

