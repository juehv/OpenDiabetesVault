/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.patientselection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TablePosition;
import opendiabetesvaultgui.launcher.MainWindowController;
import javafx.scene.control.TableColumn.CellDataFeatures;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import opendiabetesvaultgui.launcher.FatherController;
import javafx.scene.text.Font;

/**
 * FXML Controller class.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
@SuppressWarnings("unchecked")
public class PatientSelectionController extends FatherController
        implements Initializable {

    @FXML
    private AnchorPane patientPane;
    @FXML
    private TableView tableView;
    @FXML
    private TextField searchbar;
    @FXML
    private SVGPath removeEntry;
    @FXML
    private Circle removeEntryHitbox;
    @FXML
    private SVGPath addEntryButton;
    @FXML
    private Circle addEntryHitbox;
    @FXML
    private SVGPath refreshButton;
    @FXML
    private Circle refreshHitbox;
    @FXML
    private SVGPath editEntryButton;
    @FXML
    private Circle editEntryHitbox;

    private ResourceBundle resource;

    private ObservableList<ObservableList> data;

    private ButtonType okButton;
    private ButtonType cancelButton;

    private double size;

    private static ObservableList nameList;
    private static Boolean edit;

    //public ObservableList addedPatient;
    private boolean selected;
    private Button button;
    private FilteredList<ObservableList> filtered;

    Connection c;

    /**
     * Saves the current selected patient.
     *
     * @param action Event: mouse click on the patient
     * @throws Exception if the page can't be switched
     */
    @FXML
    // saves the name of the selected Patients
    private void selectPatient(final MouseEvent action) throws Exception {
        // if a empty cell or the columns are selected
        if (!tableView.getSelectionModel().getSelectedCells().isEmpty()) {

            TablePosition focusedCell = (TablePosition) tableView.
                    getSelectionModel().getSelectedCells().get(0);

            int row = focusedCell.getRow();

            TableColumn col = focusedCell.getTableColumn();

            ObservableList list = (ObservableList) tableView.getItems().
                    get(row);

            if (!(tableView.getSelectionModel().isEmpty())) {

                MainWindowController.setSelected(true);
                MainWindowController.setImported(false);
                // TODO add if to set importet false only when the patient is changed
                getPrimaryStage().setTitle("OpenDiabetesVault - "
                        + nameList.get(2) + " " + nameList.get(1));

                if (action.getButton().equals(MouseButton.PRIMARY)
                        && action.getClickCount() == 2) {
                    System.out.print("");
                    getMainWindowController().switchPane(1);
                }
            }
        }
    }

    /**
     * closes the window.
     *
     * @param event Event: mouse click on the button
     */
    public final void closeWindow(final Event event) {
        button.getScene().getWindow().hide();
    }

    /**
     * removes an entry out of the patient database.
     *
     * @param action Event: mouse click on the remove button
     * @throws IOException if failed or interrupted I/O operations.
     * @throws SQLException if sql statements invalid.
     * @throws java.text.ParseException if an error has been reached while
     * parsing.
     */
    @FXML
    public final void removeAnEntry(final Event action)
            throws SQLException, ParseException, IOException {
        if (tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "", okButton);
            alert.setTitle("OpenDiabetesVault");
            alert.setHeaderText(resource.
                    getString("patient.noPatientSelectedalertHeader"));
            alert.setContentText(resource.
                    getString("patient.noPatientSelectedalertContent"));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource(
                    "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                    toExternalForm());
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource(ICON).toExternalForm()));

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION, "",
                    okButton, cancelButton);
            alert.setHeaderText(resource.
                    getString("patient.deletePatientAlertHeader"));
            alert.setTitle("OpenDiabetesVault");
            alert.setContentText(resource.
                    getString("patient.deletePatientAlertContent")
                    + System.lineSeparator() + nameList.get(1) + ", "
                    + nameList.get(2) + "");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource(
                    "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                    toExternalForm());
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource(ICON).toExternalForm()));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == okButton) {
                // ... user chose OK
                try {
                    Statement stmt = c.createStatement();
                    String query = "DELETE FROM PATIENT\n"
                            + "WHERE id='" + nameList.get(0) + "';"; //nameList.get(0) = the selected ID
                    ResultSet rs = stmt.executeQuery(query);
                    refreshTableView();
                } catch (SQLException e) {
                    System.out.println("Error on removing Entry");
                    //TODO: ALERT MESSAGE
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }

        }
    }

    /**
     * Opens the EditWindow.fxml file without any content inside of it
     *
     * @param action Event: mouse click on the add button
     * @throws IOException if failed or interrupted I/O operations.
     */
    @FXML
    public final void addEntry(final Event action)
            throws IOException {
        edit = false;
        openPage(EDIT_PAGE, resource.
                getString("edit.addtitle"), false, resource);
        getWindowStage().setOnHiding((WindowEvent e) -> {
            try {
                refreshTableView();
            } catch (IOException | ParseException ex) {
                Logger.getLogger(PatientSelectionController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });

    }

    /**
     * Retrieves all content of the database and stores it inside of the
     * tableView.
     *
     * @throws IOException if failed or interrupted I/O operations.
     * @throws java.text.ParseException if an error has been reached while
     * parsing.
     */
    @SuppressWarnings("empty-statement")
    public final void buildData() throws IOException, ParseException {
        //Connection c;
        data = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * from PATIENT";
            ResultSet rs = c.createStatement().executeQuery(SQL);

            //////////////
            for (int i = 1; i < (rs.getMetaData().getColumnCount() - 2); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().
                        getColumnName(i + 1));
                col.setCellValueFactory(
                        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            final CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).
                                toString());
                    }
                });
                col.setSortType(TableColumn.SortType.ASCENDING);
                col.setVisible(true);
                if (i == 1) {
                    col.setText(resource.getString("patient.talbeLastName"));
                } else if (i == 2) {
                    col.setText(resource.getString("patient.tableFirstName"));
                } else if (i == 3) {
                    col.setText(resource.getString("patient.tableDateofBirth"));
                } else if (i < 3) {
                    col.setText("Undefined");
                }
                tableView.getColumns().addAll(col);
                tableView.getSortOrder().add(col);
                System.out.println("Column [" + i + "] ");

            }

            /////////////
            while (rs.next()) {
                ObservableList<String> row = FXCollections.
                        observableArrayList();
                /*for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }*/
                String id = rs.getString(1);
                row.add(id);
                String firstname = rs.getString(3);
                row.add(firstname);
                String lastname = rs.getString(2);
                row.add(lastname);

                String dob = rs.getString(4);
                if ("dd.MM.yyyy".equals(PREFS_FOR_ALL.get("dateFormat", ""))) {
                    Date inputDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                    String outPutDate = new SimpleDateFormat("dd.MM.yyyy").format(inputDate);
                    row.add(outPutDate);
                } else {
                    row.add(dob);
                }
                String Hidden1 = rs.getString(5);
                row.add(Hidden1);
                String Hidden2 = rs.getString(6);
                row.add(Hidden2);
                System.out.println("Row added: " + row);
                data.add(row);
            }

            /////////////////////
        } catch (SQLException e) {

            System.out.println("Error on Building Data");
        }
    }

    /**
     * refreshes the tableview of the patient database.
     *
     * @throws IOException if an error occurs at the buildData() function.
     * @throws java.text.ParseException if an error has been reached while
     * parsing.
     * @see #buildData()
     */
    @FXML
    public final void refreshTableView() throws IOException, ParseException {
        tableView.getColumns().clear();
        buildData();
        filtered = new FilteredList<>(data, (ObservableList p) -> true);
        tableView.refresh();
        tableView.setItems(filtered);
        searchbar.setText("");
    }

    /**
     * Opens the EditWindow.fxml file with all information of the selected entry
     * already inside of it.
     *
     * @param action Event: mouse click on the edit button
     * @throws SQLException if an error has been reached while parsing.
     * @throws IOException if an error occurs at the buildData() function.
     */
    @FXML
    public final void editData(final Event action)
            throws SQLException, IOException {
        if (tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            Alert editWarning = new Alert(AlertType.WARNING, "", okButton);
            editWarning.setTitle("OpenDiabetesVault");
            editWarning.setHeaderText(resource.
                    getString("patient.noPatientSelectedalertHeader"));
            editWarning.setContentText(resource.
                    getString("patient.noPatientSelectedalertContent"));
            DialogPane dialogPane = editWarning.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource(
                    "/opendiabetesvaultgui/stylesheets/alertStyle.css").
                    toExternalForm());
            Stage stage = (Stage) editWarning.getDialogPane().getScene().
                    getWindow();
            stage.getIcons().add(new Image(getClass().getResource(ICON).toExternalForm()));
            editWarning.showAndWait();
        } else {
            edit = true;
            openPage(EDIT_PAGE, resource.getString(
                    "edit.edittitle"), false, resource);
            getWindowStage().setOnHiding((WindowEvent e) -> {
                try {
                    refreshTableView();
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(PatientSelectionController.class.
                            getName()).log(Level.SEVERE, null, ex);
                }
            }
            );

        }

    }

    /**
     * Returns the edit Boolean
     *
     * @return edit
     */
    public static Boolean isEdit() {
        return edit;
    }

    /**
     * Returns the nameList
     *
     * @return nameList
     */
    public static ObservableList getNameList() {
        return nameList;
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
     * Initializes the controller class.
     */
    @Override
    public final void initialize(URL location, ResourceBundle resources) {

        resource = resources;

        getPrimaryStage().setTitle("OpenDiabetesVault");
        cancelButton = new ButtonType(resource.
                getString("option.alertCancelButton"), ButtonBar.ButtonData.CANCEL_CLOSE);
        okButton = new ButtonType(resource.
                getString("option.alertOKButton"), ButtonBar.ButtonData.OK_DONE);

        Font.loadFont(PatientSelectionController.class.getResource(
                "/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").
                toExternalForm(), 50);
        Tooltip removeTip = new Tooltip();
        removeTip.setText(resource.getString("patient.deleteButtonToolTip"));
        removeTip.setStyle(getTooltipStyle());
        
        Tooltip addTip = new Tooltip();
        addTip.setText(resource.getString("patient.addButtonToolTip"));
        addTip.setStyle(getTooltipStyle());
        
        Tooltip editTip = new Tooltip();
        editTip.setText(resource.getString("patient.editButtonToolTip"));
        editTip.setStyle(getTooltipStyle());
        
        Tooltip.install(removeEntry, removeTip);
        Tooltip.install(removeEntryHitbox, removeTip);
        Tooltip.install(addEntryButton, addTip);
        Tooltip.install(addEntryHitbox, addTip);
        Tooltip.install(editEntryButton, editTip);
        Tooltip.install(editEntryHitbox, editTip);
        try {
            c = DBConnection.connect();
            Statement stmt = c.createStatement();
            DatabaseMetaData dbm = c.getMetaData();
            // check if "employee" table is there
            ResultSet tables = dbm.getTables(null, null, "PATIENT", null);
            if (!tables.next()) {
                System.out.println("TABLE DOES NOT EXIST");
                String query = "CREATE TABLE PATIENT(id INTEGER IDENTITY "
                        + "PRIMARY KEY, surname VARCHAR(256) NOT NULL, "
                        + "firstname VARCHAR(256) NOT NULL, dob DATE NOT NULL, "
                        + "HIDDEN1 VARCHAR(50), HIDDEN2 VARCHAR(50),);";
                ResultSet rs = stmt.executeQuery(query);
                System.out.println("TABLE CREATED");
            }

        } catch (SQLException e) {
            System.out.println("Error on removing Entry");
            //TODO: ALERT MESSAGE
        } catch (IOException ex) {
            Logger.getLogger(PatientSelectionController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PatientSelectionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            buildData();
        } catch (IOException | ParseException ex) {
            Logger.getLogger(PatientSelectionController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        filtered = new FilteredList<>(data, (ObservableList p) -> true);
        listenerAndFilter();
        // http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/

        //   SortedList<Patients> sortedList = new SortedList<>(filtered);
        tableView.setItems(filtered);
        tableView.getSelectionModel().selectedItemProperty().
                addListener((ObservableValue observableValue,
                        Object oldValue, Object newValue) -> {
                    //Check whether item is selected and set value of selected item to Label
                    if (tableView.getSelectionModel().getSelectedItem() != null) {
                        TableViewSelectionModel selectedModel = tableView.
                                getSelectionModel();
                        Class<? extends ObservableList> selectedCells = selectedModel.
                                getSelectedCells().getClass();
                        TablePosition focusedCell = (TablePosition) tableView.
                                getSelectionModel().getSelectedCells().get(0);

                        int row = focusedCell.getRow();

                        TableColumn col = focusedCell.getTableColumn();
                        // die Liste die Daniel braucht

                        nameList = (ObservableList) tableView.getItems().get(row);
                        // wie man auf die elemente zugreift
                        nameList.get(1);
                        nameList.get(2);
                        nameList.get(3);

                        System.out.print(tableView.getItems().get(row) + "\n");

                    }
                });
        size = tableView.getWidth();

    }

    /**
     *
     */
    private void listenerAndFilter() {
        searchbar.textProperty().addListener((ObservableValue<? extends String> observable, String notused, String searchfor) -> {
            filtered.setPredicate((ObservableList patient) -> {
                int i = 1;
                if (searchfor.isEmpty()) {
                    return true;
                }

                // the input string without spaces
                String shorterVersion = searchfor.replace(" ", "").
                        toLowerCase();
                // the amount of spaces in the input string
                int countSearch = searchfor.length() - shorterVersion.
                        length();
                // the amount of spaces in the firstname (a first name could consist of two words)
                int countFirstname = ((CharSequence) (patient.get(i + 1))).
                        length() - ((String) (patient.get(i + 1))).
                                replace(" ", "").length();

                // the search consists of more than column (for example (firstname, surname), (surname, birthday) etc.)
                if (countSearch > 0 && !(countSearch == countFirstname)) {

                    // the first word of the input is the surname
                    if (shorterVersion.startsWith(((String) (patient.get(i))).
                            toLowerCase())) {

                        String evenShorter = shorterVersion.
                                substring(((CharSequence) (patient.get(i))).
                                        length());
                        return ((String) patient.get(i + 1)).toLowerCase().
                                startsWith(evenShorter)
                                || ((String) patient.get(i + 2)).toLowerCase().
                                        startsWith(evenShorter);

                    } // the first word of the input is the firstname
                    else if (shorterVersion.startsWith(((String) (patient.
                            get(i + 1))).toLowerCase())) {

                        String evenShorter = shorterVersion.
                                substring(((CharSequence) (patient.get(i + 1))).
                                        length());
                        return ((String) patient.get(i + 0)).toLowerCase().
                                startsWith(evenShorter)
                                || ((String) patient.get(i + 2)).toLowerCase().
                                        startsWith(evenShorter);

                    } // the first word of the input is the date of birth
                    else if (shorterVersion.startsWith(((String) (patient.
                            get(i + 2))).toLowerCase())) {

                        String evenShorter = shorterVersion.
                                substring(((CharSequence) (patient.get(i + 2))).
                                        length());
                        return ((String) patient.get(i + 1)).toLowerCase().
                                startsWith(evenShorter)
                                || ((String) patient.get(i + 0)).toLowerCase().
                                        startsWith(evenShorter);

                    } // no match
                    else {
                        return false;
                    }

                    // the input consist of one column
                } else {

                    // the string contains a number
                    if (searchfor.matches(".*\\d+.*")) {

                        return (((String) (patient.get(i + 2))).toLowerCase().
                                startsWith(searchfor.toLowerCase()));

                    } // the string doesnt contain number
                    else {
                        // does a firstname match?
                        if (((String) (patient.get(i + 1))).toLowerCase().
                                startsWith(searchfor.toLowerCase())) {
                            return true;
                        }
                        // does a surname match?
                        return ((String) (patient.get(i + 0))).toLowerCase().
                                startsWith(searchfor.toLowerCase());
                    }
                }
            });
        });
    }
}
