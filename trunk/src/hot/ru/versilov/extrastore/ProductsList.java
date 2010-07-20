package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import ru.versilov.extrastore.model.Product;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 18.06.2010
 * Time: 10:36:52
 * To change this template use File | Settings | File Templates.
 */

@Name("productsList")
public class ProductsList extends EntityQuery<Product> {

	private static final String EJBQL = "select product from Product product";

	private static final String[] RESTRICTIONS = {
			"lower(product.description) like lower(concat('%',#{productsList.product.description},'%'))",
			"lower(product.title) like lower(concat('%',#{productsList.product.title},'%'))",};

	private Product product = new Product();

	public ProductsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Product getProduct() {
		return product;
	}

}

