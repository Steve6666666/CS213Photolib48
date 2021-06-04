package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class of User Object
 * @author XiYue Zhang
 * @author ChuXu Song
 *
 */
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "data";
    public static final String storeFile = "Albums.txt";
    public static final String storeFile2="Photos.txt";
    /**
     * List of albums for this user
     *
     */
    public List<Album> albumList=new ArrayList<>();
    String name;

    public User(){ }
    public User(String name){
        this.name=name;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }


    /**
     * Determine the user has this album or not.
     * @param name album name
     * @return true if the album exists, false otherwise
     */
    public boolean existAlbum(String name){
        if(albumList.isEmpty())
            return false;
        else {
            for (Album a : albumList) {
                if (a.albumName.equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }


    public Album getAlbum(String name){
        for (Album a:albumList) {
            if(a.albumName.equals(name)){
                return a;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Save all users' information into Albums.txt file.
     * @param pdApp current user list
     * @throws IOException
     */
    public static void save(List<User> pdApp) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(pdApp);
        oos.close();
    }
    /**
     * Saves album information into "username"+Photos.txt file
     * @param pdApp current album list
     * @param name user name
     * @throws IOException
     */
    public void save(List<Album> pdApp,String name) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator +name+ storeFile2));
        oos.writeObject(pdApp);
        oos.close();
    }

    /**
     * Loads "username"+Photos.txt file
     * @param name the name of user for this album
     * @return List of albums from this user
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<Album> load(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + name+ storeFile2));
        List<Album> albumList = (List<Album>) ois.readObject();
        ois.close();
        return albumList;
    }

    /**
     * Loads Albums.txt file
     * @return list of users
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<User> load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
        List<User> userList=(List<User>)ois.readObject();
        ois.close();
        return userList;
    }
}
