package control;

import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * control photo Move Page
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class PhotoMoveController {
    @FXML Button move,cancel;
    @FXML TextField destination;
    /**
     * current album
     */
    public static Album curAlbum;
    public static String username;
    /**
     * current User
     */
    public User user= Photos.driver.getUser(username);
    public static String path;

    /**
     * User press move button and move to other album
     * @throws IOException
     */
    public void move() throws IOException {
        if(destination.getText().isEmpty()||!user.albumList.contains(user.getAlbum(destination.getText()))||destination.getText().equals(curAlbum.albumName)){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Destination");
            alert.setHeaderText("Please enter a valid Album Name!");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
                destination.clear();
            }
            else{
                alert.close();
                destination.clear();
            }
        }
        else if (user.getAlbum(destination.getText()).getImageByPath(path)!=null){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Duplicated");
            alert.setHeaderText("Photo already exists in destination!");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
                destination.clear();
                return;
            }
            else{
                alert.close();
                destination.clear();
                return;
            }
        }
        else{
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conform Moving");
            alert.setHeaderText("Sure you want to move Photo?");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
            }
            else{
                alert.close();
            }
            user.getAlbum(destination.getText()).addImage(curAlbum.getImageByPath(path));
            curAlbum.removeImageByPath(path);
            destination.clear();
        }
        user.save(user.albumList,username);
    }

    /**
     * User press cancel button to come back photoPage
     * @param event cancel action
     * @throws IOException
     */
    public void cancel(ActionEvent event) throws IOException {
        PhotoController.curAlbum=curAlbum;
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

}
