package ru.versilov.extrastore.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 10.07.2010
 * Time: 14:31:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="OPS")
public class OPS {
    int index;
    String opsname;
    String opstype;
    String opssubm;
    String region;
    String autonom;
    String area;
    String city;
    String city_1;
    Date actdate;
    String indexold;

    @Id
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getOpsname() {
        return opsname;
    }

    public void setOpsname(String opsname) {
        this.opsname = opsname;
    }

    public String getOpstype() {
        return opstype;
    }

    public void setOpstype(String opstype) {
        this.opstype = opstype;
    }

    public String getOpssubm() {
        return opssubm;
    }

    public void setOpssubm(String opssubm) {
        this.opssubm = opssubm;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAutonom() {
        return autonom;
    }

    public void setAutonom(String autonom) {
        this.autonom = autonom;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_1() {
        return city_1;
    }

    public void setCity_1(String city_1) {
        this.city_1 = city_1;
    }

    public Date getActdate() {
        return actdate;
    }

    public void setActdate(Date actdate) {
        this.actdate = actdate;
    }

    public String getIndexold() {
        return indexold;
    }

    public void setIndexold(String indexold) {
        this.indexold = indexold;
    }
}
