package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.SystemDao;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;
import eg.com.vodafone.model.enums.InputAccessMethod;
import eg.com.vodafone.model.enums.NodeInUse;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static eg.com.vodafone.service.BusinessException.*;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/19/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
@Transactional(readOnly = true)
public class DataCollectionSystemService implements DataCollectionSystemServiceInterface {

    private SystemDao systemDao;
    @Autowired
    private DCJobService jobService;

    @Autowired
    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }

    @Override
    public List<VNode> getSystemNodes(String systemName, Boolean activeNodesOnly){
        return systemDao.getSystemNodes(systemName,activeNodesOnly );
    }

    @Override
    public List<String> listAllSystems() {

      return systemDao.getAllSystems();
    }

    @Override
    public VSystem getSystem(String systemName, Boolean getActiveNodesOnly) {
        VSystem system =   systemDao.getSystem(systemName);
        if(system == null){
            throw new BusinessException(SYSTEM_NOT_EXIST) ;
        }
        system.setNodesList(systemDao.getSystemNodes(systemName, getActiveNodesOnly));
        system.setInputsList(systemDao.getInputs(systemName,-1));
        return   system;
    }


    @Override
    @Transactional(readOnly = false)
    public void addNewSystem(VSystem system) throws BusinessException {
        validateSystem(system);
        systemDao.saveSystemNameAndDescription(system.getName(),system.getDescription());
        systemDao.saveInputs(system.getInputsList(),-1);
        systemDao.saveNodes(system.getNodesList());

    }
    @Override
    public void validateSystem(VSystem system)throws BusinessException{
        boolean notUniqueName = systemDao.isSystemNameUsed(system.getName());

        if(notUniqueName)
            throw new BusinessException(BusinessException.DUPLICATE_SYSTEM_NAME);
        if((system.getNodesList()== null || system.getNodesList().isEmpty()) && (system.getInputsList()==null || system.getInputsList().isEmpty()) )
            throw new BusinessException(BusinessException.EMPTY_SYSTEM);
    }

    @Override
    public void validateInput(VInput input, List<String> nodeInputs, List<String> systemInputs) throws BusinessException {
        validateNumberOfInputStructuresPerInput(input);
    }

    private void validateNumberOfInputStructuresPerInput(VInput input)throws BusinessException{
        if(input.getInputStructureIds().isEmpty())
            throw new BusinessException(BusinessException.INPUT_MUST_HAVE_AT_LEAST_ONE_DATA_COLLECTION);

        if(InputAccessMethod.DB_ACCESS.getName().equals( input.getAccessMethod()) )
        {
            if(input.getInputStructureIds().size()>1)
                throw new BusinessException(BusinessException.DB_ACCESS_INPUT_CAN_HAVE_SINGLE_DATA_COLLECTION);
       }
    }
    @Override
    @Transactional(readOnly = false)
    public void addNewInput(VInput input) throws BusinessException {
        if(StringUtils.hasText(input.getNodeName())){
              long nodeId = systemDao.getNodeId(input.getSystemName(),input.getNodeName()) ;
              input.setNodeId(nodeId);
              input.setPerNode(true);
        }else{
            input.setNodeId(-1);
            input.setPerNode(false);
        }
        systemDao.saveInput(input);
    }


    public void validateUniqueNodeName(VNode node,List<VNode> systemNodes) throws BusinessException{
        if(systemNodes!= null  && !systemNodes.isEmpty())
        {
            for(VNode systemNode:systemNodes){
                if(node.getId() == systemNode.getId())
                    continue;
                if(systemNode.getName().trim().equalsIgnoreCase(node.getName())){
                    throw new BusinessException (BusinessException.DUPLICATE_NODE_NAME);
                }
            }
        }
    }
    @Override
    @Transactional(readOnly = false)
    public void addNewNode(VNode node) throws BusinessException {
      long nodeId =  systemDao.saveNodeNameAndDescription(node);
      systemDao.saveInputs(node.getInputsList(),nodeId);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateSystemDescription(String systemName, String systemDescription) {
        systemDao.updateSystemDescription(systemName,systemDescription);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateInput(VInput input) throws BusinessException {
        systemDao.updateInput(input);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateNode(VNode node) throws BusinessException {
        systemDao.updateNode(node.getId(),node.getName(),node.getDescription());
        systemDao.updateNodeInputs(node.getId(),node.getName());
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteSystem(String systemName) throws BusinessException {
        VSystem systemToDelete = getSystem(systemName,false );

        //1- delete system inputs
        systemDao.deleteInputs(systemToDelete.getInputsList());

        //2-delete system nodes
        for(int i=0;i<systemToDelete.getNodesList().size();i++){
            deleteNodeWithoutCheck(systemToDelete.getNodesList().get(i).getName(),systemName);
        }
        //3-delete system
        systemDao.deleteSystem(systemToDelete);
        List<Integer> deleteJobResults = jobService.deleteAllSystemJobs(systemName);
        for(Integer i:deleteJobResults){
            if(i != 1){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteNode(String nodeName, String systemName) throws BusinessException {
        VNode nodeToDelete = getNode(systemName, nodeName);
        if(nodeToDelete!=null)
        {   if(isLastSystemEntry(systemName))
            {
                 throw  new BusinessException(EMPTY_SYSTEM);
            }
            systemDao.deleteInputs(nodeToDelete.getInputsList());
            systemDao.deleteNode(nodeToDelete);
            List<Integer> deleteJobResults = jobService.deleteAllSystemNodeJobs(systemName.trim(),nodeName.trim());
            for(Integer i:deleteJobResults){
                if(i != 1){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    private void  deleteNodeWithoutCheck(String nodeName, String systemName){   //refactor
        VNode nodeToDelete = getNode(systemName, nodeName);
        systemDao.deleteInputs(nodeToDelete.getInputsList());
        systemDao.deleteNode(nodeToDelete);
    }
    @Override
    public VNode getNode(String systemName,String nodeName){
        VNode node = systemDao.getNode(systemName,nodeName);
        if(node == null){
            throw new BusinessException(NODE_NOT_EXIST);
        }
        node.setInputsList(systemDao.getInputs(systemName,node.getId()));
        return node;
    }

    @Override
    public List<String> searchSystems(String keyword, int startIndex, int endIndex) {
        if((startIndex<0||endIndex<0) ||startIndex>endIndex ){
            throw new BusinessException(INVALID_INDEX);
        }
        return systemDao.searchSystems(keyword,startIndex,endIndex);
    }

    @Override
    public List<String> searchSystems(String keyword) {
        return systemDao.searchSystems(keyword);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteInput(long inputId) {
        systemDao.deleteInput(inputId);
    }
    @Override
    @Transactional(readOnly = false)
    public void deleteInput(String inputName,String nodeName,String systemName){
       long inputIdToDelete = systemDao.getInputId(inputName,nodeName,systemName);
        if(StringUtils.isEmpty(nodeName)){
            if(isLastSystemEntry(systemName)){
                throw new BusinessException(EMPTY_SYSTEM);
            }
        }
        else
        {
            if(isLastNodeEntry(systemName,nodeName)){
                throw new BusinessException(EMPTY_NODE);
            }
        }
        systemDao.deleteInput(inputIdToDelete);
    }
    @Override
    public List<String> listSystems(int startIndex,int endIndex){
        if((startIndex<0||endIndex<0) ||startIndex>endIndex ){
            throw new BusinessException(INVALID_INDEX);
        }
         return  systemDao.listSystems(startIndex,endIndex);
    }
    private boolean isLastSystemEntry(String sysemName){
        int nodesCount = systemDao.countSystemNodes(sysemName);
        int inputsCount =  systemDao.countSystemInputs(sysemName);
        if( (inputsCount + nodesCount) <= 1)
        {return true;}
        return false;
    }
    private boolean isLastNodeEntry(String sysemName,String nodeName){

        int inputsCount =  systemDao.countNodeInputs(sysemName,nodeName);
        if( (inputsCount) <= 1)
        {return true;}
        return false;
    }

    public  void activateNode(String nodeName, String systemName)throws  BusinessException{
       int result =  systemDao.updateNodeStatus(nodeName,systemName, NodeInUse.YES.toString());
        if(result!= 1) throw new BusinessException(FAILED_TO_UPDATE_NODE_STATUS) ;
    }

    public  void deactivateNode(String nodeName, String systemName)throws  BusinessException{
        int result = systemDao.updateNodeStatus(nodeName,systemName, NodeInUse.NO.toString());
        if(result!= 1) throw new BusinessException(FAILED_TO_UPDATE_NODE_STATUS) ;
    }
}
