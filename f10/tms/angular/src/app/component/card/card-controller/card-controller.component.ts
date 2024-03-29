import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, SimpleChanges, Input } from '@angular/core';
import { CardService } from '../../../service/card/card.service';
import { AppSettings } from '../../../app-settings';
import { Card, CardType } from '../../../model/Scenario';
import { SelectionService } from '../../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardExecType, CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';

@Component({
  selector: 'app-card-controller'
  , templateUrl: './card-controller.component.html'
  , styleUrls: ['./card-controller.component.css']
})
export class CardControllerComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED  = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED  = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED  = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED  = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_CARD_UPDATED   = AppSettings.STR_CARD_UPDATED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'CardControllerComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  btnEnableStart: boolean;
  btnEnablePause: boolean;
  btnEnableResume: boolean;
  btnEnableStop: boolean;
  btnEnableReset: boolean;

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
          this.btnClicked(CardControllerComponent.STR_CARD_RELOADED);
        } break;
        case CardServiceType.CARD_UPDATED: {
          this.btnClicked(CardControllerComponent.STR_CARD_UPDATED);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe( item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionServiceType.CARD_SELECTED: {
          this.btnClicked(CardControllerComponent.STR_CARD_SELECTED);
        } break;
      }
    });

  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscripion.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[CardControllerComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[CardControllerComponent.STR_NORIFY_FROM_PARENT].currentValue) {
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private startCard(): void {
    const f = 'startCard';
    console.log(this.c, f);
    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.executeCard(
      card.name
      , CardExecType.START
      , true
      , false);
  }

  private stopCard(): void {
    const f = 'stopCard';
    console.log(this.c, f);
    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.executeCard(
      card.name
      , CardExecType.TERMINATE
      , true
      , false);
  }

  private pauseCard(): void {
    const f = 'pauseCard';
    console.log(this.c, f);

    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.executeCard(
      card.name
      , CardExecType.PAUSE
      , true
      , false);
  }

  private resumeCard(): void {
    const f = 'resumeCard';
    console.log(this.c, f);

    this.cardService.executeCard(
      this.selectionService.getSelectedCardId()
      , CardExecType.RESUME
      , false
      , false);
  }

  private resetCard(): void {
    const f = 'resetCard';
    console.log(this.c, f);

    const card: Card = this.cardService.getCard( this.selectionService.getSelectedCardIds());
    this.cardService.executeCard(
      card.name
      , CardExecType.STOP
      , true
      , true);
  }

  private widgetController(card: Card) {
    const f = 'widgetController';
    console.log(this.c, f);
    if ( card ) {
      console.log(this.c, f,  card.state);
      switch ( card.state ) {
        case CardType.STOPPED: {
          this.btnEnableStart = true;
          this.btnEnablePause = false;
          this.btnEnableResume = false;
          this.btnEnableStop = false;
          this.btnEnableReset = true;
        } break;
        case CardType.STOP_RUNNING: {
          this.btnEnableStart = false;
          this.btnEnablePause = false;
          this.btnEnableResume = false;
          this.btnEnableStop = false;
          this.btnEnableReset = false;
        } break;
        case CardType.STOP_TERMINATED: {
          this.btnEnableStart = true;
          this.btnEnablePause = false;
          this.btnEnableResume = false;
          this.btnEnableStop = false;
          this.btnEnableReset = true;
        } break;
        case CardType.STARTED: {
          this.btnEnableStart = false;
          this.btnEnablePause = false;
          this.btnEnableResume = false;
          this.btnEnableStop = false;
          this.btnEnableReset = true;
        } break;
        case CardType.START_RUNNING: {
          this.btnEnableStart = false;
          this.btnEnablePause = true;
          this.btnEnableResume = false;
          this.btnEnableStop = true;
          this.btnEnableReset = false;
        } break;
        case CardType.START_PAUSED: {
          this.btnEnableStart = false;
          this.btnEnablePause = false;
          this.btnEnableResume = true;
          this.btnEnableStop = false;
          this.btnEnableReset = false;
        } break;
        case CardType.START_TERMINATED: {
          this.btnEnableStart = false;
          this.btnEnablePause = false;
          this.btnEnableResume = false;
          this.btnEnableStop = false;
          this.btnEnableReset = true;
        } break;
        default: {
          this.btnEnableStart = true;
          this.btnEnablePause = true;
          this.btnEnableResume = true;
          this.btnEnableStop = true;
          this.btnEnableReset = true;
        } break;
      }
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnEnableStart = false;
    this.btnEnablePause = false;
    this.btnEnableResume = false;
    this.btnEnableStop = false;
    this.btnEnableReset = false;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case CardControllerComponent.STR_INIT: {
        this.init();
      } break;
      case CardControllerComponent.STR_CARD_RELOADED: {
        this.init();
      } break;
      case CardControllerComponent.STR_CARD_UPDATED: {
        const ids: string[] = this.selectionService.getSelectedCardIds();
        const card: Card = this.cardService.getCard(ids);
        this.widgetController(card);
      } break;
      case CardControllerComponent.STR_CARD_SELECTED: {
        const ids: string[] = this.selectionService.getSelectedCardIds();
        const card: Card = this.cardService.getCard(ids);
        this.widgetController(card);
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

    // this.sendNotifyParent(btnLabel);
  }
}
