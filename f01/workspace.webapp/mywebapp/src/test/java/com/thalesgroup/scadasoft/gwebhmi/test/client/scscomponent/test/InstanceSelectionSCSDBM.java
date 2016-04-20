package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class InstanceSelectionSCSDBM {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8081/mywebapp/scsmain/initialize?page=/scsmain/ScsMain.html";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void testInstanceSelectionSCSDBM() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement MVPButton = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(MVPButton).build().perform();

        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        driver.findElement(By.xpath("//button[@id='RefreshBId']")).click();

        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("AdvNode");
        driver.findElement(By.xpath("//option[@value='AdvNode']")).click();
        try {
            assertEquals(60, driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        driver.findElement(By.xpath("//td/div/div[2]/div/div[2]/div/div")).click();
        
        try {
        assertEquals(60, new Select(driver.findElement(By.xpath("//div[3]/table/tbody/tr[4]/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        int r = new Random().nextInt(59)+1; 
        driver.findElement(By.xpath("//option["+r+"]")).click();
        try {
            assertEquals("Class name of this instance: \"AdvNode\"", driver.findElement(By.xpath("//div[3]/table/tbody/tr[2]/td/div)")).getText());
        } catch (Error e)
        {
            verificationErrors.append(e.toString());
        }
        
        driver.findElement(By.xpath("//td[2]/div/div[5]/div")).click();
        
        driver.findElement(By.xpath("//div[3]/table/tbody/tr[4]/td/select/option[7]")).click();
      
        try {
            assertEquals("\"SITE1B001F006\"", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("\":SITE1:B001:F006\"", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[15]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[16]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
      
        assertEquals(17, new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        driver.findElement(By.xpath("//div[9]/div")).click();
    }

    private void startContext() {
        // TODO Auto-generated method stub
        driver.get(baseUrl);

        driver.findElement(By.name("j_username")).sendKeys("chief");
        driver.findElement(By.name("j_password")).sendKeys("thales");
        driver.findElement(By.cssSelector("button.gwt-Button.login-button")).click();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
