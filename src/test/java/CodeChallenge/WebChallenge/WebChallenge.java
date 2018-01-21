package CodeChallenge.WebChallenge;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;

public class WebChallenge {
	private WebDriver driver;
	private Properties properties;
	private WebDriverWait wait;
	
  @Test (priority = 1)
public void test1() throws IOException {
	  //reads the xpaths and test data from properties file
	  properties = readProperties();
	  String email = properties.getProperty("email");
	  String password = properties.getProperty("password");
	  String xpath_ad = properties.getProperty("ad");
	  String xpath_adClose = properties.getProperty("adClose");
	  String xpath_email = properties.getProperty("xpath_username");
	  String xpath_password = properties.getProperty("xpath_password");
	  String xpath_submit = properties.getProperty("submit");
	  
	  //switch to frame to close the ad
	  driver.switchTo().frame(driver.findElement(By.xpath(xpath_ad)));
	  driver.findElement(By.xpath(xpath_adClose)).click();
	  
	  //switch back to default content
	  driver.switchTo().defaultContent();
	  System.out.println(driver.getTitle());
	  
	  //wait until the the login link is clickable
	  wait = new WebDriverWait(driver, 10);
	  wait.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGIN")));

	  //click on login
	  driver.findElement(By.linkText("LOGIN")).click();
	  
	  //enter username and password and login
	  driver.findElement(By.xpath(xpath_email)).sendKeys(email);
	  driver.findElement(By.xpath(xpath_password)).sendKeys(password);
	  driver.findElement(By.xpath(xpath_submit)).click();
	  
	  //wait until logged user link is clickable
	  wait = new WebDriverWait(driver, 20);
	  wait.until(ExpectedConditions.elementToBeClickable(By.linkText(email)));

	  
	  //verify whether user has been logged in successfully
	  if (driver.findElement(By.linkText(email)).getText().equals(email)){
		  System.out.println("User has been logged in successfully");
	  }
	  else {
		  System.out.println("User login failed");  
	  }
	  
  }
  
  @Test (priority = 2)
  public void test2() throws IOException {
	  
	  properties = readProperties();
	  String xpath_mainImage = properties.getProperty("xpath_mainImage");
	  System.out.println("xpath_mainImage: "+xpath_mainImage);
	  
	  WebDriverWait wait = new WebDriverWait(driver, 10);
	  wait.until(ExpectedConditions.elementToBeClickable(By.xpath("xpath_mainImage")));

      WebElement mainImage =  driver.findElement(By.xpath("xpath_mainImage"));
    //div[@class='views-row-odd views-row-first views-row-last media  article-449636 ']//div[@class='pull-left']//div[@class='media-group fadecount1']//ul//li//div[@class='file file-image file-image-jpeg']//div[@class='content']//div[@class='borealis image-style-retina_large borealis-js borealis-wrapper loaded']//img[@class='borealis image-style-retina_large borealis-js img-responsive']
    
      try {
		checkImageValidity(mainImage);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  @Test (priority = 3)
  public void test3() throws IOException{
	  
	  //read xpaths from property file
	  properties = readProperties();
	  String xpath_mainArticleHeading = properties.getProperty("xpath_mainArticleHeading");
	  //String xpath_mainArticle= properties.getProperty("xpath_mainArticle");
	  
	  //take the main article heading to a variable and click on main article
	  String heading = driver.findElement(By.xpath(xpath_mainArticleHeading)).getText();
	  driver.findElement(By.xpath(xpath_mainArticleHeading)).click();

	  
	System.out.println(heading);
	System.out.println(driver.getTitle());
	
	//verify the article
	if (driver.getTitle().contains(heading)){
		System.out.println("Successfully navigate to main article");
	}
	else {
		System.out.println("Failed to navigate to main article");
	}
	
  }
  
  @Test (priority = 4)
  public void test4() throws IOException {
	  //read properties file and takes the xpath main article image
	  properties = readProperties();
	  String xpath_mainArticleImage= properties.getProperty("xpath_mainArticleImage");

	  //check image size and verify whether image is present 
      WebElement mainImage =  driver.findElement(By.xpath(xpath_mainArticleImage));
      System.out.println(mainImage.getSize());
      
      if(mainImage.isDisplayed()) {
    	  System.out.println("Image Displayed");
    	  }
      else {
    	  System.out.println("Image not displayed");
      }
      }
  
  @Test (priority = 5)
  public void test5() throws IOException {
	  //read xpaths from property file
	  properties = readProperties();
	  String xpath_mainVideo = properties.getProperty("xpath_mainVideo");
	  
	  //verify video presence
	  if (driver.findElement(By.xpath(xpath_mainVideo)) != null) {
		  System.out.println("Video element exists");
	  }
	  else {
		  System.out.println("Video element not exists");
	  }
	  
  }
  
  public Boolean checkImageValidity(WebElement Im) throws IOException{
	  //this method can be used to check whether the image is present
	  //check the scr attribute value of the image tag and check whether it gives 200 HTTP response using HTTP GET
	    String Source = Im.getAttribute("src");
	    System.out.println(Source);
	    
	    URL url = new URL(Source);
	    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
	    connection.connect();

	    int code = connection.getResponseCode();
	    if (code == 200) {
	    System.out.println(code);
	    return true;
	    }
	    
	    else {
	    	System.out.println(code);
	    	return false;
	    }
	}
  
  public Properties readProperties() throws IOException {
	  //this method can be used to read testcondtion values from config.properties file
	  properties = new Properties();
	  FileInputStream ip = new FileInputStream("/Users/nilmi/Documents/SPH/WebChallenge/src/test/java/CodeChallenge/WebChallenge/config.properties");
	  properties.load(ip);
	  return properties;
  }
  
  
  
  @BeforeTest
  public void beforeTest() throws IOException {
	  //read the brower, url values from config.properties file
	  properties = readProperties();	  
	  String browserName = properties.getProperty("browser");
	  String url = properties.getProperty("URL");
	  
	  //Launch driver	  
	  if (browserName.equals("Chrome")) {
		  System.setProperty("webdriver.chrome.driver", "/Users/nilmi/Documents/SPH/chromedriver");
		   driver = new ChromeDriver();		  //driver.get("http://google.com");
	  }
	  else if(browserName.equals("FireFox")) {
		  System.setProperty("webdriver.gecko.driver", "/Users/nilmi/Documents/SPH/geckodriver");
		   driver = new FirefoxDriver(); 
	  }
	  else if(browserName.equals("Safari")) {
		   driver = new SafariDriver();
	  }
	  
	  System.out.println("URL: "+url);
	  //go to the url
	  driver.get(url);
	  
  }

  @AfterTest
  public void afterTest() {
	  //close the driver
	  driver.close();
  }

}
