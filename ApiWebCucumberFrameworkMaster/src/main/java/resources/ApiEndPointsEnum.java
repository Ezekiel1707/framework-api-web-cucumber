package resources;

public enum ApiEndPointsEnum {

    //agregar aqui los enpoint de la apiUrl

    addPlaceAPI("/maps/api/place/add/json"),
    getPlaceAPI("/maps/api/place/get/json"),
    deletePlaceAPI("/maps/api/place/delete/json");
    private String resource;

    ApiEndPointsEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
