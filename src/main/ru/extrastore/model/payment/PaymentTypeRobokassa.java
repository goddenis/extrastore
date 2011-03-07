package ru.extrastore.model.payment;

import ru.extrastore.model.Order;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * http://extrastore.ru
 * Created by Catalyst on 05.03.11 at 23:47
 */
@Entity
@DiscriminatorValue("ROBOKASSA")
public class PaymentTypeRobokassa extends PaymentType{
    @Column(nullable = true, length = 32)
    String login;

    @Column(nullable = true, length = 32)
    String password1;

    @Column(nullable = true, length = 32)
    String password2;

    @Column(nullable = true, length = 32)
    String currency;

    @Column(nullable = true, length = 32)
    String culture;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    @Transient
    public String getOrderCRC(Order order) {
        return getCRC(this.login + ":" + order.getTotalCost() + ":" + order.getId() + ":" + this.password1);
    }

    @Transient
    public String getResultCRC(long outSum, long orderId) {
        return getCRC(outSum + ":" + orderId + ":" + this.password2);
    }

    @Transient
    public String getSuccessCRC(long outSum, long orderId) {
        return getCRC(outSum + ":" + orderId + ":" + this.password1);
    }

    @Transient
    public String getCRC(String msg) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        byte[] md5 = md.digest(msg.getBytes());

        BigInteger i = new BigInteger(1,md5);

		return String.format("%1$032X", i);
    }
}
