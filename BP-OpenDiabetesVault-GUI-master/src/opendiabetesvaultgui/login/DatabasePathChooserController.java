/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.login;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.filechooser.FileSystemView;
import opendiabetesvaultgui.launcher.FatherController;
import static opendiabetesvaultgui.launcher.FatherController.ICON;
import static opendiabetesvaultgui.launcher.FatherController.LANGUAGE_DISPLAY;
import static opendiabetesvaultgui.launcher.FatherController.MAIN_PAGE;
import static opendiabetesvaultgui.launcher.FatherController.MAIN_STAGE;
import static opendiabetesvaultgui.launcher.FatherController.PREFS_FOR_ALL;
import static opendiabetesvaultgui.launcher.FatherController.RESOURCE_PATH;
import static opendiabetesvaultgui.launcher.FatherController.getPrimaryStage;
import static opendiabetesvaultgui.launcher.FatherController.setMainWindowController;
import opendiabetesvaultgui.launcher.MainWindowController;

/**
 * FXML Controller class.
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class DatabasePathChooserController extends FatherController
        implements Initializable {

    @FXML
    private Rectangle pathBox;
    @FXML
    private Rectangle pathRectangle;
    @FXML
    private Label pathLabel;
    @FXML
    private TextField pathField;
    @FXML
    private Button choosePathButton;
    @FXML
    private Button acceptButton;

    private String path;
    private final double defaultPositionX = 350;

    private final double defaultPositionY = 350;

    private final double defaultSizeMultiplier = 0.6;

    /**
     * Initializes the controller class.
     *
     * @param url the url of the dataBasePathChooser
     * @param rb the passed ResourceBundle
     */
    @Override
    public final void initialize(final URL url, final ResourceBundle rb) {
        pathField.setText(FileSystemView.getFileSystemView().
                getDefaultDirectory().getPath() + File.separator + "ODV"
                + File.separator + "database");
        PREFS_FOR_ALL.putBoolean("databaseInMemory", false);
    }

    /**
     * Not implemented yet.
     *
     * @param event calls method when triggered
     */
    @FXML
    private void listenToText(final ActionEvent event) {
    }
    
    /**
     * This method fires the acceptButton and is assigned to the TextField.
     *
     *
     * @param e KeyEvent which calls this function
     *
     */
    @FXML
    private void databaseAcceptKey(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            acceptButton.fire();

        }

    }

    /**
     * Choses and saves a new path.
     *
     * @param event the event to trigger this method
     */
    @FXML
    private void choosePath(final ActionEvent event) {
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().
                getWindow();

        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory != null) {
            path = selectedDirectory.getAbsolutePath();
            pathField.setText(path);
        }
        //else do nothing
    }

    /**
     * Saves the database path as a preference and opens the main window.
     * @param event calls method when triggered
     * @throws IOException if  the  MainWindow.fxml or the passed ResourceBundle wasnt found
     */
    @FXML
    private void acceptChanges(final ActionEvent event) throws IOException {
        PREFS_FOR_ALL.put("pathDatabase", pathField.getText());
        acceptButton.getScene().getWindow().hide();
        openMainWindow(getClass().getResource(MAIN_PAGE));
    }

    private void openMainWindow(final URL path) throws IOException {

        FXMLLoader loader = new FXMLLoader(path,
                ResourceBundle.getBundle(RESOURCE_PATH, new Locale(
                        PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, ""))));
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
        if (!PREFS_FOR_ALL.getBoolean("fullScreen", false)) {
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

        } // if the window was on fullscreen
        else {
            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());
            stage.setFullScreen(true);

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

            if (getPrimaryStage().isFullScreen()) {
                PREFS_FOR_ALL.putBoolean("fullScreen", true);
            } else {
                PREFS_FOR_ALL.putDouble("windowWidth", stage.getWidth());
                PREFS_FOR_ALL.putDouble("windowHeight", stage.getHeight());
                PREFS_FOR_ALL.putBoolean("fullScreen", false);

            }

            Platform.exit();
        });
    }

}
