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

public class SeleniumTest {

	private static Logger logger = Logger.getLogger(SeleniumTest.class);
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

		BasicConfigurator.configure();
		driver.get(baseUrl);
		driver.findElement(By.name("j_username")).sendKeys("chief");
		driver.findElement(By.name("j_password")).sendKeys("thales");
		driver.findElement(By.cssSelector("button.gwt-Button.login-button")).click();
		Thread.sleep(5000);

		/*
		 * If the MVP Button is present, we doubleClick on it. As the id of the
		 * button is generated dynamically at runtime. It can be gwt-uid-31 or
		 * gwt-uid-20.
		 */

		Actions act = new Actions(driver);
		WebElement MVPButton;

		int idButton = 0;
		for (int second = 0;; second++) {
			if (second >= 25)
				fail("timeout");
			try {
				if (isElementPresent(By.xpath("//div[@id='gwt-uid-32']/table/tbody/tr/td/table/tbody/tr/td[2]/div"))) {
					idButton = 32;
					break;
				}
				else if (isElementPresent(By.xpath("//div[@id='gwt-uid-21']/table/tbody/tr/td/table/tbody/tr/td[2]/div"))) {
				    idButton = 21;
				    break;
				}
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}

		/*
		 * According to the button ID, we double double click on it.
		 */
		switch (idButton) {
		case 32:

			MVPButton = driver
					.findElement(By.xpath("//div[@id='gwt-uid-32']/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
			act.doubleClick(MVPButton).build().perform();
			break;
		case 21:
		    
		    MVPButton = driver
		    .findElement(By.xpath("//div[@id='gwt-uid-21']/table/tbody/tr/td/table/tbody/tr/td[2]/div"));
		    act.doubleClick(MVPButton).build().perform();
		    break;
		default:
			logger.info("The MVP Button is not present");
			break;
		}

		Thread.sleep(2000);

		/*
		 * We click on the refresh button to get classes.
		 */
//		driver.findElement(By.xpath("//button[@id='RefreshBId']")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
		Thread.sleep(400);

		int sizeClass = 53;
		//int sizeClass = 2; // sizeClass = 2 only for testing the application **************TEST TEST TEST TEST*******************TEST TEST TEST TEST*******************TEST TEST TEST TEST TEST**********************
		selectClass(sizeClass);

		/*
		 * The following section is useful for make an element to be present in
		 * explorator list box This section prepare the explorator List Box. In
		 * fact, the fileExplorator() function works when there is at least one
		 * item in the list box. First, we look for a class which contains at least one
		 * instances. Then we select the first instance in order to update the
		 * explorator list box.
		 */
		Thread.sleep(1000);
		Select classLB = new Select(driver.findElement(By.cssSelector("select.gwt-ListBox")));
		int index = 0;
		int sizeInst = 0;
		do {
			classLB.selectByIndex(index);
			index++;
			Thread.sleep(500);
			sizeInst = new Select(driver.findElement(By.xpath("//td[3]/fieldset/select"))).getOptions().size();

		} while (sizeInst == 0);

		/*
		 * We select the first instance. It make explorator list box to be updated.
		 */
		Select instLB = new Select(driver.findElement(By.xpath("//td[3]/fieldset/select")));
		Thread.sleep(500);
		instLB.selectByIndex(0);
		Thread.sleep(500);

		// We go up to the parent of the entire tree in order to browse it with
		// the fileExplorator() function.
		String firstItem = null;
		do {
			firstItem = driver
					.findElement(By
							.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option[1]"))
					.getAttribute("value");
			driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
			Thread.sleep(500);
		} while (!driver
				.findElement(By
						.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option[1]"))
				.getAttribute("value").toString().equals(firstItem.toString()));
		Thread.sleep(500);

		// Now we can run the fileExplorator function.
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
					driver.findElement(By.xpath("//td[3]/table/tbody/tr/td/fieldset/table/tbody/tr/td/select")))
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
					driver.findElement(By.xpath("//td[3]/table/tbody/tr/td/fieldset/table/tbody/tr/td/select")))
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
					driver.findElement(By.xpath("//td[3]/table/tbody/tr/td/fieldset/table/tbody/tr/td/select")));
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

			String selectedFormula = driver.findElement(By.xpath("(//input[@type='text'])[22]")).getAttribute("value");
			logger.info("Attribute <" + selectedAtt + ">" + " has this formula : " + selectedFormula);
		}
	}

	/*
	 * Function to retrieve each attribute of each item included in explorator
	 * list box.
	 */
	private void selectChild() {
		int sizeExplorator = new Select(driver.findElement(By.xpath("//td/select"))).getOptions().size();
		for (int indexChild = 1; indexChild <= sizeExplorator; indexChild++) {
			String selectedChild = driver.findElement(By
					.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option["
							+ indexChild + "]"))
					.getAttribute("value");

			/*
			 * We must click on the element to select it. This make the attribute list box to be updated.
			 */
			driver.findElement(By
					.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option["
							+ indexChild + "]"))
					.click();

			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			logger.info("**************************************************************");
			logger.info(selectedChild);
			logger.info("**************************************************************");

			int sizeAtt = new Select(
					driver.findElement(By.xpath("//td[3]/table/tbody/tr/td/fieldset/table/tbody/tr/td/select")))
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
		int sizeExplorator = new Select(driver.findElement(By.xpath("//td/select"))).getOptions().size();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (sizeExplorator > 0) {
			selectChild();
			for (int i = 1; i <= sizeExplorator; i++) {
				String selectedChild = driver.findElement(By
						.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option["
								+ i + "]"))
						.getAttribute("value");
				
				driver.findElement(By
						.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option["
								+ i + "]")).click();
				
				/*
				 * Try to doucle click.
				 */
				act.doubleClick(driver.findElement(By
						.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option["
								+ i + "]")))
						.perform();
				
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
			String firstChild = driver
					.findElement(By
							.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option[1]"))
					.getAttribute("value");
			driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if ((driver
					.findElement(By
							.xpath("/html/body/div[2]/div[2]/div/div[3]/div/div[3]/div/div[3]/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr/td/fieldset/table/tbody/tr[3]/td/select/option[1]"))
					.getAttribute("value")).toString().equals(firstChild.toString())) {
				driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
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
}
