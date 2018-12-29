/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.about;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import opendiabetesvaultgui.launcher.MainWindowController;

/**
 * FXML Controller class.
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class AboutPageController implements Initializable {

    /**
     * Hyperlink Element for License Information.
     */
    @FXML
    private Hyperlink licenseInformationLink;

    /**
     * Hyperlink Element for List of Contributers.
     */
    @FXML
    private Hyperlink listOfContributersLink;

    /**
     * Hyperlink Element for How to Contribute.
     */
    @FXML
    private Hyperlink contributionLink;

    /**
     * Hyperlink Element for Redistribution conditions.
     */
    @FXML
    private Hyperlink redistributionConditionsLink;

    /**
     * Hyperlink Element for Donate.
     */
    @FXML
    private Hyperlink donationLink;

    /**
     * The the passed ResourceBundle.
     */
    @FXML
    private ResourceBundle myResource;

    /**
     * Displays the main text of the page.
     */
    @FXML
    private TextArea text;

    /**
     * Initializes the controller class.
     *
     * @param url Url the url to About.fxml
     * @param rb ResourceBundle the used ResourceBundle for localization
     */
    @Override
    public final void initialize(final URL url, final ResourceBundle rb) {

        Font.loadFont(MainWindowController.class.getResource(
                "/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").
                toExternalForm(), 10);
        text.setPadding(new Insets(2));
    }

    /**
     *
     * NOT IMPLEMENTED YET. This method will be executed as soon as a link is
     * clicked.
     *
     * @param event when triggered calls method
     */
    @FXML
    private void handleLinkAction(final ActionEvent event) {
        //TODO implement basic Link opening.
    }
}
