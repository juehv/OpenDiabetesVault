/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.login;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import opendiabetesvaultgui.launcher.FatherController;
import java.util.prefs.Preferences;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Schwind Laptop
 */
public class InputPasswordController extends FatherController implements Initializable {

    private final Preferences pref = Preferences.userNodeForPackage(InputPasswordController.class);
    @FXML
    private PasswordField firstEntry;
    @FXML
    private PasswordField passwordCheck;
    @FXML
    private Button savePasswordButton;
    @FXML
    private Rectangle passwordBox;
    @FXML
    private Rectangle passwordRectangle;
    @FXML
    private Label passwordLabel;
    @FXML
    private Rectangle secondPasswordBox;
    @FXML
    private Rectangle secondPasswordRectangle;
    @FXML
    private Label secondPasswordLabel;
    @FXML
    private ComboBox selectLanguage;
    @FXML
    private AnchorPane fatherPane;

    private ResourceBundle myResource;

    private final Color wrongColor = Color.web("ec4c4c");
    private final Color standartColor = Color.web("007399");
    private final Color backgroundColor = Color.web("E0E0E0");

    /**
     * Saves the users password as hash as a preference.
     *
     *
     * @param action call method when triggered
     * @throws java.security.NoSuchAlgorithmException if the requestes
     * alogorithm isnt available
     * @throws java.io.IOException if the fxml file or the ResourceBundle wasnt
     * found
     */
    @FXML
    public void savePassword(ActionEvent action) throws IOException, NoSuchAlgorithmException {

        // password cant be empty
        if (!("".equals(passwordCheck.getText()) || "".equals(firstEntry.getText()))) {

            // if the two entries are the same string
            if (firstEntry.getText().equals(passwordCheck.getText())) {
                MessageDigest hashFunction = MessageDigest.getInstance("SHA-256");
                // creates password hash
                hashFunction.update(firstEntry.getText().getBytes("UTF-8"));
                // saves password_hash as hex in a string
                String hash = String.format("%01x", new BigInteger(1, hashFunction.digest()));
                // saves password
                pref.put("properties", hash);
                // open LoginPage
                openPage(DATABASE_PATH_CHOOSER, myResource.getString("pathChooser.title"), false, myResource);

                // close this page
                firstEntry.getScene().getWindow().hide();
                // changes color of rectangle and text of label depending on user entry
            } else {

                secondPasswordRectangle.setFill(wrongColor);
                secondPasswordRectangle.setStroke(wrongColor);

                secondPasswordLabel.setText(myResource.getString("inputpw.entriesDontMatchText"));

                secondPasswordLabel.setTextFill(wrongColor);
                secondPasswordBox.setStroke(wrongColor);
                passwordCheck.clear();
            }
        } else {
            if (firstEntry.getText().isEmpty()) {
                passwordRectangle.setFill(wrongColor);
                passwordRectangle.setStroke(wrongColor);

                passwordLabel.setText(myResource.getString("inputpw.enterPasswordFirstText"));
                passwordLabel.setTextFill(wrongColor);
                passwordBox.setStroke(wrongColor);
            } else {
                passwordRectangle.setFill(standartColor);
                passwordRectangle.setStroke(standartColor);

                passwordLabel.setText(myResource.getString("inputpw.enterPasswordText"));
                passwordLabel.setTextFill(standartColor);
                passwordBox.setStroke(backgroundColor);
            }
            if (passwordCheck.getText().isEmpty()) {
                secondPasswordRectangle.setFill(wrongColor);
                secondPasswordRectangle.setStroke(wrongColor);

                secondPasswordLabel.setTextFill(wrongColor);
                secondPasswordBox.setStroke(wrongColor);
            }
        }

    }

    /**
     * This method fires the savePasswordButton when ENTER is pressed.
     *
     * @param e calls method when triggered
     */
    @FXML
    private void savePasswordKey(KeyEvent e) {
        // if enter pressed 
        if (e.getCode() == KeyCode.ENTER) {
            savePasswordButton.fire();
        }

    }

    /*
     * Initializes the controller class.
     *
     * @param location the url of OptionsWindow.fxml
     * @param resources the used resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myResource = resources;

        // set the current supported languages
        ALL_LANGUAGES.keySet().forEach((String e) -> {

            selectLanguage.getItems().add(e);
        });

        // load specific font
        Font.loadFont(InputPasswordController.class.getResource("/opendiabetesvaultgui/stylesheets/fonts/Roboto-Thin.ttf").toExternalForm(), 50);
        Font.loadFont(InputPasswordController.class.getResource("/opendiabetesvaultgui/stylesheets/fonts/Roboto-Regular.ttf").toExternalForm(), 50);
        // if no language was set
        if (PREFS_FOR_ALL.get(LANGUAGE_NAME, "").isEmpty()) {
            String javaDefault = java.util.Locale.getDefault().toString().substring(0, 2);
            // if system language is supported, set system language
            if (ALL_LANGUAGES.containsValue(javaDefault)) {
                String startLanguage = FOR_DEFAULT_LANGUAGE.getOrDefault(javaDefault, "");
                selectLanguage.setValue(startLanguage);
                PREFS_FOR_ALL.put(LANGUAGE_NAME, startLanguage);
                PREFS_FOR_ALL.put(LANGUAGE_DISPLAY, javaDefault);
                PREFS_FOR_ALL.put("dateFormat", "yyyy-MM-dd");

                // if the system language isnt supported yet
                // set the language to english
            } else {

                selectLanguage.setValue("English");
                PREFS_FOR_ALL.put(LANGUAGE_DISPLAY, ALL_LANGUAGES.getOrDefault("English", ""));
                PREFS_FOR_ALL.put(LANGUAGE_NAME, ALL_LANGUAGES.getOrDefault("", ""));

            }
        } else // shows the current language after reload
        {
            selectLanguage.setValue(PREFS_FOR_ALL.get(LANGUAGE_NAME, ""));
        }

        languageListener();

    }

    /**
     * Changes the language of the GUI. If a new language is selected, the value
     * assigned to LANGUAGE_DISPLAY and LANGUAGE_NAME will be updated. The fxml
     * file will be loaded again with a new locale saved in LANGUAGE_DISPLAY.
     */
    private void languageListener() {
        // if selected language changes
        selectLanguage.valueProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
            // saves the new language as ISO-639 language code
            PREFS_FOR_ALL.put(LANGUAGE_DISPLAY, ALL_LANGUAGES.getOrDefault(newValue, ""));
            // saves the language name
            PREFS_FOR_ALL.put(LANGUAGE_NAME, (String) newValue);
            // clears the content
            fatherPane.getChildren().clear();
            // and load it again with new locale
            FXMLLoader loader;
            loader = new FXMLLoader(InputPasswordController.this.getClass().getResource(PASSWORD_INPUT_PAGE), ResourceBundle.getBundle(RESOURCE_PATH, new Locale(PREFS_FOR_ALL.get(LANGUAGE_DISPLAY, ""))));
            try {
                fatherPane.getChildren().add(loader.load());
            } catch (IOException ex) {
                Logger.getLogger(InputPasswordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
