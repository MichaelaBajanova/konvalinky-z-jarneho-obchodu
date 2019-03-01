package roihunter.task;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * @author Michaela Bajanova (469166)
 */
public class EnvironmentManager {
   private WebDriver webDriver;

   public EnvironmentManager(String key, String value) {
       initWebDriver(key, value);
   }

   public WebDriver getWebDriver() {
       return webDriver;
   }

   private void initWebDriver(String key, String value) {
       System.setProperty(key, value);
       webDriver = new ChromeDriver();
       webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
   }

   public void quitWebDriver() {
       webDriver.quit();
   }
}
