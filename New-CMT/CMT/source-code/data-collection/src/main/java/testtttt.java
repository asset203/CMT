import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class testtttt {
	
	 private static DataSource reportingConnectionPool = null;
	
	/*public static void main(String[] args) throws InputException,
	ApplicationException{
		
System.out.println
	("DBInputDAO.getConnection() - started getConnection for input id=");
String driverName = "oracle.jdbc.driver.OracleDriver";
try {
	
	DriverManager.registerDriver((Driver) Class.forName(driverName)
			.newInstance());
	String DBURL = null;
	DBURL = "jdbc:oracle:thin:" + "@" + "(DESCRIPTION=(FAILOVER=on)(LOAD_BALANCE=on)(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.75)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.74)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.73)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.72)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.71)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.70)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.69)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.68)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.45)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.44)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.35)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.42)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.41)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.40)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.39)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.230.83.38)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=sys_admin)))"; 
	String dataBaseUserName = "tmohsen";
	String dataBasepPassword = "tmohsen321";
    String maxNoConnections ="1";
    String dataBaseURL = DBURL;

    reportingConnectionPool = new OracleDataSource();
    ((OracleDataSource)reportingConnectionPool).setConnectionCacheName("reportingConnectionPool");
    ((OracleDataSource)reportingConnectionPool).setURL(dataBaseURL);
    ((OracleDataSource)reportingConnectionPool).setUser(dataBaseUserName);
    ((OracleDataSource)reportingConnectionPool).setPassword(dataBasepPassword);
	Properties cacheProps = new Properties();
	cacheProps.setProperty("MaxLimit", Integer.parseInt(maxNoConnections)+"");
    ((OracleDataSource)reportingConnectionPool).setConnectionCacheProperties(cacheProps);
	((OracleDataSource)reportingConnectionPool).setConnectionCachingEnabled(true);
	
	 Connection conn = reportingConnectionPool.getConnection();
	System.out.println("eeee" + conn.getMetaData());
	
	
	
	
	/*if (driverName.indexOf("oracle") != -1) {
		DBURL = "jdbc:oracle:thin:" + "@" + input.getServer() + 
		((input.getInputName().startsWith("/"))?input.getInputName(): ":" + input.getInputName());
	} else if (driverName.indexOf("SQLServerDriver") != -1) {
		DBURL = input.getPathsList()[0] + "//" + input.getServer() + ";user="
				+ input.getUser() + ";password=" + input.getPassword()
				+ ";DatabaseName=" + input.getInputName();
	} else if (driverName.indexOf("mysql") != -1) {
		DBURL = input.getPathsList()[0] + "//" + input.getServer() + ":3306/"
		+ input.getInputName();
	} else if (driverName.indexOf("sybase") != -1) {
		DBURL =input.getPathsList()[0]+ input.getServer()+ "/" +input.getInputName();
	}//
	System.out.println("DBInputDAO.getConnection() - DB url: " + DBURL);

	Connection connection = DriverManager.getConnection(DBURL, "tmohsen","tmohsen321");
	System.out.println("DBInputDAO.getConnection() - finished getConnection for input id=" + connection.getWarnings());
	
} catch (InstantiationException e) {
	System.out.println("- DB-1001: DBInputDAO.getConnection() - Couldn't instantiate DB driver: "
					+ driverName + ":" + e);
	e.printStackTrace();
	throw new ApplicationException("" + e);
} catch (SQLException e) {

	System.out.println("- DB-1002:DBInputDAO.getConnection() - SQL error while creating connection"
					+ e);
	e.printStackTrace();
	throw new InputException("" + e);
} catch (IllegalAccessException e) {
	System.out.println("- DB-1001:DBInputDAO.getConnection() - Error with DB driver "
			+ driverName + ":" + e);
	throw new ApplicationException("" + e);
} catch (ClassNotFoundException e) {
	System.out.println("- DB-1004: DBInputDAO.getConnection() - Couldn't find DB driver class "
					+ driverName + ":" + e);
	throw new ApplicationException("" + e);
}
} */

 public static void main(String[] args){
     Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z\\d_]*$");
     Matcher matcher = p.matcher("_8sdfsd");
     if(matcher.matches()){
         System.out.println("********** valid *****************");
     }
     else
     {
         System.out.println("********** Invalid *****************");
     }
 }
}
