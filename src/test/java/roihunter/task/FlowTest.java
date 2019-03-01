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
import roihunter.task.trello_resources.TrelloBoard;
import roihunter.task.trello_resources.TrelloResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michaela Bajanova (469166)
 */

public class FlowTest {
    private APIResourcesManager resourcesManager;
    private EnvironmentManager manager;
    private WebDriver webDriver;
    private BoardCreator boardCreator;
    private ListCreator listCreator;
    private CardCreator cardCreator;

    private String boardId;

    @BeforeTest
    public void init() {
        manager = new EnvironmentManager("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        webDriver = manager.getWebDriver();
        resourcesManager = new APIResourcesManager("7ff37dbe95b9d233eddf6966da5308c4",
                "3c86c6a00ad0ae17e693442d49583d777fbcdf19f409a1035d3b3e81d4ebefa9");
        boardCreator = new BoardCreator(webDriver);
        listCreator = new ListCreator(webDriver);
        cardCreator = new CardCreator(webDriver, resourcesManager);

        TrelloManager.goToHomepage(webDriver);
        TrelloManager.logIn("MichaelaBajan@gmail.com", "jebgaw-zumSy9-supjeg", webDriver);
        //after logging in the web driver is on trello homepage

        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        Unirest.setHttpClient(httpClient);
    }

    @Test
    public void flowTest() throws UnirestException, IOException, ListNotFoundException, RequestFailException {
        boardId = boardCreator.create("My test board");
        TrelloManager.goToHomepage(webDriver);

        listCreator.create("My test board", "My test list");
        TrelloManager.goToHomepage(webDriver);

        List<String> items = new ArrayList<String>();
        items.add("First item");
        items.add("Second item");
        items.add("Third item");
        cardCreator.create("My test board", "My test list", "My test card");
        cardCreator.addDescription("My test list", "My test description");
        cardCreator.addComment("My test list", "My test comment");
        cardCreator.addChecklist("My test list", "My test checklist", items);
        cardCreator.addImageAttachment("My test list", "/Users/michaelab/Desktop/puppy.jpg");
        TrelloManager.goToHomepage(webDriver);

        TrelloBoard board = resourcesManager.getBoard(boardId);
        Assert.assertEquals(board.getName(), "My test board");

        List<TrelloResource> lists = resourcesManager.getListsInBoard(boardId);
        TrelloResource listCreated = checkResourceUsingName(lists, "My test list");
        Assert.assertNotNull(listCreated);

        List<TrelloResource> cards = resourcesManager.getCardsInList(listCreated.getId());
        TrelloResource cardCreated = checkResourceUsingName(cards, "My test card");
        Assert.assertNotNull(cardCreated);

        List<TrelloResource> attachments = resourcesManager.getAttachmentsInCard(cardCreated.getId());
        TrelloResource attachmentCreated = checkResourceUsingName(attachments, "puppy.jpg");
        Assert.assertNotNull(attachmentCreated);

        List<TrelloResource> checklists = resourcesManager.getChecklistsInCard(cardCreated.getId());
        TrelloResource checklistCreated = checkResourceUsingName(checklists, "My test checklist");
        Assert.assertNotNull(checklistCreated);

        List<TrelloResource> checklistItems = resourcesManager
                .getChecklistItemsInCard(checklistCreated.getId());
        TrelloResource firstChecklistItem = checkResourceUsingName(checklistItems, "First item");
        TrelloResource secondChecklistItem = checkResourceUsingName(checklistItems, "Second item");
        TrelloResource thirdChecklistItem = checkResourceUsingName(checklistItems, "Third item");
        Assert.assertNotNull(firstChecklistItem);
        Assert.assertNotNull(secondChecklistItem);
        Assert.assertNotNull(thirdChecklistItem);
    }

    private TrelloResource checkResourceUsingName(List<TrelloResource> resources, String name) {
        for (TrelloResource resource : resources) {
            if (resource.getName().equals(name)) {
                return resource;
            }
        }
        return null;
    }

//    public void deleteBoards() throws RequestFailException, UnirestException {
//        for (String boardId : boardIds) {
//            resourcesManager.deleteBoard(boardId);
//        }
//    }

//    @AfterGroups(groups = {"board-test", "list-test", "card-test"})
//    public void cleanUp() throws RequestFailException, UnirestException {
//        resourcesManager.deleteBoard(boardId);
//    }

    @AfterTest
    public void end() throws UnirestException, RequestFailException {
        resourcesManager.deleteBoard(boardId);
        manager.quitWebDriver();
    }
}
