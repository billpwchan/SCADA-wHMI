package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ListBoxSelectionSCSCTL {
    
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
    public void testListBoxSelectionSCSCTL() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement SCSCTL = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div[2]/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(SCSCTL).build().perform();     
        
        // Click on refresh button
        driver.findElement(By.xpath("//tr[2]/td/button")).click();
        
        // We get the size of each list box
        try {
        assertEquals(1, new Select(driver.findElement(By.xpath("//select"))).getOptions().size());
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[3]/select"))).getOptions().size());
        assertEquals(1, new Select(driver.findElement(By.xpath("//div[4]/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }


        new Select(driver.findElement(By.xpath("//option[1]")));
        driver.findElement(By.xpath("//option[1]")).click();
        
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"RTU state\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[1]")));
        driver.findElement(By.xpath("//td[2]/select/option[1]")).click();
        String item = driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value");
        
        try {
            assertEquals(item + "(1)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[2]")));
        driver.findElement(By.xpath("//td[2]/select/option[2]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[2]")).getAttribute("value");
        
        try {
            assertEquals(item + "(2)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
      
        new Select(driver.findElement(By.xpath("//option[2]")));
        driver.findElement(By.xpath("//option[2]")).click();
        
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"RTU spy state\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[1]")));
        driver.findElement(By.xpath("//td[2]/select/option[1]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value");
        
        try {
            assertEquals(item + "(1)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[2]")));
        driver.findElement(By.xpath("//td[2]/select/option[2]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[2]")).getAttribute("value");
        
        try {
            assertEquals(item + "(2)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        
        new Select(driver.findElement(By.xpath("//option[5]")));
        driver.findElement(By.xpath("//option[5]")).click();
        
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"Open/Close command\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[1]")));
        driver.findElement(By.xpath("//td[2]/select/option[1]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value");
        
        try {
            assertEquals(item + "(1)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[2]")));
        driver.findElement(By.xpath("//td[2]/select/option[2]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[2]")).getAttribute("value");
        
        try {
            assertEquals(item + "(2)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//option[15]")));
        driver.findElement(By.xpath("//option[15]")).click();
        
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"Record Control\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[1]")));
        driver.findElement(By.xpath("//td[2]/select/option[1]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value");
        
        try {
            assertEquals(item + "(1)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[2]")));
        driver.findElement(By.xpath("//td[2]/select/option[2]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[2]")).getAttribute("value");
        
        try {
            assertEquals(item + "(2)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//option[16]")));
        driver.findElement(By.xpath("//option[16]")).click();
        
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"Camera Status\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[1]")));
        driver.findElement(By.xpath("//td[2]/select/option[1]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value");
        
        try {
            assertEquals(item + "(1)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[2]")));
        driver.findElement(By.xpath("//td[2]/select/option[2]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[2]")).getAttribute("value");
        
        try {
            assertEquals(item + "(2)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//option[45]")));
        driver.findElement(By.xpath("//option[45]")).click();
        
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"Lock command\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[1]")));
        driver.findElement(By.xpath("//td[2]/select/option[1]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value");
        
        try {
            assertEquals(item + "(1)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        new Select(driver.findElement(By.xpath("//td[2]/select/option[2]")));
        driver.findElement(By.xpath("//td[2]/select/option[2]")).click();
        item = driver.findElement(By.xpath("//td[2]/select/option[2]")).getAttribute("value");
        
        try {
            assertEquals(item + "(2)", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }     
        
        
        // Analog List Box
        driver.findElement(By.xpath("//td/div/div[2]/div/div[2]/div/div")).click();
        
        new Select(driver.findElement(By.xpath("//div[3]/select/option[1]")));
        driver.findElement(By.xpath("//div[3]/select/option[1]")).click();
        
        try {
            assertEquals("\",aeoSITE1B001F001CCTVCA001angle,0.0\"", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"Angle Control\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("[MIN_VALUE, MAX_VALUE] : [-0.01,360.01]", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        assertEquals(0, new Select(driver.findElement(By.xpath("//td[2]/select/option[1]"))).getOptions().size());
       
        new Select(driver.findElement(By.xpath("//div[3]/select/option[2]")));
        driver.findElement(By.xpath("//div[3]/select/option[2]")).click();
        
        try {
            assertEquals("\",aeoSITE1B001F001CCTVCA001zoom,0.0\"", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"Zoom Control\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("[MIN_VALUE, MAX_VALUE] : [0,100]", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        assertEquals(0, new Select(driver.findElement(By.xpath("//td[2]/select/option[1]"))).getOptions().size());
        
     // Structured List Box
        driver.findElement(By.xpath("//td/div/div[2]/div/div[3]/div/div")).click();
        
        new Select(driver.findElement(By.xpath("//div[4]/select/option[1]")));
        driver.findElement(By.xpath("//div[4]/select/option[1]")).click();
        
        try {
            assertEquals("\",seoSITE1B001F001ACCESSDO001unlck,\"", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
            assertEquals("\"unlock door\"", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
            assertEquals("1", driver.findElement(By.xpath("(//input[@type='text'])[12]")).getAttribute("value"));
            assertEquals("3", driver.findElement(By.xpath("(//input[@type='text'])[13]")).getAttribute("value"));
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[14]")).getAttribute("value"));
        }
        catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        assertEquals(0, new Select(driver.findElement(By.xpath("//td[2]/select/option[1]"))).getOptions().size());
    }
    
    private void startContext() {
        // TODO Auto-generated method stub
        driver.get(baseUrl);

        driver.findElement(By.name("j_username")).sendKeys("chief");
        driver.findElement(By.name("j_password")).sendKeys("thales");
        driver.findElement(By.cssSelector("button.gwt-Button.login-button")).click();
    }

}