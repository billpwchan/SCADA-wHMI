import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, SimpleChanges, Input } from '@angular/core';
import { StorageService } from '../../../service/card/storage.service';
import { CardService } from '../../../service/card/card.service';
import { Card } from '../../../model/Scenario';
import { Subscription } from 'rxjs/Subscription';
import { AppSettings } from '../../../app-settings';
import { CardServiceType } from '../../../service/card/card-settings';
import { SettingsService } from '../../../service/settings.service';
import { StorageSettings } from './storage-settings';
import { StorageResponse } from '../../../service/card/storage-settings';

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.css']
})
export class StorageComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_CARD_MODIFIED = 'cardmodified';
  public static readonly STR_STEP_MODIFIED = 'stepmodified';

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = StorageComponent.name;

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  storageSubscription: Subscription;

  btnDisabledSave: boolean;
  btnDisabledCancel: boolean;

  btnDisabledSaveScenario: boolean;
  btnDisabledReloadScenario: boolean;

  disableSaveToStorageMsg: boolean;
  disableSaveToStorageSuccessMsg: boolean;
  disableSaveToStorageFaildMsg: boolean;

  disableReloadFromStorageMsg: boolean;

  disableLoadFromStorageConfirmMsg: boolean;
  disableReloadFromStorageSuccessMsg: boolean;
  disableReloadFromStorageFailedMsg: boolean;

  ignoreReload: boolean;

  initCardsBeforeSave: boolean;

  private firstLoaded = false;

  constructor(
    private settingsService: SettingsService
    , private cardService: CardService
    , private storageService: StorageService
  ) {}

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
    .subscribe( item => {
      console.log(this.c, f, 'storageSubscription', item);
      switch (item) {
        case CardServiceType.CARD_EDITED: {
          if ( !this.ignoreReload ) {
            this.btnClicked(StorageComponent.STR_CARD_MODIFIED);
          }
          this.ignoreReload = false;
        } break;
        case CardServiceType.STEP_EDITED: {
          if ( !this.ignoreReload ) {
            this.btnClicked(StorageComponent.STR_STEP_MODIFIED);
          }
          this.ignoreReload = false;
        } break;
        case CardServiceType.CARD_UPDATED: {
          this.btnClicked(StorageComponent.STR_CARD_UPDATED);
        } break;
      }
    });

    this.storageSubscription = this.storageService.storageItem
    .subscribe( item => {
      console.log(this.c, f, 'storageSubscription', item);
      switch ( item ) {
        case StorageResponse.LOAD_SUCCESS: {
          if ( this.firstLoaded ) {
            this.disableSaveToStorageSuccessMsg = true;
            this.disableSaveToStorageFaildMsg = true;
            this.disableReloadFromStorageSuccessMsg = false;
            this.disableReloadFromStorageFailedMsg = true;
          }
          this.firstLoaded = true;
        } break;
        case StorageResponse.LOAD_FAILED: {
          this.disableSaveToStorageSuccessMsg = true;
          this.disableSaveToStorageFaildMsg = true;
          this.disableReloadFromStorageSuccessMsg = true;
          this.disableReloadFromStorageFailedMsg = false;
        } break;
        case StorageResponse.SAVE_SUCCESS: {
          this.disableSaveToStorageSuccessMsg = false;
          this.disableSaveToStorageFaildMsg = true;
          this.disableReloadFromStorageSuccessMsg = true;
          this.disableReloadFromStorageFailedMsg = true;
        } break;
        case StorageResponse.SAVE_FAILED: {
          this.disableSaveToStorageSuccessMsg = true;
          this.disableSaveToStorageFaildMsg = false;
          this.disableReloadFromStorageSuccessMsg = true;
          this.disableReloadFromStorageFailedMsg = true;
        } break;
      }
    });

    this.btnClicked(StorageComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.storageSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if ( changes[StorageComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[StorageComponent.STR_NORIFY_FROM_PARENT].currentValue) {
        // case StorageComponent.STR_NEWSTEP: {
        // } break;
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadSettings() {
    const f = 'loadSettings';
    console.log(this.c, f);

    const component: string = StorageComponent.name;
    this.initCardsBeforeSave = this.settingsService.getSetting(this.c, f, component, StorageSettings.STR_INIT_CARDS_BEFORE_STORAGE);
  }

  private init(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.ignoreReload  = false;

    this.btnDisabledSave = false;
    this.btnDisabledCancel = false;

    // Reload avaiable only
    this.btnDisabledSaveScenario = true;
    this.btnDisabledReloadScenario = false;

    this.disableSaveToStorageMsg = true;
    this.disableSaveToStorageSuccessMsg = true;
    this.disableSaveToStorageFaildMsg = true;

    this.disableReloadFromStorageMsg = true;

    this.disableLoadFromStorageConfirmMsg = true;
    this.disableReloadFromStorageSuccessMsg = true;
    this.disableReloadFromStorageFailedMsg = true;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case StorageComponent.STR_INIT: {
        this.init();
      } break;
      case StorageComponent.STR_CARD_MODIFIED: {

        this.btnDisabledSaveScenario = false;
        this.btnDisabledReloadScenario = false;

        this.disableSaveToStorageMsg = false;
        this.disableSaveToStorageSuccessMsg = true;
        this.disableSaveToStorageFaildMsg = true;

        this.disableLoadFromStorageConfirmMsg = true;
        this.disableReloadFromStorageSuccessMsg = true;
        this.disableReloadFromStorageFailedMsg = true;
      } break;
      case StorageComponent.STR_STEP_MODIFIED: {

        this.btnDisabledSaveScenario = false;
        this.btnDisabledReloadScenario = false;

        this.disableSaveToStorageMsg = false;
        this.disableSaveToStorageSuccessMsg = true;
        this.disableSaveToStorageFaildMsg = true;

        this.disableLoadFromStorageConfirmMsg = true;
        this.disableReloadFromStorageSuccessMsg = true;
        this.disableReloadFromStorageFailedMsg = true;
      } break;
      case 'savescenario': {
        const cards: Card[] = this.cardService.getCards();
        const strCards = JSON.stringify(cards);
        let clonedCards = JSON.parse(strCards);
        if ( this.initCardsBeforeSave ) {
          clonedCards = this.cardService.initCards(clonedCards);
        }
        this.storageService.saveCards(clonedCards);
        this.disableSaveToStorageMsg = true;
        this.disableLoadFromStorageConfirmMsg = true;
        this.btnDisabledSaveScenario = true;
        this.btnDisabledReloadScenario = false;
      } break;
      case 'reloadscenario': {
        this.disableSaveToStorageMsg = true;
        this.disableLoadFromStorageConfirmMsg = false;

        this.disableReloadFromStorageMsg = false;
      } break;
      case 'confirm': {
        this.disableSaveToStorageMsg = true;
        this.disableLoadFromStorageConfirmMsg = true;
        this.btnDisabledSaveScenario = true;
        this.btnDisabledReloadScenario = true;
        this.ignoreReload = true;
        this.storageService.loadCard();
      } break;
      case 'cancel': {
        this.disableSaveToStorageMsg = true;
        this.disableLoadFromStorageConfirmMsg = true;
      } break;
      case 'disableSaveToStorageMsg': {
        this.disableSaveToStorageMsg = true;
      } break;
      case 'disableSaveToStorageSuccessMsg': {
        this.disableSaveToStorageSuccessMsg = true;
      } break;
      case 'disableSaveToStorageFaildMsg': {
        this.disableSaveToStorageFaildMsg = true;
      } break;
      case 'disableReloadFromStorageSuccessMsg': {
        this.disableReloadFromStorageSuccessMsg = true;
      } break;
      case 'disableReloadFromStorageFailedMsg': {
        this.disableReloadFromStorageFailedMsg = true;
      } break;
    }

    if ( this.cardService.isRunning() ) {
      this.btnDisabledSaveScenario = true;
      this.btnDisabledReloadScenario = true;
    } else {
      this.btnDisabledSaveScenario = false;
      this.btnDisabledReloadScenario = false;
    }

    // this.sendNotifyParent(btnLabel);
  }
}
