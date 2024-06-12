package eg.com.vodafone.service;

import eg.com.vodafone.model.*;
import eg.com.vodafone.model.enums.*;
import eg.com.vodafone.model.enums.NodeColumnType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/31/13
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations={"applicationContext-test.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "cmtTransactionManager", defaultRollback = true)
public class DataCollectionServiceTest {

    private String sampleTextDataCollectionId = "vssm_disk_input_struct";
    private String sampleTextDcConvertorName ="VSSMDiskConverter";
    private String sampleExcelDataCollectionId="mms_availablity_kpi_input_struct";
    private String sampleDatabaseDataCollectionId="header_log_input_struct";
    private String sampleGenerixTextDataCollectionId="ADD_SMS_RATE_Process_input_struct";

@Autowired
private DataCollectionServiceInterface dataCollectionService;


    @Before
    public void setup() {

    }
    @After
    public void teardown() {
    }

    @Test
    public void testListAllDbDataCollectionsNamesWithNodeEnabled(){
        dataCollectionService.listAllDatabaseDataCollections(true);
    }
    @Test
    public void testListAllDbDataCollectionsNamesWithNodeDisabled(){
        dataCollectionService.listAllDatabaseDataCollections(false);
    }
    @Test
    public void testListAllDatabaseDataCollectionsWithNodeEnabled(){
        dataCollectionService.listAllNonDataBaseDataCollection(true);
    }
    @Test
    public void testListAllDatabaseDataCollectionsWithNodeDisabled(){
        dataCollectionService.listAllNonDataBaseDataCollection(false);
    }
    @Test
    public void testGetDataCollectionTextType(){
        VInputStructure dataCollection = dataCollectionService.getDataCollection("Siebel_Services_input_struct");
        Assert.assertTrue("Test GetDataCollection Failed",dataCollection instanceof TextInputStructure);
        TextInputStructure textDC = (TextInputStructure)dataCollection;
    }
    @Test
    public void testGetDataCollectionDbType(){
        VInputStructure dataCollection = dataCollectionService.getDataCollection(sampleDatabaseDataCollectionId);
    }
    @Test
    public void testGetDataCollectionGenericTextType(){
        VInputStructure dataCollection = dataCollectionService.getDataCollection("testAlia"); //(sampleGenerixTextDataCollectionId);
        Assert.assertTrue(dataCollection.isTruncateBeforeInsertion());
    }
    @Test(expected = BusinessException.class)
    public void testValidateUniqueDataCollectionNameWithNonUniqueName(){
          dataCollectionService.validateUniqueDataCollectionName(sampleTextDataCollectionId);
    }
    @Test
    public void testValidateUniqueDataCollectionNameWithUniqueName(){
        dataCollectionService.validateUniqueDataCollectionName(sampleTextDataCollectionId+"_test");
    }
    @Test
    public void testValidateDataCollectionColumns(){

    }
    @Test
    public void testCreateDataCollectionOutputTable() throws SQLException {
       String tableName="Test_create_OUTPUT_table";
        List<DataColumn>columns = new ArrayList<DataColumn>();
        DataColumn col1 = new DataColumn("column1", DataColumnType.STRING.getName());
        col1.setSrcColumn("column1");
        col1.setStrSize(1000);
        col1.setDefaultValue("defaultString");
        columns.add(col1);
        DataColumn col2= new DataColumn("column2",DataColumnType.NUMBER.getName());
        col2.setSrcColumn("column2");
        col2.setDefaultValue("1986");
        columns.add(col2) ;
        DataColumn col3=   new DataColumn("column3",DataColumnType.FLOAT.getName());
        col3.setSrcColumn("column3");
        col3.setDefaultValue("1.22");
        columns.add(col3);
        DataColumn col4 =  new DataColumn("column4",DataColumnType.DATE.getName());
        col4.setSrcColumn("column4");
        col4.setDateFormat("dd/mm/yyyy");
        col4.setDefaultValue("22/10/2013");
        columns.add(col4);
       // dataCollectionService.createDataCollectionOutputTable(tableName,columns,NodeColumnType.NON.getTypeCode());

    }
    @Test
    public void testListAvailableOutputTables(){
         List<String> tables =dataCollectionService.listAvailableOutputTables();
        Assert.assertTrue("testListAvailableOutputTables failed",tables.size() == 605);
    }
    @Test
    public void testGetOutputTableColumns() throws SQLException {
       List<DataColumn> columns = dataCollectionService.getOutputTableColumns("dc1");
       Assert.assertTrue(" testGetOutputTableColumns failed",columns.size() >0);
    }
    @Test
    public void testValidateNewOutputTableColumns(){

    }
    @Test
    public void testUpdateOutputTable() throws SQLException {
       String outputTableName="TEST_CREATE_OUTPUT_TABLE";
       DataColumn newColumn = new DataColumn("basma_new_Column2","");
        newColumn.setStrSize(500);
        newColumn.setTypeCode(DataColumnType.NUMBER.getTypeCode());
        newColumn.setDefaultValue("1986");
       List<DataColumn> newColumns = new ArrayList<DataColumn>();
       newColumns.add(newColumn);
       //dataCollectionService.updateOutputTable(outputTableName,newColumns, NodeColumnType.CONFIGURABLE.getTypeCode());
    }
    @Test
    public void testGetColumnsOfMatchingType(){

    }
    @Test
    public void testAddDBDataCollection(){
        DBInputStructure newInputStructure = new DBInputStructure();
        newInputStructure.setId("basma_DB_Input_Structure");
        newInputStructure.setType(InputStructureType.GENERIC_DB.getTypeCode());
        newInputStructure.setNodeColumnType(NodeColumnType.CONFIGURABLE.getTypeCode());
        newInputStructure.setDbType(DataBaseType.MY_SQL.getTypeCode());
       // dataCollectionService.add(newInputStructure);
    }
    @Test

    public void testAddXmlDataCollection(){
        GenericXmlInputStructure newInputStructure = new GenericXmlInputStructure();
        newInputStructure.setId("basma_Xml_Input_Structure");
        newInputStructure.setType(InputStructureType.GENERIC_XML.getTypeCode());
        newInputStructure.setNodeColumnType(NodeColumnType.CONFIGURABLE.getTypeCode());
        //newInputStructure.setConverterId(XmlDataCollectionConvertors.NON.getConvertorId());
       // dataCollectionService.addXmlDataCollection(newInputStructure);
    }
    @Test

    public void testAddTextDataCollection(){
        GenericTextInputStructure newInputStructure = new GenericTextInputStructure();
        newInputStructure.setId("basma_Text_Input_Structure");
        newInputStructure.setType(InputStructureType.GENERIC_TEXT.getTypeCode());
        newInputStructure.setNodeColumnType(NodeColumnType.CONFIGURABLE.getTypeCode());
        newInputStructure.setDelimiter(",");
       // dataCollectionService.addTextDataCollection(newInputStructure);
    }
    @Test
    public void testEditDataCollection(){
        VInputStructure inputStructure = dataCollectionService.getDataCollection("risk_mgmt_transactions_input_struct");
        inputStructure.setExtractionSql("Select 1 FROM Dual");
        inputStructure.setDateFormat("dd/mm/yyyy");
        inputStructure.setTruncateBeforeInsertion(true);
        inputStructure.setDateColumn("dateColumn");
        inputStructure.setNodeColumnType(1);
        inputStructure.setNodeColumn("nodeName");
        inputStructure.setExtractDate(true);
        dataCollectionService.editDataCollection(inputStructure,"BASMA" );
    }
    @Test
    public void testEditDataCollectionAndUpdateExsitingTable() throws SQLException {
        VInputStructure inputStructure = dataCollectionService.getDataCollection("risk_mgmt_transactions_input_struct");
        inputStructure.setExtractionSql("Select 1 FROM Dual");
        inputStructure.setDateFormat("dd/mm/yyyy");
        inputStructure.setTruncateBeforeInsertion(true);
        inputStructure.setDateColumn("dateColumn");
        inputStructure.setNodeColumnType(1);
        inputStructure.setNodeColumn("nodeName");
        inputStructure.setExtractDate(true);
        inputStructure.setMappedTable("RADWA");

        List<DataColumn> columnsToAdd = new ArrayList<DataColumn>();

        DataColumn col1 = new DataColumn();
        col1.setName("col1");
        col1.setTypeCode(DataColumnType.STRING.getTypeCode());
        col1.setStrSize(50);
        columnsToAdd.add(col1);

        DataColumn col2 = new DataColumn();
        col2.setName("col2");
        col2.setTypeCode(DataColumnType.DATE.getTypeCode());
        col2.setDateFormat("dd/MM/yyyy");
        columnsToAdd.add(col2);

        DataColumn col3 = new DataColumn();
        col3.setName("col3");
        col3.setTypeCode(DataColumnType.FLOAT.getTypeCode());
        columnsToAdd.add(col3);

        DataColumn col4 = new DataColumn();
        col4.setName("col4");
        col4.setTypeCode(DataColumnType.NUMBER.getTypeCode());
        columnsToAdd.add(col4) ;

       //dataCollectionService.editDataCollectionAndUpdateOutputTable(inputStructure,columnsToAdd,"BASMA" );
    }
    @Test
    public void testEditDataCollectionAndCreateNewOutputTable() throws SQLException {
        VInputStructure inputStructure = dataCollectionService.getDataCollection("risk_mgmt_transactions_input_struct");
        inputStructure.setExtractionSql("Select 1 FROM Dual");
        inputStructure.setDateFormat("dd/mm/yyyy");
        inputStructure.setTruncateBeforeInsertion(true);
        inputStructure.setDateColumn("dateColumn");
        inputStructure.setNodeColumnType(1);
        inputStructure.setNodeColumn("nodeName");
        inputStructure.setExtractDate(true);
        inputStructure.setMappedTable("Basma27");

        List<DataColumn> columnsToAdd = new ArrayList<DataColumn>();

        DataColumn col1 = new DataColumn();
        col1.setName("col1");
        col1.setTypeCode(DataColumnType.STRING.getTypeCode());
        col1.setStrSize(50);
        columnsToAdd.add(col1);

        DataColumn col2 = new DataColumn();
        col2.setName("col2");
        col2.setTypeCode(DataColumnType.DATE.getTypeCode());
        col2.setDateFormat("dd/MM/yyyy");
        columnsToAdd.add(col2);

        DataColumn col3 = new DataColumn();
        col3.setName("col3");
        col3.setTypeCode(DataColumnType.FLOAT.getTypeCode());
        columnsToAdd.add(col3);

        DataColumn col4 = new DataColumn();
        col4.setName("col4");
        col4.setTypeCode(DataColumnType.NUMBER.getTypeCode());
        columnsToAdd.add(col4) ;

        //dataCollectionService.editDataCollectionAndCreateOutputTable(inputStructure,columnsToAdd, "BASMA");
    }
    @Test//(expected = BusinessException.class)
    public void testDeleteDataCollection() throws SQLException {
        //dataCollectionService.deleteDataCollection("test_call_collect_deploy_input_struct");
       // VInputStructure struct = dataCollectionService.getDataCollection("dtms_quota_counter_input_struct");
    }

    @Test
    public void testListAllDataCollectionsWithEditFlag(){
        int count = dataCollectionService.listAllDataCollectionsWithEditFlag().size();
        Assert.assertTrue(count>1);
    }

    @Test
    public void testListAllDataCollectionsWithIndexes(){
        Map<String,Boolean> result = dataCollectionService.listAllDataCollectionsWithEditFlag(1,10);
        Assert.assertTrue(result.size()==10 );
    }
    @Test
    public void testListAllDataCollectionsWithKeyword(){
        Map<String,Boolean> result = dataCollectionService.listAllDataCollectionsWithEditFlag("_");
        Assert.assertTrue(result.size()==531 );
    }
    @Test
    public void testListAllDataCollectionsWithIndexesAndKeyword(){
        Map<String,Boolean> result = dataCollectionService.listAllDataCollectionsWithEditFlag("_",1,10);
        Assert.assertTrue(result.size()==10 );
    }
    @Test
    public void testListAllDataCollections(){
        List<String> list1 = dataCollectionService.listAllDataCollections(true);
        List<String> list2 = dataCollectionService.listAllDataCollections(false);
        Map<String,Boolean> list3 = dataCollectionService.listAllDataCollectionsWithEditFlag();
        Assert.assertTrue(list1.size()+list2.size() == list3.size());
    }
    @Test
    public void testListAllNodeEnabledDataCollections(){
        List<String> list1 = dataCollectionService.listAllDatabaseDataCollections(true);
        List<String> list2 = dataCollectionService.listAllNonDataBaseDataCollection(true);
        List<String> list3 = dataCollectionService.listAllDataCollections(true);
        Assert.assertTrue(list1.size()+list2.size() == list3.size());
    }
    @Test
    public void testListAllNotNodeEnabledDataCollections(){
        List<String> list1 = dataCollectionService.listAllDatabaseDataCollections(false);
        List<String> list2 = dataCollectionService.listAllNonDataBaseDataCollection(false);
        List<String> list3 = dataCollectionService.listAllDataCollections(false);
        Assert.assertTrue(list1.size()+list2.size() == list3.size());
    }
    @Test
    public void testListByTypeAndNodeEnabled(){
        List<String> list1 = dataCollectionService.listDataCollectionsByType( InputStructureType.TEXT,true);
        List<String> list2 = dataCollectionService.listDataCollectionsByType(InputStructureType.DIRECT_TEXT,true);
        List<String> list3 = dataCollectionService.listDataCollectionsByType(InputStructureType.EXCEL,true);
        List<String> list4 = dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_INPUT,true);
        List<String> list5 = dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_TEXT,true);
        List<String> list6 = dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_XML,true);
        int total = list1.size()+list2.size()+list3.size()+list4.size()+list5.size()+list6.size();
        List<String> list7 = dataCollectionService.listAllNonDataBaseDataCollection(true);
        Assert.assertTrue(total == list7.size());
    }
    @Test
    public void testListByTypesListAndNodeEnabled(){
        List<InputStructureType>types = new ArrayList<InputStructureType>();
        types.add(InputStructureType.TEXT);
        types.add(InputStructureType.DIRECT_TEXT);
        types.add(InputStructureType.EXCEL);
        types.add(InputStructureType.GENERIC_INPUT);
        types.add(InputStructureType.GENERIC_TEXT);
        types.add(InputStructureType.GENERIC_XML);
        types.add(InputStructureType.TEXT);
        List<String> list1 = dataCollectionService.listDataCollectionsByType(types ,true);
        List<String> list2 = dataCollectionService.listAllNonDataBaseDataCollection(true);
        Assert.assertTrue(list1.size() == list2.size());
    }
    @Test
    public void testListByTypeAndNodeDisabled(){
        List<String> list1 = dataCollectionService.listDataCollectionsByType( InputStructureType.TEXT,false);
        List<String> list2 = dataCollectionService.listDataCollectionsByType(InputStructureType.DIRECT_TEXT,false);
        List<String> list3 = dataCollectionService.listDataCollectionsByType(InputStructureType.EXCEL,false);
        List<String> list4 = dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_INPUT,false);
        List<String> list5 = dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_TEXT,false);
        List<String> list6 = dataCollectionService.listDataCollectionsByType(InputStructureType.GENERIC_XML,false);
        int total = list1.size()+list2.size()+list3.size()+list4.size()+list5.size()+list6.size();
        List<String> list7 = dataCollectionService.listAllNonDataBaseDataCollection(false);
        Assert.assertTrue(total == list7.size());
    }

    @Test
    public void testListDatabaseDataCollection(){
        List<String> oracle = dataCollectionService.getDataBaseDataCollection(DataBaseType.ORACLE,true);
        Assert.assertTrue(oracle.size()>0);
    }


}

