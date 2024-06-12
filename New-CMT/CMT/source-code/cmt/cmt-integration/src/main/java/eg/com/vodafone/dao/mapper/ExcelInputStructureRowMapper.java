package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.ExcelInputStructure;
import eg.com.vodafone.utils.Utils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/18/13
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelInputStructureRowMapper  implements RowMapper<ExcelInputStructure> {
    @Override
    public ExcelInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        ExcelInputStructure excelInputStructure = new ExcelInputStructure();
        excelInputStructure.setUseSheetInData("Y".equals(resultSet.getString("USE_SHEET")));
        List<String> sheetNames = new ArrayList<String>(Arrays.asList(resultSet.getString("SHEET_NAMES").split(",")));
        excelInputStructure.setSheetNamesList(sheetNames);
        excelInputStructure.setSkip(resultSet.getInt("SKIP"));
        excelInputStructure.setHorizontal("Y".equals(resultSet.getString("HORIZONTAL")));
        excelInputStructure.setParametersMap(Utils.getMapFromString(resultSet.getString("PARAMATERS_MAP")));
        return excelInputStructure;
    }


}
