/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;

public interface Checkout
{
    public int getAuthType();
    public void setAuthType(int authType);

    public void createOrder();
    public String auth();
    public void address();
    public void pay();
    public Order submitOrder();

    public void destroy();
}
