import { Component, OnInit, OnDestroy, OnChanges, EventEmitter, Output, SimpleChanges, Input, ViewChild } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { Card, CardType, StepType } from '../../../model/Scenario';
import { DatatableCard } from '../../../model/DatatableScenario';
import { AppSettings } from '../../../app-settings';
import { CardsSettings, SortingDirection, CardColumnIndex } from './cards-settings';
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
  public static readonly STR_CARD_UPDATED  = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'CardsComponent';

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

  // Init the Card Datatable Sorting Order
  private columnsSorting: SortingDirection[] = [SortingDirection.NON_ORDING , SortingDirection.NON_ORDING];
  private strSortingOrderName = CardsSettings.STR_CARD_DG_HEADER_SORTING_NON;
  private strSortingOrderState = CardsSettings.STR_CARD_DG_HEADER_SORTING_NON;

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
          this.btnClicked(CardsComponent.STR_CARD_RELOADED);
        } break;
        case CardServiceType.CARD_UPDATED: {
          this.btnClicked(CardsComponent.STR_CARD_UPDATED);
        } break;
        case CardServiceType.STEP_UPDATED: {
          this.btnClicked(CardsComponent.STR_CARD_UPDATED);
        } break;
        case CardServiceType.STEP_RELOADED: {
          this.btnClicked(CardsComponent.STR_CARD_UPDATED);
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

  onHeaderClick(elementName: string, columnName: string): void {
    const f = 'sort';
    console.log(this.c, f);
    console.log(this.c, f, 'elementName[' + elementName + '] columnName[' + columnName + ']');

    let columnIndex: CardColumnIndex = CardColumnIndex.OUTOFRANGE;
    if ( 'name' === columnName ) {
      columnIndex = CardColumnIndex.NAME;
      this.columnsSorting[CardColumnIndex.STATE] = SortingDirection.NON_ORDING;
    } else if ( 'state' === columnName ) {
      columnIndex = CardColumnIndex.STATE;
      this.columnsSorting[CardColumnIndex.NAME] = SortingDirection.NON_ORDING;
    }
    if ( CardColumnIndex.OUTOFRANGE !== columnIndex ) {
      switch ( this.columnsSorting[columnIndex] ) {
        case SortingDirection.NON_ORDING: {
          this.columnsSorting[columnIndex] = SortingDirection.DES_ORDING;
        } break;
        case SortingDirection.ASC_ORDING: {
          this.columnsSorting[columnIndex] = SortingDirection.DES_ORDING;
        } break;
        case SortingDirection.DES_ORDING: {
          this.columnsSorting[columnIndex] = SortingDirection.ASC_ORDING;
        } break;
      }
      this.reloadCards(true);
    }
  }

  private sortingCards(cards: Card[], columnsSorting: SortingDirection[] ): Card[] {
    const f = 'sortingCards';
    console.log(this.c, f);
    let ret: Card[] = cards;
    // Sorting
    if ( SortingDirection.ASC_ORDING === columnsSorting[CardColumnIndex.STATE] ) {
      this.strSortingOrderName = CardsSettings.STR_CARD_DG_HEADER_SORTING_NON;
      this.strSortingOrderState = CardsSettings.STR_CARD_DG_HEADER_SORTING_ASC;
      ret = cards.sort((n1, n2) => ( n1.state > n2.state ? 1 : (n1.state < n2.state ) ? -1 : 0 ));
    } else if ( SortingDirection.DES_ORDING === columnsSorting[CardColumnIndex.STATE] ) {
      this.strSortingOrderName = CardsSettings.STR_CARD_DG_HEADER_SORTING_NON;
      this.strSortingOrderState = CardsSettings.STR_CARD_DG_HEADER_SORTING_DES;
      ret = cards.sort((n1, n2) => ( n1.state < n2.state ? 1 : (n1.state > n2.state ) ? -1 : 0 ));
    } else if ( SortingDirection.ASC_ORDING === columnsSorting[CardColumnIndex.NAME] ) {
      this.strSortingOrderName = CardsSettings.STR_CARD_DG_HEADER_SORTING_ASC;
      this.strSortingOrderState = CardsSettings.STR_CARD_DG_HEADER_SORTING_NON;
      ret = cards.sort((n1, n2) => ( n1.name > n2.name ? 1 : (n1.name < n2.name ) ? -1 : 0 ));
    } else if ( SortingDirection.DES_ORDING === columnsSorting[CardColumnIndex.NAME] ) {
      this.strSortingOrderName = CardsSettings.STR_CARD_DG_HEADER_SORTING_DES;
      this.strSortingOrderState = CardsSettings.STR_CARD_DG_HEADER_SORTING_NON;
      ret = cards.sort((n1, n2) => ( n1.name < n2.name ? 1 : (n1.name > n2.name ) ? -1 : 0 ));
    }
    return ret;
  }

  private isStoppedPartial ( card: Card ): boolean {
    const f = 'isStoppedPartial';
    console.log(this.c, f);
    let ret = false;
    if ( null != card ) {
      if ( null != card.steps ) {
        card.steps.forEach(item => {
          if (
            StepType.START === item.state
            || StepType.START_FAILED === item.state
          ) {
            ret = true;
          }
        });
      } else {
        console.warn(this.c, f, 'card and steps IS NULL');
      }
    } else {
      console.warn(this.c, f, 'card IS NULL');
    }
    return ret;
  }
  private isStartedPartial ( card: Card ): boolean {
    const f = 'isStartedPartial';
    console.log(this.c, f);
    let ret = false;
    if ( null != card ) {
      if ( null != card.steps ) {
        card.steps.forEach(item => {
          if (
            StepType.STOPPED === item.state
            || StepType.STOPPED_FAILED === item.state
          ) {
            ret = true;
          }
        });
      } else {
        console.warn(this.c, f, 'card and steps IS NULL');
      }
    } else {
      console.warn(this.c, f, 'card IS NULL');
    }
    return ret;
  }

  private getCardTypeStr(card: Card): string {
    const f = 'getCardTypeStr';
    console.log(this.c, f, card.state);

    let ret = CardsSettings.STR_CARD_DG_STATE_UNKNOW;
    switch (card.state) {
      case CardType.STOPPED: {
        ret = CardsSettings.STR_CARD_DG_STATE_STOPPED;
        if ( this.isStoppedPartial(card) ) {
          ret = CardsSettings.STR_CARD_DG_STATE_STOPPED_PARTIAL;
        }
      } break;
      case CardType.STOP_RUNNING: {
        ret = CardsSettings.STR_CARD_DG_STATE_STOP_RUNNING;
      } break;
      case CardType.STOP_TERMINATED: {
        ret = CardsSettings.STR_CARD_DG_STATE_STOP_TERMINATED;
      } break;
      case CardType.STARTED: {
        ret = CardsSettings.STR_CARD_DG_STATE_STARTED;
        if ( this.isStartedPartial(card) ) {
          ret = CardsSettings.STR_CARD_DG_STATE_STOPPED_PARTIAL;
        }
      } break;
      case CardType.START_RUNNING: {
        ret = CardsSettings.STR_CARD_DG_STATE_START_RUNNING;
      } break;
      case CardType.START_PAUSED: {
        ret = CardsSettings.STR_CARD_DG_STATE_START_PAUSE;
      } break;
      case CardType.START_TERMINATED: {
        ret = CardsSettings.STR_CARD_DG_STATE_START_TERMINATED;
      } break;
    }
    return ret;
  }

  private reloadingCards(keepSelected: boolean, cb ? ): void {
    const f = 'reloadingCard';
    console.log(this.c, f);

    // Avoid the sorting modify the internal cards order
    const clonedCards: Card[] = new Array<Card>();
    this.cardService.getCards().forEach ( item => {
      clonedCards.push(new Card(item.name, item.state, 0));
    });
    const sortedCards: Card[] = this.sortingCards(this.cardService.getCards(), this.columnsSorting);

    const preSelected = this.selected_card;
    // Reset datatable
    this.rows_card = [];

    // Renew datatable from card service
    sortedCards.forEach((item, index) => {
      this.rows_card.push(
        new DatatableCard(
          item.name
          , this.getCardTypeStr(item)
          , new Date()
        )
      );
    });
    this.rows_card = [...this.rows_card];

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
    this.selected_card = [...this.selected_card];

    if ( cb ) {
      cb();
    }
  }

  private reloadCards(keepSelected: boolean = false): void {
    const f = 'reloadCards';
    console.log(this.c, f);
    console.log(this.c, f, keepSelected);

    this.loadingIndicator = true;
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

    // Quick Fix for the ngx-datatable view update
    window.dispatchEvent(new Event('resize'));
  }

  onActivate(name: string, event) {
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
      case CardsComponent.STR_CARD_RELOADED: {
        this.reloadCards();
      } break;
      case CardsComponent.STR_CARD_SELECTED: {
        this.setSelectedCards();
      } break;
      case CardsComponent.STR_CARD_UPDATED: {
        this.reloadCards(true);
      } break;
    }
    // this.sendNotifyParent(btnLabel);
  }
}
