package eg.com.vodafone.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/27/13
 * Time: 3:04 PM
 */
public class KPINotificationList implements Serializable {
    private static final long serialVersionUID = 1;
    private VNode node;
    private List<KPIContact> contactList;

    public KPINotificationList() {
    }

    public KPINotificationList(VNode node, List<KPIContact> contactList) {
        this.node = node;
        this.contactList = contactList;
    }

    public VNode getNode() {
        return node;
    }

    public void setNode(VNode node) {
        this.node = node;
    }

    public List<KPIContact> getContactList() {
        return contactList;
    }

    public void setContactList(List<KPIContact> contactList) {
        this.contactList = contactList;
    }
}
