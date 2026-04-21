package base;

import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import java.nio.file.Path;

public class basePageAPI {
    private static Properties globalProperties;

    public static String getGlobalValue(String key) throws IOException {
        if (globalProperties == null) {
            globalProperties = new Properties();
            Path propPath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "resources",
                    "global.properties");
            try (FileInputStream data = new FileInputStream(propPath.toFile())) {
                globalProperties.load(data);
            }
        }
        return globalProperties.getProperty(key);
    }

    public String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }

    public void validateResults(String actual, String expected) {
        Assert.assertEquals(actual, expected);
    }

    public static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    }

}
