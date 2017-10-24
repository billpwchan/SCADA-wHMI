package com.thalesis.config.business.constraints.validator;

import junit.framework.*;
import junit.extensions.TestSetup;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Iterator;
import java.util.Set;
import com.thalesis.config.utils.*;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.business.component.ved.VEDManager;
import com.thalesis.config.business.engine.BasicTreeWalker;
import com.thalesis.config.persistence.*;
import com.thalesis.config.exception.*;


public class ValidatorFunctionsTest extends TestCase {
	String vedName = "vedTest";
	String operatorName = "ValidatorFunctionsTest";
	
    public ValidatorFunctionsTest(String name) {
        super(name);
    }
    
    public static void main(String[] args) {
        String arg1[] = { "-noloading", ValidatorFunctionsTest.class.getName() };
        junit.swingui.TestRunner.main(arg1);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ValidatorFunctionsTest.class);

        return suite;
    }

	/**
    * Analyse un path en dissociant le path lui-m�me du nom de l'attribut s'il contient des deref.
    * @param path          le path entier
	* @param walker iterateur sur les donn�es
    * @param attributeName pour permettre le renvoi du nom de l'attribut
    * @return le path sans l'attribut
    */
    public void testAnalyzeDerefPath()
    {
		System.out.println("<testAnalyzeDerefPath>");
		// Test avec un DEREF simple ok
		String derefPath = "DEREF(:R:A:Reseau:DUNKE4G_SYN3:DUNKE4G_SYN3.link_elt_rac_amont):^:^.libelle_configuration";
        try{
        	internalAnalyzeDerefPath(derefPath, ":R:A:Reseau:SENE:DUNKE:DUNKE4", "libelle_configuration");
        }catch( NoSuchObjectException e){
            e.printStackTrace(System.err);
            fail("error : " + e.toString());
        }
		// Test avec un DEREF simple ok et le choix du num�ro de lien
		derefPath = "DEREF(:R:A:Reseau:DUNKE4G_SYN3:DUNKE4G_SYN3.link_elt_rac_amont(0)):^:^.libelle_configuration";
        try{
        	internalAnalyzeDerefPath(derefPath, ":R:A:Reseau:SENE:DUNKE:DUNKE4", "libelle_configuration");
        }catch( NoSuchObjectException e){
            e.printStackTrace(System.err);
            fail("error : " + e.toString());
        }
		// Test avec un DEREF double ok
		derefPath = "DEREF(DEREF(:R:A:Reseau:DUNKE4G_SYN3:DUNKE4G_SYN3.link_elt_rac_amont).link_choix_extremite):^:^.libelle_configuration";
        try{
        	internalAnalyzeDerefPath(derefPath, ":R:A:Reseau", "libelle_configuration");
        }catch( NoSuchObjectException e){
            //normal
            e.printStackTrace(System.err);
            fail("error : " + e.toString());
        }
        // Test avec un DEREF simple et donn�e absente
		derefPath = "DEREF(:R:A:Reseau:DUNKE4G_SYN3:_____SYN3.link_elt_rac_amont):^:^.libelle_configuration";
        try{
        	internalAnalyzeDerefPath(derefPath, "", "libelle_configuration");
        }catch( NoSuchObjectException e){
            //normal
            System.out.println("erreur normale : " + e.toString());
        }
        // Test avec un DEREF simple et attribut lien inexistant
		derefPath = "DEREF(:R:A:Reseau:DUNKE4G_SYN3:DUNKE4G_SYN3.libelle_configuration):^:^.libelle_configuration";
        try{
        	internalAnalyzeDerefPath(derefPath, "", "libelle_configuration");
        }catch( NoSuchObjectException e){
            //normal
            System.out.println("erreur normale : " + e.toString());
        }
        // Test avec un DEREF simple sans attribut
		derefPath = "DEREF(:R:A:Reseau:DUNKE4G_SYN3:DUNKE4G_SYN3):^:^.libelle_configuration";
        try{
        	internalAnalyzeDerefPath(derefPath, "", "libelle_configuration");
        }catch( NoSuchObjectException e){
            //normal
            System.out.println("erreur normale : " + e.toString());
        }

		System.out.println("</testAnalyzeDerefPath>");
    }
    
	/**
     * Analyse un path en dissociant le path lui-m�me du nom de l'attribut s'il contient des deref.
     * @param path          le path entier
 	* @param walker iterateur sur les donn�es
     * @param attributeName pour permettre le renvoi du nom de l'attribut
     * @return le path sans l'attribut
     */
     public void internalAnalyzeDerefPath(String derefPath, String pathExpected, String attExpected) throws NoSuchObjectException
     {
		System.out.println("<DerefPath value='" + derefPath + "'");
        ConfigurationAdapter config= null;
 		try{
 		    config =  new VEDAdapter(vedName);
 		}catch(Exception e){
            e.printStackTrace(System.err);
            System.err.println("error : " + e.toString());
 			fail(e.toString());
 		}
 		BasicTreeWalker walker = new BasicTreeWalker( config, HierarchyUtility.getRoot() );
        StringBuffer attributeName = new StringBuffer();
 		String path = ValidatorFunctions.analyzePath(derefPath, walker, attributeName);
 		if (!pathExpected.equals(path)) fail("Le path attendu n'est pas correct : attendu = " + pathExpected + " trouv� = " + path );
		if (!attExpected.equals(attributeName.toString())) fail("L'attribut attendu n'est pas correct : attendu = " + attExpected + " trouv� = " + attributeName );
		System.out.println("path=" + path + "' attName='" + attributeName + "'>");
     }

     /**
     * Recherche l'ID du noeud quel que soit le type du chemin donn� (absolu ou relatif)
     *
     * @param currentPath   ID absolu du noeud courant
     * @param pathOfAnyKind chemin du noeud absolu ou relatif
     *
     * @return l'ID absolu du noeud
     */
    public  void testGetFullPathFromAny()
    {
       System.out.println("<testGetFullPathFromAny>");
       String currentPath = HierarchyUtility.getRoot();
       ConfigurationAdapter config= null;
       try{
		    config =  new VEDAdapter(vedName);
		}catch(Exception e){
            e.printStackTrace(System.out);
            System.out.println("error : " + e.toString());
			fail(e.toString());
		}
       TreeWalker walker =  new BasicTreeWalker( config, HierarchyUtility.getRoot() );
       String pathOfAnyKind = "DEREF(:R:A:Reseau:SENE:COUDE4ZQ_EC43:COUDE4ZQ_EC43.link_elt_rac_amont):^:^";
       String result = "";
       try{
         result = ValidatorFunctions.getFullPathFromAny(currentPath , walker, pathOfAnyKind);
       }catch(Exception e){
            e.printStackTrace(System.out);
            System.out.println("ERROR : " + e.toString());
			fail (e.getMessage());
       }
       System.out.println(result);
       System.out.println("</testGetFullPathFromAny>");
    }

    /**
    * Analyse un path en dissociant le path lui-m�me du nom de l'attribut s'il contient des deref.
    *
    * @param path          le path entier
	* @param walker iterateur sur les donn�es
    * @param attributeName pour permettre le renvoi du nom de l'attribut
    *
    * @return le path sans l'attribut
    */
    public   void testAnalyzeVectorPath()
    {
		String vectorPath = ":GPH.V_att(0)";
        TreeWalker walker = new TestTreeWalker();

        StringBuffer attributeName = new StringBuffer();
        String path = null;
        try{
		    path = ValidatorFunctions.analyzePath(vectorPath, walker, attributeName);
        } catch ( NoSuchObjectException e){
            e.printStackTrace(System.out);
            System.out.println("error : " + e.toString());
            fail(e.toString());
        }
		System.out.println("path = " + path);
		System.out.println("attribute = " + attributeName);
    }

	//////////////////// INNER CLASS ///////////////////////////
		/**
	 *
	 */
	class TestTreeWalker implements TreeWalker{
    	/** @return le nombre de fils */
    	public int countChildren(){ return -1;}

        /** identifie si le noeud courant est squeletique*/
        public Boolean isCurrentNodeSkeleton(){ return Boolean.FALSE; }
    	/**
    	 * @param type type des fils
    	 * @return le nombre de fils du type sp�cifi�
    	 */
        public int countChildrenNodesOfType(String type){ return -1;}

    	/**
    	 * r�cup�ration de tous les fils
    	 * @return un iterateur sur les fils
    	 */
    	public Iterator getChildrenNodes(){ return null; }

    	/**
    	 * r�cup�ration des fils
    	 * @param type type des fils demand�s
    	 * @return un iterateur sur les fils
    	 */
    	public Iterator getChildrenNodesOfType(String type){ return null;}

 	/**
 	 * r�cup�ration des types des fils
 	 */
   	 public Set getChildrenTypes(){ return null;}

	/**
	 * r�cup�ration de la position
	 * @return nom du noeud courant
	 */
 	public String getCurrentName(){ return null;}

	/** r�cup�ration de l'�l�ment point� */
	public SAXElement getCurrentNode(){
		org.xml.sax.helpers.AttributesImpl atts = new org.xml.sax.helpers.AttributesImpl();
        atts.addAttribute("","","ID","",relativePath_ + ":test");
		atts.addAttribute("","","regionRef","",relativePath_ + ":regionRef");
		atts.addAttribute("","","lk0","",relativePath_ + ":lk0");
        return new SAXElement("testTag", atts);
    }

	/**
    * Methode retoutant le type de l'objet courant
    * NB: utile pour l'optimisation lors de la recherche de l'existance
    * pour les verification de la conformit� des liens
    * */
    public String getCurrentNodeType(){ return "testNodeType";}

	/**
	 * r�cup�ration de la position
	 * @return ID du noeud courant
	 */
	public String getCurrentPath(){ return null;}

	/** @return un iterateur sur la liste des ID des noeuds associ�s */
	public Iterator getLinkedNodeIDs(String linkName){ return null;}

	/**
	 * se d�placer dans la hi�rarchie (au parents, relativement (un descendant d'un parent)
	 * de mani�re absolue
	 * @return this
	 */
	public TreeWalker goToNode(String pathOfAnyKind){ return this;}

	/**
	 * retourne au point de d�part
	 * @return this
	 */
	public TreeWalker goToStartNode(){ return null;}

	/**
	 * se d�placer dans la hi�rarchie en absolu
	 * @return this
	 */
    public TreeWalker jumpToNode(String absolutePath){ return null;}

	/** cr�er un cursor � partir de la position courante */
	public TreeWalker newTreeWalker(){ return null;}

	/**
	 * aller � l'anc�tre
	 * @param level degr� de parent� (level = 2 signifie le grand-p�re)
	 * @return this
	 */
	public TreeWalker walkToAncestorNode(int level){ return null;}

	/**
	 * aller au fils par le nom
	 * @return this
	 */
	public TreeWalker walkToChildNode(String childName){ return null;}

	/**
	 * aller au premier anc�tre (le noeud courant y compris) de type sp�cifi�
	 * si il n'y en a pas, le walker fini nullepart
	 * @param type type de l'anc�tre rechercher
	 * @return this
	 */
	public TreeWalker walkToFirstAncestorNodeOfType(String type){ return null;}
	/** */
    private String relativePath_ = null;
	/**
	 * se d�placer dans la hi�rarchie en relatif
	 * @return this
	 */
    public TreeWalker walkToNode(String relativePath){ relativePath_ = relativePath; return null;}

	/** retourne au p�re
	 * @return this
	 */
	public TreeWalker walkToParentNode(){ return null;}

	/** @return un iterateur sur la liste des valeurs des noeuds */
	public Iterator getVectorNodeValues(String vectorName){  return null;}


    /** met � jour la valeur de l'attribut courant du noeud en base */
    public void updateCurrentAttributeValue( String attributeName, String attributeValue) throws NoSuchObjectException, TechnicalException{
    }
    }

}