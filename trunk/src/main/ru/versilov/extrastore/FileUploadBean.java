package ru.versilov.extrastore;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 04.04.2010
 * Time: 2:17:19
 * To change this template use File | Settings | File Templates.
 */
/**
 * @author Ilya Shaikovsky
 *
 */
@Scope(ScopeType.SESSION)
@Name("fileUploadBean")
public class FileUploadBean{

        private ArrayList<UploadedFile> files = new ArrayList<UploadedFile>();
        private int uploadsAvailable = 5;
        private boolean autoUpload = false;
        public int getSize() {
                if (getFiles().size()>0){
                        return getFiles().size();
                }else
                {
                        return 0;
                }
        }

        public FileUploadBean() {
        }

        public synchronized void paint(OutputStream stream, Object object) throws IOException {
                stream.write(getFiles().get((Integer)object).getData());
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
                setUploadsAvailable(5);
                return null;
        }

        public long getTimeStamp(){
                return System.currentTimeMillis();
        }

        public ArrayList<UploadedFile> getFiles() {
                return files;
        }

        public void setFiles(ArrayList<UploadedFile> files) {
                this.files = files;
        }

        public int getUploadsAvailable() {
                return uploadsAvailable;
        }

        public void setUploadsAvailable(int uploadsAvailable) {
                this.uploadsAvailable = uploadsAvailable;
        }

        public boolean isAutoUpload() {
                return autoUpload;
        }

        public void setAutoUpload(boolean autoUpload) {
                this.autoUpload = autoUpload;
        }

}