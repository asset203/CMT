package eg.com.vodafone.web.mvc.formbean.manageSystems;

import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VNode;

import javax.validation.Valid;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/17/13
 * Time   : 10:30 AM
 */
public class DcNodeFormBean {

    @Valid
    private VNode node;
    private boolean editMode;
    private String systemName;
    private String systemDescription;
    private boolean isNewSystem;

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public VNode getNode() {
        return node;
    }

    public void setNode(VNode node) {
        this.node = node;
    }
    public VInput getInput(String inputName){
        for(VInput input : node.getInputsList()){
            if(inputName.equals(input.getId())){
                return input;
            }
        }
        return null;
    }
    public void replaceInput(VInput newInput,String oldInputName){
        for(VInput input : node.getInputsList()){
            if(input.getId().equals(oldInputName)){
                node.getInputsList().remove(input);
                node.getInputsList().add(newInput);
                break;
            }
        }
    }
    public void deleteInput(String inputName){
        for(VInput input : node.getInputsList()){
            if(input.getId().equals(inputName)){
                node.getInputsList().remove(input);
                break;
            }
        }
    }

    public void prepareNodeForSave(){
        for(VInput input : node.getInputsList()){
            input.setNodeName(node.getName());
            input.setSystemName(node.getSystemName());
        }
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemDescription() {
        return systemDescription;
    }

    public void setSystemDescription(String systemDescription) {
        this.systemDescription = systemDescription;
    }

    public boolean isNewSystem() {
        return isNewSystem;
    }

    public void setNewSystem(boolean newSystem) {
        isNewSystem = newSystem;
    }
}
