package com.cognizant.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Class to update test results in Azure DevOps
 *
 */
public class AzureIntegration {
	private static Properties properties = Settings.getInstance();
	private String organizationURL = properties.getProperty("AzureOrgURL");
	private String projectName = properties.getProperty("AzureProject");
	private static String strTestPlanName = properties.getProperty("AzureTestPlan");
	private static String strMasterTestSuiteName = properties.getProperty("AzureTestPlan");
	private static String strTestSuiteName = properties.getProperty("AzureExecutionTestSuite");
	private static int runID = 0;
	private static int testPlanID = 0;	
	private static int testMasterSuiteID = 0;
	private static int testRegressionSuiteID = 0;
	private static int testSuiteID = 0;
	private static int testCaseID = 0;
	@SuppressWarnings("unused")
	private int testPointID;
	private static final String AUTHORIZATIONHEADER = "Authorization";
	private String accessToken = properties.getProperty("AzureAccessToken");
	private static final String MIMETYPEJSON = "application/json";
			
	
	
	public void getTestPlanIDAPI() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/plans?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    /*String strJson = "{"
		    					+ "\"automated\" : \"true\""			    				
		    				+ "}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));*/
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
				if(jsonTestCase.getAsJsonObject().get("name").getAsString().trim().equals(strTestPlanName)) {
					testPlanID = jsonTestCase.getAsJsonObject().get("id").getAsInt();
					break;
				}				
			}
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createTestSuite() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID+"/suites?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);		    
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
				if(jsonTestCase.getAsJsonObject().get("name").getAsString().trim().equals(strMasterTestSuiteName)) {
					testMasterSuiteID = jsonTestCase.getAsJsonObject().get("id").getAsInt();
					break;
				}				
			}
		    boolean blnSuiteFound = false;
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
				if(jsonTestCase.getAsJsonObject().get("name").getAsString().trim().equals(strTestSuiteName)) {
					testRegressionSuiteID = jsonTestCase.getAsJsonObject().get("id").getAsInt();
					blnSuiteFound = true;
					break;
				}				
			}
		    
		    if(!blnSuiteFound) {
		    	httpClient = HttpClientBuilder.create().build();
				HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID+"/suites?api-version=7.0");			
				postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
				postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
			    String strJson = "{"
			    					+ "\"suiteType\" : \"staticTestSuite\","
				    				+ "\"name\" : \""+strTestSuiteName+"\","
				    				+"\"parentSuite\":{"
				    					+ "\"id\" : "+testMasterSuiteID
				    				+ "}"
					    			+"}";
			    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
			    response = httpClient.execute(postRequest);
			    strResponse = EntityUtils.toString(response.getEntity());
			    jsonParser = new JsonParser();
			    jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
			    testRegressionSuiteID = jsonobj.get("id").getAsInt();
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void createHostNameSuite(String strHostName) {		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/suiteentry/"+testRegressionSuiteID+"?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    boolean blnTestSuiteFound = false;
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {		    			    	
				if(getTestSuiteName(jsonTestCase.getAsJsonObject().get("id").getAsString()).trim().equals(strHostName)) {
					testSuiteID = jsonTestCase.getAsJsonObject().get("id").getAsInt();
					blnTestSuiteFound = true;
					break;
				}				
			}
		    
		    if(!blnTestSuiteFound) {
		    	httpClient = HttpClientBuilder.create().build();
				HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID+"/suites?api-version=7.0");			
				postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
				postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
			    String strJson = "{"
			    					+ "\"suiteType\" : \"staticTestSuite\","
				    				+ "\"name\" : \""+strHostName+"\","
				    				+"\"parentSuite\":{"
				    					+ "\"id\" : "+testRegressionSuiteID
				    				+ "}"
					    			+"}";
			    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
			    response = httpClient.execute(postRequest);
			    strResponse = EntityUtils.toString(response.getEntity());
			    jsonParser = new JsonParser();
			    jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
			    testSuiteID = jsonobj.get("id").getAsInt();
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testCaseClone(String strTestCaseName) {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID+"/Suites/"+
				testSuiteID+"/TestCase?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);		    
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    boolean blnTestCaseFound = false;
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
				if(jsonTestCase.getAsJsonObject().get("workItem").getAsJsonObject().get("name")
						.getAsString().equals(strTestCaseName)) {
					blnTestCaseFound = true;
					break;
				}
				
			}
		    
		    if(!blnTestCaseFound) {
		    	httpClient = HttpClientBuilder.create().build();
		    	int intTestCaseID = getTestCaseIDFromMasterSuite(strTestCaseName);
				HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/testplan/TestCases/CloneTestCaseOperation?api-version=7.0");
				@SuppressWarnings("unused")
				String runName = "Run_"+new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss").format(new Date());
				postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
				postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
			    String strJson = "{"
			    		+ "    \"cloneOptions\":{"
			    		+ "        \"includeAttachments\": false"
			    		+ "    },"
			    		+ "    \"sourceTestPlan\":{"
			    		+ "        \"id\": "+testPlanID+""
			    		+ "    },"
			    		+ "    \"sourceTestSuite\":{"
			    		+ "        \"id\": "+testMasterSuiteID+""
			    		+ "    },"
			    		+ "    \"destinationTestPlan\":{"
			    		+ "        \"id\": "+testPlanID+""
			    		+ "    },"
			    		+ "    \"destinationTestSuite\":{"
			    		+ "        \"id\": "+testSuiteID+""
			    		+ "    },"
			    		+ "    \"testCaseIds\": ["+intTestCaseID+"]"
			    		+ "}";
			    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
			    response = httpClient.execute(postRequest);
			    /*strResponse = EntityUtils.toString(response.getEntity());
			    jsonParser = new JsonParser();
			    jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
			    runID = jsonobj.get("id").getAsInt();*/
		    }
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTestSuiteName(String strRegressionID) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID+"/suites/"+strRegressionID+"?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    return jsonobj.get("name").getAsString();		    		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public void getTestSuiteIDAPI() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID+"/suites?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);		    
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
				if(jsonTestCase.getAsJsonObject().get("name").getAsString().trim().equals(strTestSuiteName)) {
					testSuiteID = jsonTestCase.getAsJsonObject().get("id").getAsInt();
					break;
				}				
			}
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to create Run in tests tab of release 
	 */
	public void createRun(int intTestPointID) {		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/runs?api-version=5.0");
		String runName = "Run_"+new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss").format(new Date());
		try {
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"automated\" : \"true\","
			    				+ "\"name\" : \""+runName+"\","
			    				+ "\"outcome\" : \"inProgress\","
			    				+"\"plan\":{"
			    					+ "\"id\" : "+testPlanID
			    				+ "},"			    				
					    		+ "\"testPoint\": {"
					    		+ "  \"id\": \""+intTestPointID+"\""
					    		+ " } ,"
				    			+"}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    HttpResponse response = httpClient.execute(postRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
		    runID = jsonobj.get("id").getAsInt();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public int getRunID() {
		return runID;
	}
	public int getTestPlanID() {
		return testPlanID;
	}
	public int getTestSuiteID() {
		return testSuiteID;
	}
	
	public int getTestCaseIDFromMasterSuite(String strCurrentTestName) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID
				+"/suites/"+testMasterSuiteID+"/TestCase?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);		   
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
		    	JsonObject jsObjWorkItem = jsonTestCase.getAsJsonObject().get("workItem").getAsJsonObject();
				jsonTestCase.getAsJsonObject().get("testCase");
				if(jsObjWorkItem.get("name").getAsString().trim().equals(strCurrentTestName)) {
					testCaseID = jsObjWorkItem.get("id").getAsInt();
					return testCaseID;					
				}
			}
		    return 0;
		    
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public int getTestPoint(String strCurrentTestName) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(organizationURL+projectName+"/_apis/test/Plans/"+testPlanID
				+"/Suites/"+testSuiteID+"/points?api-version=7.0");		
		try {
			getRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			getRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			getRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    /*String strJson = "{"
		    					+ "\"automated\" : \"true\""			    				
		    				+ "}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));*/
		    HttpResponse response = httpClient.execute(getRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();		    
		    
		    for (JsonElement jsonTestCase : jsonobj.get("value").getAsJsonArray()) {
				jsonTestCase.getAsJsonObject().get("testCase");
				if(jsonTestCase.getAsJsonObject().get("testCase").getAsJsonObject().
						get("name").getAsString().trim().equals(strCurrentTestName)) {
					testCaseID = jsonTestCase.getAsJsonObject().get("testCase").getAsJsonObject().get("id").getAsInt();
					return jsonTestCase.getAsJsonObject().get("id").getAsInt();					
				}
			}
		    return 0;
		    
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}	
	
	/**
	 * Method to add Test results to run
	 */
	public void addResultsToRun(String strTestResult){
		try {
			//String json = new Gson().toJson(testDetails);
			HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpPatch patchRequest = new HttpPatch(organizationURL+projectName+"/_apis/test/Runs/"+runID
		    		+"/results?api-version=7.0");
		    patchRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
		    patchRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    patchRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "["
		    		+ "  {"
		    		+ "    \"outcome\": \""+strTestResult+"\""		    		
		    		+ "  }"
		    		+ "]";
		    patchRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    @SuppressWarnings("unused")
			HttpResponse response = httpClient.execute(patchRequest);
//		    StringEntity entity = new StringEntity(json);
//		    patchRequest.setEntity(entity);
//		    httpClient.execute(patchRequest);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int createResultsInRun(int intTestPointID, String strComputerHostName, String strTestResult){
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/Runs/"+runID
		    		+"/results?api-version=7.0");
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "["
		    		+ "  {"
		    		+ "    \"outcome\": \""+strTestResult+"\","
		    		+ "    \"state\": \"inProgress\","
		    		+ "    \"computerName\": \""+strComputerHostName+"\","
		    		+      "\"runBy\": {"
                    +             "\"displayName\": \"Babu Jayavelu\","
                     +            "\"id\": \"78eb12be-d84f-64c1-91b4-03def39c7299\""
                      +       "},"
		    		+ "    \"testCase\": {"
		    		+ "        \"id\": \""+testCaseID+"\""
		    		+ "    },"
		    		+ "    \"testPoint\": {"
		    		+ "        \"id\": \""+intTestPointID+"\""
		    		+ "    } ,"
		    		+ "    \"testCaseTitle\": \"Windows_Calculator\","
		    		+ "    \"testCaseRevision\": 1"
		    		+ "  }"
		    		+ "]";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    HttpResponse response = httpClient.execute(postRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
		    
		    for (JsonElement jsonTestResult : jsonobj.get("value").getAsJsonArray()) {
		    	return jsonTestResult.getAsJsonObject().get("id").getAsInt();				
			}
//		    String strID = jsonobj.get("id").getAsString();
//		    return Integer.parseInt(strID);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		return 0;
	}
	
	public void updateTestResultRunBy(int intTestPointID){
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpPatch postRequest = new HttpPatch(organizationURL+projectName+"/_apis/testplan/Plans/"+testPlanID
		    		+"/Suites/"+testSuiteID+"/TestPoint?api-version=7.0");
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "["
		                      +"{"
		                       +   "\"id\": "+intTestPointID+","
		                       +   "\"results\": {"
		                       +      "\"lastResultDetails\": {"
		                       +           "\"duration\": 0,"
		                        +          "\"runBy\": {"
		                         +             "\"displayName\": \"Babu Jayavelu\","
		                          +            "\"id\": \"78eb12be-d84f-64c1-91b4-03def39c7299\""
		                           +       "}"
		                       +       "}"
		                       +   "}"
		                   +   "}"
		                 +" ]";
//		    String strJson = "["
//		    		+ "  {"
//		    		+ "    \"id\": \""+intTestResultID+"\","
//		    		+ "    \"outcome\": \""+strStatus+"\""
//		    		//+ "    \"state\": \"Completed\""
//		    		+ "  }"
//		    		+ "]";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    HttpResponse response = httpClient.execute(postRequest);
		    @SuppressWarnings("unused")
			String strResponse = EntityUtils.toString(response.getEntity());
//		    JsonParser jsonParser = new JsonParser();
//		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
//		    for (JsonElement jsonTestResult : jsonobj.get("value").getAsJsonArray()) {
//		    	jsonTestResult.getAsJsonObject().get("id").getAsInt();				
//			}
		    
		    //jsonobj.get("id").getAsInt();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
	
	public void updateTestResult(int intTestResultID, String strStatus){
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpPatch postRequest = new HttpPatch(organizationURL+projectName+"/_apis/test/Runs/"+runID+"/results?api-version=7.0");
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "["
		    		+ "        {"
		    		+ "    \"id\": \""+intTestResultID+"\","
		    		+ "    \"outcome\": \""+strStatus+"\","
		    		+ "    \"state\": \"Completed\""
		    		+ "        }"
		    		+ "    ]";
//		    String strJson = "["
//		    		+ "  {"
//		    		+ "    \"id\": \""+intTestResultID+"\","
//		    		+ "    \"outcome\": \""+strStatus+"\""
//		    		//+ "    \"state\": \"Completed\""
//		    		+ "  }"
//		    		+ "]";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    HttpResponse response = httpClient.execute(postRequest);
		    String strResponse = EntityUtils.toString(response.getEntity());
		    JsonParser jsonParser = new JsonParser();
		    JsonObject jsonobj = jsonParser.parse(strResponse).getAsJsonObject();
		    for (JsonElement jsonTestResult : jsonobj.get("value").getAsJsonArray()) {
		    	jsonTestResult.getAsJsonObject().get("id").getAsInt();				
			}
		    
		    //jsonobj.get("id").getAsInt();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
	
	public void createAttachmentZIP(String strResultsPath, String strZIPFileName) {
		if(new File(System.getProperty("user.dir")+Util.getFileSeparator()+strZIPFileName+".zip").exists()) {
			new File(System.getProperty("user.dir")+Util.getFileSeparator()+strZIPFileName+".zip").delete();
		}
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(System.getProperty("user.dir")+Util.getFileSeparator()+strZIPFileName+".zip");
			ZipOutputStream zipOut = new ZipOutputStream(fos);
	        File fileToZip = new File(strResultsPath);
	        zipFile(fileToZip, fileToZip.getName(), zipOut);
	        zipOut.close();
	        fos.close();
		} catch (IOException e) {
			System.out.println("Error in creating zip file for results upload");
		}
	}
	
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
		try {
			if (fileToZip.isHidden()) {
	            return;
	        }
	        if (fileToZip.isDirectory()) {
	            if (fileName.endsWith("/")) {
	                zipOut.putNextEntry(new ZipEntry(fileName));
	                zipOut.closeEntry();
	            } else {
	                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
	                zipOut.closeEntry();
	            }
	            File[] children = fileToZip.listFiles();
	            for (File childFile : children) {
	                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
	            }
	            return;
	        }
	        FileInputStream fis = new FileInputStream(fileToZip);
	        ZipEntry zipEntry = new ZipEntry(fileName);
	        zipOut.putNextEntry(zipEntry);
	        byte[] bytes = new byte[1024];
	        int length;
	        while ((length = fis.read(bytes)) >= 0) {
	            zipOut.write(bytes, 0, length);
	        }
	        fis.close();
		} catch(Exception e) {
			System.out.println("Error in creating zip file for results upload");
		}
    }
	/**
	 * Method to update Run Status in Azure
	 */
	public void updateRun(String strStatus) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPatch patchRequest = new HttpPatch(organizationURL+projectName+"/_apis/test/runs/"+runID+"?api-version=5.0");
		try {
			patchRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			patchRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			patchRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"state\" : \"Completed\","
		    					+ "\"outcome\" : \""+strStatus+"\""
		    				+ "}";
		    patchRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    httpClient.execute(patchRequest);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTestPoint(int intTestPointID) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPatch patchRequest = new HttpPatch(organizationURL+projectName+"/_apis/test/runs/"+runID+"/results?api-version=7.0");
		try {
			patchRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			patchRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
			patchRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "[{\"id\":100000,{\"testPoint\": {"
		    					+ "\"id\": \""+intTestPointID+"\""
            				+"}}]";
		    patchRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    httpClient.execute(patchRequest);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Method to add Attachments to Run in Azure
	 * @param strAttachmentPath - path of the file which has to be attached
	 * @param strAttachmentName - Name of the Attachment in Azure
	 * @param strComments - Comments to be captured against attachment in Azure
	 */
	public void addAttachmentToRun(String strAttachmentPath, String strAttachmentName, String strComments) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/Runs/"+runID+"/"+ "attachments?api-version=5.0-preview.1");
		try {
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"Stream\" : \""+getStream(strAttachmentPath)+"\","
		    					+"\"fileName\" : \""+strAttachmentName+"\","
		    					+"\"comment\" : \""+strComments+"\","
		    					+"\"attachmentType\" : \"GeneralAttachment\""
		    				+"}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    httpClient.execute(postRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addAttachmentToResult(int intTestResultID, String strAttachmentPath, String strAttachmentName, String strComments) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(organizationURL+projectName+"/_apis/test/Runs/"+runID+"/Results/"+intTestResultID+"/attachments?api-version=7.0");
		try {
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MIMETYPEJSON);
			postRequest.setHeader(HttpHeaders.ACCEPT, MIMETYPEJSON);
		    postRequest.setHeader(AUTHORIZATIONHEADER, accessToken);
		    String strJson = "{"
		    					+ "\"Stream\" : \""+getStream(strAttachmentPath)+"\","
		    					+"\"fileName\" : \""+strAttachmentName+"\","
		    					+"\"comment\" : \""+strComments+"\","
		    					+"\"attachmentType\" : \"GeneralAttachment\""
		    				+"}";
		    postRequest.setEntity(new StringEntity(strJson, ContentType.APPLICATION_JSON));
		    httpClient.execute(postRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Method to get base64 encoded file stream
	 * @param strFilePath - path of the file for which encoded string is to be returned 
	 * @return - Base64 encoded file stream
	 */
	public String getStream(String strFilePath) {
		File file = new File(strFilePath);
		String strEncodedString = "";
		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
			strEncodedString = new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strEncodedString;
	}
}
