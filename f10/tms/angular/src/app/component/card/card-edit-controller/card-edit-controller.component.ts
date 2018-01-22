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
import { CardEditSettings } from '../card-edit/card-edit-settings';
import { CardEditControllerSettings } from './card-edit-controller-settings';

@Component({
  selector: 'app-card-edit-controller',
  templateUrl: './card-edit-controller.component.html',
  styleUrls: ['./card-edit-controller.component.css']
})
export class CardEditControllerComponent implements OnInit , OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c: string = 'CardEditControllerComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  selectedCardName: string;

  btnNewDisable: boolean;
  btnModifyDisable: boolean;
  btnDeleteDisable: boolean;
  btnCopyDisable: boolean;

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
          this.btnClicked(CardEditControllerComponent.STR_CARD_RELOADED);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe(item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionServiceType.CARD_SELECTED: {
          this.btnClicked(CardEditControllerComponent.STR_CARD_SELECTED);
        } break;
      }
    });
    this.btnClicked(CardEditControllerComponent.STR_INIT);
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
    if ( changes[CardEditControllerComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[CardEditControllerComponent.STR_NORIFY_FROM_PARENT].currentValue) {
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

  /**
   * Delete the card from CardService by current selection
   *
   * @private
   * @memberof CardsComponent
   */
  private deleteCard() {
    const f = 'deleteCard';
    console.log(this.c, f);
    const identitys: string[] = new Array<string>();
    this.cardService.getCards(this.selectionService.getSelectedCardIds()).forEach ( item => {
      identitys.push(item.name);
    });
    this.cardService.deleteCards(identitys);
    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
    this.cardService.notifyUpdate(CardServiceType.CARD_EDITED);
  }

  /**
   * Copy the card from CardService by current selection
   *
   * @private
   * @memberof CardsComponent
   */
  private CopyCard() {
    const f = 'CopyCard';
    console.log(this.c, f);

    const sourceCard: Card = this.cardService.getCard(this.selectionService.getSelectedCardIds());
    const strNewCard: string = JSON.stringify(sourceCard);
    const newCard: Card = JSON.parse(strNewCard);
    newCard.name = this.getNewName(sourceCard.name, this.translate.instant(CardEditSettings.STR_NEW_CARD_APPENDIX));
    this.cardService.addCards([newCard]);

    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
    this.cardService.notifyUpdate(CardServiceType.CARD_EDITED);
  }

  /**
   * Get Non-duplicate card name with name prefix
   *
   * @private
   * @returns {string} new non-duplicate card name
   * @memberof CardsComponent
   */
  private getNewName(prefix: string, appendix: string = ''): string {
    let counter = this.cardService.getCards().length;
    let name = prefix + appendix + counter;
    while ( this.cardService.getCards([name]).length > 0 ) {
      name = prefix + appendix + ++counter;
    }
    return name;
  }

  private setCurrentCardName(preName: string, curName: string): void {
    this.cardService.getCard([preName]).name = curName;
    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
  }

  private getSelectCardName(): string {
    return this.selectionService.getSelectedCardId();
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnNewDisable = false;
    this.btnModifyDisable = true;
    this.btnDeleteDisable = true;
    this.btnCopyDisable = true;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardEditControllerComponent.STR_INIT: {
        this.init();
      } break;
      case CardEditControllerComponent.STR_CARD_RELOADED: {
        this.init();
      } break;
      case CardEditControllerComponent.STR_CARD_SELECTED: {
        this.btnModifyDisable = false;
        this.btnCopyDisable = false;
        this.btnDeleteDisable = false;
        this.selectedCardName = this.getSelectCardName();
      } break;
      case 'newdiv': {
        this.sendNotifyParent(CardEditControllerSettings.STR_CARD_EDIT_CONTROLLER_ADD_ENABLE);
      } break;
      case 'modify': {
        this.sendNotifyParent(CardEditControllerSettings.STR_CARD_EDIT_CONTROLLER_MODIFY_ENABLE);
      } break;
      case 'delete': {
        this.deleteCard();
        this.init();
      } break;
      case 'copy': {
        this.CopyCard();
        this.init();
      } break;
    }

    if ( this.cardService.isRunning() ) {
      this.btnNewDisable = true;
      this.btnModifyDisable = true;
      this.btnDeleteDisable = true;
      this.btnCopyDisable = true;
    }

    // this.sendNotifyParent(btnLabel);
  }
}
