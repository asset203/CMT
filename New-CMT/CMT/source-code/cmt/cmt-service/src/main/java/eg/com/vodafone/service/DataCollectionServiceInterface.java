package eg.com.vodafone.service;

import eg.com.vodafone.dao.DataCollectionDao;
import eg.com.vodafone.model.*;
import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.InputStructureType;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/19/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

public interface DataCollectionServiceInterface {

  public Map listAllDataCollectionsWithEditFlag();  //returns data collection name, and if it is editable

  public Map listAllDataCollectionsWithEditFlag(int startIndex,int endIndex);

  public Map listAllDataCollectionsWithEditFlag(String keyword);  //returns data collection name, and if it is editable

  public Map listAllDataCollectionsWithEditFlag(String keyword,int startIndex,int endIndex);

  public List<String> listAllDataCollections(boolean  nodeEnabled);
  public List<String> listAllDatabaseDataCollections(boolean nodeEnabled);//return data collection name,and database type
  public List<String> listAllNonDataBaseDataCollection(boolean nodeEnabled);
  public List<String> getDataBaseDataCollection(DataBaseType dataBaseType,boolean nodeEnabled);
  public List<String> listDataCollectionsByType(List<InputStructureType>types, boolean nodeEnabled);
  public List<String> listDataCollectionsByType(InputStructureType type, boolean nodeEnabled);

  public VInputStructure getDataCollection(String inputStructureId);

  public void validateUniqueDataCollectionName(String inputStructureId) throws BusinessException;


    public void createDataCollectionOutputTable(VInputStructure tableName, List<DataColumn> columns, int nodeColumnType ,String autoFilledDateColumnName);
  public List<String> listAvailableOutputTables();

  public List<DataColumn> getOutputTableColumns(String tableName);


    public void updateOutputTable(String tableName, List<DataColumn> newColumns, int nodeNameColumnType,String autoFilledDateColumnName);


  public void editDataCollection(VInputStructure inputStructure, String currentUser);


  public void editDataCollectionAndUpdateOutputTable(VInputStructure inputStructure, List<DataColumn> newColumns, String currentUser , String dataCollectionName , String dataCollectionKpiId);


  public void deleteDataCollection(String inputStructureId, boolean dropOutputTable) throws BusinessException;


  public String buildAndExecuteExtractionSqlOnTempTable(String inputStructureId, List<DataColumn> columns, String wherePart, String groupByPart,
                                                        String havingPart, String dateColumn,String dateColumnFormat,String[] sampleData);


  public void addDataCollectionUsingExsitingOutputTable(VInputStructure inputStructure, List<DataColumn> columnsToAdd, String currentUser ,  String dataCollectionName , String dataCollectionKpiId);


  public void addDataCollectionAndCreateOutputTable(VInputStructure inputStructure, List<DataColumn> outputTableColumns, String currentUser , String dataCollectionName , String dataCollectionKpiId);


  public List<XmlVendor> listXmlVendor();


  public List<XmlConverter> listXmlConverterByVendorId(long vendorId);

  public List<XmlConverterElement> listXmlConverterElementsByConverterId(long converterId);

  public void setDataCollectionDao(DataCollectionDao dataCollectionDao);

  public List<String> getDataBaseDataCollection(List<Long> dataBaseTypes, boolean nodeEnabled);
  
  public List<KPIType> listAllKpi();
  
  public List<KPIType> getKpiTypeByDataCollectionName(String dataCollectionName);
}
