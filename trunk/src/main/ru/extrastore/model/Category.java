package ru.extrastore.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.jboss.seam.contexts.Contexts;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ксюха
 * Date: 13.02.11
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Indexed
public class Category implements Serializable{
    @Id
    @DocumentId
    @Field(index = Index.TOKENIZED)
    String id;

    @Column(nullable = false, length = 64)
    @Field(index = Index.TOKENIZED)
    String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    Set<Product> products;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    /* Return only products of this category, that belong to the current store */
    @Transient
    public Product[] getStoreProducts() {
        Store s = (Store) Contexts.getSessionContext().get("store");
        Set<Product> intersection = new HashSet<Product>(products);
        intersection.retainAll(s.getProducts());
        return intersection.toArray(new Product[] {});
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category other = (Category) o;

        return (id.equals(other.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
