package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.DataCollectionDao;
import eg.com.vodafone.dao.KPITypeDao;
import eg.com.vodafone.dao.UserDao;
import eg.com.vodafone.dao.XmlConverterDao;
import eg.com.vodafone.dao.XmlConverterElementDao;
import eg.com.vodafone.dao.XmlVendorDao;
import eg.com.vodafone.model.*;
import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.model.enums.InputStructureType;
import eg.com.vodafone.model.enums.NodeColumnType;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.utils.Utils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import static eg.com.vodafone.service.BusinessException.*;
import static eg.com.vodafone.utils.Utils.*;

/**
 * Created with IntelliJ IDEA. User: basma.alkerm Date: 3/19/13 Time: 2:06 PM To
 * change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true, value = "cmtTransactionManager")
public class DataCollectionService implements DataCollectionServiceInterface {

    public static final String NODE_NAME_COLUMN = "NODE_NAME";
    public static final String SEQUENCE_NAME_PREFIX = "GSQ_";
    public static final String TRIGGER_NAME_PREFIX = "GTR_";
    public static final String SELECT_KEY_WORD = " SELECT ";
    public static final String FROM_KEY_WORD = " FROM ";
    public static final String WHERE_KEY_WORD = " WHERE ";
    public static final String HAVING_KEY_WORD = " HAVING ";
    public static final String GROUP_BY_KEY_WORD = " GROUP BY ";

    private DataCollectionDao dataCollectionDao;

    @Autowired
    XmlVendorDao xmlVendorDao;

    @Autowired
    KPITypeDao kpiTypeDao;

    @Autowired
    UserDao userDao;

    @Autowired
    XmlConverterDao xmlConverterDao;

    @Autowired
    XmlConverterElementDao xmlConverterElementDao;

    @Autowired
    public void setDataCollectionDao(DataCollectionDao dataCollectionDao) {
        this.dataCollectionDao = dataCollectionDao;
    }

    @Override
    public List<String> listAllDataCollections(boolean nodeEnabled) {
        return dataCollectionDao.getAllInputStructuresIds(nodeEnabled);
    }

    @Override
    public Map listAllDataCollectionsWithEditFlag() {
        return dataCollectionDao.listAllDataCollectionsWithEditFlag();
    }

    @Override
    public Map listAllDataCollectionsWithEditFlag(int startIndex, int endIndex) {
        return dataCollectionDao.listAllDataCollectionsWithEditFlag(startIndex, endIndex);
    }

    @Override
    public Map listAllDataCollectionsWithEditFlag(String keyword) {
        return dataCollectionDao.listAllDataCollectionsWithEditFlag(keyword);
    }

    @Override
    public Map listAllDataCollectionsWithEditFlag(String keyword, int startIndex, int endIndex) {
        return dataCollectionDao.listAllDataCollectionsWithEditFlag(keyword, startIndex, endIndex);
    }

    @Override
    public List<String> listAllDatabaseDataCollections(boolean nodeEnabled) {
        List<Long> types = new ArrayList<Long>();
        types.add(new Long(InputStructureType.DIRECT_DB.getTypeCode()));
        types.add(new Long(InputStructureType.DB.getTypeCode()));
        types.add(new Long(InputStructureType.GENERIC_DB.getTypeCode()));
        return dataCollectionDao.listDataCollectionsNames(types, nodeEnabled);
    }

    @Override
    public List<String> listDataCollectionsByType(List<InputStructureType> types, boolean nodeEnabled) {
        List<Long> typesCodes = new ArrayList<Long>();
        for (InputStructureType type : types) {
            typesCodes.add(new Long(type.getTypeCode()));
        }
        return dataCollectionDao.listDataCollectionsNames(typesCodes, nodeEnabled);
    }

    @Override
    public List<String> listDataCollectionsByType(InputStructureType type, boolean nodeEnabled) {
        return dataCollectionDao.listDataCollectionsNames(type.getTypeCode(), nodeEnabled);
    }

    @Override
    public List<String> listAllNonDataBaseDataCollection(boolean nodeEnabled) {
        List<Long> types = new ArrayList<Long>();
        types.add(new Long(InputStructureType.EXCEL.getTypeCode()));
        types.add(new Long(InputStructureType.TEXT.getTypeCode()));
        types.add(new Long(InputStructureType.DIRECT_TEXT.getTypeCode()));
        types.add(new Long(InputStructureType.GENERIC_TEXT.getTypeCode()));
        types.add(new Long(InputStructureType.GENERIC_XML.getTypeCode()));
        types.add(new Long(InputStructureType.GENERIC_INPUT.getTypeCode()));
        return dataCollectionDao.listDataCollectionsNames(types, nodeEnabled);
    }

    @Override
    public VInputStructure getDataCollection(String inputStructureId) {
        VInputStructure inputStructure = dataCollectionDao.getInputStructure(inputStructureId);
        if (inputStructure == null) {
            throw new BusinessException(DATA_COLLECTION_NOT_EXIST);
        }
        if (inputStructure.getType() == InputStructureType.GENERIC_INPUT.getTypeCode()) {
            ExtractionField[] fields = dataCollectionDao.getExtractionFields(inputStructureId).toArray(new ExtractionField[]{});
            ((GenericInputStructure) inputStructure).setExtractionFields(fields);
            GenericMapping mapping = new GenericMapping();
            mapping.setDateColumnName(inputStructure.getDateColumn());
            mapping.setNodeColumnName(inputStructure.getNodeColumn());
            mapping.setTableName(inputStructure.getMappedTable());
            mapping.setAssocMapping(dataCollectionDao.readMapping(inputStructureId));
            ((GenericInputStructure) inputStructure).setOutputMapping(mapping);
        } else {
            inputStructure.setColumnsList(dataCollectionDao.getInputStructureColumns(inputStructureId));
        }
        return inputStructure;
    }

    @Override
    public void validateUniqueDataCollectionName(String inputStructureId) throws BusinessException {
        if (!dataCollectionDao.isInputStructureIdUnique(inputStructureId)) {
            throw new BusinessException(DUPLICATE_INPUT_STRUCTURE_NAME);
        }
        if (dataCollectionDao.isInputStructureNameUsedInSchema(inputStructureId)) {
            throw new BusinessException(NAME_USED_BY_OTHER_DB_OBJECT);
        }

    }

    @Override
    public void createDataCollectionOutputTable(VInputStructure inputStructure, List<DataColumn> columns, int nodeColumnType, String autoFilledDateColumnName) {
       // NaDa changed first paramter type from String to VInputStructure
       String tableName = inputStructure.getId();
        if (nodeColumnType == NodeColumnType.CONFIGURABLE.getTypeCode()) {
            DataColumn nodeNameColumn = getNodeNameColumn();
            columns.add(nodeNameColumn);
        }
        if (StringUtils.hasText(autoFilledDateColumnName)) {
            DataColumn autoFilledDateColumn = getDateColumn(autoFilledDateColumnName);
            columns.add(autoFilledDateColumn);
        }
        dataCollectionDao.createOutputTable(inputStructure, columns, SEQUENCE_NAME_PREFIX + tableName, TRIGGER_NAME_PREFIX + tableName);
    }

    @Override
    public List<String> listAvailableOutputTables() {
        return dataCollectionDao.getOutputTablesNames();
    }

    @Override
    public List<DataColumn> getOutputTableColumns(String tableName) {
        if (!StringUtils.hasText(tableName)) {
            throw new BusinessException(INVALID_TABLE_NAME);
        }
        List<DataColumn> columns = dataCollectionDao.getOutputTableColumns(tableName);
        if (columns == null) {
            throw new BusinessException(TABLE_DOES_NOT_EXIST);
        }
        return columns;
    }

    @Override
    public void updateOutputTable(String tableName, List<DataColumn> newColumns, int nodeNameColumnType,
            String autoFilledDateColumnName) {
        if (!StringUtils.hasText(tableName)) {
            throw new BusinessException(INVALID_TABLE_NAME);
        }
        List<DataColumn> oldColumns = getOutputTableColumns(tableName);
        if (nodeNameColumnType == NodeColumnType.CONFIGURABLE.getTypeCode() && !isColumnExist(oldColumns, NODE_NAME_COLUMN)) {
            DataColumn nodeNameColumn = getNodeNameColumn();
            if (newColumns == null) {
                newColumns = new ArrayList<DataColumn>();
            }
            newColumns.add(nodeNameColumn);
        } else if (nodeNameColumnType == NodeColumnType.MAPPED.getTypeCode() && isColumnExist(oldColumns, NODE_NAME_COLUMN)) {
            for (DataColumn column : newColumns) {
                if (NODE_NAME_COLUMN.equalsIgnoreCase(column.getName())) {
                    newColumns.remove(column);
                }
            }

        }
        if (StringUtils.hasText(autoFilledDateColumnName)) {
            if (!isColumnExist(oldColumns, autoFilledDateColumnName)) {
                if (!isColumnExist(newColumns, autoFilledDateColumnName)) {
                    newColumns.add(getDateColumn(autoFilledDateColumnName));
                }
            }
        }
        if (newColumns == null || newColumns.isEmpty()) {
            return;
        }
        boolean done = dataCollectionDao.addColumnsToOutputTable(tableName, newColumns);
        if (!done) {
            throw new BusinessException(FAILED_TO_UPDATE_TABLE);
        }
    }

    private void addDatabaseDataCollection(DBInputStructure inputStructure, String currentUser) {
        dataCollectionDao.saveDBInputStructure(inputStructure, currentUser);
        dataCollectionDao.savInputStructureColumns(inputStructure.getId(), inputStructure.getColumnsList());
    }

    private void addXmlDataCollection(GenericXmlInputStructure inputStructure, String currentUser) {
        dataCollectionDao.saveGenericXmlInputStructure(inputStructure, currentUser);
        dataCollectionDao.savInputStructureColumns(inputStructure.getId(), inputStructure.getColumnsList());
    }

    private void addTextDataCollection(GenericTextInputStructure inputStructure, String currentUser) {
        dataCollectionDao.saveGenericTextInputStructure(inputStructure, currentUser);
        dataCollectionDao.savInputStructureColumns(inputStructure.getId(), inputStructure.getColumnsList());
    }

    @Override
    @Transactional(readOnly = false)
    public void addDataCollectionUsingExsitingOutputTable(VInputStructure inputStructure, List<DataColumn> columnsToAdd, String currentUser, String dataCollectionName, String dataCollectionKpiId) {

        updateOutputTable(inputStructure.getMappedTable(), columnsToAdd, inputStructure.getNodeColumnType(), inputStructure.getAutoFilledDateColumn());
        TransactionSynchronizationManager.
                registerSynchronization(
                        new DcOutputTablesHandler(inputStructure.getMappedTable(), columnsToAdd, false, dataCollectionDao));
        addDataCollection(inputStructure, currentUser, false);
        
        //Added by Awad
        updateOutputTableColumnsToCreateIndexes(inputStructure.getMappedTable(), inputStructure.getColumnsList());
        addDataCollectionKpi(dataCollectionName, dataCollectionKpiId, currentUser);
        // End 
    }

    @Override
    @Transactional(readOnly = false)
    public void addDataCollectionAndCreateOutputTable(VInputStructure inputStructure, List<DataColumn> outputTableColumns, String currentUser, String dataCollectionName, String dataCollectionKpiId) {
        createDataCollectionOutputTable(inputStructure, outputTableColumns,
                inputStructure.getNodeColumnType(), inputStructure.getAutoFilledDateColumn());
        TransactionSynchronizationManager.
                registerSynchronization(
                        new DcOutputTablesHandler(inputStructure.getId(), outputTableColumns, true, dataCollectionDao));
        addDataCollection(inputStructure, currentUser, true);
        //Added by Awad
        addDataCollectionKpi(dataCollectionName, dataCollectionKpiId, currentUser);
        // End 
    }

    private void addDataCollection(VInputStructure inputStructure, String currentUser, boolean newOutputTable) {
        if (newOutputTable) {
            dataCollectionDao.addOutputTableName(inputStructure.getId());
        }
        if (inputStructure instanceof DBInputStructure) {
            addDatabaseDataCollection((DBInputStructure) inputStructure, currentUser);
        } else if (inputStructure instanceof GenericXmlInputStructure) {
            addXmlDataCollection((GenericXmlInputStructure) inputStructure, currentUser);
        } else if (inputStructure instanceof GenericTextInputStructure) {
            addTextDataCollection((GenericTextInputStructure) inputStructure, currentUser);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void editDataCollection(VInputStructure inputStructure, String currentUser) {
        dataCollectionDao.updateInputStructure(inputStructure, currentUser);
        dataCollectionDao.updateInputStructureColumns(inputStructure.getId(), inputStructure.getColumnsList());
    }

    @Override
    @Transactional(readOnly = false)
    public void editDataCollectionAndUpdateOutputTable(VInputStructure inputStructure, List<DataColumn> newColumns, String currentUser, String dataCollectionName, String dataCollectionKpiId) {
        updateOutputTable(inputStructure.getMappedTable(), newColumns, inputStructure.getNodeColumnType(), inputStructure.getAutoFilledDateColumn());
        TransactionSynchronizationManager.
                registerSynchronization(
                        new DcOutputTablesHandler(inputStructure.getMappedTable(), newColumns, false, dataCollectionDao));
        dataCollectionDao.updateInputStructure(inputStructure, currentUser);
        dataCollectionDao.updateInputStructureColumns(inputStructure.getId(), inputStructure.getColumnsList());

        //Added by Awad
        updateOutputTableColumnsToCreateIndexes(inputStructure.getMappedTable(), inputStructure.getColumnsList());
        editDataCollectionKpi(dataCollectionName, dataCollectionKpiId, currentUser);
        //End 
    }

    /* @Override
    @Transactional(readOnly = false)
    public void editDataCollectionAndCreateOutputTable(VInputStructure inputStructure, List<DataColumn> columns, String currentUser) {
        createDataCollectionOutputTable(inputStructure.getMappedTable(),columns,inputStructure.getNodeColumnType());
        TransactionSynchronizationManager.
                registerSynchronization(
                        new DcOutputTablesHandler(inputStructure.getMappedTable(), columns, true,dataCollectionDao));
        dataCollectionDao.updateInputStructure(inputStructure,currentUser );
        dataCollectionDao.updateInputStructureColumns(inputStructure.getId(),inputStructure.getColumnsList());
    } */
    @Override
    @Transactional(readOnly = false)
    public void deleteDataCollection(String inputStructureId, boolean dropOutputTable) throws BusinessException {
        if (dataCollectionDao.isInputStructureUsedBySystemOrNode(inputStructureId)) {
            throw new BusinessException(INPUT_STRUCTURE_USED_FAILD_TO_DELETE);
        }
        String outputTableName = null;
        //get the table name before dropping the record in table "Output_table_Info"
        if (dropOutputTable) {
            outputTableName = dataCollectionDao.getOutputTableName(inputStructureId);
        }
        dataCollectionDao.deleteInputStructureColumns(inputStructureId);
        dataCollectionDao.deleteInputStructure(inputStructureId);
        if (outputTableName != null) {
            dropDataCollectionOutputTable(outputTableName);
        }
    }

    @Override
    public String buildAndExecuteExtractionSqlOnTempTable(String inputStructureId, List<DataColumn> columns, String wherePart, String groupByPart,
            String havingPart, String dateColumnName, String dateColumnFormat, String[] sampleData) {
        //1- build "select part"
        long rnd = RandomUtils.nextInt();
        String tempTaleName = "TMB" + new Date().getTime() + "_" + rnd;
        StringBuffer query = new StringBuffer(SELECT_KEY_WORD);
        StringBuffer insertStatement = new StringBuffer("INSERT INTO ")
                .append(tempTaleName).append('(');
        StringBuffer columnsPart = new StringBuffer();
        StringBuffer valuesPart = new StringBuffer();
        Map<String, Object> params = new HashMap<String, Object>();
        DataColumn dateColumn = null;
        boolean checkForDateColumn = StringUtils.hasText(dateColumnName);
        HashMap<Integer, DataColumn> columnsByIndex = new HashMap<Integer, DataColumn>();
        int maxIndex = 0;
        for (DataColumn column : columns) {
            columnsByIndex.put(column.getIndex(), column);
            if (column.getIndex() > maxIndex) {
                maxIndex = column.getIndex();
            }
        }
        for (int i = 0; i <= maxIndex; i++) {
            DataColumn col = columnsByIndex.get(i);
            if (checkForDateColumn && col.getSrcColumn().equals(dateColumnName)) {
                dateColumn = col;
                checkForDateColumn = false;
            }
            if (!col.isActive()) {
                continue;
            }
            if (StringUtils.hasText(col.getSqlExpression())) {
                query.append(col.getSqlExpression()).append(" AS ").append(col.getSrcColumn()).append(',');
            } else {
                query.append(col.getSrcColumn()).append(',');
                columnsPart.append(col.getSrcColumn()).append(',');
                valuesPart.append(":").append(col.getSrcColumn()).append(',');
                if (col.getTypeCode() == DataColumnType.DATE.getTypeCode()) {
                    params.put(col.getSrcColumn(), Utils.getTimeStampObject(col.getDateFormat(), sampleData[i]));
                } else {
                    params.put(col.getSrcColumn(), StringUtils.hasText(sampleData[i]) ? sampleData[i].trim() : null);
                }
            }
        }
        query.deleteCharAt(query.length() - 1);
        columnsPart.deleteCharAt(columnsPart.length() - 1);
        valuesPart.deleteCharAt(valuesPart.length() - 1);

        query.append(FROM_KEY_WORD).append(tempTaleName);
        insertStatement.append(columnsPart).append(") VALUES (").append(valuesPart).append(')');

        //2- the where,having,group by part
        String dateCondition = null;
        String dateString = "";
        if (dateColumn != null) {
            String javaFmt = "";
            String dateConditionToUse = "";
            if (dateColumnFormat.contains("H") || dateColumnFormat.contains("h")
                    || dateColumnFormat.contains("K") || dateColumnFormat.contains("k")) {
                javaFmt = JAVA_DATE_TIME_FORMAT;
                dateConditionToUse = HOURLY_DATE_CONDITION;
            } else {
                javaFmt = JAVA_DATE_FORMAT;
                dateConditionToUse = DAILY_DATE_CONDITION;

            }
            if (StringUtils.hasText(sampleData[dateColumn.getIndex()])) {
                dateString = Utils.convertToFormat(sampleData[dateColumn.getIndex()], dateColumn.getDateFormat(), javaFmt);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(javaFmt);
                dateString = simpleDateFormat.format(new Date());
            }
            dateCondition = dateConditionToUse;
            dateCondition = dateCondition.replace(DATE_COLUMN_NAME_PLACEHOLDER, dateColumn.getSrcColumn());
            dateCondition = dateCondition.replace(TARGET_DATE_STRING_PLACEHOLDER, dateString);
        }
        if (StringUtils.hasText(wherePart)) {
            query.append(WHERE_KEY_WORD).append(wherePart);
            if (StringUtils.hasText(dateCondition)) {
                query.append(" AND ").append(dateCondition);
            }
        } else {
            if (StringUtils.hasText(dateCondition)) {
                query.append(WHERE_KEY_WORD).append(dateCondition);
            }
        }
        if (StringUtils.hasText(havingPart)) {
            query.append(HAVING_KEY_WORD).append(havingPart);
        }
        if (StringUtils.hasText(groupByPart)) {
            query.append(GROUP_BY_KEY_WORD).append(groupByPart);
        }

        //3- test execution
        try {
            //create temp table
            dataCollectionDao.createTempTable(tempTaleName, columns);
            dataCollectionDao.executeInsertOnTempSchema(insertStatement.toString(), params);
            dataCollectionDao.executeQueryOnTempSchema(query.toString());
        } finally {
            //drop temp table
            dataCollectionDao.dropInputStructureTempTable(tempTaleName);
        }

        //4- replace sample values by placeHolders
        String queryStr = query.toString();
        if (StringUtils.hasText(dateColumnName)) {
            queryStr = queryStr.replace(dateString, TARGET_DATE_STRING_PLACEHOLDER);
        }
        queryStr = queryStr.replace(tempTaleName, TEMP_TABLE_NAME_PLACEHOLDER);

        return queryStr;
    }

    private boolean isColumnExist(List<DataColumn> columns, String targetColumn) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getName().equalsIgnoreCase(targetColumn)) {
                return true;
            }
        }
        return false;
    }

    private void dropDataCollectionOutputTable(String tableName) {
        dataCollectionDao.dropInputStructureOutputTable(tableName, SEQUENCE_NAME_PREFIX + tableName);
        dataCollectionDao.deleteTableName(tableName);
    }

    private DataColumn getNodeNameColumn() {
        DataColumn nodeNameColumn = new DataColumn();
        nodeNameColumn.setName(NODE_NAME_COLUMN);
        nodeNameColumn.setTypeCode(DataColumnType.STRING.getTypeCode());
        nodeNameColumn.setStrSize(1024);
        // Awad
        nodeNameColumn.setKpiType(4);
        // End
        return nodeNameColumn;
    }

    private DataColumn getDateColumn(String name) {
        DataColumn dateColumn = new DataColumn();
        dateColumn.setName(name);
        dateColumn.setTypeCode(DataColumnType.DATE.getTypeCode());
        return dateColumn;
    }

    @Override
    public List<XmlVendor> listXmlVendor() {
        return xmlVendorDao.listXmlVendors();
    }

    @Override
    public List<XmlConverter> listXmlConverterByVendorId(long vendorId) {
        return xmlConverterDao.listXmlConverterByVendorId(vendorId);
    }

    @Override
    public List<XmlConverterElement> listXmlConverterElementsByConverterId(long converterId) {
        return xmlConverterElementDao.listXmlConverterElementByConverterId(converterId);
    }

    @Override
    public List<String> getDataBaseDataCollection(DataBaseType dataBaseType, boolean nodeEnabled) {
        return dataCollectionDao.getDataBaseDataCollections(dataBaseType, nodeEnabled);
    }

    @Override
    public List<String> getDataBaseDataCollection(List<Long> dataBaseTypes, boolean nodeEnabled) {
        return dataCollectionDao.getDataBaseDataCollections(dataBaseTypes, nodeEnabled);
    }

    @Override
    public List<KPIType> listAllKpi() {
        return kpiTypeDao.listAllKpi();
    }

    //Added by Awad
    private void addDataCollectionKpi(String dataCollectionName, String dataCollectionKpiId, String userName) {

        User user = userDao.findUserByUsername(userName);
        dataCollectionDao.saveDataCollectionKpi(dataCollectionName, dataCollectionKpiId, user.getId());
    }
    // End

    //Added by Awad
    private void editDataCollectionKpi(String dataCollectionName, String dataCollectionKpiId, String userName) {

        User user = userDao.findUserByUsername(userName);
        dataCollectionDao.deleteDataCollectionKpi(dataCollectionName);
        dataCollectionDao.saveDataCollectionKpi(dataCollectionName, dataCollectionKpiId, user.getId());
    }
    // End

    @Override
    public List<KPIType> getKpiTypeByDataCollectionName(String dataCollectionName) {
        return kpiTypeDao.getKpiTypeByDataCollectionName(dataCollectionName);
    }

    private void updateOutputTableColumnsToCreateIndexes(String tableName , List<DataColumn> dataCollectionColumns) {

        if (!StringUtils.hasText(tableName)) {
            throw new BusinessException(INVALID_TABLE_NAME);
        }
        List<DataColumn> tableColumns = getOutputTableColumns(tableName);
        
        boolean done = dataCollectionDao.createIndexForColumns(tableName ,tableColumns, dataCollectionColumns);
        
        if (!done) {
            throw new BusinessException(FAILED_TO_UPDATE_TABLE);
        }
    }
    
}
