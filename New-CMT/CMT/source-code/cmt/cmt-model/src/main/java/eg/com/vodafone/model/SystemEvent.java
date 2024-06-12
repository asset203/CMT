package eg.com.vodafone.model;

import eg.com.vodafone.model.enums.EventLevel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/18/13
 * Time: 9:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class SystemEvent implements Serializable {

    private static final long serialVersionUID = 1;

    private int id;
    private Date dateTime;
    @NotNull
    private EventLevel levelType;
    private String systemName;
    @NotEmpty
    @Size(max = 50)
    private String commentDesc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public EventLevel getLevelType() {
        return levelType;
    }

    public void setLevelType(EventLevel levelType) {
        this.levelType = levelType;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }
}
