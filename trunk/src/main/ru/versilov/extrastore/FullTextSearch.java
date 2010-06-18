//$Id: FullTextSearch.java 7028 2008-01-04 21:58:25Z nrichards $
package ru.versilov.extrastore;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Emmanuel Bernard
 */
public interface FullTextSearch
{
   public String getSearchQuery();
   public void setSearchQuery(String searchQuery);

   public Long getSelectedId();
   public void setSelectedId(Long id);


   public int getNumberOfResults();
   public boolean isLastPage();
   public boolean isFirstPage();
   public void nextPage();
   public void prevPage();

   public String doSearch();
   public void selectFromRequest();
   public void addToCart();
   public void addAllToCart();
   public void paint(OutputStream stream, Object object) throws IOException;


   public int getPageSize();
   public void setPageSize(int pageSize);
  
   public void reset();
   public void destroy();
}
