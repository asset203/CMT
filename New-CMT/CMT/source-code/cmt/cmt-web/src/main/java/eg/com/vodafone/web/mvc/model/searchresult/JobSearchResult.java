package eg.com.vodafone.web.mvc.model.searchresult;

import java.io.Serializable;

/**
 * @author marwa.goda
 * @since 4/24/13
 */
public class JobSearchResult extends SearchResult implements Serializable {
    private String jobName;
    // Selection Reason
    private String selectionReason;
    // Selection status
    private boolean selected;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSelectionReason() {
        return selectionReason;
    }

    public void setSelectionReason(String selectionReason) {
        this.selectionReason = selectionReason;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
