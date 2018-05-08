/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.opendiabetes.vault.gui.launcher;

import de.opendiabetes.vault.engine.plugin.management.OpenDiabetesPluginManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import static de.opendiabetes.vault.gui.launcher.FatherController.LOGIN_PAGE;
import static de.opendiabetes.vault.gui.launcher.FatherController.PREFS_FOR_ALL;
import de.opendiabetes.vault.gui.login.InputPasswordController;

/**
 *
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 *
 */
public class Launch extends Application {

    /**
     * Java preferences.
     */
    private final Preferences prefs = Preferences.
            userNodeForPackage(InputPasswordController.class);

    @Override
    public final void start(final Stage stage) throws IOException, BackingStoreException {

        stage.setTitle("OpenDiabetesVault - Please Log in");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);

        File dbFile = new File(System.getProperty("user.home") + File.separator
                + ".odv" + File.separator + "database");
        if (!dbFile.exists()) {
            if (dbFile.mkdirs()) {
                System.out.println("Directory created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        Parent root;
        
        // if a password was set
        if ((Arrays.toString(prefs.keys())).contains("properties")) {
            URL url = getClass().getResource(LOGIN_PAGE);
            FXMLLoader loader = new FXMLLoader(url,
                    ResourceBundle.getBundle(FatherController.RESOURCE_PATH,
                            new Locale(FatherController.PREFS_FOR_ALL.
                                    get(FatherController.LANGUAGE_DISPLAY, ""))));
            root = loader.load();
            // the application was started the first time
        } else {
            URL url = getClass().getResource(FatherController.PASSWORD_INPUT_PAGE);
            FXMLLoader loader = new FXMLLoader(url,
                    ResourceBundle.getBundle(FatherController.RESOURCE_PATH));
//            , new Locale(
//                            FatherController.PREFS_FOR_ALL.get(FatherController.LANGUAGE_DISPLAY, java.util.Locale.
//                                    getDefault().toString()))));
            root = loader.load();

        }

        // prime plugin manager with path
        File pluginPath = new File(PREFS_FOR_ALL.get("pathDatabase", "")
                + File.separator + "plugins");
        System.out.println(pluginPath.getAbsolutePath());
        if (!pluginPath.exists()) {
            throw new IllegalAccessError("Your fucking file path is wrong!");
        }
        OpenDiabetesPluginManager.setPluginPath(pluginPath.getAbsolutePath());
        
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource(FatherController.ICON).toExternalForm()));

        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
