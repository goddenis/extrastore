package ru.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.io.Serializable;

/**
 * File profile
 * Creator: Catalyst
 * Date: 21.02.11
 * Time: 17:47
 */
@Name("checkout")
@Scope(ScopeType.CONVERSATION)
public class CheckoutAction implements Serializable {
    @In
    Cart cart;
}
