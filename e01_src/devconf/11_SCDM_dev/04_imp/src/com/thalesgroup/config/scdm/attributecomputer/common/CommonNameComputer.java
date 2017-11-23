package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/*
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *                                 THALES IS
 * NOM    : CommonUnivNameComputer
 *
 * DESCRIPTION : Classe de calcul d'un UNIVNAME
 *
 * CONTENU :  - compute
 *
 * CREATION : 21 Juillet 2004 / BUN Mono
 *
 * MODIFICATIONS :  - néant
 *
 *-----------------------------------------------------------------
*/

public class CommonNameComputer extends AttributeComputer {

	/*
	 *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * NOM : compute
	 *
	 * DESCRIPTION : Calul de l'name
	 * 				 name = name(Père) + : + nom_instance
	 *
	 * ARGUMENTS :
	 * (TreeWalker)  walker :  walker initialiser sur la position
	 * 			     		   courante de l'instance
	 * (ComputeContext)  context : contexte courant où s'effectue
	 * 			                   le calcul
	 *
	 * VALEUR DE RETOUR :
	 * ( String )  valeur de l'attribut ou de la fonction
	 * 
	 *---------------------------------------------------------------
	*/
	  public String compute(TreeWalker walker, ComputeContext context)
  	  {
    		// Récupération de l'élément
    		SAXElement element = walker.getCurrentNode();
    		// récupération du libellé
    		String nom_instance = element.getAttribute("nom_instance");

    		// déplacement sur le père
    		walker.walkToParentNode();
    		SAXElement parentElement = walker.getCurrentNode();
    		if (parentElement == null)
   			{
      			return NOT_CALCULABLE;
   			}

    		String nameParent = parentElement.getAttribute("name");
    		if ( NOT_CALCULABLE.equals(nameParent) || "".equals(nameParent))
    		{
      			return NOT_CALCULABLE;
    		} 
    		else if ( nameParent == null ) {
    			nameParent = "";
    		}
    		else if ( NOT_COMPUTED_VALUE.equals(nameParent))
    		{
    			return null;
    		}

			String name = nameParent + ":" + nom_instance;
    		return name;
    }
}