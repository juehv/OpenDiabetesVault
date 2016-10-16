/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app;

import de.jhit.openmediavault.app.gui.MainFrame;
import java.awt.Frame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author mswin
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Until Alpha test:
        // Until Beta Test:
        // * fast Statistics
        // * Better Meal registration
        // * Export Menu (select what should be exported)
        // * Import Options (Registered Unit, Name, BG IDs, Pump ID, Fom-To Dates) --> Show on first Page
        // Other:
        // * more Statistics
        // * Java FX GUI
        // * Google Activities
        // * Automatic Data Export?
        // * Help Pages
        // * Donate Button
        
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // do nothing
        }

        Frame window = new MainFrame();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
