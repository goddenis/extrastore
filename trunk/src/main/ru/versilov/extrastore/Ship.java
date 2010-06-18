/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;

public interface Ship {
    public String getTrack();
    public void   setTrack(String track);

    public String ship();
    public String viewTask();   

    public void   destroy();
}
