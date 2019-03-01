package roihunter.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import roihunter.task.exceptions.RequestFailException;
import roihunter.task.trello_resources.TrelloAttachment;
import roihunter.task.trello_resources.TrelloBoard;
import roihunter.task.trello_resources.TrelloCard;
import roihunter.task.trello_resources.TrelloChecklist;
import roihunter.task.trello_resources.TrelloChecklistItem;
import roihunter.task.trello_resources.TrelloList;
import roihunter.task.trello_resources.TrelloResource;

import java.io.IOException;
import java.util.List;

/**
 * @author Michaela Bajanova (469166)
 */
public class APIResourcesManager {
    private ObjectMapper objectMapper;
    private String key;
    private String token;

    public APIResourcesManager(String key, String token) {
        this.key = key;
        this.token = token;
        this.objectMapper = new ObjectMapper();
    }

    private void checkStatus(HttpResponse<JsonNode> response) throws RequestFailException {
        if (response.getStatus() < 200 || response.getStatus() > 299) {
            throw new RequestFailException("Response status not 200.");
        }
    }

    public String createAPIBoard(String name) throws UnirestException, IOException, RequestFailException {
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/boards/")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .queryString("name", name)
                .asJson();
        checkStatus(response);

        TrelloBoard board = objectMapper.readValue(response.getBody().toString(), TrelloBoard.class);
        return board.getId();
    }

    public String crateAPIList(String boardId, String title) throws UnirestException, IOException, RequestFailException {
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/lists")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .queryString("name", title)
                .queryString("idBoard", boardId)
                .asJson();
        checkStatus(response);

        TrelloList list = objectMapper.readValue(response.getBody().toString(), TrelloList.class);
        return list.getId();
    }

    public TrelloBoard getBoard(String boardId) throws UnirestException, RequestFailException, IOException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" + boardId)
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        checkStatus(response);

        TrelloBoard board = objectMapper.readValue(response.getBody().toString(), TrelloBoard.class);
        return board;
    }

    public List<TrelloResource> getListsInBoard(String boardId) throws UnirestException, IOException, RequestFailException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" + boardId + "/lists")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        checkStatus(response);

        List<TrelloResource> listsInBoard = objectMapper
                .readValue(response.getBody().toString(), new TypeReference<List<TrelloList>>(){});
        return listsInBoard;
    }

    public List<TrelloResource> getCardsInList(String listId) throws UnirestException, RequestFailException, IOException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/lists/" + listId + "/cards")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        checkStatus(response);

        List<TrelloResource> cardsInList = objectMapper
                .readValue(response.getBody().toString(), new TypeReference<List<TrelloCard>>(){});
        return cardsInList;
    }

    public List<TrelloResource> getAttachmentsInCard(String cardId) throws RequestFailException, UnirestException, IOException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/cards/" + cardId + "/attachments")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .queryString("fields", "all")
                .queryString("filter", "false")
                .asJson();
        checkStatus(response);

        List<TrelloResource> attachmentsInCard = objectMapper
                .readValue(response.getBody().toString(), new TypeReference<List<TrelloAttachment>>(){});
        return attachmentsInCard;
    }

    public List<TrelloResource> getChecklistsInCard(String cardId) throws RequestFailException, UnirestException, IOException {
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/cards/" + cardId + "/checklists")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        checkStatus(response);

        List<TrelloResource> checklistsInCard = objectMapper
                .readValue(response.getBody().toString(), new TypeReference<List<TrelloChecklist>>(){});
        return checklistsInCard;
    }

    public List<TrelloResource> getChecklistItemsInCard(String checklistId) throws RequestFailException, UnirestException, IOException {
        HttpResponse<JsonNode> response = Unirest
                .get("https://api.trello.com/1/checklists/" + checklistId + "/checkItems")
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        checkStatus(response);

        List<TrelloResource> checklistItemsInCard = objectMapper
                .readValue(response.getBody().toString(), new TypeReference<List<TrelloChecklistItem>>(){});
        return checklistItemsInCard;
    }

    public void deleteBoard(String boardId) throws UnirestException, RequestFailException {
        HttpResponse<JsonNode> response = Unirest.delete("https://api.trello.com/1/boards/" + boardId)
                .header("accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        checkStatus(response);
    }
}
