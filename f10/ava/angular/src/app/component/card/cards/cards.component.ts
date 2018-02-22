import { Component, OnInit, OnDestroy, OnChanges, EventEmitter, Output, SimpleChanges, Input, ViewChild } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { Card } from '../../../model/Scenario';
import { DatatableCard } from '../../../model/DatatableScenario';
import { AppSettings } from '../../../app-settings';
import { CardsSettings, SortingDirection, CardColumnIndex } from './cards-settings';
import { FormGroup, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';
import { AppComponent } from '../../../app.component';
import { DatatableComponent } from '@swimlane/ngx-datatable';

@Component({
  selector: 'app-cards'
  , templateUrl: './cards.component.html'
  , styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NORIFY_FROM_PARENT = AppSettings.STR_NOTIFY_FROM_PARENT;

  readonly c = 'CardsComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  private updated: Card[];
  @Input()
  set updateCards(cards: Card[]) {
    const f = 'updateCards';
    console.log(this.c, f);

    if ( null != cards ) {

      this.updated = new Array<Card>();

      for ( let i = 0 ; i < cards.length ; ++i ) {
        const card = cards[i];
        const newCard = new Card(
                                  card.univname
                                  , card.index
                                  , card.name
                                  , card.state
                                  , card.status);
        this.updated.push(newCard);
      }
      console.log(this.c, f, 'this.updated', this.updated);

      this.reloadData();
    } else {
      console.warn(this.c, f, 'data IS INVALID');
    }
  }
  @Output() onUpdatedCardSelection = new EventEmitter<number[]>();

  // Datatable
  @ViewChild('cardsDataTable') cardsDataTable: DatatableComponent;

  rows_card = new Array<DatatableCard>();
  selected_card = new Array<DatatableCard>();

  // properties for ngx-datatable
  public messages = {};

  private skipSelectedCardUpdate = false;

  constructor(
    private translate: TranslateService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.btnClicked(CardsComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
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

  private getStateStr(state: boolean): string {
    // const f = 'getStateStr';
    // console.log(this.c, f);
    return ( state ? CardsSettings.STR_CARD_DG_STATE_ENABLED : CardsSettings.STR_CARD_DG_STATE_DISABLED );
  }

  private getStatusStr(status: boolean): string {
    // const f = 'getStatusStr';
    // console.log(this.c, f);
    return ( status ? CardsSettings.STR_CARD_DG_STATUS_TRIGGERED : CardsSettings.STR_CARD_DG_STATUS_NOT_TRIGGERED );
  }

  private reloadData(keepSelection: boolean = true) {
    const f = 'reloadData';
    console.log(this.c, f);
    const selIds = [];
    this.selected_card.forEach( (value, key) => {
      selIds.push(value);
    });

    if ( null == this.rows_card ) {
      this.rows_card = [];
    }

    this.updated.forEach ( (item, index) => {
      const orgItem = this.rows_card[index];
      if ( null == orgItem ) {

        this.rows_card[index] = new DatatableCard(
          String(item.index)
          , item.name
          , this.getStateStr(item.state)
          , this.getStatusStr(item.status)
          , new Date()
        );

      } else {
        const card = this.rows_card[index];
        card.index = String(item.index);
        card.name = item.name;
        card.state = this.getStateStr(item.state);
        card.status = this.getStatusStr(item.status);
        card.updated = new Date();
      }
    });

    if ( ! keepSelection ) {
      this.rows_card = [...this.rows_card];
    } else {
      this.skipSelectedCardUpdate = true;
      this.rows_card = [...this.rows_card];
      this.selected_card = [];
      selIds.forEach( (value1, key1) => {
        this.rows_card.forEach( (item2, key2) => {
          if ( item2.index === value1.index ) {
            this.selected_card.push(item2);
          }
        });
      });
      this.skipSelectedCardUpdate = false;
    }
  }

  private setSelectedCards(): void {
    const f = 'setSelectedCards';
    // if ( ! this.skipSelectedCardUpdate ) {
      const selCardIds: number[] = new Array<number>();
      this.selected_card.forEach(item => {
        selCardIds.push(Number(item.index));
      });
      this.onUpdatedCardSelection.emit(selCardIds);
    // }
  }

  onRowSelect(name: string, event: Event) {
    const f = 'onRowSelect';
    console.log(this.c, f);

    this.setSelectedCards();

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
    this.selected_card = [];
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardsComponent.STR_INIT: {
        this.init();
      } break;
    }
  }
}
