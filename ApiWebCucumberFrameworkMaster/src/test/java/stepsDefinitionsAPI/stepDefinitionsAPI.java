package stepsDefinitionsAPI;

import base.BasePageAPI;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static io.restassured.RestAssured.*;
import static resources.FolderUtil.createFolder;

public class stepDefinitionsAPI extends BasePageAPI {

    @Given("parteApi{int}")
    public void parteapi(int arg0) {
    }

    @Then("parteApi{int} {string}")
    public void parteapi(int arg0, String arg1) {
    }


    @Given("parteApiUI{int}")
    public void parteapiui(int arg0) {

    }

    @Then("parteApiUI{int} {string}")
    public void parteapiui(int arg0, String arg1) {

    }
}
