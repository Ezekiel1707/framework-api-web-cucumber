package base;

import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import io.restassured.path.json.JsonPath;

public class BasePageAPI {

    public static String getGlobalValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream data = new FileInputStream(
                System.getProperty("user.dir") + "\\src\\main\\java\\resources\\global.properties");
        prop.load(data);
        return prop.getProperty(key);
    }

    public String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }

}
