package eg.com.vodafone.web.mvc.formbean;

import eg.com.vodafone.model.SystemEvent;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/18/13
 * Time: 9:52 AM
 */
public class SystemEventFormBean {

    final private static SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    final private static Logger logger = LoggerFactory.getLogger(SystemEventFormBean.class);

    @Valid
    private SystemEvent selectedSystemEvent;
    private List<SystemEvent> systemEvents;
    private String selectedSystem;
    @NotEmpty
    private String selectedSystemEventDateStr;

    public SystemEvent getSelectedSystemEvent() {
        return selectedSystemEvent;
    }

    public void setSelectedSystemEvent(SystemEvent selectedSystemEvent) {
        this.selectedSystemEvent = selectedSystemEvent;
    }

    public List<SystemEvent> getSystemEvents() {
        return systemEvents;
    }

    public void setSystemEvents(List<SystemEvent> systemEvents) {
        this.systemEvents = systemEvents;
    }

    public String getSelectedSystem() {
        return selectedSystem;
    }

    public void setSelectedSystem(String selectedSystem) {
        this.selectedSystem = selectedSystem;
    }

    public void setSelectedSystemEventDateStr(String selectedSystemEventDateStr) {
        this.selectedSystemEventDateStr = selectedSystemEventDateStr;
        if(this.getSelectedSystemEvent() == null){
            this.setSelectedSystemEvent(new SystemEvent());
        }

        try{
            this.getSelectedSystemEvent().setDateTime(
                    simpleDateFormat.parse(selectedSystemEventDateStr));
        }catch (ParseException ex){
            logger.error("Parse exception occurred", ex.getMessage());
        }
    }

    public String getSelectedSystemEventDateStr() {
        if(this.getSelectedSystemEvent() != null
                && this.getSelectedSystemEvent().getDateTime() != null){
            return simpleDateFormat.format(this.getSelectedSystemEvent().getDateTime());
        }
        return this.selectedSystemEventDateStr;
    }
}
