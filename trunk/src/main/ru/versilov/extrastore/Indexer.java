//$Id: Indexer.java 5454 2007-06-23 22:04:52Z gavin $
package ru.versilov.extrastore;

import java.util.Date;

/**
 * @author Emmanuel Bernard
 */
public interface Indexer
{
   Date getLastIndexingTime();
   void index();
   void stop();
}
