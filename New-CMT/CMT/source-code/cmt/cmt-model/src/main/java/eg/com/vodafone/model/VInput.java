package eg.com.vodafone.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 9:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class VInput implements Serializable {

    private String systemName;
    private String nodeName;
    private String id;
    private boolean perNode;
    private String inputName;
    private String originalInputName;
    private String hourlyName;
    private String server;
    private String user;
    private String password;
    private String accessMethod;
    private int type;

    //new attributes
    private long inputId;//DB auto-generated ID
    private long nodeId;
    private List<String> pathsList;
    private List<VInputStructure> inputStructuresList;
    private List<String> inputStructureIds;

    public VInput() {
        inputStructureIds = new ArrayList<String>();
        inputStructuresList = new ArrayList<VInputStructure>();
        pathsList = new ArrayList<String>();
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }
    public String getOriginalInputName() {
        return originalInputName;
    }

    public void setOriginalInputName(String originalInputName) {
        this.originalInputName = originalInputName;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<VInputStructure> getInputStructuresList() {
        return inputStructuresList;
    }

    public void setInputStructuresList(List<VInputStructure> inputStructuresList) {
        this.inputStructuresList = inputStructuresList;
    }

    public List<String> getPathsList() {
        return pathsList;
    }

    public void setPathsList(List<String> pathsList) {
        this.pathsList = pathsList;
    }

    public void setPerNode(boolean perNode) {
        this.perNode = perNode;
    }

    public boolean isPerNode() {
        return perNode;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getInputName() {
        return inputName;
    }

    public void setAccessMethod(String accessMethod) {
        this.accessMethod = accessMethod;
    }

    public String getAccessMethod() {
        return accessMethod;
    }


    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getHourlyName() {
        return hourlyName;
    }

    public void setHourlyName(String hourlyName) {
        this.hourlyName = hourlyName;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public long getInputId() {
        return inputId;
    }


    public void setInputId(long inputId) {
        this.inputId = inputId;
    }

    public List<String> getInputStructureIds() {
        return inputStructureIds;
    }

    public void setInputStructureIds(List<String> inputStructureIds) {
        this.inputStructureIds = inputStructureIds;
    }

    public void addInputStructureId(String id){
        this.inputStructureIds.add(id);
    }

    public void setInputStructures(VInputStructure[] inputStructures) {
        this.inputStructuresList = Arrays.asList(inputStructures);
    }

    public VInputStructure[] getInputStructures() {
        return inputStructuresList.toArray(new VInputStructure[]{});
    }

    public VInputStructure getInputStructure() {
        if (inputStructuresList.size() == 1) {
            return inputStructuresList.get(0);
        } else {
            return null;
        }
    }

    public void setPaths(String[] paths) {
        this.pathsList = Arrays.asList(paths);
    }

    public String[] getPaths() {
        return pathsList.toArray(new String[]{});
    }
 /*@Override
    public Object clone() throws CloneNotSupportedException {
        try{
            VInput clonedInput = (VInput) super.clone();
            List<VInputStructure> thisInputStructures = this.getInputStructuresList();
            List <VInputStructure> copiedInputStructures = null;
            if(thisInputStructures != null){
                copiedInputStructures = new ArrayList<VInputStructure>(thisInputStructures.size());
                for(int i = 0;i<thisInputStructures.size();i++){
                    copiedInputStructures.add((VInputStructure)thisInputStructures.get(i).clone());
                }
            }
            clonedInput.setInputStructuresList(copiedInputStructures);
            return clonedInput;
        }catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }*/
}
