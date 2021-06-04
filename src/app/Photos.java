package app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Admin;
import model.Album;
import model.User;

import java.io.IOException;

/**
 * Photos class.
 * @author XiYue Zhang
 * @author ChuXu Song
 *
 */
public class Photos extends Application {
    public static Admin driver = new Admin();
    public Stage mainStage;

    /**
     * initial the whole process
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            mainStage = primaryStage;
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(
                    getClass().getResource("/view/LoginPage.fxml"));
            TitledPane root = loader.load();


            Scene scene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setTitle("PhotoLib");
            mainStage.setScene(scene);
            mainStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Main class to start the application
     * @param args
     * @exception IOException
     * @exception ClassNotFoundException
     *
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
            driver.usersList=User.load();
            launch(args);
    }

}
