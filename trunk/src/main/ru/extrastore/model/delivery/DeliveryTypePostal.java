package ru.extrastore.model.delivery;

import ru.extrastore.model.Order;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * http://extrastore.ru
 * Created by Catalyst on 01.03.11 at 20:08
 */
@Entity
@DiscriminatorValue("POSTAL")
public class DeliveryTypePostal extends DeliveryType {
    @Override
    public long getCost(Order order) {
        if (this.getFreeAfter() != 0) {
            return super.getCost(order);    // Fixed delivery cost with free limit
        } else {
            return -1; // Calculate cost based on russian post rules.
        }
    }
}
