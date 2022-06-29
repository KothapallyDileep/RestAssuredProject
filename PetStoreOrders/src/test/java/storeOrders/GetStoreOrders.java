package storeOrders;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.log4j.LogManager;
import io.qameta.allure.*;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import utils.RestAssuredRequestFilter;
import utils.TestUtils;
import utils.URL;
import static utils.TestUtils.*;
import static storeOrders.EnvironmentSetup.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("GetStore-112")
public class GetStoreOrders {
	private static Logger log = LogManager.getLogger(GetStoreOrders.class.getName());
	String URI;
	@BeforeClass(groups = { "functest"})
    public void setup() {
		log.info("Setting up prerequisite for test execution");
		URI = URL.getEndPoint(BaseURL,getOrders);
    }
    //To Check With the Specification	
	ResponseSpecification checkStatusCodeAndContentType = new ResponseSpecBuilder().expectStatusCode(200)
			.expectContentType(ContentType.JSON).build();
    
	@Feature("GetStore-TestCase1")
	@Story("To Get the Order Data")
	@Test(groups = { "functest"})
    public void getOrder_SuccessResponse() {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        
        String orderId = "1";
        
        Response response = given().
        		filter(new RestAssuredRequestFilter()).
				relaxedHTTPSValidation().
				pathParam("orderId", orderId).
        when().
        		get(URI).
        then().
        		log().ifValidationFails().
                assertThat().spec(checkStatusCodeAndContentType).
                body(matchesJsonSchemaInClasspath("StoreOrders.json")).
                extract().response();
        
		log.debug(response);
        //Response Validation
        Assert.assertEquals("placed", response.jsonPath().getString("status"));
        Assert.assertEquals("true", response.jsonPath().getString("complete"));
        log.info("End of The "+URL.testName+" rest call");
    }
    
    
	@Feature("GetStore-TestCase2")
	@Story("To Get the Order Data with Invalid ID -1 for invalid data response")
	@Test(groups = { "functest"})
    public void getOrder_Id_InvalidResponse() {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        
        String orderId = "-1";
		Response response = given().
								filter(new RestAssuredRequestFilter()).
								relaxedHTTPSValidation().
								pathParam("orderId", orderId).
							when()
								.get(URI).
							then()
								.extract().response();
		//Header Validation        
        TestUtils.verifyStatusCode(response, 400);
        TestUtils.verifyContentType(response,"application/json");
        log.info("End of The "+URL.testName+" rest call");
    }
    
	@Feature("GetStore-TestCase3")
	@Story("To Get the Order Data with not available OrderID for data not found response")
	@Test(groups = { "functest"})
    public void getOrder_NotFoundResponse() {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        String orderId = "9999999";
		Response response = given().
								filter(new RestAssuredRequestFilter()).
								relaxedHTTPSValidation().
								pathParam("orderId", orderId).
							when()
								.get(URI).
							then()
								.extract().response();
        log.debug(response);
        //Header Validation
        TestUtils.verifyStatusCode(response, 404);
        TestUtils.verifyContentType(response,"application/json");
        
        //Response Validation
        Assert.assertEquals("error",response.jsonPath().getString("type"));
        Assert.assertEquals("Order not found",response.jsonPath().getString("message"));
        log.info("End of The "+URL.testName+" rest call");
    }
    
}
