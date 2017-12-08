import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Subscription } from 'rxjs/Subscription';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { Card, Step, StepType, CardType } from '../../model/Scenario';
import { DacSimExecution, EIV, DacSimExecType, ExecResult } from './../../service/scs/dac-sim-settings';
import { AppSettings } from '../../app-settings';
import { SelectionService } from './selection.service';
import { StepExistsResult, CardExistsResult, CardExecType } from './card-setting';
import { DacSimService } from '../scs/dac-sim.service';

@Injectable()
export class CardService {

  public static readonly STR_INIT           = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED  = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_UPDATED   = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED  = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_UPDATED   = AppSettings.STR_STEP_UPDATED;

  readonly c = CardService.name;

  // Observable source
  private cardSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  cardItem = this.cardSource.asObservable();

  cards: Card[] = new Array<Card>();

  timerSubscription: Subscription;
  dacSimSubscription: Subscription;

  constructor(
    private dacSimService: DacSimService
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
        const step: Step = this.getCard([item.cardId]).steps[item.stepId]
        if ( null != step ) {
          console.log(this.c, f, 'item.execType', item.execType);
          if ( DacSimExecType.START == item.execType ) {
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
          } else if ( DacSimExecType.STOP == item.execType ) {
            switch (item.ret) {
              case ExecResult.INIT: {
                step.state = StepType.STOP_RUNNING;
              } break;
              case ExecResult.SENT: {
                step.state = StepType.STOP_RUNNING;
              } break;
              case ExecResult.FINISH: {
                step.state = StepType.STOP;
              } break;
              case ExecResult.FAILED: {
                step.state = StepType.STOP_FAILED;
              } break;
            }
          }
          console.log(this.c, f, 'step.state', step.state);
          this.notifyUpdate(CardService.STR_STEP_UPDATED);
        } else {
          console.log(this.c, f, 'step IS NULL');
        }
      }
    });
  }

  // Service command
  cardChanged(str) {
    const f = 'cardChanged';
    console.log(this.c, f);
    console.log(this.c, f, str);
    this.cardSource.next(str);
  }

  notifyUpdate(str: string): void {
    const f = 'notifyUpdate';
    console.log(this.c, f);
    console.log(this.c, f, str);

    switch (str) {
      case CardService.STR_CARD_RELOADED: {
        this.cardChanged(CardService.STR_CARD_RELOADED);
      } break;
      case CardService.STR_CARD_UPDATED: {
        this.cardChanged(CardService.STR_CARD_UPDATED);
      } break;
      case CardService.STR_STEP_RELOADED: {
        this.cardChanged(CardService.STR_STEP_RELOADED);
      } break;
      case CardService.STR_STEP_UPDATED: {
        this.cardChanged(CardService.STR_STEP_UPDATED);
      } break;
    }
  }

  // Validite
  cardExists(cardId: string, stepId: number): CardExistsResult {
    let ret = CardExistsResult.UNKNOW;
    const card: Card = this.getCard([cardId]);
    if ( undefined != card && null != card ) {
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

  getCard(cardIds?: string[]): Card {
    return this.getCards(cardIds)[0];
  }

  // Cards Getter
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
  // Cards Setter
  setCards(cards: Card[]): void { this.cards = cards; }

  addCards(cards: Card[]): void {
    cards.forEach(card => {
      this.cards.push(card);
    });
  }

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
    const cards: Card[] = this.getCards([cardIdentify]);
    if ( null != cards ) {
      cards[0].steps = steps;
    }
  }

  /**
   * Remote the cards by the identitys mapping
   *
   * @param {string[]} cardIds List of identity to remove
   * @memberof CardService
   */
  deleteCards(cardIds: string[]) {
    const f = 'deleteCard';
    console.log(this.c, f);

    cardIds.forEach(element => {
      for ( let i = 0 ; i < this.cards.length ; ++i ) {
        console.log(this.c, f, 'cards[i].name', this.cards[i].name);
        if ( this.cards[i].name === element ) {
          console.log(this.c, f, 'remove', 'name', element);
          this.cards.splice(i, 1);
        }
      }
    });
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

  executeStep(cardName: string, stepId: number, stepType: DacSimExecType): void {
    const f = 'executeStep';
    console.log(this.c, f);

    const steps: Step[] = this.getSteps(cardName, [stepId]);
    const step: Step = steps[0];

    const eivs: EIV[] = new Array<EIV>();
    step.equipment.ev.forEach( item => {

      let value: number = NaN;
      switch (stepType) {
        case DacSimExecType.START: {
          value = item.value.start;
        } break;
        case DacSimExecType.STOP: {
          value = item.value.stop;
        } break;
      }

      eivs.push(
        new EIV(
          item.name
          , value
        )
      )
    });

    const dacSimExe: DacSimExecution = new DacSimExecution(
      stepType
      , cardName
      , stepId
      , step.equipment.connAddr
      , eivs
      , ExecResult.INIT
    );

    this.dacSimService.writeEv(dacSimExe);
  }

  pauseCard(cardName: string): void {
    const f = 'pauseCard';
    console.log(this.c, f);
    console.log(this.c, f, 'cardName[' + cardName + ']');

    const execCard: Card = this.getCard([cardName]);

    if ( null != execCard ) {

      console.log(this.c, f, 'PAUSE');

      // Stop the timer
      execCard.timer.unsubscribe();

      // Change the card status = pause
      switch (execCard.state) {
        case CardType.START_RUNNING: {
          execCard.state = CardType.START_PAUSE;
        } break;
        case CardType.STOP_RUNNING: {
          execCard.state = CardType.STOP_PAUSE;
        } break;
      }

      this.notifyUpdate(CardService.STR_CARD_UPDATED);

    } else {
      console.log(this.c, f, 'card not found');
    }
  }

  executeCard(cardName: string, execType: CardExecType, firstStep: boolean = false): void {
    const f = 'executeCard';
    console.log(this.c, f);
    console.log(this.c, f, 'cardName[' + cardName + ']');
    console.log(this.c, f, 'firstStep[' + firstStep + ']');

    const execCard: Card = this.getCard([cardName]);

    if ( null != execCard ) {
      // Start time and set it to disable
      if ( firstStep ) {
        execCard.step = 0;
      }

      console.log(this.c, f, 'execCard.name         [' + execCard.name + ']');
      console.log(this.c, f, 'execCard.state        [' + execCard.state + ']');
      console.log(this.c, f, 'execCard.step         [' + execCard.step + ']');
      console.log(this.c, f, 'execCard.steps.length [' + execCard.steps.length + ']');
      console.log(this.c, f, 'execCard.steps        [' + execCard.steps + ']');
      console.log(this.c, f, 'execCard.type         [' + execCard.type + ']');

      if ( execCard.step < execCard.steps.length ) {

        let stepExecType: DacSimExecType = DacSimExecType.UNKNOW;
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

        this.notifyUpdate(CardService.STR_CARD_UPDATED);

        this.executeStep(
          execCard.name
          , execCard.step
          , stepExecType);

        const timeout = execCard.steps[execCard.step].delay;
        console.log(this.c, f, 'timeout[' + timeout + ']');
        execCard.timer = Observable.interval(1000 * timeout).map((x) => {
          console.log(this.c, f, 'map');

        }).subscribe((x) => {
          console.log(this.c, f, 'subscribe');
          // execCard.steps[execCard.step].state = StepType.;
          execCard.step++;

          console.log(this.c, f, 'unsubscribe');
          execCard.timer.unsubscribe();

          console.log(this.c, f, 'reloadScenarioStep');
          this.cardChanged('reloadSteps');

          console.log(this.c, f, 'executeCard');
          this.executeCard(execCard.name, execType);
        });
      } else {
        console.log(this.c, f, 'end of the steps in the card');

        switch ( execType ) {
          case CardExecType.START: {
            execCard.state = CardType.START;
          } break;
          case CardExecType.STOP: {
            execCard.state = CardType.STOP;
          } break;
        }

        this.notifyUpdate(CardService.STR_CARD_UPDATED);
      }
    } else {
      console.log(this.c, f, 'CARD NOT FOUND');
    }
  }

  resumeCard(cardName: string): void {
    const f = 'resumeCard';
    console.log(this.c, f);
    console.log(this.c, f, 'cardName[' + cardName + ']');

    const execCard: Card = this.getCard([cardName]);

    if ( null != execCard ) {
      let cardType: CardExecType = CardExecType.UNKNOW;
      switch (execCard.state) {
        case CardType.START_PAUSE: {
          cardType = CardExecType.START;
        } break;
        case CardType.STOP_PAUSE: {
          cardType = CardExecType.START;
        } break;
      }

      this.notifyUpdate(CardService.STR_CARD_UPDATED);

      this.executeCard(cardName, cardType);
    } else {
      console.log(this.c, f, 'CARD NOT FOUND');
    }
  }

}
