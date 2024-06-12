/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.web.mvc.model;

/**
 *
 * @author mahmoud.awad
 */
public enum ApplicationsToAccess {

    CMT("CMT"),
    CMT_DASHBOARD("CMT_DASHBOARD"),
    ALL("ALL");

    String appName;

    ApplicationsToAccess(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return this.appName;
    }

}
