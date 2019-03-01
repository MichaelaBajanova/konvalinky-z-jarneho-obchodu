package roihunter.task;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.WebDriver;
import roihunter.task.exceptions.ListNotFoundException;
import roihunter.task.exceptions.RequestFailException;
import roihunter.task.trello_resources.TrelloBoard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michaela Bajanova (469166)
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, UnirestException, IOException, RequestFailException, ListNotFoundException {
//        if (args.length != 4) {
//            throw new IllegalArgumentException("Invalid number of arguments.");
//        }
//        String email = args[0];
//        String password = args[1];
//        String key = args[2];
//        String token = args[3];
//
//        EnvironmentManager manager = new EnvironmentManager("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
//        WebDriver webDriver = manager.getWebDriver();
//
//        TrelloManager.goToHomepage(webDriver);
//        TrelloManager.logIn(email, password, webDriver);
//
//        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
//
//        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
//        Unirest.setHttpClient(httpClient);
//
//        APIResourcesManager resourcesManager = new APIResourcesManager(key, token);
//        String boardId = resourcesManager.createAPIBoard("my_api_board");
//        resourcesManager.crateAPIList(boardId, "my_api_list");
//
//        TrelloBoard board = resourcesManager.getBoard(boardId);

//        BoardCreator boardCreator = new BoardCreator(webDriver);
//        ListCreator listCreator = new ListCreator(webDriver);
//
//        boardCreator.create("My selenium board");
//        TrelloManager.goToHomepage(webDriver);
//        listCreator.create("My selenium board", "My selenium list");
//        TrelloManager.goToHomepage(webDriver);

//        CardCreator cardCreator = new CardCreator(webDriver, resourcesManager);
//        cardCreator.create("my_api_board", "my_api_list", "My selenium card");
//
//        List<String> items = new ArrayList<String>();
//        items.add("First item");
//        items.add("Second item");
//        items.add("Third item");
//        cardCreator.addDescription("my_api_list","My selenium description");
//        cardCreator.addComment("my_api_list", "My selenium comment");
//        cardCreator.addChecklist("my_api_list", "My selenium checklist", items);
//        cardCreator.addImageAttachment("my_api_list", "/Users/michaelab/Desktop/puppy.jpg");
    }
}
