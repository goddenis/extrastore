package ru.versilov.extrastore;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import ru.versilov.extrastore.model.Order;
import ru.versilov.extrastore.model.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 26.10.2010
 * Time: 23:29:40
 * To change this template use File | Settings | File Templates.
 */
@Name("resourcesHome")
public class ResourcesHome extends EntityHome<Resource> {

    public void setResourceId(Long id) {
        this.setId(id);
    }

    public Long getResourceId() {
        return (Long)getId();
    }

    @Override
    protected Resource createInstance() {
        Resource resource = new Resource();
        return resource;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }
}
