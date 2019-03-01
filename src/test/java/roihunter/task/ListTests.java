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
import roihunter.task.trello_resources.TrelloResource;

import java.io.IOException;
import java.util.List;

/**
 * @author Michaela Bajanova (469166)
 */
public class ListTests {
    private EnvironmentManager manager;
    private WebDriver webDriver;
    private APIResourcesManager resourcesManager;
    private ListCreator listCreator;

    private String boardId;

    @BeforeTest
    public void init() throws UnirestException, IOException, RequestFailException {
        manager = new EnvironmentManager("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        webDriver = manager.getWebDriver();
        resourcesManager = new APIResourcesManager("your key",
                "your token");
        listCreator = new ListCreator(webDriver);

        TrelloManager.goToHomepage(webDriver);
        TrelloManager.logIn("your trello account", "your trello password", webDriver);
        //after logging in the web driver is on trello homepage

        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpClient);

        boardId = resourcesManager.createAPIBoard("api_board");
    }

    @Test(priority = 1)
    public void listIsCreatedInTheRightBoard() throws UnirestException, IOException, RequestFailException {
        List<TrelloResource> listsInBoardBefore = resourcesManager.getListsInBoard(boardId);
        listCreator.create("api_board", "My test list");
        TrelloManager.goToHomepage(webDriver);
        List<TrelloResource> listsInBoardAfter = resourcesManager.getListsInBoard(boardId);

        boolean isCreated = (listsInBoardAfter.size() - listsInBoardBefore.size() == 1);
        Assert.assertEquals(isCreated, true);
    }

    @Test(priority = 2)
    public void listIsCreatedWithCorrectName() throws UnirestException, IOException, RequestFailException {
        listCreator.create("api_board", "My amazing test list");
        TrelloManager.goToHomepage(webDriver);

        boolean isCreated = false;
        List<TrelloResource> listsInBoard = resourcesManager.getListsInBoard(boardId);
        for (TrelloResource list : listsInBoard) {
            if (list.getName().equals("My amazing test list")) {
                isCreated = true;
                break;
            }
        }
        Assert.assertEquals(isCreated, true);
    }

    @Test(priority = 3, expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenListParameterIsNull() {
        listCreator.create(null, "My test list");
    }

    @Test(priority = 4, expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenListParameterIsEmpty() {
        listCreator.create("api_board", "");
    }

    @AfterTest
    public void end() throws UnirestException, RequestFailException {
        resourcesManager.deleteBoard(boardId);
        manager.quitWebDriver();
    }
}
