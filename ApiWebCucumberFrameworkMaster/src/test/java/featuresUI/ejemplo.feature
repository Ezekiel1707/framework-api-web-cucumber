@TAG @Regresion
Feature: El detalle de la prueba
  Preconditions:
  *Lo que haga falta antes de la prueba
  Background:

    Given parte1
    Then parte2

  @TAG_1  @Screenshot
  Scenario: que se va a probar

    Then parte3
    And parte4


  @TAG_1  @Screenshot
  Scenario Outline: que se va a probar 2

    Then parte3 "<data>"
    And parte4

    Examples:
      | data     |
      | talento1 |