package roihunter.task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Michaela Bajanova (469166)
 */
public class ListCreator extends ResourceCreator {

    public ListCreator(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * creates a new list via web driver in a selected board
     * @param inBoard - board the list will be created in
     * @param listName - selected name of the list
     * @return - returns id of the new list
     */
    public void create(String inBoard, String listName) {
        if (inBoard == null || listName == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        if (inBoard.equals("") || listName.equals("")) {
            throw new IllegalArgumentException("Parameter cannot be an empty string.");
        }

        TrelloManager.openBoard(super.getWebDriver(), inBoard);

        WebElement addListButton = super.getWebDriver()
                .findElement(By.xpath("//span[contains(text(), 'Add') and contains(text(), 'list')]"));
        addListButton.click();

        WebElement listTitleField = super.getWebDriver()
                .findElement(By.xpath("//input[@placeholder='Enter list title...']"));
        listTitleField.sendKeys(listName);

        addListButton = super.getWebDriver().findElement(By.xpath("//input[@value='Add List']"));
        addListButton.click();
    }
}
