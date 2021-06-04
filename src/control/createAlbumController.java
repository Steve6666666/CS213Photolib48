package control;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Admin;
import model.Album;
import model.User;

import java.beans.EventHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * control photo createAlbum Page
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public class createAlbumController {
    @FXML
    Button create,cancel;
    @FXML
    TextField text;

    static Admin admin=Photos.driver;
    static String curName;

    /**
     * User press create button to create new album
     * @throws IOException
     */
    public void create() throws IOException {
        if(admin.getUser(curName).existAlbum(text.getText())||text.getText().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Name");
            alert.setHeaderText("Duplicated or Invalid Name!");
            Optional<ButtonType> Clicked=alert.showAndWait();
            if(Clicked.get()==ButtonType.OK){
                alert.close();
                text.clear();
            }
            else{
                alert.close();
                text.clear();
            }
        }
        else{
            Album album=new Album(text.getText());
            //System.out.println(admin.getUser(curName).getName());
            admin.getUser(curName).albumList.add(new Album(text.getText()));
            User.save(admin.usersList);
            //System.out.println(admin.getUser(curName).albumList.size());
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Create Album");
            alert.setHeaderText("Successfully created album: "+text.getText());
            alert.show();
            text.clear();
        }
    }

    /**
     * User press cancel button and come back to UserPage
     * @param event cancel action
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void cancel(ActionEvent event) throws IOException, ClassNotFoundException {
        UserController.username=curName;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserPage.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        UserController userController = fxmlLoader.getController();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        userController.open(appStage);
        appStage.setScene(adminScene);
        appStage.show();
    }


}
