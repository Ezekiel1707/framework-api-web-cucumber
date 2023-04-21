@AddDeletePlace
Feature: Validar Place API's
  Preconditions:
  *Ninguna
  Background:

    Given Dando los datos de prueba de la pagina "key" y "qaclick123" armamos el Request basico

  @AddPlace @Regression
  Scenario Outline: Verificar que Agregar un lugar funciona usando el endpoint

    Then Add Place Payload with "<name>" "<language>" "<address>"
    When user calls "addPlaceAPI" with "POST" http request
    Then the API call got success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    And verify place_Id created maps to "<name>" using "getPlaceAPI"

    Examples:
      |name 	 | language |address		   |
      |AAhouse |  English |World cross center|
#	|BBhouse | Spanish  |Sea cross center  |

  @DeletePlace @Regression
  Scenario: Verificar que Delete PLace funciona correctamente

    Given DeletePlace Payload
    When user calls "deletePlaceAPI" with "POST" http request
    Then the API call got success with status code 200
    And "status" in response body is "OK"