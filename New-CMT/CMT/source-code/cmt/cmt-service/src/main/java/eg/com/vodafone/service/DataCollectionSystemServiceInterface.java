package eg.com.vodafone.service;

import eg.com.vodafone.dao.SystemDao;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/19/13
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataCollectionSystemServiceInterface {

    public List<String> listAllSystems();
    public List<String> listSystems(int startIndex,int endIndex);
    public List<VNode> getSystemNodes(String systemName, Boolean activeNodesOnly);
    public VSystem getSystem(String systemName, Boolean getActiveNodesOnly);
    public void validateSystem(VSystem system)throws  BusinessException;

    public void addNewSystem(VSystem system) throws  BusinessException;
  //  public void validateNode(VNode node,List<String> systemNodes)throws  BusinessException;
    public void validateUniqueNodeName(VNode nodeName,List<VNode> systemNodes) throws BusinessException;
    public void addNewNode(VNode node) throws  BusinessException;
    public void validateInput(VInput input,List<String> nodeInputs,List<String> systemInputs)throws  BusinessException;
    public void addNewInput(VInput input) throws  BusinessException;
    public void updateSystemDescription(String systemName,String systemDescription);
    public void updateInput(VInput input)throws BusinessException;
    public void updateNode(VNode node) throws BusinessException;
    public boolean deleteSystem(String systemName)  throws BusinessException;
    public boolean deleteNode(String nodeName, String systemName)throws BusinessException;
    public void deleteInput(long inputId);
    public void deleteInput(String inputName,String nodeName,String SystemName);
    public VNode getNode(String systemName,String nodeName);
    public List<String> searchSystems(String keyword,int startIndex,int endIndex);
    public List<String> searchSystems(String keyword);
    public void setSystemDao(SystemDao systemDao);
    public  void activateNode(String nodeName, String systemName)throws  BusinessException;
    public  void deactivateNode(String nodeName, String systemName)throws  BusinessException;
}
