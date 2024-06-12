package eg.com.vodafone.service.utils;

import com.jcraft.jsch.*;
import eg.com.vodafone.model.Job;
import eg.com.vodafone.model.SSHResult;
import eg.com.vodafone.service.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * This utility class is used to execute command or execute a shell script
 * on a Unix/Linux remote machine
 * <p/>
 * <b>Reference:</b>http://www.jcraft.com/jsch/
 * http://wiki.jsch.org/index.php?Manual
 * <p/>
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 5/2/13
 * Time: 10:06 AM
 */

public class SSHUtilityService implements Callable<SSHResult> {

    //TODO load from DB or property file
    public static final int TIMEOUT = 30000;
    public static final String TIMEOUT_MESSAGE = "TIMEOUT, operation may be working in background";
    private static final Logger LOGGER = LoggerFactory.getLogger(SSHUtilityService.class);
    private String macIP;
    private String port;
    private String userName;
    private String password;
    private String shellScriptPath;
    private String shellScriptName;
    private String[] params;

    public SSHUtilityService(String macIP, String port, String userName, String password,
                             String shellScriptPath, String shellScriptName, String... params) {
        this.macIP = macIP;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.shellScriptPath = shellScriptPath;
        this.shellScriptName = shellScriptName;
        this.params = params;
    }

    public String getMacIP() {
        return macIP;
    }

    public void setMacIP(String macIP) {
        this.macIP = macIP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
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

    public String getShellScriptPath() {
        return shellScriptPath;
    }

    public void setShellScriptPath(String shellScriptPath) {
        this.shellScriptPath = shellScriptPath;
    }

    public String getShellScriptName() {
        return shellScriptName;
    }

    public void setShellScriptName(String shellScriptName) {
        this.shellScriptName = shellScriptName;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * Executes a shell script on a remote machine giving it n parameters
     *
     * @param macIP
     * @param port
     * @param userName
     * @param password
     * @param shellScriptPath
     * @param shellScriptName
     * @param params
     * @return
     */
    public SSHResult executeShellScript(String macIP, String port, String userName,
                                        String password, String shellScriptPath,
                                        String shellScriptName, String... params) {
        LOGGER.debug("Parameters received for SSH command execution, macIP:{}, username:{}, password:{}",
                new Object[]{macIP, userName, password});

        LOGGER.info("Shell script path received:{}\nShell script name received:{}\nShell Script parameters:{}",
                new Object[]{shellScriptPath, shellScriptName, params});

        SSHResult sshResult = new SSHResult();

        if (StringUtils.hasText(macIP) && StringUtils.hasText(userName)
                && StringUtils.hasText(password) && StringUtils.hasText(shellScriptPath)
                && StringUtils.hasText(shellScriptName)) {

            //Start connection
            JSch jsch = new JSch();
            Channel channel = null;
            Session session = null;
            try {
                session = jsch.getSession(userName, macIP, Integer.valueOf(port));
                session.setPassword(password);
                /**
                 * The following property is mandatory otherwise the following exception will be thrown:
                 * com.jcraft.jsch.JSchException: UnknownHostKey
                 */
                Properties config
                        = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setTimeout(TIMEOUT);
                session.setServerAliveCountMax(TIMEOUT);
                session.connect();
                if (session != null && session.isConnected()) {
                    StringBuffer command = new StringBuffer(400);

                    command.append("cd ").append(shellScriptPath).append(";./").append(shellScriptName).append(' ');
                    if (params != null && params.length > 0) {
                        LOGGER.info("parameters received with size:{}", params.length);
                        for (String param : params) {
                            command.append('\"').append(param).append("\" ");
                        }
                        LOGGER.info("Final command:{}", command);
                    }

                    channel = session.openChannel("exec");
                    ((ChannelExec) channel).setCommand(command.toString());
                    channel.setInputStream(null);
                    InputStream in = channel.getInputStream();

                    LOGGER.debug("Starting shell script execution");

                    channel.connect();


                    BufferedReader buf = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    StringBuffer commandOutput = new StringBuffer();
                    while ((line = buf.readLine()) != null) {
                        commandOutput.append(line).append('\n');
                    }

                    LOGGER.info("console output:\n{}", commandOutput);

                    /**
                     * If error stream is not empty then result should be false
                     */
                    String errorCode = getErrorCode(commandOutput);
                    if (StringUtils.hasText(errorCode)) {
                        sshResult.setErrorMessage(
                                errorCode.replace(Job.JOB_REMOTE_ERROR_KEYWORD, " "));
                    } else {
                        sshResult.setSuccess(true);
                        sshResult.setErrorMessage(" ");
                    }


                    channel.disconnect();
                    session.disconnect();
                }
            } catch (JSchException e) {
                LOGGER.error("JSchException", e);
                throw new BusinessException("JSchException " + e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error("IOException", e);
                throw new BusinessException("IOException " + e.getMessage(), e);
            } finally {
                channel.disconnect();
                session.disconnect();
            }

        } else {
            throw new BusinessException("One or more of the received parameters is null");
        }

        return sshResult;
    }

    /**
     * Extract error code from command output
     *
     * @param commandOutput command execution output
     * @return error code if found
     */
    private String getErrorCode(StringBuffer commandOutput) {
        String errorCode = "";
        if (StringUtils.hasText(commandOutput)
                && commandOutput.indexOf(Job.JOB_REMOTE_ERROR_KEYWORD) > 0) {
            errorCode = commandOutput.substring(commandOutput.indexOf(Job.JOB_REMOTE_ERROR_KEYWORD),
                    commandOutput.length());
        }
        LOGGER.error("Error code found:" + errorCode);
        return errorCode;
    }

    @Override
    public SSHResult call() throws Exception {
        return executeShellScript(macIP, port, userName, password,
                shellScriptPath, shellScriptName, params);
    }
}
