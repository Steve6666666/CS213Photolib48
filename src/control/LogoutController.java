package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * A interface that logs the user out of the program and redirects them to the login screen
 * @author Xiyue Zhang
 * @author Chuxu Song
 */
public interface LogoutController {
    /**
     * A helper method that logs the user out of the program and redirects them to the login screen
     * @param e log out action
     * @throws IOException
     */
   default void logOut(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setContentText("Sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
            Parent sceneManager = fxmlLoader.load();
            Scene adminScene = new Scene(sceneManager);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.setScene(adminScene);
            appStage.show();
        } else {
            return;
        }

    }

}
