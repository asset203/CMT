package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.DataCollectionDao;
import eg.com.vodafone.model.DataColumn;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 7/22/13
 * Time   : 8:22 AM
 */
public class DcOutputTablesHandler  implements TransactionSynchronization {

    private String tableName;
    private boolean isNewTable;
    private List<DataColumn> tableColumns;
    private boolean tableCreated;

    private DataCollectionDao dataCollectionDao;




    public DcOutputTablesHandler(String tableName,List<DataColumn> newColumns,boolean isNewTable,DataCollectionDao dataCollectionDao ){
         this.tableName = tableName;
        this.isNewTable = isNewTable;
        this.tableColumns = newColumns;
        this.dataCollectionDao = dataCollectionDao;
    }
    public DcOutputTablesHandler(){
    }
    @Override
    public void suspend() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void flush() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void beforeCompletion() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterCommit() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterCompletion(int status) {
       if(status == STATUS_ROLLED_BACK){
          if(isNewTable)
          {
            dataCollectionDao.dropInputStructureOutputTable
                      (tableName,DataCollectionService.SEQUENCE_NAME_PREFIX+tableName);
          }
           else
          {
               dataCollectionDao.removeColumnsFromOutputTable(tableName,tableColumns);
          }
       }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isNewTable() {
        return isNewTable;
    }

    public void setNewTable(boolean newTable) {
        isNewTable = newTable;
    }

    public List<DataColumn> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<DataColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public boolean isTableCreated() {
        return tableCreated;
    }

    public void setTableCreated(boolean tableCreated) {
        this.tableCreated = tableCreated;
    }
}
