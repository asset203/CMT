package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.*;
import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.model.enums.InputStructureType;
import eg.com.vodafone.model.enums.NodeColumnType;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.web.mvc.formbean.dataCollection.*;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eg.com.vodafone.utils.Utils.*;

/**
 * Create By: Radwa Osama Date: 4/12/13, 9:41 AM
 */
public class DataCollectionUtil {

    private DataCollectionServiceInterface dataCollectionService;
    private final static String VAR_CHAR_MAX_LENGTH = "4000";
    private final static Set<String> reservedWords = new HashSet<String>(Arrays.asList("ACCESS",
            "ACCOUNT", "ACTIVATE", "ADD", "ADMIN", "ADVISE", "AFTER", "ALL", "ALL_ROWS", "ALLOCATE",
            "ALTER", "ANALYZE", "AND", "ANY", "ARCHIVE", "ARCHIVELOG", "ARRAY", "AS", "ASC", "AT",
            "AUDIT", "AUTHENTICATED", "AUTHORIZATION", "AUTOEXTEND", "AUTOMATIC", "BACKUP", "BECOME",
            "BEFORE", "BEGIN", "BETWEEN", "BFILE", "BITMAP", "BLOB", "BLOCK", "BODY", "BY",
            "CACHE", "CACHE_INSTANCES", "CANCEL", "CASCADE", "CAST", "CFILE", "CHAINED", "CHANGE",
            "CHAR", "CHAR_CS", "CHARACTER", "CHECK", "CHECKPOINT", "CHOOSE", "CHUNK", "CLEAR",
            "CLOB", "CLONE", "CLOSE", "CLOSE_CACHED_OPEN_CURSORS", "CLUSTER", "COALESCE", "COLUMN",
            "COLUMNS", "COMMENT", "COMMIT", "COMMITTED", "COMPATIBILITY", "COMPILE", "COMPLETE",
            "COMPOSITE_LIMIT", "COMPRESS", "COMPUTE", "CONNECT", "CONNECT_TIME", "CONSTRAINT",
            "CONSTRAINTS", "CONTENTS", "CONTINUE", "CONTROLFILE", "CONVERT", "COST", "CPU_PER_CALL",
            "CPU_PER_SESSION", "CREATE", "CURRENT", "CURRENT_SCHEMA", "CURREN_USER", "CURSOR",
            "CYCLE", "DANGLING", "DATABASE", "DATAFILE", "DATAFILES", "DATAOBJNO", "DATE", "DBA",
            "DBHIGH", "DBLOW", "DBMAC", "DEALLOCATE", "DEBUG", "DEC", "DECIMAL", "DECLARE",
            "DEFAULT", "DEFERRABLE", "DEFERRED", "DEGREE", "DELETE", "DEREF", "DESC", "DIRECTORY",
            "DISABLE", "DISCONNECT", "DISMOUNT", "DISTINCT", "DISTRIBUTED", "DML", "DOUBLE", "DROP",
            "DUMP", "EACH", "ELSE", "ENABLE", "END", "ENFORCE", "ENTRY", "ESCAPE", "EXCEPT",
            "EXCEPTIONS", "EXCHANGE", "EXCLUDING", "EXCLUSIVE", "EXECUTE", "EXISTS", "EXPIRE",
            "EXPLAIN", "EXTENT", "EXTENTS", "EXTERNALLY", "FAILED_LOGIN_ATTEMPTS", "FALSE", "FAST",
            "FILE", "FIRST_ROWS", "FLAGGER", "FLOAT", "FLOB", "FLUSH", "FOR", "FORCE", "FOREIGN",
            "FREELIST", "FREELISTS", "FROM", "FULL", "FUNCTION", "GLOBAL", "GLOBALLY", "GLOBAL_NAME",
            "GRANT", "GROUP", "GROUPS", "HASH", "HASHKEYS", "HAVING", "HEADER", "HEAP", "IDENTIFIED",
            "IDGENERATORS", "IDLE_TIME", "IF", "IMMEDIATE", "IN", "INCLUDING", "INCREMENT", "INDEX",
            "INDEXED", "INDEXES", "INDICATOR", "IND_PARTITION", "INITIAL", "INITIALLY", "INITRANS",
            "INSERT", "INSTANCE", "INSTANCES", "INSTEAD", "INT", "INTEGER", "INTERMEDIATE", "INTERSECT",
            "INTO", "IS", "ISOLATION", "ISOLATION_LEVEL", "KEEP", "KEY", "KILL", "LABEL", "LAYER",
            "LESS", "LEVEL", "LIBRARY", "LIKE", "LIMIT", "LINK", "LIST", "LOB", "LOCAL", "LOCK",
            "LOCKED", "LOG", "LOGFILE", "LOGGING", "LOGICAL_READS_PER_CALL", "LOGICAL_READS_PER_SESSION",
            "LONG", "MANAGE", "MASTER", "MAX", "MAXARCHLOGS", "MAXDATAFILES", "MAXEXTENTS", "MAXINSTANCES",
            "MAXLOGFILES", "MAXLOGHISTORY", "MAXLOGMEMBERS", "MAXSIZE", "MAXTRANS", "MAXVALUE", "MIN",
            "MEMBER", "MINIMUM", "MINEXTENTS", "MINUS", "MINVALUE", "MLSLABEL", "MLS_LABEL_FORMAT",
            "MODE", "MODIFY", "MOUNT", "MOVE", "MTS_DISPATCHERS", "MULTISET", "NATIONAL", "NCHAR",
            "NCHAR_CS", "NCLOB", "NEEDED", "NESTED", "NETWORK", "NEW", "NEXT", "NOARCHIVELOG",
            "NOAUDIT", "NOCACHE", "NOCOMPRESS", "NOCYCLE", "NOFORCE", "NOLOGGING", "NOMAXVALUE",
            "NOMINVALUE", "NONE", "NOORDER", "NOOVERRIDE", "NOPARALLEL", "NOPARALLEL", "NOREVERSE",
            "NORMAL", "NOSORT", "NOT", "NOTHING", "NOWAIT", "NULL", "NUMBER", "NUMERIC",
            "NVARCHAR2", "OBJECT", "OBJNO", "OBJNO_REUSE", "OF", "OFF", "OFFLINE", "OID",
            "OIDINDEX", "OLD", "ON", "ONLINE", "ONLY", "OPCODE", "OPEN", "OPTIMAL",
            "OPTIMIZER_GOAL", "OPTION", "OR", "ORDER", "ORGANIZATION", "OSLABEL", "OVERFLOW",
            "OWN", "PACKAGE", "PARALLEL", "PARTITION", "PASSWORD", "PASSWORD_GRACE_TIME",
            "PASSWORD_LIFE_TIME", "PASSWORD_LOCK_TIME", "PASSWORD_REUSE_MAX", "PASSWORD_REUSE_TIME",
            "PASSWORD_VERIFY_FUNCTION", "PCTFREE", "PCTINCREASE", "PCTTHRESHOLD", "PCTUSED",
            "PCTVERSION", "PERCENT", "PERMANENT", "PLAN", "PLSQL_DEBUG", "POST_TRANSACTION",
            "PRECISION", "PRESERVE", "PRIMARY", "PRIOR", "PRIVATE", "PRIVATE_SGA", "PRIVILEGE",
            "PRIVILEGES", "PROCEDURE", "PROFILE", "PUBLIC", "PURGE", "QUEUE", "QUOTA",
            "RANGE", "RAW", "RBA", "READ", "READUP", "REAL", "REBUILD", "RECOVER",
            "RECOVERABLE", "RECOVERY", "REF", "REFERENCES", "REFERENCING", "REFRESH",
            "RENAME", "REPLACE", "RESET", "RESETLOGS", "RESIZE", "RESOURCE", "RESTRICTED",
            "RETURN", "RETURNING", "REUSE", "REVERSE", "REVOKE", "ROLE", "ROLES", "ROLLBACK",
            "ROW", "ROWID", "ROWNUM", "ROWS", "RULE", "SAMPLE", "SAVEPOINT", "SB4",
            "SCAN_INSTANCES", "SCHEMA", "SCN", "SCOPE", "SD_ALL", "SD_INHIBIT", "SD_SHOW",
            "SEGMENT", "SEG_BLOCK", "SEG_FILE", "SELECT", "SEQUENCE", "SERIALIZABLE", "SESSION",
            "SESSION_CACHED_CURSORS", "SESSIONS_PER_USER", "SET", "SHARE", "SHARED", "SHARED_POOL",
            "SHRINK", "SIZE", "SKIP", "SKIP_UNUSABLE_INDEXES", "SMALLINT", "SNAPSHOT", "SOME",
            "SORT", "SPECIFICATION", "SPLIT", "SQL_TRACE", "STANDBY", "START", "STATEMENT_ID",
            "STATISTICS", "STOP", "STORAGE", "STORE", "STRUCTURE", "SUCCESSFUL", "SWITCH",
            "SYS_OP_ENFORCE_NOT_NULL$", "SYS_OP_NTCIMG$", "SYNONYM", "SYSDATE", "SYSDBA", "SYSOPER",
            "SYSTEM", "TABLE", "TABLES", "TABLESPACE", "TABLESPACE_NO", "TABNO", "TEMPORARY", "THAN",
            "THE", "THEN", "THREAD", "TIMESTAMP", "TIME", "TO", "TOPLEVEL", "TRACE", "TRACING",
            "TRANSACTION", "TRANSITIONAL", "TRIGGER", "TRIGGERS", "TRUE", "TRUNCATE", "TX",
            "TYPE", "UB2", "UBA", "UID", "UNARCHIVED", "UNDO", "UNION", "UNIQUE", "UNLIMITED",
            "UNLOCK", "UNRECOVERABLE", "UNTIL", "UNUSABLE", "UNUSED", "UPDATABLE", "UPDATE", "USAGE",
            "USE", "USER", "USING", "VALIDATE", "VALIDATION", "VALUE", "VALUES", "VARCHAR",
            "VARCHAR2", "VARYING", "VIEW", "WHEN", "WHENEVER", "WHERE", "WITH", "WITHOUT", "WORK",
            "WRITE", "WRITEDOWN", "WRITEUP", "XID", "YEAR", "ZONE", "NODE_NAME"));

    public DataCollectionUtil() {

    }

    public DataCollectionUtil(DataCollectionServiceInterface dataCollectionService) {
        this.dataCollectionService = dataCollectionService;
    }

    public VInputStructure createVInputStructure(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        DataCollectionTypeFormBean dataCollectionTypeFormBean = dataCollectionWizardFormBean.getDataCollectionTypeFormBean();

        if (DataCollectionType.TEXT.equals(dataCollectionTypeFormBean.getDataCollectionType())) {
            return createGenericTextInputStructure(dataCollectionWizardFormBean);
        }

        if (DataCollectionType.DB.equals(dataCollectionTypeFormBean.getDataCollectionType())) {
            return createDBInputStructure(dataCollectionWizardFormBean);
        }

        if (DataCollectionType.XML.equals(dataCollectionTypeFormBean.getDataCollectionType())) {
            return createGenericXmlInputStructure(dataCollectionWizardFormBean);
        }

        return null;
    }

    /**
     * @param dataCollectionWizardFormBean web bean
     * @return model bean holding Generic Text Input Structure data
     */
    public GenericTextInputStructure createGenericTextInputStructure(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        ExtractSourceDataFormBean extractSourceDataFormBean = dataCollectionWizardFormBean.getExtractSourceDataFormBean();
        DataCollectionTypeFormBean dataCollectionTypeFormBean = dataCollectionWizardFormBean.getDataCollectionTypeFormBean();
        DefineOutputTableFormBean defineOutputTableFormBean = dataCollectionWizardFormBean.getDefineOutputTableFormBean();
        DefineSQLColumnsFormBean defineSQLColumnsFormBean = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean();

        // Create Text Data Collection
        GenericTextInputStructure textDataCollection = new GenericTextInputStructure();

        // Set id to data collection name
        textDataCollection.setId(dataCollectionTypeFormBean.getDataCollectionName());

        textDataCollection.setUseHeaders(Header.ALWAYS_USE == extractSourceDataFormBean.getHeader());

        if (StringUtils.isNotEmpty(extractSourceDataFormBean.getIgnoreLines())) {
            textDataCollection.setIgnoredLinesCount(
                    Integer.parseInt(extractSourceDataFormBean.getIgnoreLines())
            );
        }

        if (Delimiter.OTHER.equals(extractSourceDataFormBean.getDelimiter())) {

            textDataCollection.setDelimiter(
                    StringUtils.trim(extractSourceDataFormBean.getOtherDelimiter()));

        } else {
            textDataCollection.setDelimiter(
                    extractSourceDataFormBean.getDelimiter().getValue());
        }

        List<SourceColumn> queryColumns
                = dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns();

        List<SourceColumn> expressionColumns
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

        List<SourceColumn> allColumns = new ArrayList<SourceColumn>();

        if (queryColumns != null) {
            allColumns.addAll(queryColumns);
        }

        if (expressionColumns != null) {
            allColumns.addAll(expressionColumns);
        }

        if (allColumns.isEmpty()) {
            throw new BusinessException("Data collection can't have empty columns");
        }

        textDataCollection.setColumnsList(createDataColumn(allColumns));
        textDataCollection.setType(InputStructureType.GENERIC_TEXT.getTypeCode());
        textDataCollection.setExtractionSql(defineSQLColumnsFormBean.getExtractionSql());
        textDataCollection.setTruncateBeforeInsertion(defineOutputTableFormBean.isTruncateBeforeInsertion());
        if (defineOutputTableFormBean.isTruncateBeforeInsertion()) {
            textDataCollection.setDateColumn(defineOutputTableFormBean.getDateColumn());
        } else {
            textDataCollection.setDateColumn(null);
        }

        //NaDa Dahrooug 24/4/2021
        textDataCollection.setIsPartitioned(defineOutputTableFormBean.isIsPartitioned());
        if (defineOutputTableFormBean.isIsPartitioned()) {
            textDataCollection.setPartitionColumnName(defineOutputTableFormBean.getPartitionColumnName());
        } else {
            textDataCollection.setPartitionColumnName(null);
        }

        textDataCollection.setNodeColumnType(NodeColumnType.valueOf(
                defineOutputTableFormBean.getNodeName().name()).getTypeCode());
        if (textDataCollection.getNodeColumnType() == NodeColumnType.CONFIGURABLE.getTypeCode()) {
            textDataCollection.setNodeColumn(NODE_NAME_COLUMN);
        } else {
            textDataCollection.setNodeColumn(null);
        }

        if (OUTPUT_TABLE_OPTION.CREATE_NEW.equals(defineOutputTableFormBean.getOutputTableOption())) {
            textDataCollection.setMappedTable(dataCollectionTypeFormBean.getDataCollectionName());
        } else {
            textDataCollection.setMappedTable(defineOutputTableFormBean.getExistingTable());
        }
        if (dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getUseDateColumn()) {
            textDataCollection.setExtractDate(true);
            textDataCollection.setExtractionDateColumn(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getDateColumnName());
            textDataCollection.setDateFormat(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getDateColumnPrecession().getFormat());
        } else {
            textDataCollection.setExtractDate(false);
            textDataCollection.setExtractionDateColumn(null);
            textDataCollection.setDateFormat(null);
        }

        if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().isAddAutoFilledDateColumn()) {
            textDataCollection.setAutoFilledDateColumn(dataCollectionWizardFormBean.getDefineOutputTableFormBean().getAutoFilledDateColumnName());
        } else {
            textDataCollection.setAutoFilledDateColumn(null);
        }
        return textDataCollection;
    }

    /**
     * @param dataCollectionWizardFormBean web bean
     * @return model bean holding Database Input Structure data
     */
    public DBInputStructure createDBInputStructure(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        DefineDBExtractionSQL defineDBExtractionSQL = dataCollectionWizardFormBean.getDefineDBExtractionSQL();
        DataCollectionTypeFormBean dataCollectionTypeFormBean = dataCollectionWizardFormBean.getDataCollectionTypeFormBean();
        DefineOutputTableFormBean defineOutputTableFormBean = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        // Create DB Input Structure
        DBInputStructure dbInputStructure = new DBInputStructure();
        dbInputStructure.setId(dataCollectionTypeFormBean.getDataCollectionName());
        dbInputStructure.setType(InputStructureType.GENERIC_DB.getTypeCode());

        List<SourceColumn> queryColumns
                = dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns();

        if (queryColumns.isEmpty()) {
            throw new BusinessException("Data collection can't have empty columns");
        }

        dbInputStructure.setColumnsList(createDataColumn(queryColumns));
        dbInputStructure.setExtractionSql(defineDBExtractionSQL.getQuery());
        dbInputStructure.setTruncateBeforeInsertion(defineOutputTableFormBean.isTruncateBeforeInsertion());
        if (defineOutputTableFormBean.isTruncateBeforeInsertion()) {
            dbInputStructure.setDateColumn(defineOutputTableFormBean.getDateColumn());
        } else {
            dbInputStructure.setDateColumn(null);
        }

        //NaDa Dahrooug 24/4/2021
        dbInputStructure.setIsPartitioned(defineOutputTableFormBean.isIsPartitioned());
        if (defineOutputTableFormBean.isIsPartitioned()) {
            dbInputStructure.setPartitionColumnName(defineOutputTableFormBean.getPartitionColumnName());
        } else {
            dbInputStructure.setPartitionColumnName(null);
        }

        dbInputStructure.setDbType(
                DataBaseType.valueOf(defineDBExtractionSQL.getSelectedDBType().name()).getTypeCode());
        dbInputStructure.setNodeColumnType(NodeColumnType.valueOf(
                defineOutputTableFormBean.getNodeName().name()).getTypeCode());
        if (dbInputStructure.getNodeColumnType() == NodeColumnType.CONFIGURABLE.getTypeCode()) {
            dbInputStructure.setNodeColumn(NODE_NAME_COLUMN);
        } else {
            dbInputStructure.setNodeColumn(null);
        }

        if (OUTPUT_TABLE_OPTION.CREATE_NEW.equals(defineOutputTableFormBean.getOutputTableOption())) {
            dbInputStructure.setMappedTable(dataCollectionTypeFormBean.getDataCollectionName());
        } else {
            dbInputStructure.setMappedTable(defineOutputTableFormBean.getExistingTable());
        }
        dbInputStructure.setAutoFilledDateColumn(null);

        return dbInputStructure;
    }

    public GenericXmlInputStructure createGenericXmlInputStructure(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        DataCollectionTypeFormBean dataCollectionTypeFormBean = dataCollectionWizardFormBean.getDataCollectionTypeFormBean();
        DefineOutputTableFormBean defineOutputTableFormBean = dataCollectionWizardFormBean.getDefineOutputTableFormBean();
        DefineSQLColumnsFormBean defineSQLColumnsFormBean = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean();
        ExtractXMLSourceColumns extractXMLSourceColumns = dataCollectionWizardFormBean.getExtractXMLSourceColumns();

        // Create Generic XML data structure
        GenericXmlInputStructure genericXmlInputStructure = new GenericXmlInputStructure();

        if (XMLComplexity.SIMPLE.equals(extractXMLSourceColumns.getXmlComplexity())) {
            genericXmlInputStructure.setSimple(true);
            genericXmlInputStructure.setConverterId(-1);
        } else {
            genericXmlInputStructure.setConverterId(Integer.parseInt(extractXMLSourceColumns.getXmlConverter()));
        }

        List<SourceColumn> queryColumns
                = dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns();

        List<SourceColumn> expressionColumns
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

        List<SourceColumn> allColumns = new ArrayList<SourceColumn>();

        if (queryColumns != null) {
            allColumns.addAll(queryColumns);
        }

        if (expressionColumns != null) {
            allColumns.addAll(expressionColumns);
        }

        if (allColumns.isEmpty()) {
            throw new BusinessException("Data collection can't have empty columns");
        }

        genericXmlInputStructure.setId(dataCollectionTypeFormBean.getDataCollectionName());
        genericXmlInputStructure.setColumnsList(createDataColumn(allColumns));
        genericXmlInputStructure.setType(InputStructureType.GENERIC_XML.getTypeCode());
        genericXmlInputStructure.setExtractionSql(defineSQLColumnsFormBean.getExtractionSql());
        genericXmlInputStructure.setTruncateBeforeInsertion(defineOutputTableFormBean.isTruncateBeforeInsertion());
        if (defineOutputTableFormBean.isTruncateBeforeInsertion()) {
            genericXmlInputStructure.setDateColumn(defineOutputTableFormBean.getDateColumn());
        } else {
            genericXmlInputStructure.setDateColumn(null);
        }

        //NaDa Dahrooug 24/4/2021
        genericXmlInputStructure.setIsPartitioned(defineOutputTableFormBean.isIsPartitioned());
        if (defineOutputTableFormBean.isIsPartitioned()) {
            genericXmlInputStructure.setPartitionColumnName(defineOutputTableFormBean.getPartitionColumnName());
        } else {
            genericXmlInputStructure.setPartitionColumnName(null);
        }
        genericXmlInputStructure.setNodeColumnType(NodeColumnType.valueOf(
                defineOutputTableFormBean.getNodeName().name()).getTypeCode());
        if (genericXmlInputStructure.getNodeColumnType() == NodeColumnType.CONFIGURABLE.getTypeCode()) {
            genericXmlInputStructure.setNodeColumn(NODE_NAME_COLUMN);
        } else {
            genericXmlInputStructure.setNodeColumn(null);
        }

        if (OUTPUT_TABLE_OPTION.CREATE_NEW.equals(defineOutputTableFormBean.getOutputTableOption())) {
            genericXmlInputStructure.setMappedTable(dataCollectionTypeFormBean.getDataCollectionName());
        } else {
            genericXmlInputStructure.setMappedTable(defineOutputTableFormBean.getExistingTable());
        }
        if (dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getUseDateColumn()) {
            genericXmlInputStructure.setExtractDate(true);
            genericXmlInputStructure.setExtractionDateColumn(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getDateColumnName());
            genericXmlInputStructure.setDateFormat(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getDateColumnPrecession().getFormat());
        } else {
            genericXmlInputStructure.setExtractDate(false);
            genericXmlInputStructure.setExtractionDateColumn(null);
            genericXmlInputStructure.setDateFormat(null);
        }
        if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().isAddAutoFilledDateColumn()) {
            genericXmlInputStructure.setAutoFilledDateColumn(dataCollectionWizardFormBean.getDefineOutputTableFormBean().getAutoFilledDateColumnName());
        } else {
            genericXmlInputStructure.setAutoFilledDateColumn(null);
        }

        return genericXmlInputStructure;
    }

    public DataCollectionWizardFormBean createDataCollectionWizardFormBean(String dataCollectionName) {

        if (dataCollectionService == null) {
            throw new BusinessException("No data collection service instance was set.");
        }

        VInputStructure vInputStructure = dataCollectionService.getDataCollection(dataCollectionName);

        if (vInputStructure instanceof GenericTextInputStructure) {
            return createDataCollectionWizardFormBean((GenericTextInputStructure) vInputStructure);
        }

        if (vInputStructure instanceof DBInputStructure) {
            return createDataCollectionWizardFormBean((DBInputStructure) vInputStructure);
        }

        if (vInputStructure instanceof GenericXmlInputStructure) {
            return createDataCollectionWizardFormBean((GenericXmlInputStructure) vInputStructure);
        }

        return new DataCollectionWizardFormBean();
    }

    private DataCollectionWizardFormBean createDataCollectionWizardFormBean(DBInputStructure vInputStructure) {

        DataCollectionWizardFormBean dataCollectionWizardFormBean = new DataCollectionWizardFormBean();

        DefineDBExtractionSQL defineDBExtractionSQL = buildDefineDBExtractionSQL(vInputStructure);
        DataCollectionTypeFormBean dataCollectionType = buildDataCollectionType(vInputStructure, DataCollectionType.DB);
        ExtractSourceColumnFormBean extractSourceColumnFormBean = buildExtractSourceColumnFormBean(vInputStructure);
        DefineSQLColumnsFormBean defineSQLColumnsFormBean = buildDefineSQLColumnsFormBean(vInputStructure);
        DefineOutputTableFormBean defineOutputTableFormBean = buildDefineOutputTableFormBean(vInputStructure);

        dataCollectionWizardFormBean.setDefineDBExtractionSQL(defineDBExtractionSQL);
        dataCollectionWizardFormBean.setDataCollectionTypeFormBean(dataCollectionType);
        dataCollectionWizardFormBean.setExtractSourceColumnFormBean(extractSourceColumnFormBean);
        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(defineSQLColumnsFormBean);
        dataCollectionWizardFormBean.setDefineOutputTableFormBean(defineOutputTableFormBean);

        return dataCollectionWizardFormBean;
    }

    private DataCollectionWizardFormBean createDataCollectionWizardFormBean(GenericXmlInputStructure vInputStructure) {

        DataCollectionWizardFormBean dataCollectionWizardFormBean = new DataCollectionWizardFormBean();

        ExtractXMLSourceColumns extractXMLSourceColumns = buildExtractXMLSourceColumns(vInputStructure);
        DataCollectionTypeFormBean dataCollectionType = buildDataCollectionType(vInputStructure, DataCollectionType.XML);
        ExtractSourceColumnFormBean extractSourceColumnFormBean = buildExtractSourceColumnFormBean(vInputStructure);
        DefineSQLColumnsFormBean defineSQLColumnsFormBean = buildDefineSQLColumnsFormBean(vInputStructure);
        DefineOutputTableFormBean defineOutputTableFormBean = buildDefineOutputTableFormBean(vInputStructure);

        dataCollectionWizardFormBean.setExtractXMLSourceColumns(extractXMLSourceColumns);
        dataCollectionWizardFormBean.setDataCollectionTypeFormBean(dataCollectionType);
        dataCollectionWizardFormBean.setExtractSourceColumnFormBean(extractSourceColumnFormBean);
        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(defineSQLColumnsFormBean);
        dataCollectionWizardFormBean.setDefineOutputTableFormBean(defineOutputTableFormBean);

        return dataCollectionWizardFormBean;
    }

    private DataCollectionWizardFormBean createDataCollectionWizardFormBean(GenericTextInputStructure vInputStructure) {

        DataCollectionWizardFormBean dataCollectionWizardFormBean = new DataCollectionWizardFormBean();

        ExtractSourceDataFormBean extractSourceDataFormBean = buildExtractSourceDataFormBean(vInputStructure);
        DataCollectionTypeFormBean dataCollectionType = buildDataCollectionType(vInputStructure, DataCollectionType.TEXT);
        ExtractSourceColumnFormBean extractSourceColumnFormBean = buildExtractSourceColumnFormBean(vInputStructure);
        DefineSQLColumnsFormBean defineSQLColumnsFormBean = buildDefineSQLColumnsFormBean(vInputStructure);
        DefineOutputTableFormBean defineOutputTableFormBean = buildDefineOutputTableFormBean(vInputStructure);

        dataCollectionWizardFormBean.setExtractSourceDataFormBean(extractSourceDataFormBean);
        dataCollectionWizardFormBean.setDataCollectionTypeFormBean(dataCollectionType);
        dataCollectionWizardFormBean.setExtractSourceColumnFormBean(extractSourceColumnFormBean);
        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(defineSQLColumnsFormBean);
        dataCollectionWizardFormBean.setDefineOutputTableFormBean(defineOutputTableFormBean);

        return dataCollectionWizardFormBean;
    }

    private ExtractSourceDataFormBean buildExtractSourceDataFormBean(GenericTextInputStructure vInputStructure) {

        ExtractSourceDataFormBean extractSourceDataFormBean = new ExtractSourceDataFormBean();

        if (Delimiter.contains(vInputStructure.getDelimiter())) {
            extractSourceDataFormBean.setDelimiter(Delimiter.getDelimiter(vInputStructure.getDelimiter()));
        } else {

            extractSourceDataFormBean.setDelimiter(Delimiter.OTHER);
            extractSourceDataFormBean.setOtherDelimiter(vInputStructure.getDelimiter());
        }

        if (vInputStructure.isUseHeaders()) {
            extractSourceDataFormBean.setHeader(Header.ALWAYS_USE);
        } else {
            extractSourceDataFormBean.setHeader(Header.DONT_USE);
        }

        extractSourceDataFormBean.setIgnoreLines("" + vInputStructure.getIgnoredLinesCount());
        return extractSourceDataFormBean;
    }

    private DataCollectionTypeFormBean buildDataCollectionType(VInputStructure vInputStructure,
            DataCollectionType type) {

        DataCollectionTypeFormBean dataCollectionTypeFormBean = new DataCollectionTypeFormBean();

        dataCollectionTypeFormBean.setDataCollectionType(type);
        dataCollectionTypeFormBean.setDataCollectionName(vInputStructure.getId());

        return dataCollectionTypeFormBean;
    }

    private ExtractSourceColumnFormBean buildExtractSourceColumnFormBean(VInputStructure vInputStructure) {

        ExtractSourceColumnFormBean extractSourceColumnFormBean = new ExtractSourceColumnFormBean();

        List<DataColumn> dataColumnList = vInputStructure.getColumnsList();
        List<SourceColumn> sourceColumnList = new ArrayList<SourceColumn>();

        if (dataColumnList != null) {
            for (DataColumn dataColumn : dataColumnList) {

                if (!isExpressionColumn(dataColumn)) {
                    sourceColumnList.add(createSourceColumn(dataColumn));
                }

            }
        }

        extractSourceColumnFormBean.setColumns(sourceColumnList);
        extractSourceColumnFormBean.setUseDateColumn(vInputStructure.isExtractDate());
        extractSourceColumnFormBean.setDateColumnName(vInputStructure.getExtractionDateColumn());
        extractSourceColumnFormBean.setDateColumnPrecession(getDatePrecession(vInputStructure.getDateFormat()));

        return extractSourceColumnFormBean;
    }

    private DefineSQLColumnsFormBean buildDefineSQLColumnsFormBean(VInputStructure vInputStructure) {

        DefineSQLColumnsFormBean defineSQLColumnsFormBean = new DefineSQLColumnsFormBean();

        List<DataColumn> dataColumnList = vInputStructure.getColumnsList();
        List<SourceColumn> sourceColumnList = new ArrayList<SourceColumn>();

        if (dataColumnList != null) {
            for (DataColumn dataColumn : dataColumnList) {

                if (isExpressionColumn(dataColumn)) {
                    sourceColumnList.add(createSourceColumn(dataColumn));
                }

            }
        }

        defineSQLColumnsFormBean.setSqlExpressionColumns(sourceColumnList);

        String extractionSql = vInputStructure.getExtractionSql();
        //*** added by basma
        String wherePart = extract(extractionSql, getWhereClauseRegex());
        if (vInputStructure.isExtractDate()
                && !StringUtils.isEmpty(wherePart)
                && !StringUtils.isEmpty(vInputStructure.getExtractionDateColumn())
                && !StringUtils.isEmpty(vInputStructure.getDateFormat())) {
            String dateColumnCondition = "";
            if (vInputStructure.getDateFormat().contains("H") || vInputStructure.getDateFormat().contains("h")
                    || vInputStructure.getDateFormat().contains("K") || vInputStructure.getDateFormat().contains("k")) {

                dateColumnCondition = HOURLY_DATE_CONDITION;
            } else {
                dateColumnCondition = DAILY_DATE_CONDITION;

            }
            dateColumnCondition
                    = dateColumnCondition.replace(DATE_COLUMN_NAME_PLACEHOLDER, vInputStructure.getExtractionDateColumn());
            dateColumnCondition = dateColumnCondition.toUpperCase();
            wherePart = wherePart.toUpperCase();
            wherePart = wherePart.replace(dateColumnCondition.trim(), "");
            if (wherePart.indexOf("AND") > 0) {
                wherePart = wherePart.substring(0, wherePart.toUpperCase().lastIndexOf("AND"));
            } else {
                wherePart = "";
            }
        }
        //*** end added by basma
        defineSQLColumnsFormBean.setWhereClause(wherePart);
        defineSQLColumnsFormBean.setHavingClause(extract(extractionSql, getHavingClauseRegex()));
        defineSQLColumnsFormBean.setGroupByClause(extract(extractionSql, getGroupByClauseRegex()));

        return defineSQLColumnsFormBean;
    }

    private DataColumn getDateColumn(VInputStructure vInputStructure) {

        String dateColumnName = vInputStructure.getExtractionDateColumn();
        if (!vInputStructure.isExtractDate() || StringUtils.isEmpty(dateColumnName)) {
            return null;
        }
        for (DataColumn column : vInputStructure.getColumnsList()) {
            if (dateColumnName.trim().equalsIgnoreCase(column.getSrcColumn().trim())) {
                return column;
            }
        }
        return null;
    }

    private DefineOutputTableFormBean buildDefineOutputTableFormBean(VInputStructure vInputStructure) {

        DefineOutputTableFormBean defineOutputTableFormBean = new DefineOutputTableFormBean();

        defineOutputTableFormBean.setExistingTable(vInputStructure.getMappedTable());
        defineOutputTableFormBean.setOutputTableOption(OUTPUT_TABLE_OPTION.USER_EXISTING);

        //Added by Awad
        // CMT DashBoard Configuration
        defineOutputTableFormBean.setExistingTableColumns(
                setKpiColumns(dataCollectionService.getOutputTableColumns(vInputStructure.getMappedTable()), vInputStructure));

        defineOutputTableFormBean.setTruncateBeforeInsertion(vInputStructure.isTruncateBeforeInsertion());
        defineOutputTableFormBean.setDateColumn(vInputStructure.getDateColumn());
          defineOutputTableFormBean.setIsPartitioned(vInputStructure.isIsPartitioned());
        defineOutputTableFormBean.setPartitionColumnName(vInputStructure.getPartitionColumnName());
        defineOutputTableFormBean.setNodeName(NODE_NAME.getNodeName(vInputStructure.getNodeColumnType()));
        defineOutputTableFormBean.setTableSelected(true);
        if (StringUtils.isNotBlank(vInputStructure.getAutoFilledDateColumn())) {
            defineOutputTableFormBean.setAddAutoFilledDateColumn(true);
            defineOutputTableFormBean.setAutoFilledDateColumnName(vInputStructure.getAutoFilledDateColumn());
        }

        return defineOutputTableFormBean;
    }

    private ExtractXMLSourceColumns buildExtractXMLSourceColumns(GenericXmlInputStructure vInputStructure) {

        ExtractXMLSourceColumns extractXMLSourceColumns = new ExtractXMLSourceColumns();

        if (vInputStructure.isSimple()) {
            extractXMLSourceColumns.setXmlComplexity(XMLComplexity.SIMPLE);
        } else {
            extractXMLSourceColumns.setXmlComplexity(XMLComplexity.VENDOR_SPECIFIC);
            extractXMLSourceColumns.setXmlConverter(vInputStructure.getConverterId() + "");
        }

        return extractXMLSourceColumns;
    }

    private DefineDBExtractionSQL buildDefineDBExtractionSQL(DBInputStructure vInputStructure) {

        DefineDBExtractionSQL defineDBExtractionSQL = new DefineDBExtractionSQL();

        List<String> names = new ArrayList<String>();
        for (DataColumn dataColumn : vInputStructure.getColumns()) {
            names.add(dataColumn.getSrcColumn());
        }

        defineDBExtractionSQL.setColumns(StringUtils.join(names, ","));
        defineDBExtractionSQL.setQuery(vInputStructure.getExtractionSql());
        defineDBExtractionSQL.setSelectedDBType(DBType.getDBType(vInputStructure.getDbType()));

        return defineDBExtractionSQL;
    }

    private String getWhereClauseRegex() {
        return ".*?WHERE(.*?)($|GROUP BY(.*?)|HAVING(.*?))";
    }

    private String getGroupByClauseRegex() {
        return ".*?GROUP BY(.*?)($|HAVING.*?)";
    }

    private String getHavingClauseRegex() {
        return ".*?HAVING(.*?)($|GROUP BY.*?)";
    }

    private String extract(String input, String regex) {

        if (StringUtils.isEmpty(input) || StringUtils.isEmpty(regex)) {
            return "";
        }

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(input.toUpperCase());

        if (matcher.matches()) {
            return StringUtils.trim(matcher.group(1));
        }

        return "";
    }

    /**
     * @param dataColumn object holding data column values
     * @return true if this data column is an expression column
     */
    private boolean isExpressionColumn(DataColumn dataColumn) {
        return dataColumn != null && StringUtils.isNotEmpty(dataColumn.getSqlExpression());
    }

    /**
     * @param sourceColumnList list of source columns
     * @return list of data columns
     */
    public List<DataColumn> createDataColumn(List<SourceColumn> sourceColumnList) {

        if (sourceColumnList == null || sourceColumnList.isEmpty()) {
            return new ArrayList<DataColumn>();
        }

        List<DataColumn> dataColumnList = new ArrayList<DataColumn>(sourceColumnList.size());
        int index = 0;
        for (SourceColumn sourceColumn : sourceColumnList) {
            DataColumn dataColumn = createDataColumn(sourceColumn);
            if (dataColumn.getIndex() < 0) {
                dataColumn.setIndex(index);
            }
            dataColumnList.add(dataColumn);
            index++;
        }

        return dataColumnList;
    }

    /**
     * @param sourceColumn bean
     * @return Data Column instance
     */
    public DataColumn createDataColumn(SourceColumn sourceColumn) {

        DataColumn dataColumn = new DataColumn();

        String outputColumnName;
        int kpiType;

        // Added by awad 
        if (sourceColumn.getOutputColumnName() != null && sourceColumn.getOutputColumnName().contains(",")) {
            String[] arr = sourceColumn.getOutputColumnName().split(",");
            outputColumnName = arr[0];
            kpiType = Integer.parseInt(arr[1]);
        } else {
            outputColumnName = sourceColumn.getOutputColumnName();
            kpiType = sourceColumn.getKpiValue();
        }
        // End Awad

        dataColumn.setSrcColumn(StringUtils.trim(sourceColumn.getName()));
        dataColumn.setDefaultValue(StringUtils.trim(sourceColumn.getDefaultValue()));
        dataColumn.setActive(sourceColumn.isSelected());
        dataColumn.setName(outputColumnName);
        dataColumn.setIndex(sourceColumn.getIndex());
        dataColumn.setSqlExpression(sourceColumn.getSqlExpression());

        // awad
        dataColumn.setKpiType(kpiType);
        // end

        if (sourceColumn.getType() != null) {

            Type type = sourceColumn.getType();
            String typeCustomProperty = sourceColumn.getCustomType();

            dataColumn.setTypeCode(DataColumnType.valueOf(type.name()).getTypeCode());

            switch (type) {
                case DATE:
                    dataColumn.setDateFormat(StringUtils.trim(typeCustomProperty));
                    break;
                case STRING:
                    dataColumn.setStrSize(Integer.parseInt(typeCustomProperty));
                    break;
            }
        } else {

            dataColumn.setTypeCode(DataColumnType.UN_DEFINED.getTypeCode());
        }

        return dataColumn;
    }

    public List<SourceColumn> createSourceColumn(List<DataColumn> dataColumnList) {

        List<SourceColumn> sourceColumnList = new ArrayList<SourceColumn>();

        if (dataColumnList != null) {
            for (DataColumn dataColumn : dataColumnList) {
                sourceColumnList.add(createSourceColumn(dataColumn));
            }
        }

        return sourceColumnList;
    }

    public SourceColumn createSourceColumn(DataColumn dataColumn) {

        SourceColumn sourceColumn = new SourceColumn();

        sourceColumn.setName(dataColumn.getSrcColumn());
        sourceColumn.setOutputColumnName(dataColumn.getName());
        sourceColumn.setDefaultValue(dataColumn.getDefaultValue());

        sourceColumn.setSqlExpression(dataColumn.getSqlExpression());
        sourceColumn.setSelected(dataColumn.isActive());
        sourceColumn.setIndex(dataColumn.getIndex());

        // Added by Awad
        // CMT DashBoard Configurations
        sourceColumn.setKpiValue(dataColumn.getKpiType());
        // End

        if (!DataColumnType.UN_DEFINED.equals(DataColumnType.getDataColumnType(dataColumn.getTypeCode()))) {

            sourceColumn.setType(Type.valueOf(DataColumnType.getDataColumnType(dataColumn.getTypeCode()).name()));

            switch (sourceColumn.getType()) {
                case STRING:
                    sourceColumn.setCustomType(dataColumn.getStrSize() + "");
                    break;
                case DATE:
                    sourceColumn.setCustomType(dataColumn.getDateFormat());
                    break;
            }
        }

        return sourceColumn;
    }

    /*private String getSqlExpression(DefineSQLColumnsFormBean defineSQLColumnsFormBean) {

    StringBuilder sqlExpression = new StringBuilder();

    if (StringUtils.isNotEmpty(defineSQLColumnsFormBean.getWhereClause()) &&
      !StringUtils.isWhitespace(defineSQLColumnsFormBean.getWhereClause())) {
      sqlExpression.append("WHERE ").append(defineSQLColumnsFormBean.getWhereClause()).append(" ");
    }

    if (StringUtils.isNotEmpty(defineSQLColumnsFormBean.getGroupByClause()) &&
      !StringUtils.isWhitespace(defineSQLColumnsFormBean.getGroupByClause())) {
      sqlExpression.append("GROUP BY ").append(defineSQLColumnsFormBean.getGroupByClause()).append(" ");
    }

    if (StringUtils.isNotEmpty(defineSQLColumnsFormBean.getHavingClause()) &&
      !StringUtils.isWhitespace(defineSQLColumnsFormBean.getHavingClause())) {
      sqlExpression.append("HAVING ").append(defineSQLColumnsFormBean.getHavingClause()).append(" ");
    }

    return sqlExpression.toString();
  } */
    public List<SourceColumn> getSelectedSourceColumn(List<SourceColumn> columns) {

        List<SourceColumn> selectedColumn = new ArrayList<SourceColumn>();
        for (SourceColumn column : columns) {
            if (column.isSelected()) {
                selectedColumn.add(column);
            }
        }
        return selectedColumn;
    }

    /**
     * Set character size to 4000 Set default Date format
     *
     * @param columns to reset its type parameters
     */
    public void setTypeParametersToDefault(List<SourceColumn> columns) {

        if (columns != null) {
            for (SourceColumn column : columns) {

                switch (column.getType()) {
                    case STRING:
                        column.setCustomType(VAR_CHAR_MAX_LENGTH);
                        break;
                    case DATE:
                        column.setCustomType("dd/mm/yyyy");
                        break;
                }
            }
        }
    }

    public List<SourceColumn> getOutputColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        OUTPUT_TABLE_OPTION outputTableOption = defineOutputTableFormBean.getOutputTableOption();

        if (OUTPUT_TABLE_OPTION.CREATE_NEW.equals(outputTableOption)) {

            List<SourceColumn> outputColumns
                    = getSelectedSourceColumn(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns());

            List<SourceColumn> expressionColumns
                    = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

            setTypeParametersToDefault(expressionColumns);

            if (expressionColumns != null) {
                outputColumns.addAll(expressionColumns);
            }

            return outputColumns;

        } else if (OUTPUT_TABLE_OPTION.USER_EXISTING.equals(outputTableOption)) {

            //*** added by basma
            List<DataColumn> existingTableColumns = new ArrayList<DataColumn>(
                    dataCollectionWizardFormBean.getDefineOutputTableFormBean().getExistingTableColumns());
            if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNodeName() == NODE_NAME.CONFIGURABLE) {
                for (DataColumn column : existingTableColumns) {
                    if (CMTConstants.NODE_NAME_COLUMN.equals(column.getName())) {
                        existingTableColumns.remove(column);
                        break;
                    }
                }
            }
            //*** end added by basma
            List<SourceColumn> outputTableColumns = createSourceColumn(existingTableColumns);

            // Fix having two attributes with variable functionality (name and srcName)
            for (SourceColumn sourceColumn : outputTableColumns) {
                sourceColumn.setName(sourceColumn.getOutputColumnName());
                sourceColumn.setOutputColumnName("");
            }

            List<SourceColumn> newOutputTableColumns
                    = dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNewOutputTableColumns();

            if (newOutputTableColumns != null) {
                outputTableColumns.addAll(newOutputTableColumns);
            }

            //exclude the auto filled date column , so it will not appear in mapping page
            if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().isAddAutoFilledDateColumn()
                    && StringUtils.isNotBlank(defineOutputTableFormBean.getAutoFilledDateColumnName())) {
                String autoFilledColumn = defineOutputTableFormBean.getAutoFilledDateColumnName().trim();

                for (SourceColumn col : outputTableColumns) {
                    if (autoFilledColumn.equalsIgnoreCase(col.getName())) {
                        outputTableColumns.remove(col);
                        break;
                    }
                }
            }
            return outputTableColumns;
        }

        return new ArrayList<SourceColumn>();
    }

    /**
     * @param dataCollectionWizardFormBean wizard bean
     * @return In case of creating new table return all the new table columns,
     * In case of altering an old table return only the new columns
     */
    public List<SourceColumn> getNewOutputColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        OUTPUT_TABLE_OPTION outputTableOption = defineOutputTableFormBean.getOutputTableOption();

        if (OUTPUT_TABLE_OPTION.CREATE_NEW.equals(outputTableOption)) {

            List<SourceColumn> selectedSourceColumns
                    = getSelectedSourceColumn(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns());
            if (NODE_NAME.MAPPED.equals(dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNodeName())) {
                selectedSourceColumns.add(createNodeNameColumn());
            }
            List<SourceColumn> expressionColumns
                    = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

            setTypeParametersToDefault(expressionColumns);

            if (expressionColumns != null) {
                selectedSourceColumns.addAll(expressionColumns);
            }

            List<SourceColumn> outputColumns = new ArrayList<SourceColumn>();
            for (SourceColumn c : selectedSourceColumns) {
                SourceColumn copy = new SourceColumn();
                copy.setOutputColumnName(c.getName());
                copy.setType(c.getType());
                copy.setCustomType(c.getCustomType());
                copy.setDefaultValue(c.getDefaultValue());
                //awad
                copy.setKpiValue(Integer.parseInt(c.getOutputColumnName().split(",")[1]));
                //end
                outputColumns.add(copy);
            }

            return outputColumns;

        } else if (OUTPUT_TABLE_OPTION.USER_EXISTING.equals(outputTableOption)) {

            List<SourceColumn> outputColumns
                    = dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNewOutputTableColumns();

            if (outputColumns == null) {
                outputColumns = new ArrayList<SourceColumn>();
            }

            setTypeParametersToDefault(outputColumns);

            if (NODE_NAME.MAPPED.equals(dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNodeName())) {

                boolean hasNodeName = false;
                for (DataColumn dataColumn : defineOutputTableFormBean.getExistingTableColumns()) {
                    if ("NODE_NAME".equals(dataColumn.getName())) {
                        hasNodeName = true;
                        break;
                    }
                }

                if (!hasNodeName) {
                    outputColumns.add(dataCollectionWizardFormBean.getDefineDataCollectionMappingFormBean().getNodeNameColumn());
                }
            }

            return outputColumns;
        }

        return new ArrayList<SourceColumn>();
    }

    /**
     * @return Node Name Column with default configuration
     */
    public SourceColumn createNodeNameColumn() {
        // String NODE_NAME_COLUMN = "NODE_NAME";
        String NODE_NAME_COLUMN_SIZE = "1024";

        SourceColumn nodeSourceColumn = new SourceColumn();
        nodeSourceColumn.setName(NODE_NAME_COLUMN);
        nodeSourceColumn.setCustomType(NODE_NAME_COLUMN_SIZE);
        nodeSourceColumn.setType(Type.STRING);
        return nodeSourceColumn;
    }

    private List<SourceColumn> createSourceColumnsFromDefineDBExtractionSQL(DefineDBExtractionSQL defineDBExtractionSQL) {

        String columnsString = defineDBExtractionSQL.getColumns();

        List<String> columns = Arrays.asList(columnsString.split(","));

        List<SourceColumn> sourceColumns = new ArrayList<SourceColumn>();
        int index = 0;
        for (String column : columns) {
            SourceColumn sc = new SourceColumn();
            sc.setName(StringUtils.trim(column));
            sc.setSelected(true);
            sc.setCustomType(VAR_CHAR_MAX_LENGTH);
            sc.setType(Type.STRING);
            sc.setIndex(index);
            sourceColumns.add(sc);
            index++;
        }

        return sourceColumns;
    }

    public List<SourceColumn> getSourceColumns(DefineDBExtractionSQL defineDBExtractionSQL, ExtractSourceColumnFormBean extractSourceColumnFormBean) {

        if (extractSourceColumnFormBean == null) {
            return createSourceColumnsFromDefineDBExtractionSQL(defineDBExtractionSQL);
        }
        List<SourceColumn> finalColumnsList = new ArrayList<SourceColumn>();
        List<SourceColumn> previouslyDefinedColumns = extractSourceColumnFormBean.getColumns();
        List<String> columnsNames = Arrays.asList(defineDBExtractionSQL.getColumns().split(","));
        int index = 0;
        for (String columnName : columnsNames) {
            SourceColumn column = findInList(columnName, previouslyDefinedColumns);
            if (column != null) {
                column.setIndex(index++);
                finalColumnsList.add(column);
                continue;
            }
            column = new SourceColumn();
            column.setName(StringUtils.trim(columnName));
            column.setSelected(true);
            column.setCustomType(VAR_CHAR_MAX_LENGTH);
            column.setType(Type.STRING);
            column.setIndex(index++);
            finalColumnsList.add(column);
        }
        return finalColumnsList;
    }

    private SourceColumn findInList(String targetColumnName, List<SourceColumn> columnsList) {
        if ((columnsList != null && !columnsList.isEmpty())
                && StringUtils.isNotEmpty(targetColumnName)) {
            for (SourceColumn aColumn : columnsList) {
                if (aColumn.getName().equals(targetColumnName)) {
                    return aColumn;
                }
            }
        }
        return null;
    }

    public List<SourceColumn> createSourceColumnsForExtractXMLSourceColumns(ExtractXMLSourceColumns extractXMLSourceColumns) {

        String converter = extractXMLSourceColumns.getXmlConverter();

        if (StringUtils.isEmpty(converter) || StringUtils.isWhitespace(converter) || !StringUtils.isNumeric(converter)) {

            throw new BusinessException("Invalid converter id");
        }

        List<XmlConverterElement> xmlConverterElements
                = dataCollectionService.listXmlConverterElementsByConverterId(Long.parseLong(converter));

        List<SourceColumn> sourceColumns = new ArrayList<SourceColumn>();
        int index = 0;
        for (XmlConverterElement xmlConverterElement : xmlConverterElements) {
            SourceColumn sc = new SourceColumn();
            sc.setName(xmlConverterElement.getName());
            sc.setType(Type.valueOf(DataColumnType.getDataColumnType(xmlConverterElement.getTypeCode()).name()));
            sc.setSelected(true);
            sc.setIndex(index);
            sourceColumns.add(sc);
            index++;
        }

        setTypeParametersToDefault(sourceColumns);

        return sourceColumns;
    }

    public boolean isReservedWord(String word) {
        if (StringUtils.isNotEmpty(word) && !StringUtils.isWhitespace(word)) {
            return reservedWords.contains(word.toUpperCase());
        }

        return false;
    }

    private DateColumnPrecession getDatePrecession(String dateFormat) {
        if (StringUtils.isEmpty(dateFormat)) {
            return DateColumnPrecession.NONE;
        }
        if (dateFormat.contains("h") || dateFormat.contains("H")) {
            return DateColumnPrecession.HOURS;
        }
        return DateColumnPrecession.DAYS;
    }

    public List<SourceColumn> getSelectedPlainAndSqlColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        List<SourceColumn> allSelectedColumns = new ArrayList<SourceColumn>();
        allSelectedColumns.addAll(getSelectedSourceColumn(dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns()));

        List<SourceColumn> expressionColumns
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

        if (expressionColumns != null) {
            allSelectedColumns.addAll(expressionColumns);
        }
        return allSelectedColumns;
    }

    //Added by Awad
    //CMT DashBoard Configuration
    private List<DataColumn> setKpiColumns(List<DataColumn> tableColumns, VInputStructure vInputStructure) {

        List<DataColumn> columnsList = vInputStructure.getColumnsList();

        if (tableColumns != null && !tableColumns.isEmpty()) {

            for (int i = 0; i < tableColumns.size(); i++) {
                for (int j = 0; j < columnsList.size(); j++) {
                    if (tableColumns.get(i).getName().equalsIgnoreCase(columnsList.get(j).getName())) {
                        tableColumns.get(i).setKpiType(columnsList.get(j).getKpiType());
                    }
                }

            }
        }
        return tableColumns;
    }

    // End Awad
}
