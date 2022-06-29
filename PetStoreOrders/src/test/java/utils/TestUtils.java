package utils;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import io.restassured.response.Response;
import org.testng.Assert;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static java.lang.System.getProperty;

public class TestUtils {
    private static Logger log = LogManager.getLogger(TestUtils.class.getName());
    static FileInputStream fs;
    static Properties property;
    public static final String env=System.getProperty("testEnvironment");
    public static String outputPath;

    //To load the properties file based on env variable
    public static void loadProperties() throws IOException {
        fs=new FileInputStream(getProperty("user.dir")+"/resources/properties/"+env+".properties");
        property=new Properties();
        property.load(fs);
        log.info("In loadProperties method");
    }

    //To read the property from properties file
    public static String ReadProperty(String key){
        log.info("In ReadProperty method");
        log.info(property.getProperty(key));
        return property.getProperty(key);
    }

    //To get response as string
    public static String getResposeString(Response response){
        log.info("Converting Response to String");
        String strResponse = response.getBody().asString();
        log.debug(strResponse);
        return strResponse;
    }

    //To get the status code of the api
    public static int getStatusCode(Response response){
        log.info("Getting Response Code");
        int statusCode = response.getStatusCode();
        log.info(statusCode);
        return statusCode;
    }

    //To get the content type of the api
    public static String getContentType(Response response){
        log.info("Getting Response Code");
        String statusCode = response.contentType();
        log.info(statusCode);
        return statusCode;
    }

    //TO verify the status code
    public static void verifyStatusCode(Response response, int status){
        Assert.assertEquals(TestUtils.getStatusCode(response), status);
    }

    //To verify the contentType
    public static void verifyContentType(Response response, String contentType){
        Assert.assertEquals(TestUtils.getContentType(response), contentType);
    }    

    public static void createOutputFolder(){
        LocalDate myObj = LocalDate.now(); // Create a date object
        String rootdir= getProperty("user.dir");//To get the root dir
        outputPath=rootdir+ReadProperty("outputPath")+myObj;
        File fileDir=new File(outputPath);
        if(fileDir.isDirectory())
        {
            fileDir.delete();
        }
        fileDir.mkdirs();
    }

    public static void createTestCaseOutputFolder(String testName){
        LocalDate myObj = LocalDate.now(); // Create a date object
        String rootdir= getProperty("user.dir");//To get the root dir
        outputPath=rootdir+ReadProperty("outputPath")+myObj+"\\"+testName;
        File fileDir=new File(outputPath);
        if(fileDir.isDirectory())
        {
            fileDir.delete();
        }
        fileDir.mkdirs();
    }

}
