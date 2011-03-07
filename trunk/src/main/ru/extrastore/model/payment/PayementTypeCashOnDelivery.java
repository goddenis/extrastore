package ru.extrastore.model.payment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * http://extrastore.ru
 * Created by Catalyst on 06.03.11 at 1:14
 */
@Entity
@DiscriminatorValue("CASHONDELIVERY")
public class PayementTypeCashOnDelivery extends PaymentType{

}
