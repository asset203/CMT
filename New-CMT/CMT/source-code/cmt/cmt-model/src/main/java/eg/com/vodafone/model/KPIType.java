/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.model;

import java.io.Serializable;

/**
 *
 * @author mahmoud.awad
 */
public class KPIType implements Serializable {

    private long id;
    private String name;

    public KPIType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
