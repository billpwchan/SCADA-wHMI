package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class SCSDBMDemoInitialisation {
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
    public void testSCSDBMDemoInitialisation() throws Exception {
        // ERROR: Caught exception [ERROR: Unsupported command [setSpeed | 500 |
        // ]]
        driver.get(baseUrl);

        driver.findElement(By.name("j_username")).sendKeys("chief");
        driver.findElement(By.name("j_password")).sendKeys("thales");
        driver.findElement(By.cssSelector("button.gwt-Button.login-button")).click();
        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement MVPButton = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(MVPButton).build().perform();

        // ERROR: Caught exception [ERROR: Unsupported command [doubleClick |
        // //div[@id='gwt-uid-33']/table/tbody/tr/td/table/tbody/tr/td[2]/div |
        // ]]
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class:",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class:",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ACI_TYPE", driver.findElement(By.xpath("//option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("AdvEquipment", driver.findElement(By.xpath("//option[2]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("AdvNode", driver.findElement(By.xpath("//option[3]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("BasicEquipment", driver.findElement(By.xpath("//option[4]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("BasicNode", driver.findElement(By.xpath("//option[5]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ClassConfig", driver.findElement(By.xpath("//option[6]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("DCI_TYPE", driver.findElement(By.xpath("//option[7]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("DMM", driver.findElement(By.xpath("//option[8]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("GrcPoint", driver.findElement(By.xpath("//option[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("HSER", driver.findElement(By.xpath("//option[10]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("LogicalEnv", driver.findElement(By.xpath("//option[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("POI", driver.findElement(By.xpath("//option[12]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("POI_FLDR", driver.findElement(By.xpath("//option[13]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("POST", driver.findElement(By.xpath("//option[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("PhysEnv", driver.findElement(By.xpath("//option[15]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("SCI_STRING_TYPE", driver.findElement(By.xpath("//option[16]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("SCI_TYPE", driver.findElement(By.xpath("//option[17]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ScadaSoft", driver.findElement(By.xpath("//option[18]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ScsCtlGrc", driver.findElement(By.xpath("//option[19]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("UserFields", driver.findElement(By.xpath("//option[20]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("VMS", driver.findElement(By.xpath("//option[21]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aac_type", driver.findElement(By.xpath("//option[22]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aal_type", driver.findElement(By.xpath("//option[23]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aci_cb_type", driver.findElement(By.xpath("//option[24]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aci_mt_type", driver.findElement(By.xpath("//option[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aci_st_type", driver.findElement(By.xpath("//option[26]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aco_type", driver.findElement(By.xpath("//option[27]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aes_type", driver.findElement(By.xpath("//option[28]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("afo_type", driver.findElement(By.xpath("//option[29]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aio_type", driver.findElement(By.xpath("//option[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("clAlarmsynthesis", driver.findElement(By.xpath("//option[31]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("clScsAlarm", driver.findElement(By.xpath("//option[32]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dac_type", driver.findElement(By.xpath("//option[33]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dal_type", driver.findElement(By.xpath("//option[34]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dci_cb_type", driver.findElement(By.xpath("//option[35]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dci_mt_type", driver.findElement(By.xpath("//option[36]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dci_st_type", driver.findElement(By.xpath("//option[37]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dco_type", driver.findElement(By.xpath("//option[38]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("des_type", driver.findElement(By.xpath("//option[39]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dfo_type", driver.findElement(By.xpath("//option[40]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dio_type", driver.findElement(By.xpath("//option[41]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dov_type", driver.findElement(By.xpath("//option[42]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sac_type", driver.findElement(By.xpath("//option[43]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sacs", driver.findElement(By.xpath("//option[44]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sci_cb_type", driver.findElement(By.xpath("//option[45]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sci_mt_type", driver.findElement(By.xpath("//option[46]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sci_st_type", driver.findElement(By.xpath("//option[47]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sco_type", driver.findElement(By.xpath("//option[48]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("scos", driver.findElement(By.xpath("//option[49]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sfo_type", driver.findElement(By.xpath("//option[50]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sfos", driver.findElement(By.xpath("//option[51]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sio_string_type", driver.findElement(By.xpath("//option[52]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sio_type", driver.findElement(By.xpath("//option[53]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td/div/div[2]/div/div[2]/div/div")).click();
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Class name of this instance:",
                    driver.findElement(By.xpath("//div[3]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this instance:",
                    driver.findElement(By.xpath("//div[3]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("//div[3]/table/tbody/tr[4]/td/select/option[1]"))
                    .getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td/div/div[2]/div/div[3]/div/div")).click();
        try {
            assertEquals("",
                    driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select/option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
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
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes:",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("miliseconds...",
                    driver.findElement(By.xpath("(//input[@type='text'])[22]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[23]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[24]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertFalse(driver.findElement(By.id("gwt-uid-46")).isSelected());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertFalse(driver.findElement(By.id("gwt-uid-47")).isSelected());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertFalse(driver.findElement(By.id("gwt-uid-48")).isSelected());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[26]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[27]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[11]/div")).click();
        try {
            assertEquals(":SITE1", driver.findElement(By.xpath("//tr[2]/td/select/option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(":ScadaSoft",
                    driver.findElement(By.xpath("//tr[2]/td/select/option[2]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(":classConfig",
                    driver.findElement(By.xpath("//tr[2]/td/select/option[3]")).getAttribute("value"));
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
            assertEquals("", driver.findElement(By.xpath("//td[2]/select/option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals("Enter an address (:SITE1:B001:F000:CCTV)",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        driver.findElement(By.id("RefreshBId")).click();
        try {
            assertEquals("Total number of classes: 53",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of instances in this class:",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this class:",
                    driver.findElement(By.xpath("//div[2]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ACI_TYPE", driver.findElement(By.xpath("//option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("AdvEquipment", driver.findElement(By.xpath("//option[2]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("AdvNode", driver.findElement(By.xpath("//option[3]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("BasicEquipment", driver.findElement(By.xpath("//option[4]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("BasicNode", driver.findElement(By.xpath("//option[5]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ClassConfig", driver.findElement(By.xpath("//option[6]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("DCI_TYPE", driver.findElement(By.xpath("//option[7]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("DMM", driver.findElement(By.xpath("//option[8]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("GrcPoint", driver.findElement(By.xpath("//option[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("HSER", driver.findElement(By.xpath("//option[10]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("LogicalEnv", driver.findElement(By.xpath("//option[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("POI", driver.findElement(By.xpath("//option[12]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("POI_FLDR", driver.findElement(By.xpath("//option[13]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("POST", driver.findElement(By.xpath("//option[14]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("PhysEnv", driver.findElement(By.xpath("//option[15]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("SCI_STRING_TYPE", driver.findElement(By.xpath("//option[16]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("SCI_TYPE", driver.findElement(By.xpath("//option[17]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ScadaSoft", driver.findElement(By.xpath("//option[18]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("ScsCtlGrc", driver.findElement(By.xpath("//option[19]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("UserFields", driver.findElement(By.xpath("//option[20]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("VMS", driver.findElement(By.xpath("//option[21]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aac_type", driver.findElement(By.xpath("//option[22]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aal_type", driver.findElement(By.xpath("//option[23]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aci_cb_type", driver.findElement(By.xpath("//option[24]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aci_mt_type", driver.findElement(By.xpath("//option[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aci_st_type", driver.findElement(By.xpath("//option[26]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aco_type", driver.findElement(By.xpath("//option[27]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aes_type", driver.findElement(By.xpath("//option[28]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("afo_type", driver.findElement(By.xpath("//option[29]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("aio_type", driver.findElement(By.xpath("//option[30]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("clAlarmsynthesis", driver.findElement(By.xpath("//option[31]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("clScsAlarm", driver.findElement(By.xpath("//option[32]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dac_type", driver.findElement(By.xpath("//option[33]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dal_type", driver.findElement(By.xpath("//option[34]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dci_cb_type", driver.findElement(By.xpath("//option[35]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dci_mt_type", driver.findElement(By.xpath("//option[36]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dci_st_type", driver.findElement(By.xpath("//option[37]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dco_type", driver.findElement(By.xpath("//option[38]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("des_type", driver.findElement(By.xpath("//option[39]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dfo_type", driver.findElement(By.xpath("//option[40]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dio_type", driver.findElement(By.xpath("//option[41]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("dov_type", driver.findElement(By.xpath("//option[42]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sac_type", driver.findElement(By.xpath("//option[43]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sacs", driver.findElement(By.xpath("//option[44]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sci_cb_type", driver.findElement(By.xpath("//option[45]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sci_mt_type", driver.findElement(By.xpath("//option[46]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sci_st_type", driver.findElement(By.xpath("//option[47]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sco_type", driver.findElement(By.xpath("//option[48]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("scos", driver.findElement(By.xpath("//option[49]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sfo_type", driver.findElement(By.xpath("//option[50]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sfos", driver.findElement(By.xpath("//option[51]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sio_string_type", driver.findElement(By.xpath("//option[52]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("sio_type", driver.findElement(By.xpath("//option[53]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[9]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[10]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[11]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td/div/div[2]/div/div[2]/div/div")).click();
        try {
            assertEquals("Number of instances: 0",
                    driver.findElement(By.xpath("//td/div/div[3]/div/div[3]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Class name of this instance:",
                    driver.findElement(By.xpath("//div[3]/table/tbody/tr[2]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes of this instance:",
                    driver.findElement(By.xpath("//div[3]/table/tbody/tr[3]/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("//div[3]/table/tbody/tr[4]/td/select/option[1]"))
                    .getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td/div/div[2]/div/div[3]/div/div")).click();
        try {
            assertEquals("",
                    driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select/option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
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
        driver.findElement(By.xpath("//div[9]/div")).click();
        try {
            assertEquals(0, new Select(driver.findElement(By.xpath("//div[4]/table/tbody/tr/td/select"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("Number of attributes:",
                    driver.findElement(By.xpath("//td[2]/table/tbody/tr/td/div")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("miliseconds...",
                    driver.findElement(By.xpath("(//input[@type='text'])[22]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[23]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[24]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertFalse(driver.findElement(By.id("gwt-uid-46")).isSelected());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertFalse(driver.findElement(By.id("gwt-uid-47")).isSelected());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertFalse(driver.findElement(By.id("gwt-uid-48")).isSelected());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[26]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals("", driver.findElement(By.xpath("(//input[@type='text'])[27]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//td[2]/div/div[11]/div")).click();
        try {
            assertEquals(":SITE1", driver.findElement(By.xpath("//tr[2]/td/select/option[1]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(":ScadaSoft",
                    driver.findElement(By.xpath("//tr[2]/td/select/option[2]")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        try {
            assertEquals(":classConfig",
                    driver.findElement(By.xpath("//tr[2]/td/select/option[3]")).getAttribute("value"));
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
            assertEquals(0, new Select(driver.findElement(By.xpath("//td[2]/select/option"))).getOptions().size());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.xpath("//div[15]/div")).click();
        try {
            assertEquals("Enter an address (:SITE1:B001:F000:CCTV)",
                    driver.findElement(By.xpath("(//input[@type='text'])[30]")).getAttribute("value"));
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
