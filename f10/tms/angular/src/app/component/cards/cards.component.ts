import { Component, OnInit, EventEmitter, Output, SimpleChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CardService } from '../../service/card/card.service';
import { Card } from '../../model/Scenario';
import { DatatableCard } from '../../model/DatatableScenario';
import { AppSettings } from '../../app-settings';
import { CardsSettings } from './cards-settings';
import { OnChanges, OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { FormGroup, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';
import { SelectionService } from '../../service/card/selection.service';

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

  readonly c = CardsComponent.name;

  @Input() cardUpdate: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  // Datatable
  columns_card = [
    { prop: 'name' }
    , { name: 'State' }
  ];
  rows_card = new Array<DatatableCard>();
  selected_card = new Array<DatatableCard>();

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private selectionService: SelectionService
  ) {
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(f);
    
    this.cardSubscription = this.cardService.cardItem
    .subscribe(item => {
      console.log(this.c, f, 'cardSubscription', item);
      if ( CardService.STR_CARD_RELOADED == item ) {
        this.btnClicked(CardsComponent.STR_RELOAD_CARD);
      }
    });

    this.btnClicked(CardsComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestory';
    console.log(f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(f, 'changes[' + changes + ']');
    if ( changes['cardUpdate'] ) {
      switch (changes['cardUpdate'].currentValue) {
        case CardsComponent.STR_RELOAD_CARD: {
        } break;
      }
    }

    // Debug Dump
    // for ( let propName in changes ) {
    //   let change = changes[propName];
    //   let curVal = JSON.stringify(change.currentValue);
    //   let preVal = JSON.stringify(change.previousValue);
    //   console.log(f, 'curVal'+curVal+']');
    //   console.log(f, 'curVal'+curVal+']');
    // }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private reloadCard(): void {
    const f = 'reloadCard';
    console.log(this.c, f);

    // Reset datatable
    this.rows_card = [];

    // Renew datatable from card service
    this.cardService.getCards().forEach((item, index) => {
      const name = item.name;
      const state = item.state ? AppSettings.STR_CARD_STOP_RUNNING : AppSettings.STR_CARD_STOP;
      console.log(this.c, f, 'index[' + index + '] name[' + name + '] state[' + state + ']');
      this.rows_card.push(new DatatableCard(name, state));
    });

    // this.rows_card = [...this.rows_card];

    this.selected_card = [];
    // this.selected_card = [...this.selected_card];
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

  private onRowSelect(name: string, event: Event) {
    this.btnClicked(CardsComponent.STR_CARD_SELECTED, event);
  }
  private init(): void {
    const f = 'init';
    console.log(this.c, f);
    
    // Reset datatable
    this.rows_card = [];    
    // this.rows_card = [...this.rows_card];

    this.selected_card = [];
    // this.selected_card = [...this.selected_card];

    this.cardService.notifyUpdate(CardService.STR_CARD_RELOADED);
  }

  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardsComponent.STR_INIT: {
        this.init();
      } break;
      case CardsComponent.STR_RELOAD_CARD: {
        this.reloadCard();
      } break;
      case CardsComponent.STR_CARD_SELECTED: {
        this.setSelectedCards();
      } break;
    }
    this.sendNotifyParent(btnLabel);
  }
}
