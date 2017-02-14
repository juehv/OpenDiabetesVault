/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app;

import com.j256.ormlite.logger.LocalLog;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.openmediavault.app.gui.MainFrame;
import java.awt.Frame;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "INFO");
        // setup db
        try {
            VaultDao.initializeDb();
        } catch (SQLException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            // stop without db
            try {
                VaultDao.finalizeDb();
            } catch (IOException ex1) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.exit(-1);
        }

        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        Frame window = new MainFrame();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // add shutdown hook to destroy database
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    VaultDao.finalizeDb();
                } catch (IOException ex) {
                    Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
