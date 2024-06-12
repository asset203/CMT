package eg.com.vodafone.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/19/13
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusinessException  extends RuntimeException{
	private static final long serialVersionUID = 1;
    public static final long DUPLICATE_SYSTEM_NAME = 1;
    public static final long EMPTY_SYSTEM=2;
    public static final long DUPLICATE_NODE_NAME=3;
    public static final long FAILED_TO_UPDATE_NODE_STATUS=4;
    public static final long DB_ACCESS_INPUT_CAN_HAVE_SINGLE_DATA_COLLECTION=6;
    public static final long INPUT_MUST_HAVE_AT_LEAST_ONE_DATA_COLLECTION=7;
    public static final long EMPTY_NODE=8;
    public static final long DUPLICATE_INPUT_STRUCTURE_NAME=9;
    public static final long INPUT_STRUCTURE_USED_FAILD_TO_DELETE = 12;
    public static final long TABLE_DOES_NOT_EXIST=13;
    public static final long DATA_COLLECTION_NOT_EXIST = 15;
    public static final long SYSTEM_NOT_EXIST = 16;
    public static final long NODE_NOT_EXIST = 17;
    public static final long INVALID_INDEX = 18;
    public static final long INVALID_TABLE_NAME=19;
    public static final long FAILED_TO_UPDATE_TABLE=20;
    public static final long NAME_USED_BY_OTHER_DB_OBJECT=21;


    private long code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(long code) {
        this.code = code;
    }

    public BusinessException(String message, long code) {
        super(message);
        this.code = code;
    }
    public BusinessException(long code, Map<String,Object> params){
        this.code=code;
    }

    public BusinessException(String message, Throwable cause, long code) {
        super(message, cause);
        this.code = code;
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause, long code) {
        super(cause);
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }
}
