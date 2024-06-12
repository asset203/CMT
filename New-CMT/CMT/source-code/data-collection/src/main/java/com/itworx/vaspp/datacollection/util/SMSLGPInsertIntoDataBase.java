package com.itworx.vaspp.datacollection.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;

public class SMSLGPInsertIntoDataBase {

	private PersistenceManager persistenceManager;
	
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	private static final String lOG_CONF = "resources" + FILE_SEPARATOR + "configuration" + FILE_SEPARATOR + "log4j.xml";

	private final static String sql = 
		"insert into DC_SMS_LGP_DETAILED(" +
							"ID," +
							"DATE_TIME," +
							"HEADER," +
							"REJ_CAUSE," +
							"SUB_RESULT," +
							"ORIG_COUNTRY," +
							"ORIG_NETWORK," +
							"APP_NAME," +
							"APP_SC," +
							"TERM_COUNTRY," +
							"TERM_NETWORK," +
							"TERM_MSISDN," +
							"SRI_ACTION," +
							"SRI_QUERY_RESULT," +
							"TERM_MSC," +
							"DELIVERY_RESULT," +
							"COUNT," +
							"LGP_DB" +
							") " +
							" values " +
							"(DC_SMS_LGP_DETAILED_SEQ.nextval,TO_DATE(?,'MM/DD/YYYY HH24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private Logger logger = null;
	
	public SMSLGPInsertIntoDataBase(PersistenceManager pManager,String systemName){
		this.persistenceManager = pManager;
		this.logger = Logger.getLogger(systemName);
	}
	
	public void insertIntoDatabase(File[] inputFiles ,String nodeName, Date executionDate, int batchSize)	throws ApplicationException, InputException {
		logger.debug("Inside SMSLGPInsertIntoDataBase - started inserting input files");
		BufferedReader inputStream = null;
		if(inputFiles.length==0)
			return;
		
		Connection connection = null;
		Session session = null;
		
		try{
			session = getSession();
			if(session==null)
				return;
			
			connection = session.connection();
			if(connection==null)
				return;
			int currentLine = 0;

			PreparedStatement deleteStatement = null;
			if(false){
			//if(executionDate != null){
				deleteStatement = connection.prepareStatement("DELETE FROM DC_SMS_LGP_DETAILED WHERE TRUNC(DATE_TIME) = TRUNC(?) AND LGP_DB = ?");
				deleteStatement.setDate(1, Utils.sqlDate(executionDate));
				deleteStatement.setString(2, nodeName);
				
				int rowsDeleted = deleteStatement.executeUpdate();
				connection.commit();
				logger.info("SMSLGPInsertIntoDataBase - deleted : "+rowsDeleted+" before inserting date for date :"+executionDate+" and for node : "+nodeName );
				
			}
			PreparedStatement statement = connection.prepareStatement(sql);
			
		
			try {
				for (int i = 0; i < inputFiles.length; i++) {
					logger.debug("SMSLGPInsertIntoDataBase.insertIntoDataBse() - converting file " + inputFiles[i].getName());
					inputStream = new BufferedReader(new FileReader(inputFiles[i]));
					String line;
					String columns[]=null;
					boolean hasData = false;
					
					while ((line = inputStream.readLine()) != null) {
						currentLine++;
						if (!line.contains(",")){
							logger.error("SMSLGPInsertIntoDataBase - invalid line ["+line+"] in line no ["+currentLine+"]");
							continue;
						}
						columns = line.split(",");
						if(columns.length != 16){
							logger.error("SMSLGPInsertIntoDataBase - line doesn't match ["+line+"] in line no ["+currentLine+"]");
							continue;
						}
						
						saveDataToTable(statement,columns ,nodeName);
						statement.addBatch();
						hasData = true;
						
						if(currentLine % batchSize == 0){
							statement.executeBatch();
							connection.commit();
							statement.clearBatch();
						}
					}
					if(hasData){
						statement.executeBatch();
						connection.commit();
						statement.clearBatch();						
					}
				} 
			} catch (Exception e) {
				logger.error("SMSLGPInsertIntoDataBase - error done while conversion ",e);
			} finally {
				if(deleteStatement != null){
					try{
						deleteStatement.close();
					}catch (Exception e) {
						logger.error("SMSLGPInsertIntoDataBase - error happned while closing delete statement");
					}
				}
				if(statement != null){
					try{
						statement.executeBatch();
						connection.commit();
						statement.clearBatch();
					} catch (Exception e) {
						logger.error("SMSLGPInsertIntoDataBase - error happned while clearing batch statement");
					}
					try{
						statement.close();
					}catch (Exception e) {
						logger.error("SMSLGPInsertIntoDataBase - error happned while closing batch statement");
					}
				}
				
				if(inputStream != null){
					inputStream.close();
				}
			}

			
		} catch (FileNotFoundException e) {
			logger.error("SMSLGPInsertIntoDataBase - Input file not found " , e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("SMSLGPInsertIntoDataBase - Couldn't read input file" , e);
			throw new ApplicationException(e);
		} catch (Exception e) {
			logger.error("SMSLGPInsertIntoDataBase - error happned while conversion ",e);
			throw new ApplicationException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (session != null) {
				session.close();
			}
		}
	}
	public void saveDataToTable(PreparedStatement statement,String columns[],String nodeName)throws ApplicationException
	{
		 try 
		 {
			statement.clearParameters();
			for (int i=0;i<columns.length;i++) {
				statement.setString(i+1, columns[i]);
            }
			statement.setString(17, nodeName);
		 } catch (SQLException e) {
			 logger.error("SMSLGPInsertIntoDataBase - [saveDataToTable.saveDataToTable()]:Exception:" ,e);
			 throw new ApplicationException(e);
		 }

	}
	private Session getSession(){
		 Session session=null;
		 try{
				session = persistenceManager.getNewSession();
				if(session ==  null){
					logger.error("Can't pen new database session");
					return null;
				}
				}catch(Exception e){
					logger.error("Exception : ",e);
					return null;
				}
		return session;
	}
	public static void main(String args[]) {
		Date startDate = new Date();
		long startTime = startDate.getTime();
		
		try {
			
			//args = new String[]{"NODE_1","C:\\OUT_20111212_VASQ_00_05_SMS_LGP_NEW_DETAILED_details_1323767754711","D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode\\DataCollection","1001"};
			
			if (args.length < 3) {
				System.out.println("Usage Parameters:");
				System.out.println("");
				System.out.println(" <<nodeName>> <<fileName>> <<Path>> <<batchSize>>");
				System.exit(1);
			}
			try {
				DOMConfigurator.configure(args[2] + FILE_SEPARATOR + lOG_CONF);
			} catch (Exception e) {
				System.out.println("SMSLGPInsertIntoDataBase.main() - " + e.getMessage());
			}

			PropertyReader.init(args[2]);
			PersistenceManager persistenceManager = new PersistenceManager(true);
			
			SMSLGPInsertIntoDataBase s = new SMSLGPInsertIntoDataBase(persistenceManager,"SMS_LGP_NEW_DETAILED");
			File[] input = new File[1];
			input[0] = new File(args[1]);
			int batchSize = 1000;
			
			try{
				if(args.length == 4){
					batchSize = Integer.parseInt(args[3]);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			s.insertIntoDatabase(input, args[0],null,batchSize);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date endDate = new Date();
		long endTime = endDate.getTime();
		System.out.println("startDate:"+startDate);
		System.out.println("startTime:"+startTime);
		System.out.println("endDate:"+endDate);
		System.out.println("endTime:"+endTime);
		System.out.println("diff:"+(endTime-startTime));
		
	}
}
