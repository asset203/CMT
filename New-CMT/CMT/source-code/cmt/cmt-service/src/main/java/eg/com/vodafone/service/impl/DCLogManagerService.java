package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.DCLogEntryDao;
import eg.com.vodafone.dao.SystemDao;
import eg.com.vodafone.model.DCLogEntry;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.enums.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/14/13
 * Time: 1:04 PM
 */
@Service
@Transactional(readOnly = true)
public class DCLogManagerService{

    private final static Logger logger = LoggerFactory.getLogger(DCLogManagerService.class);

    @Autowired
    private DCLogEntryDao dcLogEntryDao;


    @Autowired
    private SystemDao systemDao;

    @Transactional(readOnly = true)
    public List<String> getAllSystems(){
       return systemDao.getAllSystems();
    }

    @Transactional(readOnly = true)
    public int getFilteredDCLogsCount(String systemName, String logType, Date fromDate, Date toDate){

        if(!StringUtils.isEmpty(systemName) && systemName.equals("All")){
            systemName = "%";
        }

        if(!StringUtils.isEmpty(logType) && logType.equals(LogType.ALL.getDescription())){
            logType = "%";
        }

        int count = dcLogEntryDao.getResultSetCount(systemName, logType, fromDate, toDate);
        logger.debug("returned result count is: {}", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<DCLogEntry> getFilteredDCLogsByPageIndex(
            String systemName, String logType, Date fromDate, Date toDate,
            String sortField, String sortType, int startIndex, int endIndex){

        logger.debug("Parameters passed:\nSystem name:{}\nLogType:{}\nFrom Date:{}\nTo Date:{}\nSort Field:{}" +
                "\nSort Type:{}\nStart Index:{}\nEnd Index:{}",
                new Object[]{systemName, logType, fromDate, toDate, sortField, sortType, startIndex, endIndex});


        if(!StringUtils.isEmpty(systemName) && systemName.equals("All")){
            systemName = "%";
        }

        if(!StringUtils.isEmpty(logType) && logType.equals(LogType.ALL.getDescription())){
            logType = "%";
        }

        return dcLogEntryDao.getFilteredResultsByPageIndex(systemName, logType , fromDate, toDate,
                sortField, sortType, startIndex, endIndex);
    }


}
