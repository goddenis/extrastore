package ru.extrastore.model.payment;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * http://extrastore.ru
 * Created by Catalyst on 06.03.11 at 0:42
 */
@Entity
@DiscriminatorValue("CARD")
public class PaymentTypeCard extends PaymentType{
    @Column(nullable = true, length = 32)
    String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
