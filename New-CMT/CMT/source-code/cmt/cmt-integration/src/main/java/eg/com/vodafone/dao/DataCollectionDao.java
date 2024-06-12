package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.*;
import eg.com.vodafone.model.*;
import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.model.enums.InputStructureType;
import eg.com.vodafone.model.enums.NodeColumnType;
import eg.com.vodafone.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * Created with IntelliJ IDEA. Author : basma.alkerm Date : 3/14/13 Time : 11:23
 * AM
 */
@Repository
public class DataCollectionDao {

    public final static String INPUT_STRUCTURE_TABLE_NAME = "VINPUT_STRUCTURE";
    public final static String INPUT_INPUT_STRUCTURES_TABLE_NAME = "VINPUT_INPUT_STRUCTURES";
    public final static String DATA_COLUMN_TABLE_NAME = "DATA_COLUMN";
    public final static String DB_INPUT_STRUCTURE_TABLE_NAME = "DB_INPUT_STRUCT";
    public final static String GENERIC_TEXT_INPUT_STRUCTURE_TABLE_NAME = "GENERIC_TEXT_INPUT_STRUCTURE";
    public final static String GENERIC_XML_INPUT_STRUCTURE_TABLE_NAME = "GENERIC_XML_INPUT_STRUCTURE";
    public final static String EXCEL_INPUT_STRUCTURE_TABLE_NAME = "EXCEL_INPUT_STRUCT";
    public final static String TEXT_INPUT_STRUCTURE_TABLE_NAME = "TEXT_INPUT_STRUCT";
    public final static String OUTPUT_TABLES_TABLE_NAME = "OUTPUT_TABLES_NAMES";
    public final static String GENERIC_MAPPING_TABLE_NAME = "GENERIC_MAPPING";
    public final static String OUTPUT_TABLE_INFO_TABLE_NAME = "OUTPUT_TABLE_INFO";

    //Added by Awad
    public final static String DATA_COLLECTION_KPI = "CPD_DATA_COLLECTION_KPI";
    //End

    private final SimpleJdbcInsert insertInputStructure;
    private final SimpleJdbcInsert insertDBInputStructure;
    private final SimpleJdbcInsert insertGenericTextInputStructure;
    private final SimpleJdbcInsert insertGenericXmlInputStructure;
    private final SimpleJdbcInsert insertDataColumn;
    private final SimpleJdbcInsert insertOutputTableName;
    private final SimpleJdbcInsert insertOutputTableInfo;

    //Added by Awad
    private final JdbcTemplate insertDataCollectionKpi;
    // End
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate cmtDbTemplate;
    private JdbcTemplate oldDcTablesTemplate;
    private JdbcTemplate tempDbTemplate;
    private NamedParameterJdbcTemplate namedParameterTempJdbcTemplate;
    private final static String TIME_ZONE = "GMT+2:00";

    private final static Logger logger = LoggerFactory.getLogger(DataCollectionDao.class);

    @Autowired
    public DataCollectionDao(@Qualifier(value = "cmtDataSource") DataSource dataSource1) {
        insertInputStructure = new SimpleJdbcInsert(dataSource1)
                .withTableName(INPUT_STRUCTURE_TABLE_NAME);

        insertDBInputStructure = new SimpleJdbcInsert(dataSource1)
                .withTableName(DB_INPUT_STRUCTURE_TABLE_NAME);

        insertGenericTextInputStructure = new SimpleJdbcInsert(dataSource1)
                .withTableName(GENERIC_TEXT_INPUT_STRUCTURE_TABLE_NAME);

        insertGenericXmlInputStructure = new SimpleJdbcInsert(dataSource1)
                .withTableName(GENERIC_XML_INPUT_STRUCTURE_TABLE_NAME);

        insertDataColumn = new SimpleJdbcInsert(dataSource1)
                .withTableName(DATA_COLUMN_TABLE_NAME);

        insertOutputTableName = new SimpleJdbcInsert(dataSource1)
                .withTableName(OUTPUT_TABLES_TABLE_NAME);

        insertOutputTableInfo = new SimpleJdbcInsert(dataSource1)
                .withTableName(OUTPUT_TABLE_INFO_TABLE_NAME);

        insertDataCollectionKpi = new JdbcTemplate(dataSource1);

        cmtDbTemplate = new JdbcTemplate(dataSource1);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource1);

    }

    @Autowired
    public void setOldDcTablesTemplate(@Qualifier(value = "vfePP3DataSource") DataSource dataSource) {
        oldDcTablesTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setTempDbTemplate(@Qualifier(value = "cmtTempDataSource") DataSource dataSource) {
        tempDbTemplate = new JdbcTemplate(dataSource);
        namedParameterTempJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void saveDBInputStructure(DBInputStructure dbInputStructure, String currentUser) {
        logger.debug("saving DBInputStructure");
        saveInputStructureCommonAttributes(dbInputStructure, currentUser);
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", dbInputStructure.getId())
                .addValue("DB_TYPE", dbInputStructure.getDbType());
        insertDBInputStructure.execute(params);
        logger.debug(" save DBInputStructure done");
    }

    public void saveGenericTextInputStructure(GenericTextInputStructure textInputStructure, String currentUser) {
        logger.debug("saving GenericTextInputStructure");
        saveInputStructureCommonAttributes(textInputStructure, currentUser);
        MapSqlParameterSource params = new MapSqlParameterSource("DELEMITER", textInputStructure.getDelimiter()).
                addValue("USE_HEADERS", textInputStructure.isUseHeaders() ? "Y" : "N").
                addValue("INPUT_STRUCT_ID", textInputStructure.getId()).
                addValue("IGNORE_LINES", textInputStructure.getIgnoredLinesCount());

        insertGenericTextInputStructure.execute(params);
        logger.debug("save genericTextInputStructure done");
    }

    public void saveGenericXmlInputStructure(GenericXmlInputStructure xmlInputStructure, String currentUser) {
        logger.debug("saving genericXmlInputStructure ");
        saveInputStructureCommonAttributes(xmlInputStructure, currentUser);
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", xmlInputStructure.getId()).
                addValue("IS_SIMPLE", xmlInputStructure.isSimple() ? "Y" : "N").
                addValue("CONVERTOR_ID", xmlInputStructure.getConverterId());
        insertGenericXmlInputStructure.execute(params);
        logger.debug("Save genericXmlInputStructure done");
    }

    public boolean isInputStructureIdUnique(String idToCheck) {
        logger.debug("check {} is unique data collection id", idToCheck);
        StringBuffer query = new StringBuffer("SELECT COUNT(INPUT_STRUCT_ID)  FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID) ");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", idToCheck);

        try {
            int used = namedParameterJdbcTemplate.queryForInt(query.toString(), params);
            return !(used > 0);
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    public void savInputStructureColumns(String inputStructureId, List<DataColumn> columns) {
        if (columns == null) {
            logger.debug("input structure {} columns = null");
            return;
        }
        logger.debug("start save {} columns for inputStructure {}", columns.size(), inputStructureId);
        MapSqlParameterSource columnParams;
        for (int i = 0; i < columns.size(); i++) {
            columnParams = new MapSqlParameterSource("NAME", columns.get(i).getName()).
                    addValue("TYPE_CODE", columns.get(i).getTypeCode()).
                    addValue("COL_INDEX", columns.get(i).getIndex()).
                    addValue("SRC_COLUMN", columns.get(i).getSrcColumn()).
                    addValue("VINPUT_STRUCT_ID", inputStructureId).
                    addValue("SQL_EXSPRESSION", columns.get(i).getSqlExpression()).
                    addValue("ACTIVE", columns.get(i).isActive() ? "Y" : "N").
                    addValue("DATE_FORMAT", columns.get(i).getDateFormat()).
                    addValue("DEFAULT_VALUE", columns.get(i).getDefaultValue()).
                    addValue("STRING_SIZE", columns.get(i).getStrSize()).
                    addValue("KPI_TYPE", columns.get(i).getKpiType());
            insertDataColumn.execute(columnParams);
        }
        logger.debug("saving columns for {} done", inputStructureId);

    }

    public List<String> getAllInputStructuresIds(boolean nodeEnabled) {
        MapSqlParameterSource namedParameters
                = new MapSqlParameterSource("NODE_COLUMN_TYPE", NodeColumnType.CONFIGURABLE.getTypeCode())
                        .addValue("OLD_DC_TYPES", Utils.getExcludedDcTypes());

        StringBuffer query = new StringBuffer("SELECT INPUT_STRUCT_ID FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME);
        if (nodeEnabled) {
            query.append(" WHERE (NODE_COLUMN_TYPE = :NODE_COLUMN_TYPE)");
        } else {
            query.append(" WHERE (NODE_COLUMN_TYPE <> :NODE_COLUMN_TYPE)");
        }
        query.append(" OR ( TYPE IN (:OLD_DC_TYPES) )");
        return namedParameterJdbcTemplate.queryForList(query.toString(), namedParameters, String.class);
    }

    public Map listAllDataCollectionsWithEditFlag() {
        StringBuffer query1
                = new StringBuffer("SELECT INPUT_STRUCT_ID, 'true' AS EDITABLE FROM ")
                        .append(INPUT_STRUCTURE_TABLE_NAME)
                        .append(" WHERE TYPE NOT IN ( :nonEditableTypes) ")
                        .append(" UNION SELECT INPUT_STRUCT_ID, 'false' AS EDITABLE FROM ")
                        .append(INPUT_STRUCTURE_TABLE_NAME)
                        .append("  WHERE TYPE IN (:nonEditableTypes) ORDER BY INPUT_STRUCT_ID ASC ");

        List<Integer> nonEditableTypes = new ArrayList<Integer>();
        nonEditableTypes.add(InputStructureType.EXCEL.getTypeCode());
        nonEditableTypes.add(InputStructureType.TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.GENERIC_INPUT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DB.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_DB.getTypeCode());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("nonEditableTypes", nonEditableTypes);

        return (Map<String, Boolean>) namedParameterJdbcTemplate.query(query1.toString(), namedParameters, new EditableInputStructureMapExtractor());
    }

    public Map listAllDataCollectionsWithEditFlag(int start, int end) {
        StringBuffer query1
                = new StringBuffer("SELECT INPUT_STRUCT_ID,  EDITABLE,  RANK  FROM (")
                        .append("SELECT INPUT_STRUCT_ID, 'true' AS EDITABLE ,RANK FROM ")
                        .append("(SELECT  INPUT_STRUCT_ID ,TYPE ,DENSE_RANK() OVER( ORDER BY INPUT_STRUCT_ID ) AS RANK  FROM  VINPUT_STRUCTURE)")
                        .append("WHERE TYPE NOT IN (:nonEditableTypes) UNION SELECT INPUT_STRUCT_ID, 'false' AS EDITABLE,RANK FROM")
                        .append("(SELECT  INPUT_STRUCT_ID ,TYPE ,DENSE_RANK() OVER( ORDER BY INPUT_STRUCT_ID ) AS RANK  FROM  VINPUT_STRUCTURE)")
                        .append("WHERE TYPE  IN (:nonEditableTypes) )")
                        .append(" WHERE RANK BETWEEN :START_IDX AND :END_IDX  ORDER BY INPUT_STRUCT_ID ASC ");

        List<Integer> nonEditableTypes = new ArrayList<Integer>();
        nonEditableTypes.add(InputStructureType.EXCEL.getTypeCode());
        nonEditableTypes.add(InputStructureType.TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.GENERIC_INPUT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DB.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_DB.getTypeCode());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("nonEditableTypes", nonEditableTypes);
        namedParameters.addValue("START_IDX", start).addValue("END_IDX", end);

        return (Map<String, Boolean>) namedParameterJdbcTemplate.query(query1.toString(), namedParameters, new EditableInputStructureMapExtractor());
    }

    public Map listAllDataCollectionsWithEditFlag(String keyword, int start, int end) {
        StringBuffer query1
                = new StringBuffer("SELECT INPUT_STRUCT_ID,  EDITABLE,  RANK  FROM (")
                        .append("SELECT INPUT_STRUCT_ID, 'true' AS EDITABLE ,RANK FROM ")
                        .append("(SELECT  INPUT_STRUCT_ID ,TYPE ,DENSE_RANK() OVER( ORDER BY INPUT_STRUCT_ID ) AS RANK  FROM ")
                        .append(INPUT_STRUCTURE_TABLE_NAME).append(" WHERE upper(INPUT_STRUCT_ID) like upper(:KEY_WORD)");
        if (keyword.indexOf('_') > -1) {
            query1.append(" ESCAPE '\\' )");
        } else {
            query1.append(")");
        }

        query1.append("WHERE TYPE NOT IN (:nonEditableTypes) ")
                .append(" UNION SELECT INPUT_STRUCT_ID, 'false' AS EDITABLE,RANK FROM")
                .append("(SELECT  INPUT_STRUCT_ID ,TYPE ,DENSE_RANK() OVER( ORDER BY INPUT_STRUCT_ID ) AS RANK  FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME).append(" WHERE upper(INPUT_STRUCT_ID) like upper(:KEY_WORD)");
        if (keyword.indexOf('_') > -1) {
            query1.append(" ESCAPE '\\' )");
        } else {
            query1.append(")");
        }
        query1.append("WHERE TYPE  IN (:nonEditableTypes)   )")
                .append(" WHERE RANK BETWEEN :START_IDX AND :END_IDX  ORDER BY INPUT_STRUCT_ID ASC");

        List<Integer> nonEditableTypes = new ArrayList<Integer>();
        nonEditableTypes.add(InputStructureType.EXCEL.getTypeCode());
        nonEditableTypes.add(InputStructureType.TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.GENERIC_INPUT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DB.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_DB.getTypeCode());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("nonEditableTypes", nonEditableTypes);
        if (keyword.indexOf('_') > -1) {
            keyword = keyword.replace("_", "\\_");
        }

        namedParameters.addValue("START_IDX", start).addValue("END_IDX", end).addValue("KEY_WORD", '%' + keyword + '%');

        return (Map<String, Boolean>) namedParameterJdbcTemplate.query(query1.toString(), namedParameters, new EditableInputStructureMapExtractor());
    }

    public Map listAllDataCollectionsWithEditFlag(String keyword) {
        StringBuffer query1
                = new StringBuffer("SELECT INPUT_STRUCT_ID, 'true' AS EDITABLE  FROM ")
                        .append(INPUT_STRUCTURE_TABLE_NAME)
                        .append(" WHERE TYPE NOT IN (:nonEditableTypes)  AND upper(INPUT_STRUCT_ID) like upper(:KEY_WORD)");
        if (keyword.indexOf('_') > -1) {
            query1.append(" ESCAPE '\\' ");
        }
        query1.append(" UNION SELECT INPUT_STRUCT_ID, 'false' AS EDITABLE FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE TYPE  IN (:nonEditableTypes) AND upper(INPUT_STRUCT_ID) like upper(:KEY_WORD) ");
        if (keyword.indexOf('_') > -1) {
            query1.append(" ESCAPE '\\' ");
        }
        query1.append("ORDER BY INPUT_STRUCT_ID ASC");

        List<Integer> nonEditableTypes = new ArrayList<Integer>();
        nonEditableTypes.add(InputStructureType.EXCEL.getTypeCode());
        nonEditableTypes.add(InputStructureType.TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.GENERIC_INPUT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DB.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_TEXT.getTypeCode());
        nonEditableTypes.add(InputStructureType.DIRECT_DB.getTypeCode());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("nonEditableTypes", nonEditableTypes);
        if (keyword.indexOf('_') > -1) {
            keyword = keyword.replace("_", "\\_");
        }
        namedParameters.addValue("KEY_WORD", '%' + keyword + '%');

        return (Map<String, Boolean>) namedParameterJdbcTemplate.query(query1.toString(), namedParameters, new EditableInputStructureMapExtractor());
    }

    public List<String> listDataCollectionsNames(List<Long> types, boolean nodeEnabled) {
        MapSqlParameterSource params = new MapSqlParameterSource("types", types)
                .addValue("NODE_COLUMN_TYPE", NodeColumnType.CONFIGURABLE.getTypeCode())
                .addValue("OLD_DC_TYPES", Utils.getExcludedDcTypes());
        StringBuffer query = new StringBuffer("SELECT INPUT_STRUCT_ID ")
                .append(" FROM ").append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE (TYPE IN (:types) ");
        if (nodeEnabled) {
            query.append(" AND NODE_COLUMN_TYPE = :NODE_COLUMN_TYPE)");
        } else {
            query.append(" AND NODE_COLUMN_TYPE <> :NODE_COLUMN_TYPE)");
        }
        query.append(" OR ( TYPE IN (:OLD_DC_TYPES) )");
        return namedParameterJdbcTemplate.queryForList(query.toString(), params, String.class);
    }

    public List<String> listDataCollectionsNames(long type, boolean nodeEnabled) {
        MapSqlParameterSource params = new MapSqlParameterSource("type", type);

        StringBuffer query = new StringBuffer("SELECT INPUT_STRUCT_ID ")
                .append(" FROM ").append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE TYPE = :type");

        if (type != InputStructureType.DB.getTypeCode()
                && type != InputStructureType.TEXT.getTypeCode()
                && type != InputStructureType.EXCEL.getTypeCode()) {
            params.addValue("NODE_COLUMN_TYPE", NodeColumnType.CONFIGURABLE.getTypeCode());
            if (nodeEnabled) {
                query.append(" AND NODE_COLUMN_TYPE = :NODE_COLUMN_TYPE");
            } else {
                query.append(" AND NODE_COLUMN_TYPE <> :NODE_COLUMN_TYPE");
            }
        }
        query.append(" ORDER BY INPUT_STRUCT_ID ASC ");
        return namedParameterJdbcTemplate.queryForList(query.toString(), params, String.class);
    }

    public VInputStructure getInputStructure(String id) {
        logger.debug("start get InputStructure {} ", id);
        StringBuffer query = new StringBuffer("SELECT * FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME).append(" a,").append(OUTPUT_TABLE_INFO_TABLE_NAME)
                .append(" b WHERE upper(a.INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)")
                .append(" AND upper(a.INPUT_STRUCT_ID) = upper(b.INPUT_STRUCTURE_ID)");

        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", id);
        VInputStructure inputStructure;
        try {
            inputStructure = namedParameterJdbcTemplate.queryForObject(query.toString(), params, new VInputStructureRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        StringBuffer subQuery = new StringBuffer("SELECT * FROM ");
        StringBuffer whereClue = new StringBuffer(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID) ");
        if (isDBType(inputStructure.getType())) {
            subQuery.append(DB_INPUT_STRUCTURE_TABLE_NAME).append(whereClue);
            DBInputStructure dbInputStructure;
            try {
                dbInputStructure = namedParameterJdbcTemplate.queryForObject(subQuery.toString(), params, new DBInputStructureRowMapper());
            } catch (EmptyResultDataAccessException ex) {
                return null;
            }
            dbInputStructure.resetSuper(inputStructure);
            return dbInputStructure;
        } else if (inputStructure.getType() == InputStructureType.EXCEL.getTypeCode()) {
            subQuery.append(EXCEL_INPUT_STRUCTURE_TABLE_NAME).append(whereClue);
            ExcelInputStructure excelInputStructure;
            try {
                excelInputStructure = namedParameterJdbcTemplate.queryForObject(subQuery.toString(), params, new ExcelInputStructureRowMapper());
            } catch (EmptyResultDataAccessException ex) {
                return null;
            }
            excelInputStructure.resetSuper(inputStructure);
            return excelInputStructure;
        } else if (isTextType(inputStructure.getType())) {
            subQuery.append(TEXT_INPUT_STRUCTURE_TABLE_NAME).append(whereClue);
            TextInputStructure textInputStructure;
            try {
                textInputStructure = namedParameterJdbcTemplate.queryForObject(subQuery.toString(), params, new TextInputStructureRowMapper());
            } catch (EmptyResultDataAccessException ex) {
                return null;
            }
            textInputStructure.resetSuper(inputStructure);
            return textInputStructure;
        } else if (inputStructure.getType() == InputStructureType.GENERIC_TEXT.getTypeCode()) {
            subQuery.append(GENERIC_TEXT_INPUT_STRUCTURE_TABLE_NAME).append(whereClue);
            GenericTextInputStructure genericTextInputStructure;
            try {
                genericTextInputStructure = namedParameterJdbcTemplate.queryForObject(subQuery.toString(), params, new GenericTextInputStructureRowMapper());
            } catch (EmptyResultDataAccessException ex) {
                return null;
            }
            genericTextInputStructure.resetSuper(inputStructure);
            return genericTextInputStructure;
        } else if (inputStructure.getType() == InputStructureType.GENERIC_INPUT.getTypeCode()) {
            subQuery.append(GENERIC_TEXT_INPUT_STRUCTURE_TABLE_NAME).append(whereClue);
            GenericInputStructure genericInputStructure;
            try {
                genericInputStructure = namedParameterJdbcTemplate.queryForObject(subQuery.toString(), params, new GenericInputStructureRowMapper());
            } catch (EmptyResultDataAccessException ex) {
                return null;
            }
            genericInputStructure.resetSuper(inputStructure);
            return genericInputStructure;
        } else if (inputStructure.getType() == InputStructureType.GENERIC_XML.getTypeCode()) {
            subQuery.append(GENERIC_XML_INPUT_STRUCTURE_TABLE_NAME).append(whereClue);
            GenericXmlInputStructure genericXmlInputStructure;
            try {
                genericXmlInputStructure = namedParameterJdbcTemplate.queryForObject(subQuery.toString(), params, new GenericXmlInputStructureRowMapper());
            } catch (EmptyResultDataAccessException ex) {
                return null;
            }
            genericXmlInputStructure.resetSuper(inputStructure);
            return genericXmlInputStructure;
        }
        logger.debug("finish get inputStructure {} ", id);
        return inputStructure;
    }

    public List<DataColumn> getInputStructureColumns(String inputStructureId) {
        StringBuffer query = new StringBuffer("SELECT * ")
                .append(" FROM ").append(DATA_COLUMN_TABLE_NAME)
                .append(" WHERE upper(VINPUT_STRUCT_ID) = upper(:VINPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("VINPUT_STRUCT_ID", inputStructureId);
        return namedParameterJdbcTemplate.query(query.toString(), params, new DataColumnRowMapper());

    }

    public List<ExtractionField> getExtractionFields(String inputStructureId) {
        StringBuffer query = new StringBuffer("SELECT * ")
                .append(" FROM ").append(DATA_COLUMN_TABLE_NAME)
                .append(" WHERE upper(VINPUT_STRUCT_ID) = upper(:VINPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("VINPUT_STRUCT_ID", inputStructureId);
        return namedParameterJdbcTemplate.query(query.toString(), params, new ExtractionFieldRowMapper());

    }

    public boolean isInputStructureUsedBySystemOrNode(String inputStructureId) {
        logger.debug("check if inputStructure {} is used by systems or nodes", inputStructureId);
        StringBuffer query = new StringBuffer("SELECT COUNT(INPUT_STRUCT_ID) FROM ").append(INPUT_INPUT_STRUCTURES_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        int used = namedParameterJdbcTemplate.queryForInt(query.toString(), params);
        return (used > 0);
    }

    public void updateInputStructure(VInputStructure inputStructure, String currentUser) {
        logger.debug("start update input structure {}", inputStructure.getId());
        String sqlStatement = inputStructure.getExtractionSql().replace("'", "''");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructure.getId())
                .addValue("NODE_COLUMN_TYPE", inputStructure.getNodeColumnType())
                .addValue("EXCTRACT_DATE", inputStructure.isExtractDate() ? "Y" : "N")
                .addValue("EXCTRACTION_DATE_COLUMN", inputStructure.getExtractionDateColumn())
                .addValue("EXCTRACT_DATE_FORMAT", inputStructure.getDateFormat())
                .addValue("EXTRACTION_SQL", sqlStatement)
                .addValue("TRUNCATE_OLD_DATA", inputStructure.isTruncateBeforeInsertion() ? "Y" : "N")
                .addValue("LAST_MODIFIED_BY", currentUser);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        params.addValue("LAST_MODIFIED_DATE", Calendar.getInstance().getTime());
        StringBuffer queryStr = new StringBuffer();
        queryStr.append("UPDATE ").append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" SET EXTRACTION_SQL = :EXTRACTION_SQL,NODE_COLUMN_TYPE = :NODE_COLUMN_TYPE,")
                .append(" TRUNCATE_OLD_DATA = :TRUNCATE_OLD_DATA ,EXCTRACT_DATE = :EXCTRACT_DATE , EXCTRACT_DATE_FORMAT = :EXCTRACT_DATE_FORMAT ,")
                .append(" LAST_MODIFIED_BY =:LAST_MODIFIED_BY,LAST_MODIFIED_DATE = :LAST_MODIFIED_DATE,")
                .append(" EXCTRACTION_DATE_COLUMN = :EXCTRACTION_DATE_COLUMN WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)");
        namedParameterJdbcTemplate.update(queryStr.toString(), params);

        updateOutputTableInfo(inputStructure);
        if (isDBType(inputStructure.getType())) {
            updateDBInputStructure((DBInputStructure) inputStructure);
        } else if (inputStructure.getType() == InputStructureType.GENERIC_TEXT.getTypeCode()) {
            updateGenericTextInputStructure((GenericTextInputStructure) inputStructure);
        } else if (inputStructure.getType() == InputStructureType.GENERIC_XML.getTypeCode()) {
            updateGenericXmlInputStructure((GenericXmlInputStructure) inputStructure);
        }
        logger.debug(" finish update inputStructure {}", inputStructure.getId());
    }

    public void updateInputStructureColumns(String inputStructureId, List<DataColumn> columns) {
        deleteInputStructureColumns(inputStructureId);
        savInputStructureColumns(inputStructureId, columns);
    }

    public String createTempTable(String tableName, List<DataColumn> columns) {
        logger.debug("create temp table {} ", tableName);
        tempDbTemplate.execute(Utils.getCreateTempTableStatement(tableName, columns, false));

        return tableName;
    }

    public void executeQueryOnTempSchema(String sqlStatement) throws DataAccessException {
        logger.debug("start executeQueryOnTempSchema \n {} ", sqlStatement);
        tempDbTemplate.execute(sqlStatement);
        logger.debug("finish executeQueryOnTempSchema");
    }

    public void executeInsertOnTempSchema(String sqlStatement, Map<String, Object> params) throws DataAccessException {
        logger.debug("start executeInsertOnTempSchema \n {} ", sqlStatement);
        namedParameterTempJdbcTemplate.update(sqlStatement, params);
        logger.debug("finish executeInsertOnTempSchema");
    }

    public List<String> getOutputTablesNames() {
        StringBuffer query = new StringBuffer("SELECT TABLE_NAME FROM ")
                .append(OUTPUT_TABLES_TABLE_NAME)
                .append(" ORDER BY TABLE_NAME");
        return cmtDbTemplate.queryForList(query.toString(), String.class);
    }

    public List<DataColumn> getOutputTableColumns(String tableName) {
        logger.debug("start getOutputTableColumns for table {}", tableName);
        DatabaseMetaData dbMetaData = null;
        ResultSet tableColumnsResult = null;
        Connection connection = null;
        String owner = null;
        List<DataColumn> tableColumnsArr = null;
        try {
            if (isObjectDefinedInSchema(tableName, "TABLE", cmtDbTemplate)) {
                connection = cmtDbTemplate.getDataSource().getConnection();
                dbMetaData = connection.getMetaData();
                logger.debug("table found in cmt schema");
            } else {
                if (isObjectDefinedInSchema(tableName, "TABLE", oldDcTablesTemplate)) {
                    connection = oldDcTablesTemplate.getDataSource().getConnection();
                    dbMetaData = connection.getMetaData();
                    logger.debug("table found in oldDcSchema");
                } else {
                    logger.debug("table [{}] not found !!!", tableName);
                    return null;
                }
            }
            owner = dbMetaData.getUserName();
            tableColumnsResult = dbMetaData.getColumns(null, owner, tableName.toUpperCase(), "%");
            tableColumnsArr = new ArrayList<DataColumn>();
            while (tableColumnsResult.next()) {
                String colName = tableColumnsResult.getString("COLUMN_NAME");
                String colType = Utils.resolveColumnType(tableColumnsResult.getInt("DATA_TYPE"));

                if (!colName.equalsIgnoreCase("ID"))//auto generated primary key column,exclude from mapping
                {
                    DataColumn col = new DataColumn(colName, colType);
                    if (DataColumnType.STRING.getName().equals(colType)) {
                        int size = tableColumnsResult.getInt("COLUMN_SIZE");
                        col.setStrSize(size);
                    }
                    tableColumnsArr.add(col);
                }
            }
        } catch (SQLException e) {
            logger.error("failed to get table columns", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (tableColumnsResult != null) {
                    tableColumnsResult.close();
                }
            } catch (SQLException e) {
                logger.error("failed to close connection or result set", e);
                DataAccessException ex = new DataAccessResourceFailureException("failed to close connection or result set");
                ex.initCause(e);
                throw ex;
            }
        }
        logger.debug("finish getOutputTableColumns for table {}", tableName);
        return tableColumnsArr;
    }

    public boolean addColumnsToOutputTable(String tableName, List<DataColumn> columnsToAdd) {
        logger.debug("start addColumnsToOutputTable {}", tableName);
        if (isObjectDefinedInSchema(tableName, "TABLE", cmtDbTemplate)) {
            cmtDbTemplate.execute(Utils.getAlterTableStatement(tableName, columnsToAdd));
            logger.debug("finish addColumnsToOutputTable {}", tableName);

            for (int i = 0; i < columnsToAdd.size(); i++) {
                if (columnsToAdd.get(i).getKpiType() == 4) {
                    logger.debug("start create index ");
                    cmtDbTemplate.execute(Utils.getCreateIndexStatement(tableName, columnsToAdd.get(i).getName()));
                    logger.debug("Index {} created  ", tableName + "_" + columnsToAdd.get(i).getName() + "_INDEX");
                }
            }

            logger.debug("finish creating index {}", tableName);

        } else {
            if (isObjectDefinedInSchema(tableName, "TABLE", oldDcTablesTemplate)) {
                oldDcTablesTemplate.execute(Utils.getAlterTableStatement(tableName, columnsToAdd));
                logger.debug("finish addColumnsToOutputTable {}", tableName);

                for (int i = 0; i < columnsToAdd.size(); i++) {
                    if (columnsToAdd.get(i).getKpiType() == 4) {
                        logger.debug("start create index ");
                        oldDcTablesTemplate.execute(Utils.getCreateIndexStatement(tableName, columnsToAdd.get(i).getName()));
                        logger.debug("Index {} created  ", tableName + "_" + columnsToAdd.get(i).getName() + "_INDEX");
                    }
                }

                logger.debug("finish creating index {}", tableName);
            } else {
                logger.error("table name {} not found !!!", tableName);
                return false;
            }
        }
        return true;
    }

    public boolean removeColumnsFromOutputTable(String tableName, List<DataColumn> columnsToRemove) {
        logger.debug("start removeColumnsFromOutputTable {}", tableName);
        if (isObjectDefinedInSchema(tableName, "TABLE", cmtDbTemplate)) {
            cmtDbTemplate.execute(Utils.getAlterTableRemoveColumnsStatement(tableName, columnsToRemove));
            logger.debug("finish removeColumnsFromOutputTable {}", tableName);
        } else {
            if (isObjectDefinedInSchema(tableName, "TABLE", oldDcTablesTemplate)) {
                oldDcTablesTemplate.execute(Utils.getAlterTableRemoveColumnsStatement(tableName, columnsToRemove));
                logger.debug("finish addColumnsToOutputTable {}", tableName);
            } else {
                logger.error("table name {} not found !!!", tableName);
                return false;
            }
        }
        return true;
    }

    public void createOutputTable(VInputStructure inputStructure, List<DataColumn> tableColumns, String sequenceName, String triggerName) {
        int stepsDone = 0;
        String tableName = inputStructure.getId();
        try {
            logger.debug("start create output table {} ", tableName);
            cmtDbTemplate.execute(Utils.getCreateOutputTableStatement(inputStructure, tableColumns, true));
            stepsDone++;
            logger.debug("output table {} created", tableName);
            cmtDbTemplate.execute(Utils.getCreateSequenceStatement(sequenceName));
            stepsDone++;
            logger.debug("sequence {} created", sequenceName);
            cmtDbTemplate.execute(Utils.getCreateTableIdTriggerStatement(triggerName, tableName, sequenceName));
            stepsDone++;
            logger.debug("trigger {} created", triggerName);

            for (int i = 0; i < tableColumns.size(); i++) {
                if (tableColumns.get(i).getKpiType() == 4) {
                    logger.debug("start create index ");
                    cmtDbTemplate.execute(Utils.getCreateIndexStatement(tableName, tableColumns.get(i).getName()));
                    logger.debug("Index {} created  ", tableName + "_" + tableColumns.get(i).getName() + "_INDEX");
                }
            }

            logger.debug("finish create output table {} ", tableName);
        } catch (RuntimeException e) {
            if (stepsDone == 2) {//trigger failed
                cmtDbTemplate.execute(Utils.getDropTableStatement(tableName, true));
                cmtDbTemplate.execute(Utils.getDropSequenceStatement(sequenceName));
            } else if (stepsDone == 1)//sequence failed
            {
                cmtDbTemplate.execute(Utils.getDropTableStatement(tableName, true));
            }
            throw e;
        }
    }

    public void addOutputTableName(String name) {
        logger.debug("start addOutputTableName");
        MapSqlParameterSource params = new MapSqlParameterSource("TABLE_NAME", name);
        insertOutputTableName.execute(params);
        logger.debug("finish  addOutputTableName");
    }

    public void deleteInputStructure(String inputStructureId) {
        logger.debug("start deleteInputStructure {}  ", inputStructureId);
        int type = getInputStructureType(inputStructureId);
        logger.debug("input structure type = {}", type);

        if (isDBType(type)) {
            deleteDBInputStructure(inputStructureId);
        } else if (type == InputStructureType.EXCEL.getTypeCode()) {
            deleteExcelInputStructure(inputStructureId);
        } else if (isTextType(type)) {
            deleteTextInputStructure(inputStructureId);
        } else if (type == InputStructureType.GENERIC_TEXT.getTypeCode()) {
            deleteGenericTextInputStructureId(inputStructureId);
        } else if (type == InputStructureType.GENERIC_XML.getTypeCode()) {
            deleteGenericXmlInputStructure(inputStructureId);

        }
        deleteOutputTableInfo(inputStructureId);
        StringBuffer query = new StringBuffer("DELETE FROM ").append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID) ");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("finish deleteInputStructure {}  ", inputStructureId);
    }

    public int getInputStructureType(String inputStructureId) {
        StringBuffer query = new StringBuffer("SELECT TYPE FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(?)");
        try {
            return cmtDbTemplate.queryForInt(query.toString(), new Object[]{inputStructureId});

        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public void deleteInputStructureColumns(String inputStructureId) {
        logger.debug("start delete input structure {} columns ", inputStructureId);
        StringBuffer query = new StringBuffer("DELETE  FROM ").append(DATA_COLUMN_TABLE_NAME)
                .append(" WHERE upper(VINPUT_STRUCT_ID) = upper(:VINPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("VINPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("finish delete input structure {} columns ", inputStructureId);
    }

    public void dropInputStructureTempTable(String tableName) {
        logger.debug("start drop temp table {} ", tableName);
        if (isObjectDefinedInSchema(tableName, "TABLE", tempDbTemplate)) {
            tempDbTemplate.execute(Utils.getDropTableStatement(tableName, true));
            logger.debug(" table {} dropped ", tableName);
        } else {
            logger.debug("temp table {} was not found in cmt temp schema", tableName);
        }
    }

    public void dropInputStructureOutputTable(String tableName, String sequenceName) {
        logger.debug("start drop InputStructure Output table {} ", tableName);
        if (isObjectDefinedInSchema(tableName, "TABLE", cmtDbTemplate)) {
            logger.debug("table found in cmt schema");
            cmtDbTemplate.execute(Utils.getDropTableStatement(tableName, true));
            if (isObjectDefinedInSchema(sequenceName, "SEQUENCE", cmtDbTemplate)) {
                dropSequence(sequenceName, cmtDbTemplate);
            }
            logger.debug("finish drop InputStructure Output Table {}  ", tableName);
        } else if (isObjectDefinedInSchema(tableName, "TABLE", oldDcTablesTemplate)) {
            oldDcTablesTemplate.execute(Utils.getDropTableStatement(tableName, true));
            if (isObjectDefinedInSchema(sequenceName, "SEQUENCE", oldDcTablesTemplate)) {
                dropSequence(sequenceName, oldDcTablesTemplate);
            }
            logger.debug("finish drop InputStructure Output Table {}  ", tableName);
        } else {
            logger.error("table {} WAS NOT FOUND", tableName);
        }
    }

    public void dropSequence(String sequenceName, JdbcTemplate template) {
        if (isObjectDefinedInSchema(sequenceName, "SEQUENCE", template)) {
            template.execute(Utils.getDropSequenceStatement(sequenceName));
        }
    }

    public void deleteTableName(String name) {
        StringBuffer deleteTableNameStat = new StringBuffer("DELETE FROM ")
                .append(OUTPUT_TABLES_TABLE_NAME)
                .append(" WHERE TABLE_NAME = ?");
        cmtDbTemplate.update(deleteTableNameStat.toString(), new Object[]{name});
    }

    private void saveInputStructureCommonAttributes(VInputStructure inputStructure, String currentUser) {
        logger.debug("start save common attributes");
        String sqlStatement = "";
        if (StringUtils.hasLength(inputStructure.getExtractionSql())) {
            sqlStatement = inputStructure.getExtractionSql().replace("'", "''");
        }
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructure.getId())
                .addValue("TYPE", inputStructure.getType())
                .addValue("NODE_COLUMN_TYPE", inputStructure.getNodeColumnType())
                .addValue("EXTRACTION_SQL", sqlStatement)
                .addValue("USE_UPDATE_EVENT", inputStructure.isUseUpdateEvent() ? "Y" : "N")
                .addValue("EXCTRACT_DATE", inputStructure.isExtractDate() ? "Y" : "N")
                .addValue("EXCTRACTION_DATE_COLUMN", inputStructure.getExtractionDateColumn())
                .addValue("EXCTRACT_DATE_MONTHLY", inputStructure.isExtractDateMonthly() ? "Y" : "N")
                .addValue("EXCTRACT_DATE_FORMAT", inputStructure.getDateFormat())
                .addValue("TRUNCATE_OLD_DATA", inputStructure.isTruncateBeforeInsertion() ? "Y" : "N")
                .addValue("PARTITIONE_TABLE", inputStructure.isIsPartitioned() ? "Y" : "N")
                .addValue("LAST_MODIFIED_BY", currentUser);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        params.addValue("LAST_MODIFIED_DATE", c.getTime());
        insertInputStructure.execute(params);
        saveOutputTableInfo(inputStructure);
        logger.debug("finish save common attributes");
    }

    private void updateDBInputStructure(DBInputStructure dbInputStructure) {
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", dbInputStructure.getId()).
                addValue("DB_TYPE", dbInputStructure.getDbType());
        StringBuffer query = new StringBuffer("UPDATE ").append(DB_INPUT_STRUCTURE_TABLE_NAME)
                .append(" SET DB_TYPE = :DB_TYPE ")
                .append(" WHERE INPUT_STRUCT_ID = :INPUT_STRUCT_ID");
        namedParameterJdbcTemplate.update(query.toString(), params);
    }

    private void updateGenericTextInputStructure(GenericTextInputStructure textInputStructure) {
        logger.debug("start update genericTextInputStructure {} ", textInputStructure.getId());
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", textInputStructure.getId()).
                addValue("DELEMITER", textInputStructure.getDelimiter()).
                addValue("USE_HEADERS", textInputStructure.isUseHeaders() ? "Y" : "N").
                addValue("IGNORE_LINES", textInputStructure.getIgnoredLinesCount());
        StringBuffer query = new StringBuffer("UPDATE ").append(GENERIC_TEXT_INPUT_STRUCTURE_TABLE_NAME)
                .append(" SET DELEMITER = :DELEMITER,")
                .append(" USE_HEADERS = :USE_HEADERS, ")
                .append("IGNORE_LINES = :IGNORE_LINES")
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)");
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("finish update genericTextInputStructure {} ", textInputStructure.getId());
    }

    private void updateGenericXmlInputStructure(GenericXmlInputStructure xmlInputStructure) {
        logger.debug("start update genericXmlInputStructure {} ", xmlInputStructure.getId());
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", xmlInputStructure.getId()).
                addValue("CONVERTOR_ID", xmlInputStructure.getConverterId()).
                addValue("IS_SIMPLE", xmlInputStructure.isSimple() ? "Y" : "N");
        StringBuffer query = new StringBuffer("UPDATE ").append(GENERIC_XML_INPUT_STRUCTURE_TABLE_NAME)
                .append(" SET CONVERTOR_ID = :CONVERTOR_ID,")
                .append(" IS_SIMPLE = :IS_SIMPLE ")
                .append(" WHERE INPUT_STRUCT_ID = :INPUT_STRUCT_ID");
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("finish update genericXmlInputStructure {} ", xmlInputStructure.getId());
    }

    private void deleteDBInputStructure(String inputStructureId) {
        StringBuffer query = new StringBuffer("DELETE FROM ").append(DB_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID) ");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("Done delete DB inputstructure {}", inputStructureId);
    }

    private void deleteGenericTextInputStructureId(String inputStructureId) {
        StringBuffer query = new StringBuffer("DELETE FROM ").append(GENERIC_TEXT_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("Done delete Generic text inputstructure {}", inputStructureId);
    }

    private void deleteGenericXmlInputStructure(String inputStructureId) {
        StringBuffer query = new StringBuffer("DELETE FROM ").append(GENERIC_XML_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("Done delete Generic xml inputstructure {}", inputStructureId);
    }

    private void deleteExcelInputStructure(String inputStructureId) {
        StringBuffer query = new StringBuffer("DELETE FROM ").append(EXCEL_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID) ");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("Done delete Excel inputstructure {}", inputStructureId);
    }

    private void deleteTextInputStructure(String inputStructureId) {
        StringBuffer query = new StringBuffer("DELETE FROM ").append(TEXT_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCT_ID) = upper(:INPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructureId);
        namedParameterJdbcTemplate.update(query.toString(), params);
        logger.debug("Done delete Text input structure {}", inputStructureId);
    }

    private boolean isDBType(int type) {
        return (type == InputStructureType.DB.getTypeCode()
                || type == InputStructureType.DIRECT_DB.getTypeCode()
                || type == InputStructureType.GENERIC_DB.getTypeCode());
    }

    private boolean isTextType(int type) {
        return (type == InputStructureType.TEXT.getTypeCode()
                || type == InputStructureType.DIRECT_TEXT.getTypeCode());
    }

    private boolean isObjectDefinedInSchema(String objectName, String objectType, JdbcTemplate template) {
        boolean objectFound = false;
        StringBuffer query = new StringBuffer("SELECT COUNT(OBJECT_ID)")
                .append(" FROM ALL_OBJECTS WHERE ")
                .append(" upper(OBJECT_NAME) = upper(?) ")
                .append(" AND upper(OWNER) = upper(?)");
        boolean addTypeCondition = StringUtils.hasText(objectType);
        if (addTypeCondition) {
            query.append(" AND upper(OBJECT_TYPE) = upper(?)");
        }
        String owner = null;
        Connection connection = null;
        try {
            connection = template.getDataSource().getConnection();
            owner = connection.getMetaData().getUserName();
            int count = 0;
            if (addTypeCondition) {
                count = template.queryForInt(query.toString(),
                        new Object[]{objectName.toUpperCase(), owner.toUpperCase(), objectType.toUpperCase()});
            } else {
                count = template.queryForInt(query.toString(),
                        new Object[]{objectName.toUpperCase(), owner.toUpperCase()});
            }
            objectFound = count > 0;

        } catch (EmptyResultDataAccessException e) {
            logger.error("object not found ", e);
        } catch (SQLException e) {
            logger.error("failed to get metadata", e);
            DataAccessException ex = new DataRetrievalFailureException("failed to get metadata");
            ex.initCause(e);
            throw ex;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("failed to close connection or result set", e);
                DataAccessException ex = new DataAccessResourceFailureException("failed to close connection or result set");
                ex.initCause(e);
                throw ex;
            } finally {
                return objectFound;
            }
        }
    }

    public String getOutputTableName(String inputStructure) {
        StringBuffer query = new StringBuffer("SELECT TABLE_NAME FROM ")
                .append(OUTPUT_TABLE_INFO_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCTURE_ID) = upper(:INPUT_STRUCT_ID)");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCT_ID", inputStructure);
        try {
            return namedParameterJdbcTemplate.queryForObject(query.toString(), params, String.class);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("NO table name found");
            return "";
        }
    }

    public List<String> getDataBaseDataCollections(DataBaseType dataBaseType, boolean nodeEnabled) {
        MapSqlParameterSource params = new MapSqlParameterSource("DB_TYPE", dataBaseType.getTypeCode())
                .addValue("NODE_COLUMN_TYPE", NodeColumnType.CONFIGURABLE.getTypeCode())
                .addValue("OLD_DB_DC_TYPE", InputStructureType.DB.getTypeCode());
        StringBuffer query = new StringBuffer("SELECT INPUT_STRUCT_ID ")
                .append(" FROM ").append(DB_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE DB_TYPE = :DB_TYPE  AND INPUT_STRUCT_ID IN (SELECT INPUT_STRUCT_ID FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE ");
        if (nodeEnabled) {
            query.append(" ( NODE_COLUMN_TYPE = :NODE_COLUMN_TYPE )");
        } else {
            query.append("(NODE_COLUMN_TYPE <> :NODE_COLUMN_TYPE )");
        }
        query.append(" OR (TYPE = :OLD_DB_DC_TYPE) )");
        return namedParameterJdbcTemplate.queryForList(query.toString(), params, String.class);
    }

    public List<String> getDataBaseDataCollections(List<Long> dataBaseTypes, boolean nodeEnabled) {
        MapSqlParameterSource params = new MapSqlParameterSource("DB_TYPES", dataBaseTypes)
                .addValue("NODE_COLUMN_TYPE", NodeColumnType.CONFIGURABLE.getTypeCode())
                .addValue("OLD_DB_DC_TYPE", InputStructureType.DB.getTypeCode());
        StringBuffer query = new StringBuffer("SELECT INPUT_STRUCT_ID ")
                .append(" FROM ").append(DB_INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE DB_TYPE IN (:DB_TYPES)  AND INPUT_STRUCT_ID IN (SELECT INPUT_STRUCT_ID FROM ")
                .append(INPUT_STRUCTURE_TABLE_NAME)
                .append(" WHERE ");
        if (nodeEnabled) {
            query.append(" ( NODE_COLUMN_TYPE = :NODE_COLUMN_TYPE )");
        } else {
            query.append("(NODE_COLUMN_TYPE <> :NODE_COLUMN_TYPE )");
        }
        query.append(" OR (TYPE = :OLD_DB_DC_TYPE) )");
        return namedParameterJdbcTemplate.queryForList(query.toString(), params, String.class);
    }

    public Properties readMapping(String inputStructureId) {
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCTURE_ID", inputStructureId);
        StringBuffer query = new StringBuffer("SELECT * FROM ").append(GENERIC_MAPPING_TABLE_NAME)
                .append(" WHERE INPUT_STRUCTURE_ID = :INPUT_STRUCTURE_ID");

        return namedParameterJdbcTemplate.query(query.toString(), params, new ResultSetExtractor<Properties>() {
            @Override
            public Properties extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                GenericMapping genericMapping = new GenericMapping();
                Properties mappedColumns = new Properties();
                while (resultSet.next()) {
                    mappedColumns.put(resultSet.getString("RESULT_FIELD_IDENTIFIER"), resultSet.getString("OUTPUT_COLUMN"));
                }
                return mappedColumns;
            }
        });
    }

    public boolean isInputStructureNameUsedInSchema(String inputStructureName) {
        if (isObjectDefinedInSchema(inputStructureName, null, cmtDbTemplate)) {
            return true;
        } else {
            return isObjectDefinedInSchema(inputStructureName, null, oldDcTablesTemplate);
        }
    }

    private boolean saveOutputTableInfo(VInputStructure inputStructure) {
        logger.debug("start saveOutputTableInfo {} ", inputStructure.getId());
        MapSqlParameterSource params
                = new MapSqlParameterSource("INPUT_STRUCTURE_ID", inputStructure.getId())
                        .addValue("TABLE_NAME", inputStructure.getMappedTable())
                        .addValue("NODE_COL", inputStructure.getNodeColumn())
                        .addValue("DELETE_DATE_COL", inputStructure.getDateColumn())
                        .addValue("AUTO_FILLED_DATE_COL", inputStructure.getAutoFilledDateColumn())
                        .addValue("PARTITION_COL_NAME", inputStructure.getPartitionColumnName());
        logger.debug("finish saveOutputTableInfo {} ", inputStructure.getId());
        return (insertOutputTableInfo.execute(params) > 0);
    }

    private boolean updateOutputTableInfo(VInputStructure inputStructure) {
        logger.debug("start updateOutputTableInfo {} ", inputStructure.getId());
        StringBuffer query = new StringBuffer("UPDATE ")
                .append(OUTPUT_TABLE_INFO_TABLE_NAME)
                .append(" SET TABLE_NAME = :TABLE_NAME,NODE_COL = :NODE_COL,")
                .append(" DELETE_DATE_COL = :DELETE_DATE_COL ,AUTO_FILLED_DATE_COL = :AUTO_FILLED_DATE_COL ")
                .append(" WHERE upper(INPUT_STRUCTURE_ID) = upper(:INPUT_STRUCTURE_ID)");
        MapSqlParameterSource params
                = new MapSqlParameterSource("INPUT_STRUCTURE_ID", inputStructure.getId())
                        .addValue("TABLE_NAME", inputStructure.getMappedTable())
                        .addValue("NODE_COL", inputStructure.getNodeColumn())
                        .addValue("DELETE_DATE_COL", inputStructure.getDateColumn())
                        .addValue("AUTO_FILLED_DATE_COL", inputStructure.getAutoFilledDateColumn());
        logger.debug("finish updateOutputTableInfo {} ", inputStructure.getId());
        return (namedParameterJdbcTemplate.update(query.toString(), params) > 0);
    }

    private boolean deleteOutputTableInfo(String inputStructureId) {
        logger.debug("start deleteOutputTableInfo {} ", inputStructureId);
        StringBuffer query = new StringBuffer("DELETE FROM ").append(OUTPUT_TABLE_INFO_TABLE_NAME)
                .append(" WHERE upper(INPUT_STRUCTURE_ID) = upper(:INPUT_STRUCTURE_ID) ");
        MapSqlParameterSource params = new MapSqlParameterSource("INPUT_STRUCTURE_ID", inputStructureId);
        logger.debug("finish deleteOutputTableInfo {} ", inputStructureId);
        return (namedParameterJdbcTemplate.update(query.toString(), params) > 0);
    }

    public void saveDataCollectionKpi(String dataCollectionName, String dataCollectionKpiId, int userId) {
        logger.debug("saving DataCollection Kpi");

        StringBuffer query = new StringBuffer(" INSERT INTO CPD_DATA_COLLECTION_KPI (DATA_COLLECTION_NAME , KPI_ID ,  USER_ID ) VALUES ")
                .append(" (?,?,?)");

        Object[] params = new Object[]{dataCollectionName, Integer.parseInt(dataCollectionKpiId), userId};

        int[] types = new int[]{Types.VARCHAR, Types.NUMERIC, Types.NUMERIC};

        insertDataCollectionKpi.update(query.toString(), params, types);
        logger.debug("save DataCollection Kpi done");
    }

    public void deleteDataCollectionKpi(String dataCollectionName) {
        logger.debug("deleting DataCollection Kpi");

        StringBuffer query = new StringBuffer(" DELETE FROM CPD_DATA_COLLECTION_KPI WHERE DATA_COLLECTION_NAME = ? ");

        Object[] params = new Object[]{dataCollectionName};

        int[] types = new int[]{Types.VARCHAR};

        insertDataCollectionKpi.update(query.toString(), params, types);
        logger.debug("delete DataCollection Kpi done");
    }

    public boolean createIndexForColumns(String tableName, List<DataColumn> tableColumns, List<DataColumn> dataCollectionColumns) {

        try {

            logger.debug("start create Indexes For Columns {} ", tableName);

            for (int i = 0; i < dataCollectionColumns.size(); i++) {
                for (int j = 0; j < tableColumns.size(); j++) {
                    if (dataCollectionColumns.get(i).getName() != null
                            && dataCollectionColumns.get(i).getName().trim().length() != 0
                            && dataCollectionColumns.get(i).getName().equalsIgnoreCase(tableColumns.get(j).getName())) {
                        if (dataCollectionColumns.get(i).getKpiType() == 4) {
                            try {
                                logger.debug("start create index ");
                                cmtDbTemplate.execute(Utils.getCreateIndexStatement(tableName, dataCollectionColumns.get(i).getName().toUpperCase()));
                                logger.debug("Index {} created  ", tableName + "_" + dataCollectionColumns.get(i).getName().toUpperCase() + "_INDEX");
                            } catch (Exception e) {
                                logger.debug("Exception Occured ", e.getMessage());
                                logger.error("Exception Occured ", e.getMessage());
                                logger.debug("Exception Occured ", e);
                                logger.error("Exception Occured ", e);
                                if (!e.getMessage().substring(e.getMessage().lastIndexOf(":")).trim().equalsIgnoreCase(": name is already used by an existing object")) {
                                    throw e;
                                }
                            } catch (Throwable th) {
                                logger.debug("Exception Occured ", th);
                                logger.error("Exception Occured ", th);
                                return false;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }

            logger.debug("finish create Indexes For Columns {} ", tableName);
        } catch (Exception e) {
            logger.debug("Exception Occured ", e);
            logger.error("Exception Occured ", e);
            return false;
        }

        return true;
    }

}
