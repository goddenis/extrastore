/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.jboss.seam.annotations.Name;
import org.hibernate.validator.*;

@Entity
@Name("customer")
@DiscriminatorValue("customer")
public class Customer
    extends User
    implements Serializable
{
    private static final long serialVersionUID = 5699525147178760355L;

    public static String[] cctypes = {"MasterCard", "Visa", "Discover", "Amex", "Dell Preferred"}; 

    String  address1;
    String  address2;
    String  zip;
    String  region;
    String  area;
    String  city;

    String  phone;

    Integer creditCardType = 1;
    String  creditCard     = "000-0000-0000";
    int     ccMonth        = 1;
    int     ccYear         = 2005;

    Set<Order> orders;


    public Customer() {
    }


    @Column(name="ADDRESS1",length=50)
//    @NotNull
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Column(name="ADDRESS2",length=50)
//    @NotNull
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Column(name="CITY",length=50)  
//    @NotNull
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    // For captials the city is coded in the region field, while other fields are empty.
    // This method is guaranteed to return city name in all occasions
    @Transient
    public String getCityNevertheless() {
        return ((this.city != null && this.city.length() > 0) ? this.city : this.region);
    }


    @Column(name="ZIP", length=10)
    @Length(min=5, max=10, message = "Слишком длинный или короткий.")
//    @Pattern(regex="[0-9]{5}(-[0-9]{4})?", message="not a valid zipcode") // {validator.zip}
//    @NotNull(message = "Обязательное поле")
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    @Column(name="region", length=36)
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Column(name="area", length=36)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Column(name="PHONE",length=50)
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Column(name="CREDITCARDTYPE")    
    public Integer getCreditCardType() {
        return creditCardType;
    }
    public void setCreditCardType(Integer type) {
        this.creditCardType = type;
    }

    @Transient public String getCreditCardTypeString() {
        if (creditCardType<1 || creditCardType>cctypes.length) {
            return "";
        }
        return cctypes[creditCardType-1];
    }

    @Column(name="CC_NUM", length=50)
    public String getCreditCard() {
        return creditCard;
    }
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Column(name="CC_MONTH", length=50)
    public int getCreditCardMonth() {
        return ccMonth;
    }
    public void setCreditCardMonth(int ccMonth) {
        this.ccMonth = ccMonth;
    }

    @Column(name="CC_YEAR", length=50)
    public int getCreditCardYear() {
        return ccYear;
    }
    public void setCreditCardYear(int ccYear) {
        this.ccYear = ccYear;
    }

    @Transient
    public String getCreditCardExpiration() {
        return "" + ccMonth + "/" + ccYear;
    }

    @Override
    public String toString() {
        return "Customer#" + getId() + "(" + email + ")";
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy="customer")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}

