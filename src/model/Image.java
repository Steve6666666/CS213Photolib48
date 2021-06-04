package model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;

/**
 * Class of Image Object
 * @author XiYue Zhang
 * @author ChuXu Song
 *
 */
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;


    public Date date;
    public Calendar calendar;

    public File file;
    /**
     * Image path absolute path in computer
     */
    public String path;

    /**
     *  Image tag list
     */
    public List<Tag> tags;
    public String caption;
    public boolean isStock=false;


    public void setFile(File file) {
        this.file = file;
    }

    public Date getDate() {
        return date;
    }

    public File getFile() {
        return file;
    }


    /**
     * determine whether image have some certain tag name
     * @param name tar key User input
     * @return true if image have this input tag name
     */
    public boolean hasTag(String name){
        for (Tag t:tags) {
            if(t.name.equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public List<Tag> getTags() {
        return tags;
    }



    public Image(File file, String path){
        this.path=path;
        if (file != null) this.file=new File(path);
        else this.file=file;
        this.tags= new ArrayList<Tag>();
        calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0);
        this.date = calendar.getTime();

    }
}
