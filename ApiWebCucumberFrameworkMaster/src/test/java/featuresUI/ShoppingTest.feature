@Shopping_Test @Regresion
Feature: Prueba para agregar y retirar un item de la canasta de la pagina de prueba
  Preconditions:
  *Ninguna
  Background:

    Given Cierro la ventana emergente de Cookies

  @Basket_Test  @Screenshot
  Scenario: agregar un producto y comprobar que el costos es el correcto

    Then Navego a la tienda de prueba
    And Elijo el producto 1 y elijo si tiene la talla "M"
    Then Incremento la cantidad de producto 1 vez y lo agrego al Carrito de compras
    And Elijo continuar comprando
    And Elijo el producto 2 y elijo si tiene la talla "L"
    Then Incremento la cantidad de producto 0 vez y lo agrego al Carrito de compras
    And Le doy al checkout
    Then Elimino el segundo producto y espero que no este en la Pantalla
    And Corroboro que el monto final es "$45.24"

  @Complete_order  @Screenshot
  Scenario Outline: Hacer los pasos completo en la tienda de prueba para hacer la compra del producto

    Then Navego a la tienda de prueba
    And Elijo el producto 1 y elijo si tiene la talla "M"
    Then Incremento la cantidad de producto 1 vez y lo agrego al Carrito de compras
    And Le doy al checkout
    Then Agrego el codigo de descuento "20OFF" y continuo el proceso de compra
    Then Lleno la data de la información personal del "<data>" de la hoja de excel "compradores"
    Then Lleno la data de entrega
    | Calle           | Ciudad   | Estado    | CodigoPostal|
    | 123 Main Street | Houston  | Texas     | 77021       |
    And Coloco el mensaje "If I am not in, please leave my delivery on my porch." para el delivery
    Then Coloco los datos de pago y completo la orden

    Examples:
    |data      |
    |comprador1|


  @Login_Store @Screenshot
  Scenario: Hacer los pasos del login

    Then Navego a la tienda de prueba
    And Navego a la pagina de login de la tienda
    Then Mando los datos del email: "test17@test.com" de la tienda
    And Mando los datos de la contraseña: "123456789g" de la tienda
    And Le doy clic al botón de Login de la tienda y corroboro que se hizo login
    And Hago LogOut y corroboro que se hizo logout
