package utils;


import org.apache.commons.logging.Log;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.commons.logging.LogFactory;
import utils.URL;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import static utils.TestUtils.ReadProperty;

public class RestAssuredRequestFilter implements Filter {
        private static final Log log = LogFactory.getLog(RestAssuredRequestFilter.class);

        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
            Response response = ctx.next(requestSpec, responseSpec);
            if (response.statusCode() != 200) {
                log.error(requestSpec.getMethod() + " " + requestSpec.getURI() + " => " +
                        response.getStatusCode() + " " + response.getStatusLine());
            }
            LocalDate myObj = LocalDate.now(); // Create a date object
            String rootdir=System.getProperty("user.dir");//To get the root dir
            String outputPath=rootdir+ReadProperty("outputPath")+myObj;
            File fileDir=new File(outputPath);
            if(fileDir.isDirectory())
            {
                fileDir.delete();
                fileDir.mkdirs();
            } else {
                fileDir.mkdirs();
            }
            FileWriter os = null;
            try {
                //String outputfilepathForTestcase=outputPath+"\\"+testcaseName+"_"+tablename+".txt";
                os = new FileWriter(new File(fileDir+"\\"+ URL.testName+"\\"+ URL.testName+".txt"));
                String str=requestSpec.getMethod() + " " + requestSpec.getURI()+" \n Request Headers =>" +requestSpec.getHeaders() +" \n Request Body =>" + requestSpec.getBody() + "\n Response Status => " +
                        response.getStatusCode() + " " + response.getStatusLine() + " \n Response Body => " + response.getBody().prettyPrint();
                os.write(str);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.info(requestSpec.getMethod() + " " + requestSpec.getURI() + " \n Request Body =>" + requestSpec.getBody() + "\n Response Status => " +
                    response.getStatusCode() + " " + response.getStatusLine() + " \n Response Body => " + response.getBody().prettyPrint());
            return response;
        }
    }

