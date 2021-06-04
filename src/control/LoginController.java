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
import model.Admin;
import model.Album;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Control user login Page
 * @author Xiyue Zhang
 * @author Chuxu Song
 */

public class LoginController {
    @FXML TextField username;
    @FXML Button login;

    public final String admin="admin";
    public static Admin driver= Photos.driver;

    /**
     * User  press login button and login with Username
     * @param event login Action
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void login(ActionEvent event) throws IOException, ClassNotFoundException {
        String name = username.getText().trim();
        if(name.equals("admin")){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AdminUser.fxml"));
            Parent root = fxmlLoader.load();
            AdminController adminController = fxmlLoader.getController();
            Scene adminScene = new Scene(root);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            adminController.open();
            appStage.setScene(adminScene);
            appStage.show();
        }
        else if(driver.exist(name)){
            driver.cur=driver.getUser(name);
            try {
                driver.getUser(name).albumList = driver.cur.load(name);
            }
            catch (IOException e){
            }
            UserController.username = name;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserPage.fxml"));
            Parent sceneManager = fxmlLoader.load();
            UserController userController = fxmlLoader.getController();
            Scene userScene = new Scene(sceneManager);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            userController.open(appStage);
            appStage.setScene(userScene);
            appStage.show();
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
    }


}
