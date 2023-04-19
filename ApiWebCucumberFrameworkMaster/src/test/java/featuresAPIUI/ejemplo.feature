@TAG @Regresion
Feature: El detalle de la prueba
  Preconditions:
  *Lo que haga falta antes de la prueba
  Background:

    Given parteApiUI1
    Then parteApiUI2

  @TAG_1  @Screenshot
  Scenario: que se va a probar

    Then parteApiUI3
    And parteApiUI4


  @TAG_1  @Screenshot
  Scenario Outline: que se va a probar 2

    Then parteApiUI3 "<data>"
    And parteApiUI4

    Examples:
      | data     |
      | talento1 |