import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';
import { Subscription } from 'rxjs/Subscription';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { Card, Step, StepType, CardType, Execution } from '../../model/Scenario';
import { DacSimExecution, EIV, DacSimExecType, ExecResult } from './../../service/scs/dac-sim-settings';
import { AppSettings } from '../../app-settings';
import { SelectionService } from './selection.service';
import { StepExistsResult, CardExistsResult, CardExecType, CardServiceType } from './card-settings';
import { DacSimService } from '../scs/dac-sim.service';
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
    private dacSimService: DacSimService
    , private dbmPollingService: DbmPollingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    this.dacSimSubscription = this.dacSimService.dacSimItem
    .subscribe(item => {
      console.log(this.c, f, 'dacSimSubscription');
      console.log(this.c, f, 'item.execType', item.execType);
      console.log(this.c, f, 'item.cardId', item.cardId);
      console.log(this.c, f, 'item.stepId', item.stepId);
      console.log(this.c, f, 'item.connAddr', item.connAddr);
      console.log(this.c, f, 'item.eivs', item.eivs);
      console.log(this.c, f, 'item.ret', item.ret);

      if ( this.stepExists(item.cardId, item.stepId) === StepExistsResult.STEP_FOUND ) {
        const step: Step = this.getCard([item.cardId]).steps[item.stepId];
        if ( null != step ) {
          console.log(this.c, f, 'item.execType', item.execType);
          if ( DacSimExecType.START === item.execType ) {
            switch (item.ret) {
              case ExecResult.INIT: {
                step.state = StepType.START_RUNNING;
              } break;
              case ExecResult.SENT: {
                step.state = StepType.START_RUNNING;
              } break;
              case ExecResult.FINISH: {
                step.state = StepType.START;
              } break;
              case ExecResult.FAILED: {
                step.state = StepType.START_FAILED;
              } break;
            }
          } else if ( DacSimExecType.STOP === item.execType ) {
            switch (item.ret) {
              case ExecResult.INIT: {
                step.state = StepType.STOP_RUNNING;
              } break;
              case ExecResult.SENT: {
                step.state = StepType.STOP_RUNNING;
              } break;
              case ExecResult.FINISH: {
                step.state = StepType.STOPPED;
              } break;
              case ExecResult.FAILED: {
                step.state = StepType.STOPPED_FAILED;
              } break;
            }
          }
          console.log(this.c, f, 'step.state', step.state);
          this.notifyUpdate(CardServiceType.STEP_UPDATED);
        } else {
          console.log(this.c, f, 'step IS NULL');
        }
      }
    });
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

  isRunning(): boolean {
    const f = 'isRunning';
    console.log(this.c, f);

    let ret = false;
    this.getCards().forEach( item => {
      if (
         CardType.START_RUNNING === item.state
      || CardType.START_PAUSED === item.state
      || CardType.STOP_RUNNING === item.state
      || CardType.STOP_PAUSED === item.state
    ) {
        ret = true;
      }
    });
    console.log(this.c, f, 'ret', ret);
    return ret;
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
      console.log(this.c, f, 'card IS NULL');
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

    if ( null != card.timer ) {
      // Stop the timer
      card.timer.unsubscribe();
      card.timer = null;
    }

    // Init card state
    const cardType = CardType.STOPPED;
    const step = 0;
    const stepType = StepType.STOPPED;

    console.log(this.c, f, 'Target card.name[' + card.name + '] cardType[' + cardType + '] step[' + step + '] stepType[' + stepType + ']');

    // Reset state and execute
    card.state = cardType;
    card.step = step;
    card.steps.forEach ( item => {
      item.state = stepType;
      item.execute = true;
    });

    return card;
  }

  executeStep(cardName: string, stepId: number, stepType: DacSimExecType): void {
    const f = 'executeStep';
    console.log(this.c, f);

    const step: Step = this.getStep(cardName, [stepId]);

    const phase: Execution[] = step.equipment.phases[stepType];

    const eivs: EIV[] = new Array<EIV>();

    phase.forEach(exec => {
      eivs.push(
        new EIV(
          exec.name
          , exec.value
        )
      );
    });

    const dacSimExe: DacSimExecution
     = new DacSimExecution(
                            stepType
                            , cardName
                            , stepId
                            , step.equipment.connAddr
                            , eivs
                            , ExecResult.INIT
                          );

    this.dacSimService.writeEv(dacSimExe);
  }

  executeCard(cardName: string, execType: CardExecType, firstStep: boolean, byPassTimer: boolean): void {
    const f = 'executeCard';
    console.log(this.c, f);
    console.log(this.c, f, 'cardName[' + cardName + '] execType[' + execType + '] firstStep[' + firstStep + ']');

    const execCard: Card = this.getCard([cardName]);

    if ( null != execCard ) {

      let stop = false;

      if ( execType === CardExecType.PAUSE ) {
        // Pause

        // Stop timer
        execCard.timer.unsubscribe();
        execCard.timer = null;

        // Change card status = pause
        switch (execCard.state) {
          case CardType.START_RUNNING: {
            execCard.state = CardType.START_PAUSED;
          } break;
          case CardType.STOP_RUNNING: {
            execCard.state = CardType.STOP_PAUSED;
          } break;
        }

        this.notifyUpdate(CardServiceType.CARD_UPDATED);

        stop = true;

      } else if ( execType === CardExecType.RESUME ) {
        // Resume

        let cardType: CardExecType = CardExecType.UNKNOW;
        switch (execCard.state) {
          case CardType.START_PAUSED: {
            cardType = CardExecType.START;
          } break;
          case CardType.STOP_PAUSED: {
            cardType = CardExecType.START;
          } break;
        }

        this.notifyUpdate(CardServiceType.CARD_UPDATED);

        this.executeCard(cardName, cardType, firstStep, byPassTimer);

        stop = true;
      } else if ( execType === CardExecType.TERMINATE ) {

        // Stop timer
        execCard.timer.unsubscribe();
        execCard.timer = null;

        switch (execCard.state) {
          case CardType.START_RUNNING: {
            execCard.state = CardType.START_TERMINATED;
          } break;
          case CardType.STOP_RUNNING: {
            execCard.state = CardType.STOP_TERMINATED;
          } break;
        }

        this.notifyUpdate(CardServiceType.CARD_UPDATED);

        stop = true;
      }

      if ( !stop) {

        this.dbmPollingService.subscribe(execCard);

        if (
          (
            CardType.START_PAUSED === execCard.state
          || CardType.START_RUNNING === execCard.state
          )
          && CardExecType.STOP === execType
        ) {
          // Terminate
          this.executeCard(execCard.name, CardExecType.TERMINATE, firstStep, byPassTimer);
        } else if (
          (
            CardType.STOP_PAUSED === execCard.state
            || CardType.STOP_RUNNING === execCard.state
          )
          && CardExecType.START === execType
        ) {
          // Terminate
          this.executeCard(execCard.name, CardExecType.TERMINATE, firstStep, byPassTimer);
        }

        // Init
        if ( firstStep ) {
          execCard.step = 0;
          firstStep = false;
        }

        let stepExecType: DacSimExecType;
        switch ( execType ) {
          case CardExecType.START: {
            execCard.state = CardType.START_RUNNING;
            stepExecType = DacSimExecType.START;
          } break;
          case CardExecType.STOP: {
            execCard.state = CardType.STOP_RUNNING;
            stepExecType = DacSimExecType.STOP;
          } break;
        }

        console.log(this.c, f, 'execCard.name         [' + execCard.name + ']');
        console.log(this.c, f, 'execCard.state        [' + execCard.state + ']');
        console.log(this.c, f, 'execCard.step         [' + execCard.step + ']');
        console.log(this.c, f, 'execCard.steps.length [' + execCard.steps.length + ']');
        console.log(this.c, f, 'execCard.steps        [' + execCard.steps + ']');

        if ( execCard.step < execCard.steps.length ) {

          this.notifyUpdate(CardServiceType.CARD_UPDATED);

          console.log(this.c, f, 'executeStep'
          , 'execCard.name', execCard.name
          , 'execCard.step', execCard.step
          , 'stepExecType', stepExecType
          , 'execCard.steps[execCard.step]', execCard.steps[execCard.step]);

          if ( execCard.steps[execCard.step].execute ) {

            this.executeStep(execCard.name, execCard.step, stepExecType);

          } else {
            switch ( execType ) {
              case CardExecType.START: {
                execCard.steps[execCard.step].state = StepType.START_SKIPPED;
              } break;
              case CardExecType.STOP: {
                execCard.steps[execCard.step].state = StepType.STOP_SKIPPED;
              } break;
            }
            console.log(this.c, f, 'executeCard', 'execCard.name', execCard.name, 'execType', execType, 'Step Skipped', execCard.step);
          }

          // Reading the delay
          const timeout = execCard.steps[execCard.step].delay;
          console.log(this.c, f, 'timeout[' + timeout + ']');

          // Update Step index
          execCard.step++;

          const interval = (byPassTimer ? 100 : 1000) * timeout;
          console.log(this.c, f, 'interval', interval);

          execCard.timer = Observable.interval(interval).map((x) => {
            console.log(this.c, f, 'interval map');

          }).subscribe((x) => {
            console.log(this.c, f, 'interval subscribe');

            console.log(this.c, f, 'interval unsubscribe timer');
            execCard.timer.unsubscribe();
            execCard.timer = null;

            this.cardChanged(CardServiceType.STEP_UPDATED);

            console.log(this.c, f, 'executeCard', 'execCard.name', execCard.name, 'execType', execType);
            this.executeCard(execCard.name, execType, firstStep, byPassTimer);
          });

        } else {
          console.log(this.c, f, 'end of steps', 'execCard.name', execCard.name, 'execType', execType);

          switch ( execType ) {
            case CardExecType.START: {
              execCard.state = CardType.STARTED;
            } break;
            case CardExecType.STOP: {
              execCard.state = CardType.STOPPED;
            } break;
          }

          this.dbmPollingService.unsubscribe(execCard);

          this.notifyUpdate(CardServiceType.CARD_UPDATED);
        }
      } else {
        this.dbmPollingService.unsubscribe(execCard);
      }
    } else {
      console.log(this.c, f, 'CARD NOT FOUND');
    }
  }
}
