package roihunter.task;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import roihunter.task.exceptions.RequestFailException;
import roihunter.task.trello_resources.TrelloBoard;

import java.io.IOException;

/**
 * @author Michaela Bajanova (469166)
 */
public class BoardTests {
    private EnvironmentManager manager;
    private WebDriver webDriver;
    private APIResourcesManager resourcesManager;
    private BoardCreator boardCreator;

    private String boardId;

    @BeforeTest
    public void init() {
        manager = new EnvironmentManager("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        webDriver = manager.getWebDriver();
        resourcesManager = new APIResourcesManager("your key",
                "your token");
        boardCreator = new BoardCreator(webDriver);

        TrelloManager.goToHomepage(webDriver);
        TrelloManager.logIn("your trello account", "your trello password", webDriver);
        //after logging in the web driver is on trello homepage

        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpClient);
    }

    @Test(priority = 1)
    public void boardIsCreatedWithCorrectName() throws IOException, UnirestException, RequestFailException {
        boardId = boardCreator.create("My test board");
        TrelloManager.goToHomepage(webDriver);

        TrelloBoard board = resourcesManager.getBoard(boardId);
        Assert.assertEquals(board.getName(), "My test board");
    }

    @Test(priority = 2, expectedExceptions = IllegalArgumentException.class)
    public void exceptionThrownWhenBoardNameIsNull() {
        boardCreator.create(null);
    }

    @Test(priority = 3, expectedExceptions = IllegalArgumentException.class)
    public void exceptionThrownWhenBoardNameIsEmpty() {
        boardCreator.create("");
    }

    @AfterTest
    public void end() throws UnirestException, RequestFailException {
        resourcesManager.deleteBoard(boardId);
        manager.quitWebDriver();
    }
}
