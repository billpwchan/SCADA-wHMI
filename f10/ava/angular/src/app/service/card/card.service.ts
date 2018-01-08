import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';
import { Subscription } from 'rxjs/Subscription';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { Card, Step, Execution } from '../../model/Scenario';
import { AppSettings } from '../../app-settings';
import { SelectionService } from './selection.service';
import { StepExistsResult, CardExistsResult, CardServiceType } from './card-settings';
import { DbmPollingService } from '../scs/dbm-polling.service';

@Injectable()
export class CardService {

  public static readonly STR_INIT           = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED  = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_UPDATED   = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED  = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_UPDATED   = AppSettings.STR_STEP_UPDATED;

  readonly c = 'CardService';

  // Observable source
  private cardSource = new BehaviorSubject<CardServiceType>(CardServiceType.UNKNOW);

  // Observable cardItem stream
  cardItem = this.cardSource.asObservable();

  cards: Card[] = new Array<Card>();

  dacSimSubscription: Subscription;

  constructor(
    private dbmPollingService: DbmPollingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);
  }

  // Service command
  cardChanged(serviceType: CardServiceType) {
    const f = 'cardChanged';
    console.log(this.c, f);
    console.log(this.c, f, serviceType);
    this.cardSource.next(serviceType);
  }

  notifyUpdate(serviceType: CardServiceType): void {
    const f = 'notifyUpdate';
    console.log(this.c, f);
    console.log(this.c, f, serviceType);

    switch (serviceType) {
      case CardServiceType.CARD_RELOADED: {
        this.cardChanged(CardServiceType.CARD_RELOADED);
      } break;
      case CardServiceType.CARD_EDITED: {
        this.cardChanged(CardServiceType.CARD_EDITED);
      } break;
      case CardServiceType.CARD_UPDATED: {
        this.cardChanged(CardServiceType.CARD_UPDATED);
      } break;
      case CardServiceType.STEP_RELOADED: {
        this.cardChanged(CardServiceType.STEP_RELOADED);
      } break;
      case CardServiceType.STEP_EDITED: {
        this.cardChanged(CardServiceType.STEP_EDITED);
      } break;
      case CardServiceType.STEP_UPDATED: {
        this.cardChanged(CardServiceType.STEP_UPDATED);
      } break;
    }
  }

  // Validite
  cardExists(cardId: string, stepId: number): CardExistsResult {
    let ret = CardExistsResult.UNKNOW;
    const card: Card = this.getCard([cardId]);
    if ( card ) {
      ret = CardExistsResult.CARD_FOUND;
    } else {
      ret = CardExistsResult.CARD_NOT_FOUND;
    }
    return ret;
  }

  // Validite
  stepExists(cardId: string, stepId: number): StepExistsResult {
    let ret = StepExistsResult.UNKNOW;
    const cards: Card[] = this.getCards([cardId]);
    if ( cards.length > 0 ) {
      const steps: Step[] = this.getSteps(cardId, [stepId]);
      if ( steps.length > 0 ) {
        ret = StepExistsResult.STEP_FOUND;
      } else {
        ret = StepExistsResult.STEP_NOT_FOUND;
      }
    } else {
      ret = StepExistsResult.CARD_NOT_FOUND;
    }
    return ret;
  }

  /**
   * Return Ref of card object
   *
   * @param {string[]} [cardIds] Identity of the card
   * @returns {Card} Ref of card object
   * @memberof CardService
   */
  getCard(cardIds?: string[]): Card {
    return this.getCards(cardIds)[0];
  }

  /**
   * Return Ref of card objects
   *
   * @param {string[]} [identitys] Identity of the cards
   * @returns {Card[]} Ref of card objects
   * @memberof CardService
   */
  getCards(identitys?: string[]): Card[] {
    if ( null == identitys ) {
      return this.cards;
    } else {
      const cards: Card[] = new Array<Card>();
      identitys.forEach(item1 => {
        this.cards.forEach(item2 => {
          if ( item2.name === item1 ) {
            cards.push(item2);
          }
        });
      });
      return cards;
    }
  }

  /**
   * Set a Ref of card objects (Replace existing cards ref)
   *
   * @param {Card[]} cards New Ref of the card objects
   * @memberof CardService
   */
  setCards(cards: Card[]): void { this.cards = cards; }

  /**
   * Add an Ref of card objects (Push at end of cards list)
   *
   * @param {Card[]} cards Ref of the card objects to insert
   * @memberof CardService
   */
  addCards(cards: Card[]): void {
    cards.forEach(card => {
      this.cards.push(card);
    });
  }

  /**
   * Remove the cards by the identitys
   *
   * @param {string[]} identitys List of identity to remove
   * @memberof CardService
   */
  deleteCards(identitys: string[]) {
    const f = 'deleteCard';
    console.log(this.c, f);

    identitys.forEach(element => {
      for ( let i = 0 ; i < this.cards.length ; ++i ) {
        console.log(this.c, f, 'cards[i].name', this.cards[i].name);
        if ( this.cards[i].name === element ) {
          console.log(this.c, f, 'remove', 'name', element);
          this.cards.splice(i, 1);
        }
      }
    });
  }

  getStep(cardIdentity: string, stepIdentifys?: number[]): Step {
    return this.getSteps(cardIdentity, stepIdentifys)[0];
  }

  // Return by Value
  getSteps(cardIdentity: string, stepIdentifys?: number[]): Step[] {
    let steps: Step[] = null;
    const card: Card = this.getCard([cardIdentity]);
    if ( card != null ) {
      steps = new Array<Step>();
      if ( null != stepIdentifys ) {
        stepIdentifys.forEach(element => {
          card.steps.forEach( step => {
            if ( step.step === element ) {
              steps.push(step);
            }
          });
        });
      } else {
        card.steps.forEach( step => {
          steps.push(step);
        });
      }
    }
    return steps;
  }

  setSteps(cardIdentify: string, steps: Step[]): void {
    const card: Card = this.getCard([cardIdentify]);
    if ( null != card ) {
      card.steps = steps;
    }
  }

  deleteStep(cardId: string, stepIds: number[]): void {
    const f = 'deleteStep';
    console.log(this.c, f);
    const card: Card = this.getCard([cardId]);
    if ( null != card ) {
      for ( let x = 0 ; x < stepIds.length ; ++x ) {
        console.log(this.c, f, 'delete', 'stepIds[x]', stepIds[x], 'x', x);
        for ( let y = 0 ; y < card.steps.length ; ++y ) {
          const step: Step = card.steps[y];
          console.log(this.c, f, 'delete', 'stepIds[x]', stepIds[x]);
          if ( step.step === stepIds[x] ) {
            console.log(this.c, f, 'delete', 'y', y);
            card.steps.splice(y, 1);
          }
        }
      }
    } else {
      console.warn(this.c, f, 'card IS NULL');
    }
  }

  initCards(cards: Card[]): Card[] {
    const f = 'initCards';
    console.log(this.c, f);
    cards.forEach( card => {
      card = this.initCard(card);
    } );
    return cards;
  }

  initCard(card: Card): Card {
    const f = 'initCard';
    console.log(this.c, f);

    // Init card state
    const step = 0;

    console.log(this.c, f, 'Target card.name[' + card.name + '] step[' + step + ']');

    // Reset state and execute
    card.steps.forEach ( item => {
    });

    return card;
  }
}
