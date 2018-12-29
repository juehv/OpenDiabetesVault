/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.process;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import opendiabetesvaultgui.launcher.FatherController;
import static opendiabetesvaultgui.launcher.FatherController.getPrimaryStage;

/**
 * FXML Controller class
 *
 * @author kai
 */
public class ProcessController extends FatherController implements Initializable {

  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    /*    try {
            // String markdown = "*kursiv*";  // hier müsste ein .md file geladen werden und in String gespeichert.
            String html;
            //html = opendiabetesVaultGui.markdownParser.Test.mdParse(markdown);
            html = Parser.mdParse("src/opendiabetesVaultGui/wiki/Process_help.md");
             WebEngine webEngine = webview.getEngine();
            webEngine.loadContent(html);
        } catch (FileNotFoundException ex) {
        
            Logger.getLogger(ProcessController.class.getName()).log(Level.SEVERE, null, ex);
        } */
 /*       group_two.getChildren().setAll(
                createTitledPane("Filter", true),
                createTitledPane("Filter 2", false),
                createTitledPane("irgendein Plugin", true),
                createTitledPane("testos 3", true),
                createTitledPane("testos 3", false)


        );
        
 group_one.getChildren().setAll(
                createTitledPane("TestPlugin", true),
                createTitledPane("Plugger", true),
                createTitledPane("Egal eigentlich", false)
        );
       ((TitledPane) (group_one.getChildren().get(0))).setExpanded(true);
       ((TitledPane) (group_two.getChildren().get(0))).setExpanded(true);

        Scene testScene = getPrimaryStage().getScene();
        
        one.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        two.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
       // one.widthProperty().
       
            
            
            
            
            
            
        
    }
     public TitledPane createTitledPane(String name, Boolean need_path) {

        AnchorPane content = new AnchorPane();
 // -------------------- IMPORT BUTTON -----------------------------------//
        Button import_button = new Button("Import");

        import_button.setLayoutX(50);
        import_button.setLayoutY(80);
        import_button.setPrefWidth(100);
 // -------------------- PROGRESSBAR -----------------------------------//

        ProgressBar progress = new ProgressBar();

        progress.setLayoutX(180);
        progress.setLayoutY(85);
        progress.setPrefWidth(150);
        progress.setPrefHeight(20);
       
// list for all added elements
        List<Node> list = new ArrayList<>(
                Arrays.asList(import_button, progress));
// if the plugin needs a path
        if (need_path) {
            Button browse_import = new Button("Browse");
            TextField field = new TextField();

      /*      browse_import.setOnAction((ActionEvent action) -> {
                choose_files_and_get_paths(action, field);
            }); 
            browse_import.setLayoutX(260);
            browse_import.setLayoutY(20);
            field.setLayoutX(30);
            field.setLayoutY(20);
            field.setPrefWidth(180);
            browse_import.setPrefWidth(120);

            list.add(field);
            list.add(browse_import);
        }
   // add all elements to the pane
        content.getChildren().addAll(list);
        TitledPane pane = new TitledPane(name, content);
        pane.setExpanded(false);

        return pane;
    } */
}
}