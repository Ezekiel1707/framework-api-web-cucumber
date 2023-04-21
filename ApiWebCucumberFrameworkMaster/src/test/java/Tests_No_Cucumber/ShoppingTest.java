package Tests_No_Cucumber;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import base.Hooks;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.ExtentReportManager;
import pageObjectsUI.*;
import resources.excelUtil;

@Listeners(resources.Listeners.class)
public class ShoppingTest extends Hooks {

    LandingPage landing = new LandingPage();
    ShopHome shopHome = new ShopHome();
    ShopProductPage shopProduct = new ShopProductPage();
    ShopContent contentPanel = new ShopContent();
    ShoppingCart cart = new ShoppingCart();
    OrderFormInfo Info = new OrderFormInfo();
    OrderFormDelivery Delivery = new OrderFormDelivery();
    OrderFormShipping shippingMethod = new OrderFormShipping();
    OrderFormPayment payment = new OrderFormPayment();
    StoreLoginPage loginStore = new StoreLoginPage();

    public ShoppingTest() throws IOException {
        super();
    }

    @Test
    public void Basket_Test() throws Exception {

        ExtentReportManager.log("Comenzando Basket_Test...");
        ExtentReportManager.log("Cierro la ventana emergente de Cookies");
        if(cssElementIsPresent(".close-cookie-warning > span")){
            if(landing.getCookiesBtn().isDisplayed()){
                click2(landing.getCookiesBtn());
            }
        }

        ExtentReportManager.log("Navego a la tienda de prueba");
        click2(landing.getTestStoreLink());
        ExtentReportManager.pass("Se llego a la tienda de prueba");

        ExtentReportManager.log("Elijo el producto 1 y elijo si tiene la talla M");
        click(shopHome.getProdOne());
        selectByText(shopProduct.getSizeOption(), "M");
        ExtentReportManager.pass("Se elijio el producto deseado");

        ExtentReportManager.log("Incremento la cantidad de producto 1 vez y lo agrego al Carrito de compras");
        click(shopProduct.getQuantIncrease());
        shopProduct.getAddToCartBtn().click();
        ExtentReportManager.pass("Se agrego el producto al carrito");

        ExtentReportManager.log("Elijo continuar comprando");
        click2(contentPanel.getContinueShopBtn());
        clickJS(shopProduct.getHomepageLink());
        ExtentReportManager.pass("Se llego a la tienda de prueba");

        ExtentReportManager.log("Elijo el producto 2 y elijo si tiene la talla L");
        click(shopHome.getProdTwo());
        selectByText(shopProduct.getSizeOption(), "L");
        ExtentReportManager.pass("Se elijio el producto deseado");

        ExtentReportManager.log("Lo agrego al Carrito de compras");
        shopProduct.getAddToCartBtn().click();
        ExtentReportManager.pass("Se agrego el producto al carrito");

        ExtentReportManager.log("Le doy al checkout");
        contentPanel.getCheckoutBtn().click();
        ExtentReportManager.pass("Se Llego al checkout");

        ExtentReportManager.log("Elimino el segundo producto y espero que no este en la Pantalla");
        cart.getDeleteItemTwo().click();
        waitForElementInvisible(cart.getDeleteItemTwo(), Duration.ofSeconds(30));

        ExtentReportManager.log("Corroboro que el monto final es $45.24");
        try {
            validateResults(getText(cart.getTotalAmount()), "$45.24");
            ExtentReportManager.pass("El monto final es el mismo que el esperado");

        } catch (AssertionError e) {
            ExtentReportManager.fail("El monto esperado no coincide con el mostrado"
                    +"Se muestra: "+getText(cart.getTotalAmount()));
            Assert.fail("Se muestra: "+getText(cart.getTotalAmount()));
        }
        ExtentReportManager.log("regreso a la pagina principal de las pruebas");
        getDriver().get(getGlobalValue("url"));
    }

    @Test
    public void Complete_order() throws Exception {

        ExtentReportManager.log("Comenzando Basket_Test...");
        ExtentReportManager.log("Cierro la ventana emergente de Cookies");
        if(cssElementIsPresent(".close-cookie-warning > span")){
            if(landing.getCookiesBtn().isDisplayed()){
                click2(landing.getCookiesBtn());
                }
            }

        ExtentReportManager.log("Navego a la tienda de prueba");
        click2(landing.getTestStoreLink());
        ExtentReportManager.pass("Se llego a la tienda de prueba");

        ExtentReportManager.log("Elijo el producto 1 y elijo si tiene la talla M");
        click(shopHome.getProdOne());
        selectByText(shopProduct.getSizeOption(), "M");
        ExtentReportManager.pass("Se elijio el producto deseado");

        ExtentReportManager.log("Incremento la cantidad de producto 1 vez y lo agrego al Carrito de compras");
        click(shopProduct.getQuantIncrease());
        shopProduct.getAddToCartBtn().click();
        ExtentReportManager.pass("Se agrego el producto al carrito");

        ExtentReportManager.log("Le doy al checkout");
        contentPanel.getCheckoutBtn().click();
        ExtentReportManager.pass("Se Llego al checkout");

        ExtentReportManager.log("Se llego al carrito de compras");
        click(cart.getHavePromo());
        ExtentReportManager.log("Se agrega el codigo de promoción");
        sendText(cart.getPromoTextbox(),"20OFF");
        click2(cart.getPromoAddBtn());
        ExtentReportManager.pass("Se agrego el codigo de promoción");
        click2(cart.getProceedCheckoutBtn());

        ExtentReportManager.log("Lleno la data de la información personal del comprador1 de la hoja de excel compradores");
        Map<String, String> excelDataMap = excelUtil.getData("comprador1", "compradores");
        if(Objects.equals(excelDataMap.get("genero"),"masculino")){
            Info.getGenderMr().click();
        }
        sendText(Info.getFirstNameField(),excelDataMap.get("nombre"));
        sendText(Info.getLastnameField(),excelDataMap.get("apellido"));
        sendText(Info.getEmailField(),excelDataMap.get("email"));
        click(Info.getTermsConditionsCheckbox());
        click(Info.getContinueBtn());
        ExtentReportManager.pass("Se agrego toda la información del comprador");

        ExtentReportManager.log("LlenoLaDataDeEntrega");
        sendText(Delivery.getAddressField(),"123 Main Street");
        sendText(Delivery.getCityField(),"Houston");
        selectByText(Delivery.getStateDropdown(),"Texas");
        sendText(Delivery.getPostcodeField(),"77021");
        click(Delivery.getContinueBtn());
        ExtentReportManager.pass("Se agrego la información de entrega de manera efectiva");

        ExtentReportManager.log("Coloco el mensaje If I am not in, please leave my delivery on my porch. para el delivery");
        sendText(shippingMethod.getDeliveryMsgTextbox(),"If I am not in, please leave my delivery on my porch.");
        click(shippingMethod.getContinueBtn());
        ExtentReportManager.pass("Se agrego el mensaje de entrega");

        ExtentReportManager.log("Coloco los datos de pago y completo la orden");
        click(payment.getPayByCheckRadioBtn());
        click2(payment.getTermsConditionsCheckbox());
        clickJS(payment.getOrderBtn());
        ExtentReportManager.pass("Se puso la orden de manera correcta");

        ExtentReportManager.log("regreso a la pagina principal de las pruebas");
        getDriver().get(getGlobalValue("url"));
    }

    @Test
    public void loginStore() throws IOException, InterruptedException {

        ExtentReportManager.log("Comenzando Login del Store_Test...");
        ExtentReportManager.log("Cierro la ventana emergente de Cookies");
        if(cssElementIsPresent(".close-cookie-warning > span")){
            if(landing.getCookiesBtn().isDisplayed()){
                click2(landing.getCookiesBtn());
            }
        }

        ExtentReportManager.log("Navego a la tienda de prueba");
        click2(landing.getTestStoreLink());
        ExtentReportManager.pass("Se llego a la tienda de prueba");

        Thread.sleep(3000);

        click2(shopHome.getSingInBtn());
        ExtentReportManager.pass("Se Llega a la pagina de Login");
        Thread.sleep(3000);
        validateResults(getText(loginStore.getHeaderTxt()).toLowerCase(),"log in to your account");
        sendText(loginStore.getEmail(),"test17@test.com");
        ExtentReportManager.pass("Se coloco correctamente el correo electronico");
        sendText(loginStore.getPassword(),"123456789g");
        ExtentReportManager.pass("Se coloco correctamente la clave");
        click(loginStore.getLoginBtn());
        ExtentReportManager.pass("Se le dio clic al botón de login");
        validateResults(getText(loginStore.getHeaderTxt()).toLowerCase(),"your account");
        ExtentReportManager.pass("Se hizo login");
        click(loginStore.getLogOutBtn());
        validateResults(getText(loginStore.getHeaderTxt()).toLowerCase(),"log in to your account");
        ExtentReportManager.pass("Se hizo logout");
        Thread.sleep(3000);
    }
}
