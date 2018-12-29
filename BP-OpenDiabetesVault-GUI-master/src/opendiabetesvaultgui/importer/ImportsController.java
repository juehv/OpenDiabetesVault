/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.importer;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.data.VaultDao;
import de.opendiabetes.vault.plugin.common.OpenDiabetesPlugin.StatusListener;
import de.opendiabetes.vault.plugin.fileimporter.FileImporter;
import de.opendiabetes.vault.plugin.interpreter.Interpreter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import de.opendiabetes.vault.plugin.util.HelpLanguage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import opendiabetesvaultgui.launcher.FatherController;
import opendiabetesvaultgui.patientselection.PatientSelectionController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import static opendiabetesvaultgui.launcher.FatherController.PLUGIN_CONTROL_PAGE;
import static opendiabetesvaultgui.launcher.FatherController.PLUGIN_HELP_PAGE;
import opendiabetesvaultgui.launcher.MainWindowController;

/**
 * FXML Controller class.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class ImportsController extends FatherController
        implements Initializable {

    private final Preferences prefs = Preferences.
            userNodeForPackage(ImportsController.class);

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private AnchorPane greatFatherPane;
    @FXML
    private VBox importDisplay;
    @FXML
    private CheckBox importAllCheckBox;
    @FXML
    private Button importAllButton;
    @FXML
    private Accordion accord;
    @FXML
    private TitledPane importTitledPane;

    private OpenDiabetesPluginManager pluginManager;

    private ResourceBundle myResource;

    private final ObservableList inputList = PatientSelectionController.
            getNameList();
    private String controls;
    private final Boolean pluginCheck[] = new Boolean[4];
    private final int pluginCount = 3;

    private List<String> importPlugins;
    private List<String> interpreterPlugins;

    private final String defaultHelpPagePath = "/resources/default.md";
    //private  PluginHelpController pluginHelpController;
    private List<List<VaultEntry>> importedData;

    /**
     * initializes the controller class.
     *
     * @param location the url of Imports.fxml
     * @param resources the passed ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        accord.setExpandedPane(importTitledPane);
        importTitledPane.setExpanded(true);
        Font.loadFont(MainWindowController.class.getResource(
                "/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").
                toExternalForm(), 10);

        myResource = resources;
        // default value for the datepicker
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().withDayOfYear(LocalDate.now().
                getDayOfYear() - 31));

        pluginManager = OpenDiabetesPluginManager.getInstance();

        // the interpreter and import plugin lists
        importPlugins = pluginManager.getPluginIDsOfType(FileImporter.class);
        interpreterPlugins = pluginManager.getPluginIDsOfType(Interpreter.class);

        for (int i = 0; i < importPlugins.size(); i++) {
            FileImporter plugin = pluginManager.
                    getPluginFromString(FileImporter.class, importPlugins.
                            get(i));

            try {
                importDisplay.getChildren().add(createImportPlugin(importPlugins.
                        get(i), plugin));
            } catch (Exception ex) {
                showAlert(myResource.getString("import.somethingWentWrongAlertHeader"),
                        myResource.getString("import.importerCoulndtBeLoadedAlertContent"));

            }

        }

        importDisplay.setTranslateX(0);
        importDisplay.setTranslateY(0);

        importAllCheckBox.selectedProperty().addListener((
                ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)
                -> {
            if (!oldValue) {
                importAllButton.setDisable(false);
            } else {
                importAllButton.setDisable(true);
            }
        });

    }

    /**
     * Returns the css Styling for tooltips as a String. To add this Style to a
     * tooltip, simply write yourToolTip.setStyle(getToolTipStyle());
     *
     * @return The css styling as a String
     */
    public static String getTooltipStyle() {
        return "-fx-background-color: #F8F8F8;"
                + "-fx-text-fill: black;"
                + "-fx-font-family: 'Roboto';"
                + "-fx-font-size: 12;"
                + "-fx-background-radius: 0 0 0 0;";
    }

    /**
     * Remove every elment from the passed list, whose type isnt an instance of
     * clazz.
     *
     * @param <E> the type of elements in this list
     * @param list list which needs to be filtered
     * @param clazz the class to be filtered.
     * @return the filtered list
     */
    private <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        return list.stream().anyMatch(e -> clazz.isInstance(e));
    }

    /**
     * Creates a Importer plugin as TitledPane. All necessary elements for the
     * plugin will be created, designed and positioned. Methods will be assigned
     * to specific elements. If the passed plugin has compatible Interpreter,
     * new Interpreter specific elements will also be added.
     *
     * @param name the name of the plugin
     * @param plugin the passed plugin
     * @return the TitledPane
     * @throws Exception if the help page wasnt found
     */
    public TitledPane createImportPlugin(String name, FileImporter plugin) throws Exception {

        AnchorPane content = new AnchorPane();
        ProgressBar progress = new ProgressBar();

        String tmp;

        try {
            tmp = pluginManager.getHelpFilePath(plugin, HelpLanguage.LANG_EN).toString();
//            tmp = pluginManager.getHelpFilePath(plugin).toString();
        } catch (Exception ex) {
            tmp = defaultHelpPagePath;
        }

        final String helpPath = tmp;

        String file = "export/" + pluginManager.pluginToString(plugin) + "-0.0.1/"
                + pluginManager.pluginToString(plugin) + ".properties";

        importedData = new ArrayList<>();
        List<List<VaultEntry>> interpretedData = new ArrayList<>();
        Properties pro = new Properties();
        pro.load(new FileInputStream(file));
        plugin.loadConfiguration(pro);
        List<String> compatiblePlugins = plugin.getListOfCompatiblePluginIDs();

        List<Node> list = new ArrayList();
        final int i;
        Button interpreterButton = new Button(myResource.getString("import.interpreterButton"));

        if (!compatiblePlugins.isEmpty()) {
            pluginManager.pluginsFromStringList(compatiblePlugins);
            if (containsInstance(pluginManager.pluginsFromStringList(compatiblePlugins), Interpreter.class)) {
                FilteredList<String> filtered;
                filtered = new FilteredList<>(FXCollections.observableList(compatiblePlugins), (String p)
                        -> pluginManager.getPluginFromString(Interpreter.class, p) != null);
                ComboBox selectInterpreter = new ComboBox(FXCollections.observableArrayList(filtered));
                selectInterpreter.getSelectionModel().selectFirst();

                Rectangle comboBoxBox = new Rectangle();
                comboBoxBox.setWidth(230);
                comboBoxBox.setHeight(49);
                comboBoxBox.setSmooth(true);
                comboBoxBox.setId("comboBoxBox");

                Rectangle comboBoxRectangle = new Rectangle();
                comboBoxRectangle.setWidth(230);
                comboBoxRectangle.setHeight(1.5);
                comboBoxRectangle.setSmooth(true);
                comboBoxRectangle.setId("comboBoxRectangle");

                Label interpreterLabel = new Label(myResource.getString("import.fieldLabel"));
                interpreterLabel.setId("interpreterLabel");

                selectInterpreter.setPrefSize(230, 25);
                selectInterpreter.relocate(510, 55);
                interpreterButton.setDisable(true);
                interpreterButton.relocate(620, 100);
                interpreterButton.setPrefWidth(120);

                Circle hitBoxGearInterpreter = new Circle(10);
                hitBoxGearInterpreter.setOpacity(0);
                hitBoxGearInterpreter.setCursor(Cursor.HAND);
                hitBoxGearInterpreter.relocate(680, 10);

                Tooltip hitBoxGearInterpreterTip = new Tooltip();
                hitBoxGearInterpreterTip.setText(myResource.getString("import.interpreterControlToolTip"));
                hitBoxGearInterpreterTip.setStyle(getTooltipStyle());
                Tooltip.install(hitBoxGearInterpreter, hitBoxGearInterpreterTip);

                SVGPath gearInterpreter = createGear();

                Circle hitBoxHelpSignInterpreter = new Circle(10);
                hitBoxHelpSignInterpreter.setCursor(Cursor.HAND);
                hitBoxHelpSignInterpreter.setOpacity(0);
                hitBoxHelpSignInterpreter.relocate(720, 10);

                Tooltip hitBoxHelpSignInterpreterTip = new Tooltip();
                hitBoxHelpSignInterpreterTip.setText(myResource.getString("import.interpreterHelpToolTip"));
                hitBoxHelpSignInterpreterTip.setStyle(getTooltipStyle());
                Tooltip.install(hitBoxHelpSignInterpreter, hitBoxHelpSignInterpreterTip);

                SVGPath helpSignInterpreter = createHelpSing();

                helpSignInterpreter.relocate(hitBoxHelpSignInterpreter.getLayoutX() - 10, hitBoxHelpSignInterpreter.
                        getLayoutY() - 10);
                gearInterpreter.relocate(hitBoxGearInterpreter.getLayoutX() - 10, hitBoxGearInterpreter.
                        getLayoutY() - 10);

                hitBoxHelpSignInterpreter.setOnMouseClicked((MouseEvent event) -> {
                    Interpreter interpreterPlugin = pluginManager.getPluginFromString(Interpreter.class, selectInterpreter.getSelectionModel().getSelectedItem().toString());
                    String helpPagePath;
                    try {
                        helpPagePath = pluginManager.getHelpFilePath(interpreterPlugin, HelpLanguage.LANG_EN).toString();
//                        helpPagePath = pluginManager.getHelpFilePath(interpreterPlugin).toString();
                    } catch (Exception ex) {
                        helpPagePath = defaultHelpPagePath;
                    }
                    try {
                        openPluginHelp(helpPagePath, pluginManager.pluginToString(interpreterPlugin) + " " + myResource.
                                getString("import.pluginHelpTitle"));
                    } catch (IOException | URISyntaxException ex) {

                        showAlert(myResource.getString("import.somethingWentWrongAlertHeader"),
                                myResource.getString("import.helpPageCoulndtBeLoadedAlertContent"));
                    }

                });

                ////////////////////////////////////////
                // neue position impotieren
                list.addAll(Arrays.asList(comboBoxBox, comboBoxRectangle, interpreterButton, selectInterpreter,
                        interpreterLabel, helpSignInterpreter, hitBoxHelpSignInterpreter,
                        gearInterpreter, hitBoxGearInterpreter));

                interpreterButton.setOnAction((ActionEvent action) -> {
                    Interpreter interpreter
                            = pluginManager.getPluginFromString(Interpreter.class,
                                    (String) selectInterpreter.getSelectionModel().getSelectedItem());

                    for (int h = 0; h < importedData.size(); h++) {

                        interpretedData.add(interpreter.interpret(importedData.get(h)));
                    }
                });
            }
        }

        pluginManager.getCompatiblePluginIDs(plugin);

        Button importButton = new Button(myResource.
                getString("import.importButton"));
        importButton.setPrefWidth(100);
        importButton.relocate(390, 100);

        importAllButton.pressedProperty().addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                importButton.fire();
            }
        });

        importButton.setDisable(true);
        SVGPath gear = createGear();
        Circle hitBoxGear = new Circle(10);
        hitBoxGear.setOpacity(0);
        hitBoxGear.setCursor(Cursor.HAND);

        Tooltip importGearTip = new Tooltip();
        importGearTip.setText(myResource.getString("import.pluginControlToolTip"));
        importGearTip.setStyle(getTooltipStyle());
        Tooltip.install(gear, importGearTip);

        hitBoxGear.setOnMouseClicked(event -> {
            try {
                openPluginControll(name + " " + myResource.
                        getString("import.pluginControlTitle"));
            } catch (IOException ex) {
                showAlert(myResource.getString(myResource.getString("import.somethingWentWrongAlertContent")),
                        myResource.getString("import.controlPageCouldntBeOpenedAlertContent"));
            }
        });

        Circle hitBoxHelpSign = new Circle(10);
        hitBoxHelpSign.setCursor(Cursor.HAND);
        hitBoxHelpSign.setOpacity(0);
        hitBoxHelpSign.setOnMouseClicked((MouseEvent event) -> {
            try {
                openPluginHelp(helpPath, name + " " + myResource.
                        getString("import.pluginHelpTitle"));
            } catch (IOException | URISyntaxException ex) {
                showAlert(myResource.getString("import.somethingWentWrongAlertHeader"),
                        myResource.getString("import.helpPageCoulndtBeLoadedAlertContent"));
            }
        });

        Tooltip hitBoxHelpSignTip = new Tooltip();
        hitBoxHelpSignTip.setText(myResource.getString("import.pluginHelpToolTip"));
        hitBoxHelpSignTip.setStyle(getTooltipStyle());
        Tooltip.install(hitBoxHelpSign, hitBoxHelpSignTip);

        SVGPath helpSign = createHelpSing();
        hitBoxGear.relocate(420, 10);
        hitBoxHelpSign.relocate(460, 10);
        helpSign.relocate(hitBoxHelpSign.getLayoutX() - 10, hitBoxHelpSign.
                getLayoutY() - 10);
        gear.relocate(hitBoxGear.getLayoutX() - 10, hitBoxGear.
                getLayoutY() - 10);

        Tooltip helpSignTip = new Tooltip();
        helpSignTip.setText(myResource.getString("import.pluginHelpToolTip"));
        helpSignTip.setStyle(getTooltipStyle());
        Tooltip.install(helpSign, helpSignTip);

        Tooltip hitBoxGearTip = new Tooltip();
        hitBoxGearTip.setText(myResource.getString("import.pluginControlToolTip"));
        hitBoxGearTip.setStyle(getTooltipStyle());
        Tooltip.install(hitBoxGear, hitBoxGearTip);

        Button browseButton = new Button(myResource.
                getString("import.browseButton"));
        TextField field = new TextField();

        browseButton.setOnAction((ActionEvent action) -> {
            chooseFiles(action, field, importButton);
        });

        browseButton.relocate(18, 100);
        field.relocate(20, 55);
        field.setPrefWidth(465);
        field.setPrefHeight(25);
        field.setId("importPfad");
        field.setText(FileSystemView.getFileSystemView().getDefaultDirectory().
                getPath());
        browseButton.setPrefWidth(120);

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

        Label fieldLabel = new Label(myResource.getString("import.fieldLabel"));
        fieldLabel.setId("fieldLabel");

        list.add(fieldBox);
        list.addAll(Arrays.asList(fieldLabel, fieldRectangle, field, browseButton,
                importButton, gear, hitBoxGear, helpSign, hitBoxHelpSign));

        /////////////////////////
        TitledPane pane = new TitledPane("", content);

        content.getChildren().addAll(list);

        pane.setGraphic(createBorderPane(name, pane, progress, importButton));

        pane.setAnimated(true);

        importButton.setOnAction(e -> {
            try {
                String[] dataArray = field.getText().split(";");

                StatusListener st = (int j, String string) -> {

                    progress.setProgress(j / 100);

                };
                plugin.registerStatusCallback(st);

                for (int k = 0; k < dataArray.length; k++) {

                    importedData.add(plugin.importData(dataArray[k]));

                }
                
                VaultDao.initializeDb();
                VaultDao vaultDao = VaultDao.getInstance();
                
                for (List<VaultEntry> list1 : importedData) {
                    for (VaultEntry vaultEntry : list1) {
                        vaultDao.putEntry(vaultEntry);
                    }
                }
                
                MainWindowController.setImported(true);
                interpreterButton.setDisable(false);

            } catch (Exception ex) {
                showAlert(myResource.getString("import.somethingWentWrongAlertHeader"),
                        myResource.getString("import.importWentWrongAlertContent")
                        + "\n (" + pluginManager.pluginToString(plugin) + ")");

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
     * element will be positioned using methods of the BorderPane. Calls {@link #checkBoxListener(CheckBox box, TitledPane target, String name,
     * BorderPane bPane, Button button)}
     *
     *
     * @param name the title of a TitledPane
     * @param pane the respective a TitledPane
     * @param progress a ProgressBar
     * @param button the Button passed to the checkBoxListener
     * @return the BorderPane
     * @see #getFileString(Stage stage)
     *
     */
    public BorderPane createBorderPane(String name, TitledPane pane, ProgressBar progress, Button button) {
        BorderPane bPane = new BorderPane();
        CheckBox cBox = new CheckBox("");
        Tooltip cBoxTip = new Tooltip();
        cBoxTip.setText(myResource.getString("import.pluginCheckboxToolTip"));
        cBoxTip.setStyle(getTooltipStyle());
        cBox.setTooltip(cBoxTip);
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
        Region centerFiller = new Region();
        centerFiller.setMinWidth(20);
        Region rightFiller = new Region();
        rightFiller.setMinWidth(20);
        centerPane.setCenter(centerFiller);
        centerPane.setRight(rightFiller);
        bPane.setCenter(centerPane);
        bPane.setRight(progress);
        bPane.prefWidthProperty().bind(pane.widthProperty().subtract(200));
        checkBoxListener(cBox, pane, name, bPane, button);
        return bPane;
    }

    /**
     * Adds listener to CheckBoxe's selectedProperty(). CheckBox state
     * determines, if the TitledPane is collapsible.
     *
     * @param box the checkbox from the TitledPane
     * @param target the TitledPane
     * @param name title of the TitledPane
     * @param bPane the BorderPane inside of the TitledPane
     * @param button the Button, which should be deactivatable.
     */
    private void checkBoxListener(CheckBox box, TitledPane target, String name,
            BorderPane bPane, Button button) {
        box.selectedProperty().addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)
                -> {
            if (!newValue) {
                target.setExpanded(newValue);
                button.setDisable(!newValue);
            }
            target.setCollapsible(newValue);
            prefs.putBoolean(name + inputList.get(0), !newValue);
        });
    }

    private SVGPath createGear() {
        SVGPath gear = new SVGPath();
        gear.setContent("M19.43 12.98c.04-.32.07-.64.07-.98s-.03-.66-.07-.98l2."
                + "11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.39-.3-.61-."
                + "22l-2.49 1c-.52-.4-1.08-.73-1.69-.98l-.38-2.65C14.46 2."
                + "18 14.25 2 14 2h-4c-.25 0-.46.18-.49.42l-.38 2.65c-.61."
                + "25-1.17.59-1.69.98l-2.49-1c-.23-.09-.49 0-.61.22l-2 3."
                + "46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.07.65-.07.98s."
                + "03.66.07.98l-2.11 1.65c-.19.15-.24.42-.12.64l2 3.46c.12.22."
                + "39.3.61.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.03.24.24."
                + "42.49.42h4c.25 0 .46-.18.49-.42l.38-2.65c.61-.25 1.17-.59 1."
                + "69-.98l2.49 1c.23.09.49 0 .61-.22l2-3.46c.12-.22.07-.49-."
                + "12-.64l-2.11-1.65zM12 15.5c-1.93 0-3.5-1.57-3.5-3.5s1.57-3."
                + "5 3.5-3.5 3.5 1.57 3.5 3.5-1.57 3.5-3.5 3.5z");
        gear.setStyle("-fx-fill: #007399");
        return gear;
    }

    private SVGPath createHelpSing() {
        SVGPath helpSign = new SVGPath();
        helpSign.setContent("M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4."
                + "48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3."
                + "59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1."
                + "79-4 4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2."
                + "5 3-5 0-2.21-1.79-4-4-4z");
        /*helpSign.setRotationAxis(Rotate.X_AXIS);
        helpSign.setRotate(180);*/
        helpSign.setStyle("-fx-fill: #007399;");
        return helpSign;
    }

    /**
     * Opens the control page as a popup.
     *
     * @param name title of the window.
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     *
     *
     */
    private void openPluginControll(String name) throws IOException {
        openPage(PLUGIN_CONTROL_PAGE, name, false,
                myResource);
    }

    /**
     * Opens the window, which displays a help page.
     *
     * @param path the path to the help page
     * @param title the title of the help page
     * @throws java.io.IOException if fxml file or ResourceBundle wasnt found.
     * @throws java.net.URISyntaxException if path couldnt be parsed as URI
     *
     */
    public void openPluginHelp(String path, String title) throws IOException, URISyntaxException {
        URL url = getClass().getResource(PLUGIN_HELP_PAGE);
        FXMLLoader loader = new FXMLLoader(url,
                myResource);
        Parent root = loader.load();
        PluginHelpController pluginHelpController = loader.getController();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        pluginHelpController.loadHelpPage(path);
        stage.show();

    }

    /**
     * Saves the path of the chosen file inside a textfield and is assigend to
     * the #importButton.
     *
     * @see #getFileString(Stage stage)
     *
     * @param event calls method when triggered.
     * @param pathField the textfield in which the path is saved.
     * @param importButton the import button
     */
    @FXML
    private void chooseFiles(final ActionEvent event, final TextField pathField, Button importButton) {
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().
                getWindow();
        pathField.setText(getFileString(stage));
        importButton.setDisable(false);
    }

    /**
     * Opens a fileChooser. Multiple data paths will concatenated to one String
     * seperated by ;.
     *
     * @param stage stage which displays the filechooser
     * @return The path of the chosen file/files
     */
    private String getFileString(Stage stage) {

        FileChooser fileChooser = new FileChooser();

        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            files.forEach((item) -> {
                sb.append(item.getAbsolutePath()).append(";");
            });

            return sb.toString().substring(0, sb.length() - 1);

        }

        return "";
    }
}
