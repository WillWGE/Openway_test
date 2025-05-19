//import required dependencies
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PeriplusCartTest {

    private WebDriver driver;
    private WebDriverWait wait;

    //  User config
    private final String PERIPLUS_URL = "https://www.periplus.com/";
    private final String USER_EMAIL = "willgunawan88@gmail.com";
    private final String USER_PASSWORD = "admin123";
    private final String PRODUCT_TO_SEARCH = "The Lord of the Rings";


    //Element Locators 
    //Login Page Elements
    private final By loginIconLocator = By.xpath("//a[contains(@href,'/login')]"); 
    private final By emailFieldLocator = By.id("email"); 
    private final By passwordFieldLocator = By.id("password"); 
    private final By loginButtonLocator = By.xpath("//button[contains(text(),'Login') or @type='submit']"); 

    //Search and Product Page Elements
    private final By searchBarLocator = By.name("q"); 
    private final By searchSubmitButtonLocator = By.xpath("//button[@type='submit' and contains(@class,'search')]"); 
    private final By firstProductLinkLocator = By.xpath("(//div[contains(@class,'product-item')]/a)[1]"); 
    private final By addToCartButtonLocator = By.xpath("//button[contains(text(),'Add to Cart')]"); 

    
    @BeforeClass
    public void setUp() {
    
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Start browser maximized
        options.addArguments("--disable-notifications"); // Disable browser notifications that might interfere

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // wait time
    }

   @Test(priority = 1, description = "Login to Periplus website")
    public void loginTest() {
        driver.get(PERIPLUS_URL);

        // Click on the login icon/link to navigate to the login page
        try {
            WebElement loginIcon = wait.until(ExpectedConditions.elementToBeClickable(loginIconLocator));
            loginIcon.click();
            System.out.println("Clicked login icon.");
        } catch (Exception e) {
            System.out.println("Login icon not found or login form might be directly accessible. Proceeding...");
            // If login form is directly on a page or modal, this click might not be needed.
            // Adjust based on actual website flow.
        }


        // Wait for email field to be visible and enter email
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(emailFieldLocator));
        emailField.sendKeys(USER_EMAIL);
        System.out.println("Entered email: " + USER_EMAIL);

        // Enter password
        WebElement passwordField = driver.findElement(passwordFieldLocator);
        passwordField.sendKeys(USER_PASSWORD);
        System.out.println("Entered password.");

        // Click login button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginButton.click();
        System.out.println("Clicked login button.");

        // Add a verification step for successful login 
        System.out.println("Login attempt submitted, waiting to load the page");
         try {
            Thread.sleep(5000); // Static wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test(priority = 2, description = "Search for a product and add it to cart", dependsOnMethods = "loginTest")
    public void addProductToCartTest() {
        // Navigate to homepage again if login redirected elsewhere or to ensure a clean start for search
        // Find the search bar, enter product name, and submit
        WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBarLocator));
        searchBar.sendKeys(PRODUCT_TO_SEARCH);
        System.out.println("Entered search term: " + PRODUCT_TO_SEARCH);

        try {
            WebElement searchSubmit = driver.findElement(searchSubmitButtonLocator); //handle if search is by pressing Enter
            searchSubmit.click();
            System.out.println("Clicked search submit button.");
        } catch (Exception e) {
            System.out.println("Search submit button not found or not needed. Assuming search initiated.");
        }


        // Wait for search results and click on the first product
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(firstProductLinkLocator));
        String selectedProductName = firstProduct.getText(); // Or get from an attribute if text is not reliable
        System.out.println("Found product, attempting to click: " + selectedProductName);
        firstProduct.click();

        // Wait for the "Add to Cart" button on the product details page and click it
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonLocator));
        System.out.println("Located 'Add to Cart' button.");
        addToCartBtn.click();
        System.out.println("Clicked 'Add to Cart' button.");

    }

    public void tearDown() {
        if (driver != null) {
            System.out.println("Closing the browser.");
            driver.quit();
        }
    }
}
