package ru.versilov.extrastore;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 05.12.2009
 * Time: 1:09:18
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface Browse {
    Integer getCategoryId();
    void setCategoryId(Integer categoryId);
    List<Product> getProducts();
}
