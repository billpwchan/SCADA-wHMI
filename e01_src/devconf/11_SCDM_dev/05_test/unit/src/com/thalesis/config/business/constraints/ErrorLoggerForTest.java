package com.thalesis.config.business.constraints;

import com.thalesis.config.business.engine.ErrorLogger;

import junit.framework.*;
import junit.extensions.TestSetup;

public class ErrorLoggerForTest implements ErrorLogger
{
    /**
    * notification en cas d'une erreur
    * @param  ruleID est l'identifiant de l'erreur (qui doit correspondre à une entrée dans le fichier de propriété des erreurs)
    * @param nodeID est l'identifiant du noeud qui est corrompu
    * @param attributeName est le nom de l'attribut qui ne valide pas la règle
    * @param comment est un commentaire supplémentaire sur la notification de l'erreur
    */
    public void error(String ruleID, String nodeID, String attributeName, String comment)
    {
        System.out.println( "ERROR : RULE : " + ruleID + ", NODEID : " + nodeID + ", ATTRNAME : "+
                            attributeName + ", COMMENT : " + comment);
    }

	/** renvoie le nombre maximum d'erreur */
	public int getMaximalCapacity(){ return -1;}

    /**
    * notification en cas d'une erreur grave de type systeme
    * @param message correspond au message de l'erreur
    */
    public void fatalError(String message) {
      System.out.println( "FATAL : " + message);
    }
    /**
    * notification en cas d'une erreur grave de type systeme
    * @param exception correspond à l'exception bloquante levé
    */
    public void fatalError(Exception exception) {
      System.out.println( "EXCEPTION : " + exception.getMessage());
      exception.printStackTrace(System.err);
    }
    /**
    * notification en cas d'une erreur non bloquante
    * @param  ruleID est l'identifiant de l'erreur (qui doit correspondre à une entrée dans le fichier de propriété des erreurs)
    * @param nodeID est l'identifiant du noeud qui est corrompu
    * @param attributeName est le nom de l'attribut qui ne valide pas la règle
    * @param comment est un commentaire supplémentaire sur la notification de l'erreur
    */
    public void warning(String ruleID, String nodeID, String attributeName, String comment){
      System.out.println( "WARN : RULE : " + ruleID + ", NODEID : " + nodeID + ", ATTRNAME : "+
                            attributeName + ", COMMENT : " + comment);
      }
    /**
    * permet de vérifier quand le rapport est plein
    */
    public boolean isFull() {return false;}

	/**
    * permet d'indiquer qu'il y a une erreure fatale dans le rapport
    */
    public boolean isWithErrorFatal() {return false;}

    /** notifie la fin du check */
    public void endCheck() {}
    /** parametrage du handler pour l'arret sur la premiere erreur */
    public void setStopOnError(boolean onOff){}
    /** retourne le nombre d'erreurs */
    public int getNbErrors() { return 0;}
}