/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetesvault.fx.gui;

import com.sun.deploy.ui.ProgressDialog;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author mswin
 */
public class MainGuiController implements Initializable {

    private final Preferences prefs = Preferences.userNodeForPackage(LauncherFx.class);

    @FXML
    private AnchorPane ap;

    @FXML
    private TextField medtronicTextField;
    @FXML
    private CheckBox medtronicCheckBox;

    private static void configureFileChooser(final FileChooser fileChooser,
            File initDir, FileChooser.ExtensionFilter filter) {
        // fileChooser.setTitle("View Pictures");
        if (initDir != null && initDir.exists()) {
            if (initDir.isDirectory()) {
                fileChooser.setInitialDirectory(initDir);
            } else {
                fileChooser.setInitialDirectory(initDir.getParentFile());
            }
        } else {
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home")));
        }

        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                filter,
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    @FXML
    private void handleButtonBrowseMedtronic(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.MEDTRONIC_IMPORT_PATH_KEY, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.CSV_EXTENSION_FILTER);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            medtronicTextField.setText(file.getAbsolutePath());
            medtronicCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseAbbott(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @FXML
    private void handleButtonBrowseGoogleFit(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @FXML
    private void handleButtonBrowseGoogleTracks(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @FXML
    private void handleButtonBrowseRoche(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @FXML
    private void handleButtonBrowseOpenDiabetesVault(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @FXML
    private void handleButtonImport(ActionEvent event) {
//        Dialog<Void> dialog = new Dialog<>();
//        dialog.initModality(Modality.WINDOW_MODAL);
//        dialog.initOwner((Stage) ap.getScene().getWindow());//stage here is the stage of your webview
//        dialog.initStyle(StageStyle.TRANSPARENT);
//        Label loader = new Label("LOADING");
//        loader.setContentDisplay(ContentDisplay.TOP);
//        loader.setGraphic(new ProgressIndicator());
//        dialog.getDialogPane().setGraphic(loader);
//        DropShadow ds = new DropShadow();
//        ds.setOffsetX(1.3);
//        ds.setOffsetY(1.3);
//        ds.setColor(Color.BLACK);
//        dialog.getDialogPane().setEffect(ds);
//        dialog.showAndWait();
//        --> try this http://docs.oracle.com/javafx/2/ui_controls/progress.htm

        // set import Options if import was succesfull
        prefs.putBoolean(Constants.MEDTRONIC_IMPORT_CHECKBOX_KEY, medtronicCheckBox.isSelected());
        prefs.put(Constants.MEDTRONIC_IMPORT_PATH_KEY, medtronicTextField.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        // Medtronic
        File lastPathMedtronic = new File(prefs.get(Constants.MEDTRONIC_IMPORT_PATH_KEY, ""));
        medtronicTextField.setText(lastPathMedtronic.getAbsolutePath());
        medtronicCheckBox.setSelected(prefs.getBoolean(Constants.MEDTRONIC_IMPORT_CHECKBOX_KEY, false));
    }

}
