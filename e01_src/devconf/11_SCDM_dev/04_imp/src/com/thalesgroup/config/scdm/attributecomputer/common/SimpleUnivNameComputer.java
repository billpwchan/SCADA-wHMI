package com.thalesgroup.config.scdm.attributecomputer.common;


import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/*
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *                                 THALES IS
 * NOM    : SimpleUnivNameComputer
 *
 * DESCRIPTION : Calcul de l'Univname
 *   UNIVNAME = nom_instance
 *
 * CONTENU :  - compute
 *
 * CREATION : 21 Juillet 2004 / BUN Mono
 *
 * MODIFICATIONS :  - néant
 *
 *-----------------------------------------------------------------
*/

public class SimpleUnivNameComputer extends AttributeComputer {

	/*
	 *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * NOM : compute
	 *
	 * DESCRIPTION : calculateur de la valeur de l'attribut déduit
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
	 * CONTEXTE : -
	 * CREATION : 23 Aout 2004 / BUN Mono
	 *
	 *---------------------------------------------------------------
	*/

  public String compute(TreeWalker walker, ComputeContext context)
  {
    SAXElement element = walker.getCurrentNode();
    String Univname = element.getAttribute("nom_instance");
    return Univname.replace( ' ' , '_' );
  }
}
