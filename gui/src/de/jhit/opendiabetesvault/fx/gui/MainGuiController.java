/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetesvault.fx.gui;

import de.jhit.openmediavault.app.data.CarelinkCsvImporter;
import de.jhit.openmediavault.app.data.GoogleFitCsvImporter;
import de.jhit.openmediavault.app.data.LibreTxtImporter;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author mswin
 */
public class MainGuiController implements Initializable {

    private final Preferences prefs = Preferences.userNodeForPackage(LauncherFx.class);

    @FXML
    private AnchorPane ap;

    // Import
    @FXML
    private TextField medtronicTextField;
    @FXML
    private CheckBox medtronicCheckBox;
    @FXML
    private TextField abbottTextField;
    @FXML
    private CheckBox abbottCheckBox;
    @FXML
    private TextField googleFitTextField;
    @FXML
    private CheckBox googleFitCheckBox;
    @FXML
    private TextField googleTracksTextField;
    @FXML
    private CheckBox googleTracksCheckBox;
    @FXML
    private TextField rocheTextField;
    @FXML
    private CheckBox rocheCheckBox;
    @FXML
    private TextField odvTextField;
    @FXML
    private CheckBox odvCheckBox;
    @FXML
    private DatePicker periodFromPicker;
    @FXML
    private DatePicker periodToPicker;
    @FXML
    private CheckBox periodCheckbox;

    // Export
    // Interpreter
    @FXML
    private CheckBox fillAsNewKathederCheckbox;
    @FXML
    private Spinner<Integer> cooldownTimeSpinner;

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

    private static boolean checkIfFileExists(String path) {
        File checkPath = new File(path);
        return checkPath.exists() && checkPath.isFile() && checkPath.canRead();
    }

    // #########################################################################################
    // Import
    // #########################################################################################
    @FXML
    private void handleButtonBrowseMedtronic(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_KEY, ""));

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
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_ABBOTT_IMPORT_PATH_KEY, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.TXT_EXTENSION_FILTER);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            abbottTextField.setText(file.getAbsolutePath());
            abbottCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseGoogleFit(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File initDir = new File(prefs.get(Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_KEY, ""));

        DirectoryChooser chooser = new DirectoryChooser();
        if (initDir.exists()) {
            if (initDir.isDirectory()) {
                chooser.setInitialDirectory(initDir);
            } else {
                chooser.setInitialDirectory(initDir.getParentFile());
            }
        } else {
            chooser.setInitialDirectory(
                    new File(System.getProperty("user.home")));
        }
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory != null) {
            googleFitTextField.setText(selectedDirectory.getAbsolutePath());
            googleFitCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseGoogleTracks(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_KEY, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.JSON_EXTENSION_FILTER);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            googleTracksTextField.setText(file.getAbsolutePath());
            googleTracksCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseRoche(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_ROCHE_IMPORT_PATH_KEY, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.CSV_EXTENSION_FILTER);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            rocheTextField.setText(file.getAbsolutePath());
            rocheCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseOpenDiabetesVault(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_ODV_IMPORT_PATH_KEY, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.CSV_EXTENSION_FILTER);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            odvTextField.setText(file.getAbsolutePath());
            odvCheckBox.setSelected(true);
        }
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
        // check if sth is selected
        if (!medtronicCheckBox.isSelected()
                && !abbottCheckBox.isSelected()
                && !googleFitCheckBox.isSelected()
                && !googleTracksCheckBox.isSelected()
                && !rocheCheckBox.isSelected()
                && !odvCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "No data selected.\nPlease select a dataset and try again.",
                    ButtonType.CLOSE);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        Task bgTask = new Task() {
            @Override
            protected Void call() throws Exception {
                // set wait cursor
                Cursor cursorBackup = ap.getScene().getCursor();
                ap.getScene().setCursor(Cursor.WAIT);

                // import Data
                try {
                    if (medtronicCheckBox.isSelected()) {
                        if (checkIfFileExists(medtronicTextField.getText())) {
                            CarelinkCsvImporter.parseData(medtronicTextField.getText());
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "File \"" + medtronicTextField.getText()
                                    + "\" does not exist.\nNothing imported for this type.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    } else if (abbottCheckBox.isSelected()) {
                        if (checkIfFileExists(abbottTextField.getText())) {
                            LibreTxtImporter.parseData(abbottTextField.getText());
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "File \"" + abbottTextField.getText()
                                    + "\" does not exist.\nNothing imported for this type.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    } else if (googleFitCheckBox.isSelected()) {
                        if (checkIfFileExists(googleFitTextField.getText())) {
                            GoogleFitCsvImporter.parseData(googleFitTextField.getText());
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "File \"" + googleFitTextField.getText()
                                    + "\" does not exist.\nNothing imported for this type.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    } else if (googleTracksCheckBox.isSelected()) {
                        if (checkIfFileExists(googleTracksTextField.getText())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "Not Implemented Yet.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "File \"" + googleTracksTextField.getText()
                                    + "\" does not exist.\nNothing imported for this type.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    } else if (rocheCheckBox.isSelected()) {
                        if (checkIfFileExists(rocheTextField.getText())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "Not Implemented Yet.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "File \"" + rocheTextField.getText()
                                    + "\" does not exist.\nNothing imported for this type.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    } else if (odvCheckBox.isSelected()) {
                        if (checkIfFileExists(odvTextField.getText())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "Not Implemented Yet.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "File \"" + odvTextField.getText()
                                    + "\" does not exist.\nNothing imported for this type.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainGuiController.class.getName()).log(Level.SEVERE, null, ex);
                }
                // reset cursor
                ap.getScene().setCursor(cursorBackup);
                return null;
            }
        };
        Thread th = new Thread(bgTask);
        th.setDaemon(true);
        th.start();

        // set import Options if import was succesfull
        prefs.putBoolean(Constants.IMPORTER_MEDRTONIC_IMPORT_CHECKBOX_KEY, medtronicCheckBox.isSelected());
        prefs.put(Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_KEY, medtronicTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_ABBOTT_IMPORT_CHECKBOX_KEY, abbottCheckBox.isSelected());
        prefs.put(Constants.IMPORTER_ABBOTT_IMPORT_PATH_KEY, abbottTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_GOOGLE_FIT_IMPORT_CHECKBOX_KEY, googleFitCheckBox.isSelected());
        prefs.put(Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_KEY, googleFitTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_CHECKBOX_KEY, googleTracksCheckBox.isSelected());
        prefs.put(Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_KEY, googleTracksTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_ROCHE_IMPORT_CHECKBOX_KEY, rocheCheckBox.isSelected());
        prefs.put(Constants.IMPORTER_ROCHE_IMPORT_PATH_KEY, rocheTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_ODV_IMPORT_CHECKBOX_KEY, odvCheckBox.isSelected());
        prefs.put(Constants.IMPORTER_ODV_IMPORT_PATH_KEY, odvTextField.getText());

        prefs.putBoolean(Constants.IMPORTER_PERIOD_ALL_KEY, periodCheckbox.isSelected());

    }

    @FXML
    private void handlePeriodCheckboxClicked(ActionEvent event) {
        boolean periodAll = periodCheckbox.isSelected();
        periodToPicker.setDisable(periodAll);
        periodFromPicker.setDisable(periodAll);
    }

    // #########################################################################################
    // Export
    // #########################################################################################
    // #########################################################################################
    // Interpreter
    // #########################################################################################
    @FXML
    private void handleFillAsNewKathederClicked(ActionEvent event) {
        boolean fillNewKat = fillAsNewKathederCheckbox.isSelected();
        cooldownTimeSpinner.setDisable(!fillNewKat);
        prefs.putBoolean(Constants.INTERPRETER_FILL_AS_KAT_KEY, fillNewKat);
    }

    @FXML
    private void handleCooldownTimeClicked(MouseEvent event) {
        int value = cooldownTimeSpinner.getValue();
        prefs.putInt(Constants.INTERPRETER_FILL_AS_KAT_COOLDOWN_KEY, value);
    }

    @FXML
    private void handleSetToDefaultButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Nothing happened :)");
        alert.showAndWait();
    }
    // #########################################################################################
    // About
    // #########################################################################################

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        // IMPORTER
        medtronicTextField.setText(prefs.get(
                Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_KEY, ""));
        medtronicCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_MEDRTONIC_IMPORT_CHECKBOX_KEY, false));
        abbottTextField.setText(prefs.get(
                Constants.IMPORTER_ABBOTT_IMPORT_PATH_KEY, ""));
        abbottCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_ABBOTT_IMPORT_CHECKBOX_KEY, false));
        googleFitTextField.setText(prefs.get(
                Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_KEY, ""));
        googleFitCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_GOOGLE_FIT_IMPORT_CHECKBOX_KEY, false));
        googleTracksTextField.setText(prefs.get(
                Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_KEY, ""));
        googleTracksCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_CHECKBOX_KEY, false));
        rocheTextField.setText(prefs.get(
                Constants.IMPORTER_ROCHE_IMPORT_PATH_KEY, ""));
        rocheCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_ROCHE_IMPORT_CHECKBOX_KEY, false));
        odvTextField.setText(prefs.get(
                Constants.IMPORTER_ODV_IMPORT_PATH_KEY, ""));
        odvCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_ODV_IMPORT_CHECKBOX_KEY, false));

        periodToPicker.setValue(LocalDate.now());
        periodFromPicker.setValue(LocalDate.now().minusWeeks(4));
        boolean periodAll = prefs.getBoolean(Constants.IMPORTER_PERIOD_ALL_KEY, false);
        periodCheckbox.setSelected(periodAll);
        periodToPicker.setDisable(periodAll);
        periodFromPicker.setDisable(periodAll);

        // INTERPRETER
        cooldownTimeSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 300,
                        prefs.getInt(Constants.INTERPRETER_FILL_AS_KAT_COOLDOWN_KEY, 60),
                        10));
        boolean fillAsKat = prefs.getBoolean(Constants.INTERPRETER_FILL_AS_KAT_KEY, false);
        fillAsNewKathederCheckbox.setSelected(fillAsKat);
        cooldownTimeSpinner.setDisable(!fillAsKat);
    }

}
