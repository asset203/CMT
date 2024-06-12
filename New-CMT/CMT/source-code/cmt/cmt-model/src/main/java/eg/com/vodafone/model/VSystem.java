package eg.com.vodafone.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class VSystem  implements Serializable{

    private List<VNode> nodesList;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @Size(max = 100)
    private String description;
    private boolean generic;
    private List<VInput> inputsList;

    public VSystem()

    {
        inputsList = new ArrayList<VInput>();
        nodesList =new ArrayList<VNode>();
    }

    public List<VInput> getInputsList() {
        return inputsList;
    }

    public void setInputsList(List<VInput> inputsList) {
        this.inputsList = inputsList;
    }

    public List<VNode> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<VNode> nodesList) {
        this.nodesList = nodesList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGeneric() {
        return generic;
    }

    public void setGeneric(boolean generic) {
        this.generic = generic;
    }

    public List<String> getNodesNames(){
        List<String> names = new ArrayList<String>();
        for(int i=0;i< nodesList.size();i++){
            names.add(nodesList.get(i).getName().trim());
        }
        return names;
    }
    public List<String> getInputsNames(){
        List<String> names = new ArrayList<String>();
        for(int i=0;i< inputsList.size();i++){
            names.add(inputsList.get(i).getId().trim());
        }
        return names;
    }

    public void setNodes(VNode[] nodes) {
        this.nodesList = Arrays.asList( nodes);
    }

    public VNode[] getNodes() {
        return nodesList.toArray(new VNode[]{});
    }
    public void setInputs(VInput[] inputs) {
        this.inputsList = Arrays.asList(inputs);
    }

    public VInput[] getInputs() {
        return inputsList.toArray(new VInput[]{});
    }
}
