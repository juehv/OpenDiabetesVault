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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
//        datePanel.setEnabled(isEnabled);
//        startDatePicker.setEnabled(isEnabled);
//        endDatePicker.setEnabled(isEnabled);
        emailButton.setEnabled(isEnabled);
        jTabbedPane1.setEnabledAt(1, isEnabled);
        jTabbedPane1.setEnabledAt(2, isEnabled);
        jTabbedPane1.setEnabledAt(3, isEnabled);
        jTabbedPane1.setEnabledAt(4, isEnabled);
        jTabbedPane1.setEnabledAt(5, false);

    }

    private void updateCarelinkData() {
        // compute lists of interest
//        bgListData = DataHelper.filterTimeRange(
//                DataHelper.createCleanBgList(entrys),
//                fromRagen, toRagen);
        bgListData = DataHelper.createCleanBgList(entrys);
        hypoListData = DataHelper.createHypoList(bgListData,
                currentHypoThreshold,
                prefs.getInt(Constants.HYPO_FOLLOW_TIME_KEY,
                        Constants.HYPO_FOLLOW_TIME_DEFAULT));
        hyperListData = DataHelper.createHyperList(bgListData,
                currentHyperThreshold,
                prefs.getInt(Constants.HYPER_FOLLOW_TIME_KEY,
                        Constants.HYPER_FOLLOW_TIME_DEFAULT));
        primeListData = DataHelper.createCleanPrimeList(entrys);
        bolusWizardKeListData = DataHelper.createCleanFoodBolusList(entrys);
        exerciseMarkerListData = DataHelper.createExerciseMarkerList(entrys);

        // Update Gui components
        primeList.setListData(DataHelper.createGuiList(primeListData));
        hypoList.setListData(DataHelper.createGuiList(hypoListData));
        hyperList.setListData(DataHelper.createGuiList(hyperListData));
        bolusWizardKeList.setListData(DataHelper.createGuiList(bolusWizardKeListData));

        // clear secondary components
        hypoList.clearSelection();
        hypoFollowingValuesList.clearSelection();
        exerciseHistoryList.clearSelection();
        hypoLastMealList.clearSelection();
        hypoFollowingValuesList.setListData(new String[]{});
        exerciseHistoryList.setListData(new String[]{});
        hypoLastMealList.setListData(new String[]{});
        hyperList.clearSelection();
        hyperFollowingValuesList.clearSelection();
        hyperLastMealList.clearSelection();
        hyperFollowingValuesList.setListData(new String[]{});
        hyperLastMealList.setListData(new String[]{});
        primeList.clearSelection();
        lastPrimeLabel.setVisible(false);
        sleepRadioButtonGroup.clearSelection();

        // repaint components
//        hypoFollowingValuesList.repaint();
//        hypoList.repaint();
//        primeList.repaint();
//        hyperList.repaint();
    }

    private final Preferences prefs = Preferences.userNodeForPackage(Launcher.class);
//    private Date fromRagen = new Date();
//    private Date toRagen = new Date();
    private List<DataEntry> entrys = new ArrayList<>();
    private List<DataEntry> bgListData = new ArrayList<>();
    private List<DataEntry> hypoListData = new ArrayList<>();
    private List<DataEntry> hyperListData = new ArrayList<>();
    private List<DataEntry> primeListData = new ArrayList<>();
    private List<DataEntry> bolusWizardKeListData = new ArrayList<>();
    private List<DataEntry> exerciseMarkerListData = new ArrayList<>();
    private double currentHypoThreshold = 0;
    private double currentHyperThreshold = 0;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();

        // restore last path
        carelinkPathField.setText(prefs.get(Constants.CARELINK_CSV_FILE_LAST_PATH, ""));

        setComonents(false);

        // Init Date Picker
        //model.setDate(20,04,2014);           
//        p.put("text.today", "Today");
//        p.put("text.month", "Month");
//        p.put("text.year", "Year");
//        datePanel = new JDatePanelImpl(model, p);
//
//        startDatePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
//        jPanel1.add(startDatePicker);
//        startDatePicker.setSize(100, 30);
//        startDatePicker.setLocation(9, 148);
//
//        endDatePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
//        jPanel1.add(endDatePicker);
//        endDatePicker.setSize(100, 30);
//        endDatePicker.setLocation(160, 148);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sleepRadioButtonGroup = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        carelinkPathField = new javax.swing.JTextField();
        carelinkPathBrowseButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        googleImportCheckbox = new javax.swing.JCheckBox();
        optionsButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        emailButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hypoFollowingValuesList = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        hypoList = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        exerciseHistoryList = new javax.swing.JList<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        hypoLastMealList = new javax.swing.JList<>();
        wakeRadioButton = new javax.swing.JRadioButton();
        sleepRadioButton = new javax.swing.JRadioButton();
        nnRadioButton = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        hyperList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        hyperFollowingValuesList = new javax.swing.JList<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        hyperLastMealList = new javax.swing.JList<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        primeList = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        lastPrimeLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        bolusWizardKeList = new javax.swing.JList<>();
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
                            .addComponent(googleImportCheckbox))
                        .addGap(0, 403, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optionsButton)
                    .addComponent(importButton)
                    .addComponent(emailButton))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Import", jPanel1);

        hypoFollowingValuesList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "BG Series" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        hypoFollowingValuesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(hypoFollowingValuesList);

        hypoList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Hypos" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        hypoList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        hypoList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hypoListMouseClicked(evt);
            }
        });
        hypoList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hypoListKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(hypoList);

        exerciseHistoryList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Exercise/Activity" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        exerciseHistoryList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(exerciseHistoryList);

        hypoLastMealList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Last Meals" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        hypoLastMealList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane8.setViewportView(hypoLastMealList);

        sleepRadioButtonGroup.add(wakeRadioButton);
        wakeRadioButton.setText("awake");
        wakeRadioButton.setEnabled(false);

        sleepRadioButtonGroup.add(sleepRadioButton);
        sleepRadioButton.setText("sleep");
        sleepRadioButton.setEnabled(false);

        sleepRadioButtonGroup.add(nnRadioButton);
        nnRadioButton.setText("NN");
        nnRadioButton.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nnRadioButton)
                            .addComponent(wakeRadioButton)
                            .addComponent(sleepRadioButton))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(wakeRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sleepRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nnRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Hypo", jPanel2);

        hyperList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Hyper" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        hyperList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        hyperList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hyperListMouseClicked(evt);
            }
        });
        hyperList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hyperListKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(hyperList);

        hyperFollowingValuesList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "BG Series" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        hyperFollowingValuesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(hyperFollowingValuesList);

        hyperLastMealList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Last Meals" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        hyperLastMealList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane9.setViewportView(hyperLastMealList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Hyper", jPanel3);

        primeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        primeList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                primeListMouseClicked(evt);
            }
        });
        primeList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                primeListKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(primeList);

        jLabel8.setText("Time to last prime event:");

        lastPrimeLabel.setText("0 m");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(lastPrimeLabel))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastPrimeLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Prime", jPanel4);

        bolusWizardKeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(bolusWizardKeList);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Food / Bolus", jPanel6);

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
                .addContainerGap(454, Short.MAX_VALUE))
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
                .addContainerGap(133, Short.MAX_VALUE))
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
            //TODO find out which unit is used
            currentHypoThreshold = prefs.getDouble(Constants.HYPO_THRESHOLD_MG_KEY,
                    Constants.HYPO_THRESHOLD_MG_DEFAULT);
            currentHyperThreshold = prefs.getDouble(Constants.HYPER_THRESHOLD_MG_KEY,
                    Constants.HYPER_THRESHOLD_MG_DEFAULT);
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
            setNormalCursor();
            sleepRadioButtonGroup.clearSelection();
            hypoFollowingValuesList.setListData(new String[]{});
            exerciseHistoryList.setListData(new String[]{});
            hypoLastMealList.setListData(new String[]{});
            return;
        }
        DataEntry hypo = hypoListData.get(index);
        // get hypo series
        List<DataEntry> followingValues = DataHelper.filterFollowingHypoValues(
                bgListData, hypo.timestamp,
                prefs.getInt(Constants.HYPO_FOLLOW_TIME_KEY,
                        Constants.HYPO_FOLLOW_TIME_DEFAULT),
                currentHypoThreshold);
        if (followingValues.isEmpty()) {
            // if no value in range, show info and next available entry
            DataEntry nextValue = DataHelper.filterNextValue(bgListData,
                    hypo.timestamp);
            String nextValueString = "";
            if (nextValue != null) {
                nextValueString = nextValue.toGuiListEntry();
            }
            hypoFollowingValuesList.setListData(new String[]{"No BG values in range ["
                + prefs.getInt(Constants.HYPO_FOLLOW_TIME_KEY,
                Constants.HYPO_FOLLOW_TIME_DEFAULT) + "]",
                nextValueString});
        } else {
            hypoFollowingValuesList.setListData(DataHelper.createGuiList(followingValues));
        }
        hypoFollowingValuesList.repaint();

        // find exercise information
        List<DataEntry> exerciseMarker = DataHelper
                .filterHistoryValues(exerciseMarkerListData, hypo.timestamp,
                        prefs.getInt(Constants.HYPO_EXERCISE_HISTORY_TIME_KEY,
                                Constants.HYPO_EXERCISE_HISTORY_TIME_DEFAULT));
        if (exerciseMarker.isEmpty()) {
            exerciseHistoryList.setListData(new String[]{"No exercise in range ["
                + prefs.getInt(Constants.HYPO_EXERCISE_HISTORY_TIME_KEY,
                Constants.HYPO_EXERCISE_HISTORY_TIME_DEFAULT)
                + "]"});
        } else {
            exerciseHistoryList.setListData(DataHelper.createGuiList(exerciseMarker));
        }
        exerciseHistoryList.repaint();

        // last meals
        List<DataEntry> lastMeals = DataHelper
                .filterHistoryValues(bolusWizardKeListData, hypo.timestamp,
                        prefs.getInt(Constants.HYPO_MEAL_HISTORY_TIME_KEY,
                                Constants.HYPO_MEAL_HISTORY_TIME_DEFAULT));
        if (lastMeals.isEmpty()) {
            // if no value in range, show info and next available entry
            DataEntry lastValue = DataHelper.filterLastValue(bolusWizardKeListData,
                    hypo.timestamp);
            String lastValueString = "";
            if (lastValue != null) {
                lastValueString = lastValue.toGuiListEntry();
            }
            hypoLastMealList.setListData(new String[]{"No meal in range ["
                + prefs.getInt(Constants.HYPO_MEAL_HISTORY_TIME_KEY,
                Constants.HYPO_MEAL_HISTORY_TIME_DEFAULT)
                + "]", lastValueString});
        } else {
            hypoLastMealList.setListData(DataHelper.createGuiList(lastMeals));
        }

        // sleeping inication
        DataEntry lastUserAction = DataHelper.filterLastValue(entrys,
                hypo.timestamp);
        if (lastUserAction != null) {
            int lastEventMinutes = DataHelper.minutesDiff(lastUserAction.timestamp,
                    hypo.timestamp);
            int wakupTime = prefs.getInt(Constants.SLEEP_INDICATION_WAKEUP_TIME_KEY,
                    Constants.SLEEP_INDICATION_WAKEUP_TIME_DEFAULT);
            int bedTime = prefs.getInt(Constants.SLEEP_INDICATION_BED_TIME_KEY,
                    Constants.SLEEP_INDICATION_BED_TIME_DEFAULT);
            int sleepThreshold = prefs.getInt(Constants.SLEEP_INDICATION_THRESHOLD_KEY,
                    Constants.SLEEP_INDICATION_THRESHOLD_DEFAULT);
            Calendar cal = new GregorianCalendar(Locale.GERMANY);
            cal.setTime(hypo.timestamp);

            if (lastEventMinutes > sleepThreshold && cal.get(Calendar.HOUR) > bedTime
                    || cal.get(Calendar.HOUR_OF_DAY) < wakupTime) {
                // inside sleep time and over threshold
                sleepRadioButton.setSelected(true);
            } else if (lastEventMinutes < sleepThreshold && cal.get(Calendar.HOUR) < bedTime
                    || cal.get(Calendar.HOUR_OF_DAY) > wakupTime) {
                // there was an action and its day time
                wakeRadioButton.setSelected(true);
            } else {
                nnRadioButton.setSelected(true);
            }
        } else {
            nnRadioButton.setSelected(true);
        }
        setNormalCursor();
    }//GEN-LAST:event_hypoListMouseClicked

    private void primeListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_primeListMouseClicked
        setWaitCursor();
        int index = primeList.getSelectedIndex();
        if (index < 0) {
            lastPrimeLabel.setVisible(false);
            setNormalCursor();
            return;
        }
        DataEntry prime = primeListData.get(index);

        DataEntry lastPrime = DataHelper.filterLastValue(primeListData,
                prime.timestamp);
        if (lastPrime != null) {
            int minutes = DataHelper.minutesDiff(lastPrime.timestamp,
                    prime.timestamp);
            lastPrimeLabel.setText(String.format(
                    "%dd %02dh %02dM",
                    minutes / 1440,
                    (minutes % 1440) / 60,
                    minutes % 60));
            lastPrimeLabel.setVisible(true);
        } else {
            lastPrimeLabel.setVisible(false);
        }
        setNormalCursor();
    }//GEN-LAST:event_primeListMouseClicked

    private void hypoListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hypoListKeyReleased
        hypoListMouseClicked(null);
        // TODO clean up GUI <--> logic
    }//GEN-LAST:event_hypoListKeyReleased

    private void primeListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primeListKeyReleased
        primeListMouseClicked(null);
    }//GEN-LAST:event_primeListKeyReleased

    private void hyperListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyperListMouseClicked
        setWaitCursor();
        int index = hyperList.getSelectedIndex();
        if (index < 0) {
            setNormalCursor();
            hyperFollowingValuesList.setListData(new String[]{});
            hyperLastMealList.setListData(new String[]{});
            return;
        }

        DataEntry hyper = hyperListData.get(index);

        // get hyper series
        List<DataEntry> followingValues = DataHelper.filterFollowingHyperValues(
                bgListData, hyper.timestamp,
                prefs.getInt(Constants.HYPER_FOLLOW_TIME_KEY,
                        Constants.HYPER_FOLLOW_TIME_DEFAULT),
                currentHyperThreshold);
        if (followingValues.isEmpty()) {
            // if no value in range, show info and next available entry
            DataEntry nextValue = DataHelper.filterNextValue(bgListData,
                    hyper.timestamp);
            String nextValueString = "";
            if (nextValue != null) {
                nextValueString = nextValue.toGuiListEntry();
            }
            hyperFollowingValuesList.setListData(new String[]{"No BG values in range ["
                + prefs.getInt(Constants.HYPER_FOLLOW_TIME_KEY,
                Constants.HYPER_FOLLOW_TIME_DEFAULT) + "]",
                nextValueString});
        } else {
            hyperFollowingValuesList.setListData(DataHelper.createGuiList(followingValues));
        }

        // last meals
        List<DataEntry> lastMeals = DataHelper
                .filterHistoryValues(bolusWizardKeListData, hyper.timestamp,
                        prefs.getInt(Constants.HYPER_MEAL_HISTORY_TIME_KEY,
                                Constants.HYPER_MEAL_HISTORY_TIME_DEFAULT));
        if (lastMeals.isEmpty()) {
            // if no value in range, show info and next available entry
            DataEntry lastValue = DataHelper.filterLastValue(bolusWizardKeListData,
                    hyper.timestamp);
            String lastValueString = "";
            if (lastValue != null) {
                lastValueString = lastValue.toGuiListEntry();
            }
            hyperLastMealList.setListData(new String[]{"No meal in range ["
                + prefs.getInt(Constants.HYPER_MEAL_HISTORY_TIME_KEY,
                Constants.HYPER_MEAL_HISTORY_TIME_DEFAULT)
                + "]", lastValueString});
        } else {
            hyperLastMealList.setListData(DataHelper.createGuiList(lastMeals));
        }

        setNormalCursor();
    }//GEN-LAST:event_hyperListMouseClicked

    private void hyperListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hyperListKeyReleased
        hyperListMouseClicked(null);
    }//GEN-LAST:event_hyperListKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> bolusWizardKeList;
    private javax.swing.JButton carelinkPathBrowseButton;
    private javax.swing.JTextField carelinkPathField;
    private javax.swing.JButton emailButton;
    private javax.swing.JList<String> exerciseHistoryList;
    private javax.swing.JCheckBox googleImportCheckbox;
    private javax.swing.JList<String> hyperFollowingValuesList;
    private javax.swing.JList<String> hyperLastMealList;
    private javax.swing.JList<String> hyperList;
    private javax.swing.JList<String> hypoFollowingValuesList;
    private javax.swing.JList<String> hypoLastMealList;
    private javax.swing.JList<String> hypoList;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lastPrimeLabel;
    private javax.swing.JRadioButton nnRadioButton;
    private javax.swing.JButton optionsButton;
    private javax.swing.JList<String> primeList;
    private javax.swing.JRadioButton sleepRadioButton;
    private javax.swing.ButtonGroup sleepRadioButtonGroup;
    private javax.swing.JRadioButton wakeRadioButton;
    // End of variables declaration//GEN-END:variables
//    private final UtilDateModel model = new UtilDateModel();
//    private final Properties p = new Properties();
//    private final JDatePanelImpl datePanel;
//    private final JDatePickerImpl startDatePicker;
//    private final JDatePickerImpl endDatePicker;
}
