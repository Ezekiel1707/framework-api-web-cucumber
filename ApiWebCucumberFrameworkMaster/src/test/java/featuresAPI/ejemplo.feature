@TAG @Regresion
Feature: El detalle de la prueba
  Preconditions:
  *Lo que haga falta antes de la prueba
  Background:

    Given parteApi1
    Then parteApi2

  @TAG_1  @Screenshot
  Scenario: que se va a probar

    Then parteApi3
    And parteApi4


  @TAG_1  @Screenshot
  Scenario Outline: que se va a probar 2

    Then parteApi3 "<data>"
    And parteApi4

    Examples:
      | data     |
      | talento1 |