package eg.com.vodafone.web.mvc.util;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 4/9/13
 * Time: 10:12 AM
 */
public class CMTConstants {
    public static final String USERNAME_PATTERN = "[[[0-9][A-Za-z]|(_)|(.)]+]{4,15}";
    public static final String MOBILE_PATTERN = "^$|^01[0-2]{1}[0-9]{8}";
    public static final String PASSWORD_PATTERN = "^.*(?=.{6,20})(?=.*\\d)(?=.*[a-zA-Z]).*$";
    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String DATE_DB_PATTERN = "dd-MMM-yyyy";
    public static final String PORT_PATTERN = "\\d{1,5}";
    public static final String VALID_DB_IDENTEFIER_PATTERN = "^[a-zA-Z][a-zA-Z\\d_]{1,25}$";
    public static final String VALID_DB_IDENTEFIER_PATTERN_LNGTH_30 = "^[a-zA-Z][a-zA-Z\\d_]{1,30}$";
    public static final String VALID_INPUT_NAME_PATTERN = "^[\\sa-zA-Z0-9_-]{1,50}$";//"[[a-zA-Z][0-9]|(_)]{1,50}$";
    public final static String SUCCESS_MSG_KEY = "successMsg";
    public final static String ERROR_MSG_KEY = "errorMsg";
    public static final String DEFAULT_SELECT = "Select ... ";
    //Validation error keys
    public final static String FIELD_REQUIRED = "field.required";
    public final static String FIELD_NOT_EXIST = "field.not.exist";
    public final static String FIELD_INVALID = "field.invalid";
    public final static String EMAIL_FIELD_SIZE_INVALID = "email.size.invalid";
    //DataCollection constants
    public final static String DC_INPUT_FORM_BEAN_NAME = "dcInputFormBean";
    public final static String DC_SYSTEM_FORM_BEAN_NAME = "dcSystemFormBean";
    public final static String DC_NODE_FORM_BEAN_NAME = "dcNodeFormBean";
    public final static String ERRORS = "errors";
    public static final String NODE_NAME_COLUMN = "NODE_NAME";
    public static final String INVALID_DC_NAME = "invalid_dataCollection_name";
    //Jobs
    public static final String FORCE_JOB_DATE_PATTERN = "dd/M/yyyy";
    public static final String PLEASE_SELECT = "Please select";
    public static final String CREATE_SUCSS_MSG_FLAG = "create";
    public static final String DELETE_SUCSS_MSG_FLAG = "delete";
    public static final String DELETE_JOB_ERROR_FLAG = "delete_jobs_error";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    //System event
    public static final String EDIT_SYSTEM_EVENT = "editSystemEvent";
    public static final String ACTION_ADD_EVENT = "addSystemEvent";


}
