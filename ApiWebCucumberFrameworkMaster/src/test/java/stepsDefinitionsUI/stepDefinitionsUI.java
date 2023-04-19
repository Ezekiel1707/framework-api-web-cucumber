package stepsDefinitionsUI;

import base.BasePageUI;
import java.io.IOException;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

public class stepDefinitionsUI extends BasePageUI {

    public stepDefinitionsUI() throws IOException {
        super();
    }

    @Given("parte{int}")
    public void parte1() {
    }

    @Then("parte{int}")
    public void parte(int arg0) {
    }

    @Then("parte{int} {string}")
    public void parte(int arg0, String arg1) {
    }
}
