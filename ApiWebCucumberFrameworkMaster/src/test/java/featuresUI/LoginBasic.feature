@LoginBasic @Regresion
Feature: Prueba para login
  Preconditions:
  *Ninguna
  Background:

    Given Cierro la ventana emergente de Cookies

  @Screenshot
  Scenario: Hacer los pasos del login

    Then Navego a la pagina de Login
    Then Mando los datos del email: "test17@test.com"
    And Mando los datos de la contraseña: "123456789g"
    And Le doy clic al botón de Login y cierro la ventana emergente