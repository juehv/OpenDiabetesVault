/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.opendiabetes.vault.gui.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.filechooser.FileSystemView;
import static de.opendiabetes.vault.gui.launcher.FatherController.LANGUAGE_DISPLAY;
import static de.opendiabetes.vault.gui.launcher.FatherController.PREFS_FOR_ALL;
import static de.opendiabetes.vault.gui.launcher.FatherController.RESOURCE_PATH;
import de.opendiabetes.vault.gui.patientselection.DBConnection;

/**
 * FXML Controller classe, manages the main window.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class MainWindowController extends FatherController
        implements Initializable {

    /**
     * the pane were everything is arranged. espacially the workflowbar
     * elements, the menu bar and the functionPane
     */
    @FXML
    private AnchorPane fatherPane;
    /**
     * the pane in which the different pages are loaded and displayed.
     */
    @FXML
    private AnchorPane functionPane;

    /**
     * SVGPath of the patient selection page workflowbar element.
     */
    @FXML
    private SVGPath patientNavigation;
    /**
     * SVGPath of the import page workflowbar element.
     */
    @FXML
    private SVGPath importNavigation;
    /**
     * SVGPath of the slice page workflowbar element.
     */
    @FXML
    private SVGPath sliceNavigation;
    /**
     * SVGPath of the the process page workflowbar element.
     */
    @FXML
    private SVGPath processNavigation;
    /**
     * SVGPath of the export page workflowbar element.
     */
    @FXML
    private SVGPath exportNavigation;

    /**
     * SVGPath of the patient selection page workflowbar check.
     */
    @FXML
    private SVGPath patientCheck;
    /**
     * SVGPath of the import page workflowbar check.
     */
    @FXML
    private SVGPath importCheck;
    /**
     * SVGPath of the slice page workflowbar check.
     */
    @FXML
    private SVGPath sliceCheck;
    /**
     * SVGPath of the process page workflowbar check.
     */
    @FXML
    private SVGPath processCheck;
    /**
     * SVGPath of the export page workflowbar check.
     */
    @FXML
    private SVGPath exportCheck;

    /**
     * workflowbar Label of the patient selection page.
     */
    @FXML
    private Label patientLabel;
    /**
     * workflowbar Label of the import page.
     */
    @FXML
    private Label importLabel;
    /**
     * workflowbar Label of the slice page.
     */
    @FXML
    private Label sliceLabel;
    /**
     * workflowbar Label of the process page.
     */
    @FXML
    private Label processLabel;
    /**
     * workflowbar Label of the export page.
     */
    @FXML
    private Label exportLabel;

    /**
     * MenuItem of MainWindow MenuBar FullScreen.
     */
    @FXML
    private MenuItem fullScreen;

    /**
     * MenuItem of MainWindow MenuBar FullScreen.
     */
    @FXML
    private MenuItem defaultSize;

    /**
     * MenuItem of MainWindow MenuBar Help.
     */
    @FXML
    private MenuItem menuItemHelp;

    /**
     * MenuItem of MainWindow MenuBar About.
     */
    @FXML
    private MenuItem menuItemAbout;

    /**
     * MenuItem of MainWindow MenuBar Option.
     */
    @FXML
    private MenuItem menuItemOption;

    /**
     * a rectangle to seperate the active page from the workflowbar.
     */
    @FXML
    private Rectangle seperatorBar;

    /**
     * the hashmap containing the workflowbar element for every page. the key is
     * the correlated page number: 0 for patient selection 1 for import 2 for
     * slice 3 for process 4 for export
     */
    private Map<Integer, NavigationBarElement> navigationbarElements;

    /**
     * constant to store patient selection id
     */
    private static final int PATIENT_SELECTION_ID = 0;
    /**
     * constant to store import id
     */
    private static final int IMPORT_ID = 1;
    /**
     * constant to store slice id
     */
    private static final int SLICE_ID = 2;
    /**
     * constant to store process id
     */
    private static final int PROCESS_ID = 3;
    /**
     * constant to store export id
     */
    private static final int EXPORT_ID = 4;
    /**
     * integer value page to show the current page ID. same as the respective
     * page ID (example: IMPORT_ID)
     */
    private static int page = 0;
    /**
     * The used ResourceBundle in MainWindowController. It will be set in
     * initialize.
     */
    private ResourceBundle usedResource;

    /**
     * boolean value to check if an patient has been selected.
     */
    private static boolean selected;
    /**
     * boolean value to check if files has been imported.
     */
    private static boolean imported;

    /**
     * The height of the navigationBar.
     */
    private final double navigationBarHeight = 44;
    /**
     * The padding used to relocate the checks.
     */
    private final double checksPaddingX = 10;
    /**
     * The padding used to relocate the checks.
     */
    private final double checksPaddingY = 3;
    /**
     * The padding used to relocate the svgPaths.
     */
    private final double svgPathPaddingX = 60;
    /**
     * the default window size. given as percentage displaysize
     */
    private final double defaultWindowSize = 0.7;

    /**
     * handles page(pane) switching while in mainwindow.
     * stackoverflow.com/questions/33748127/how-to-load-fxml-file-inside-pane
     *
     * @param targetPage the page to switch to, as an integer value 0 for
     * patient selection 1 for import 2 for slice 3 for process 4 for export
     * @throws java.io.IOException if a fxml file or ResourceBundle wasnt found.
     */
    public final void switchPane(final int targetPage)
            throws IOException {
        resizeNavigationBar();
        /**
         * switching is only possible: between patientSelection and import (back
         * and forth) from/to every page after the import was completed This
         * resets, when a new patient is selected
         */
        if (((targetPage == 0 && page == 1) || (targetPage == 1 && page == 0)
                || imported) && (targetPage != page)) {

            if (page == 0 && !selected && targetPage == 1 && !imported) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("OpenDiabetesVault");
                alert.setHeaderText(usedResource.
                        getString("main.continueAlertHeader"));
                alert.setContentText(usedResource.getString(
                        "main.continueAlertContent"));
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource(
                        FatherController.STYLESHEET_PATH+"/alertStyle.css").
                        toExternalForm());
                Stage stage = (Stage) alert.getDialogPane().getScene().
                        getWindow();
                /**
                 * stage.getIcons(). add(new
                 * Image(this.getClass().getResource(ICON).toString()));
                 */
                stage.getIcons().add(new Image(getClass().getResource(ICON).toExternalForm()));
                alert.showAndWait();

            } else {
                changePane(navigationbarElements.get(targetPage).getPagePath(), functionPane, usedResource);
                setSVGPathColor("seen", navigationbarElements.get(page).
                        getNavigationElement());
                setLabelColor("seen", navigationbarElements.get(page).
                        getLabelElement());
                setSVGPathColor("current", navigationbarElements.
                        get(targetPage).getNavigationElement());
                setLabelColor("current", navigationbarElements.get(targetPage).
                        getLabelElement());
                navigationbarElements.get(page).getCheckElement().
                        setVisible(true);
                for (int i = targetPage; i < navigationbarElements.size();
                        i++) {
                    navigationbarElements.get(i).getCheckElement().
                            setVisible(false);
                }
                setPage(targetPage);

                if (selected) {
                    setSelected(false);
                }
            }

        }
    }

    /**
     * Switches the current page to the patientselection page.
     *
     * @param e Event: mouse click on patientNavigation/patientLabel
     * @throws java.io.IOException if the PatientSelection.fxml or the
     * ResourceBundle wasnt found
     */
    @FXML

    private void clickPatientSelection(final MouseEvent e)
            throws IOException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(PATIENT_SELECTION_ID);
        }
    }

    /**
     * Switches the current page to the import page.
     *
     * @param e Event: mouse click on importNavigation/importLabel
     * @throws java.io.IOException if the Imports.fxml or the ResourceBundle
     * wasnt found
     *
     */
    @FXML
    private void clickImport(final MouseEvent e) throws IOException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(IMPORT_ID);
        }
    }

    /**
     * Switches the current page to the slice page.
     *
     * @param e Event: mouse click on processNavigation/processLabel
     * @throws java.io.IOException if the Slice.fxml or the ResourceBundle wasnt
     * found
     */
    @FXML
    private void clickSlice(final MouseEvent e) throws IOException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(SLICE_ID);
        }
    }

    /**
     * Switches the current page to the process page.
     *
     * @param e Event: mouse click on processNavigation/processLabel
     * @throws java.io.IOException if the Process.fxml or the ResourceBundle
     * wasnt found
     */
    @FXML
    private void clickProcess(final MouseEvent e) throws IOException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(PROCESS_ID);
        }
    }

    /**
     * Switches the current page to the export page.
     *
     * @param e Event: mouse click on exportNavigation/exportLabel
     * @throws java.io.IOException if Exports.fxml or the ResourceBundle wasnt
     * found
     *
     */
    @FXML
    public final void clickExport(final MouseEvent e) throws IOException {
        if (e.getButton() == MouseButton.PRIMARY) {
            switchPane(EXPORT_ID);
        }
    }

    /**
     * Not used right now. resets the pages back to the Patient selection page.
     *
     * @param action call method when triggered
     * @throws IOException Exception if patientSelection.fxml is not found
     */
    @FXML
    public final void reset(final ActionEvent action)
            throws IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("OpenDiabetesVault");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to reset your progress:");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource(
                        FatherController.STYLESHEET_PATH+"/alertStyle.css").
                        toExternalForm());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource(ICON).toExternalForm()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() != ButtonType.CANCEL) {
            changePane(navigationbarElements.get(0).getPagePath(), functionPane,
                    usedResource);
            for (int i = 0; i < navigationbarElements.size(); i++) {
                NavigationBarElement element = navigationbarElements.get(i);
                setLabelColor("unseen", element.getLabelElement());
                setSVGPathColor("current", element.getNavigationElement());
                element.getCheckElement().setVisible(false);
            }
            setPage(0);
        }
    }

    /**
     * Opens the about page as a popup.
     *
     * @param action ActionEvent: when triggerd calls the method
     * @throws IOException Exception if AboutPage.fxml isnt found
     */
    @FXML
    public final void openAboutPage(final ActionEvent action)
            throws
            IOException {

        openPageSingleInstance(ABOUT_PAGE,
                usedResource.getString("about.title"), false, usedResource);
    }

    /**
     * Opens the option page as a popup.
     *
     * @param action ActionEvent : when triggered calls the method
     * @throws IOException Exception if OptionsWindow.fxml isnt found
     */
    @FXML
    public final void openOptionWindow(final ActionEvent action)
            throws IOException {
        String oldLanguage = PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, "");
        String oldDateFormat = PREFS_FOR_ALL.get("dateFormat", "");

        openPage(MAIN_OPTIONS_WINDOW, usedResource
                .getString("option.title"), false, usedResource);

        getWindowStage().setOnHiding((WindowEvent e) -> {
            if (!oldLanguage.equals(PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, "")) || !oldDateFormat.equals(PREFS_FOR_ALL.get("dateFormat", ""))) {

                try {

                    FXMLLoader loader;

                    try {
                        DBConnection.closeConnection();
                    } catch (SQLException ex) {
                        Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    URL url = getClass().getResource(MAIN_PAGE);
                    loader = new FXMLLoader(url,
                            ResourceBundle.getBundle(RESOURCE_PATH,
                                    new Locale(PREFS_FOR_ALL.
                                            get(LANGUAGE_DISPLAY, ""))));
                    fatherPane.getChildren().clear();
                    Pane languageChange = loader.load();
                    setMainWindowController((MainWindowController) loader.
                            getController());
                    fatherPane.getChildren().add(languageChange);
                    AnchorPane pane = (AnchorPane) fatherPane.getChildren().
                            get(0);
                    AnchorPane.setBottomAnchor(pane, 0.0);
                    AnchorPane.setLeftAnchor(pane, 0.0);
                    AnchorPane.setRightAnchor(pane, 0.0);
                    AnchorPane.setTopAnchor(pane, 0.0);
                    getMainWindowController().resizeNavigationBar();
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    /**
     * Opens the help page as a popup.
     *
     * @param event Action event : when triggered starts the routine
     * @throws IOException Exception if help.fxml is not found
     */
    public final void openHelpPage(final Event event)
            throws IOException {
        openPageSingleInstance(MAIN_HELP_PAGE,
                usedResource.getString("help.title"), true, usedResource);
    }

    /**
     * This method sets the MainWindow to fullscreen. If the window is already
     * in fullscreen, it will be set back to windowed mode.
     *
     * @param e when triggered calls this method
     */
    @FXML
    public final void doFullScreen(final ActionEvent e) {
        if (!getPrimaryStage().isFullScreen()) {
            getPrimaryStage().setFullScreen(true);
            fullScreen.setText(usedResource.
                    getString("main.menuBarViewMenuItemExitFullScreen"));

        } else {
            getPrimaryStage().setFullScreen(false);
            fullScreen.setText(usedResource.
                    getString("main.menuBarViewMenuItemFullScreen"));
        }

    }

    /**
     * Sets the default Window size.
     *
     * @param e the Event to trigger it
     */
    @FXML
    public final void defaultSize(final ActionEvent e) {
        if (getPrimaryStage().isFullScreen()) {
            getPrimaryStage().setFullScreen(false);
        }
        getPrimaryStage().
                setWidth((SCREEN_BOUNDS.getWidth()) * defaultWindowSize);
        getPrimaryStage().
                setHeight((SCREEN_BOUNDS.getHeight()) * defaultWindowSize);
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

    @Override
    // Call at the beginning PatientSelection
    public final void initialize(final URL location,
            final ResourceBundle resources) {
        setImported(false);
        usedResource = resources;

        // Shortcut for Helppage
        this.menuItemHelp.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        // Shortcut for About
        this.menuItemAbout.setAccelerator(new KeyCodeCombination(KeyCode.F1,
                KeyCombination.SHIFT_DOWN));
        // Shortcut for Options
        this.menuItemOption.setAccelerator(new KeyCodeCombination(KeyCode.C,
                KeyCombination.CONTROL_DOWN));
        // Shortcut for FullScreen
        this.fullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F11));
        // Shortcut for DefaultSize
        this.defaultSize.setAccelerator(new KeyCodeCombination(KeyCode.F12));

        initializeNavigationbarElements();

        if (PREFS_FOR_ALL.get("pathDatabase", "").equals("")) {
            PREFS_FOR_ALL.put("pathDatabase",
                    (FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
                    + File.separator
                    + "ODV" + File.separator + "database"));
        }

        setPage(0);

        Font.loadFont(MainWindowController.class.getResource(
                FatherController.STYLESHEET_PATH+"/fonts/Roboto-Regular.ttf").
                toExternalForm(), 10);

        stageSizeChangeListener();
        setSVGPathColor("current", patientNavigation);
        try {
            changePane(PATIENT_SELECTION_PAGE, functionPane,
                    usedResource);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Tooltip patientSelectionTip = new Tooltip();
        patientSelectionTip.setText(usedResource.getString("main.patientSelectionToolTip"));
        patientSelectionTip.setStyle(getTooltipStyle());
        
        Tooltip importSelectionTip = new Tooltip();
        importSelectionTip.setText(usedResource.getString("main.importToolTip"));
        importSelectionTip.setStyle(getTooltipStyle());
        
        Tooltip sliceSelectionTip = new Tooltip();
        sliceSelectionTip.setText(usedResource.getString("main.sliceToolTip"));
        sliceSelectionTip.setStyle(getTooltipStyle());
        
        Tooltip processSelectionTip = new Tooltip();
        processSelectionTip.setText(usedResource.getString("main.processToolTip"));
        processSelectionTip.setStyle(getTooltipStyle());
        
        Tooltip exportSelectionTip = new Tooltip();
        exportSelectionTip.setText(usedResource.getString("main.exportToolTip"));
        exportSelectionTip.setStyle(getTooltipStyle());
        
        Tooltip.install(patientNavigation, patientSelectionTip);
        Tooltip.install(importNavigation, importSelectionTip);
        Tooltip.install(sliceNavigation, sliceSelectionTip);
        Tooltip.install(processNavigation, processSelectionTip);
        Tooltip.install(exportNavigation, exportSelectionTip);

    }

    /**
     *
     * Changes the color of SVGPath element.
     *
     * @param color the new color to set
     * @param changeMyColor the element on which the change shall apply
     */
    public final void setSVGPathColor(final String color,
            final SVGPath changeMyColor) {
        String hex;
        switch (color) {
            case "seen":
                hex = "#007399";
                break;
            case "current":
                hex = "#0096c9";
                break;
            case "unseen":
                hex = "#c3c3c3";
                break;
            default:
                hex = "#000000";
                System.out.println("THIS COLOR ISNT SUPPORTED ");
        }

        changeMyColor.setFill(Paint.valueOf(hex));

    }

    /**
     * Changes the color of an Label.
     *
     * @param color the new color to set
     * @param changeMyColor the Label on which the change shall apply
     */
    public final void setLabelColor(final String color,
            final Label changeMyColor) {
        String hex;
        switch (color) {
            case "current":
                hex = "#FFFFFF";
                break;
            case "seen":
                hex = "#FFFFFF";
                break;
            case "unseen":
                hex = "#000000";
                break;
            default:
                hex = "#FF69b4";
                System.out.println("THIS LANGUAGE ISNT SUPPORTED");
        }

        changeMyColor.setTextFill(Paint.valueOf(hex));

    }

    /**
     * resizes the navigation bar in order to the window size.
     */
    private void resizeNavigationBar() {
        double windowWidth = FatherController.getPrimaryStage().getWidth();
        double originalWidth = importNavigation.getLayoutBounds().getWidth();
        double requiredWidth = windowWidth * (1.0 / navigationbarElements.
                size());
        double scale = (requiredWidth + 23) / originalWidth;

        //******* relocate and scale the start page elements ************//
        //naviagtion element
        navigationbarElements.get(0).getNavigationElement().setScaleX(scale);
        navigationbarElements.get(0).getNavigationElement().
                relocate(requiredWidth / 2, navigationBarHeight);
        //label
        navigationbarElements.get(0).getLabelElement().
                setLayoutX(navigationbarElements.get(0).getNavigationElement().
                        getLayoutX());
        //check element
        navigationbarElements.get(0).getCheckElement().
                relocate(navigationbarElements.get(0).getLabelElement().
                        getLayoutX() + navigationbarElements.
                                get(0).getLabelElement().getWidth()
                        + checksPaddingX, navigationbarElements.get(0).
                                getLabelElement().getLayoutY()
                        + checksPaddingY);

        //******* relocate and scale all other elements ************//
        for (int i = 1; i < navigationbarElements.size(); i++) {
            NavigationBarElement element = navigationbarElements.get(i);
            //Navigation elements
            element.getNavigationElement().setScaleX(scale);
            element.getNavigationElement().
                    relocate(navigationbarElements.get(i - 1).
                            getNavigationElement().getLayoutX() + requiredWidth,
                            navigationBarHeight);
            //labels
            element.getLabelElement().setLayoutX(element.getNavigationElement()
                    .getLayoutX() + svgPathPaddingX);
            //check element
            element.getCheckElement().relocate(element.getLabelElement().
                    getLayoutX() + element.getLabelElement().getWidth()
                    + checksPaddingX, element.getLabelElement().getLayoutY()
                    + checksPaddingY);
        }
        seperatorBar.setWidth(windowWidth);
    }

    /**
     * listens to size changes on the main stage.
     *
     * calls resizeNavigationBar if the width is changed
     *
     * https://stackoverflow.com/questions/47044623/stage-resize-event-javafx
     */
    private void stageSizeChangeListener() {
        getPrimaryStage().widthProperty().
                addListener((ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) -> {
                    resizeNavigationBar();
                });
    }

    /**
     * Initializes Hashmap which indicates the order and the different elements
     * of each workflow pane.
     *
     */
    private void initializeNavigationbarElements() {
        navigationbarElements = new HashMap<>();
        navigationbarElements.put(PATIENT_SELECTION_ID, new NavigationBarElement(patientNavigation,
                patientCheck, patientLabel, PATIENT_SELECTION_PAGE));
        navigationbarElements.put(IMPORT_ID, new NavigationBarElement(importNavigation,
                importCheck, importLabel, IMPORT_PAGE));
        navigationbarElements.put(SLICE_ID, new NavigationBarElement(sliceNavigation,
                sliceCheck, sliceLabel, SLICE_PAGE));
        navigationbarElements.put(PROCESS_ID, new NavigationBarElement(processNavigation,
                processCheck, processLabel, PROCESS_PAGE));
        navigationbarElements.put(EXPORT_ID, new NavigationBarElement(exportNavigation,
                exportCheck, exportLabel, EXPORT_PAGE));
    }

    /**
     *
     * @return importerd
     */
    public static boolean isImported() {
        return imported;
    }

    /**
     * Sets imported.
     *
     * @param isImported the value which imported is set to.
     */
    public static void setImported(final boolean isImported) {
        MainWindowController.imported = isImported;
    }

    /**
     *
     * @return selected
     */
    public static boolean isSelected() {
        return selected;
    }

    /**
     * Sets selected.
     *
     * @param isSelected the value selected is set to.
     */
    public static void setSelected(final boolean isSelected) {
        MainWindowController.selected = isSelected;
    }

    /**
     * returns the actual page value.
     *
     * @return int
     */
    public static int getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param pageValue the value page is set to
     */
    public static void setPage(final int pageValue) {
        MainWindowController.page = pageValue;

    }

    /**
     * returns the actual page language.
     *
     * @return String "de" for german or " " for default english
     */
    public static String getLanguage() {
        return PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, "");
    }

    /**
     * PATIENT_SELECTION_ID getter.
     *
     * @return the PATIENT_SELECTION_ID value
     */
    public static int getPATIENT_SELECTION_ID() {
        return PATIENT_SELECTION_ID;
    }

    /**
     * IMPORT_ID getter.
     *
     * @return the PATIENT_SELECTION_ID value
     */
    public static int getIMPORT_ID() {
        return IMPORT_ID;
    }

    /**
     * SLICE_ID getter.
     *
     * @return the SLICE_ID value
     */
    public static int getSLICE_ID() {
        return SLICE_ID;
    }

    /**
     * PROCESS_ID getter.
     *
     * @return the PROCESS_ID value
     */
    public static int getPROCESS_ID() {
        return PROCESS_ID;
    }

    /**
     * EXPORT_ID getter.
     *
     * @return the EXPORT_ID value
     */
    public static int getEXPORT_ID() {
        return EXPORT_ID;
    }
}
