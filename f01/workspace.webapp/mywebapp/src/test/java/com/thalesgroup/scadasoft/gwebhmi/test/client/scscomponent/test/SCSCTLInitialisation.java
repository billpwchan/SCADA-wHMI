package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class SCSCTLInitialisation {
    
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
    public void testSCSCTLInitialisation() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement SCSCTL = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div[2]/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(SCSCTL).build().perform();     
        
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//select"))).getOptions().size());
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[3]/select"))).getOptions().size());
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }

        
        try {
            assertEquals("No selection", driver.findElement(By.xpath("xpath=(//input[@type='text'])[9]")).getAttribute("value"));
            assertEquals("No selection", driver.findElement(By.xpath("xpath=(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("No selection", driver.findElement(By.xpath("xpath=(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("No selection", driver.findElement(By.xpath("xpath=(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("No selection", driver.findElement(By.xpath("xpath=(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("xpath=(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        driver.findElement(By.xpath("//tr[2]/td/button")).click();
    }
    
    private void startContext() {
        // TODO Auto-generated method stub
        driver.get(baseUrl);

        driver.findElement(By.name("j_username")).sendKeys("chief");
        driver.findElement(By.name("j_password")).sendKeys("thales");
        driver.findElement(By.cssSelector("button.gwt-Button.login-button")).click();
    }

}