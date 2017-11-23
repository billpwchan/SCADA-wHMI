package com.thalesis.config.business.constraints;

import org.xml.sax.*;
import java.util.*;
import java.io.*;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.extensions.TestSetup;
import org.xml.sax.helpers.DefaultHandler;

import com.thalesis.config.utils.Chrono;
import com.thalesis.config.utils.Flag;
import com.thalesis.config.exception.NoSuchObjectException;
import com.thalesis.config.exception.BugException;

import com.thalesis.config.business.component.library.LibraryManager;
import com.thalesis.config.business.engine.ErrorLogger;
import com.thalesis.config.business.engine.SubscriberContainer;

import com.thalesis.config.persistence.ConfigurationAdapter;
import com.thalesis.config.persistence.VEDAdapter;
import com.thalesis.config.persistence.VDCAdapter;

import com.thalesis.config.storage.sql.BatchTransactionalStrategy;
import com.thalesis.config.storage.sql.TransactionalStrategy;

/** 
 * This class is a unitary test for the business rules.
 * It is run as a server and no independant running server is needed.
 * An installed server must be available for the run environment.
 */
public class SpecificValidatorTest extends TestCase {

   /** 
    * name of the VDC and of the VED. 
    * VED must be null to check a VDC
    */
   static String VED_NAME = null;
   static String VDC_NAME = "SCS_DEMO";
   /**
    * package containing the validators
    */
   static String classePackage_ = "com.thalesis.config.umcd.validator.";
   /**
    * Name of the validator classes to test
    */
   static String[] loadedClasses_ = new String[]{
   		"UnivNameRule1Validator"
   };
   
   /** resource courante */
   private static ConfigurationAdapter _resource = null;

   Flag flag = new Flag();
   /**
    * Constructor - ConstraintsTest
    */
   public SpecificValidatorTest(String name) {
      super(name);
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite();

      suite.addTest(new SpecificValidatorTest("testCheckSpecificClass"));

      TestSetup wrapper= new TestSetup(suite) {

         public void setUp() throws Exception
         {
            if (null==VED_NAME) {
               _resource = new VDCAdapter(VDC_NAME);
            }
            else {
               _resource = new VEDAdapter(VED_NAME);
            }
         }
         public void tearDown() throws Exception{}
      };

      return wrapper;
   }

   public static void main(String args[]) throws IOException
   {
      String arg1[] = { "-noloading", SpecificValidatorTest.class.getName() };
      junit.textui.TestRunner.main(arg1);
   }

   public void testCheckSpecificClass() throws Exception
   {
      System.out.println("<testCheckSpecificClass>");
      SubscriberContainer SubscribersForCheck_ = new SubscriberContainer();

      for(int i=0;i < loadedClasses_.length; i++)
      {
         System.out.println("Inclusion du validateur "+loadedClasses_[i]);
         ValidatorFactory.loadValidator(classePackage_,loadedClasses_[i],SubscribersForCheck_);
      }
      doSpecificCheck("testCheckSpecificClass", SubscribersForCheck_, 1);
   }


   private void doSpecificCheck(String testName, SubscriberContainer SubscribersForCheck_, int repeat) throws Exception
   {
      try {
         Chrono chrono = new Chrono();

         for (int i = 0; i < repeat; i++)
         {
            chrono.start();
            flag.setState(true);

            //Pour accéléler, on récupère une adapter qui fonctionne en transactionnel
            ConfigurationAdapter config = null;
            TransactionalStrategy transaction = new BatchTransactionalStrategy();

            try
            {
               if (_resource instanceof VEDAdapter) {
                  config = new VEDAdapter(_resource.getName(), transaction);
               }
               else if (_resource instanceof VDCAdapter) {
                  config = new VDCAdapter(_resource.getName(), transaction);
               }

               ErrorLogger errorLogger = new ErrorLoggerForTest();

               ValidatorFactory.checkWithSubstribers(
                   config,
                   LibraryManager.getInstance().getLibrary(VDC_NAME),
                   errorLogger,
                   flag,
                   SubscribersForCheck_);

               System.out.println(testName + " temps du check : " + ((float) chrono.end() / 1000.0));
            }
            catch (NoSuchObjectException ex) {
               throw new BugException(ex);
            }

         }
         System.out.println("<checkWithSubstribers>");
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         fail(ex.toString());
      }
   }

}
