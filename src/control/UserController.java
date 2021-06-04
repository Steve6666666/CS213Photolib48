package control;

import app.Photos;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * Control User Albums Page
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class UserController implements LogoutController{
    @FXML Button create,delete,rename,search,open,logout;
    @FXML ListView albumName;
    @FXML TextField renameText;

    public static String username;
    public ObservableList<Album> obsList;
    public static Admin admin= Photos.driver;
    public static User user;
    public static boolean stock;
    public Album selected;
    public List<Album> albumList;
    public static ArrayList<Album> existAlbum =new ArrayList<>();

    /**
     * initialize the state
     * @param curStage Stage
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void open(Stage curStage) throws IOException, ClassNotFoundException {
        if(admin.getUser(username).albumList==null){
            admin.getUser(username).albumList=new ArrayList<>();
        }
        this.albumList=admin.getUser(username).albumList;
        //System.out.println(albumList.size());
        if(!albumList.isEmpty()){
            update();
        }
        delete.setDisable(true);
        rename.setDisable(true);
        open.setDisable(true);
        albumName.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!albumName.getSelectionModel().isEmpty()){
                    selected=obsList.get(albumName.getSelectionModel().getSelectedIndex());
                    delete.setDisable(false);
                    rename.setDisable(false);
                    open.setDisable(false);
                }
            }
        });
    }

    /**
     * User press create button to open createAlbum page
     * @param event create Action
     * @throws IOException
     */
    public void create(ActionEvent event) throws IOException {
        createAlbumController.curName=username;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/CreateAlbum.fxml"));
        Parent root = fxmlLoader.load();
        createAlbumController create= fxmlLoader.getController();
        Scene createScene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(createScene);
        appStage.show();
    }

    /**
     * User press delete button to delete selected album
     * @throws IOException
     */
    public void delete() throws IOException {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Sure you want to delete Album:"+selected.albumName+"?");
        Optional<ButtonType> Clicked=alert.showAndWait();
        if(Clicked.get()==ButtonType.OK){
            admin.getUser(username).albumList.remove(selected);
            update();
            //User.save(admin.usersList);
            user.save(user.albumList,username);
        }
        else{
            alert.close();
        }
    }

    /**
     * User press rename button to rename selected album with value in renameText field
     * @throws IOException
     */
    public void rename() throws IOException {
        if(renameText.getText().isEmpty() ){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Name");
            alert.setHeaderText("Please enter a name in the field!");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
                renameText.clear();
            }
            else{
                alert.close();
                renameText.clear();
            }
        }
        else if(admin.getUser(username).existAlbum(renameText.getText())) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Duplicated Name");
            alert.setHeaderText("Duplicated Name!");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
                renameText.clear();
            }
            else{
                alert.close();
                renameText.clear();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Rename");
            alert.setHeaderText("Sure you want to Rename Album:" + selected.albumName + "?");
            Optional<ButtonType> Clicked = alert.showAndWait();
            if (Clicked.get() == ButtonType.OK) {
                alert.close();
                Album album=admin.getUser(username).getAlbum(selected.albumName);
                album.setAlbumName(renameText.getText());
                update();
               //
                User.save(admin.usersList);
                renameText.clear();
            } else {
                alert.close();
                renameText.clear();
            }
        }
    }

    /**
     * User press search button to open searching page
     * @param event ActionEvent
     * @throws IOException
     */
    public void search(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SearchingPage.fxml"));
        searchController.userName=username;
        Parent root = fxmlLoader.load();
        searchController sc = fxmlLoader.getController();
        Scene searchScene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(searchScene);
        appStage.show();
    }

    /**
     * User press open button to go to photoPage
     * @param event ActionEvent
     * @throws IOException
     */
    public void open(ActionEvent event) throws IOException {
        PhotoController.curAlbum=selected;
        PhotoController.userName=username;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PhotoPage.fxml"));
        Parent root = fxmlLoader.load();
        PhotoController photoController = fxmlLoader.getController();
        Scene photoScene = new Scene(root);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        photoController.open();
        appStage.setScene(photoScene);
        appStage.show();
    }

    /**
     * User press logout button to go to login page
     * @param event logout action
     * @throws IOException
     */
    public void logout(ActionEvent event) throws IOException {
        logOut(event);
    }

    /**
     * update the existAlbum list
     */
    public void update(){
        user=admin.getUser(username);
        existAlbum.clear();
        for (Album album:albumList) {
            existAlbum.add(album);
        }
        obsList= FXCollections.observableList(existAlbum);
        albumName.setItems(obsList);
    }


}
