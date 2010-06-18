package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 02.04.2010
 * Time: 2:22:48
 * To change this template use File | Settings | File Templates.
 */

@Stateful
@Scope(ScopeType.SESSION)
@Name("addProduct")
public class AddProductAction implements AddProduct {

    @PersistenceContext
    EntityManager em;

    Product product = new Product();

    List<Category> selCategories;

    ArrayList<UploadedFile> files = new ArrayList<UploadedFile>();
    int uploadsAvailable = 9;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Category> getSelCategories() {
        return selCategories;
    }

    public void setSelCategories(List<Category> selCategories) {
        this.selCategories = selCategories;
    }

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public ArrayList<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadedFile> files) {
        this.files = files;
    }

    public int getSize() {
        if (getFiles().size()>0){
            return getFiles().size();
        } else                {
            return 0;
        }
    }

    public long getTimeStamp(){
        return System.currentTimeMillis();
    }


    public synchronized void paint(OutputStream stream, Object object) throws IOException {
        if (object instanceof Integer) {
            stream.write(getFiles().get((Integer)object).getData());
        } else if (object instanceof Product) {
            stream.write(((Product)object).getImage());
        }
    }

    public synchronized void listener(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        UploadedFile file = new UploadedFile();
        file.setLength(item.getData().length);
        file.setName(item.getFileName());
        file.setData(item.getData());
        files.add(file);
        uploadsAvailable--;
    }

    public String clearUploadData() {
        files.clear();
        setUploadsAvailable(9);
        return null;
    }

    public void save() {
        System.out.println("Добавлен продукт: " + product.title);
        Set<Category> cats = new HashSet<Category>();
        for(Category cat: selCategories) {
            cats.add(cat);
        }
        product.setCategories(cats);
        em.persist(product);

        // Make an empty inventory — as the product is new, we assume it's zero quantity in the warehouse
        Inventory inv = new Inventory();
        inv.setProduct(product);
        inv.setQuantity(0);
        inv.setSales(0);
        em.persist(inv);

        if (files.size() > 0) {
            // Create file
            try {
                FileOutputStream fos = new FileOutputStream("/proj/extrastore/out/extrastore.ear/extrastore.war/img/" + product.getProductId() + ".gif");
                fos.write(files.get(0).getData());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            product.setImageURL("/img/" + product.getProductId() + ".gif");
            em.merge(product);
        }


        this.reset();
    }

    @End
    public void reset() {
        clearUploadData();
        product = new Product();
        selCategories = null;
    }

    @Remove
    public void remove() {
        
    }
}
