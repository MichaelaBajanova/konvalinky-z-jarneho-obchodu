package roihunter.task;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import roihunter.task.utils.UrlParser;

/**
 * @author Michaela Bajanova (469166)
 */
public class BoardCreator extends ResourceCreator {

    public BoardCreator(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * creates a new board via web driver
     * @param boardName - selected name for the board
     * @return - returns id of the new board
     */
    public String create(String boardName) {
        if (boardName == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        if (boardName.equals("")) {
            throw new IllegalArgumentException("Board name cannot be empty.");
        }

        TrelloManager.openBoardsMenuInHeader(super.getWebDriver());

        WebElement createBoardButton = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"boards-drawer\"]/div/div[2]/div[2]/div/a[1]"));
        createBoardButton.click();

        WebElement boardNameTextField = super.getWebDriver()
                .findElement(By.xpath("//input[@placeholder='Add board title']"));
        boardNameTextField.sendKeys(boardName);

        createBoardButton = super.getWebDriver()
                .findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/div/form/button/span[2]"));
        createBoardButton.click();

        WebDriverWait wait = new WebDriverWait(super.getWebDriver(), 30);
        wait.until(ExpectedConditions.urlContains("/b/"));

        return UrlParser.getIdFromUrl(super.getWebDriver().getCurrentUrl());
    }
}
