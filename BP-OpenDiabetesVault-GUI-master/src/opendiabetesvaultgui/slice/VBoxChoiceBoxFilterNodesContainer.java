/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.slice;

import de.opendiabetes.vault.processing.filter.options.guibackend.FilterNode;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Nutzer
 */
public class VBoxChoiceBoxFilterNodesContainer {

    private VBox vBox;
    private ChoiceBox choicebox;
    private List<FilterNode> filterNodes;

    public VBoxChoiceBoxFilterNodesContainer(VBox vBox, ChoiceBox choicebox, List<FilterNode> filterNodes) {
        this.vBox = vBox;
        this.choicebox = choicebox;
        this.filterNodes = filterNodes;
    }
    
    
    

    public VBox getVBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public ChoiceBox getChoicebox() {
        return choicebox;
    }

    public void setChoicebox(ChoiceBox choicebox) {
        this.choicebox = choicebox;
    }

    public List<FilterNode> getFilterNodes() {
        return filterNodes;
    }

    public void setFilterNodes(List<FilterNode> filterNodes) {
        this.filterNodes = filterNodes;
    }
    
    

}
