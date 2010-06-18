package ru.versilov.extrastore;

import javax.ejb.Local;

@Local
public interface Authenticator
{
  boolean authenticate();
}
