package com.thalesis.config.business.attributecomputer;

import junit.framework.*;
import junit.extensions.TestSetup;
import java.io.IOException;
import org.xml.sax.ContentHandler;

import com.thalesis.config.utils.Flag;
import com.thalesis.config.utils.HierarchyUtility;

import com.thalesis.config.business.component.library.LibraryManager;
import com.thalesis.config.business.engine.SubscriberContainer;
import com.thalesis.config.business.engine.ParsingTreeWalker;

import com.thalesis.config.persistence.ConfigurationAdapter;
import com.thalesis.config.persistence.VEDAdapter;
import com.thalesis.config.persistence.VDCAdapter;


/**
 * Test class for compute of attributes
 * It is run as a server and no independant running server is needed.
 * An installed server must be available for the run environment.
 * By default, all the computers are activated. It can be usefull to activate only
 * one computer, in this case use the CLASS_NAME and ATT_NAME parameters.
 */
public class ConfigComputerUtilityTest extends TestCase
{
	/** 
	 * name of the VDC and of the VED. 
	 * VED must be null to check a VDC
	 */
	private static String VED_NAME = null;
	private static String VDC_NAME = "SCS_DEMO";

	/**
	 * To activate only one computer, give the name of the class
	 * and of the attribute to compute (as defined in the datamodel).
	 * startElement_ indicates if the computer is activated at the startElement event (endElement by default)
	 */
	private static String CLASS_NAME = "dci_type";
	private static String ATT_NAME = "UNIVNAME";
	private static boolean startElement_ = false;
	   
	   
   public ConfigComputerUtilityTest(String name) {
      super(name);
   }
   public ConfigComputerUtilityTest() {
      super("withoutName");
   }
   /** resource courante */
   private static ConfigurationAdapter _resource = null;

   /** nom de l'operateur */
   private static String opName = null==VED_NAME ? VDC_NAME : VED_NAME;
   /** commentaire */
   private static String comment = "Unitary test for the resource " + opName;

   public static Test suite() {
		TestSuite suite = new TestSuite("ConfigComputerUtilityTest");
		if (null == CLASS_NAME) {
			suite.addTest(new ConfigComputerUtilityTest("testCompute"));
		} else {
			suite.addTest(new ConfigComputerUtilityTest("testSingleComputer"));
		}

		TestSetup wrapper = new TestSetup(suite) {
			public void setUp() throws Exception {
				if (null == VED_NAME) {
					_resource = new VDCAdapter(VDC_NAME);
				} else {
					_resource = new VEDAdapter(VED_NAME);
				}
			}
		};

		return wrapper;
	}

   public static void main(String args[]) throws IOException {
      String arg1[] = {
	  "-noloading", ConfigComputerUtilityTest.class.getName()};
      junit.textui.TestRunner.main(arg1);
   }

   /**
    * Test for computing all computers
    */
   public void testCompute() {
      System.out.println("<testCompute>");
      long time = System.currentTimeMillis();
      try {
         HierarchyUtility.setRootId(":R");
         HierarchyUtility.setAlphaRootID(":R:A");
         HierarchyUtility.setGraphicRootID(":R:G");
	 ConfigComputerFactory.getSingleton().computeAllAttributesIn( (
	     ConfigurationAdapter) _resource,
	     LibraryManager.getInstance().getLibrary(VDC_NAME),
	     new Flag(),"rr");
      }
      catch (Exception ex) {
         ex.printStackTrace();
	 //pour tous les problèmes hors problème de convergence
	 if (ex.toString().indexOf("converg") == -1) {
	    System.out.println("error : " + ex.toString());
	    fail(ex.toString());
	 }
	 else {
	    System.out.println("pas de convergence!! " + ex.toString());
	 }
      }
      time = System.currentTimeMillis() - time;
      System.out.println( (time / 100) + "</testCompute>");
   }

   /**
    * Test for a single computer
    */
   public void testSingleComputer() {
      System.out.println("<testSingleComputer>");
      long time = System.currentTimeMillis();
      try {
         HierarchyUtility.setRootId(":R");
         HierarchyUtility.setAlphaRootID(":R:A");
         HierarchyUtility.setGraphicRootID(":R:G");

         SubscriberContainer subscriber = new SubscriberContainer();
         //on récupère le bon computer
         AttributeComputer computer = AttributeComputerHome.getInstance().getComputerFor(CLASS_NAME, ATT_NAME);
         //fixe le niveau pour l'évaluation
         computer.setEvaluationPass(1);
         //fixe le nom de l'attribut
         computer.setAttributeName(ATT_NAME);

         // startElement ou endElement
         if (startElement_) {
         	subscriber.addStartElementObserver(CLASS_NAME, computer );
         } else {
            subscriber.addEndElementObserver(CLASS_NAME, computer );
         }

         //création du walker
         ParsingTreeWalker walker = new ParsingTreeWalker(_resource, -1);

         //création du context
         ComputeContext context = new ComputeContext(_resource, LibraryManager.getInstance().getLibrary(VDC_NAME));

         //création du dispatcher
         DispatcherForTest dispatcher = new DispatcherForTest(walker, context, subscriber);

         //construction du handler SAX
         //on place le dispatcher
         ContentHandler handler = dispatcher;

         //on ajoute le filtre pour les noeuds skelettiques si nécessaire
         if (_resource.mayContainSkeletonNode()) {
             handler = ConfigurationAdapter.newSkeletonFilter().setParent(handler);
         }
         //puis le filtre mettant à jour le walker
         handler = walker.getSAXFilter().setParent(handler);

         // On lance le parsing
         _resource.getContentAsSAX(handler, ConfigurationAdapter.getAlphaRootId(), 0, true);
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
      finally {
        time = System.currentTimeMillis() - time;
        System.out.println("Elapsed time = " + (time / 100) + " s");
        System.out.println("</testSingleComputer>");
      }
   }

}