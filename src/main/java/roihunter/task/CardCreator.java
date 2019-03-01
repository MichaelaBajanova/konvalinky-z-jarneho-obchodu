package roihunter.task;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import roihunter.task.exceptions.ListNotFoundException;
import roihunter.task.exceptions.RequestFailException;
import roihunter.task.trello_resources.TrelloList;
import roihunter.task.trello_resources.TrelloResource;
import roihunter.task.utils.UrlParser;

import java.io.IOException;
import java.util.List;

/**
 * @author Michaela Bajanova (469166)
 */
public class CardCreator extends ResourceCreator {
    private APIResourcesManager resourcesManager;

    public CardCreator(WebDriver webDriver, APIResourcesManager resourcesManager) {
        super(webDriver);
        this.resourcesManager = resourcesManager;
    }

    private int getListPositionInBoard(List<TrelloResource> listsInBoard, String listName) throws ListNotFoundException {
        int listPosition = -1;
        for (TrelloResource list : listsInBoard) {
            if (list.getName().equals(listName)) {
                listPosition = listsInBoard.indexOf(list) + 1;
            }
        }
        if (listPosition == -1) {
            throw new ListNotFoundException("List with name " + listName + " does not exist");
        }
        return listPosition;
    }

    public void create(String inBoard, String inList, String cardName) throws UnirestException, IOException, ListNotFoundException, RequestFailException {
        if (inBoard == null || inList == null || cardName == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }

        if (inBoard.equals("") || inList.equals("") || cardName.equals("")) {
            throw new IllegalArgumentException("Parameter cannot be an empty string.");
        }

        TrelloManager.openBoard(super.getWebDriver(), inBoard);
        String boardId = UrlParser.getIdFromUrl(super.getWebDriver().getCurrentUrl());
        List<TrelloResource> listsInBoard = resourcesManager.getListsInBoard(boardId);
        int listPosition = getListPositionInBoard(listsInBoard, inList);

        WebElement addCardButton = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"board\"]/div[" + Integer.toString(listPosition) + "]/div[1]/a"));
        addCardButton.click();
        WebElement cardTitleField = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"board\"]/div["
                        + Integer.toString(listPosition)
                        + "]/div[1]/div[2]/div/div[1]/div/textarea"));
        cardTitleField.sendKeys(cardName);
        addCardButton = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"board\"]/div["
                        + Integer.toString(listPosition)
                        + "]/div[1]/div[2]/div/div[2]/div[1]/input"));
        addCardButton.click();
    }

    /**
     * Adds card description.
     * Has to be called when web driver is on board page.
     * @param description
     */
    public void addDescription(String inList, String description) throws IOException, UnirestException, ListNotFoundException, RequestFailException {
        String boardId = UrlParser.getIdFromUrl(super.getWebDriver().getCurrentUrl());
        List<TrelloResource> listsInBoard = resourcesManager.getListsInBoard(boardId);
        int listPosition = getListPositionInBoard(listsInBoard, inList);

        TrelloManager.openCardOptions(super.getWebDriver(), listPosition);

        WebElement descriptionTextField = super.getWebDriver()
        .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[5]/div[2]/div[2]/div/div/div[3]/textarea"));
        descriptionTextField.sendKeys(description);

        WebElement saveButton = super.getWebDriver()
        .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[5]/div[2]/div[2]/div/div/div[3]/div/input"));
        saveButton.click();

        TrelloManager.closeCardOptions(super.getWebDriver());
    }

    /**
     * Adds comment to card.
     * Has to be called when web driver is on board page.
     * @param comment
     */
    public void addComment(String inList, String comment) throws IOException, UnirestException, ListNotFoundException, RequestFailException {
        String boardId = UrlParser.getIdFromUrl(super.getWebDriver().getCurrentUrl());
        List<TrelloResource> listsInBoard = resourcesManager.getListsInBoard(boardId);
        int listPosition = getListPositionInBoard(listsInBoard, inList);

        TrelloManager.openCardOptions(super.getWebDriver(), listPosition);

        WebElement commentTextField = super.getWebDriver()
        .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[5]/div[9]/div[2]/form/div[1]/div/textarea"));
        commentTextField.sendKeys(comment);

        WebElement saveButton = super.getWebDriver()
        .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[5]/div[9]/div[2]/form/div[2]/input"));
        saveButton.click();

        TrelloManager.closeCardOptions(super.getWebDriver());
    }

    /**
     * Adds image attachment to card.
     * Has to be called when web driver is on board page.
     * @param pathToImage
     */
    public void addImageAttachment(String inList, String pathToImage) throws IOException, UnirestException, ListNotFoundException, RequestFailException {
        String boardId = UrlParser.getIdFromUrl(super.getWebDriver().getCurrentUrl());
        List<TrelloResource> listsInBoard = resourcesManager.getListsInBoard(boardId);
        int listPosition = getListPositionInBoard(listsInBoard, inList);

        TrelloManager.openCardOptions(super.getWebDriver(), listPosition);

        WebElement attachmentButton = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[6]/div[1]/div/a[5]"));
        attachmentButton.click();

        WebElement uploadFromComputerOption = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"classic\"]/div[4]/div/div[2]/div/div/div/ul/li[1]/form/input[3]"));
        uploadFromComputerOption.sendKeys(pathToImage);

        TrelloManager.closeCardOptions(super.getWebDriver());
    }

    public void addChecklist(String inList, String checklistName, List<String> checklistItems) throws IOException, UnirestException, ListNotFoundException, RequestFailException {
        String boardId = UrlParser.getIdFromUrl(super.getWebDriver().getCurrentUrl());
        List<TrelloResource> listsInBoard = resourcesManager.getListsInBoard(boardId);
        int listPosition = getListPositionInBoard(listsInBoard, inList);

        TrelloManager.openCardOptions(super.getWebDriver(), listPosition);

        WebElement checklistButton = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[6]/div[1]/div/a[3]/span[2]"));
        checklistButton.click();

        WebElement checklistNameTextField = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"id-checklist\"]"));
        checklistNameTextField.sendKeys(checklistName);

        WebElement addButton = super.getWebDriver().findElement(By.xpath("//input[@value='Add' and @type='submit']"));
        addButton.click();

        WebElement addItemTextField = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/div[5]/div[8]/div/div[4]/textarea"));
        addItemTextField.click();

        for (String item : checklistItems) {
            addItemTextField.sendKeys(item);
            addItemTextField.sendKeys(Keys.RETURN);
        }

        TrelloManager.closeCardOptions(super.getWebDriver());
    }
}
///Users/michaelab/Desktop/puppy.jpg
