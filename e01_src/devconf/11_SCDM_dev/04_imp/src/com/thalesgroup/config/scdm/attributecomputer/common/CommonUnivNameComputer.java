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
 * MODIFICATIONS :  - n�ant
 *
 *-----------------------------------------------------------------
*/

public class CommonUnivNameComputer extends AttributeComputer {

	/*
	 *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * NOM : compute
	 *
	 * DESCRIPTION : Calul de l'UNIVNAME
	 * 				 UNIVNAME = UNIVNAME(P�re) + nom_instance
	 *
	 * ARGUMENTS :
	 * (TreeWalker)  walker :  walker initialiser sur la position
	 * 			     		   courante de l'instance
	 * (ComputeContext)  context : contexte courant o� s'effectue
	 * 			                   le calcul
	 *
	 * VALEUR DE RETOUR :
	 * ( String )  valeur de l'attribut ou de la fonction
	 *
	 * CONTEXTE : -
	 * CREATION : 23 Aout 2004 / BUN Mono
	 *
	 *---------------------------------------------------------------
	*/
	  public String compute(TreeWalker walker, ComputeContext context)
  	  {
    		// R�cup�ration de l'�l�ment
    		SAXElement element = walker.getCurrentNode();
    		// r�cup�ration du libell�
    		String nom_instance = element.getAttribute("nom_instance");

    		// d�placement sur le p�re
    		walker.walkToParentNode();
    		SAXElement parentElement = walker.getCurrentNode();
    		if (parentElement == null)
   			{
      			return NOT_CALCULABLE;
   			}

    		String univnameParent = parentElement.getAttribute("UNIVNAME");
    		if ( NOT_CALCULABLE.equals(univnameParent) || "".equals(univnameParent))
    		{
      			return NOT_CALCULABLE;
    		} 
    		else if ( univnameParent == null ) {
    			univnameParent = "";
    		}
    		else if ( NOT_COMPUTED_VALUE.equals(univnameParent))
    		{
    			return null;
    		}

			String Univname = univnameParent + nom_instance ;
    		return Univname.replace( ' ' , '_' );
    }
}