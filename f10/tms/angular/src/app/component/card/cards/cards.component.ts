import { Component, OnInit, OnDestroy, OnChanges, EventEmitter, Output, SimpleChanges, Input, ViewChild } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { Card, CardType } from '../../../model/Scenario';
import { DatatableCard } from '../../../model/DatatableScenario';
import { AppSettings } from '../../../app-settings';
import { CardsSettings } from './cards-settings';
import { FormGroup, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';
import { SelectionService } from '../../../service/card/selection.service';
import { AppComponent } from '../../../app.component';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { StorageService } from '../../../service/card/storage.service';

@Component({
  selector: 'app-cards'
  , templateUrl: './cards.component.html'
  , styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_RELOAD_CARD = 'reloadcard';

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = CardsComponent.name;

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  // Datatable
  @ViewChild('cardsDataTable') cardsDataTable: DatatableComponent;

  rows_card = new Array<DatatableCard>();
  selected_card = new Array<DatatableCard>();
  loadingIndicator: boolean;
  reorderable: boolean;

  // properties for ngx-datatable
  public messages = {};

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private selectionService: SelectionService
    , private storageService: StorageService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscription = this.cardService.cardItem
    .subscribe(item => {
      console.log(this.c, f, 'cardSubscription', item);
      switch (item) {
        case CardServiceType.CARD_RELOADED: {
          this.btnClicked(CardsComponent.STR_RELOAD_CARD);
        } break;
        case CardServiceType.CARD_UPDATED: {
          this.reloadCards(true);
        } break;
      }
    });

    this.btnClicked(CardsComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[CardsComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[CardsComponent.STR_NORIFY_FROM_PARENT].currentValue) {
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  loadTranslations() {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.cardsDataTable.messages['emptyMessage'] = this.translate.instant('&cards_dg_footer_emptymessage');
    this.cardsDataTable.messages['selectedMessage'] = this.translate.instant('&cards_dg_footer_selectedmessage');
    this.cardsDataTable.messages['totalMessage'] = this.translate.instant('&cards_dg_footer_totalmessage');
  }

  getRowClass(row) {
    const f = 'getRowClass';
    // console.log(this.c, f);
    return {
      'age-is-ten': (row.age % 10) === 0
    };
  }

  getCellClass({ row, column, value }): any {
    const f = 'getCellClass';
    // console.log(this.c, f);
    return {
      'is-female': value === 'female'
    };
  }

  private getCardTypeStr(cardType: CardType): string {
    const f = 'getCardTypeStr';
    console.log(this.c, f, cardType);

    let ret = CardsSettings.STR_CARD_UNKNOW;
    switch (cardType) {
      case CardType.STOP: {
        ret = CardsSettings.STR_CARD_STOP;
      } break;
      case CardType.STOP_RUNNING: {
        ret = CardsSettings.STR_CARD_STOP_RUNNING;
      } break;
      case CardType.START: {
        ret = CardsSettings.STR_CARD_START;
      } break;
      case CardType.START_RUNNING: {
        ret = CardsSettings.STR_CARD_START_RUNNING;
      } break;
      case CardType.START_PAUSE: {
        ret = CardsSettings.STR_CARD_PAUSE;
      } break;
    }
    return ret;
  }

  private reloadingCards(keepSelected: boolean, cb): void {
    const f = 'reloadingCard';
    console.log(this.c, f);

    const preSelected = this.selected_card;
    // Reset datatable
    this.rows_card = [];

    // Renew datatable from card service
    this.cardService.getCards().forEach((item, index) => {
      this.rows_card.push(
        new DatatableCard(
          item.name
          , this.getCardTypeStr(item.state)
        )
      );
    });

    // this.rows_card = [...this.rows_card];

    if ( keepSelected ) {
      let selected = false;
      preSelected.forEach( item1 => {
        this.rows_card.forEach( item2 => {
          if ( item1.name === item2.name ) {
            this.selected_card = [item2];
            selected = true;
          }
        });
      });
      if ( selected ) {
        this.selectionService.notifyUpdate(SelectionServiceType.CARD_SELECTED);
      }
    } else {
      this.selected_card = [];
    }
    // this.selected_card = [...this.selected_card];

    return;
  }

  private reloadCards(keepSelected: boolean = false): void {
    const f = 'reloadCards';
    console.log(this.c, f);
    console.log(this.c, f, keepSelected);

    this.reloadingCards( keepSelected, (data) => {
      setTimeout(() => { this.loadingIndicator = false; }, 1500);
    });
  }

  private setSelectedCards(): void {
    const f = 'setSelectedCards';
    const selectedCards: string[] = new Array<string>();
    this.selected_card.forEach(item => {
      console.log(this.c, f, 'item.name[' + item.name + ']');
      selectedCards.push(item.name);
    });
    this.selectionService.setSelectedCardIds(selectedCards);
  }

  onRowSelect(name: string, event: Event) {
    const f = 'onRowSelect';
    console.log(this.c, f);
    this.btnClicked(CardsComponent.STR_CARD_SELECTED, event);
  }

  onActivate(name: string, event: Event) {
    const f = 'onActivate';
    console.log(this.c, f);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    // Reset datatable
    this.rows_card = [];
    // Reload
    // this.rows_card = [...this.rows_card];

    this.selected_card = [];
    // this.selected_card = [...this.selected_card];

    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardsComponent.STR_INIT: {
        this.init();
      } break;
      case CardsComponent.STR_RELOAD_CARD: {
        this.reloadCards();
      } break;
      case CardsComponent.STR_CARD_SELECTED: {
        this.setSelectedCards();
      } break;
    }
    this.sendNotifyParent(btnLabel);
  }
}
