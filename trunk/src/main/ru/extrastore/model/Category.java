package ru.extrastore.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.jboss.seam.contexts.Contexts;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
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

    @Column(length = 64, nullable = false)
    @Field(index = Index.TOKENIZED)
    String name;

    @Column(length = 1024, nullable = true)
    String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        assert this.id != null;
        assert other.id != null;

        if (this.id.equals(other.id)) {
            assert this.name.equals(other.name);
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
