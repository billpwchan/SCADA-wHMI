package com.thalesis.config.business.attributecomputer;

import org.xml.sax.*;
import java.util.*;

import com.thalesis.config.business.engine.SubscriberContainer;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.storage.ResourceStorageManager;

public class DispatcherForTest extends Dispatcher {

  /**
   * Constructeur -
   */
  protected DispatcherForTest(  TreeWalker walker,
                                ComputeContext context,
                                SubscriberContainer subscribers)
  {
    super(walker, context, subscribers);
  }

  /**
   * Constructeur -
   * @param reader parseur des resources en base
   */
  protected DispatcherForTest(  TreeWalker walker,
                                ComputeContext context,
                                ResourceUpdater res,
                                SubscriberContainer subscribers)
  {
    super(walker, context, res, subscribers);
  }

  /**
   * notification aux sous-scripteurs (template method)
   * NOTE : effectue le test pour savoir si le calculateur ppeut �tre effectu�
   * si la valeur � d�j� �t� calcul� alors pas de recalcul
   * NOTE 2 : on test si la passe d'optimum pour le validateur est pass�
   * @param observers est une list des objet � notifier
   */
  protected void doNotifySubscriber(Object subscriber) throws SAXException{
    notifySubscriber(subscriber);
  }

  /**
   * Envoie une notification � tous les observers pour lancer la verification
   * @param subscribers est une list des objet � notifier
   */
  protected void notifySubscriber(Object subscriber) throws SAXException
  {
    try{
      String calculatedValue = ((AttributeComputer) subscriber).compute(     walker_.goToStartNode(),
          context_ );

      //si la valeur est null on note qu'il faudra le calculer plus tard
      if ( null == calculatedValue ) {
        nbAttributesToCompute_ +=   1;
      }
      // sinon on trace le r�sultat
      else  {
        //On Incr�mente le nombre de calcul�
        nbAttributesComputed_ += 1;

        // on prend les 500 premiers caract�res pour la base
        if (calculatedValue.length() > ResourceStorageManager.COLSIZE_ATT_RESOURCE) {
          calculatedValue = calculatedValue.substring(0, ResourceStorageManager.COLSIZE_ATT_RESOURCE - 1);
        }
        System.out.println(walker_.goToStartNode().getCurrentPath() + "." + ((AttributeComputer)subscriber).getAttributeName() + " = " + calculatedValue );
      }
    }
    catch (RuntimeException ex) {
      throw new SAXException(ex);
    }
    catch (Exception ex) {
      throw new SAXException(ex);
    }

  }
}
