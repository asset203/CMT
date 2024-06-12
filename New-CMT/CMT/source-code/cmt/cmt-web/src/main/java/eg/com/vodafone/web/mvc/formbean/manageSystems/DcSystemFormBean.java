package eg.com.vodafone.web.mvc.formbean.manageSystems;

import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/15/13
 * Time   : 11:22 AM
 */
public class DcSystemFormBean {

    private boolean editMode;
    private String errors;
    @Valid
    private VSystem system;

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public VSystem getSystem() {
        return system;
    }

    public void setSystem(VSystem system) {
        this.system = system;
    }
    public VNode getNode(String nodeName){
        for(VNode node : system.getNodesList()){
            if(node.getName().equals(nodeName)){
                return node;
            }
        }
        return null;
    }
    public VInput getInput(String inputName){
        for(VInput input : system.getInputsList()){
            if(inputName.equals(input.getId())){
                return input;
            }
        }
        return null;
    }
    public void updateNode(String oldNodeName,String newNodeName,String newNodeDescription){
        for(VNode node : system.getNodesList()){
            if(node.getName().equals(oldNodeName)){
                node.setName(newNodeName);
                node.setSystemName(system.getName());
                node.setDescription(newNodeDescription);
                break;
            }
        }
    }
    public void updateNodeStatus(String nodeName,String newStatus){
        for(VNode node : system.getNodesList()){
            if(node.getName().equals(nodeName)){
                node.setInUse(newStatus);
                break;
            }
        }
    }
    public void removeNode(String nodeName){
        for(VNode node : system.getNodesList()){
            if(node.getName().equals(nodeName)){
                system.getNodesList().remove(node);
                break;
            }
        }
    }
    public void replaceInput(String oldInputName,VInput inputObject){
        for(VInput input : system.getInputsList()){
            if(input.getId().equals(oldInputName)){
                system.getInputsList().remove(input);
                system.getInputsList().add(inputObject);
                break;
            }
        }
    }
    public void deleteInput(String inputName){
        for(VInput input : system.getInputsList()){
            if(input.getId().equals(inputName)){
                system.getInputsList().remove(input);
                break;
            }
        }
    }
    public void addInput(VInput inputObject){
        system.getInputsList().add(inputObject);
    }
    public void prepareSystemForSave(){
        for(VNode node : system.getNodesList()){
            node.setSystemName(system.getName());
            for(VInput input : node.getInputsList()){
                input.setNodeName(node.getName());
                input.setSystemName(system.getName());
            }
        }
        for(VInput input : system.getInputsList()){
            input.setSystemName(system.getName());
        }
    }
}
