package ru.extrastore.model;


import org.jboss.seam.annotations.security.management.RoleName;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Catalyst on 10.03.11 at 11:26
 */
@Entity
public class Role implements Serializable {

    @Id
    @RoleName
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
