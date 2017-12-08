import { Component, OnInit, EventEmitter, Output, SimpleChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CardService } from '../../service/card/card.service';
import { Card, CardType } from '../../model/Scenario';
import { DatatableCard } from '../../model/DatatableScenario';
import { AppSettings } from '../../app-settings';
import { CardsSettings } from './../cards/cards-settings';
import { OnChanges, OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { FormGroup, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';
import { SelectionService } from '../../service/card/selection.service';
import { CardServiceType } from '../../service/card/card-settings';
import { SelectionServiceType } from '../../service/card/selection-settings';

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

  readonly c: string = CardEditComponent.name;

  @Input() cardEditUpdate: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  selectedCardName = null;

  // UI Data Building
  // New/Modify/Delete/Copy - Card
  btnNewDisable = false;
  btnModifyDisable = false;
  btnDeleteDisable = false;
  btnCopyDisable = false;

  //  New Card
  divNewCardEnable = false;
    txtNewAddName = '';
    btnAddDisable = false;
    btnAddCancelDisable = false;

  // Modify Card
  divModifyCardEnable = false;
    txtModifyName = '';
    btnModifySaveDisable = false;
    btnModifyCancelDisable = false;

  txtNewAddNameTooShortInvalid = false;
  txtNewAddNameTooLongInvalid = false;
  txtNewAddNameDuplicatedInvalid = false;

  txtModifyNameTooShortInvalid = false;
  txtModifyNameTooLongInvalid = false;
  txtModifyNameDuplicatedInvalid = false;

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
    const f = 'ngOnDestory';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    if ( changes['cardEditUpdate'] ) {
      // Loading
      console.log(this.c, f, 'changes', changes);
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private newCard(name: string) {
    const f = 'newCard';
    console.log(this.c, f);
    this.cardService.getCards().push(
      new Card(
        name
        , CardType.STOP
        , 0
      ));
    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
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
    const sourceCard: Card[] = this.cardService.getCards(this.selectionService.getSelectedCardIds());
    const translatedStr = this.translate.instant(CardsSettings.STR_NEW_CARD_APPENDIX);
    const newCardName = this.getNewName(sourceCard[0].name, translatedStr);
    const clonedCard = sourceCard.map(x => Object.assign({}, x));
    clonedCard[0].name = newCardName;
    this.cardService.addCards([clonedCard[0]]);
    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
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
    this.cardService.getCards([preName])[0].name = curName;
    this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
  }

  private getSelectCardName(): string {
    return this.selectionService.getSelectedCardIds()[0];
  }

  private onChange(name: string, event?: Event): void {
    const f = 'onChange';
    console.log(this.c, f);
    if ( name === 'txtNewAddName' ) {
      const newName = this.txtNewAddName;

      this.btnAddDisable = false;

      this.txtNewAddNameTooShortInvalid = false;
      this.txtNewAddNameTooLongInvalid = false;
      this.txtNewAddNameDuplicatedInvalid = false;

      if ( newName.length <= CardsSettings.INT_CARD_NAME_MIN ) {
        this.btnAddDisable = true;
        this.txtNewAddNameTooShortInvalid = true;

      } else if ( newName.length >= CardsSettings.INT_CARD_NAME_MAX ) {
        this.btnAddDisable = true;
        this.txtNewAddNameTooLongInvalid = true;

      } else if ( this.cardService.getCards([newName]).length > 0 ) {
        this.btnAddDisable = true;
        this.txtNewAddNameDuplicatedInvalid = true;

      }
    } else if ( name === 'txtModifyName' ) {
      const newName = this.txtModifyName;
      const curName = this.selectedCardName;

      this.btnModifySaveDisable = false;

      this.txtModifyNameTooShortInvalid = false;
      this.txtModifyNameTooLongInvalid = false;
      this.txtModifyNameDuplicatedInvalid = false;

      if ( newName.length <= CardsSettings.INT_CARD_NAME_MIN ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameTooShortInvalid = true;

      } else if ( newName.length >= CardsSettings.INT_CARD_NAME_MAX ) {
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

    this.btnModifyDisable = true;
    this.btnCopyDisable = true;
    this.btnDeleteDisable = true;
    this.divNewCardEnable = false;
    this.divModifyCardEnable = false;
  }

  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case CardEditComponent.STR_INIT: {
        this.init();
      } break;
      case CardEditComponent.STR_CARD_SELECTED: {
        this.btnModifyDisable = false;
        this.btnCopyDisable = false;
        this.btnDeleteDisable = false;
        this.selectedCardName = this.getSelectCardName();
      } break;
      case 'newdiv': {
        this.divNewCardEnable = true;
        const translatedStr = this.translate.instant(CardsSettings.STR_NEW_CARD_PREFIX);
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
        this.txtModifyName = this.selectedCardName;
        this.divModifyCardEnable = true;
        this.btnModifySaveDisable = true;
      } break;
      case 'modifysave': {
        this.setCurrentCardName(this.selectedCardName, this.txtModifyName);
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

    this.sendNotifyParent(btnLabel);
  }
}
