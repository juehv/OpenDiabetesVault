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

        // Daten einlesen
        // Hypo, -> Messungen in 1h danach -> Körperliche Aktivität von Google -> letzte Mahlzeiten -> Schätzen welche es war
        // Hypers listen (>250)
        // Refills
        // Mahlzeitenübersicht
        // Anz gemessene Werte pro Tag
        
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
