/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetesvault.fx.gui;

import de.jhit.opendiabetes.vault.container.SliceEntry;
import de.jhit.opendiabetes.vault.container.csv.SliceCsVEntry;
import de.jhit.opendiabetes.vault.container.csv.VaultCsvEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.exporter.ExporterOptions;
import de.jhit.opendiabetes.vault.exporter.FileExporter;
import de.jhit.opendiabetes.vault.exporter.OdvDbJsonExporter;
import de.jhit.opendiabetes.vault.exporter.SliceLayoutCsvExporter;
import de.jhit.opendiabetes.vault.exporter.SourceCodeExporter;
import de.jhit.opendiabetes.vault.exporter.VaultCsvExporter;
import de.jhit.opendiabetes.vault.exporter.VaultOdvExporter;
import de.jhit.opendiabetes.vault.importer.FileImporter;
import de.jhit.opendiabetes.vault.importer.GoogleFitCsvImporter;
import de.jhit.opendiabetes.vault.importer.LibreTxtImporter;
import de.jhit.opendiabetes.vault.importer.MedtronicCsvImporter;
import de.jhit.opendiabetes.vault.importer.OdvDbJsonImporter;
import de.jhit.opendiabetes.vault.importer.SonySWR12Importer;
import de.jhit.opendiabetes.vault.importer.interpreter.ExerciseInterpreter;
import de.jhit.opendiabetes.vault.importer.interpreter.ExerciseInterpreterOptions;
import de.jhit.opendiabetes.vault.importer.interpreter.NonInterpreter;
import de.jhit.opendiabetes.vault.importer.interpreter.PumpInterpreter;
import de.jhit.opendiabetes.vault.importer.interpreter.PumpInterpreterOptions;
import de.jhit.opendiabetes.vault.util.FileCopyUtil;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
    private TextField googleGatheredTextField;
    @FXML
    private CheckBox googleGatheredCheckBox;
    @FXML
    private TextField sonyTextField;
    @FXML
    private CheckBox sonyCheckBox;
    @FXML
    private TextField odvTextField;
    @FXML
    private CheckBox odvCheckBox;
    @FXML
    private DatePicker importPeriodFromPicker;
    @FXML
    private DatePicker importPeriodToPicker;
    @FXML
    private CheckBox importPeriodAllCheckbox;
    @FXML
    private ProgressBar importPorgressBar;

    // Export
    @FXML
    private TextField exportOdvTextField;
    @FXML
    private CheckBox exportOdvCheckBox;
    @FXML
    private TextField exportPlotDailyTextField;
    @FXML
    private CheckBox exportPlotDailyCheckBox;
    @FXML
    private DatePicker exportPeriodFromPicker;
    @FXML
    private DatePicker exportPeriodToPicker;
    @FXML
    private CheckBox exportPeriodAllCheckbox;
    @FXML
    private ProgressBar exportPorgressBar;

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

    private static DirectoryChooser configureDirectoryChooser(File initDir) {
        // fileChooser.setTitle("View Pictures");
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (initDir != null && initDir.exists()) {
            if (initDir.isDirectory()) {
                dirChooser.setInitialDirectory(initDir);
            } else {
                dirChooser.setInitialDirectory(initDir.getParentFile());
            }
        } else {
            dirChooser.setInitialDirectory(
                    new File(System.getProperty("user.home")));
        }

        return dirChooser;
    }

    private static boolean checkIfFileExists(String path) {
        File checkPath = new File(path);
        return checkPath.exists() && checkPath.isFile() && checkPath.canRead();
    }

    private static boolean checkIfFilesExists(String paths) {
        for (String item : paths.split(Constants.MULTI_FILE_PATH_DELIMITER)) {
            if (item != null && !item.isEmpty()) {
                if (!checkIfFileExists(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkIfFolderExists(String paths) {
        File checkPath = new File(paths);
        return checkPath.exists() && checkPath.isDirectory() && checkPath.canWrite();
    }

    private void saveMultiFileSelection(String pathKey, String countKey, String multifileString) {
        String[] paths = multifileString.split(Constants.MULTI_FILE_PATH_DELIMITER);
        prefs.putInt(countKey, paths.length);
        for (int i = 0; i < paths.length; i++) {
            prefs.put(pathKey + i, paths[i]);
        }
    }

    private String loadMultiFileSelection(String pathKey, String countKey) {
        StringBuilder sb = new StringBuilder();
        int max = prefs.getInt(countKey, 0);
        for (int i = 0; i < max; i++) {
            String path = prefs.get(pathKey + i, "");
            if (path != null && !path.isEmpty()) {
                sb.append(path).append(";");
            }
        }
        return sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";

    }

    // #########################################################################################
    // Import
    // #########################################################################################
    @FXML
    private void handleButtonBrowseMedtronic(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_KEY + 0, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.CSV_EXTENSION_FILTER);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(Constants.MULTI_FILE_PATH_DELIMITER);
            });
            medtronicTextField.setText(sb.toString().substring(0, sb.length() - 1));
            medtronicCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseAbbott(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_ABBOTT_IMPORT_PATH_KEY + 0, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.TXT_EXTENSION_FILTER);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(Constants.MULTI_FILE_PATH_DELIMITER);
            });
            abbottTextField.setText(sb.toString().substring(0, sb.length() - 1));
            abbottCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseGoogleFit(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_KEY + 0, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.CSV_EXTENSION_FILTER);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(Constants.MULTI_FILE_PATH_DELIMITER);
            });
            googleFitTextField.setText(sb.toString().substring(0, sb.length() - 1));
            googleFitCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseGoogleTracks(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_KEY + 0, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.JSON_EXTENSION_FILTER);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(Constants.MULTI_FILE_PATH_DELIMITER);
            });
            googleGatheredTextField.setText(sb.toString().substring(0, sb.length() - 1));
            googleGatheredCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseRoche(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_ROCHE_IMPORT_PATH_KEY + 0, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.CSV_EXTENSION_FILTER);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(Constants.MULTI_FILE_PATH_DELIMITER);
            });
            sonyTextField.setText(sb.toString().substring(0, sb.length() - 1));
            sonyCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonBrowseOpenDiabetesVault(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.IMPORTER_ODV_IMPORT_PATH_KEY + 0, ""));

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, lastPath, Constants.JSON_EXTENSION_FILTER);
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(Constants.MULTI_FILE_PATH_DELIMITER);
            });
            odvTextField.setText(sb.toString().substring(0, sb.length() - 1));
            odvCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonImport(ActionEvent event) {
//        progress dialog
//        --> try this http://docs.oracle.com/javafx/2/ui_controls/progress.htm

        // check if sth is selected
        if (!medtronicCheckBox.isSelected()
                && !abbottCheckBox.isSelected()
                && !googleFitCheckBox.isSelected()
                && !googleGatheredCheckBox.isSelected()
                && !sonyCheckBox.isSelected()
                && !odvCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "No data selected.\nPlease select a dataset and try again.",
                    ButtonType.CLOSE);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // check paths
        StringBuilder sb = new StringBuilder();
        if (medtronicCheckBox.isSelected() && !checkIfFilesExists(medtronicTextField.getText())) {
            sb.append(medtronicTextField.getText());
        }
        if (abbottCheckBox.isSelected() && !checkIfFilesExists(abbottTextField.getText())) {
            if (sb.length() > 0) {
                sb.append("\",\n\"");
            }
            sb.append(abbottTextField.getText());
        }
        if (googleFitCheckBox.isSelected() && !checkIfFilesExists(googleFitTextField.getText())) {
            if (sb.length() > 0) {
                sb.append("\",\n\"");
            }
            sb.append(googleFitTextField.getText());

        }
        if (googleGatheredCheckBox.isSelected() && !checkIfFilesExists(googleGatheredTextField.getText())) {
            if (sb.length() > 0) {
                sb.append("\",\n\"");
            }
            sb.append(googleGatheredTextField.getText());
        }
        if (sonyCheckBox.isSelected() && !checkIfFilesExists(sonyTextField.getText())) {
            if (sb.length() > 0) {
                sb.append("\",\n\"");
            }
            sb.append(sonyTextField.getText());
        }
        if (odvCheckBox.isSelected() && !checkIfFilesExists(odvTextField.getText())) {
            if (sb.length() > 0) {
                sb.append("\",\n\"");
            }
            sb.append(odvTextField.getText());
        }
        if (sb.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "There is an error with the path(s):\n\"" + sb.toString()
                    + "\"\nNothing imported for this type(s).",
                    ButtonType.CLOSE);
            alert.setHeaderText(null);
            alert.show();
            return;
        }

        // read interpreter options
        PumpInterpreterOptions iOptions = new PumpInterpreterOptions(
                prefs.getBoolean(Constants.INTERPRETER_FILL_AS_KAT_KEY, false),
                prefs.getInt(Constants.INTERPRETER_FILL_AS_KAT_COOLDOWN_KEY, 60),
                !importPeriodAllCheckbox.isSelected(),
                TimestampUtils.fromLocalDate(importPeriodFromPicker.getValue()),
                TimestampUtils.fromLocalDate(importPeriodToPicker.getValue(), 86399000)); //86399000 = 1 day - 1 second

        ExerciseInterpreterOptions fOptions = new ExerciseInterpreterOptions(
                !importPeriodAllCheckbox.isSelected(),
                TimestampUtils.fromLocalDate(importPeriodFromPicker.getValue()),
                TimestampUtils.fromLocalDate(importPeriodToPicker.getValue(), 86399000),
                5,
                10);

        // do the work
        importPorgressBar.setProgress(
                -1.0);
        Task bgTask = new Task() {
            @Override
            protected Void call() throws Exception {
                // set wait cursor
                Cursor cursorBackup = ap.getScene().getCursor();
                ap.getScene().setCursor(Cursor.WAIT);

                // import Data
                try {
                    Platform.runLater(() -> {
                        importPorgressBar.setProgress(0.05);
                    });
                    if (medtronicCheckBox.isSelected()) {
                        PumpInterpreter interpreter
                                = new PumpInterpreter(
                                        new MedtronicCsvImporter(null),
                                        iOptions, VaultDao.getInstance());
                        String[] unsortedPaths = medtronicTextField.getText().split(Constants.MULTI_FILE_PATH_DELIMITER);
                        Arrays.sort(unsortedPaths);
                        for (String filePath : unsortedPaths) {
                            if (filePath != null && !filePath.isEmpty()) {
                                ((FileImporter) interpreter.getImporter()).setImportFilePath(filePath);
                                interpreter.importAndInterpret();
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        importPorgressBar.setProgress(0.2);
                    });
                    if (abbottCheckBox.isSelected()) {
                        for (String filePath : abbottTextField.getText().split(Constants.MULTI_FILE_PATH_DELIMITER)) {
                            if (filePath != null && !filePath.isEmpty()) {
                                LibreTxtImporter.parseData(filePath);
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        importPorgressBar.setProgress(0.35);
                    });
                    if (googleFitCheckBox.isSelected()) {
                        ExerciseInterpreter interpreter
                                = new ExerciseInterpreter(
                                        new GoogleFitCsvImporter(null),
                                        fOptions, VaultDao.getInstance());
                        for (String filePath : googleFitTextField.getText().split(Constants.MULTI_FILE_PATH_DELIMITER)) {
                            if (filePath != null && !filePath.isEmpty()) {
                                ((FileImporter) interpreter.getImporter()).setImportFilePath(filePath);
                                interpreter.importAndInterpret();
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        importPorgressBar.setProgress(0.50);
                    });
                    if (googleGatheredCheckBox.isSelected()) {
                        for (String filePath : googleGatheredTextField.getText().split(Constants.MULTI_FILE_PATH_DELIMITER)) {
                            if (filePath != null && !filePath.isEmpty()) {//TODO
                            }
                        }
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "Google Tracks import is not implemented yet.",
                                    ButtonType.CLOSE);
                            alert.setHeaderText(null);
                            alert.show();
                        });
                    }
                    Platform.runLater(() -> {
                        importPorgressBar.setProgress(0.65);
                    });
                    if (sonyCheckBox.isSelected()) {
                        ExerciseInterpreter interpreter
                                = new ExerciseInterpreter(
                                        new SonySWR12Importer(null),
                                        fOptions, VaultDao.getInstance());
                        for (String filePath : sonyTextField.getText().split(Constants.MULTI_FILE_PATH_DELIMITER)) {
                            if (filePath != null && !filePath.isEmpty()) {
                                ((FileImporter) interpreter.getImporter()).setImportFilePath(filePath);
                                interpreter.importAndInterpret();
                            }
                        }
                    }
                    Platform.runLater(() -> {
                        importPorgressBar.setProgress(0.80);
                    });
                    if (odvCheckBox.isSelected()) {
                        for (String filePath : odvTextField.getText().split(Constants.MULTI_FILE_PATH_DELIMITER)) {
//                            NonInterpreter interpreter = new NonInterpreter(
//                                    new VaultOdvImporter(null),
//                                    iOptions,
//                                    VaultDao.getInstance());

                            NonInterpreter interpreter = new NonInterpreter(
                                    new OdvDbJsonImporter(null),
                                    iOptions,
                                    VaultDao.getInstance());
                            if (filePath != null && !filePath.isEmpty()) {
                                ((FileImporter) interpreter.getImporter()).setImportFilePath(filePath);
                                interpreter.importAndInterpret();
                            }
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainGuiController.class.getName()).log(Level.SEVERE,
                            "Error while importing files.", ex);
                }
                Platform.runLater(() -> {
                    importPorgressBar.setProgress(1.0);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Import finished.",
                            ButtonType.CLOSE);
                    alert.setHeaderText(null);
                    alert.show();
                });

                // clean db
                VaultDao.getInstance().removeDublicates();

                // reset cursor
                ap.getScene().setCursor(cursorBackup);
                return null;
            }
        };
        Thread th = new Thread(bgTask);
        th.setDaemon(true);
        th.start();
        //th.join();
        // join will block the gui --> seperate progress dialog

        // set import Options if import was succesfull
        prefs.putBoolean(Constants.IMPORTER_MEDRTONIC_IMPORT_CHECKBOX_KEY, medtronicCheckBox.isSelected());
        saveMultiFileSelection(Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_KEY,
                Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_COUNT_KEY,
                medtronicTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_ABBOTT_IMPORT_CHECKBOX_KEY, abbottCheckBox.isSelected());
        saveMultiFileSelection(Constants.IMPORTER_ABBOTT_IMPORT_PATH_KEY,
                Constants.IMPORTER_ABBOTT_IMPORT_PATH_COUNT_KEY,
                abbottTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_GOOGLE_FIT_IMPORT_CHECKBOX_KEY, googleFitCheckBox.isSelected());
        saveMultiFileSelection(Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_KEY,
                Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_COUNT_KEY,
                googleFitTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_CHECKBOX_KEY, googleGatheredCheckBox.isSelected());
        saveMultiFileSelection(Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_KEY,
                Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_COUNT_KEY,
                googleGatheredTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_ROCHE_IMPORT_CHECKBOX_KEY, sonyCheckBox.isSelected());
        saveMultiFileSelection(Constants.IMPORTER_ROCHE_IMPORT_PATH_KEY,
                Constants.IMPORTER_ROCHE_IMPORT_PATH_COUNT_KEY,
                sonyTextField.getText());
        prefs.putBoolean(Constants.IMPORTER_ODV_IMPORT_CHECKBOX_KEY, odvCheckBox.isSelected());
        saveMultiFileSelection(Constants.IMPORTER_ODV_IMPORT_PATH_KEY,
                Constants.IMPORTER_ODV_IMPORT_PATH_COUNT_KEY,
                odvTextField.getText());

        prefs.putBoolean(Constants.IMPORTER_PERIOD_ALL_KEY, importPeriodAllCheckbox.isSelected());

    }

    @FXML
    private void handlePeriodCheckboxClicked(ActionEvent event) {
        boolean periodAll = importPeriodAllCheckbox.isSelected();
        importPeriodToPicker.setDisable(periodAll);
        importPeriodFromPicker.setDisable(periodAll);
    }

    // #########################################################################################
    // Export
    // #########################################################################################
    @FXML
    private void handleExportButtonBrowseOpenDiabetesVaultClicked(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.EXPORTER_ODV_PATH_KEY, ""));

        DirectoryChooser dirChooser = configureDirectoryChooser(lastPath);
        File file = dirChooser.showDialog(stage);

        if (checkIfFolderExists(file.getAbsolutePath())) {
            exportOdvTextField.setText(file.getAbsolutePath());
            exportOdvCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleExportButtonBrowsePlotDailyClicked(ActionEvent event) {
        Stage stage = (Stage) ap.getScene().getWindow();
        File lastPath = new File(prefs.get(Constants.EXPORTER_PLOT_DAILY_PATH_KEY, ""));

        DirectoryChooser dirChooser = configureDirectoryChooser(lastPath);
        File file = dirChooser.showDialog(stage);

        if (checkIfFolderExists(file.getAbsolutePath())) {
            exportPlotDailyTextField.setText(file.getAbsolutePath());
            exportPlotDailyCheckBox.setSelected(true);
        }
    }

    @FXML
    private void handleButtonProcessing(ActionEvent event) {
        // add your processing code here

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Processing finished.",
                    ButtonType.CLOSE);
            alert.setHeaderText(null);
            alert.show();
        });
    }

    @FXML
    private void handleButtonExportClicked(ActionEvent event) {
        // check if sth is selected
        if (!exportOdvCheckBox.isSelected()
                && !exportPlotDailyCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "No export method selected.\nPlease select a exporter and try again.",
                    ButtonType.CLOSE);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // check paths
        StringBuilder sb = new StringBuilder();
        if (exportOdvCheckBox.isSelected() && !checkIfFolderExists(exportOdvTextField.getText())) {
            sb.append(exportOdvTextField.getText());
        }
        if (exportPlotDailyCheckBox.isSelected() && !checkIfFolderExists(exportPlotDailyTextField.getText())) {
            if (sb.length() > 0) {
                sb.append("\",\n\"");
            }
            sb.append(exportPlotDailyTextField.getText());
        }
        if (sb.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "There is an error with the path(s):\n\"" + sb.toString()
                    + "\"\nExport stopped.",
                    ButtonType.CLOSE);
            alert.setHeaderText(null);
            alert.show();
            return;
        }

        // do the work
        exportPorgressBar.setProgress(
                -1.0);
        Task bgTask = new Task() {
            @Override
            protected Void call() throws Exception {
                // set wait cursor
                Cursor cursorBackup = ap.getScene().getCursor();
                ap.getScene().setCursor(Cursor.WAIT);

                // export Data
                try {
                    Platform.runLater(() -> {
                        exportPorgressBar.setProgress(0.05);
                    });
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM-HHmmss");
                    String odvExpotFileName = "export-"
                            + VaultCsvEntry.VERSION_STRING
                            + "-"
                            + formatter.format(new Date())
                            + ".csv"; //TODO read format from options
                    if (exportOdvCheckBox.isSelected() || exportPlotDailyCheckBox.isSelected()) {
                        String path;
                        if (exportOdvCheckBox.isSelected()) {
                            path = exportOdvTextField.getText();
                        } else {
                            path = System.getProperty("java.io.tmpdir");
                        }
                        odvExpotFileName = new File(path).getAbsolutePath()
                                + "/" + odvExpotFileName;

                        ExporterOptions eOptions = new ExporterOptions(
                                !exportPeriodAllCheckbox.isSelected(),
                                TimestampUtils.fromLocalDate(
                                        exportPeriodFromPicker.getValue()),
                                TimestampUtils.fromLocalDate(
                                        exportPeriodToPicker.getValue(), 86399000));// 86399000 = 1 day - 1 second      

                        // standard export
                        FileExporter exporter = new VaultCsvExporter(eOptions,
                                VaultDao.getInstance(),
                                odvExpotFileName);
                        int result = exporter.exportDataToFile(null);
                        if (result != VaultCsvExporter.RESULT_OK) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR,
                                        "Could not export to odv csv file: "
                                        + result + "\nSee logfile for details.",
                                        ButtonType.CLOSE);
                                alert.setHeaderText(null);
                                alert.show();
                            });
                        }

                        // novel odv export
//                        odvExpotFileName = new File(path).getAbsolutePath()
//                                + "/"
//                                + "export-"
//                                + VaultCsvEntry.VERSION_STRING
//                                + "-"
//                                + formatter.format(new Date())
//                                + ".odv";
//                        exporter = new VaultOdvExporter(eOptions,
//                                VaultDao.getInstance(),
//                                odvExpotFileName);
//                        int result2 = exporter.exportDataToFile(null);
//                        if (result2 != VaultCsvExporter.RESULT_OK) {
//                            Platform.runLater(() -> {
//                                Alert alert = new Alert(Alert.AlertType.ERROR,
//                                        "Could not export to odv csv file: "
//                                        + result2 + "\nSee logfile for details.",
//                                        ButtonType.CLOSE);
//                                alert.setHeaderText(null);
//                                alert.show();
//                            });
//                        }

                        // code exporter
//                        odvExpotFileName = new File(path).getAbsolutePath()
//                                + "/"
//                                + "export-"
//                                + VaultCsvEntry.VERSION_STRING
//                                + "-"
//                                + formatter.format(new Date())
//                                + ".txt";
//                        exporter = new SourceCodeExporter(eOptions,
//                                VaultDao.getInstance(),
//                                odvExpotFileName);
//                        int result4 = exporter.exportDataToFile(null);
//                        if (result4 != VaultCsvExporter.RESULT_OK) {
//                            Platform.runLater(() -> {
//                                Alert alert = new Alert(Alert.AlertType.ERROR,
//                                        "Could not export to code text file: "
//                                        + result4 + "\nSee logfile for details.",
//                                        ButtonType.CLOSE);
//                                alert.setHeaderText(null);
//                                alert.show();
//                            });
//                        }

                        // slice exporter
//                        List<SliceEntry> entries = new ArrayList<>();
//                        // today    
//                        Calendar date = new GregorianCalendar();
//                        // reset hour, minutes, seconds and millis
//                        date.set(Calendar.HOUR_OF_DAY, 0);
//                        date.set(Calendar.MINUTE, 0);
//                        date.set(Calendar.SECOND, 0);
//                        date.set(Calendar.MILLISECOND, 0);
//                        Date today = TimestampUtils.createCleanTimestamp(date.getTime());
//
//                        for (int i = 27; i >= 0; i--) {
//                            entries.add(new SliceEntry(
//                                    TimestampUtils.addMinutesToTimestamp(today, Math.round(i * -1440)),
//                                    360));
//                        }
//                        odvExpotFileName = new File(path).getAbsolutePath()
//                                + "/"
//                                + "slice-"
//                                + SliceCsVEntry.VERSION_STRING
//                                + "-"
//                                + formatter.format(new Date())
//                                + ".csv";
//                        exporter = new SliceLayoutCsvExporter(eOptions,
//                                odvExpotFileName, entries);
//                        int result3 = exporter.exportDataToFile(null);
//                        if (result3 != VaultCsvExporter.RESULT_OK) {
//                            Platform.runLater(() -> {
//                                Alert alert = new Alert(Alert.AlertType.ERROR,
//                                        "Could not export to odv csv file: "
//                                        + result3 + "\nSee logfile for details.",
//                                        ButtonType.CLOSE);
//                                alert.setHeaderText(null);
//                                alert.show();
//                            });
//                        }

                        // json exporter
                        odvExpotFileName = new File(path).getAbsolutePath()
                                + "/"
                                + "export-"
                                + VaultCsvEntry.VERSION_STRING
                                + "-"
                                + formatter.format(new Date())
                                + ".json";
                        exporter = new OdvDbJsonExporter(eOptions,
                                odvExpotFileName);
                        int result5 = exporter.exportDataToFile(
                                VaultDao.getInstance().queryVaultEntrysBetween(
                                        eOptions.exportPeriodFrom,
                                        eOptions.exportPeriodTo));
                        if (result5 != VaultCsvExporter.RESULT_OK) {
                            
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR,
                                        "Could not export to code text file: "
                                        + result5 + "\nSee logfile for details.",
                                        ButtonType.CLOSE);
                                alert.setHeaderText(null);
                                alert.show();
                            });
                        }
                    }
                    Platform.runLater(() -> {
                        exportPorgressBar.setProgress(0.5);
                    });
                    if (exportPlotDailyCheckBox.isSelected()) {
                        //TODO move to engine
                        String tmpPath = System.getProperty("java.io.tmpdir");
                        // kill old dir
                        File tmpDir = new File(tmpPath + "/plot");
                        if (tmpDir.exists()) {
                            tmpDir.delete();
                        }
                        FileCopyUtil.copyDirectory(new File("../plot"),
                                tmpDir);

                        //Plot single days
                        String cmd = "python ./plot.py " + odvExpotFileName;
                        ProcessBuilder pb = new ProcessBuilder(cmd);
                        pb.directory(tmpDir);
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                        Process p = pb.start();
                        synchronized (p) {
                            p.wait();
                        }
                        // create sheet
                        cmd = "pdflatex buildPDF.tex";
                        pb = new ProcessBuilder(cmd);
                        pb.directory(tmpDir);
                        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                        p = pb.start();
                        synchronized (p) {
                            p.wait();
                        }

                        // copy result
                        FileCopyUtil.copyFile(
                                new File(tmpPath + "/plot/buildPDF.pdf"),
                                new File(exportPlotDailyTextField.getText()
                                        + "/export-" + formatter.format(new Date()) + ".pdf"));
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainGuiController.class.getName()).log(Level.SEVERE,
                            "Error while exporting files.", ex);
                }
                Platform.runLater(() -> {
                    exportPorgressBar.setProgress(1.0);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Export finished.",
                            ButtonType.CLOSE);
                    alert.setHeaderText(null);
                    alert.show();
                });

                // reset cursor
                ap.getScene().setCursor(cursorBackup);
                return null;
            }
        };
        Thread th = new Thread(bgTask);
        th.setDaemon(true);
        th.start();
        //th.join();
        // join will block the gui --> seperate progress dialog

        // set import Options if import was succesfull
        prefs.putBoolean(Constants.EXPORTER_ODV_CHECKBOX_KEY, exportOdvCheckBox.isSelected());
        prefs.put(Constants.EXPORTER_ODV_PATH_KEY, exportOdvTextField.getText());
        prefs.putBoolean(Constants.EXPORTER_PLOT_DAILY_CHECKBOX_KEY, exportPlotDailyCheckBox.isSelected());
        prefs.put(Constants.EXPORTER_PLOT_DAILY_PATH_KEY, exportPlotDailyTextField.getText());

        prefs.putBoolean(Constants.EXPORTER_PERIOD_ALL_KEY, exportPeriodAllCheckbox.isSelected());
    }

    @FXML
    private void handleExportPeriodCheckboxClicked(ActionEvent event) {
        boolean periodAll = exportPeriodAllCheckbox.isSelected();
        exportPeriodToPicker.setDisable(periodAll);
        exportPeriodFromPicker.setDisable(periodAll);
    }

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

        // IMPORT
        medtronicTextField.setText(loadMultiFileSelection(
                Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_KEY,
                Constants.IMPORTER_MEDTRONIC_IMPORT_PATH_COUNT_KEY));
        medtronicCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_MEDRTONIC_IMPORT_CHECKBOX_KEY, false));
        abbottTextField.setText(loadMultiFileSelection(
                Constants.IMPORTER_ABBOTT_IMPORT_PATH_KEY,
                Constants.IMPORTER_ABBOTT_IMPORT_PATH_COUNT_KEY));
        abbottCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_ABBOTT_IMPORT_CHECKBOX_KEY, false));
        googleFitTextField.setText(loadMultiFileSelection(
                Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_KEY,
                Constants.IMPORTER_GOOGLE_FIT_IMPORT_PATH_COUNT_KEY));
        googleFitCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_GOOGLE_FIT_IMPORT_CHECKBOX_KEY, false));
        googleGatheredTextField.setText(loadMultiFileSelection(
                Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_KEY,
                Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_PATH_COUNT_KEY));
        googleGatheredCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_GOOGLE_TRACKS_IMPORT_CHECKBOX_KEY, false));
        sonyTextField.setText(loadMultiFileSelection(
                Constants.IMPORTER_ROCHE_IMPORT_PATH_KEY,
                Constants.IMPORTER_ROCHE_IMPORT_PATH_COUNT_KEY));
        sonyCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_ROCHE_IMPORT_CHECKBOX_KEY, false));
        odvTextField.setText(loadMultiFileSelection(
                Constants.IMPORTER_ODV_IMPORT_PATH_KEY,
                Constants.IMPORTER_ODV_IMPORT_PATH_COUNT_KEY));
        odvCheckBox.setSelected(prefs.getBoolean(
                Constants.IMPORTER_ODV_IMPORT_CHECKBOX_KEY, false));

        importPeriodToPicker.setValue(LocalDate.now());
        importPeriodFromPicker.setValue(LocalDate.now().minusWeeks(4));
        boolean periodAll = prefs.getBoolean(Constants.IMPORTER_PERIOD_ALL_KEY, false);
        importPeriodAllCheckbox.setSelected(periodAll);
        importPeriodToPicker.setDisable(periodAll);
        importPeriodFromPicker.setDisable(periodAll);

        // EXPORT
        exportOdvCheckBox.setSelected(prefs.getBoolean(Constants.EXPORTER_ODV_CHECKBOX_KEY, false));
        exportPlotDailyCheckBox.setSelected(prefs.getBoolean(Constants.EXPORTER_PLOT_DAILY_CHECKBOX_KEY, false));
        exportOdvTextField.setText(prefs.get(Constants.EXPORTER_ODV_PATH_KEY, ""));
        exportPlotDailyTextField.setText(prefs.get(Constants.EXPORTER_PLOT_DAILY_PATH_KEY, ""));

        exportPeriodToPicker.setValue(LocalDate.now());
        exportPeriodFromPicker.setValue(LocalDate.now().minusWeeks(4));
        periodAll = prefs.getBoolean(Constants.EXPORTER_PERIOD_ALL_KEY, false);
        exportPeriodAllCheckbox.setSelected(periodAll);
        exportPeriodToPicker.setDisable(periodAll);
        exportPeriodFromPicker.setDisable(periodAll);

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
