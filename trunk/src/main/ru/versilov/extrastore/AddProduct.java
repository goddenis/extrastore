package ru.versilov.extrastore;

import org.richfaces.event.UploadEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 03.04.2010
 * Time: 1:24:04
 * To change this template use File | Settings | File Templates.
 */


public interface AddProduct {
    Product getProduct();
    void setProduct(Product product);

    List<Category> getSelCategories();
    void setSelCategories(List<Category> selCategories);

    int getUploadsAvailable();
    void setUploadsAvailable(int uploadsAvailable);

    ArrayList<UploadedFile> getFiles();
    void setFiles(ArrayList<UploadedFile> files);

    int getSize();
    long getTimeStamp();

    String clearUploadData();
    void paint(OutputStream stream, Object object) throws IOException;
    void listener(UploadEvent event) throws Exception;

    void save();
    void remove();
}
