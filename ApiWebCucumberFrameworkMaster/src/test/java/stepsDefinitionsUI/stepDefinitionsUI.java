package stepsDefinitionsUI;

import base.BasePageUI;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import base.ExtentReportManager;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.testng.Assert;
import pageObjectsUI.*;
import resources.excelUtil;

public class stepDefinitionsUI extends BasePageUI {
    LoginPage login = new LoginPage();
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

    public stepDefinitionsUI() throws IOException {
        super();
    }

    @Given("Cierro la ventana emergente de Cookies")
    public void cierroLaVentanaEmergenteDeCookies() throws IOException {
        click2(landing.getCookiesBtn());
    }

    @Then("Navego a la pagina de Login")
    public void navegoALaPaginaDeLogin() throws IOException, InterruptedException {
        click(landing.getLoginPortalLink());
        Thread.sleep(3000);
    }

    @Then("Mando los datos del email: {string}")
    public void mandoLosDatosDelEmail(String email) throws IOException {
        sendText(login.getEmail(),email);
    }

    @And("Mando los datos de la contraseña: {string}")
    public void mandoLosDatosDeLaContraseña(String password) throws IOException {
        sendText(login.getPassword(),password);
    }

    @And("Le doy clic al botón de Login y cierro la ventana emergente")
    public void leDoyClicAlBotónDeLoginYCierroLaVentanaEmergente() throws IOException, InterruptedException {
        click(login.getLoginBtn());
        getDriver().switchTo().alert().accept();
        System.out.println("user has logged in");
        Thread.sleep(3000);
    }
    @Then("Navego a la tienda de prueba")
    public void navegoALaTiendaDePrueba() throws IOException {
        click2(landing.getTestStoreLink());
    }

    @And("Elijo el producto {int} y elijo si tiene la talla {string}")
    public void elijoElProductoYElijoSiTieneLaTalla(int num, String talla) throws IOException {
        if (num == 1) {
            click(shopHome.getProdOne());
            selectByText(shopProduct.getSizeOption(), talla);
        } else if (num == 2) {
            click(shopHome.getProdTwo());
            selectByText(shopProduct.getSizeOption(), talla);
        }
    }

    @Then("Incremento la cantidad de producto {int} vez y lo agrego al Carrito de compras")
    public void incrementoLaCantidadDeProductoVezYLoAgregoAlCarritoDeCompras(int num) throws IOException {
        for (int i = 0; i < num; i++) {
            click(shopProduct.getQuantIncrease());
        }
        ExtentCucumberAdapter.addTestStepLog("Se agrego la cantidad de pruducto deseado");
        shopProduct.getAddToCartBtn().click();
    }

    @And("Elijo continuar comprando")
    public void elijoContinuarComprando() throws IOException {
        click2(contentPanel.getContinueShopBtn());
        clickJS(shopProduct.getHomepageLink());
    }

    @And("Le doy al checkout")
    public void leDoyAlCheckout() throws IOException {
        contentPanel.getCheckoutBtn().click();
    }

    @Then("Elimino el segundo producto y espero que no este en la Pantalla")
    public void eliminoElSegundoProductoYEsperoQueNoEsteEnLaPantalla() throws IOException {
        cart.getDeleteItemTwo().click();
        waitForElementInvisible(cart.getDeleteItemTwo(), Duration.ofSeconds(30));
    }

    @And("Corroboro que el monto final es {string}")
    public void corroboroQueElMontoFinalEs(String monto) throws IOException {
        try {
            validateResults(getText(cart.getTotalAmount()), monto);
            ExtentCucumberAdapter.addTestStepLog("El monto final es el mismo que el esperado");

        } catch (AssertionError e) {
            ExtentCucumberAdapter.addTestStepLog("El monto esperado no coincide con el mostrado");
            ExtentCucumberAdapter.addTestStepLog("Se muestra: "+getText(cart.getTotalAmount()));
            Assert.fail("Se muestra: "+getText(cart.getTotalAmount()));
        }
    }

    @Then("Agrego el codigo de descuento {string} y continuo el proceso de compra")
    public void agregoElCodigoDeDescuentoYContinuoElProcesoDeCompra(String codigo) throws IOException {
        click(cart.getHavePromo());
        sendText(cart.getPromoTextbox(),codigo);
        click2(cart.getPromoAddBtn());
        ExtentCucumberAdapter.addTestStepLog("Se le dio al botón de agregar descuento");
        click2(cart.getProceedCheckoutBtn());
    }

    @Then("Lleno la data de la información personal del {string} de la hoja de excel {string}")
    public void llenoLaDataDeLaInformaciónPersonalDelDeLaHojaDeExcel(String data, String sheet) throws Exception {
        Map<String, String> excelDataMap = excelUtil.getData(data, sheet);

        if(Objects.equals(excelDataMap.get("genero"),"masculino")){
            Info.getGenderMr().click();
        }
        sendText(Info.getFirstNameField(),excelDataMap.get("nombre"));
        sendText(Info.getLastnameField(),excelDataMap.get("apellido"));
        sendText(Info.getEmailField(),excelDataMap.get("email"));
        click(Info.getTermsConditionsCheckbox());
        click(Info.getContinueBtn());
    }

    @Then("Lleno la data de entrega")
    public void llenoLaDataDeEntrega(DataTable table) throws IOException {

        List<Map<String, String>> envioList = table.asMaps(String.class, String.class);
        sendText(Delivery.getAddressField(),envioList.get(0).get("Calle"));
        sendText(Delivery.getCityField(),envioList.get(0).get("Ciudad"));
        selectByText(Delivery.getStateDropdown(),envioList.get(0).get("Estado"));
        sendText(Delivery.getPostcodeField(),envioList.get(0).get("CodigoPostal"));
        click(Delivery.getContinueBtn());
    }

    @And("Coloco el mensaje {string} para el delivery")
    public void colocoElMensajeParaElDelivery(String mensaje) throws IOException {
        sendText(shippingMethod.getDeliveryMsgTextbox(),mensaje);
        click(shippingMethod.getContinueBtn());
    }

    @Then("Coloco los datos de pago y completo la orden")
    public void colocoLosDatosDePagoYCompletoLaOrden() throws IOException {
        click(payment.getPayByCheckRadioBtn());
        click2(payment.getTermsConditionsCheckbox());
        clickJS(payment.getOrderBtn());
        ExtentCucumberAdapter.addTestStepLog("Se puso la orden de manera correcta");
    }


    @And("Navego a la pagina de login de la tienda")
    public void navegoALaPaginaDeLoginDeLaTienda() throws IOException, InterruptedException {
        click2(shopHome.getSingInBtn());
        Thread.sleep(2000);
        validateResults(getText(loginStore.getHeaderTxt()).toLowerCase(),"log in to your account");
    }

    @Then("Mando los datos del email: {string} de la tienda")
    public void mandoLosDatosDelEmailDeLaTienda(String email) throws IOException {
        sendText(loginStore.getEmail(),email);
    }

    @And("Mando los datos de la contraseña: {string} de la tienda")
    public void mandoLosDatosDeLaContraseñaDeLaTienda(String password) throws IOException {
        sendText(loginStore.getPassword(),password);
    }

    @And("Le doy clic al botón de Login de la tienda y corroboro que se hizo login")
    public void leDoyClicAlBotónDeLoginDeLaTiendaYCorroboroQueSeHizoLogin() throws IOException {
        click(loginStore.getLoginBtn());
        validateResults(getText(loginStore.getHeaderTxt()).toLowerCase(),"your account");
    }

    @And("Hago LogOut y corroboro que se hizo logout")
    public void hagoLogOutYCorroboroQueSeHizoLogout() throws IOException {
        click(loginStore.getLogOutBtn());
        validateResults(getText(loginStore.getHeaderTxt()).toLowerCase(),"log in to your account");
    }
}
