package com.thalesgroup.scadasoft.gwebhmi.test.client.scscomponent.test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.*; //package name for jUnit4
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ExploratorSCSDBM {

    private static Logger logger = Logger.getLogger(ExploratorSCSDBM.class);
    private WebDriver driver;
    private String baseUrl;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {

        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8081/mywebapp/scsmain/initialize?page=/scsmain/ScsMain.html";
        driver.manage().window().maximize();
    }

    @Test
    public void testSelenium() throws Exception {

        startContext();

        Thread.sleep(5000);

        Actions act = new Actions(driver);
        WebElement MVPButton = driver.findElement(By.xpath(
                "//div[2]/div/div[2]/div/div/table/tbody/tr/td[2]/div/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
        act.doubleClick(MVPButton).build().perform();

        driver.findElement(By.xpath("//td[2]/div/div[3]/div")).click();
        driver.findElement(By.xpath("//button[@id='RefreshBId']")).click();

        selectClass(53);

        Thread.sleep(1000);

        driver.findElement(By.xpath("//td[2]/div/div[11]/div")).click();

        fileExplorator(act);

        /*
         * Query Test
         */
        // driver.findElement(By.xpath("(//input[@type='text'])[16]")).sendKeys("DO");
        // driver.findElement(By.xpath("(//input[@type='text'])[17]")).sendKeys("SITE1:B005:F010");
        // driver.findElement(By.xpath("(//button[@type='button'])[7]")).click();
    }

    private boolean isElementPresent(By by) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
    }

    /*
     * This function browse each class included in class list box. For each
     * class selected, we retrieve each attribute of each instance.
     */
    private void selectClass(int sizeClass) {
        Select listBox = new Select(driver.findElement(By.cssSelector("select.gwt-ListBox")));
        List<WebElement> list = listBox.getOptions();
        int sizeC = list.size();
        if (sizeC == 0) {
            logger.info("No database found");
        }

        for (int iClass = 0; iClass < sizeClass; iClass++) {
            Select classLB = new Select(driver.findElement(By.cssSelector("select.gwt-ListBox")));
            classLB.selectByIndex(iClass);
            String selectedClass = classLB.getFirstSelectedOption().getText();

            try {
                Thread.sleep(400);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            logger.info("**************************************************************");
            logger.info("Selected Class : " + selectedClass);
            logger.info("**************************************************************");

            int sizeAtt = new Select(
                    driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                            .getOptions().size();
            if (sizeAtt != 0) {
                selectAttribute(sizeAtt);
            } else {
                logger.info("No attribute for this class" + selectedClass);
            }

            int sizeInst = new Select(driver.findElement(By.xpath("//td[3]/fieldset/select"))).getOptions().size();
            if (sizeInst != 0) {
                selectInstance(sizeInst);
            } else {
                logger.info("No instance for this class :" + selectedClass);
            }
        }
    }

    private void selectInstance(int sizeInst) {
        for (int iInstance = 0; iInstance < sizeInst; iInstance++) {

            Select instLB = new Select(driver.findElement(By.xpath("//td[3]/fieldset/select")));
            instLB.selectByIndex(iInstance);

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String selectedInst = instLB.getFirstSelectedOption().getText();

            int sizeAtt = new Select(
                    driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                            .getOptions().size();

            logger.info("**************************************************************");
            logger.info("Selected Instance : " + selectedInst);
            logger.info("**************************************************************");

            if (sizeAtt != 0) {
                selectAttribute(sizeAtt);
            } else {

                logger.info("No attribute for this instance");
            }
        }
    }

    private void selectAttribute(int sizeAtt) {
        for (int iAtt = 0; iAtt < sizeAtt; iAtt++) {
            Select attributeLB = new Select(
                    driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")));
            attributeLB.selectByIndex(iAtt);

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String selectedAtt = attributeLB.getFirstSelectedOption().getText();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String selectedFormula = driver.findElement(By.xpath("(//input[@type='text'])[25]")).getAttribute("value");
            logger.info("Attribute <" + selectedAtt + ">" + " has this formula : " + selectedFormula);
        }
    }

    /*
     * Function to retrieve each attribute of each item included in explorator
     * list box.
     */
    private void selectChild() {
        int sizeExplorator = new Select(driver.findElement(By.xpath("//tr[2]/td/select"))).getOptions().size();
        for (int indexChild = 1; indexChild <= sizeExplorator; indexChild++) {
            String selectedChild = driver.findElement(By.xpath("//tr[2]/td/select/option[" + indexChild + "]"))
                    .getAttribute("value");

            /*
             * We must click on the element to select it. This make the
             * attribute list box to be updated.
             */
            driver.findElement(By.xpath("//tr[2]/td/select/option[" + indexChild + "]")).click();

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("**************************************************************");
            logger.info(selectedChild);
            logger.info("**************************************************************");

            int sizeAtt = new Select(
                    driver.findElement(By.xpath("//fieldset/table/tbody/tr/td/select")))
                            .getOptions().size();
            if (sizeAtt != 0) {
                selectAttribute(sizeAtt);
            } else {
                logger.info("No attribute for this item");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Recursive function to browse a tree. This function begins from the parent
     * of the entire tree. When the list box is empty (size = 0), we have run
     * through each child of a branch and we must access the next branch by
     * turning back to correct parent.
     * 
     * If the listbox isn't empty (we are not at the end of the branch) : LOOP {
     * - We call selectChild function (to retrieve attribute) - Then we place
     * into the child of each item (we go down the tree) - We call again the
     * fileExplorator function } The loop allows us to run through the list box
     * and the recursivity allows us to go up the end of each branch
     */
    private void fileExplorator(Actions act) {
        int sizeExplorator = new Select(driver.findElement(By.xpath("//tr[2]/td/select"))).getOptions().size();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (sizeExplorator > 0) {
            selectChild();
            for (int i = 1; i <= sizeExplorator; i++) {
                String selectedChild = driver.findElement(By.xpath("//tr[2]/td/select/option[" + i + "]"))
                        .getAttribute("value");

                driver.findElement(By.xpath("//tr[2]/td/select/option[" + i + "]")).click();

                driver.findElement(By.xpath("(//button[@type='button'])[12]")).click();

                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logger.info("--------\\");
                logger.info("---------\\");
                logger.info("----------\\");
                logger.info("-----------\\");
                logger.info("-----------" + "/");
                logger.info("----------" + "/");
                logger.info("---------" + "/");
                logger.info("--------" + "/");
                logger.info("We are in : " + selectedChild + ", its children are the following item :");
                fileExplorator(act);
            }

            // There is two cases when we go out of the loop :
            // - The previous selected item contains child
            // - The previous selected item doesn't contain child
            // In the second case, we must click twice on the up button.
            String firstChild = driver.findElement(By.xpath("//tr[2]/td/select/option[1]")).getAttribute("value");
            driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ((driver.findElement(By.xpath("//tr[2]/td/select/option[1]")).getAttribute("value")).toString()
                    .equals(firstChild.toString())) {
                driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            driver.findElement(By.xpath("(//button[@type='button'])[11]")).click();
            logger.info("Current item doesn't contains child, we turn back to next item");
            logger.info("   " + "/" + "--------");
            logger.info("  " + "/" + "---------");
            logger.info(" " + "/" + "----------");
            logger.info("" + "/" + "-----------");
            logger.info("\\-----------");
            logger.info(" \\----------");
            logger.info("  \\---------");
            logger.info("   \\--------");
            logger.info("We turn back to the next item");

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
}
