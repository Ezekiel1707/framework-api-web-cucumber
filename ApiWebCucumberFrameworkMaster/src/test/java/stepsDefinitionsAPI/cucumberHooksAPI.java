package stepsDefinitionsAPI;

import io.cucumber.java.Before;

import java.io.IOException;

public class cucumberHooksAPI {

    @Before("@DeletePlace")
    public void beforeScenario() throws IOException {

        stepDefinitionsAPI m =new stepDefinitionsAPI();
        if(stepDefinitionsAPI.place_id==null)
        {
            m.add_Place_Payload_with("Shetty", "French", "Asia");
            m.user_calls_with_http_request("AddPlaceAPI", "POST");
            m.verify_place_Id_created_maps_to_using("Shetty", "getPlaceAPI");
        }
    }
}
