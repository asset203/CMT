/**
 * 
 */
package com.itworx.vaspp.datacollection.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.itworx.vaspp.datacollection.logmanager.LogFilesDAO;
import com.itworx.vaspp.datacollection.logmanager.LogManager;
import com.itworx.vaspp.datacollection.objects.LogErrorRecord;
import eg.com.vodafone.model.DCLogContacts;

/**
 * @author ahmad.abushady
 *
 */
public class SystemLogParser {

	private Hashtable existingNodes;
	
	
	/**
	 * 
	 */
	public SystemLogParser() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * This function parse a log file. It takes the name of a system and a file
	 * as an input file. It itterates over all the lines in the file. Then return
	 * an arrayList containing the information about jobs that had errors.
	 * 
	 * It checks whether the line has any started jobs, or finished jobs or errors
	 * and accordingly it will deduce jobs with errors.
	 * 
	 * @param sysName
	 * @param inputFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList parse(String sysName, String nodeName,File inputFile)
	throws FileNotFoundException, IOException, ParseException{
		
		BufferedReader inputStream = new BufferedReader(new FileReader(inputFile));
		ArrayList<LogErrorRecord> Result = new ArrayList<LogErrorRecord>();
		
		Hashtable StartedJobs = new Hashtable();
		
		existingNodes = new Hashtable();
		
		String collectNodeStart = "started collectNodeInputs( ";
		String collectNodeEnd = "finished collectNodeInputs( ";
		String collectSystemStart = "started collectSystemInputs( ";
		String collectSystemEnd = "finished collectSystemInputs( ";
		String batchCollectorStart = "BatchCollector.dispatchJob() - started dispatchJob";
		String batchCollectorEnd = "BatchCollector.dispatchJob() - finished dispatchJob";
		String ErrorString = " ERROR ";
		
		boolean globalErrorExisted = false;
		boolean addGlobalError = true;
		
		
		String line = inputStream.readLine();
		
		while(line != null){
			if(line.indexOf(collectNodeStart) > 0){
				int indx = line.indexOf(collectNodeStart);
				String[] info = line.substring(indx + collectNodeStart.length(),line.length() -1).split(",");
				info[1] = getDate(info[1]);
				if(existingNodes.get(info[0]) == null){
					existingNodes.put(info[0],info[0]);
				}
				if(nodeName.equals("All") || nodeName.equalsIgnoreCase(info[0])){
					StartedCollecting(sysName, info[0], info[1], StartedJobs);
				}
			}
			else if(line.indexOf(collectNodeEnd) > 0){
				int indx = line.indexOf(collectNodeEnd);
				String[] info = line.substring(indx + collectNodeEnd.length(),line.length() -1).split(",");
				info[1] = getDate(info[1]);
				if(existingNodes.get(info[0]) == null){
					existingNodes.put(info[0],info[0]);
				}
				if(nodeName.equals("All") || nodeName.equalsIgnoreCase(info[0])){
					FinishedCollecting(sysName, info[0], info[1], StartedJobs, Result);
				}
			}
			else if(line.indexOf(collectSystemStart) > 0){
				int indx = line.indexOf(collectSystemStart);
				String[] info = line.substring(indx + collectSystemStart.length(),line.length() -1).split(",");
				info[0] = "system";
				info[1] = getDate(info[1]);
				if(existingNodes.get(info[0]) == null){
					existingNodes.put(info[0],info[0]);
				}
				if(nodeName.equals("All") || nodeName.equalsIgnoreCase(info[0])){
					StartedCollecting(sysName, info[0], info[1], StartedJobs);
				}
			}
			else if(line.indexOf(collectSystemEnd) > 0){
				int indx = line.indexOf(collectSystemEnd);
				String[] info = line.substring(indx + collectSystemEnd.length(),line.length() -1).split(",");
				info[0] = "system";
				info[1] = getDate(info[1]);
				if(existingNodes.get(info[0]) == null){
					existingNodes.put(info[0],info[0]);
				}
				if(nodeName.equals("All") || nodeName.equalsIgnoreCase(info[0])){
					FinishedCollecting(sysName, info[0], info[1], StartedJobs, Result);
				}
			}
			else if(line.indexOf(ErrorString) > 0){
				String errLine = line.substring(line.indexOf('-') + 1).trim();
				String date = getYesterdayDate(line.substring(0,10));
				LogError(errLine, sysName, nodeName, date, StartedJobs);
				
			}
			else if(line.indexOf(batchCollectorStart) > 0){
				do{
					line = inputStream.readLine();
				}while(line.indexOf(batchCollectorEnd) < 0);
			}
			
			line = inputStream.readLine();
		}
		//get any general error and add it to the result
		LogErrorRecord globalErrorRec = (LogErrorRecord)StartedJobs.get(sysName + " node");
		if(globalErrorRec != null){
			globalErrorRec.setType("Error");
			//Result.add(globalErrorRec);
			globalErrorExisted = true;
			StartedJobs.remove(sysName + " node");
		}
		
		//get unfinished jobs and add it to the result
		Iterator nodesIterator = StartedJobs.values().iterator();
		while(nodesIterator.hasNext()){
			addGlobalError = retrieveAllNodeJobs((Hashtable)nodesIterator.next(), Result, globalErrorExisted, globalErrorRec);
		}
		
		if(addGlobalError && globalErrorExisted){
			Result.add(globalErrorRec);
		}

        /**
         * Retrieve error codes
         */
        ArrayList<LogErrorRecord> copyLogErrorRecords = new ArrayList<LogErrorRecord>();

        String errorRecordStr = "";
        for(LogErrorRecord errorRecord : Result){
            if(errorRecord.getDescription() != null
                    && !errorRecord.getDescription().trim().equals("")
                    && errorRecord.getDescription().indexOf(':') > 0
                    && errorRecord.getType().equalsIgnoreCase("Error")){
                errorRecordStr = errorRecord.getDescription().substring(0,
                        errorRecord.getDescription().indexOf(':'));
                errorRecord.setDescription(errorRecord.getDescription().substring(
                        errorRecord.getDescription().indexOf(':')+1,
                        errorRecord.getDescription().length()-1));
                System.out.println("Error code before parsing:" + errorRecordStr);
                System.out.println("Desc:" + errorRecord.getDescription());
                if(errorRecord.getDescription().lastIndexOf('-') > 0
                        && errorRecord.getDescription().lastIndexOf('-') < errorRecord.getDescription().indexOf(':')){
                    if(errorRecordStr.length() > 1
                            && errorRecordStr.length() > errorRecord.getDescription().lastIndexOf('-')){
                        errorRecordStr = errorRecordStr.substring(errorRecord.getDescription().lastIndexOf('-') + 1,
                                errorRecordStr.length()-1);
                    }
                    //System.out.println("Error code after parsing:" + errorRecordStr);
                    errorRecord.setErrorCode(errorRecordStr.toString().trim());
                }else{
                    errorRecord.setErrorCode(errorRecordStr.toString().trim());
                }
                copyLogErrorRecords.add(errorRecord);
            }
        }
		return copyLogErrorRecords;
	}
	
	
	/**
	 * This function takes a date as string in a format parse it then return it
	 * in another format to be used.
	 * @param d
	 * @return d in this format "MM/dd/yyyy"
	 * @throws ParseException
	 */
	private String getDate(String d) throws ParseException{
		SimpleDateFormat a = new SimpleDateFormat();
		a.applyPattern("EEE MMM dd HH:mm:ss zzz yyyy");
		Date x = a.parse(d);
		a.applyPattern("MM/dd/yyyy");
		return a.format(x);
	}
	
	
	/**
	 * This function is used to get the date of the day before the given date
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	private String getYesterdayDate(String day) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    GregorianCalendar gc = new GregorianCalendar();
	    java.sql.Date d 	=  	new java.sql.Date(sdf.parse(day).getTime());
	    gc.setTime(d);
	    int dayBefore = gc.get(Calendar.DAY_OF_YEAR);
	    gc.roll(Calendar.DAY_OF_YEAR, -1);
	    int dayAfter = gc.get(Calendar.DAY_OF_YEAR);
	    if(dayAfter > dayBefore) {
	        gc.roll(Calendar.YEAR, -1);
	    }
	    gc.get(Calendar.DATE);
	    java.util.Date yesterday = gc.getTime();
	    sdf.applyPattern("MM/dd/yyyy");
	    String currentDate = sdf.format(yesterday);
	    
	    return currentDate;
	}
	
	
	/**
	 * This function takes info about a certain job (i.e. system name, node name,
	 * the date of the job). Then it checks in the startedjobs for a job with
	 * the same exact data. In case the job was not started before, it adds this
	 * new job. However, if it found that the same exact job was started before
	 * this means that the job failed before. Thus it will increment the retry
	 * count of this job, indicating that this job had errors.
	 * 
	 * @param sysName
	 * @param nodeName
	 * @param jobDate
	 * @param StartedJobs
	 */
	private void StartedCollecting(String sysName, String nodeName, 
			String jobDate,	Hashtable StartedJobs){
		
		Hashtable node_Job = (Hashtable)StartedJobs.get(nodeName);
		
		if(node_Job == null){
			node_Job = new Hashtable();
			node_Job.put(jobDate, new LogErrorRecord(sysName,jobDate,nodeName));
			StartedJobs.put(nodeName, node_Job);
			
		}
		else{
			LogErrorRecord errRec = (LogErrorRecord)node_Job.get(jobDate);
			
			if(errRec == null){
				node_Job.put(jobDate, new LogErrorRecord(sysName,jobDate,nodeName));
			}
			else{
				errRec.incRetry();
			}
		}
	}
	
	
	/**
	 * This function takes info about a certain job (i.e. system name, node name,
	 * the date of the job). Then it checks if this job have encountered any
	 * errors while running. If this is the case then it adds the job record to
	 * the Result ArrayList. Finally the function removes the job from running
	 * jobs. In case the finished job was not started it will be ignored.
	 * 
	 * @param sysName
	 * @param nodeName
	 * @param jobDate
	 * @param StartedJobs
	 * @param Result
	 */
	private void FinishedCollecting(String sysName, String nodeName, 
			String jobDate,	Hashtable StartedJobs, ArrayList Result){
		
		Hashtable node_Jobs = (Hashtable)StartedJobs.get(nodeName);
		
		if(node_Jobs != null){
			LogErrorRecord errRec = ((LogErrorRecord)(node_Jobs).get(jobDate));
			
			if(errRec != null){
				//there is error for the collected data
				if(errRec.isHaveErrors()){
					errRec.setType("Warning");
					Result.add(errRec);
					Hashtable node_Job = (Hashtable)StartedJobs.get(nodeName);
					node_Job.remove(jobDate);
					if(node_Job.size() <= 0){
						StartedJobs.remove(nodeName);
					}
				}
				//there is no error for the collected data but the job started more than once
				else if(errRec.getRetry() >= 0){
					errRec.decRetry();
					if(errRec.getRetry() < 0){
						Hashtable node_Job = (Hashtable)StartedJobs.get(nodeName);
						node_Job.remove(jobDate);
						if(node_Job.size() <= 0){
							StartedJobs.remove(nodeName);
						}
					}else{
						if(!errRec.getDescription().equals("")){
							// to handle the case of error happened in that day and more than one job started with different dates
							// and one of them started again and finished correctly, so it will be logged as warning with the error 
							errRec.setType("Warning");
							errRec.incRetry();
							Result.add(errRec);
							Hashtable node_Job = (Hashtable)StartedJobs.get(nodeName);
							node_Job.remove(jobDate);
							if(node_Job.size() <= 0){
								StartedJobs.remove(nodeName);
							}
						}/*else if(!nodeName.equals("system")){
							// to handle the case of "global error" not an normal error happened in that day and more than one job started with different dates
							// and one of them started again and finished correctly, so it will be logged as warning with the error
							errRec.incRetry();
						}*/
					}
				}
			}
		}
	}
	
	
	/**
	 * This function loggs any encountered errors. In case this error did not
	 * have the node that caused this error, it will be logged as a general error
	 * @param errLine
	 * @param sysName
	 * @param date
	 * @param StartedJobs
	 */
	private void LogError(String errLine, String sysName, String nodeName,
			String date, Hashtable StartedJobs){
		//check for the node name in the error
		int startIndex = errLine.indexOf('-');
		int endIndex = errLine.indexOf("- ", startIndex + 1);
		String nName;
		
		//if there is a node name, either empty or not add this error to the node
		if(endIndex > 0){
			nName = errLine.substring(startIndex + 1, endIndex);
			 //if node name is empty it means that it is a system error
			 if(nName.equals("")){
				 nName = "system";
			 }
			 
			 
			 //check if this error belong to the intended node
			 if(nodeName.equals("All") || nodeName.equalsIgnoreCase(nName)){
				 
				 
				 Hashtable node_Jobs = (Hashtable)StartedJobs.get(nName);
				 
				 //if there is no jobs started for this node, log it as a global error
				 if(node_Jobs == null){
					nName = sysName + " node";
					LogErrorRecord errRec = (LogErrorRecord) StartedJobs.get(nName);
					if(errRec == null){
						errRec = new LogErrorRecord(sysName,date,nName);
						errRec.LogError(errLine);
						StartedJobs.put(nName, errRec);
					}
					else{
						errRec.LogError(errLine);
					}
				 }
				 /*if there is a job for this node, add this error to all the jobs as
				 * unknown error. in case any of the jobs ended without any error
				 * this unknown error will be discarded. it will only appear to jobs
				 * that did not finish or finished with some known errors.
				 */
				 else{
					 Iterator allJobs = node_Jobs.values().iterator();
					 String subError = errLine.substring(endIndex+1).trim();
					 while(allJobs.hasNext()){
						 LogErrorRecord errRec = (LogErrorRecord) allJobs.next();
						 if(node_Jobs.size() > 1){
							 errRec.LogUnknownError(subError);
						 }
						 else errRec.LogError(subError);
					 }
				 }
			 }
			 /*
			  * Handle the case an error happened to a node that is not the intended node
			  * if this node did not start before, handle it as general error.
			  */
			 else{
				 if(existingNodes.get(nName) == null){
					nName = sysName + " node";
					LogErrorRecord errRec = (LogErrorRecord) StartedJobs.get(nName);
					if(errRec == null){
						errRec = new LogErrorRecord(sysName,date,nName);
						errRec.LogError(errLine);
						StartedJobs.put(nName, errRec);
					}
					else{
						errRec.LogError(errLine);
					}
				 }
			 }
		}
		//if there is no node name (i.e. -Node- or -- or any 2 - in the line) handle this case as if there is an error
		//from an unknown source
		else{
			nName = sysName + " node";
			LogErrorRecord errRec = (LogErrorRecord) StartedJobs.get(nName);
			if(errRec == null){
				errRec = new LogErrorRecord(sysName,date,nName);
				errRec.LogError(errLine);
				StartedJobs.put(nName, errRec);
			}
			else{
				errRec.LogError(errLine);
			}
		}
				
	}
	
	
	/**
	 * this function takes a hash table containing jobs started for a certain node
	 * it sets the type of these jobs to error and add it to the result
	 * @param node_Jobs
	 * @param Result
	 */
	private boolean retrieveAllNodeJobs(Hashtable node_Jobs, ArrayList Result, boolean globalErrorExisted, LogErrorRecord globalErrorRec){
		Iterator allJobs = node_Jobs.values().iterator();
		boolean addGlobalError = true;
		 while(allJobs.hasNext()){
			 LogErrorRecord errRec = (LogErrorRecord) allJobs.next();
			 if( errRec.isHaveErrors() || !errRec.getDescription().equals("") ){
				 errRec.setType("Error");
				}else if(!errRec.isHaveErrors() && errRec.getDescription().equals("") && globalErrorExisted && errRec.getRetry() > 0){
					errRec.setType("Error");
					errRec.setDescription("Global Error occurred with description: "+globalErrorRec.getDescription());
					addGlobalError = false;
				}else if(errRec.getRetry() >= 0){
					errRec.setType("Warning");
					//if(errRec.getNode().equals("system")){
						errRec.incRetry();
					//}					
					if(globalErrorExisted){
						errRec.setDescription("Global Error occurred with description: "+globalErrorRec.getDescription());
						addGlobalError = false;
					}else{
						errRec.setDescription("A Job with no error started but did not finish.");
						addGlobalError = true;
					}
				}
			 Result.add(errRec);
		 }
		 return addGlobalError;
	}
	
	
	 // part for testing

	public static void main(String[] args) throws Exception{
		SystemLogParser par = new SystemLogParser();
		ArrayList<LogErrorRecord> myArr = par.parse("VPN_XML","All", new File(
                "F:\\Work\\VFE_CMt_SVN\\SourceCode\\vodafone\\etc\\resources\\logs\\VPN_XML.log"));
		for(int i=0; i< myArr.size(); i++){
			System.out.println("System Name:" + myArr.get(i).getSysName()
            + "\nNode name:" + myArr.get(i).getNode()
                    + "\nLog type:" + myArr.get(i).getType()
                    + "\nError Code:" + myArr.get(i).getErrorCode()
                    + "\nDescription:" + myArr.get(i).getDescription());
		}
	}

}
