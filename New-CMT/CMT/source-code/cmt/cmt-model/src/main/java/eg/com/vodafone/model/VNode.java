package eg.com.vodafone.model;

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
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class VNode implements Serializable {

    private long id;
    @NotEmpty
    @Size(max = 50)
    private String name;
    private String systemName;
    @Size(max = 50)
    private String description;
    private List<VInput> inputsList;
    private String inUse;

    public VNode(long id,  String systemName, String name, String description) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.systemName = systemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VNode() {
        inputsList =new ArrayList<VInput>();
    }

    public List<VInput> getInputsList() {
        return inputsList;
    }

    public void setInputsList(List<VInput> inputsList) {
        this.inputsList = inputsList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public List<String> getInputsNames(){
        List<String> names = new ArrayList<String>();
        for(int i=0;i< inputsList.size();i++){
            names.add(inputsList.get(i).getId().trim());
        }
        return names;
    }
    public void setInputs(VInput[] inputs) {
        this.inputsList = Arrays.asList(inputs);
    }

    public VInput[] getInputs() {
        return inputsList.toArray(new VInput[]{});
    }

    public String getInUse() {
        return inUse;
    }

    public void setInUse(String inUse) {
        this.inUse = inUse;
    }
}

