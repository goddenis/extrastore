package ru.versilov.extrastore;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;
import ru.versilov.extrastore.model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 20.07.2010
 * Time: 1:38:08
 * To change this template use File | Settings | File Templates.
 */
@Name("usersList")
public class UsersList {

    @In("entityManager")
    private EntityManager em;

    private ExtendedTableDataModel<User> usersDataModel;

    private Selection selection = new SimpleSelection();
    private List<User> selectedUsers = new ArrayList<User>();
    

    private List<User> resultList;

    public List<User> getResultList() {
        if (resultList == null) {
            resultList =  em.createQuery("select u from User u").getResultList();
        }
        return resultList;
    }


    
    public ExtendedTableDataModel<User> getUsersDataModel() {
     		if (usersDataModel == null) {
			usersDataModel = new ExtendedTableDataModel<User>(new DataProvider<User>(){

				private static final long serialVersionUID = 505408782103164847L;

				public User getItemByKey(Object key) {
					for(User u : getResultList()){
						if (key.equals(getKey(u))){
							return u;
						}
					}
					return null;
				}

				public List<User> getItemsByRange(int firstRow, int endRow) {
					return getResultList().subList(firstRow, endRow);
				}

				public Object getKey(User item) {
					return item.getId();
				}

				public int getRowCount() {
					return getResultList().size();
				}

			});
		}
		return usersDataModel;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }
    
    protected void takeSelection() {
    	selectedUsers.clear();
		Iterator<Object> iterator = getSelection().getKeys();
		while (iterator.hasNext()) {
			Object key = iterator.next();
            usersDataModel.setRowKey(key);
            if (usersDataModel.isRowAvailable()) {
                User u = usersDataModel.getRowData();
			    selectedUsers.add(u);
            }
		}    
    }

    public void removeSelection() {
        takeSelection();
        for (User u : selectedUsers) {
            em.remove(u);
        }
    }
}
