package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import ru.versilov.extrastore.model.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 26.10.2010
 * Time: 12:43:37
 * To change this template use File | Settings | File Templates.
 */
@Name("resourcesList")
public class ResourcesList extends EntityList<Resource> {

    @Override
    protected long getItemId(Resource item) {
        return item.getId();
    }

    @Override
    public String getEJBQLQuery() {
        return "select r from Resource r";
    }
}
