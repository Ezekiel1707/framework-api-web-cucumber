package Tests_No_Cucumber;

import base.BasePageAPI;
import base.ExtentReportManagerAPI;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import resources.ApiEndPointsEnum;
import resources.TestData.TestDataBuild;
import resources.excelUtil;
import stepsDefinitionsAPI.stepDefinitionsAPI;


import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static resources.FolderUtil.createFolder;

@Listeners(resources.ListenersAPI.class)
public class AddDeletePLace extends BasePageAPI {
    public static RequestSpecification req;
    RequestSpecification res;
    Response response;
    TestDataBuild data =new TestDataBuild();
    static String place_id;
    @Test
    public void AddPlace() throws Exception {
        ExtentReportManagerAPI.log("Comenzando Test de AddPlace...");

        if(req==null) {
            String dataFolder = createFolder("RequestSpec");
            PrintStream log = new PrintStream(
                    Files.newOutputStream(
                            Paths.get(dataFolder + "/RequestSpec.txt")));
            req=new RequestSpecBuilder().setBaseUri(getGlobalValue("apiUrl"))
                    .addQueryParam("key", "qaclick123")
                    .setContentType(ContentType.JSON).build()
                    .filter(RequestLoggingFilter.logRequestTo(log))
                    .filter(ResponseLoggingFilter.logResponseTo(log));
        }
        ExtentReportManagerAPI.log("Add Place Payload");
        String dataFolder = createFolder("Place");
        PrintStream log = new PrintStream(
                Files.newOutputStream(
                        Paths.get(dataFolder + "/Place.txt")));
        Map<String, String> excelDataMap = excelUtil.getData("place1", "apidata");
        res=given().spec(req)
                .body(data.addPlacePayLoad(excelDataMap.get("name"),excelDataMap.get("language"),excelDataMap.get("address")))
                .filter(RequestLoggingFilter.logRequestTo(log))
                .filter(ResponseLoggingFilter.logResponseTo(log));

        ExtentReportManagerAPI.log("user calls addPlaceAPI with POST http request");
        ApiEndPointsEnum resourceAPI=ApiEndPointsEnum.valueOf("addPlaceAPI");

        ExtentReportManagerAPI.log("the API call got success with status code 200");
        response =res.when().post(resourceAPI.getResource());
        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManagerAPI.pass("El status code de esta solicitud es: " + response.getStatusCode());

        ExtentReportManagerAPI.log("status in response body is OK");
        Assert.assertEquals(getJsonPath(response, "status"), "OK");
        ExtentReportManagerAPI.pass("La respuesta obtenida es: " + getJsonPath(response, "status"));

        ExtentReportManagerAPI.log("scope in response body is APP");
        Assert.assertEquals(getJsonPath(response, "scope"), "APP");
        ExtentReportManagerAPI.pass("La respuesta obtenida es: " + getJsonPath(response, "scope"));

        ExtentReportManagerAPI.log("verify place_Id created maps using getPlaceAPI");
        place_id=getJsonPath(response,"place_id");
        ExtentReportManagerAPI.log("El lugar enviado es: " + place_id);

        String dataFolder2 = createFolder("VerifyPLace");
        PrintStream log2 = new PrintStream(
                Files.newOutputStream(
                        Paths.get(dataFolder2 + "/VerifyPLace.txt")));
        res=given().spec(req).queryParam("place_id",place_id)
                .filter(RequestLoggingFilter.logRequestTo(log2))
                .filter(ResponseLoggingFilter.logResponseTo(log2));

        ApiEndPointsEnum resourceAPI2=ApiEndPointsEnum.valueOf("getPlaceAPI");
        response =res.when().get(resourceAPI2.getResource());
        String actualName=getJsonPath(response,"name");
        ExtentReportManagerAPI.log("El lugar obtenido usando GET es: " + actualName);
        validateResults(actualName,excelDataMap.get("name"));
        ExtentReportManagerAPI.pass("Se valida que el valor enviado con POST es el mismo que el recuperado con GET");
    }

    @Test
    public void DeletePlace() throws Exception {
        ExtentReportManagerAPI.log("Comenzando Test de DeletePlace...");
        if(place_id==null){
            AddPlace();
        }
        String dataFolder = createFolder("DeletePlace");
        PrintStream log = new PrintStream(
                Files.newOutputStream(
                        Paths.get(dataFolder + "/DeletePLace.txt")));
        res =given().spec(req).body(data.deletePlacePayload(place_id))
                .filter(RequestLoggingFilter.logRequestTo(log))
                .filter(ResponseLoggingFilter.logResponseTo(log));
        ExtentReportManagerAPI.log("Se Envia el Payload para borrar un lugar");

        ExtentReportManagerAPI.log("user calls deletePlaceAPI with POST http request");
        ApiEndPointsEnum resourceAPI=ApiEndPointsEnum.valueOf("deletePlaceAPI");

        ExtentReportManagerAPI.log("the API call got success with status code 200");
        response =res.when().post(resourceAPI.getResource());
        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManagerAPI.pass("El status code de esta solicitud es: " + response.getStatusCode());

        ExtentReportManagerAPI.log("status in response body is OK");
        Assert.assertEquals(getJsonPath(response, "status"), "OK");
        ExtentReportManagerAPI.pass("La respuesta obtenida es: " + getJsonPath(response, "status"));
        
    }
}
