package model;

import app.Photos;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class of Album Object
 * @author XiYue Zhang
 * @author ChuXu Song
 *
 */
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;

    public String albumName;
    /**
     * Images in this album
     *
     */
    public ArrayList<Image> imageList;


    public Album(String albumName){
        this.albumName=albumName;
        imageList=new ArrayList<>();
    }
    /**
     * Get the earliest date from all photo dates in this album.
     * @return the earliest date
     */
    public String firstDate(){
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        String date="No Date";
        if(!imageList.isEmpty()) {
            Date first=imageList.get(0).getDate();
            for (int i = 0; i < imageList.size(); i++) {
              //  System.out.println(i+"date:"+imageList.get(i).date);
                if (imageList.get(i).date.before(first) ) {
                    first=imageList.get(i).date;
                }
            }
            date=formate.format(first);
        }
        return date;
    }

    /**
     * Get the latest date from all photo dates in this album.
     * @return the latest date
     */
    public String lastDate(){
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        String date="No Date";
        if(!imageList.isEmpty()) {
            Date last=imageList.get(0).getDate();
            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).date.after(last) ) {
                    last=imageList.get(i).date;
                }
            }
            date=formate.format(last);
        }
        return date;

    }

    /**
     * Add a image into the Album.
     * @param image image to be add to the Album
     */
    public void addImage(Image image){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0);
        image.date= calendar.getTime();
        imageList.add(image);
    }
    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Get a image instance by its path
     * @param Path the path of a image
     * @return the image instance
     */
    public Image getImageByPath(String Path){
        for(Image image:imageList){
            if(image.path.equalsIgnoreCase(Path)){
                return image;
            }
        }
        return null;
    }

    /**
     * Remove a image instance by its path
     * @param Path the path of a image
     */
    public void removeImageByPath(String Path){
        Image i=null;
        for(Image image:imageList){
            if(image.path.equalsIgnoreCase(Path)){
                i=image;
            }
        }
        if(i!=null) imageList.remove(i);
    }

    @Override
    public String toString() {
        if(imageList==null){
            return albumName + " ---#Images:0" + " ---No DateRange" ;
        }
        else {
            String dateRange = firstDate() + " to " + lastDate();
            int num = imageList.size();
            return albumName + " ---#Photos: " + num + " ---DateRange:" + dateRange;
        }
    }


}
