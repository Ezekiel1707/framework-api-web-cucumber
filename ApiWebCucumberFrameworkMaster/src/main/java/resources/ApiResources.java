package resources;

public enum ApiResources {

    //agregar aqui los enpoint de la apiUrl

    ejemplo1("/users/about"),
    ejemplo2("/users/{user-id}/filters"),
    ejemplo3("/oauth2/v1/token");
    private String resource;

    ApiResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
