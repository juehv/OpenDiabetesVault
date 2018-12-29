/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.slice;

import com.google.gson.JsonObject;
import de.opendiabetes.vault.processing.filter.options.guibackend.FilterManagementUtil;
import de.opendiabetes.vault.processing.filter.options.guibackend.FilterNode;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.container.csv.VaultCSVEntry;
import de.opendiabetes.vault.data.VaultDao;
import de.opendiabetes.vault.plugin.exporter.Exporter;
import de.opendiabetes.vault.plugin.exporter.VaultExporter;
import de.opendiabetes.vault.plugin.fileimporter.FileImporter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import de.opendiabetes.vault.processing.filter.DateTimeSpanFilter;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import de.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.opendiabetes.vault.processing.filter.options.DateTimeSpanFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import javax.activation.MimetypesFileTypeMap;
import opendiabetesvaultgui.launcher.FatherController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hsqldb.lib.StringUtil;

/**
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class SliceController extends FatherController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private ListView<Node> listviewfilterelements;

    @FXML
    private Button filterbutton;

    @FXML
    private LineChart<Number, Number> filterchart;

    @FXML
    private StackPane filterchartstackpane;

    @FXML
    private BarChart<Number, Number> filterchartforevents;

    @FXML
    private StackPane filterchartforeventsstackpane;

    @FXML
    private CategoryAxis filterChartXaxis;

    @FXML
    private CategoryAxis sampleFilterChartXaxis;

    @FXML
    private NumberAxis filterChartYaxis;

    @FXML
    private ScrollPane filtercombinationfield;

    @FXML
    private HBox filterCombinationHbox;

    @FXML
    private ScrollPane filtersamplecombinationfield;

    @FXML
    private HBox filterSampleCombinationHbox;

    @FXML
    private ImageView imageViewForFilter;

    @FXML
    private ProgressBar importprogressbar;

    @FXML
    private Label maximportnumber;

    @FXML
    private Button nextbutton;

    @FXML
    private Button previousbutton;

    @FXML
    private Spinner currentimport;

    @FXML
    private Spinner hourspinnerbefore;

    @FXML
    private Spinner minutespinnerbefore;

    @FXML
    private Spinner hourspinnerafter;

    @FXML
    private Spinner minutespinnerafter;

    @FXML
    private CheckBox checkboxforsample;

    @FXML
    private VBox samplefilterinputvbox;

    @FXML
    private GridPane gridpaneforsamplefilter;

    @FXML
    private SplitPane splitpaneforfilter;

    @FXML
    private TabPane filtercharttabpane;

    @FXML
    private SplitPane chartandfiltersplitpane;

    private static final String FILTER_NAME = "FilterName";
    private static final String SEPARATOR = "%";
    private static final String COMBINE_FILTER = "CombineFilter";
    private static final String END_OF_SUBFILTER = "EndOfSubFilter";

    private List<VBoxChoiceBoxFilterNodesContainer> vboxChoiceBoxFilterNodesContainersForFilter = new ArrayList<>();
    private List<VBoxChoiceBoxFilterNodesContainer> vboxChoiceBoxFilterNodesContainersForSample = new ArrayList<>();

    private ObservableList itemsForChocieBox;

    private double mousePositionX;

    private double mousePositionY;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd.MM.yy");

    private Map<String, FilterNode> importedFilterNodes = new HashMap<>();

    List<FilterNode> sampleFilterNodes = new ArrayList<>();

    private boolean combineMode = false;

    private List<VaultEntry> importedData;

    private VaultDao vaultDao;
    private FilterManagementUtil filterManagementUtil;
    private boolean allValid = false;
    private String validationErrorMessage = "";

    final double SCALE_DELTA = 1.1;

    @FXML
    private void doSaveFilterCombination(ActionEvent event) {
        exportVBoxChoiceBoxFilterNodesContainers(vboxChoiceBoxFilterNodesContainersForFilter);
        if (checkboxforsample.isSelected()) {
            exportVBoxChoiceBoxFilterNodesContainers(vboxChoiceBoxFilterNodesContainersForSample);
        }
    }

    private void exportVBoxChoiceBoxFilterNodesContainers(List<VBoxChoiceBoxFilterNodesContainer> vboxChoiceBoxFilterNodesContainers) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Speichern");
        File file = fileChooser.showSaveDialog(MAIN_STAGE);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (VBoxChoiceBoxFilterNodesContainer vboxChoiceBoxFilterNodesContainer : vboxChoiceBoxFilterNodesContainers) {
                bufferedWriter.write(COMBINE_FILTER + SEPARATOR + vboxChoiceBoxFilterNodesContainer.getChoicebox().getSelectionModel().getSelectedItem().toString());
                bufferedWriter.newLine();

                for (FilterNode filterNode : vboxChoiceBoxFilterNodesContainer.getFilterNodes()) {
                    if (!filterNode.getName().contains(".txt")) {
                        bufferedWriter.write(FILTER_NAME + SEPARATOR + filterNode.getName());
                        bufferedWriter.newLine();

                        Iterator iterator = filterNode.getParameterAndValues().entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) iterator.next();
                            bufferedWriter.write(pair.getKey() + SEPARATOR + pair.getValue());
                            bufferedWriter.newLine();
                        }

                        //WeitereFilterNodes
                        iterator = filterNode.getParameterAndFilterNodes().entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) iterator.next();
                            bufferedWriter.write(pair.getKey() + SEPARATOR);
                            bufferedWriter.newLine();

                            writeFilterNode(bufferedWriter, (List<FilterNode>) pair.getValue());

                            bufferedWriter.write(END_OF_SUBFILTER + SEPARATOR);
                            bufferedWriter.newLine();

                        }
                    } else {
                        String message = "Importierte Filter können nicht exportiert werden";

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Speichern Fehlgeschlagen");
                        alert.setHeaderText("Speichern Fehlgeschlagen");
                        alert.setContentText(message);

                        alert.showAndWait();

                        bufferedWriter.close();
                        fileOutputStream.close();
                        return;
                    }

                }

            }

            bufferedWriter.close();
            fileOutputStream.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void writeFilterNode(BufferedWriter bufferedWriter, List<FilterNode> filterNodes) throws IOException {
        for (FilterNode filterNode : filterNodes) {
            bufferedWriter.write(FILTER_NAME + SEPARATOR + filterNode.getName());
            bufferedWriter.newLine();

            Iterator iterator = filterNode.getParameterAndValues().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                bufferedWriter.write(pair.getKey() + SEPARATOR + pair.getValue());
                bufferedWriter.newLine();
            }

            //WeitereFilterNodes
            iterator = filterNode.getParameterAndFilterNodes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                bufferedWriter.write(pair.getKey() + SEPARATOR);
                bufferedWriter.newLine();

                writeFilterNode(bufferedWriter, (List<FilterNode>) pair.getValue());

                bufferedWriter.write(END_OF_SUBFILTER + SEPARATOR);
                bufferedWriter.newLine();

            }
        }

    }

    BufferedReader bufferedReader;

    @FXML
    private void doLoadFilterCombination(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Laden");
        File file = fileChooser.showOpenDialog(MAIN_STAGE);

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            List<String> combineFiltersForImport = new ArrayList<>();
            List<List<FilterNode>> filterNodesForImport = new ArrayList<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplit = line.split(SEPARATOR);

                if (lineSplit[0].equals(COMBINE_FILTER)) {
                    combineFiltersForImport.add(lineSplit[1]);
                    filterNodesForImport.add(new ArrayList<>());
                } else if (lineSplit[0].equals(FILTER_NAME)) {
                    filterNodesForImport.get(filterNodesForImport.size() - 1).add(new FilterNode(lineSplit[1]));
                } else if (lineSplit.length == 1) {
                    FilterNode tempFilterNode = importSubFilter();
                    filterNodesForImport.get(filterNodesForImport.size() - 1).get(filterNodesForImport.get(filterNodesForImport.size() - 1).size() - 1).addParameterAndFilterNodes(lineSplit[0], tempFilterNode);
                } else {
                    filterNodesForImport.get(filterNodesForImport.size() - 1).get(filterNodesForImport.get(filterNodesForImport.size() - 1).size() - 1).addParam(lineSplit[0], lineSplit[1]);
                }

            }
            bufferedReader.close();

            List<Filter> filters = getFiltersFromLists(combineFiltersForImport, filterNodesForImport);
            importedFilterNodes.put(file.getName(), new FilterNode(file.getName(), filters));

            //Element für listview erstellen
            HBox hBox = new HBox();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);

            File imageFile = new File("src/opendiabetesvaultgui/shapes/gear.png");
            Image image = new Image(imageFile.toURI().toString());
            imageView.setImage(image);

            hBox.getChildren().add(imageView);

            Label label = new Label();
            label.setText(file.getName());
            hBox.getChildren().add(label);;

            listviewfilterelements.getItems().add(hBox);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private FilterNode importSubFilter() throws IOException {
        FilterNode result = new FilterNode("");

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineSplit = line.split(SEPARATOR);

            if (lineSplit[0].equals(FILTER_NAME)) {
                result.setName(lineSplit[1]);
            } else if (lineSplit.length == 1) {
                if (lineSplit[0].equals(END_OF_SUBFILTER)) {
                    break;
                } else {
                    FilterNode tempFilterNode = importSubFilter();
                    result.addParameterAndFilterNodes(lineSplit[0], tempFilterNode);
                }
            } else {
                result.addParam(lineSplit[0], lineSplit[1]);
            }

        }

        return result;
    }

    List<FilterResult> filterResultsForSplit;
    int filterResultPositionForSplit = -1;

    private void splitEntries() {
        Date beginDate = importedData.get(0).getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);

        filterResultsForSplit = new ArrayList<>();

        int hoursBefore = Integer.parseInt(hourspinnerbefore.getValue().toString());
        int minutesBefore = Integer.parseInt(minutespinnerbefore.getValue().toString());

        int hoursAfter = Integer.parseInt(hourspinnerafter.getValue().toString());
        int minutesAfter = Integer.parseInt(minutespinnerafter.getValue().toString());

        if (sampleFilterNodes != null && sampleFilterNodes.size() > 0) {
            Filter filter = filterManagementUtil.getFilterFromFilterNode(sampleFilterNodes.get(0), null);

            FilterResult tempFilterResult = filter.filter(importedData);

            for (VaultEntry vaultEntry : tempFilterResult.filteredData) {
                calendar.setTime(vaultEntry.getTimestamp());
                calendar.add(Calendar.HOUR_OF_DAY, -hoursBefore);
                calendar.add(Calendar.MINUTE, -minutesBefore);

                Date startDate = calendar.getTime();

                calendar.setTime(vaultEntry.getTimestamp());
                calendar.add(Calendar.HOUR_OF_DAY, hoursAfter);
                calendar.add(Calendar.MINUTE, minutesAfter);

                Date endDate = calendar.getTime();

                DateTimeSpanFilter dateTimeSpanFilter = new DateTimeSpanFilter(new DateTimeSpanFilterOption(startDate, endDate));

                filterResultsForSplit.add(dateTimeSpanFilter.filter(importedData));

            }
        } else {

            Date lastVaultEntryTimestamp = importedData.get(importedData.size() - 1).getTimestamp();

            if (filterResultsForSplit.size() > 0) {
                lastVaultEntryTimestamp = filterResultsForSplit.get(filterResultsForSplit.size() - 1).filteredData.get(filterResultsForSplit.get(filterResultsForSplit.size() - 1).filteredData.size() - 1).getTimestamp();
            }

            while (lastVaultEntryTimestamp.after(calendar.getTime())) {
                Date startDate = calendar.getTime();
                calendar.add(Calendar.HOUR_OF_DAY, hoursAfter + hoursBefore);
                calendar.add(Calendar.MINUTE, minutesAfter + minutesBefore);
                Date endDate = calendar.getTime();

                DateTimeSpanFilter dateTimeSpanFilter = new DateTimeSpanFilter(new DateTimeSpanFilterOption(startDate, endDate));

                filterResultsForSplit.add(dateTimeSpanFilter.filter(importedData));

            }

        }
        if (filterResultsForSplit != null && filterResultsForSplit.size() > 0) {
            filterResultPositionForSplit = 0;
            populateChart(filterResultsForSplit.get(filterResultPositionForSplit));
        } else {
            String message = "Das sampeln ist mit den gegebenen Parametern nicht möglich";

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sampeln Fehlgeschlagen");
            alert.setHeaderText("Sampeln Fehlgeschlagen:");
            alert.setContentText(message);

            alert.showAndWait();
        }

        /**
         * String message = "Ein Filter muss zum Sampeln ausgewählt werden";
         *
         * Alert alert = new Alert(AlertType.ERROR); alert.setTitle("Sampeln
         * Fehlgeschlagen"); alert.setHeaderText("Sampeln Fehlgeschlagen:");
         * alert.setContentText(message);
         *
         * alert.showAndWait();*
         */
    }

    @FXML
    private void getNextFilterSplit(ActionEvent event) {
        if (filterResultPositionForSplit >= 0 && filterResultPositionForSplit < filterResultsForSplit.size()) {
            filterResultPositionForSplit++;
            populateChart(filterResultsForSplit.get(filterResultPositionForSplit));
        }
    }

    @FXML
    private void getPreviousFilterSplit(ActionEvent event) {
        if (filterResultPositionForSplit > 0) {
            filterResultPositionForSplit--;
            populateChart(filterResultsForSplit.get(filterResultPositionForSplit));
        }
    }

    @FXML
    private void doFilter(ActionEvent event) {

        //Daten wieder laden um Filtern immer gleich zu machen
        importedData = vaultDao.queryAllVaultEntries();

        boolean splitFilter = checkboxforsample.isSelected();

        List<Filter> filters = getFiltersFromVBoxChoiceBoxFilterNodesContainer(vboxChoiceBoxFilterNodesContainersForFilter);

        List<Filter> sampleFilters = getFiltersFromVBoxChoiceBoxFilterNodesContainer(vboxChoiceBoxFilterNodesContainersForSample);

        if (allValid && validationErrorMessage.trim().isEmpty()) {
            if (filters != null) {
                //null entfernen normalerweise keine drinnen
                while (filters.remove(null));

                FilterResult filterResult = filterManagementUtil.sliceVaultEntries(filters, importedData);

                if (filterResult != null) {

                    importedData = filterResult.filteredData;

                    //For JavaFx
                    populateChart(filterResult);

                    //Plotteria
                    generateGraphs(filterResult, null);
                }
            }

            if (splitFilter) {
                splitEntries();
            }

            if (splitFilter && filterResultsForSplit != null && filterResultsForSplit.size() > 0 && sampleFilters != null && sampleFilters.size() > 0) {

                List<FilterResult> tempFilterResults = new ArrayList<>();

                for (FilterResult filterResult : filterResultsForSplit) {

                    FilterResult tempFilterResult = filterManagementUtil.sliceVaultEntries(sampleFilters, filterResult.filteredData);

                    if (tempFilterResult != null && tempFilterResult.filteredData.size() > 0) {
                        tempFilterResults.add(tempFilterResult);
                    }

                }

                filterResultsForSplit.clear();
                filterResultsForSplit.addAll(tempFilterResults);
                filterResultPositionForSplit = 0;
                if (filterResultsForSplit != null && filterResultsForSplit.size() > 0) {
                    populateChart(filterResultsForSplit.get(filterResultPositionForSplit));
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Keine Daten gefunden");
                    alert.setHeaderText("Keine Daten gefunden");

                    alert.showAndWait();
                }

            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nicht alle Daten gesetzt");
            alert.setHeaderText("Bitte setzen Sie die restlichen Daten");
            alert.setContentText(validationErrorMessage);

            alert.showAndWait();

            validationErrorMessage = "";
        }

    }

    @FXML
    private void doReset(ActionEvent event) {

        filterCombinationHbox.getChildren().removeAll(filterCombinationHbox.getChildren());
        filterSampleCombinationHbox.getChildren().removeAll(filterSampleCombinationHbox.getChildren());
        vboxChoiceBoxFilterNodesContainersForFilter.clear();
        vboxChoiceBoxFilterNodesContainersForSample.clear();

        addNewChoiceBoxAndSeperator(filterCombinationHbox, vboxChoiceBoxFilterNodesContainersForFilter);
        addNewChoiceBoxAndSeperator(filterSampleCombinationHbox, vboxChoiceBoxFilterNodesContainersForSample);

        samplefilterinputvbox.getChildren().removeAll(samplefilterinputvbox.getChildren());
        sampleFilterNodes.clear();

        importedData = vaultDao.queryAllVaultEntries();
        FilterResult filterResult = filterManagementUtil.getLastDay(importedData);
        populateChart(filterResult);
        generateGraphs(filterResult, null);
        validationErrorMessage = "";
        hourspinnerbefore.getValueFactory().setValue(0);
        minutespinnerbefore.getValueFactory().setValue(0);
        hourspinnerafter.getValueFactory().setValue(0);
        minutespinnerafter.getValueFactory().setValue(0);
        checkboxforsample.setSelected(false);
        gridpaneforsamplefilter.setVisible(checkboxforsample.isSelected());
        splitpaneforfilter.setDividerPosition(0, 1);
        chartandfiltersplitpane.setDividerPosition(0, 0.6);
    }

    @FXML
    private void changeCurrentImage(ActionEvent event) {
        int tmp = Integer.parseInt(currentimport.getValue().toString()) - 1;

        if (tmp <= currentMaxImportNumber && tmp > 0) {
            try {
                directoryPosition = tmp;
                if (directoryListing != null) {
                    imageViewForFilter.setImage(new Image(new FileInputStream(directoryListing[directoryPosition])));
                }

                if (directoryPosition == 0) {
                    previousbutton.setDisable(true);
                    nextbutton.setDisable(false);
                } else if (directoryPosition == currentMaxImportNumber) {
                    nextbutton.setDisable(true);
                    previousbutton.setDisable(false);
                } else {
                    nextbutton.setDisable(false);
                    previousbutton.setDisable(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void getPreviousImage(ActionEvent event) {
        currentimport.decrement();
    }

    @FXML
    private void getNextImage(ActionEvent event) {
        currentimport.increment();
    }

    private List<Filter> getFiltersFromVBoxChoiceBoxFilterNodesContainer(List<VBoxChoiceBoxFilterNodesContainer> vBoxChoiceBoxFilterNodesContainers) {
        List<List<FilterNode>> columnFilterNodes = new ArrayList<>();

        for (VBoxChoiceBoxFilterNodesContainer vboxChoiceBoxFilterNodesContainer : vBoxChoiceBoxFilterNodesContainers) {

            allValid = validateFilterNodes(vboxChoiceBoxFilterNodesContainer.getFilterNodes(), vboxChoiceBoxFilterNodesContainer.getVBox().getScene());

            columnFilterNodes.add(vboxChoiceBoxFilterNodesContainer.getFilterNodes());
        }

        List<Filter> filters = filterManagementUtil.combineFilters(getCurrentCombineFiltersFromVBoxChoiceBoxFilterNodesContainer(vBoxChoiceBoxFilterNodesContainers), columnFilterNodes);
        return filters;
    }

    private List<String> getCurrentCombineFiltersFromVBoxChoiceBoxFilterNodesContainer(List<VBoxChoiceBoxFilterNodesContainer> vBoxChoiceBoxFilterNodesContainers) {
        List<String> combineFilters = new ArrayList<>();
        for (VBoxChoiceBoxFilterNodesContainer vBoxChoiceBoxFilterNodesContainer : vBoxChoiceBoxFilterNodesContainers) {
            combineFilters.add(vBoxChoiceBoxFilterNodesContainer.getChoicebox().getSelectionModel().getSelectedItem().toString());
        }
        return combineFilters;
    }

    private List<Filter> getFiltersFromLists(List<String> combineFilters, List<List<FilterNode>> filterNodes) {
        return filterManagementUtil.combineFilters(combineFilters, filterNodes);
    }

    private void populateChart(FilterResult filterResult) {

        ObservableList<String> categories = FXCollections.observableArrayList();

        filterchart.getData().removeAll(filterchart.getData());
        filterchartforevents.getData().removeAll(filterchartforevents.getData());

        Map<String, List<VaultEntry>> clusteredVaultEnries = new HashMap<>();

        for (VaultEntry vaultEntry : filterResult.filteredData) {
            if (!clusteredVaultEnries.containsKey(vaultEntry.getType().toString())) {
                List<VaultEntry> vaultEntrys = new ArrayList<>();
                vaultEntrys.add(vaultEntry);
                clusteredVaultEnries.put(vaultEntry.getType().toString(), vaultEntrys);
            } else {
                clusteredVaultEnries.get(vaultEntry.getType().toString()).add(vaultEntry);
            }

            if (!categories.contains(simpleDateFormat.format(vaultEntry.getTimestamp()))) {
                categories.add(simpleDateFormat.format(vaultEntry.getTimestamp()));
            }

        }

        filterChartXaxis.setCategories(categories);
        filterChartXaxis.setAutoRanging(false);

        sampleFilterChartXaxis.setCategories(categories);
        sampleFilterChartXaxis.setAutoRanging(false);

        List<XYChart.Series> allSeriesNormal = new ArrayList<>();
        List<XYChart.Series> allSeriesEvent = new ArrayList<>();

        for (String key : clusteredVaultEnries.keySet()) {
            boolean isEvent = false;

            XYChart.Series series = new XYChart.Series();
            XYChart.Series seriesForEvents = new XYChart.Series();

            XYChart.Data data;
            XYChart.Data dataForEvents;

            for (VaultEntry vaultEntry : clusteredVaultEnries.get(key)) {
                if (vaultEntry.getType().isEvent()) {
                    dataForEvents = new Data(simpleDateFormat.format(vaultEntry.getTimestamp()), vaultEntry.getValue());
                    seriesForEvents.getData().add(dataForEvents);
                    isEvent = true;
                } else {
                    data = new Data(simpleDateFormat.format(vaultEntry.getTimestamp()), vaultEntry.getValue());
                    series.getData().add(data);
                    isEvent = false;
                }
            }

            if (isEvent) {
                seriesForEvents.setName(key);
                filterchartforevents.getData().add(seriesForEvents);
                allSeriesEvent.add(seriesForEvents);
            } else {
                series.setName(key);
                filterchart.getData().add(series);
                allSeriesNormal.add(series);
            }
        }
        //new ZoomManager(filterchartforeventsstackpane, filterchartforevents, allSeriesEvent);
        //new ZoomManager(filterchartstackpane, filterchart, allSeriesNormal);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Daten importieren
        vaultDao = VaultDao.getInstance();
        importedData = vaultDao.queryAllVaultEntries();

        filterManagementUtil = new FilterManagementUtil();

        //Grafik laden ggf. erstmal heutigen Tag
        exportDirectory = new File(exportFileDir);
        //emptyDirectoryForGraphs(exportDirectory);
        FilterResult filterResult = filterManagementUtil.getLastDay(importedData);
        populateChart(filterResult);
        generateGraphs(filterResult, null);

        //DragFileOnImageview
        imageViewForFilter.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }

            }
        }
        );

        imageViewForFilter.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    for (File file : db.getFiles()) {
                        if (file != null && file.exists()) {
                            generateGraphs(null, file.getAbsolutePath());
                        }
                    }
                }

            }
        }
        );

        //hide sampleFilter
        gridpaneforsamplefilter.setVisible(false);
        splitpaneforfilter.setDividerPosition(0, 1);

        //Choiceboxen füllen
        itemsForChocieBox = FXCollections.observableArrayList(filterManagementUtil.getCombineFilter());
        itemsForChocieBox.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String name = o1.toString();
                String name2 = o2.toString();
                return name.compareTo(name2);
            }
        });

        addNewChoiceBoxAndSeperator(filterCombinationHbox, vboxChoiceBoxFilterNodesContainersForFilter);
        addNewChoiceBoxAndSeperator(filterSampleCombinationHbox, vboxChoiceBoxFilterNodesContainersForSample);

        //ValueFactory for Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int steps) {
                for (int i = 0; i < steps; i++) {
                    decrementImageView();
                    this.setValue(this.getValue() - 1);
                }
                if (this.getValue() <= 0) {
                    this.setValue(1);
                }
            }

            @Override
            public void increment(int steps) {
                for (int i = 0; i < steps; i++) {
                    incrementImageView();
                    this.setValue(this.getValue() + 1);
                }
                if (this.getValue() > currentMaxImportNumber) {
                    this.setValue(currentMaxImportNumber);
                }
            }
        };
        valueFactory.setValue(1);
        currentimport.setValueFactory(valueFactory);

        //addItems to listview Filter
        ObservableList<Node> items = listviewfilterelements.getItems();
        items.addAll(getItemsForFilterListView());

        //Dragevent for Listview
        listviewfilterelements.setOnDragDetected(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                Dragboard dragBoard = listviewfilterelements.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();

                Label label = (Label) ((HBox) listviewfilterelements.getSelectionModel().getSelectedItem()).getChildren().get(1);

                content.putString(label.getText());
                dragBoard.setContent(content);

                event.consume();
            }
        });

        samplefilterinputvbox.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                mousePositionX = event.getX();
                mousePositionY = event.getY();
            }
        });

        samplefilterinputvbox.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                if (sampleFilterNodes.size() < 1) {
                    onDragDroppedFilter(event, samplefilterinputvbox, sampleFilterNodes);
                }
            }
        });

        checkboxforsample.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridpaneforsamplefilter.setVisible(checkboxforsample.isSelected());

                if (checkboxforsample.isSelected()) {
                    splitpaneforfilter.setDividerPosition(0, 0.5);
                } else {
                    splitpaneforfilter.setDividerPosition(0, 1);
                }
            }
        });

    }

    public void onDragOverFilter(DragEvent event) {
        if ((event.getGestureSource() != filtercombinationfield || event.getGestureSource() != filtersamplecombinationfield)
                && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.ANY);
            mousePositionX = event.getX();
            mousePositionY = event.getY();

            //filterbutton.setText("X: " + mousePositionX + " Y: " + mousePositionY);
        }

        event.consume();
    }

    public void onDragDroppedFilter(DragEvent event, Node node, List<FilterNode> filterNodes) {

        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
            success = onDragDropFilter(db.getString(), node, filterNodes);
        }

        event.setDropCompleted(success);

        event.consume();
    }

    private boolean onDragDropFilter(String name, Node node, List<FilterNode> filterNodes) {

        VBox tmpInputVBox = new VBox();
        tmpInputVBox.setSpacing(10);
        tmpInputVBox.setPadding(new Insets(10, 10, 10, 10));

        HBox tmpHeadHBox = new HBox();
        tmpHeadHBox.setSpacing(10);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        Image image = new Image(getIcon(name).toURI().toString());
        imageView.setImage(image);

        tmpHeadHBox.getChildren().add(imageView);

        FilterNode tmpNode;
        Map<String, Class> parameterClasses = filterManagementUtil.getParametersFromName(name);
        Iterator iterator = iterator = parameterClasses.entrySet().iterator();

        if (importedFilterNodes.get(name) != null) {
            tmpNode = importedFilterNodes.get(name);
        } else {
            tmpNode = new FilterNode(name);
        }

        //Input für Values                    
        Label label = new Label();
        label.setText(name);

        tmpInputVBox.setStyle("-fx-border-color:grey; -fx-background-radius: 10; -fx-border-radius: 10; -fx-box-shadow: 2 3;");

        tmpHeadHBox.getChildren().add(label);

        tmpInputVBox.getChildren().add(tmpHeadHBox);

        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();

            final String simpleName = (String) pair.getKey();
            final Class typeClass = (Class) pair.getValue();

            HBox tmpHBox = new HBox();
            tmpHBox.setAlignment(Pos.CENTER_LEFT);
            tmpHBox.setSpacing(10);
            //tmpHBox.setMaxWidth(200);

            tmpHBox.getChildren().add(new Label(simpleName));

            if (typeClass.getSimpleName().equals("Date")) {

                final Date dummyDate = importedData.get(0).getTimestamp();

                DatePicker datePicker = new DatePicker();
                datePicker.setId(getIdFromNodeAndString(tmpNode, simpleName));
                datePicker.setMaxWidth(100);
                datePicker.setPromptText("Date");
                datePicker.setValue(dummyDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                Callback<DatePicker, DateCell> dayCellFactory = getDayCellFactory();
                datePicker.setDayCellFactory(dayCellFactory);

                tmpNode.addParam(simpleName, datePicker.getValue().toString());

                tmpHBox.getChildren().add(datePicker);

                //ActionEvent for params
                datePicker.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        tmpNode.addParam(simpleName, datePicker.getValue().toString());
                    }
                });
            } else if (typeClass.getSimpleName().equals("boolean")) {
                CheckBox checkBox = new CheckBox();
                tmpHBox.getChildren().add(checkBox);
                tmpNode.addParam(simpleName, "" + checkBox.isSelected());

                //ActionEvent for params
                checkBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        tmpNode.addParam(simpleName, "" + checkBox.isSelected());
                    }
                });
            } else if (typeClass.getSimpleName().equals("Map")) {
                ChoiceBox choiceBox = new ChoiceBox();

                final FilterOption filterOption = filterManagementUtil.getFilterAndOptionFromName(name).getOption();

                ObservableList itemsForTmpChocieBox = FXCollections.observableArrayList(filterOption.getDropDownEntries().keySet());
                itemsForTmpChocieBox.sort(new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        String name = o1.toString();
                        String name2 = o2.toString();
                        return name.compareTo(name2);
                    }
                });
                choiceBox.setItems(itemsForTmpChocieBox);
                choiceBox.getSelectionModel().selectFirst();

                tmpHBox.getChildren().add(choiceBox);
                tmpNode.addParam(simpleName, filterOption.getDropDownEntries().get(choiceBox.getSelectionModel().getSelectedItem()));

                //ActionEvent for params
                choiceBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        tmpNode.addParam(simpleName, filterOption.getDropDownEntries().get(choiceBox.getSelectionModel().getSelectedItem()));
                    }
                });
            } else if (typeClass.getSimpleName().toLowerCase().contains("data")) {
                tmpNode.setData(importedData);
                tmpHBox.getChildren().remove(tmpHBox.getChildren().get(tmpHBox.getChildren().size() - 1));
            } else if (typeClass.getSimpleName().toLowerCase().contains("filter")) {
                ScrollPane filterScrollPane = new ScrollPane();
                VBox filterVBox = new VBox();
                filterVBox.setStyle("-fx-border-color:grey; -fx-background-radius: 10; -fx-border-radius: 10; -fx-box-shadow: 2 3;");
                filterVBox.setMinHeight(50);
                filterVBox.setMinWidth(50);

                tmpNode.setParameterAndFilterNodes(simpleName, new ArrayList<FilterNode>());

                //DragOver for vBox
                filterVBox.setOnDragOver(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        onDragOverFilter(event);
                    }
                });
                //Drag loslassen
                filterVBox.setOnDragDropped(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        onDragDroppedFilter(event, filterVBox, tmpNode.getParameterAndFilterNodesFromName(simpleName));

                    }
                });

                filterScrollPane.setContent(filterVBox);
                tmpHBox.getChildren().add(filterScrollPane);

            } else {
                TextField tmpTextField = new TextField();
                tmpTextField.setId(getIdFromNodeAndString(tmpNode, simpleName));
                tmpTextField.setMaxWidth(100);
                tmpTextField.setPromptText("Value");

                if (typeClass.getSimpleName().equals("LocalTime")) {
                    tmpTextField.setPromptText("00:00");
                    TextFormatter textFormatter = new TextFormatter(new LocalTimeStringConverter());
                    tmpTextField.setTextFormatter(textFormatter);
                    tmpNode.addParam(simpleName, "00:00");
                } else if (typeClass.getSimpleName().equals("int") || typeClass.getSimpleName().equals("long")) {
                    tmpTextField.setPromptText("0");
                    TextFormatter textFormatter = new TextFormatter(new NumberStringConverter());
                    tmpTextField.setTextFormatter(textFormatter);
                    tmpNode.addParam(simpleName, "0");
                } else if (typeClass.getSimpleName().equals("double") || typeClass.getSimpleName().equals("float")) {
                    tmpTextField.setPromptText("0.0");
                    TextFormatter textFormatter = new TextFormatter(new NumberStringConverter());
                    tmpTextField.setTextFormatter(textFormatter);
                    tmpNode.addParam(simpleName, "0.0");
                }

                tmpHBox.getChildren().add(tmpTextField);

                //ActionEvent for params
                tmpTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        tmpNode.addParam(simpleName, tmpTextField.getText() + event.getText());
                    }
                });
            }
            tmpInputVBox.getChildren().add(tmpHBox);
        }

        filterNodes.add(tmpNode);

        //LoeschButton
        Button deleteButton = new Button();
        deleteButton.setText("Entfernen");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (node instanceof VBox) {
                    ((VBox) node).getChildren().remove(tmpInputVBox);
                }

                if (node instanceof Pane) {
                    ((Pane) node).getChildren().remove(tmpInputVBox);
                }

                filterNodes.clear();

            }
        });

        tmpInputVBox.getChildren().add(deleteButton);

        if (node instanceof VBox) {
            ((VBox) node).getChildren().add(tmpInputVBox);
        }

        if (node instanceof Pane) {
            ((Pane) node).getChildren().add(tmpInputVBox);
        }

        return true;
    }

    private void addNewChoiceBoxAndSeperator(HBox hBox, List<VBoxChoiceBoxFilterNodesContainer> vBoxChoiceBoxFilterNodesContainers) {

        //Separator hinzufügen
        Separator separator = new Separator(Orientation.HORIZONTAL);
        hBox.getChildren().add(separator);

        //Vbox für Filter und ChoiceBox
        VBox vBox = new VBox();
        vBox.setSpacing(5);

        ChoiceBox choiceBox = new ChoiceBox(itemsForChocieBox);
        choiceBox.getSelectionModel().selectFirst();

        vBox.getChildren().add(choiceBox);
        hBox.getChildren().add(vBox);

        VBoxChoiceBoxFilterNodesContainer vboxChoiceBoxFilterNodesContainer = new VBoxChoiceBoxFilterNodesContainer(vBox, choiceBox, new ArrayList<>());

        vBoxChoiceBoxFilterNodesContainers.add(vboxChoiceBoxFilterNodesContainer);

        //DragOver for vBox
        vBox.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                onDragOverFilter(event);
            }
        });
        //Drag loslassen
        vBox.setOnDragDropped(new EventHandler<DragEvent>() {
            boolean newvBox = true;

            public void handle(DragEvent event) {
                if (newvBox) {
                    addNewChoiceBoxAndSeperator(hBox, vBoxChoiceBoxFilterNodesContainers);
                    newvBox = false;
                }
                onDragDroppedFilter(event, vboxChoiceBoxFilterNodesContainer.getVBox(), vboxChoiceBoxFilterNodesContainer.getFilterNodes());

            }
        });

    }

    //import stuff
    private String userDir = System.getProperty("user.dir");
    private String plotteriaPath = userDir + File.separator + "plotteria";
    private String plotPyPath = plotteriaPath + File.separator + "plot.py";
    private String configIniPath = plotteriaPath + File.separator + "config.ini";
    private String exportFileDir = plotteriaPath + File.separator + "temp";
    private String exportFilePath = plotteriaPath + File.separator + "export.csv";
    private File exportDirectory;
    private int directoryPosition = 0;
    private Thread exportThread;
    private float currentProgress = -1;
    File[] directoryListing;
    int currentMaxImportNumber = 0;
    Process exportProcess;

    private void generateGraphs(FilterResult filterResult, String path) {
        try {
            exportDirectory = new File(exportFileDir);
            emptyDirectoryForGraphs(exportDirectory);

            //Exportieren
            Exporter exporter = new VaultCsvExporterExtended();
            File file = new File(exportFilePath);
            file.createNewFile();

            if (path == null) {
                exportFilePath = plotteriaPath + File.separator + "export.csv";
                exporter.exportDataToFile(exportFilePath, filterResult.filteredData);
            } else {
                exportFilePath = path;
            }

            String command = "python " + plotPyPath + " -c " + configIniPath + " -d -f " + exportFilePath + " -o " + exportFileDir;

            killExportThread();

            currentProgress = 0;
            importprogressbar.setProgress(currentProgress);
            directoryPosition = 0;
            //python command
            Task<Void> exportTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Runtime runtime = Runtime.getRuntime();
                        exportProcess = runtime.exec(command);

                        while (currentProgress < 1) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(exportProcess.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                if (line.endsWith("%")) {
                                    line = line.replace("%", "");
                                    line = line.trim();
                                    currentProgress = Float.parseFloat(line) / 100;
                                    this.updateProgress(currentProgress, 1);

                                    //laden und nur pngs anzeigen
                                    directoryListing = exportDirectory.listFiles(new FileFilter() {
                                        public boolean accept(File file) {
                                            return file.isFile() && file.getName().toLowerCase().endsWith(".png");
                                        }
                                    });

                                    if (directoryListing != null && directoryListing.length > 0) {
                                        imageViewForFilter.setImage(new Image(new FileInputStream(directoryListing[directoryPosition])));
                                    }
                                }
                            }
                        }
                        currentProgress = 1;
                        succeeded();
                    } catch (Throwable t) {
                        //t.printStackTrace();
                        updateMessage(t.getMessage());
                        failed();
                    }
                    return null;
                }

            };

            exportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    directoryListing = exportDirectory.listFiles(new FileFilter() {
                        public boolean accept(File file) {
                            return file.isFile() && file.getName().toLowerCase().endsWith(".png");
                        }
                    });

                    currentMaxImportNumber = directoryListing.length;
                    maximportnumber.setText("/ " + currentMaxImportNumber);
                }
            });

            exportTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    String message = exportTask.getMessage();

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Import Fehlgeschlagen");
                    alert.setHeaderText("Import Fehlgeschlagen:");
                    alert.setContentText(message);

                    alert.showAndWait();
                }
            });

            importprogressbar.progressProperty().bind(exportTask.progressProperty());

            exportThread = new Thread(exportTask);
            exportThread.setDaemon(true);
            exportThread.start();

        } catch (Throwable t) {
            //t.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Import Fehlgeschlagen");
            alert.setHeaderText("Import Fehlgeschlagen:");
            alert.setContentText(t.getMessage());

            alert.showAndWait();

        }

    }

    private void killExportThread() {
        if (exportThread != null) {
            importprogressbar.progressProperty().unbind();
            if (exportProcess != null) {
                exportProcess.destroyForcibly();
            }
        }
    }

    private Exporter getVaultCSVExporter() {
        Exporter result = null;
        OpenDiabetesPluginManager pluginManager;
        pluginManager = OpenDiabetesPluginManager.getInstance();
        List<String> exportPlugins = pluginManager.getPluginIDsOfType(Exporter.class
        );

        for (int i = 0; i < exportPlugins.size(); i++) {
            Exporter plugin = pluginManager.getPluginFromString(Exporter.class,
                    exportPlugins.get(i));
            if (exportPlugins.get(i).equals("VaultCSVExporter")) {
                result = plugin;
            }
        }

        return result;
    }

    //DateTimePicker avaible Dates
    private Callback<DatePicker, DateCell> getDayCellFactory() {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        LocalDate firstDate = importedData.get(0).getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        LocalDate lastDate = importedData.get(importedData.size() - 1).getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        if (item.isBefore(firstDate) || item.isAfter(lastDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

    private void emptyDirectoryForGraphs(File directory) {
        try {
            imageViewForFilter.setImage(null);
            directoryListing = null;
            killExportThread();
            System.gc();
            FileUtils.cleanDirectory(directory);
        } catch (IOException ex) {
            Logger.getLogger(SliceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void incrementImageView() {
        try {

            if (exportDirectory != null) {

                directoryListing = exportDirectory.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().toLowerCase().endsWith(".png");
                    }
                });

                if (directoryListing != null && directoryListing.length > 0 && directoryListing.length - 1 > directoryPosition) {
                    directoryPosition++;
                    imageViewForFilter.setImage(new Image(new FileInputStream(directoryListing[directoryPosition])));
                    currentMaxImportNumber = directoryListing.length;
                    maximportnumber.setText("/ " + currentMaxImportNumber);

                    previousbutton.setDisable(false);
                    if (directoryListing.length - 1 == directoryPosition) {
                        nextbutton.setDisable(true);
                    }

                }

            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void decrementImageView() {
        try {
            if (exportDirectory != null && directoryPosition > 0) {
                directoryPosition--;

                directoryListing = exportDirectory.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().toLowerCase().endsWith(".png");
                    }
                });

                if (directoryListing != null && directoryListing.length > 0) {
                    imageViewForFilter.setImage(new Image(new FileInputStream(directoryListing[directoryPosition])));
                }

                currentMaxImportNumber = directoryListing.length;
                maximportnumber.setText("/ " + currentMaxImportNumber);

                if (directoryListing != null) {
                    imageViewForFilter.setImage(new Image(new FileInputStream(directoryListing[directoryPosition])));
                }

                nextbutton.setDisable(false);
                if (0 >= directoryPosition) {
                    previousbutton.setDisable(true);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private List<Node> getItemsForFilterListView() {
        List<Node> result = new ArrayList<Node>();
        List<String> filterNames = filterManagementUtil.getAllFilters();

        filterNames.sort(String.CASE_INSENSITIVE_ORDER);

        for (String filterName : filterNames) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            ImageView imageView = new ImageView();
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            Image image = new Image(getIcon(filterName).toURI().toString());
            imageView.setImage(image);

            hBox.getChildren().add(imageView);

            Label label = new Label();
            label.setText(filterName);
            hBox.getChildren().add(label);
            result.add(hBox);

        }

        return result;
    }

    private File getIcon(String filterName) {
        File result;

        if (filterName.toLowerCase().contains("date")) {
            result = new File("src/opendiabetesvaultgui/shapes/calendar.png");
        } else if (filterName.toLowerCase().contains("time")) {
            result = new File("src/opendiabetesvaultgui/shapes/time.png");
        } else if (filterName.toLowerCase().contains("type")) {
            result = new File("src/opendiabetesvaultgui/shapes/value.png");
        } else if (filterName.toLowerCase().contains("thres")) {
            result = new File("src/opendiabetesvaultgui/shapes/loading.png");
        } else {
            result = new File("src/opendiabetesvaultgui/shapes/gear.png");
        }

        return result;
    }

    private boolean validateFilterNodes(List<FilterNode> filterNodes, Scene scene) {
        boolean result = true;

        for (FilterNode filterNode : filterNodes) {
            result = validateFilterNode(filterNode, scene);
        }

        return result;
    }

    private boolean validateFilterNode(FilterNode filterNode, Scene scene) {
        boolean result = false;

        Iterator iterator = filterNode.getParameterAndFilterNodes().keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            List<FilterNode> tempNodes = filterNode.getParameterAndFilterNodesFromName(key);

            if (tempNodes != null && tempNodes.size() > 0) {
                for (FilterNode node : tempNodes) {
                    result = validateFilterNode(node, scene);
                }
            } else {
                result = false;
                validationErrorMessage += filterNode.getName() + ": " + key + " " + "\n";
            }
        }

        iterator = filterNode.getParameterAndValues().keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Node node = scene.lookup("#" + getIdFromNodeAndString(filterNode, key));

            if (node instanceof DatePicker) {

                String value = ((DatePicker) node).getValue().toString();

                if (!value.trim().isEmpty()) {
                    filterNode.addParam(key, value);
                    result = true;
                } else {
                    validationErrorMessage += filterNode.getName() + ": " + key + " " + "\n";
                }
            } else if (node instanceof CheckBox) {
                //immer gesetzt
                result = true;
            } else if (node instanceof ChoiceBox) {
                //immer gesetzt
                result = true;
            } else if (node instanceof TextField) {
                String value = ((TextField) node).getText();

                if (!value.trim().isEmpty()) {
                    filterNode.addParam(key, value);
                    result = true;
                } else {
                    validationErrorMessage += filterNode.getName() + ": " + key + " " + "\n";
                }
            }

        }

        return result;

    }

    private String getIdFromNodeAndString(FilterNode tmpNode, String simpleName) {
        return tmpNode.getId() + simpleName;
    }

    private void exportFilterResult(FilterResult filterResult, String filePath) {
        try {
            Exporter exporter = new VaultCsvExporterExtended();
            File file = new File(filePath);
            file.createNewFile();
            exporter.exportDataToFile(filePath, filterResult.filteredData);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @FXML
    private void doExportSample(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sample Exportieren");

        File file = directoryChooser.showDialog(MAIN_STAGE);

        int counter = 0;
        for (FilterResult filterResult : filterResultsForSplit) {
            exportFilterResult(filterResult, file.getAbsolutePath().toString() + File.separator + "SampleExport" + counter + ".csv");
            counter++;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText("Export abgeschlossen");
        alert.showAndWait();
    }

    @FXML
    private void resizeTabPane(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                chartandfiltersplitpane.setDividerPosition(0, 1);
            }
        }
    }
    
    @FXML
    private void resizeFilterPane(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                chartandfiltersplitpane.setDividerPosition(0, 0);
            }
        }
    }

}
