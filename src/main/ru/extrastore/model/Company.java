package ru.extrastore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

/**
 * File profile
 * Creator: Catalyst
 * Date: 30.01.11
 * Time: 3:13
 */
@Entity
public class Company implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    String name;

    @OneToMany(mappedBy = "company")
    List<Store> stores;
}
