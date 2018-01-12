import { Component, OnInit, OnDestroy, OnChanges, EventEmitter, Output, SimpleChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { Card } from '../../../model/Scenario';
import { DatatableCard } from '../../../model/DatatableScenario';
import { AppSettings } from '../../../app-settings';
import { CardsSettings } from './../cards/cards-settings';
import { FormGroup, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';
import { SelectionService } from '../../../service/card/selection.service';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { SettingsService } from '../../../service/settings.service';
import { CardEditSettings } from './card-edit-settings';

@Component({
  selector: 'app-card-edit',
  templateUrl: './card-edit.component.html',
  styleUrls: ['./card-edit.component.css']
})
export class CardEditComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c: string = 'CardEditComponent';

  @Input() notifyFromParent: string;
  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  selCardId: string;

  // UI Data Building
  //  New Card
  divNewCardEnable: boolean;
    txtNewAddName: string;
    btnAddDisable: boolean;
    btnAddCancelDisable: boolean;

  // Modify Card
  divModifyCardEnable: boolean;
    txtModifyName: string;
    btnModifySaveDisable: boolean;
    btnModifyCancelDisable: boolean;

  txtNewAddNameTooShortInvalid: boolean;
  txtNewAddNameTooLongInvalid: boolean;
  txtNewAddNameDuplicatedInvalid: boolean;

  txtModifyNameTooShortInvalid: boolean;
  txtModifyNameTooLongInvalid: boolean;
  txtModifyNameDuplicatedInvalid: boolean;

  private cardNameMin = NaN;
  private cardNameMax = NaN;

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
          this.btnClicked(CardEditComponent.STR_CARD_RELOADED);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe(item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionServiceType.CARD_SELECTED: {
          this.btnClicked(CardEditComponent.STR_CARD_SELECTED);
        } break;
      }
    });
    this.btnClicked(CardEditComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  onParentChange(change: string): void {
    const f = 'onParentChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);

    if ( change ) {
      switch (change) {
        case CardEditSettings.STR_CARD_EDIT_ADD_ENABLE: {
          this.btnClicked('newdiv');
        } break;
        case CardEditSettings.STR_CARD_EDIT_MODIFY_ENABLE: {
          this.btnClicked('modify');
        } break;
      }
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[CardEditComponent.STR_NORIFY_FROM_PARENT] ) {
      this.onParentChange(changes[CardEditComponent.STR_NORIFY_FROM_PARENT].currentValue);
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
    this.cardNameMin = Number.parseInt(this.settingsService.getSetting(this.c, f, this.c, CardEditSettings.STR_CARD_NAME_MIN));
    this.cardNameMax = Number.parseInt(this.settingsService.getSetting(this.c, f, this.c, CardEditSettings.STR_CARD_NAME_MAX));
  }

  private newCard(name: string): void {
    const f = 'newCard';
    console.log(this.c, f);
    this.cardService.getCards().push(
      new Card(
        name
        , false
        , false
      ));
    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
    this.cardService.notifyUpdate(CardServiceType.CARD_EDITED);
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

  onChange(name: string, event?: Event): void {
    const f = 'onChange';
    console.log(this.c, f);
    console.log(this.c, f, 'name[' + name + ']');

    if ( 'txtNewAddName' === name ) {
      const newName = this.txtNewAddName;
      console.log(this.c, f, 'newName[' + newName + ']');

      this.btnAddDisable = false;

      this.txtNewAddNameTooShortInvalid = false;
      this.txtNewAddNameTooLongInvalid = false;
      this.txtNewAddNameDuplicatedInvalid = false;

      if ( newName.length <= this.cardNameMin ) {
        this.btnAddDisable = true;
        this.txtNewAddNameTooShortInvalid = true;

      } else if ( newName.length >= this.cardNameMax ) {
        this.btnAddDisable = true;
        this.txtNewAddNameTooLongInvalid = true;

      } else if ( this.cardService.getCards([newName]).length > 0 ) {
        this.btnAddDisable = true;
        this.txtNewAddNameDuplicatedInvalid = true;

      }

    } else if ( 'txtModifyName' === name ) {
      const newName = this.txtModifyName;
      const curName = this.selCardId;

      this.btnModifySaveDisable = false;

      this.txtModifyNameTooShortInvalid = false;
      this.txtModifyNameTooLongInvalid = false;
      this.txtModifyNameDuplicatedInvalid = false;

      if ( newName.length <= this.cardNameMin ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameTooShortInvalid = true;

      } else if ( newName.length >= this.cardNameMax ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameTooLongInvalid = true;

      } else if ( newName === curName ) {
        this.btnModifySaveDisable = true;

      } else if ( this.cardService.getCards([newName]).length > 0 ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameDuplicatedInvalid = true;

      }

    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    //  New Card
    this.divNewCardEnable = false;
    this.txtNewAddName = '';
      this.btnAddDisable = false;
      this.btnAddCancelDisable = false;

    // Modify Card
    this.divModifyCardEnable = false;
      this.txtModifyName = '';
      this.btnModifySaveDisable = false;
      this.btnModifyCancelDisable = false;

    this.txtNewAddNameTooShortInvalid = false;
    this.txtNewAddNameTooLongInvalid = false;
    this.txtNewAddNameDuplicatedInvalid = false;

    this.txtModifyNameTooShortInvalid = false;
    this.txtModifyNameTooLongInvalid = false;
    this.txtModifyNameDuplicatedInvalid = false;

  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardEditComponent.STR_INIT: {
        this.init();
      } break;
      case CardEditComponent.STR_CARD_RELOADED: {
      } break;
      case CardEditComponent.STR_CARD_SELECTED: {
        this.selCardId = this.selectionService.getSelectedCardId();
      } break;
      case 'newdiv': {
        this.divNewCardEnable = true;
        const translatedStr = this.translate.instant(CardEditSettings.STR_NEW_CARD_PREFIX);
        this.txtNewAddName = this.getNewName(translatedStr);
      } break;
      case 'newadd': {
        this.newCard(this.txtNewAddName);
        this.divNewCardEnable = false;
        this.init();
      } break;
      case 'newaddcancel': {
        this.divNewCardEnable = false;
      } break;
      case 'modify': {
        this.txtModifyName = this.selCardId;
        this.divModifyCardEnable = true;
        this.btnModifySaveDisable = true;
      } break;
      case 'modifysave': {
        this.setCurrentCardName(this.selCardId, this.txtModifyName);
        this.init();
      } break;
      case 'modifysavecancel': {
        this.divModifyCardEnable = false;
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

    // this.sendNotifyParent(btnLabel);
  }
}
