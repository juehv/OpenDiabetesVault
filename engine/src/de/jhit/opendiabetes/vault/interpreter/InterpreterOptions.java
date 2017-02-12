/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetes.vault.interpreter;

/**
 *
 * @author mswin
 */
public class InterpreterOptions {

    public final boolean FillCanulaAsNewKatheder;
    public final int FillCanulaCooldown;

    public InterpreterOptions(boolean FillCanulaAsNewKatheder, int FillCanulaCooldown) {
        this.FillCanulaAsNewKatheder = FillCanulaAsNewKatheder;
        this.FillCanulaCooldown = FillCanulaCooldown;
    }

}
