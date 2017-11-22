package com.thalesgroup.config.scdm.instancename;

import com.thalesis.config.business.attributecomputer.instancename.InstanceNameFactory;
import com.thalesis.config.business.datamodel.CompositionDefinition;
import com.thalesis.config.business.datamodel.DataModel;
import com.thalesis.config.business.datamodel.ObjectDefinition;
import com.thalesis.config.utils.CommonConst;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;


/**
 * Title: DefaultUtility
 * Description: Classe permettant de proposer un nom d'instance
 *              � l'op�rateur lors de la cr�ation d'un noeud
 * Copyright:    Copyright (c) 2002
 * Company: Thales IS
 */
/*
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *                                 THALES IS
 * NOM    : InstanceName
 *
 * DESCRIPTION :Classe permettant de proposer un nom d'instance
 *    � l'op�rateur lors de la cr�ation d'un noeud
 *
 * CONTENU :
 *   - getNameForObject
 *
 * CREATION : 29 Juillet 2004 / BUN Mono
 * MODIFICATIONS :
 *   - n�ant
 *
 *-----------------------------------------------------------------
*/
public final class InstanceName implements InstanceNameFactory {

  /*
   *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   * NOM : getNameForObject
   *
   * DESCRIPTION : Calcul le nom par d�faut d'un objet � partir de
   * sa classe et de son objet type
   *
   * ARGUMENTS :
   * (String) objectClass nom de la classe de l'objet
   * (TreeWalker ) walker Treewalker sur la configuration plac� sur le p�re
   * (SAXElement) templateObject SAXElement de l'objet type
   * 			 (ou null si l'op�rateur n'a rien saisi)
   *
   * VALEUR DE RETOUR : (String)  nom de l'objet
   *
   * CONTEXTE :  -
   *
   * CREATION : 29  Juillet 2004 / BUN Mono
   *
   *---------------------------------------------------------------
  */

   public String getNameForObject( String objectClass, SAXElement templateObject, TreeWalker walker)
	{
		if(templateObject != null)
		{
			return templateObject.getAttribute("nom_instance") ;
		}
		else
		{
//			TreeWalker walkToAncestorNode = walker.walkToAncestorNode(1);
			SAXElement currentNode = walker.getCurrentNode();
			String count = currentNode.getAttribute(CommonConst.COLNAME_CHILDREN_COUNT);
			if (count==null) {
				count = String.valueOf(walker.countChildren());
			}
			String tagName = currentNode.getTagName();
			ObjectDefinition objectDefinition = DataModel.getInstance().getObjectDefinition(tagName);
			// get the first possible role from tagName class to objectClass
			CompositionDefinition compositionDefinition = objectDefinition.getCompositionDefinitionFor(objectClass);
			String roleName = compositionDefinition.getRoleName();
			return roleName + CommonConst.ROLE_INDEX_SEPARATOR + count;
		}

	}

}