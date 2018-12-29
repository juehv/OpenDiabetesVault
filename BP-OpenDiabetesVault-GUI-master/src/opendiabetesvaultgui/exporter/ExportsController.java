/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.exporter;

import de.opendiabetes.vault.plugin.exporter.Exporter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import de.opendiabetes.vault.plugin.util.HelpLanguage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import opendiabetesvaultgui.importer.PluginHelpController;
import opendiabetesvaultgui.launcher.FatherController;
import static opendiabetesvaultgui.launcher.FatherController.PLUGIN_CONTROL_PAGE;
import static opendiabetesvaultgui.launcher.FatherController.PLUGIN_HELP_PAGE;
import opendiabetesvaultgui.patientselection.PatientSelectionController;

/**
 * FXML Controller class
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class ExportsController extends FatherController implements Initializable {

    /**
     * Display the export plugins as TitledPane.
     */
    @FXML
    private VBox exportDisplay;
    private OpenDiabetesPluginManager pluginManager;

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Button exportAllButton;
    @FXML
    private CheckBox exportAllCheckbox;

    private final ObservableList inputList = PatientSelectionController.
            getNameList();
    /**
     * The passed ResourceBundle.
     */
    private ResourceBundle myResource;

    private final Preferences prefs = Preferences.userNodeForPackage(ExportsController.class);
    double importBoxPositionY = 10;

    double interpreterBoxPositiony = 10;
    @FXML
    private Rectangle dateArea;
    @FXML
    private Rectangle dateTextAreaRectangle;
    @FXML
    private Rectangle startDateBox;
    @FXML
    private Rectangle startDateRectangle;
    @FXML
    private Rectangle endDateBox;
    @FXML
    private Rectangle endDateRectangle;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label dateAreaLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Accordion accord;
    @FXML
    private TitledPane exportTitledPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accord.setExpandedPane(exportTitledPane);
        myResource = resources;
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().withDayOfYear(LocalDate.now().
                getDayOfYear() - 31));
        exportDisplay.setTranslateX(0);
        exportDisplay.setTranslateY(0);

        pluginManager = OpenDiabetesPluginManager.getInstance();

        List<String> exportPlugins = pluginManager.
                getPluginIDsOfType(Exporter.class);

        for (int i = 0; i < exportPlugins.size(); i++) {
            Exporter plugin = pluginManager.
                    getPluginFromString(Exporter.class, exportPlugins.
                            get(i));
            try {
                exportDisplay.getChildren().add(createExporter(exportPlugins.
                        get(i), plugin));
            } catch (Exception ex) {
                showAlert(myResource.getString("export.somethingWentWrongAlertContent"),
                        myResource.getString("export.exporterCoulndtBeLoadedAlertContent"));
            }
        }
    }
    private final String defaultHelpPagePath = "/resources/default.md";

    /**
     * Creates a Exporte plugin as TitledPane. All necessary elements for the
     * plugin will be created, designed and positioned. Methods will be assigned
     * to specific elements.
     *
     * @param name the name of the plugin
     * @param plugin the passed plugin
     * @return the TitledPane
     */
    private TitledPane createExporter(String name, Exporter plugin) {

        AnchorPane content = new AnchorPane();
        String tmp;
        try {
            tmp = pluginManager.getHelpFilePath(plugin, HelpLanguage.LANG_EN).toString();
            //tmp = pluginManager.getHelpFilePath(plugin).toString();
        } catch (Exception ex) {
            tmp = "/resources/default.md";
        }
        exportAllCheckbox.selectedProperty().set(false);
        final String helpPath = tmp;

        Button exportButton = new Button(myResource.getString("export.exportButton"));
        exportButton.setPrefWidth(100);

        exportButton.setOnAction(e -> {

        });

        ProgressBar progress = new ProgressBar();
        progress.setPrefWidth(300);
        progress.setPrefHeight(20);

        SVGPath gear = new SVGPath();
        gear.setContent("M19.43 12.98c.04-.32.07-.64.07-.98s-.03-.66-.07-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.39-.3-.61-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98l-.38-2.65C14.46 2.18 14.25 2 14 2h-4c-.25 0-.46.18-.49.42l-.38 2.65c-.61.25-1.17.59-1.69.98l-2.49-1c-.23-.09-.49 0-.61.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.07.65-.07.98s.03.66.07.98l-2.11 1.65c-.19.15-.24.42-.12.64l2 3.46c.12.22.39.3.61.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.03.24.24.42.49.42h4c.25 0 .46-.18.49-.42l.38-2.65c.61-.25 1.17-.59 1.69-.98l2.49 1c.23.09.49 0 .61-.22l2-3.46c.12-.22.07-.49-.12-.64l-2.11-1.65zM12 15.5c-1.93 0-3.5-1.57-3.5-3.5s1.57-3.5 3.5-3.5 3.5 1.57 3.5 3.5-1.57 3.5-3.5 3.5z");
        gear.setStyle("-fx-fill: #007399");
        Circle hitBoxGear = new Circle(10);
        hitBoxGear.setOpacity(0);
        hitBoxGear.setCursor(Cursor.HAND);

        hitBoxGear.setOnMouseClicked(event -> {

            try {
                openPluginControll(name + " " + myResource.getString("import.pluginControlTitle"));
            } catch (IOException ex) {
                showAlert(myResource.getString("export.somethingWentWrongAlertContent"),
                        myResource.getString("export.controlPageCouldntBeOpenedAlertContent"));
            }
        });

        Rectangle hitBoxHepSign = new Rectangle(16, 25);
        hitBoxHepSign.setCursor(Cursor.HAND);
        hitBoxHepSign.setOpacity(0);
        hitBoxHepSign.setOnMouseClicked((MouseEvent event) -> {
            try {
                openPluginHelp(helpPath, pluginManager.pluginToString(plugin) + " " + myResource.
                        getString("import.pluginHelpTitle"));
            } catch (IOException | URISyntaxException ex) {
                showAlert(myResource.getString("export.somethingWentWrongAlertContent"),
                        myResource.getString("export.helpPageCoulndtBeLoadedAlertContent"));
            }

        });

        exportButton.setDisable(true);

        SVGPath helpSign = new SVGPath();
        helpSign.setContent("M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12"
                + " 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1.79-4 "
                + "4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2.5 3-5 0-2.21-1.79-4-4-4z");
        helpSign.setStyle("-fx-fill: #007399;");
        List<Node> list = new ArrayList<>(
                Arrays.asList(exportButton, gear, progress,
                        helpSign, hitBoxGear, hitBoxHepSign));

        exportButton.relocate(18, 80);
        progress.relocate(160, 88);
        hitBoxGear.relocate(540, 11);
        hitBoxHepSign.relocate(580, 10);
        helpSign.relocate(hitBoxHepSign.getLayoutX(), hitBoxHepSign.getLayoutY());
        gear.relocate(hitBoxGear.getLayoutX() - 11, hitBoxGear.getLayoutY() - 11);
        Button browseButton = new Button(myResource.getString("import.browseButton"));
        TextField field = new TextField();

        exportAllButton.pressedProperty().addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                exportButton.fire();
            }
        });
        browseButton.relocate(510, 40);
        field.relocate(20, 40);
        field.setPrefWidth(465);
        field.setPrefHeight(25);
        field.setId("importPfad");
        field.setText(FileSystemView.getFileSystemView().getDefaultDirectory().getPath());
        browseButton.setPrefWidth(120);

        browseButton.setOnAction((ActionEvent action) -> {
            chooseFiles(action, field, exportButton);
        });

        Rectangle fieldBox = new Rectangle();
        fieldBox.setWidth(471);
        fieldBox.setHeight(49);
        fieldBox.setSmooth(true);
        fieldBox.setId("fieldBox");

        Rectangle fieldRectangle = new Rectangle();
        fieldRectangle.setWidth(471);
        fieldRectangle.setHeight(1.5);
        fieldRectangle.setSmooth(true);
        fieldRectangle.setId("fieldRectangle");

        Label fieldLabel = new Label(myResource.getString("export.fieldLabel"));
        fieldLabel.setId("fieldLabel");

        list.add(fieldBox);
        list.add(fieldLabel);
        list.add(fieldRectangle);
        list.add(field);
        list.add(browseButton);

        TitledPane pane = new TitledPane("", content);

        content.getChildren().addAll(list);
        BorderPane bPane = new BorderPane();
        CheckBox disBox = new CheckBox("");
        Label title = new Label(name);
        progress.setDisable(true);

        pane.graphicTextGapProperty().bind(pane.widthProperty());
        pane.setGraphic(createBorderPane(name, pane, progress, exportButton));

        pane.setExpanded(false);
        pane.setAnimated(true);

        exportAllCheckbox.selectedProperty().addListener((
                ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)
                -> {
            if (!oldValue) {
                exportAllButton.setDisable(false);
            } else {
                exportAllButton.setDisable(true);
            }
        });

        return pane;
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("OpenDiabetesVault");
        alert.setHeaderText(header);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(
                "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                toExternalForm());
        alert.showAndWait();

    }

    /**
     * Creates a Borderpane consisting of a Checkbox and ProgressBar. The
     * element will be positioned using methods of the BorderPane. Calls
     * #checkBoxListener(CheckBox box, TitledPane target, String name, BorderPane bp)
     *
     * @see #getFileString(Stage stage)
     *
     * @param name the title of a TitledPane
     * @param pane the respective a TitledPane
     * @param progress a Progressbar
     * @param export the export Button
     * @return the BorderPane
     */
    private BorderPane createBorderPane(String name, TitledPane pane, ProgressBar progress, Button export) {
        BorderPane bPane = new BorderPane();
        CheckBox cBox = new CheckBox("");
        if (prefs.getBoolean(name + inputList.get(0), false)) {
            pane.setCollapsible(false);
            pane.setExpanded(false);
        } else {
            cBox.selectedProperty().set(true);
        }
        Label title = new Label(name);
        progress.setProgress(0);
        progress.setPrefWidth(300);
        progress.setPrefHeight(20);
        bPane.setPadding(new Insets(5, 10, 5, 10));
        bPane.autosize();
        bPane.setLeft(cBox);
        BorderPane centerPane = new BorderPane();
        centerPane.setPadding(new Insets(0, 0, 0, 10));
        centerPane.setLeft(title);
        bPane.setCenter(centerPane);
        bPane.setRight(progress);
        bPane.prefWidthProperty().bind(pane.widthProperty().subtract(200));
        checkBoxListener(cBox, pane, name, bPane, export);
        return bPane;
    }

    /**
     * Opens page, which displays a help page.
     *
     * @param path path to the help page.
     * @param title title of the window.
     * @throws java.net.MalformedURLException if the path couldnt be parsed as
     * URL
     * @throws java.net.URISyntaxException if URI syntax is wrong
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    private void openPluginHelp(String path, String title) throws IOException, URISyntaxException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(PLUGIN_HELP_PAGE), myResource);
        Parent root = loader.load();

        PluginHelpController pluginHelpController = loader.getController();
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        pluginHelpController.loadHelpPage(path);
        stage.show();

    }

    /**
     * Adds listener to CheckBoxe's selectedProperty(). CheckBox state
     * determines, if the TitledPane is collapsible.
     *
     * @param box the CheckBox from the TitledPane
     * @param target the TitledPane
     * @param name title of the TitledPane
     * @param bp the BorderPane inside of the TitledPane
     * @param export the export button
     */
    private void checkBoxListener(CheckBox box, TitledPane target, String name, BorderPane bp, Button export) {
        box.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
                -> {
            if (!newValue) {
                target.setExpanded(newValue);
                export.setDisable(!newValue);
            }
            target.setCollapsible(newValue);
            prefs.putBoolean(name + inputList.get(0), !newValue);
        });
    }

    /**
     * Opens the control page as a popup.
     *
     * @param name title of the window
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    private void openPluginControll(String name) throws IOException {
        openPage(PLUGIN_CONTROL_PAGE, name, false, myResource);
    }

    /**
     * Calls #openPluginHelp(String path, String title)
     *
     * @param name title of the opened window
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     */
    private void openPluginHelp(String name) throws IOException {
        openPageSingleInstance(getClass().getResource(PLUGIN_HELP_PAGE).toExternalForm(), name, false, myResource);
    }

    /**
     * Saves the path of the chosen file inside a textfield and is assigend to
     * the #exportButton.
     *
     * @see #getFileString(Stage stage)
     *
     * @param event calls method when triggered.
     * @param test the textfield in which the path is saved
     * @param button the button
     */
    private void chooseFiles(ActionEvent event, TextField test, Button button) {
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        test.setText(getFileString(stage));
        button.setDisable(false);

    }

    /**
     * Opens a directoryChooser. The chosen directory will be saved inside a
     * texfield.
     *
     * @param stage stage which displays the directory chooser
     * @return The path of the chosen directory
     */
    private String getFileString(Stage stage) {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File dir = directoryChooser.showDialog(stage);

        if (dir != null) {

            return dir.getAbsolutePath();

        }

        return "";
    }

}
