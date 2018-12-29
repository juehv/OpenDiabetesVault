/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import opendiabetesvaultgui.about.AboutPageController;
import opendiabetesvaultgui.launcher.OptionsWindowController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

/**
 *
 * @author schae
 */
public class NewEmptyJUnitTest extends ApplicationTest{
    
    ColorPicker picker;
    TextField text;
    Button button;
    Spinner<Double> spinner;
    ComboBox<Color> combobox;
    Parent mainNode;
    
    public NewEmptyJUnitTest() {
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        mainNode = FXMLLoader.load(OptionsWindowController.class.getResource("./src/opendiabetesvaultgui/launcher/OptionsWindow.fxml"));
        System.out.println("Window found");
        stage.setScene(new Scene(mainNode));
        stage.show();
        /* Do not forget to put the GUI in front of windows. Otherwise, the robots may interact with another
        window, the one in front of all the windows... */
        stage.toFront();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    public <T extends Node> T find(final String query) {
        /** TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).query();
    }
    
    @Before
    public void setUp() {
        button = find("cancelButton");
    }
    
    @After
    public void tearDown() throws TimeoutException {
         /* Close the window. It will be re-opened at the next test. */
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

     @Test
    public void testWidgetsExist() {
        final String errMsg = "One of the widget cannot be retrieved anymore";
        
        assertNotNull(errMsg, button);
    }
    
    
}


