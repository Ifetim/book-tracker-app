// URL For git:  https://csgit.ucalgary.ca/amelia.custodio/cpsc219project
// Amelia Custodio, Sarah Giesbrecht, Gray Wilson, Jessie Estrada, Ife Timothy / April 24, 2025 / T10

package booktracker;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Gets command line arguments, launches stage, code asking to save when the user clicks the "x" button
 */
public class BookTrackerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Getting the command line arguments
        String[] args = getParameters().getRaw().toArray(new String[0]);
        // Loads the fxml
        FXMLLoader fxmlLoader = new FXMLLoader(BookTrackerApplication.class.getResource("book-tracker-view.fxml"));
        // Creates the scene
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        BookTrackerController controller = fxmlLoader.getController();
        // Adds the command line arguments to the controller
        controller.commandLineData(args);
        // Displays scene
        stage.setTitle("Welcome to Book Tracker!");
        stage.setScene(scene);
        stage.show();

        // When the x button is clicked, a popup appears asking the user if they want to save first
        stage.setOnCloseRequest(event -> {
            // Set the alert popup
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to save first?\nIf you have already saved your data, click Exit.");

            // Set the buttons
            ButtonType saveButton = new ButtonType("Save");
            ButtonType exitButton = new ButtonType("Exit");
            ButtonType cancelButton = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(saveButton, exitButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();
            // If the save button was clicked, the program saves the data and exits
            if (result.get() == saveButton) {
                controller.onSaveClick(new ActionEvent());
            // If the exit button was clicked, the user has already saved their data and the program safely exits
            } else if (result.get() == exitButton) {
                stage.close();
            // If the cancel button was clicked, the popup closes
            } else if (result.get() == cancelButton) {
                event.consume();
            }
        });
    }

    /**
     * Launches the gui with command line arguments
     * @param args - String[], command line arguments
     */

    public static void main (String[] args) {
        launch(args);
    }
}