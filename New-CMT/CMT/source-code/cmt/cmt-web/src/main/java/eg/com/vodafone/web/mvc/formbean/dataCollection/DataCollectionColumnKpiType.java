/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * CMT DashBoard configuration 27-9-2018
 *
 * @author mahmoud.awad
 */
public enum DataCollectionColumnKpiType {

    NONE(0, "SELECT_KPI_TYPE"),
    KPI_DATE(1, "KPI_DATE"),
    KPI_TIME(2, "KPI_TIME"),
    KPI_DATE_TIME(3, "KPI_DATE_TIME"),
    KPI_FILTER(4, "KPI_FILTER"),
    KPI_VALUE(5, "KPI_VALUE"),
    KPI_HOUR(6,"KPI_HOUR");

    private Integer value;
    private String description;

    DataCollectionColumnKpiType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getValue() {
        return value;
    }

}
