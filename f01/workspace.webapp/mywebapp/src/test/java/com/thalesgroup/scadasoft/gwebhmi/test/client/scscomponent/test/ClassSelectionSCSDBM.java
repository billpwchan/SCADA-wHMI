package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ClassSelectionSCSDBM {
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
    public void testClassSelectionSCSDBM() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement MVPButton = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(MVPButton).build().perform();

        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        driver.findElement(By.xpath("//button[@id='RefreshBId']")).click();

        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("ACI_TYPE");
        
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("19", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

        verifyInstanceDatabaseInfo();

        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":ACI_TYPE",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("AdvEquipment");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("14", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

        verifyInstanceDatabaseInfo();

        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":AdvEquipment",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("AdvNode");
        try {
            assertEquals(60, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        try {
            assertEquals("Number of instances: 60",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 60",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("50", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("60", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

        verifyInstanceDatabaseInfo();

        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":AdvNode", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("BasicEquipment");
        try {
        assertEquals(2532, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 2532",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 2532",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("31", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("2532", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

        verifyInstanceDatabaseInfo();

        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":BasicEquipment",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("BasicNode");
        try {
        assertEquals(221, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 221",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 221",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("39", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("221", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":BasicNode",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("ClassConfig");
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":ClassConfig",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("DCI_TYPE");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("30", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":DCI_TYPE",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("DMM");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("12", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":DMM", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("GrcPoint");
        try {
        assertEquals(3, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 3",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 3",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("23", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":GrcPoint",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("HSER");
        try {
        assertEquals(11, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 11",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 11",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("37", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("11", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":HSER", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("LogicalEnv");
        try {
        assertEquals(8, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 8",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 8",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("9", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("8", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":LogicalEnv",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("POI");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("13", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":POI", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("POI_FLDR");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":POI_FLDR",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("POST");
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("43", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":POST", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("PhysEnv");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("47", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":PhysEnv", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("SCI_STRING_TYPE");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("32", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":SCI_STRING_TYPE",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("SCI_TYPE");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("42", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":SCI_TYPE",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("ScadaSoft");
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("6", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":ScadaSoft",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("ScsCtlGrc");
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":ScsCtlGrc",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("UserFields");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("22", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":UserFields",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("VMS");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("2", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":VMS", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aac_type");
        try {
        assertEquals(1009, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1009",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1009",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("49", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1009", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aac_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aal_type");
        try {
        assertEquals(1009, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1009",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1009",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("40", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1009", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aal_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aci_cb_type");
        try {
        assertEquals(1009, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1009",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1009",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("48", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1009", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aci_cb_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aci_mt_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("18", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aci_mt_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aci_st_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("17", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aci_st_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aco_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("7", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aco_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aes_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("15", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aes_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("afo_type");
        try {
        assertEquals(1009, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1009",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1009",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("8", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1009", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":afo_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("aio_type");
        try {
        assertEquals(1000, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1000",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1000",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("10", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1000", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":aio_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("clAlarmsynthesis");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("46", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":clAlarmsynthesis",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("clScsAlarm");
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 1",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 1",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("41", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":clScsAlarm",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dac_type");
        try {
        assertEquals(6562, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 6562",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 6562",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("11", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("6562", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dac_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dal_type");
        try {
        assertEquals(6574, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 6574",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 6574",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("45", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("6574", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dal_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dci_cb_type");
        try {
        assertEquals(6574, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 6574",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 6574",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("16", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("6574", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dci_cb_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dci_mt_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("34", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dci_mt_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dci_st_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("38", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dci_st_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dco_type");
        try {
        assertEquals(12, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 12",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 12",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("25", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("12", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dco_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("des_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("27", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":des_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dfo_type");
        try {
        assertEquals(6574, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 6574",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 6574",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("24", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("6574", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dfo_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dio_type");
        try {
        assertEquals(2026, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 2026",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 2026",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("26", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("2026", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dio_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("dov_type");
        try {
        assertEquals(4052, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 4052",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 4052",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("52", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("4052", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":dov_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sac_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("21", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sac_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sacs");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("29", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sacs", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sci_cb_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("44", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sci_cb_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sci_mt_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("4", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sci_mt_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sci_st_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("5", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sci_st_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sco_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("33", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sco_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("scos");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("20", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":scos", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sfo_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("36", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sfo_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sfos");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("51", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sfos", driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sio_string_type");
        try {
        assertEquals(500, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 500",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 500",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("28", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("500", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sio_string_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        new Select(driver.findElement(By.xpath("//select"))).selectByVisibleText("sio_type");
        try {
        assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } 
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyClassNameLabel();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class: 0",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class: ?",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        try {
            assertEquals("35", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("?", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));

        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("0", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyInstanceDatabaseInfo();
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals("?", new Select(driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes: ?",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        verifyExploratorDatabaseRequestPanels();
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals(":sio_type",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }

    private void verifyClassNameLabel() {
        try {
            assertEquals("Class name of this instance:",
                    driver.findElement(By.xpath("//div[3]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }

    private void verifyExploratorDatabaseRequestPanels() {
        driver.findElement(By.xpath("//td[2]/div/div[11]/div")).click();
        try {
            assertEquals(":SITE1", driver.findElement(By.xpath("//option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(":ScadaSoft", driver.findElement(By.xpath("//option[2]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(":classConfig", driver.findElement(By.xpath("//option[3]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[13]/div")).click();
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[28]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[29]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(0, new Select(driver.findElement(By.xpath("//td[2]/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }

    private void verifyInstanceDatabaseInfo() {
        driver.findElement(By.xpath("//td[2]/div/div[5]/div")).click();
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[15]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[16]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//div[7]/div")).click();
        try {
            assertEquals("21758794", driver.findElement(By.xpath("(//input[@type='text'])[17]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("53", driver.findElement(By.xpath("(//input[@type='text'])[18]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("105917", driver.findElement(By.xpath("(//input[@type='text'])[19]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("40750", driver.findElement(By.xpath("(//input[@type='text'])[20]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("A:/SCSTraining/scspaths/SRV1",
                    driver.findElement(By.xpath("(//input[@type='text'])[21]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
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
