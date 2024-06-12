package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.ManageKpiDao;
import eg.com.vodafone.model.NodeProperties;
import eg.com.vodafone.model.SystemNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author marwa.goda
 * @since 4/29/13
 */
@Service
@Transactional(readOnly = true)
public class ManageKpiService {

    @Autowired
    ManageKpiDao manageKpiDao;

    @Autowired
    KPINotificationService kpiNotificationService;

    public List<NodeProperties> getSystemKpiDetails(long systemNodeId) {
        List<NodeProperties> nodePropertiesList = manageKpiDao.getSystemKpiDetails(systemNodeId);
        return nodePropertiesList;
    }
    @Transactional(readOnly = false)
    public int addSystemKpiDetails(NodeProperties nodeProperties) {
        int affectedRows = manageKpiDao.addSystemKpiDetails(nodeProperties);
        return affectedRows;
    }
    @Transactional(readOnly = false)
    public int updateSystemNode(SystemNode systemNode) {
        int affectedRows = manageKpiDao.updateSystemNode(systemNode);
        return affectedRows;
    }
    @Transactional(readOnly = false)
    public int deleteSystemNodeProperties(int nodId, String propertyName) {
        int affectedRows = manageKpiDao.deleteSystemNodeProperties(nodId, propertyName);
        return affectedRows;
    }

    public String isSystemNodeInUse(long nodeId) {
        return manageKpiDao.isSystemNodeInUse(nodeId);
    }

    public SystemNode getSystemNodeById(long nodeId) {
        return manageKpiDao.getSystemNodeById(nodeId);
    }
    @Transactional(readOnly = false)
    public int deleteAllSystemNodesProperties(long nodeId, boolean deleteNotificationList) {
        int result = manageKpiDao.deleteAllSystemNodesProperties(nodeId);
        if(deleteNotificationList){
            kpiNotificationService.deleteNotificationList(nodeId);
        }

        return result;
    }

    public NodeProperties getNodePropertiesByIdAndName(int nodeId, String propertyName) {
        NodeProperties nodeProperties = manageKpiDao.getNodePropertiesByIdAndName(nodeId, propertyName);
        return nodeProperties;
    }

    public List<SystemNode> getSystemNodesBySystemName(String systemName) {
        List<SystemNode> systemNodes = manageKpiDao.getSystemNodesBySystemName(systemName);
        return systemNodes;
    }

    public boolean isNodeHasConfiguredProperties(String systemName, long nodeId) {
        return manageKpiDao.isNodeHasConfiguredProperties(systemName, nodeId);
    }

    public boolean isTrafficTableExist(String tableName) {
        return manageKpiDao.isTableExist(tableName);
    }
}
