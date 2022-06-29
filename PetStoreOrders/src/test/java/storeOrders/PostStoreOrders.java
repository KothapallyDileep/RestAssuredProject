package storeOrders;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.apache.log4j.LogManager;
import io.qameta.allure.*;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import model.StoreInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.DataDrivenStoreUtils;
import utils.RestAssuredRequestFilter;
import utils.TestUtils;
import utils.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static storeOrders.EnvironmentSetup.*;
import static utils.TestUtils.*;

@Epic("PostStore-111")
public class PostStoreOrders extends DataDrivenStoreUtils {
	
	private static Logger log = LogManager.getLogger(PostStoreOrders.class.getName());
	String URI;
	@BeforeClass(groups = { "functest"})
    public void setup() {
		log.info("Setting up prerequisite for test execution");
		URI = URL.getEndPoint(BaseURL,postOrders);
    }
	
	//To Check With the Specification	
  	ResponseSpecification checkStatusCodeAndContentType = new ResponseSpecBuilder().expectStatusCode(200)
  			.expectContentType(ContentType.JSON).build();
    
  	@Feature("PostOrder-TestCase1")
	@Story("To Post Store the Order Data")
  	@Parameters({ "expectedSLA" })	
	@Test(groups = { "functest"})
    public void postOrderPlaced_SuccessResponse(long expectedSLA) throws JsonProcessingException {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        String jsonInString = getStoreJsonString("1", "1", "1", "2022-03-30T20:53:08.651Z", "placed", "true");
        Response response = given().
        		filter(new RestAssuredRequestFilter()).
        		relaxedHTTPSValidation().
        		contentType(ContentType.JSON).
                body(jsonInString).			
        when().
        		post(URI).
        then().
        		log().ifValidationFails().
                assertThat().spec(checkStatusCodeAndContentType).
                body(matchesJsonSchemaInClasspath("StoreOrders.json")).
                extract().response();
        
        long sla = response.getTime();
        if(sla > expectedSLA) {
        	Assert.fail("Response SLA is Gerater : "+sla+" than the Expected :"+expectedSLA);
        }
        
        //Response Validation
        Assert.assertEquals("1",response.jsonPath().getString("id"));
        Assert.assertEquals("1",response.jsonPath().getString("petId"));
        Assert.assertEquals("1",response.jsonPath().getString("quantity"));
        Assert.assertEquals("placed",response.jsonPath().getString("status"));
        Assert.assertEquals("true",response.jsonPath().getString("complete"));
        log.info("End of The "+URL.testName+" rest call");
    }


  	@Feature("PostOrder-TestCase2")
	@Story("To Post Store the Order with Invalid Data")
	@Test(groups = { "functest"})
    public void postOrder_InvalidStatusOrderResponse() throws JsonProcessingException {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);
        String jsonInString = getStoreJsonString("1", "1", "1", "Invalid", "placed", "true");		
		Response response = given().
        		filter(new RestAssuredRequestFilter()).
        		relaxedHTTPSValidation().
        		contentType(ContentType.JSON).
                body(jsonInString).			
        when().
        		post(URI).
        then().
        		log().ifValidationFails().
                extract().response();

        //Header Validation        
        TestUtils.verifyStatusCode(response, 400);
        TestUtils.verifyContentType(response,"application/json");
        log.info("End of The "+URL.testName+" rest call");
    }
  	
  	
  	@Feature("PostOrder-TestCase3")
	@Story("To Post multiple Store Order Data")
	@Test(groups = { "datadriven"}, dataProvider = "DataForStoreOrder")
    public void postStoreOrderTestData(String id, String petId, String quantity, String shipDate, String status, String complete) throws JsonProcessingException  {
    	URL.testName=Thread.currentThread().getStackTrace()[1].getMethodName()+id;
        log.info("Start of The "+URL.testName+" rest call");
        createTestCaseOutputFolder(URL.testName);      
        String jsonInString = getStoreJsonString(id, petId, quantity, shipDate, status, complete);				
		Response response = given().
        		filter(new RestAssuredRequestFilter()).
        		relaxedHTTPSValidation().
        		contentType(ContentType.JSON).
                body(jsonInString).			
        when().
        		post(URI).
        then().
        		log().ifValidationFails().
                assertThat().spec(checkStatusCodeAndContentType).
                body(matchesJsonSchemaInClasspath("StoreOrders.json")).
                extract().response();
        
        //Response Validation
        Assert.assertEquals(id,response.jsonPath().getString("id"));
        Assert.assertEquals(petId,response.jsonPath().getString("petId"));
        Assert.assertEquals(quantity,response.jsonPath().getString("quantity"));
        Assert.assertEquals(status,response.jsonPath().getString("status"));
        Assert.assertEquals(complete.toLowerCase(),response.jsonPath().getString("complete").toLowerCase());
        log.info("End of The "+URL.testName+" rest call");
    }

  	
  	// Returns StoreOrder JSON String
	private String getStoreJsonString(String id, String petId, String quantity, String shipDate, String status,
			String complete) throws JsonProcessingException {
		StoreInfo si = new StoreInfo();
		si.setId(Integer.parseInt(id));
		si.setPetId(Integer.parseInt(petId));
		si.setQuantity(Integer.parseInt(quantity));
		si.setShipDate(shipDate);
		si.setStatus(status);
		si.setComplete(Boolean.valueOf(complete));	
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(si);
		return jsonInString;
	}


}
