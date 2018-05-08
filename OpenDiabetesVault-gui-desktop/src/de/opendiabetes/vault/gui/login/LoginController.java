/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.opendiabetes.vault.gui.login;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import java.util.prefs.Preferences;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import de.opendiabetes.vault.gui.launcher.FatherController;
import de.opendiabetes.vault.gui.launcher.MainWindowController;

/**
 * FXML Controller class
 *
 * @author kai
 */
public class LoginController extends FatherController implements Initializable {

    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Rectangle passwordRectangle;
    @FXML
    private Label passwordLabel;
    @FXML
    private Rectangle passwordBox;
    Preferences pref = Preferences.userNodeForPackage(InputPasswordController.class);

    private ResourceBundle myResource;

    private final double defaultPositionX = 350;

    private final double defaultPositionY = 350;

    private final double defaultSizeMultiplier = 0.6;

    private final Color c = Color.web("ec4c4c");

    /**
     * Checks if the password is correct.
     *
     *
     * @param action ActionEvent: when triggered calls method
     * @throws java.security.NoSuchAlgorithmException if the used hashfunction
     * wasnt found
     * @throws java.io.IOException if the fxml file or the ResourceBundle wasnt
     * found
     *
     */
    @FXML
    private void passwordLogin(ActionEvent action) throws NoSuchAlgorithmException, IOException {
        if (!(password == null) && (!password.getText().isEmpty())) {
            MessageDigest hashFunction = MessageDigest.getInstance("SHA-256");
            hashFunction.update(password.getText().getBytes("UTF-8"));
            // saves password hash as hex in a string
            String hash = String.format("%01x",
                    new BigInteger(1, hashFunction.digest()));
            // if password correct
            if (hash.equals(pref.get("properties", ""))) {
                // close window
                login.getScene().getWindow().hide();
                // open main window
                openMainWindow(MAIN_PAGE);

                // change color and text if the password was wrong
            } else {

                passwordRectangle.setFill(c);
                passwordRectangle.setStroke(c);

                passwordLabel.setText(myResource.getString("login.wrongPasswordText"));
                passwordLabel.setTextFill(c);
                passwordBox.setStroke(c);

                password.clear();

            }

        } else {

            passwordRectangle.setFill(c);
            passwordRectangle.setStroke(c);

            passwordLabel.setText(myResource.getString("login.enterPasswordText"));
            passwordLabel.setTextFill(c);
            passwordBox.setStroke(c);

        }
    }

    /**
     * This method fires the loginButton and is assigned to the whole pane.
     *
     *
     * @param e KeyEvent which calls this function
     *
     */
    @FXML
    private void passwordLoginKey(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            login.fire();

        }

    }

    /**
     * Initializes the controller class.
     *
     * @param location the url of OptionsWindow.fxml
     * @param resources the passed ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myResource = resources;
    }

    /**
     * This method opens the main window of the application. The controller will
     * be saved in the FatherController. The size and position of the window are
     * determined by the values saved in the java preferences.
     *
     * @param path url of the fxml file
     * @throws java.io.IOException if the fxml file or the ResourceBundle wasnt
     * found
     *
     */
    private void openMainWindow(final String path) throws IOException {
        URL url = getClass().getResource(path);
        FXMLLoader loader = new FXMLLoader(url,
                myResource);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        setPreferences(MAIN_STAGE);
        setMainWindowController((MainWindowController) loader.getController());
        MAIN_STAGE.getIcons().add(new Image(getClass().getResource(ICON).toExternalForm()));
        MAIN_STAGE.setScene(scene);

        MAIN_STAGE.show();

        savePreferencesListener(MAIN_STAGE);
    }

    /**
     * Sets the position and size of a stage to the values saved as
     * preferences. The keys are windowWidth, windowHeight,
     * windowPositionX and windowPositionY. If this stage closes the whole
     * appliaction closes too.
     *
     *
     * @param stage the stage, which size and position should be set
     */
    private void setPreferences(final Stage stage) {

        stage.setMinHeight((SCREEN_BOUNDS.getHeight()) * defaultSizeMultiplier);
        stage.setMinWidth((SCREEN_BOUNDS.getWidth()) * defaultSizeMultiplier);
        stage.setMaxHeight(SCREEN_BOUNDS.getHeight());
        stage.setMaxWidth(SCREEN_BOUNDS.getWidth());
        stage.setX(PREFS_FOR_ALL.getDouble("windowPositionX",
                defaultPositionX));
        stage.setY(PREFS_FOR_ALL.getDouble("windowPositionY",
                defaultPositionY));
        // if the window wasnt on fullscreen when it closed
        if (!PREFS_FOR_ALL.getBoolean("fullScreen", false) && !PREFS_FOR_ALL.getBoolean("maximized", false)) {
            Double width = PREFS_FOR_ALL.getDouble("windowWidth", 0.0);
            Double height = PREFS_FOR_ALL.getDouble("windowHeight", 0.0);

            // if the saved size is between minSize and maxSize
            if (width >= stage.getMinWidth()
                    && height >= stage.getMinHeight()
                    && height <= stage.getMaxWidth()
                    && width >= stage.getMinHeight()) {
                stage.setWidth(width);
                stage.setHeight(height);

                // set the size to default size
            } else {
                stage.setWidth(stage.getMinWidth());
                stage.setHeight(stage.getMinHeight());

            }

        } else {
            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());
            if (PREFS_FOR_ALL.getBoolean("fullScreen", false)) {
                stage.setFullScreen(true);

            } else if ((PREFS_FOR_ALL.getBoolean("maximized", false))) {
                stage.setMaximized(true);
            }

        }

    }

    /**
     * Saves the position and size of a stage as preferences,
     * when the stage is closing. The keys are windowWidth, windowHeight,
     * windowPositionX and windowPositionY. If this stage closes the whole
     * appliaction closes too.
     *
     * @param stage the stage that will be closed
     */
    private void savePreferencesListener(final Stage stage) {
        stage.setOnCloseRequest((WindowEvent e) -> {

            PREFS_FOR_ALL.putDouble("windowPositionX", stage.getX());
            PREFS_FOR_ALL.putDouble("windowPositionY", stage.getY());

            PREFS_FOR_ALL.putBoolean("fullScreen", getPrimaryStage().isFullScreen());
            PREFS_FOR_ALL.putBoolean("maximized", getPrimaryStage().isMaximized() && !getPrimaryStage().isFullScreen());
            PREFS_FOR_ALL.putDouble("windowWidth", stage.getWidth());
            PREFS_FOR_ALL.putDouble("windowHeight", stage.getHeight());

            Platform.exit();
        });
    }

}
