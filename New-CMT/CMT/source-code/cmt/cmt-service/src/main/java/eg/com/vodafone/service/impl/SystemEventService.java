package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.SystemEventDao;
import eg.com.vodafone.model.SystemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/18/13
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class SystemEventService {
    @Autowired
    private SystemEventDao systemEventDao;

    @Transactional(readOnly = false)
    public int saveEvent(SystemEvent systemEvent){
        return systemEventDao.saveEvent(systemEvent);
    }

    @Transactional(readOnly = false)
    public int updateEvent(SystemEvent updatedSystemEvent){
        return systemEventDao.updateEvent(updatedSystemEvent);
    }

    @Transactional(readOnly = false)
    public int deleteEvent(int systemEventID){
        return systemEventDao.deleteEvent(systemEventID);
    }

    public List<SystemEvent> getSystemEvents(String systemName){
         return systemEventDao.findSystemEvents(systemName);
    }

    public SystemEvent getSystemEvent(String systemName, Date eventDate){
        return systemEventDao.findSystemEvent(systemName, eventDate);
    }
  
    public boolean isSystemExist(String systemName){
      return systemEventDao.isSystemExist(systemName);
    }
}
