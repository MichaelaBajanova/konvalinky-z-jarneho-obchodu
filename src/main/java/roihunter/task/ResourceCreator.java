package roihunter.task;

import org.openqa.selenium.WebDriver;

/**
 * @author Michaela Bajanova (469166)
 */
public abstract class ResourceCreator {
    private WebDriver webDriver;

    public ResourceCreator(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}
