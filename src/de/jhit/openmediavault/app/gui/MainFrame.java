/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.gui;

import de.jhit.openmediavault.app.data.DataHelper;
import de.jhit.openmediavault.app.Launcher;
import de.jhit.openmediavault.app.container.DataEntry;
import de.jhit.openmediavault.app.data.CarelinkCsvImporter;
import de.jhit.openmediavault.app.preferences.Constants;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Jens
 */
public class MainFrame extends javax.swing.JFrame {

    private void setWaitCursor() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    private void setNormalCursor() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void setComonents(boolean isEnabled) {
        startDatePicker.setEnabled(isEnabled);
        endDatePicker.setEnabled(isEnabled);
        rangeChooserLabel.setEnabled(isEnabled);
        emailButton.setEnabled(isEnabled);
        jTabbedPane1.setEnabledAt(1, isEnabled);
        jTabbedPane1.setEnabledAt(2, isEnabled);
        jTabbedPane1.setEnabledAt(3, isEnabled);
        jTabbedPane1.setEnabledAt(4, isEnabled);
        jTabbedPane1.setEnabledAt(5, isEnabled);
    }

    private void updateCarelinkData() {
        // compute lists of interest
        bgListData = DataHelper.filterTimeRange(
                DataHelper.createCleanBgList(entrys),
                fromRagen, toRagen);
        hypoListData = DataHelper.createHypoList(bgListData,
                prefs.getDouble(Constants.HYPO_THRESHOLD_KEY,
                        Constants.HYPO_THRESHOLD_DEFAULT),
                prefs.getInt(Constants.HYPO_FOLLOW_TIME_KEY,
                        Constants.HYPO_FOLLOW_TIME_DEFAULT));
        hyperListData = DataHelper.createHyperList(bgListData,
                prefs.getDouble(Constants.HYPER_THRESHOLD_KEY,
                        Constants.HYPER_THRESHOLD_DEFAULT),
                prefs.getInt(Constants.HYPER_FOLLOW_TIME_KEY,
                        Constants.HYPER_FOLLOW_TIME_DEFAULT));
        primeListData = DataHelper.createCleanPrimeList(entrys);
        wizardListData = DataHelper.createCleanWizardList(entrys);

        // Update Gui Lists
        primeList.setListData(DataHelper.createGuiList(primeListData));
        hypoList.setListData(DataHelper.createGuiList(hypoListData));
        hyperList.setListData(DataHelper.createGuiList(hyperListData));

        // clear components
        hypoFollowingValuesList.clearSelection();
        hypoFollowingValuesList.setListData(new String[]{});
        hypoList.clearSelection();
        primeList.clearSelection();
        hyperList.clearSelection();

        // repaint components
        hypoFollowingValuesList.repaint();
        hypoList.repaint();
        primeList.repaint();
        hyperList.repaint();
    }

    private final Preferences prefs = Preferences.userNodeForPackage(Launcher.class);
    private Date fromRagen = new Date();
    private Date toRagen = new Date();
    private List<DataEntry> entrys = new ArrayList<>();
    private List<DataEntry> bgListData = new ArrayList<>();
    private List<DataEntry> hypoListData = new ArrayList<>();
    private List<DataEntry> hyperListData = new ArrayList<>();
    private List<DataEntry> primeListData = new ArrayList<>();
    private List<DataEntry> wizardListData = new ArrayList<>();

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        jTextField1.setVisible(false);

        // Init Date Picker
        //model.setDate(20,04,2014);        
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);

        startDatePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        jPanel1.add(startDatePicker);
        startDatePicker.setSize(100, 30);
        startDatePicker.setLocation(9, 148);

        endDatePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        jPanel1.add(endDatePicker);
        endDatePicker.setSize(100, 30);
        endDatePicker.setLocation(160, 148);

        // restore last path
        carelinkPathField.setText(prefs.get(Constants.CARELINK_CSV_FILE_LAST_PATH, ""));

        setComonents(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        carelinkPathField = new javax.swing.JTextField();
        carelinkPathBrowseButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        googleImportCheckbox = new javax.swing.JCheckBox();
        optionsButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        emailButton = new javax.swing.JButton();
        rangeChooserLabel = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hypoFollowingValuesList = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        hypoList = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        hyperList = new javax.swing.JList<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        primeList = new javax.swing.JList<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Study Data Helper");

        carelinkPathField.setEditable(false);

        carelinkPathBrowseButton.setText("Browse");
        carelinkPathBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carelinkPathBrowseButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Minimed Carelink Data:");

        googleImportCheckbox.setText("Import Google Activities");
        googleImportCheckbox.setEnabled(false);

        optionsButton.setText("Options");
        optionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsButtonActionPerformed(evt);
            }
        });

        importButton.setText("Import");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        emailButton.setText("E-Mail");
        emailButton.setEnabled(false);
        emailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailButtonActionPerformed(evt);
            }
        });

        rangeChooserLabel.setText("Bereichsauswahl:");
        rangeChooserLabel.setEnabled(false);

        jTextField1.setText("jTextField1");

        jLabel12.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(carelinkPathField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carelinkPathBrowseButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(optionsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(emailButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(rangeChooserLabel)
                            .addComponent(googleImportCheckbox)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(118, 118, 118)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 355, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carelinkPathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carelinkPathBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(googleImportCheckbox)
                .addGap(44, 44, 44)
                .addComponent(rangeChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optionsButton)
                    .addComponent(importButton)
                    .addComponent(emailButton))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Import", jPanel1);

        hypoFollowingValuesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(hypoFollowingValuesList);

        hypoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        hypoList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hypoListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(hypoList);

        jLabel8.setText("letzte hauptmahlzeit, 1h wenn unter schwelle");

        jLabel9.setText("körperliche aktivität (aus pumpe in den letzten stunden)");

        jLabel10.setText("geschlafen aus google + uhrzeit");

        jLabel13.setText("+ letzte pumpen akt");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel9))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(51, 51, 51)
                                        .addComponent(jLabel10))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addGap(120, 120, 120)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Hypo", jPanel2);

        hyperList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(hyperList);

        jLabel6.setText("nachfolgende werte in 24h wenn überzuckert (beendet serie wenn unter schwelle)");

        jLabel7.setText("letzte hauptmahlzeit + keton");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Hyper", jPanel3);

        primeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(primeList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Refill", jPanel4);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Food", jPanel6);

        jLabel2.setText("Refill every:");

        jLabel3.setText("BG AVG:");

        jLabel4.setText("No of Hypos:");

        jLabel5.setText("No of refills");

        jLabel11.setText("Bolus without wizard");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11))
                .addContainerGap(475, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Statistics", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void carelinkPathBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carelinkPathBrowseButtonActionPerformed
        String path = prefs.get(Constants.CARELINK_CSV_FILE_LAST_PATH, "");

        JFileChooser chooser = new JFileChooser(path);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Carelink Daten", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getAbsolutePath();
            carelinkPathField.setText(path);
            prefs.put(Constants.CARELINK_CSV_FILE_LAST_PATH, path);
        }

    }//GEN-LAST:event_carelinkPathBrowseButtonActionPerformed

    private void optionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsButtonActionPerformed
        Dialog window = new ImportOptionsDialog(this);
        window.setLocationRelativeTo(this);
        window.setVisible(true);
        updateCarelinkData();
    }//GEN-LAST:event_optionsButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        setWaitCursor();
        setComonents(false);
        // import csv data
        String path = prefs.get(Constants.CARELINK_CSV_FILE_LAST_PATH, "");
        File csvFile = new File(path);
        if (!csvFile.exists()) {
            JOptionPane.showMessageDialog(this, "Please select a File!",
                    "No File selected", JOptionPane.ERROR_MESSAGE);
            setNormalCursor();
            return;
        }
        try {
            // Parse data
            List<DataEntry> importData = CarelinkCsvImporter.parseCarelinkCsvExport(path);
            if (importData == null || importData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error while reading file.",
                        "Reading File Error", JOptionPane.ERROR_MESSAGE);
                setNormalCursor();
                return;
            }
            entrys = importData;
            updateCarelinkData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,
                    "Carelink CSV File not found", ex);
            JOptionPane.showMessageDialog(this, "Unknown Error\n"
                    + "Please send the log file to info@jensheuschkel-it.de",
                    "Error", JOptionPane.ERROR_MESSAGE);
            setNormalCursor();
            return;
        }
        // TODO import google data

        setComonents(true);
        setNormalCursor();
    }//GEN-LAST:event_importButtonActionPerformed

    private void emailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailButtonActionPerformed
        Desktop desktop;
        if (Desktop.isDesktopSupported()
                && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            try {
                URI mailto = new URI("mailto:john@example.com?subject=Hello%20World");
                desktop.mail(mailto);
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,
                        "Error while sending E-Mail", ex);
                JOptionPane.showMessageDialog(this, "E-Mail generation is not supported \n"
                        + "on your Device :(", "Not supported", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "E-Mail generation is not supported \n"
                    + "on your PC :(", "Not supported", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_emailButtonActionPerformed

    private void hypoListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hypoListMouseClicked
        setWaitCursor();
        int index = hypoList.getSelectedIndex();
        if (index < 0) {
            return;
        }
        DataEntry hypo = hypoListData.get(index);
        List<DataEntry> followingValues = DataHelper.filterFollowingHypoValues(
                bgListData, hypo.timestamp,
                prefs.getInt(Constants.HYPO_FOLLOW_TIME_KEY,
                        Constants.HYPO_FOLLOW_TIME_DEFAULT),
                prefs.getDouble(Constants.HYPO_THRESHOLD_KEY,
                        Constants.HYPO_THRESHOLD_DEFAULT));
        if (followingValues.isEmpty()) {
            // if no value in range, show info and next available entry
            DataEntry nextValue = DataHelper.filterNextValue(bgListData,
                    hypo.timestamp);
            String nextValueString = "";
            if (nextValue != null) {
                nextValueString = nextValue.toGuiListEntry();
            }
            hypoFollowingValuesList.setListData(new String[]{"No values in range ["
                + prefs.getInt(Constants.HYPO_FOLLOW_TIME_KEY,
                Constants.HYPO_FOLLOW_TIME_DEFAULT) + "]",
                nextValueString});
        } else {
            hypoFollowingValuesList.setListData(DataHelper.createGuiList(followingValues));
        }
        hypoFollowingValuesList.repaint();
        setNormalCursor();
    }//GEN-LAST:event_hypoListMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton carelinkPathBrowseButton;
    private javax.swing.JTextField carelinkPathField;
    private javax.swing.JButton emailButton;
    private javax.swing.JCheckBox googleImportCheckbox;
    private javax.swing.JList<String> hyperList;
    private javax.swing.JList<String> hypoFollowingValuesList;
    private javax.swing.JList<String> hypoList;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton optionsButton;
    private javax.swing.JList<String> primeList;
    private javax.swing.JLabel rangeChooserLabel;
    // End of variables declaration//GEN-END:variables
    private final UtilDateModel model = new UtilDateModel();
    private final Properties p = new Properties();
    private final JDatePanelImpl datePanel;
    private final JDatePickerImpl startDatePicker;
    private final JDatePickerImpl endDatePicker;
}
