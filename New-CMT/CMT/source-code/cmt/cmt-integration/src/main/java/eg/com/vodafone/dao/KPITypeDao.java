/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.dao;

import eg.com.vodafone.model.KPIType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mahmoud.awad
 */
@Repository
public class KPITypeDao {

    private final static Logger logger = LoggerFactory.getLogger(KPITypeDao.class);

    private final JdbcTemplate simpleJdbcTemplate;

    @Autowired
    public KPITypeDao(@Qualifier(value = "cmtDataSource") DataSource dataSource) {
        simpleJdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<KPIType> listAllKpi() {
        return simpleJdbcTemplate.query("SELECT * FROM CPD_KPI WHERE IS_ACTIVE = 1",
                new RowMapper<KPIType>() {
            @Override
            public KPIType mapRow(ResultSet resultSet, int i) throws SQLException {
                return new KPIType(
                        resultSet.getLong("ID"),
                        resultSet.getString("NAME")
                );
            }
        });
    }

    public List<KPIType> getKpiTypeByDataCollectionName(String dataCollectionName) {

        Object[] params = new Object[]{dataCollectionName};

        int[] types = new int[]{Types.VARCHAR};

        return simpleJdbcTemplate.query("SELECT KPI.ID , KPI.NAME FROM CPD_KPI  KPI  JOIN CPD_DATA_COLLECTION_KPI DATA_KPI ON KPI.ID = DATA_KPI.KPI_ID  WHERE DATA_COLLECTION_NAME = ?", params, types,
                new RowMapper<KPIType>() {
            @Override
            public KPIType mapRow(ResultSet resultSet, int i) throws SQLException {
                return new KPIType(
                        resultSet.getLong("ID"),
                        resultSet.getString("NAME")
                );
            }
        });
    }

}
