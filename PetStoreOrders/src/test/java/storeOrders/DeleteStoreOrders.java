package storeOrders;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.log4j.LogManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import utils.RestAssuredRequestFilter;
import utils.TestUtils;
import utils.URL;
import static storeOrders.EnvironmentSetup.*;
import static io.restassured.RestAssured.given;
import static utils.TestUtils.*;

@Epic("DeleteStore-113")
public class DeleteStoreOrders {

	private static Logger log = LogManager.getLogger(DeleteStoreOrders.class.getName());
	String URI;
	@BeforeClass(groups = { "functest"})
    public void setup() {
		log.info("Setting up prerequisite for test execution");
		URI = URL.getEndPoint(BaseURL,deleteOrders);
    }
		
    @Feature("DeleteStore-TestCase1")
	@Story("To Delete the Order Data")
	@Test(groups = { "functest"})
    public void deleteOrder_SuccessResponse() throws JsonProcessingException {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        String orderId = "1";
		Response response = given().
								filter(new RestAssuredRequestFilter()).
								relaxedHTTPSValidation().
								pathParam("orderId", orderId).
							when()
								.delete(URI).
							then()
								.extract().response();
        log.debug(response);
        //Header Validation
        if(response.getStatusCode() == 200 || response.getStatusCode() == 201 || response.getStatusCode() == 204) {
        	assert true;
        } else {
        	log.info("Expected Response Code(200 or 201 or 204) is not matching with the Actual : "+response.getStatusCode());
        	assert false;
        }
        TestUtils.verifyContentType(response,"application/json");
        log.info("End of The "+URL.testName+" rest call");
    }
    
    @Feature("DeleteStore-TestCase2")
	@Story("To Delete the Order Data with Invalid ID for invalid data response")
	@Test(groups = { "functest"})
    public void deleteOrder_InvalidResponse() throws JsonProcessingException {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        String orderId = "-1";
		Response response = given().
								filter(new RestAssuredRequestFilter()).
								relaxedHTTPSValidation().
								pathParam("orderId", orderId).
							when()
								.delete(URI).
							then()
								.extract().response();
        log.debug(response);	
        //Header Validation
        TestUtils.verifyStatusCode(response, 400);
        TestUtils.verifyContentType(response,"application/json");
        log.info("End of The "+URL.testName+" rest call");
    }
    
    @Feature("DeleteStore-TestCase3")
	@Story("To Delete the Order Data with not available OrderID for data not found response")
	@Test(groups = { "functest"})
    public void deleteOrder_NotFoundResponse() throws JsonProcessingException {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        String orderId = "9999999";
		Response response = given().
								filter(new RestAssuredRequestFilter()).
								relaxedHTTPSValidation().
								pathParam("orderId", orderId).
							when()
								.delete(URI).
							then()
								.extract().response();
        log.debug(response);
        //Header Validation
        TestUtils.verifyStatusCode(response, 404);
        TestUtils.verifyContentType(response,"application/json");
        
        //Response Validation
        Assert.assertEquals("unknown",response.jsonPath().getString("type"));
        Assert.assertEquals("Order Not Found",response.jsonPath().getString("message"));
        log.info("End of The "+URL.testName+" rest call");
    }
}
