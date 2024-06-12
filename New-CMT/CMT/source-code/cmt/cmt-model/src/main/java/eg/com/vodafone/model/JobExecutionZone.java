package eg.com.vodafone.model;

/**
 * @author marwa.goda
 * @since 4/23/13
 */
public class JobExecutionZone {

    private int id;
    private String name;
    private String ip;
    private int port;
    private String userName;
    private String password;
    private String dcPath;
    private String qrtzTblPrefix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDcPath() {
        return dcPath;
    }

    public void setDcPath(String dcPath) {
        this.dcPath = dcPath;
    }

    public String getQrtzTblPrefix() {
        return qrtzTblPrefix;
    }

    public void setQrtzTblPrefix(String qrtzTblPrefix) {
        this.qrtzTblPrefix = qrtzTblPrefix;
    }
}
