package ru.versilov.extrastore.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 26.10.2010
 * Time: 12:27:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="RESOURCES")
public class Resource {
    private long id;
    private Organization owner;
    private String name;
    private String type;

    private List<Remainder> contents;


    @Id
    @GeneratedValue
    @Column(name="ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="OWNER", nullable = false)
    public Organization getOwner() {
        return owner;
    }

    public void setOwner(Organization owner) {
        this.owner = owner;
    }

    @Column(name="NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @OneToMany(mappedBy="resource", cascade=CascadeType.ALL)
    public List<Remainder> getContents() {
        return contents;
    }

    public void setContents(List<Remainder> contents) {
        this.contents = contents;
    }

    @Transient
    public long getTotal() {
        long total = 0;
        for (Remainder r: contents) {
            total += r.getQuantity();
        }
        return total;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return (((Resource)obj).getId() == this.getId());
    }
}
