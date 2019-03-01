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
import roihunter.task.exceptions.ListNotFoundException;
import roihunter.task.exceptions.RequestFailException;
import roihunter.task.trello_resources.TrelloCard;
import roihunter.task.trello_resources.TrelloResource;

import java.io.IOException;
import java.util.List;

/**
 * @author Michaela Bajanova (469166)
 */
public class CardTests {
    private EnvironmentManager manager;
    private WebDriver webDriver;
    private APIResourcesManager resourcesManager;
    private CardCreator cardCreator;

    private String boardId;
    private String listId;

    @BeforeTest
    public void init() throws UnirestException, IOException, RequestFailException {
        manager = new EnvironmentManager("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        webDriver = manager.getWebDriver();
        resourcesManager = new APIResourcesManager("7ff37dbe95b9d233eddf6966da5308c4",
                "3c86c6a00ad0ae17e693442d49583d777fbcdf19f409a1035d3b3e81d4ebefa9");
        cardCreator = new CardCreator(webDriver, resourcesManager);

        TrelloManager.goToHomepage(webDriver);
        TrelloManager.logIn("MichaelaBajan@gmail.com", "jebgaw-zumSy9-supjeg", webDriver);
        //after logging in the web driver is on trello homepage

        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpClient);

        boardId = resourcesManager.createAPIBoard("api_board");
        listId = resourcesManager.crateAPIList(boardId, "api_list");
    }

    @Test(priority = 1)
    public void cardIsCreatedInTheRightList() throws RequestFailException, UnirestException, ListNotFoundException, IOException {
        List<TrelloResource> cardsInListBefore = resourcesManager.getCardsInList(listId);
        cardCreator.create("api_board", "api_list", "My test card");
        TrelloManager.goToHomepage(webDriver);
        List<TrelloResource> cardsInListAfter = resourcesManager.getCardsInList(listId);
        boolean isCreated = (cardsInListAfter.size() - cardsInListBefore.size() == 1);
        Assert.assertEquals(isCreated, true);
    }

    @Test(priority = 2)
    public void cardIsCreatedWithCorrectName() throws RequestFailException, UnirestException, ListNotFoundException, IOException {
        cardCreator.create("api_board", "api_list", "My boring test card");
        TrelloManager.goToHomepage(webDriver);
        List<TrelloResource> cardsInList = resourcesManager.getCardsInList(listId);
        boolean isCreated = false;
        for (TrelloResource card : cardsInList) {
            if (card.getName().equals("My boring test card")) {
                isCreated = true;
                break;
            }
        }
        Assert.assertEquals(isCreated, true);
    }

    @Test(priority = 3, expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenCardParameterIsNull() throws RequestFailException, UnirestException, ListNotFoundException, IOException {
        cardCreator.create("api_board", null, "My test card");
    }

    @Test(priority = 4, expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrownWhenCardParameterIsEmpty() throws RequestFailException, UnirestException, ListNotFoundException, IOException {
        cardCreator.create("", "", "My test card");
    }

    @AfterTest
    public void end() throws UnirestException, RequestFailException {
        resourcesManager.deleteBoard(boardId);
        manager.quitWebDriver();
    }
}
