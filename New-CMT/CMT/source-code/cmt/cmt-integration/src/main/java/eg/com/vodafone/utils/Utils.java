package eg.com.vodafone.utils;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.model.enums.InputStructureType;
import org.springframework.util.StringUtils;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.sql.Types;
/**
 * Created with IntelliJ IDEA. User: basma.alkerm Date: 3/24/13 Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public final static String TEMP_TABLE_NAME_PLACEHOLDER = "$TEMP_TABLE_NAME";
    public static final String NODE_NAME_COLUMN = "NODE_NAME";
    public static final String DATE_COLUMN_NAME_PLACEHOLDER = "$dateColumnName";
    public static final String TARGET_DATE_STRING_PLACEHOLDER = "$targetDateString";
    public static final String FROM_KEY_WORD = "FROM";
    public static final String JAVA_DATE_FORMAT = "dd/MM/yyyy";
    public static final String JAVA_DATE_TIME_FORMAT = "dd/MM/yyyy HH";
    public static final String DAILY_DATE_CONDITION = " $dateColumnName >= TO_DATE('$targetDateString','dd/MM/yyyy')"
            + " AND $dateColumnName < (TO_DATE('$targetDateString','dd/MM/yyyy')+1)";
    public static final String HOURLY_DATE_CONDITION = "$dateColumnName = TO_DATE('$targetDateString','dd/MM/yyyy HH24')";
    private static final String JAVA_COMPLETE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ORACLE_COMPLETE_DATE_FORMAT = "YYYY-MM-DD HH24:MI:SS";
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static Map<String, String> getMapFromString(String str) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (!StringUtils.hasText(str)) {
            return map;
        }
        String[] arr = str.split(":");
        for (int i = 0; i < arr.length; i = i + 2) {
            map.put(arr[i], arr[i + 1]);
        }
        return map;

    }

    public static synchronized String resolveColumnType(int type) {

        /**
         * Updated by Alia.Adel on 2013.06.24 to set undefined types to String
         * as part of issue#396829 fix
         */
        if (type == Types.CHAR || type == Types.LONGVARCHAR
                || type == Types.VARCHAR || type == Types.NCHAR
                || type == Types.NVARCHAR || type == Types.LONGNVARCHAR
                || type == Types.OTHER) {
            return DataColumnType.STRING.getName();
        } else if (type == Types.DATE || type == Types.TIMESTAMP
                || type == Types.TIME) {
            return DataColumnType.DATE.getName();
        } else if (type == Types.INTEGER || type == Types.TINYINT
                || type == Types.BIGINT || type == Types.SMALLINT
                || type == Types.DECIMAL || type == Types.DOUBLE) {
            return DataColumnType.NUMBER.getName();
        } else if (type == Types.REAL || type == Types.FLOAT
                || type == Types.NUMERIC) {
            return DataColumnType.FLOAT.getName();
        } else {
            return "";  //TODO: defualt String ? ,throw business exception?
        }
    }

    public static String getCreateTempTableStatement(String tableName, List<DataColumn> tableColumns, boolean addIDColumn) {

        StringBuffer tableCreateSql = new StringBuffer("CREATE TABLE ").append(tableName).append(" (");
        if (addIDColumn) {
            tableCreateSql.append("ID NUMBER PRIMARY KEY ,");
        }
        tableCreateSql.append(getColumnPartForTempTable(tableColumns))
                .append(" )");
        return tableCreateSql.toString();
    }

    public static String getCreateOutputTableStatement(VInputStructure inputStructure, List<DataColumn> tableColumns, boolean addIDColumn) {

        String tableName = inputStructure.getId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new Date());
        StringBuffer tableCreateSql = new StringBuffer("CREATE TABLE ").append(tableName).append(" (");
        if (addIDColumn) {
            tableCreateSql.append("ID NUMBER PRIMARY KEY ,");
        }
        tableCreateSql.append(getColumnsPartSqlUsingName(tableColumns))
                .append(" )");

        if (inputStructure.isIsPartitioned()) {
            logger.debug("Table " + tableName + " is Partitioned with column name: " + inputStructure.getPartitionColumnName());
            tableCreateSql.append("PARTITION BY RANGE (").append(inputStructure.getPartitionColumnName().toUpperCase()).append(")");
            tableCreateSql.append("INTERVAL (NUMTODSINTERVAL(1,'DAY'))");
            tableCreateSql.append("(PARTITION p1 VALUES LESS THAN (TO_DATE('").append(currentDate).append("','yyyy-MM-dd')))");
        }
        return tableCreateSql.toString();
    }

    public static String getAlterTableStatement(String tableName, List<DataColumn> columnsToAdd) {
        StringBuffer alterTableSql = new StringBuffer("ALTER Table ").append(tableName)
                .append(" ADD (");
        alterTableSql.append(getColumnsPartSqlUsingName(columnsToAdd));
        alterTableSql.append(" )");
        return alterTableSql.toString();
    }

    public static String getAlterTableRemoveColumnsStatement(String tableName, List<DataColumn> columnsToRemove) {
        StringBuffer alterTableSql = new StringBuffer("ALTER Table ").append(tableName)
                .append(" DROP (");
        for (DataColumn column : columnsToRemove) {
            alterTableSql.append(column.getName()).append(',');
        }
        alterTableSql.deleteCharAt(alterTableSql.length() - 1);
        alterTableSql.append(" )");
        return alterTableSql.toString();
    }

    private static String getColumnPartForTempTable(List<DataColumn> columns) {
        StringBuffer columnsPart = new StringBuffer();
        DataColumn column = null;
        for (int i = 0; i < columns.size(); i++) {
            column = columns.get(i);
            if (StringUtils.hasText(column.getSqlExpression())) {
                continue;
            }
            columnsPart.append(column.getSrcColumn()).append(" ")
                    .append(getColumnSqlType(column));
            if (StringUtils.hasText(column.getDefaultValue())) {
                columnsPart.append(" DEFAULT ");
                if (column.getTypeCode() == DataColumnType.STRING.getTypeCode()) {
                    columnsPart.append('\'').append(column.getDefaultValue().replace("\'", "\'\'")).append('\'');
                } else if (column.getTypeCode() == DataColumnType.DATE.getTypeCode()) {
                    columnsPart.append(" to_date(\'").append(convertToFormat(column.getDefaultValue(), column.getDateFormat(), JAVA_COMPLETE_DATE_FORMAT))
                            .append("\',\'").append(ORACLE_COMPLETE_DATE_FORMAT).append("\') ");
                } else {
                    columnsPart.append(column.getDefaultValue());
                }
            }
            columnsPart.append(" ,");
        }
        String columnsPartStr = "";
        if (columnsPart.lastIndexOf(",") != -1) {
            columnsPartStr = columnsPart.substring(0, columnsPart.lastIndexOf(","));
        }

        return columnsPartStr;
    }

    private static String getColumnsPartSqlUsingName(List<DataColumn> columns) {
        StringBuffer columnsPart = new StringBuffer();
        DataColumn column = null;
        for (int i = 0; i < columns.size(); i++) {
            column = (DataColumn) columns.get(i);
            columnsPart.append(column.getName()).append(" ")
                    .append(getColumnSqlType(column));
            if (StringUtils.hasText(column.getDefaultValue())) {
                columnsPart.append(" DEFAULT ");
                if (column.getTypeCode() == DataColumnType.STRING.getTypeCode()) {
                    columnsPart.append('\'').append(column.getDefaultValue().replace("\'", "\'\'")).append('\'');
                } else if (column.getTypeCode() == DataColumnType.DATE.getTypeCode()) {
                    columnsPart.append(" to_date(\'").append(convertToFormat(column.getDefaultValue(), column.getDateFormat(), JAVA_COMPLETE_DATE_FORMAT))
                            .append("\',\'").append(ORACLE_COMPLETE_DATE_FORMAT).append("\') ");
                } else {
                    columnsPart.append(column.getDefaultValue());
                }
            }
            columnsPart.append(" ,");
        }
        String columnsPartStr = columnsPart.substring(0, columnsPart.lastIndexOf(","));
        return columnsPartStr;
    }

    private static synchronized String getColumnSqlType(DataColumn column) {
        if (DataColumnType.STRING.getTypeCode() == column.getTypeCode()) {
            return "VARCHAR(" + column.getStrSize() + "char)";
        }
        if (DataColumnType.NUMBER.getTypeCode() == column.getTypeCode()) {
            return "NUMBER";
        }
        if (DataColumnType.FLOAT.getTypeCode() == column.getTypeCode()) {
            return "FLOAT";
        }
        if (DataColumnType.DATE.getTypeCode() == column.getTypeCode()) {
            return "DATE";
        }
        return "VARCHAR(1024 char)";
    }

    public static String getCreateTableIdTriggerStatement(String triggerName, String tableName, String sequenceName) {
        StringBuffer createTriggerSql = new StringBuffer("CREATE TRIGGER ").append(triggerName)
                .append(" BEFORE INSERT ON ").append(tableName)
                .append(" FOR EACH ROW WHEN (NEW.ID IS NULL) ")
                .append("BEGIN SELECT ").append(sequenceName).append(".NEXTVAL INTO :NEW.ID FROM DUAL; END;");
        return createTriggerSql.toString();
    }

    public static String getCreateSequenceStatement(String sequenceName) {
        StringBuffer createSequenceSql = new StringBuffer("CREATE SEQUENCE ").append(sequenceName);
        return createSequenceSql.toString();
    }

    public static String getDropTableStatement(String tableName, boolean cascad) {
        String stat = "DROP TABLE " + tableName;
        if (cascad) {
            stat = stat + " CASCADE CONSTRAINTS";
        }
        return stat;
    }

    public static String getDropSequenceStatement(String sequenceName) {
        return "DROP SEQUENCE " + sequenceName;
    }

    public static java.sql.Timestamp getTimeStampObject(String format, String dateString) {
        Date value = getDateObject(format, dateString);
        if (value == null) {
            return null;
        }
        return new java.sql.Timestamp(value.getTime());
    }

    public static Date getDateObject(String format, String dateString) {
        Date value = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            value = sdf.parse(dateString.trim());
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    public static String convertToFormat(String dateString, String sourceFormat, String targetFormat) {
        Date date = getDateObject(sourceFormat, dateString);
        SimpleDateFormat targetFormatter = new SimpleDateFormat(targetFormat);
        return targetFormatter.format(date);
    }

    /**
     * get the old dc types (hibernate-based Dc's)
     *
     * @return list of input_structure_types ids
     */
    public static List<Integer> getExcludedDcTypes() {
        List<Integer> types = new ArrayList<Integer>();
        types.add(InputStructureType.DB.getTypeCode());
        types.add(InputStructureType.TEXT.getTypeCode());
        types.add(InputStructureType.EXCEL.getTypeCode());
        return types;
    }

    public static String getCreateIndexStatement(String tableName, String columnName) {

        String indexName = tableName + "_" + columnName + "_INDEX";

        if (indexName.length() >= 30) {
            indexName = indexName.substring(0, 22) + "_INDEX";
        }

        StringBuffer createIndexeSql = new StringBuffer(" CREATE INDEX ").append(indexName)
                .append(" ON ").append(tableName).
                append(" ( ").append(columnName).append(" )");
        return createIndexeSql.toString();
    }

}
