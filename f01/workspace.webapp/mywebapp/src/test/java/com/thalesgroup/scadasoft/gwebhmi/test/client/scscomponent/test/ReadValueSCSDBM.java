package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ReadValueSCSDBM {
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
    public void testReadValueSCSDBM() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement MVPButton = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(MVPButton).build().perform();

        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        driver.findElement(By.xpath("//button[@id='RefreshBId']")).click();

        // In the explorator
        driver.findElement(By.xpath("//td[2]/div/div[11]/div")).click();
        new Select(driver.findElement(By.xpath("//tr[2]/td/select"))).selectByVisibleText(":SITE1");
        driver.findElement(By.xpath("//option[@value=':SITE1']")).click();
        driver.findElement(By.xpath("(//button[@type='button'])[12]")).click();
        new Select(driver.findElement(By.xpath("//tr[2]/td/select"))).selectByVisibleText(":SITE1:B001");
        driver.findElement(By.xpath("//option[@value=':SITE1:B001']")).click();
        driver.findElement(By.xpath("(//button[@type='button'])[12]")).click();
        new Select(driver.findElement(By.xpath("//tr[2]/td/select"))).selectByVisibleText(":SITE1:B001:F001");
        driver.findElement(By.xpath("//option[@value=':SITE1:B001:F001']")).click();
        driver.findElement(By.xpath("(//button[@type='button'])[12]")).click();
        new Select(driver.findElement(By.xpath("//tr[2]/td/select"))).selectByVisibleText(":SITE1:B001:F001:ACCESS");
        driver.findElement(By.xpath("//option[@value=':SITE1:B001:F001:ACCESS']")).click();
        driver.findElement(By.xpath("(//button[@type='button'])[12]")).click();
        new Select(driver.findElement(By.xpath("//tr[2]/td/select")))
                .selectByVisibleText(":SITE1:B001:F001:ACCESS:DO001");
        driver.findElement(By.xpath("//option[@value=':SITE1:B001:F001:ACCESS:DO001']")).click();
        
        // Verify attributes
        driver.findElement(By.xpath("//div[9]/div")).click();
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION1");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".EQPT_ACTION1 > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION2");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".EQPT_ACTION2 > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION3");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".EQPT_ACTION3 > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".EQPT_ACTION4");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".EQPT_ACTION4 > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".UNIVNAME");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".UNIVNAME > \"SITE1B001F001ACCESSDO001\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".alarmSynthesis");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".alarmSynthesis > [0,0,0,0]",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".forcedSynthesis");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".forcedSynthesis > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".functionalCat");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".functionalCat > 2",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".geographicalCat");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".geographicalCat > 1",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".inhibStatus");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".inhibStatus > 1",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".inhibSynthesis");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".inhibSynthesis > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".isControlable");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".isControlable > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".label");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".label > \"Door B01DO001\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".name");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".name > \":SITE1:B001:F001:ACCESS:DO001\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".opDate");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".opDate > \"01/01/2000 00:00:00.0\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".opSource");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".opSource > \"\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".poiList");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".poiList > \"\"", driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".prevAlarmSynthesis");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".prevAlarmSynthesis > [0,0,0,0]",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".prevValidSynthesis");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".prevValidSynthesis > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).selectByVisibleText(".scstype");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".scstype > \"DoorType\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".shortname");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".shortname > \"B01DO001\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".tag1Label");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".tag1Label > \"\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".tag2Label");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".tag2Label > \"\"",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".tagStatus");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".tagStatus > 1", driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                .selectByVisibleText(".validSynthesis");
        driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
        try {
            assertEquals(".validSynthesis > 0",
                    driver.findElement(By.xpath("//option[@value='.EQPT_ACTION1']")).getText());
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
