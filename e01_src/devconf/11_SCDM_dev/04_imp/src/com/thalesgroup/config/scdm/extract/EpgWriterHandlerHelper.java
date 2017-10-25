package com.thalesgroup.config.scdm.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesis.config.exception.PropertyNotFoundException;
import com.thalesis.config.utils.AttributesIterator;
import com.thalesis.config.utils.IOUtility;
import com.thalesis.config.utils.PropertyManager;
import com.thalesis.config.utils.UnsyncStack;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.VectorUtility;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.AttributesImpl;

/******************************************************************************/
/*  FILE  : EpgWriterHandlerHelper.java										  */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/01/26                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/**
 * <p>Writer d'écriture des fichiers de conf pour l'EPG :
 * Un fichier par classe contenant la somme des variables trouvées sous les équipements de la classe </p>
 */
public class EpgWriterHandlerHelper extends DefaultHandler {


    private static String SCS_CLASS_ID_ATT="class_id";

    // Map contenant toutes les équipements avec EPG à générer - remplie au cours du parsing
    private Map<String, Map<String, AttributesImpl>> equipments_ = null;

    // Répertoire de sortie pour les fichiers epg
    private String extractDirectory_ = null;

    // Pile LIFO des équipments courants
    private UnsyncStack stack_ = new UnsyncStack();

    private AttributesImpl currentVariableAtts_ = null;
    
    // If true the file name of the epg file is equal to the attribute named 'type'
    // else the file name of the epg file is equal to the class name,
    // set by the property 'exchange.epg.gen.by.attribute.type' in 'exchange.prop' file,
    // the default value in the property file is true.
    static private boolean GEN_BY_ATTR_TYPE = true;
    static {
    	try {
    		String strtmp = PropertyManager.getProperty( "exchange.epg.gen.by.attribute.type" );
    		GEN_BY_ATTR_TYPE = Boolean.parseBoolean( strtmp );
    	} catch( PropertyNotFoundException e ) {
    		GEN_BY_ATTR_TYPE = true;
    	}
    }
    
    /** Constructeur
    * @param schemaClassDefinions définition des classes à exporter
    * @param extractDirectory répertoire dans lequel on doit mettre le résultat
    */
    public EpgWriterHandlerHelper(	String extractDirectory ) {
        extractDirectory_ = extractDirectory;
    }

    /**
     * Receive notification of the beginning of the document.
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument () throws SAXException
    {
        equipments_ = new HashMap<String, Map<String, AttributesImpl>>();
        currentVariableAtts_ = null;
    }

    /**
    * Filtre le début de l'élément pour extraire les infos.
    * @param uri le namespace de l'element, ou une chaine vide.
    * @param localName le nom local de l'element, ou une chaine vide.
    * @param qName le nom de l'élément, ou une chaine vide.
    * @param atts attributs de l'élément.
    * @exception org.xml.sax.SAXException le client peut lever un exception.
    * @see org.xml.sax.ContentHandler#endElement
    */
    @SuppressWarnings("unchecked")
	public void startElement(String uri, String localName, String qName, Attributes atts)
        throws SAXException {
        Map<String, AttributesImpl> currentEquipment = null;
        // S'il s'agit d'un équipement
        if (isEquipment(qName, atts)) {
        	String objtype = qName;
        	if( GEN_BY_ATTR_TYPE ) {
	            String strtmp = atts.getValue("scstype");
	            if( strtmp != null 
	            		&& !"".equals(strtmp) )
	            {
	                objtype = strtmp;
	            }
        	}
            
            currentEquipment = equipments_.get(objtype);
            if (null == currentEquipment)
            {
                currentEquipment = new HashMap<String, AttributesImpl>();
                equipments_.put(objtype, currentEquipment);
            }
        } else if (isVariable(qName,atts)) {
        	// null value is used as information to know if
        	// we have to process the under vector elements
        	currentVariableAtts_ = null;
        	
        	// S'il s'agit d'une variable
            // On stocke les attributs de la variable dans la liste des attributs de l'équipement courant
        	Map<String, AttributesImpl> attributes = (Map<String, AttributesImpl>) stack_.peek();
            if (null != attributes) {
                AttributesImpl attImpl = new AttributesImpl(atts);
                if ( ! "999".equals(attImpl.getValue("hmiOrder")))
                {
                    currentVariableAtts_ = attImpl;
                    attributes.put(attImpl.getValue("nom_instance"), attImpl);
                }  else {
                    Logger.EXCHANGE.debug("EPG - hmiOrder=999 for " + qName);
                } 
            }
        } else if (isAAL(atts)) {
          // if there is an AAL under an ACI, store the 
          // there must be only one AAL per ACI (see SCDM)
          if (currentVariableAtts_ != null ) {
            currentVariableAtts_.addAttribute("", "", "AAL", "", atts.getValue("nom_instance"));
          }
        // pour optimiser le traitement on sait a priori que les attributs de la variable recherchés
        // (valeur, label, etat et valeurs limites) sont des vecteurs
        } else if (VectorUtility.isElementAVector(qName)) { 
            // S'il s'agit d'une ValueTable de AAL, DAL ou DIO, il faut récupérer les valeurs et les labels
            // et compléter la liste d'attributs
            // on est dans un equipement avec EPG voulu
            if( currentVariableAtts_ != null ) {
            	AttributesImpl attImpl = new AttributesImpl(atts);
            	AttributesIterator valueIterator = VectorUtility.iterator(attImpl);
            	String baseName = "";
            	if (qName.endsWith("valueTable_value")) {
            		baseName = "INT";
            	} else if (qName.endsWith("valueTable_label")) {
            		baseName = "STR";
            	} else if (qName.endsWith("valueTable_dovname")) { // DAL only
            		baseName = "INTdov";
            	} else if (qName.endsWith("valueTable_state")) { // AAL, DAL
            		baseName = "STA";
            	} else if (qName.endsWith("valueLimits")) { // AAL and AIO only
            		baseName = "LIM";
            	}
            	if( !"".equals(baseName) ) {
            		int indice = 0;
            		while (valueIterator.hasNext())
            		{
            			String valueVec = (String)valueIterator.next();
            			currentVariableAtts_.addAttribute("", "", baseName + Integer.toString(indice++), "", valueVec);
            		}
            	}
            }
        }
        // sinon, on ne fait rien, c'est un vecteur, un lien ou une classe qui ne nous intéresse pas
        // on stocke l'équipement courant dans la pile
        stack_.push(currentEquipment);
    }

    /**
    * Filtre la fin de l'élément pour la mise a jour des infos.
    * @param uri le namespace de l'element, ou une chaine vide.
    * @param localName le nom local de l'element, ou une chaine vide.
    * @param qName le nom de l'élément, ou une chaine vide.
    * @exception org.xml.sax.SAXException le client peut lever un exception.
    * @see org.xml.sax.ContentHandler#endElement
    */
    public void endElement(String namespaceURI, String localName, String qName) throws
        SAXException {

        stack_.pop();
    }

    /**
     * Receive notification of the end of the document.
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endDocument
     */
    public void endDocument() throws SAXException
    {
        Logger.EXCHANGE.info("EPG - Writing epg files");
        // Ecriture des fichiers
        Iterator<Entry<String, Map<String, AttributesImpl>>> valuesIt = equipments_.entrySet().iterator();
        while (valuesIt.hasNext())
        {
            Entry<String, Map<String, AttributesImpl>> equipmentEntry = valuesIt.next();
            try {
                dumpEquipmentToEpgFile(equipmentEntry.getValue(),equipmentEntry.getKey());
            } catch (Exception e)
            {
                throw new SAXException(e);
            }
        }
        // Vidage des maps
        equipments_ = null;
    }

    /**
     * Indicates if the class as an EPG file.
     * @param qName name of the class
     * @return true if the class as an EPG file
     */
    private boolean isEquipment(String qName, Attributes atts)
    {
        String classId = atts.getValue("user_class");
        if (null==classId) return false;

        int id = 0;
        try
        {
            id = Integer.parseInt(classId);
        } catch (Exception e) { return false;}
        // by convention an equipment has a classid between 1000 and 2000 (see DpcEqp)
        return id >= 1000 && id <= 2000;
    }

    /**
     * Indicates if the class is a class of variable that must be written in the EPG File
     * @param qName name of the class
     * @param atts attributes of the instance to get the class_id att.
     * @return true if the class is a variable class that must be written in the EPG file
     */
    private boolean isVariable(String qName, Attributes atts)
    {
        /** Une variable est une classe dont l'ID est un des suivants :
         * 104 : DCI
         * 105 : DIO
         * 106 : ACI
         * 107 : SCI
         * 108 : AIO
         * 109 : SIO
         */
        // Récupération de l'attribut class_id
        String classId = atts.getValue(SCS_CLASS_ID_ATT);
        if (null==classId) return false;

        int id = 0;
        try
        {
            id = Integer.parseInt(classId);
        } catch (Exception e) {}

        return id >= 104 && id <= 109;
    }

        /**
     * Indicates if the class is a AAL
     * @param atts attributes of the instance to get the class_id att.
     * @return true if the class is a AAL class
     */
    private boolean isAAL(Attributes atts)
    {
        /*606 : AAL  */
        // Récupération de l'attribut class_id
        String classId = atts.getValue(SCS_CLASS_ID_ATT);
       if (null==classId) return false;

        int id = 0;
        try
        {
            id = Integer.parseInt(classId);
        } catch (Exception e) {}

        return id == 606; // classid found in scs_source/src/inc/scsDBConst.h
    }

    /**
     * ecrit un fichier EPG pour une classe
     * @param equipment Map contenant les attributs des variables à écrire
     * @param eqName Nom de la classe
     */
    private void dumpEquipmentToEpgFile(Map<String, AttributesImpl> equipment, String eqName) throws IOException
    {
        if (null == equipment) {
        	Logger.EXCHANGE.debug("  - eqpt " + eqName + ", no variables");
        	return;
        }

        // Ouverture du fichier

        File outputFile = new File(extractDirectory_ + File.separatorChar + eqName + ".epg");
        FileWriter fw = new FileWriter(outputFile);

        // Ecriture de l'entête
        StringBuffer buffer = new StringBuffer();
        buffer.append("# Interface file for Inspector panel production\n# This file has been generated automatically by SCADASoft Configurator.\n# Generated on ");
        buffer.append(Calendar.getInstance().getTime().toString());
        buffer.append("\nEQMT ");
        buffer.append(eqName);
        buffer.append("\n");
        fw.write(buffer.toString());
        buffer.setLength(0);

        // Boucle sur les variables
        Iterator<AttributesImpl> valuesIt = equipment.values().iterator();
        while (valuesIt.hasNext()) {
            AttributesImpl atts = valuesIt.next();
            String classID = atts.getValue(SCS_CLASS_ID_ATT);
            int id = 0;
            try {
                id = Integer.parseInt(classID);
            } catch (Exception e) {}

            String varType = "";
            switch (id) {
                case 104 : {
                    varType = "DCI";
                    break;
                }
                case 105 : {
                    varType = "DIO";
                    break;
                }
                case 106 : {
                    varType = "ACI";
                    break;
                }
                case 107 : {
                    varType = "SCI";
                    break;
                }
                case 108 : {
                    varType = "AIO";
                    break;
                }
                case 109 : {
                    varType = "SIO";
                    break;
                }
                default : {
                    // on ignore la variable, classID inconnue
                    String varID = atts.getValue("ID");
                    Logger.EXCHANGE.error("Le numéro de classe de la variable + " + varID + " est inconnu : " + classID);
                    continue;
                }
            }
            buffer.append("V");
            buffer.append(varType);
            buffer.append(" ");
            buffer.append(atts.getValue("nom_instance"));
            buffer.append("\n ORD ");
            String hmiOrder = atts.getValue("hmiOrder");
            if ("".equals(hmiOrder)) hmiOrder = "0";
            buffer.append(hmiOrder);
            buffer.append("\n LBL ");
            buffer.append(atts.getValue("label"));
            buffer.append("\n");

            // Ecriture des tuples (valeur, ibellés, valeurlimites, etat)
            String baseName = "INT";
            String baseNamedov = "INTdov";
            String labelToken = "STR";
            String limitToken = "LIM";
            String stateToken = "STA";
            int indice = 0;
            String attValue = atts.getValue(baseName + Integer.toString(indice));
            String dovValue = atts.getValue(baseNamedov + Integer.toString(indice));
            String label = atts.getValue(labelToken + Integer.toString(indice));
            String state = atts.getValue(stateToken + Integer.toString(indice));
            while (attValue != null ) {
              if (!"".equals(attValue)) { // DCI et DIO
                buffer.append(" " + baseName + " ");
                buffer.append(attValue);
                if ("DIO".equals(varType)) {  // DIO
                  buffer.append(" ");
                  buffer.append(dovValue);
                }
                buffer.append("\n");
              }
              if (label != null && !"".equals(label)) {  // ACI, DCI avec AAL ou DAL
                buffer.append(" " + labelToken + " ");
                buffer.append(label);
                buffer.append("\n"); // pour eviter le parsing complique a la lecture on met une varibale par ligne
              }
              if (state != null && !"".equals(state)) {  // ACI, DCI avec AAL ou DAL
                buffer.append(" " + stateToken + " ");
                buffer.append(state);
                buffer.append("\n");
              }
              indice++;
              attValue = atts.getValue(baseName + Integer.toString(indice));
              dovValue = atts.getValue(baseNamedov + Integer.toString(indice));
              label = atts.getValue(labelToken + Integer.toString(indice));
              state = atts.getValue(stateToken + Integer.toString(indice));
            }
            String aalToken = "AAL";
            String aalName = atts.getValue(aalToken);
            if (null != aalName) {
              buffer.append(" " +  aalToken + " ");
              buffer.append(aalName);
              buffer.append("\n");
            }
            // traverse valueLimits
            indice = 0;
            String limitValue = atts.getValue(limitToken + Integer.toString(indice));
            while (limitValue != null ) {
              if (!"".equals(limitValue)) { // AIO
                buffer.append(" " +  limitToken + " ");
                buffer.append(limitValue);
                buffer.append("\n");
              }
              indice++;
              limitValue = atts.getValue(limitToken + Integer.toString(indice));
            }

            fw.write(buffer.toString());
            buffer.setLength(0);
        }
        
        IOUtility.close(fw);
    }
}