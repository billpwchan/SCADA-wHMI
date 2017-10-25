package com.thalesgroup.config.scdm.extract;

import org.xml.sax.Attributes;

import com.thalesis.config.exchange.extract.AliasFactory;
import com.thalesis.config.exchange.extract.ExtractKeys;

/******************************************************************************/
/*  FILE  : AliasUtility.java */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/10/26                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/** <p>This class implements the interface AliasFactory with the default alias construction :</p>
 *  <p>trigramme + '_' + libelle_configuration</p> 
 */
public class AliasUtility 
	extends com.thalesis.config.exchange.extract.AliasUtility
	implements AliasFactory { 
	/**
	 * <p>Compute the alias of the point from its trigramme and attributes.</p>
	 * <p>This implementation does not use the </p> 
	 * <table border="1">
	 * <tr><td>Original Char</td><td>replaced with</td>
	 * <tr><td>c<48</td><td>_</td>
	 * <tr><td>c>57 and c<65</td><td>_</td>
	 * <tr><td>c>90 and c<97</td><td>_</td>
	 * <tr><td>c>122</td><td>_</td>
	 * </table>
	 * @param trigramme short name of the object class, can be null or empty
	 * @param instanceAttributes attributes of the instance
	 */
	public String computeAlias(String trigramme, Attributes instanceAttributes)
	{
		String libelle = instanceAttributes.getValue(getAttributeForAlias());
	    if (libelle == null) {
	    	libelle = ExtractKeys.EMPTY_VALUE;
	    }
	    return computeAlias(trigramme, libelle);
	}
	
	/** nom de l'attribut libelle de configuration */
	static public String getAttributeForAlias() {
		return "UNIVNAME";
	}
	
    /**
	 * <p>Ensure that the string does not contain fordidden characters.</p>
	 * <p>The table of convertion for forbidden characters is :</p> 
	 * <table border="1">
	 * <tr><td>Original Char</td><td>replaced with</td>
	 * <tr><td>c<48 or (c>57 and c<65) or (c>90 and c<97) or c>122 or c!=45</td><td>_</td>
	 * </table>
	 * @param trigramme not used
	 * @param label second part of the alias
	 * @return alias of the point.
	 */
	public String computeAlias(String trigramme, String label) { 
		StringBuffer alias = new StringBuffer();
		char[] libelle_char_array = label.toCharArray();
		for (int i = 0; i < libelle_char_array.length; i++) {
			if (!(libelle_char_array[i] >= 97 && libelle_char_array[i] <= 122)
					&& !(libelle_char_array[i] >= 65 && libelle_char_array[i] <= 90)
					&& !(libelle_char_array[i] >= 48 && libelle_char_array[i] <= 57)
					&& !(libelle_char_array[i] == 45)) {
				libelle_char_array[i] = '_';
			}
		}
		alias.append(libelle_char_array);
		return alias.toString();
	}
}
