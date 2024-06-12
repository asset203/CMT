package eg.com.vodafone.web.mvc.formbean.manageSystems;

import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.InputAccessMethod;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/19/13
 * Time   : 9:01 AM
 */
public class DcInputFormBean {

    @NotEmpty
    @Size(max = 50)
    private String inputName;
    private String inputNameBeforeChange;
    private long inputID =-1;
    private InputAccessMethod inputAccessMethod;
    private FileType fileType;
    private DataBaseType  dataBaseType;
    private List<String> dataCollections;
    private List<String> pathsList;
    @Size(max = 200)
    private String fileNamePattern;
    @Size(max = 200)
    private String dataBaseName;
    //@NotEmpty
    @Size(max = 200)
    private String server;
    @NotEmpty
    @Size(max = 200)
    private String userName;
    @NotEmpty
    @Size(max = 200)
    private String password;
    @Size(max = 5)
    private String port;
    private String tns;
    private boolean editMode;
    private boolean enableAddDataCollection;
    private VInput inputObject;

    private boolean nodeEnabled;
    private int currentDataCollectionType;

    private Map<String,String> accessMethodOptions;
    private Map<String,String> dataBaseTypeOptions;
    private Map<String,String> fileTypeOptions;
    private List<String> dataCollectionNames;

    private String systemName;
    private String systemDescription;
    private boolean isNewSystem;
    private String nodeName;
    private long nodeId=-0;
    private String nodeDescription;
    private boolean isNewNode;

    public boolean isEnableAddDataCollection() {
        return enableAddDataCollection;
    }

    public void setEnableAddDataCollection(boolean enableAddDataCollection) {
        this.enableAddDataCollection = enableAddDataCollection;
    }

    public int getCurrentDataCollectionType() {
        return currentDataCollectionType;
    }

    public void setCurrentDataCollectionType(int currentDataCollectionType) {
        this.currentDataCollectionType = currentDataCollectionType;
    }


    public List<String> getDataCollectionNames() {
        return dataCollectionNames;
    }

    public void setDataCollectionNames(List<String> dataCollectionNames) {
        this.dataCollectionNames = dataCollectionNames;
    }

    public List<String> getDataCollections() {
        return dataCollections;
    }

    public void setDataCollections(List<String> dataCollections) {
        this.dataCollections = dataCollections;
    }

    public List<String> getPathsList() {
        return pathsList;
    }

    public void setPathsList(List<String> pathsList) {
        this.pathsList = pathsList;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public VInput getInputObject() {
        return inputObject;
    }

    public void setInputObject(VInput inputObject) {
        this.inputObject = inputObject;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getTns() {
        return tns;
    }

    public void setTns(String tns) {
        this.tns = tns;
    }

    public boolean isNodeEnabled() {
        return nodeEnabled;
    }

    public void setNodeEnabled(boolean nodeEnabled) {
        this.nodeEnabled = nodeEnabled;
    }

    public Map<String, String> getAccessMethodOptions() {
        return accessMethodOptions;
    }

    public void setAccessMethodOptions(Map<String, String> accessMethodOptions) {
        this.accessMethodOptions = accessMethodOptions;
    }

    public InputAccessMethod getInputAccessMethod() {
        return inputAccessMethod;
    }

    public void setInputAccessMethod(InputAccessMethod inputAccessMethod) {
        this.inputAccessMethod = inputAccessMethod;
    }
    public Map<String, String> getDataBaseTypeOptions() {
        return dataBaseTypeOptions;
    }

    public void setDataBaseTypeOptions(Map<String, String> dataBaseTypeOptions) {
        this.dataBaseTypeOptions = dataBaseTypeOptions;
    }

    public Map<String, String> getFileTypeOptions() {
        return fileTypeOptions;
    }

    public void setFileTypeOptions(Map<String, String> fileTypeOptions) {
        this.fileTypeOptions = fileTypeOptions;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public DataBaseType getDataBaseType() {
        return dataBaseType;
    }

    public void setDataBaseType(DataBaseType dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public boolean isNewNode() {
        return isNewNode;
    }

    public void setNewNode(boolean newNode) {
        isNewNode = newNode;
    }

    public String getInputNameBeforeChange() {
        return inputNameBeforeChange;
    }

    public void setInputNameBeforeChange(String inputNameBeforeChange) {
        this.inputNameBeforeChange = inputNameBeforeChange;
    }

    public long getInputID() {
        return inputID;
    }

    public void setInputID(long inputID) {
        this.inputID = inputID;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }
}
