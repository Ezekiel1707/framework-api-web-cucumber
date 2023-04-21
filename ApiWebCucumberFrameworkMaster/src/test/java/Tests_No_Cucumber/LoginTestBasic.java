package Tests_No_Cucumber;

import base.ExtentReportManager;
import base.Hooks;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObjectsUI.LandingPage;
import pageObjectsUI.LoginPage;

import java.io.IOException;

@Listeners(resources.Listeners.class)
public class LoginTestBasic extends Hooks {

    LandingPage landing = new LandingPage();
    LoginPage login = new LoginPage();

    public LoginTestBasic() throws IOException {
        super();
    }
    @Test
    public void login() throws IOException, InterruptedException {

        ExtentReportManager.log("Comenzando Login...");
        ExtentReportManager.log("Cierro la ventana emergente de Cookies");
        if(cssElementIsPresent(".close-cookie-warning > span")){
            if(landing.getCookiesBtn().isDisplayed()){
                click2(landing.getCookiesBtn());
            }
        }
        ExtentReportManager.log("Navego al área de login");
        click(landing.getLoginPortalLink());
        ExtentReportManager.pass("Se llego a la pagina de Login");

        Thread.sleep(3000);
        sendText(login.getEmail(),"test17@test.com");
        ExtentReportManager.pass("Se coloco correctamente el correo electronico");
        sendText(login.getPassword(),"123456789g");
        ExtentReportManager.pass("Se coloco correctamente la clave");
        click(login.getLoginBtn());
        ExtentReportManager.pass("Se le dio clic al botón de login");
        getDriver().switchTo().alert().accept();
        System.out.println("user has logged in");
        Thread.sleep(3000);
    }
}
