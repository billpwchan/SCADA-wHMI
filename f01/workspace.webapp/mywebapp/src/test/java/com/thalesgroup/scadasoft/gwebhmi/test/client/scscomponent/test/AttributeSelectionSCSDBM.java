package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class AttributeSelectionSCSDBM {
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
    public void testAttributeSelectionSCSDBM() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement MVPButton = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(MVPButton).build().perform();

        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        driver.findElement(By.xpath("//button[@id='RefreshBId']")).click();
      
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("BasicEquipment");
        driver.findElement(By.xpath("//option[@value='BasicEquipment']")).click();
        
        driver.findElement(By.xpath("//td/div/div[2]/div/div[2]/div/div")).click();
        new Select(driver.findElement(By.xpath("//div[3]/table/tbody/tr[4]/td/select")))
                .selectByVisibleText("<alias>SITE1B001F000SYSTEMPI001");
        
        driver.findElement(By.xpath("//option[@value='<alias>SITE1B001F000SYSTEMPI001']")).click();
        driver.findElement(By.xpath("//div[9]/div")).click();
        
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION1");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION2");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION3");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION4");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".UNIVNAME");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".alarmSynthesis");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".forcedSynthesis");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".functionalCat");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".geographicalCat");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".inhibStatus");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".inhibSynthesis");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".isControlable");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".label");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".name");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".opDate");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".opSource");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".poiList");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".prevAlarmSynthesis");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".prevValidSynthesis");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".scstype");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".shortname");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".tag1Label");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".tag2Label");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".tagStatus");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".validSynthesis");
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    
    private void startContext() {
        // TODO Auto-generated method stub
        driver.get(baseUrl);

        driver.findElement(By.name("j_username")).sendKeys("chief");
        driver.findElement(By.name("j_password")).sendKeys("thales");
        driver.findElement(By.cssSelector("button.gwt-Button.login-button")).click();
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
