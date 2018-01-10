import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from '../../../service/settings.service';
import { CardService } from '../../../service/card/card.service';
import { SelectionService } from '../../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { Card } from '../../../model/Scenario';
import { CardsSettings } from '../cards/cards-settings';

@Component({
  selector: 'app-card-state-controller',
  templateUrl: './card-state-controller.component.html',
  styleUrls: ['./card-state-controller.component.css']
})
export class CardStateControllerComponent implements OnInit , OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_MODIFIED = AppSettings.STR_CARD_MODIFIED;

  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_MODIFIED = AppSettings.STR_STEP_MODIFIED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c: string = 'CardStateControllerComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  private selCardIds: string[];

  btnEnableStateEnable: boolean;
  btnEnableStateDisable: boolean;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private cardService: CardService
    , private selectionService: SelectionService
  ) {
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
    .subscribe(item => {
      console.log(this.c, f, 'cardSubscription', item);
      switch (item) {
        case CardServiceType.CARD_RELOADED: {
          this.btnClicked(CardStateControllerComponent.STR_CARD_RELOADED);
        } break;
        case CardServiceType.CARD_UPDATED: {
          this.btnClicked(CardStateControllerComponent.STR_CARD_MODIFIED);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe(item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionServiceType.CARD_SELECTED: {
          this.btnClicked(CardStateControllerComponent.STR_CARD_SELECTED);
        } break;
      }
    });
    this.btnClicked(CardStateControllerComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[CardStateControllerComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[CardStateControllerComponent.STR_NORIFY_FROM_PARENT].currentValue) {
      }
    }
  }

  sendNotifyParent(str: string): void {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);
  }

  private setSelCardsState(state: boolean): void {
    const f = 'setSelCardsState';
    console.log(this.c, f);
    const selCards: Card[] = this.cardService.getCards(this.selCardIds);
    if ( null != selCards && selCards.length > 0 ) {
      selCards.forEach( (card: Card) => {
        card.state = state;
      });
    }
  }

  private refreshBtns(): void {
    const f = 'refreshBtns';
    console.log(this.c, f);
    let found = false;
    if ( null != this.selCardIds && this.selCardIds.length > 0 ) {
      const selCards: Card[] = this.cardService.getCards(this.selCardIds);
      if ( null != selCards && selCards.length > 0 ) {
        const card: Card = selCards[0];
        if ( null != card ) {
          found = true;
          if ( card.state ) {
            this.btnEnableStateDisable = true;
            this.btnEnableStateEnable = false;
          } else {
            this.btnEnableStateDisable = false;
            this.btnEnableStateEnable = true;
          }
        }
      }
    }
    if ( ! found ) {
      this.btnEnableStateDisable = false;
      this.btnEnableStateEnable = false;
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.selCardIds = [];
    this.refreshBtns();
  }

  btnClicked(btnLabel: string, event?: Event): void {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardStateControllerComponent.STR_INIT: {
        this.init();
      } break;
      case CardStateControllerComponent.STR_CARD_RELOADED: {
        this.init();
      } break;
      case CardStateControllerComponent.STR_CARD_SELECTED: {
        this.selCardIds = this.selectionService.getSelectedCardIds();
        this.refreshBtns();
      } break;
      case 'enable': {
        this.setSelCardsState(true);
        this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
        this.cardService.notifyUpdate(CardServiceType.CARD_EDITED);
      } break;
      case 'disable': {
        this.setSelCardsState(false);
        this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
        this.cardService.notifyUpdate(CardServiceType.CARD_EDITED);
      } break;
    }
  }
}
