package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The admin class that controls adding,deleting,editing users
 * @author XiYue Zhang
 * @author ChuXu Song
 *
 */
public class Admin implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "data";
    public static final String storeFile = "users.txt";

    /**
     * List of users in the app
     */
    public List<User> usersList;
    /**
     * current user
     */
    public User cur;
    public boolean loggedIn;

    public Admin(){
        usersList=new ArrayList<User>();
        usersList.add(new User("admin"));
        this.cur=null;
        this.loggedIn=false;
    }
    /**
     * Add a user in the Admin.
     * @param username the username of the user to be added
     */
    public void add(String username){
        usersList.add(new User(username));
    }

    /**
     * Delete a user in the Admin.
     * @param user the user to be deleted
     */
    public void delete(User user) {
        usersList.remove(user);
       // System.out.println(usersList);
    }


    public User getUser(String name){
        for(User user:usersList){
            if(user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }

    public void setCur(User cur) {
        this.cur = cur;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    /**
     * Determine a user exists or not by username.
     * @param name the username of the user to be searched
     * @return true if exists, false otherwise
     */
    public boolean exist(String name) {
        for(int i = 0; i < usersList.size(); i++) {
            if(usersList.get(i).getName().equals(name)) {
                this.setCur(usersList.get(i));
                this.loggedIn = true;
                return true;
            }
        }
            return false;
    }

    /**
     * Save the information for current users.
     * @param adminApp the admin user
     * @exception IOException
     */
    public static void save(Admin adminApp) throws IOException {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
            oos.writeObject(adminApp);
//            for(User user:adminApp.usersList){
//                for (Album a:user.albumList) {
//                    oos.writeObject(a);
//                    for (Image i:a.imageList) {
//                        oos.writeObject(i);
//                    }
//                }
//            }
            oos.close();
            oos.flush();
    }
}
