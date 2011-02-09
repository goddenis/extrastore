package ru.extrastore.model;

import org.jboss.seam.annotations.Name;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * File profile
 * Creator: Catalyst
 * Date: 30.01.11
 * Time: 3:13
 */
@Entity
@Name("company")
public class Company {

    @Id
    @GeneratedValue
    Long id;

    String name;

    @OneToMany(mappedBy = "company")
    List<Store> stores;
}
