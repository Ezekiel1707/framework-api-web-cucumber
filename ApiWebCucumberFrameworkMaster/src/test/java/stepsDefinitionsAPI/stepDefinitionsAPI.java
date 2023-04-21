package stepsDefinitionsAPI;

import base.BasePageAPI;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import resources.ApiEndPointsEnum;
import resources.TestData.TestDataBuild;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static resources.FolderUtil.createFolder;

public class stepDefinitionsAPI extends BasePageAPI {
    public static RequestSpecification req;
    RequestSpecification res;
    ResponseSpecification resSpec;
    Response response;
    TestDataBuild data =new TestDataBuild();
    static String place_id;

    @Given("Dando los datos de prueba de la pagina {string} y {string} armamos el Request basico")
    public void dandoLosDatosDePruebaDeLaPaginaYArmamosElRequestBasico(String data1, String data2) throws IOException {
        if(req==null) {
            String dataFolder = createFolder("RequestSpec");
            PrintStream log = new PrintStream(
                    Files.newOutputStream(
                            Paths.get(dataFolder + "/RequestSpec.txt")));
            req=new RequestSpecBuilder().setBaseUri(getGlobalValue("apiUrl"))
                    .addQueryParam(data1, data2)
                    .setContentType(ContentType.JSON).build()
                    .filter(RequestLoggingFilter.logRequestTo(log))
                    .filter(ResponseLoggingFilter.logResponseTo(log));
        }
    }

    @Then("Add Place Payload with {string} {string} {string}")
    public void add_Place_Payload_with(String name, String language, String address) throws IOException {
        String dataFolder = createFolder("Place");
        PrintStream log = new PrintStream(
                Files.newOutputStream(
                        Paths.get(dataFolder + "/Place.txt")));
        res=given().spec(req)
                .body(data.addPlacePayLoad(name,language,address))
                .filter(RequestLoggingFilter.logRequestTo(log))
                .filter(ResponseLoggingFilter.logResponseTo(log));
    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String method) throws IOException {

        ApiEndPointsEnum resourceAPI=ApiEndPointsEnum.valueOf(resource);
        resSpec =new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();

        if(method.equalsIgnoreCase("POST"))
            response =res.when().post(resourceAPI.getResource());
        else if(method.equalsIgnoreCase("GET"))
            response =res.when().get(resourceAPI.getResource());

    }

    @Then("the API call got success with status code {int}")
    public void the_API_call_got_success_with_status_code(Integer int1) {

        Assert.assertEquals(response.getStatusCode(), int1);
        ExtentCucumberAdapter
                .addTestStepLog("El status code de esta solicitud es: " + response.getStatusCode());
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String keyValue, String Expectedvalue) {

        Assert.assertEquals(getJsonPath(response, keyValue), Expectedvalue);
        ExtentCucumberAdapter.addTestStepLog("La respuesta obtenida es: " + getJsonPath(response, keyValue));
    }

    @Then("verify place_Id created maps to {string} using {string}")
    public void verify_place_Id_created_maps_to_using(String expectedName, String resource) throws IOException {

        place_id=getJsonPath(response,"place_id");
        ExtentCucumberAdapter.addTestStepLog("El lugar enviado es: " + place_id);
        String dataFolder = createFolder("VerifyPLace");
        PrintStream log = new PrintStream(
                Files.newOutputStream(
                        Paths.get(dataFolder + "/VerifyPLace.txt")));
        res=given().spec(req).queryParam("place_id",place_id)
                .filter(RequestLoggingFilter.logRequestTo(log))
                .filter(ResponseLoggingFilter.logResponseTo(log));
        user_calls_with_http_request(resource,"GET");
        String actualName=getJsonPath(response,"name");
        ExtentCucumberAdapter.addTestStepLog("El lugar obtenido usando GET es: " + actualName);
        validateResults(actualName,expectedName);
        ExtentCucumberAdapter.addTestStepLog("Se valida que el valor enviado con POST es el mismo que el recuperado con GET");
    }


    @Given("DeletePlace Payload")
    public void deleteplace_Payload() throws IOException {
        String dataFolder = createFolder("DeletePlace");
        PrintStream log = new PrintStream(
                Files.newOutputStream(
                        Paths.get(dataFolder + "/DeletePLace.txt")));
        res =given().spec(req).body(data.deletePlacePayload(place_id))
                .filter(RequestLoggingFilter.logRequestTo(log))
                .filter(ResponseLoggingFilter.logResponseTo(log));
        ExtentCucumberAdapter.addTestStepLog("Se Envia el Payload para borrar un lugar");
    }


}
