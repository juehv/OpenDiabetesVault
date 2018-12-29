/*
 * Copyright (C) 2018 Nutzer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
package de.opendiabetes.vault.util;

import com.csvreader.CsvReader;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.exporter.odvdbjsonexporter.ODVDBJSONExporter;
import de.opendiabetes.vault.plugin.exporter.odvdbjsonexporter.ODVDBJSONExporter.ODVDBJSONExporterImplementation;
import de.opendiabetes.vault.plugin.importer.ODV.ODVImporter;
import de.opendiabetes.vault.plugin.importer.ODV.ODVImporter.ODVImporterImplementation;
import de.opendiabetes.vault.plugin.importer.fileimporter.CSVImporter;
import de.opendiabetes.vault.plugin.importer.libretext.LibreTextImporter;
import de.opendiabetes.vault.plugin.importer.medtronic.MedtronicImporter;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FastImporterExporter extends Application {

    private File file;

    @Override
    public void start(Stage stage) {

        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Choose file");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    exportJson(file);
                }
            }
        });

        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().add(openButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void exportJson(File file) {
        try {

            CSVImporter csvImporter = new MedtronicImporter.MedtronicImporterImplementation();

            List<VaultEntry> vaultEntries = csvImporter.importData(file.getAbsolutePath());
            
            ODVDBJSONExporterImplementation oDVDBJSONExporter = new ODVDBJSONExporterImplementation();
            oDVDBJSONExporter.exportDataToFile(file.getAbsolutePath().replace(".csv", ".json"), vaultEntries);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

**/