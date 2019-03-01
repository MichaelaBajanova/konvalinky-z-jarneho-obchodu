package roihunter.task;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Michaela Bajanova (469166)
 */
public class TrelloManager {

    public static void logIn(String email, String password, WebDriver driver) {
        WebElement logInButton = driver.findElement(By.xpath("//a[@href='/login?returnUrl=%2F']"));
        logInButton.click();

        WebElement emailTextField =  driver.findElement(By.xpath("//input[@type='email']"));
        emailTextField.sendKeys(email);

        WebElement passwordTextField = driver.findElement(By.xpath("//input[@type='password']"));
        passwordTextField.sendKeys(password);

        logInButton = driver.findElement(By.xpath("//input[@type='submit']"));
        logInButton.click();
    }

    public static void goToHomepage(WebDriver driver) {
        driver.get("https://trello.com/");
    }

    /**
     * Opens boards menu in header of the trello homepage.
     * Has to be called when current web page is trello homepage.
     * @param driver
     */
    public static void openBoardsMenuInHeader(WebDriver driver) {
        WebElement boardsMenuButton = driver.findElement(By.xpath("//span[text()='Boards']//parent::a"));
        boardsMenuButton.click();
    }

    private static void findBoardInMenuAndOpen(WebDriver driver, String boardName) {
        WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"boards-drawer\"]/div/div[2]/input"));
        searchBar.sendKeys(boardName);

        WebElement foundBoardButton = driver
                .findElement(By.xpath("//*[@id=\"boards-drawer\"]/div/div[2]/div[1]/div/ul/li/div/a/span[2]/span"));
        foundBoardButton.click();
    }

    /**
     * Opens selected board.
     * Has to be called when current web page is trello homepage.
     * @param driver
     * @param boardName
     */
    public static void openBoard(WebDriver driver, String boardName) {
        openBoardsMenuInHeader(driver);
        findBoardInMenuAndOpen(driver, boardName);
    }

    /**
     * Opens back of the card.
     * Has to be called when web driver is on board page.
     * @param driver
     * @param listPosition
     */
    public static void openCardOptions(WebDriver driver, int listPosition) {
        WebElement card = driver.findElement(By.xpath("//*[@id=\"board\"]/div["
                + Integer.toString(listPosition)
                + "]/div[1]/div[2]/a"));
        card.click();
    }

    /**
     * Closes back of the card.
     * Back of the card has to be already opened to use this method.
     * @param driver
     */
    public static void closeCardOptions(WebDriver driver) {
        WebElement closeButton = driver.findElement(By.xpath("//*[@id=\"classic\"]/div[3]/div/div/a"));
        closeButton.sendKeys(Keys.RETURN);
    }
}
