package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.SystemEvent;
import eg.com.vodafone.model.enums.EventLevel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/19/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemEventRowMapper implements RowMapper<SystemEvent> {
    @Override
    public SystemEvent mapRow(ResultSet rs, int line) throws SQLException {
        SystemEvent systemEvent = new SystemEvent();
        systemEvent.setId(rs.getInt("ID"));
        systemEvent.setDateTime(rs.getDate("DATE_TIME"));
        systemEvent.setLevelType(EventLevel.valueOf(rs.getString("LEVEL_TYPE").toUpperCase()));
        systemEvent.setSystemName(rs.getString("SYSTEM_NAME"));
        systemEvent.setCommentDesc(rs.getString("COMMENT_DESC"));
        return systemEvent;
    }
}