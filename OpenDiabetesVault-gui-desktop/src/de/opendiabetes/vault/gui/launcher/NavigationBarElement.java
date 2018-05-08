/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.opendiabetes.vault.gui.launcher;

import javafx.scene.control.Label;
import javafx.scene.shape.SVGPath;

/**
 * Workflowbar Element class.
 * contains all visual elemtens of the navigation bar of one page
 * as well as the page URL
 * for example patient selection
 * @author Daniel Schäfer, Julian Schwind, Martin Steil, Kai Worsch
 */
public class NavigationBarElement {
    /**
     * the SVGPath element displayed at the navigation bar.
     * the actual navigation bar element.
     */
    private SVGPath navigationElement;
    /**
     * the check elemtn displayed next to the page label.
     */
    private SVGPath checkElement;
    /**
     * the page label.
     */
    private Label labelElement;
    /**
     * the page URL.
     */
    private String pagePath;

    /**
     * the constructor to initialize all fields.
     * @param navigationElement the actual navigation bar element
     * @param checkElement the echeck element
     * @param labelElement the label
     * @param pagePath the page page
     */
    NavigationBarElement(final SVGPath navigationElement,
            final SVGPath checkElement, final Label labelElement,
            final String pagePath) {
        this.navigationElement = navigationElement;
        this.checkElement = checkElement;
        this.labelElement = labelElement;
        this.pagePath = pagePath;
    }

    /**
     * navigation element getter.
     * @return the navigation element
     */
    public final SVGPath getNavigationElement() {
        return navigationElement;
    }

    /**
     * check element getter.
     * @return the check element
     */
    public final SVGPath getCheckElement() {
        return checkElement;
    }

    /**
     * label getter.
     * @return the label
     */
    public final Label getLabelElement() {
        return labelElement;
    }

    /**
     * Path getter.
     * @return the path as String
     */
    public final String getPagePath() {
        return pagePath;
    }

    /**
     * navigation element setter.
     * @param navigationElement the navigation element ot set
     */
    public final void setNavigationElement(final SVGPath navigationElement) {
        this.navigationElement = navigationElement;
    }

    /**
     * check element setter.
     * @param checkElement the check element to set
     */
    public final void setCheckElement(final SVGPath checkElement) {
        this.checkElement = checkElement;
    }

    /**
     * the label setter.
     * @param labelElement the label to set
     */
    public final void setLabelElement(final Label labelElement) {
        this.labelElement = labelElement;
    }

    /**
     * Path setter.
     * @param pagePath the path to set
     */
    public final void setPagePath(final String pagePath) {
        this.pagePath = pagePath;
    }
}
