import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { CardService } from '../../../service/card/card.service';
import { SelectionService } from '../../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';

@Component({
  selector: 'app-card-title',
  templateUrl: './card-title.component.html',
  styleUrls: ['./card-title.component.css']
})
export class CardTitleComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED = AppSettings.STR_STEP_UPDATED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'StepsComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  txtCardName: string;

  constructor(
    private cardService: CardService
    , private selectionService: SelectionService
  ) { }

  ngOnInit() {

    const f = 'ngOnInit';
    console.log(this.c, f);


    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => {
        console.log(this.c, f, 'cardSubscription', item);
        switch (item) {
          case CardServiceType.CARD_RELOADED: {
            this.btnClicked(CardTitleComponent.STR_CARD_RELOADED);
          } break;
          case CardServiceType.STEP_RELOADED: {
            this.btnClicked(CardTitleComponent.STR_STEP_RELOADED);
          } break;
          case CardServiceType.STEP_UPDATED: {
            this.btnClicked(CardTitleComponent.STR_STEP_UPDATED);
          } break;
        }
      });

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            this.btnClicked(CardTitleComponent.STR_CARD_SELECTED);
          } break;
        }
      });
  }


  ngOnDestroy() {
    const f = 'ngOnDestroy';
    console.log(this.c, f);

      // prevent memory leak when component is destroyed
      this.selectionSubscription.unsubscribe();
      this.cardSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if (changes[CardTitleComponent.STR_NORIFY_FROM_PARENT]) {
      switch (changes[CardTitleComponent.STR_NORIFY_FROM_PARENT].currentValue) {
        // case StepsComponent.STR_NEWSTEP: {
        // } break;
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
    this.txtCardName = '';
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardTitleComponent.STR_INIT: {
        this.init();
      } break;
      case CardTitleComponent.STR_CARD_RELOADED: {
        this.txtCardName = '';
      } break;
      case CardTitleComponent.STR_CARD_SELECTED: {
        this.txtCardName = this.selectionService.getSelectedCardId();
      } break;
    }
  }
}
