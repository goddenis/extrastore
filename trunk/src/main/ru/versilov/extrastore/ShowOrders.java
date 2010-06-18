/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;


public interface ShowOrders {
    public String findOrders();

    public String detailOrder();
    public String cancelOrder();

    public String reset();

    public void destroy();
}
