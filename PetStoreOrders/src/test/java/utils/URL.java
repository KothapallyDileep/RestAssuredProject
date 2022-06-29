package utils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class URL {
    private static Logger log = LogManager.getLogger(URL.class.getName());
    public static String testName;
    //to get the endpoint (URI + resource )
    public static String getEndPoint(String URL,String resource){
        log.info("URI End Point : " + URL + resource);
        return URL + resource;
    }
}
