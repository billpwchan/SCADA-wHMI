import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { Card, Step, StepType } from '../../model/Scenario';
import { DacSimService, DacSimExecution, EIV, ExecType, ExecResult } from './../../service/scs/dac-sim.service';
import { AppSettings } from '../../app-settings';
import { SelectionService } from './selection.service';


@Injectable()
export class CardService {

  public static readonly STR_INIT           = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED  = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_UPDATED   = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED  = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_UPDATED   = AppSettings.STR_STEP_UPDATED;

  public static readonly INT_EXEC_TYPE_START = 0;
  public static readonly INT_EXEC_TYPE_STOP = 1;
  public static readonly INT_EXEC_TYPE_RESET = 2;

  public static readonly INT_CARD_EXISTS_UNKNOW = 0;
  public static readonly INT_CARD_EXISTS_CARD_NOT_FOUND = 1;
  public static readonly INT_CARD_EXISTS_CARD_FOUND = 2;

  public static readonly INT_STEP_EXISTS_UNKNOW = 0;
  public static readonly INT_STEP_EXISTS_CARD_NOT_FOUND = 1;
  public static readonly INT_STEP_EXISTS_STEP_NOT_FOUND = 2;
  public static readonly INT_STEP_EXISTS_STEP_FOUND = 3;

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

      if ( this.stepExists(item.cardId, item.stepId) === CardService.INT_STEP_EXISTS_STEP_FOUND ) {
        const step: Step = this.getCard([item.cardId]).steps[item.stepId]
        if ( null != step ) {
          console.log(this.c, f, 'item.execType', item.execType);
          if ( ExecType.start == item.execType ) {
            switch (item.ret) {
              case ExecResult.init: {
                step.state = StepType.start_running;
              } break;
              case ExecResult.sent: {
                step.state = StepType.start_running;
              } break;
              case ExecResult.finish: {
                step.state = StepType.start;
              } break;
              case ExecResult.faild: {
                step.state = StepType.start_failed;
              } break;
            }
          } else if ( ExecType.stop == item.execType ) {
            switch (item.ret) {
              case ExecResult.init: {
                step.state = StepType.stop_running;
              } break;
              case ExecResult.sent: {
                step.state = StepType.stop_running;
              } break;
              case ExecResult.finish: {
                step.state = StepType.stop;
              } break;
              case ExecResult.faild: {
                step.state = StepType.stop_failed;
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
  // 0: UNKNOW ERROR
  // 1: CARD NOT FOUND
  // 2: CARD FOUND
  cardExists(cardId: string, stepId: number): number {
    let ret = CardService.INT_CARD_EXISTS_UNKNOW;
    const cards: Card[] = this.getCards([cardId]);
    if ( cards.length > 0 ) {
      ret = CardService.INT_CARD_EXISTS_CARD_FOUND;
    } else {
      ret = CardService.INT_STEP_EXISTS_CARD_NOT_FOUND;
    }
    return ret;
  }

  // Validite
  // 0: UNKNOW ERROR
  // 1: CARD NOT FOUND
  // 2: STEP NOT FOUND
  // 3: STEP FOUND
  stepExists(cardId: string, stepId: number): number {
    let ret = CardService.INT_STEP_EXISTS_UNKNOW;
    const cards: Card[] = this.getCards([cardId]);
    if ( cards.length > 0 ) {
      const steps: Step[] = this.getSteps(cardId, [stepId]);
      if ( steps.length > 0 ) {
        ret = CardService.INT_STEP_EXISTS_STEP_FOUND;
      } else {
        ret = CardService.INT_STEP_EXISTS_STEP_NOT_FOUND;
      }
    } else {
      ret = CardService.INT_STEP_EXISTS_CARD_NOT_FOUND;
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

  executeStep(cardName: string, stepId: number, stepType: ExecType): void {
    const f = 'executeStep';
    console.log(this.c, f);

    const steps: Step[] = this.getSteps(cardName, [stepId]);
    const step: Step = steps[0];

    const eivs: EIV[] = new Array<EIV>();
    step.equipment.ev.forEach( item => {

      let value: number = NaN;
      switch (stepType) {
        case ExecType.start: {
          value = item.value.start;
        } break;
        case ExecType.stop: {
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
      , ExecResult.init
    );

    this.dacSimService.writeEv(dacSimExe);
  }

  executeCard(cardName: string, firstStep: boolean = false): void {
    const f = 'executeCard';
    console.log(this.c, f);
    console.log(this.c, f, 'firstStep[' + firstStep + ']');

    const execCards: Card[] = this.getCards([cardName]);
    const execCard: Card = execCards[0];

    if ( null != execCard ) {
      // Start time and set it to disable
      if ( firstStep ) {
        execCard.state = AppSettings.INT_CARD_STOP_RUNNING;
        execCard.step = 0;
      }
      console.log(this.c, f, 'execCard.name[' + execCard.name + ']');
      console.log(this.c, f, 'execCard.state[' + execCard.state + ']');
      console.log(this.c, f, 'execCard.step[' + execCard.step + ']');
      console.log(this.c, f, 'execCard.steps.length[' + execCard.steps.length + ']');
      console.log(this.c, f, 'execCard.steps[' + execCard.steps + ']');
      console.log(this.c, f, 'execCard.type[' + execCard.type + ']');
      if ( execCard.step < execCard.steps.length ) {

        this.executeStep(
          execCard.name
          , execCard.step
          , execCard.type);

        const timeout = execCard.steps[execCard.step].delay;
        console.log(this.c, f, 'timeout[', timeout, ']');
        execCard.timer = Observable.interval(1000 * timeout).map((x) => {
          console.log(this.c, f, 'map');

        }).subscribe((x) => {
          console.log(this.c, f, 'subscribe');

          execCard.steps[execCard.step].state = AppSettings.INT_CARD_STOP;
          execCard.step++;

          console.log(this.c, f, 'unsubscribe');
          execCard.timer.unsubscribe();

          console.log(this.c, f, 'reloadScenarioStep');
          this.cardChanged('reloadSteps');

          console.log(this.c, f, 'executeCard');
          this.executeCard(execCard.name);
        });
      }
    } else {
      console.log(this.c, f, 'curExecCard IS NULL');
    }
  }

  // private getSelectedStep(): Step {
  //   const func = 'getSelectedStep';
  //   console.log(func);
  //   let step: Step = null;
  //   const card: Card = this.getSelectedCard();
  //   const selected_step: Step = this.cardService.get
  //   if ( null != card ) {
  //     for ( let x = 0 ; x < this.selected_step.length ; ++x ) {
  //       const dtStep: DatatableStep = this.selected_step[x];
  //       console.log(func, 'stdtStep.step', dtStep.step, 'stx', x);
  //       for ( let y = 0 ; y < card.steps.length ; ++y ) {
  //         console.log(func, 'stdtStep.step', dtStep.step, 'sty', y);
  //         if ( '' + card.steps[y].step === dtStep.step ) {
  //           console.log(func, 'sty', y);
  //           step = card.steps[y];
  //           break;
  //         }
  //       }
  //     }
  //   } else {
  //     console.log(func, 'stcard IS EMPTY');
  //   }
  //   return step;
  // }

  // private getSelectedCard(): Card {
  //   const func = 'getSelectedCard';
  //   console.log(func);
  //   let card: Card = null;
  //   if ( this.selected_card.length > 0 ) {
  //     this.selected_card.forEach((item, index) => {
  //       console.log(func, 'stname', item.name, 'stindex', index);
  //       const cards = this.cardService.getCards();
  //       for ( let i = 0 ; i < cards.length ; ++i ) {
  //         console.log(func, 'stcards[i].name', cards[i].name);
  //         if ( cards[i].name === item.name ) {
  //           console.log(func, cards[i]);
  //           card = cards[i];
  //           break;
  //         }
  //       }
  //     });
  //   }
  //   console.log(func, card);
  //   return card;
  // }
}
