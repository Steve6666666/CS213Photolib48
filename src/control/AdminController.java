package control;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Admin;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * control admin Page.
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class AdminController implements LogoutController{
    @FXML
    public ListView<User> userList;

    @FXML
    public Button addUser,logout,delete;

    @FXML
    public TextField username;

    public static ArrayList<User> existUser =new ArrayList<>();

    public static Admin admin = Photos.driver;

    public ObservableList<User> observableList;

    public User selected;

    /**
     * Initiate the state.
     */
    public void open(){
        update();
        delete.setDisable(true);
        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!userList.getSelectionModel().isEmpty()){
                    selected=observableList.get(userList.getSelectionModel().getSelectedIndex());
                    delete.setDisable(false);
                }
            }
        });
    }

    /**
     * admin press adduser button to create new User with name in username field to userlist
     * @param actionEvent add Action
     * @throws IOException
     */
    public void addUser(ActionEvent actionEvent) throws IOException {
        if(!username.getText().isEmpty()){
            boolean exist=false;
            for (User user:existUser) {
                if(user.getName().equals(username.getText())){
                    exist=true;
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid UserName");
                    alert.setHeaderText("Do not enter Duplicated Username!");
                    Optional<ButtonType> Clicked=alert.showAndWait();
                    if(Clicked.get()==ButtonType.OK){
                        alert.close();
                        username.clear();
                    }
                    else{
                        alert.close();
                        username.clear();
                    }
                }
            }
            if(exist==false){
                User newUser=new User(username.getText());
                admin.add(newUser.getName());
                update();
                username.clear();
            }
        }
        else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please enter a valid username!");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
            }
            else{
                alert.close();
            }
        }
        User.save(admin.usersList);

    }

    /**
     * User press logout button to logout and went back to login page
     * @param event ActionEvent
     * @throws IOException
     */
    public void logout(ActionEvent event) throws IOException {
        logOut(event);
    }

    /**
     * Admin user press delete button to delete User from userlist
     * @throws IOException
     */
    public void delete() throws IOException {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Sure you want to delete User:"+selected.getName()+"?");
        Optional<ButtonType> Clicked=alert.showAndWait();
        if(Clicked.get()==ButtonType.OK){
            admin.delete(selected);
            User.save(admin.usersList);
            update();
        }
        else{
            alert.close();
        }
    }

    /**
     * update existed users and show it in listView
     */
    public void update(){
        existUser.clear();
        List<User> adminList=admin.getUsersList();
        for(User user: adminList){
            if(!user.getName().equals("admin")) {
                existUser.add(user);
            }
        }
        /*for (int i = 0; i < adminuser.getUsers().size(); i++) {
            existUser.add(adminuser.getUsers().get(i).getUsername());
        }*/
        userList.refresh();
        observableList = FXCollections.observableList(existUser);
        userList.setItems(observableList);
    }

}
