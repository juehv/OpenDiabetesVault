/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.openmediavault.app.plot;

import de.jhit.openmediavault.app.container.VaultCsvEntry;
import java.util.List;

/**
 *
 * @author mswin
 */
public class VaultCsvPlotter {

    public static void plotVaultCsvComplete(List<VaultCsvEntry> csv) {
        double[] x = new double[csv.size()];
        double[] y = new double[csv.size()];

        for (int i = 0; i < csv.size(); i++) {
            VaultCsvEntry item = csv.get(i);

            if (item.getCgmValue() > 0.0) {
                x[i] = i;
                y[i] = item.getCgmValue();
            }
        }
        
        //TODO use gnuplot or python mathplotlib
    }
}
