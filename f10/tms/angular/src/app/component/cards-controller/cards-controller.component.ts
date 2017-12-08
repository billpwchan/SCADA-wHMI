import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { CardService } from '../../service/card/card.service';
import { AppSettings } from '../../app-settings';
import { Card, CardType } from '../../model/Scenario';
import { SelectionService } from '../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardExecType, CardServiceType } from '../../service/card/card-settings';
import { SelectionServiceType } from '../../service/card/selection-settings';

@Component({
  selector: 'app-cards-controller'
  , templateUrl: './cards-controller.component.html'
  , styleUrls: ['./cards-controller.component.css']
})
export class CardsControllerComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  readonly c = CardsControllerComponent.name;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  btnDisabledStart: boolean;
  btnDisabledPause: boolean;
  btnDisabledResume: boolean;
  btnDisabledStop: boolean;
  btnDisabledReset: boolean;

  cardSubscripion: Subscription;
  selectionSubscription: Subscription;

  constructor(
    private cardService: CardService
    , private selectionService: SelectionService
  ) {
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscripion = this.cardService.cardItem
    .subscribe( item => {
      console.log(this.c, f, 'cardSubscripion', item);
      switch (item) {
        case CardServiceType.CARD_RELOADED: {
          this.btnClicked(CardsControllerComponent.STR_INIT);
        } break;
        case CardServiceType.CARD_UPDATED: {

        } break;        
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe( item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionServiceType.CARD_SELECTED: {
          this.btnClicked(CardsControllerComponent.STR_CARD_SELECTED);
        } break;
      }
    });

  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    this.cardSubscripion.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private startCard(): void {
    const f = 'startCard';
    console.log(this.c, f);
    const card: Card = this.cardService.getCard( 
      this.selectionService.getSelectedCardIds()
    );
    if ( null != card ) {
      this.cardService.executeCard(
        card.name
        , CardExecType.START
        , true);
    }
  }

  private stopCard(): void {
    const f = 'stopCard';
    console.log(this.c, f);
    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.executeCard(
      card.name
      , CardExecType.STOP
      , true);
  }

  private pauseCard(): void {
    const f = 'pauseCard';
    console.log(this.c, f);

    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.pauseCard(
      card.name);
  }

  private resumeCard(): void {
    const f = 'resumeCard';
    console.log(this.c, f);

    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.resumeCard(
      card.name);
  }

  private resetCard(): void {
    const f = 'resetCard';
    console.log(this.c, f);
  }

  private widgetController(card: Card) {
    const f = 'widgetController';
    console.log(this.c, f);
    if ( undefined != card && null != card ) {
      console.log(this.c, f,  card.state);
      switch ( card.state ) {
        case CardType.STOP: {
          this.btnDisabledStart = false;
          this.btnDisabledPause = true;
          this.btnDisabledResume = true;
          this.btnDisabledStop = true;
          this.btnDisabledReset = true;
        } break;
        case CardType.STOP_RUNNING: {
          this.btnDisabledStart = true;
          this.btnDisabledPause = false;
          this.btnDisabledResume = true;
          this.btnDisabledStop = false;
          this.btnDisabledReset = true;
        } break;
        case CardType.START: {
          this.btnDisabledStart = false;
          this.btnDisabledPause = true;
          this.btnDisabledResume = false;
          this.btnDisabledStop = true;
          this.btnDisabledReset = true;
        } break;
        case CardType.START_RUNNING: {
          this.btnDisabledStart = true;
          this.btnDisabledPause = false;
          this.btnDisabledResume = true;
          this.btnDisabledStop = false;
          this.btnDisabledReset = true;
        } break;
        case CardType.START_PAUSE: {
          this.btnDisabledStart = true;
          this.btnDisabledPause = true;
          this.btnDisabledResume = false;
          this.btnDisabledStop = false;
          this.btnDisabledReset = true;
        } break;
        case CardType.UNKNOW: {
          this.btnDisabledStart = false;
          this.btnDisabledPause = false;
          this.btnDisabledResume = false;
          this.btnDisabledStop = false;
          this.btnDisabledReset = false;
        } break;
      }
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnDisabledStart = true;
    this.btnDisabledPause = true;
    this.btnDisabledResume = true;
    this.btnDisabledStop = true;
    this.btnDisabledReset = true;
  }

  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case CardsControllerComponent.STR_INIT: {
        this.init();
      } break;
      case CardsControllerComponent.STR_CARD_SELECTED: {
        const ids: string[] = this.selectionService.getSelectedCardIds();
        const cards: Card[] = this.cardService.getCards(ids);
        this.widgetController(cards[0]);
      } break;
      case 'start': {
        this.startCard();
      } break;
      case 'pause': {
        this.pauseCard();
      } break;
      case 'resume': {
        this.resumeCard();
      } break;
      case 'stop': {
        this.stopCard();
      } break;
      case 'reset': {
        this.resetCard();
      } break;
    }

    this.sendNotifyParent(btnLabel);
  }
}
