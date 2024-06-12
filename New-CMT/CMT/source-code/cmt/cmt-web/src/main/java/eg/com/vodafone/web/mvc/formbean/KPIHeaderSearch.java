package eg.com.vodafone.web.mvc.formbean;


import eg.com.vodafone.model.VNode;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 3/16/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class KPIHeaderSearch extends SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1;

    private String systemName;
    //private VNode vNode;
    private String nodeValue;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

   /* public VNode getVNode() {
        return vNode;
    }

    public void setVNode(VNode vNode) {
        this.vNode = vNode;
    }*/

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }
}
