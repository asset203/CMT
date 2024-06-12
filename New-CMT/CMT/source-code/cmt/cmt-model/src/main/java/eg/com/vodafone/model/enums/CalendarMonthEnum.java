package eg.com.vodafone.model.enums;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 3/16/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public enum CalendarMonthEnum implements Serializable {

    JANUARY(0, "JAN"),
    FEBRUARY(1, "FEB"),
    MARCH(2, "MAR"),
    APRIL(3, "APR"),
    MAY(4, "MAY"),
    JUNE(5, "JUN"),
    JULY(6, "JUL"),
    AUGUST(7, "AUG"),
    SEPTEMBER(8, "SEP"),
    OCTOBER(9, "OCT"),
    NOVEMBER(10, "NOV"),
    DECEMBER(11, "DEC");

    private final int month;
    private final String description;

    private CalendarMonthEnum(int month, String description) {
        this.month = month;
        this.description = description;
    }

    public int getMonth() {
        return month;
    }

    public String getDescription() {
        return description;
    }

}
