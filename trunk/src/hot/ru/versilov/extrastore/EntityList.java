package ru.versilov.extrastore;

import org.jboss.seam.annotations.In;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * T: Catalyst
 * Date: 21.07.2010
 * Time: 0:27:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class EntityList<T> {
    @In("entityManager")
    private EntityManager em;

    private ExtendedTableDataModel<T> dataModel;

    private Selection selection = new SimpleSelection();
    private List<T> selectedEntities = new ArrayList<T>();


    private List<T> resultList;

    protected void invalidateResultList() {
        resultList = null;
        dataModel = null;
    }


    public List<T> getResultList() {
        if (resultList == null) {
            em.clear();
            resultList =  em.createQuery(getEJBQLQuery()).getResultList();
        }
        return resultList;
    }


    public EntityManager getEntityManager() {
        return this.em;
    }


    public ExtendedTableDataModel<T> getDataModel() {
     		if (dataModel == null) {
			    dataModel = new ExtendedTableDataModel<T>(new DataProvider<T>(){


				public T getItemByKey(Object key) {
					for(T u : getResultList()){
						if (key.equals(getKey(u))){
							return u;
						}
					}
					return null;
				}

				public List<T> getItemsByRange(int firstRow, int endRow) {
					return getResultList().subList(firstRow, endRow);
				}

				public Object getKey(T item) {
					return getItemId(item);
				}

				public int getRowCount() {
					return getResultList().size();
				}

			});
		}
		return dataModel;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }


    protected List<T> getSelectedEntities() {
        return selectedEntities;
    }

    protected void takeSelection() {
    	selectedEntities.clear();
		Iterator<Object> iterator = getSelection().getKeys();
		while (iterator.hasNext()) {
			Object key = iterator.next();
            dataModel.setRowKey(key);
            if (dataModel.isRowAvailable()) {
                T t = dataModel.getRowData();
			    selectedEntities.add(t);
            }
		}
    }

    public void removeSelection() {
        takeSelection();
        for (T u : selectedEntities) {
            em.remove(u);
        }
        invalidateResultList();
    }

    protected abstract long getItemId(T item);
    public abstract String getEJBQLQuery();
}
