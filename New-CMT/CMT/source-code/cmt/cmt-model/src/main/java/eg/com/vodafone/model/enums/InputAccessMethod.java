package eg.com.vodafone.model.enums;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/20/13
 * Time: 5:33 AM
 * To change this template use File | Settings | File Templates.
 */
public enum InputAccessMethod {

    DB_ACCESS("DB_access"),
    FTP_ACCESS("ftp_access"),
    FTP_SINGLE_ACCESS("ftp_single_access"),
    SFTP_ACCESS("sftp_access"),
    LOCAL_ACCESS("local_access");

    private String name;

     private InputAccessMethod(String name){
         this.name=name;
     }

    public String getName() {
        return name;
    }

    public static  InputAccessMethod getAccessMethod(String name){
        for(InputAccessMethod accessMethod : InputAccessMethod.values()){
            if(accessMethod.getName().equals(name)){
                return accessMethod;
            }
        }
       return null;
    }
}
