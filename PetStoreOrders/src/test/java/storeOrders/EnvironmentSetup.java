package storeOrders;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.*;
import utils.TestUtils;
import java.io.IOException;


public class EnvironmentSetup {

    private static Logger log = LogManager.getLogger(EnvironmentSetup.class.getName());
    public static String BaseURL,postOrders,getOrders,deleteOrders;

    //To load the values before the test cases run
    @BeforeSuite(groups = { "functest" })
    public static void Env_setup() throws IOException {
       log.info("Env_setup function");
       TestUtils.loadProperties();
       BaseURL=TestUtils.ReadProperty("BaseURL");
       postOrders=TestUtils.ReadProperty("postOrders");
       getOrders=TestUtils.ReadProperty("getOrders");
       deleteOrders=TestUtils.ReadProperty("deleteOrders");       
       TestUtils.createOutputFolder();
    }

}
