package ru.extrastore.model.delivery;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * http://extrastore.ru
 * Created by Catalyst on 01.03.11 at 20:14
 */
@Entity
@DiscriminatorValue("COURIER")
public class DeliveryTypeCourier extends DeliveryType {
}
