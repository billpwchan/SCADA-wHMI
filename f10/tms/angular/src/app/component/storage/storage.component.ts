import { Component, OnInit, Output, EventEmitter, SimpleChanges, Input } from '@angular/core';
import { StorageService } from '../../service/card/storage.service';
import { CardService } from '../../service/card/card.service';
import { Card } from '../../model/Scenario';
import { Subscription } from 'rxjs/Subscription';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { AppSettings } from '../../app-settings';

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.css']
})
export class StorageComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_CARD_MODIFIED = 'cardmodified';

  readonly c = StorageComponent.name;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  storageSubscription: Subscription;

  btnDisabledSaveScenario: boolean;
  btnDisabledReloadScenario: boolean;

  disableSaveToStorageMsg: boolean;
  disableSaveToStorageSuccessMsg: boolean;
  disableSaveToStorageFaildMsg: boolean;

  disableLoadFromStorageConfirmMsg: boolean;
  disableReloadFromStorageSuccessMsg: boolean;
  disableReloadFromStorageFailedMsg: boolean;

  ignoreReload: boolean = false;

  constructor(
    private storageService: StorageService
    , private cardService: CardService
  ) {}

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscription = this.cardService.cardItem
    .subscribe( item => {
      console.log(this.c, f, 'storageSubscription', item);
      if ( CardService.STR_CARD_RELOADED == item ) {
        if ( !this.ignoreReload ) {
          this.btnClicked(StorageComponent.STR_CARD_MODIFIED);
        }
        this.ignoreReload = false;
        
      }
    });

    this.storageSubscription = this.storageService.storageItem
    .subscribe( item => {
      console.log(this.c, f, 'storageSubscription', item);
    });

    this.btnClicked(StorageComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    this.cardSubscription.unsubscribe();
    this.storageSubscription.unsubscribe();
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f, 'btnLabel[' + btnLabel +']');
    switch (btnLabel) {
      case StorageComponent.STR_INIT: {
        // Reload avaiable only
        this.btnDisabledSaveScenario = true;
        this.btnDisabledReloadScenario = false;
      
        this.disableSaveToStorageMsg = true;
        this.disableSaveToStorageSuccessMsg = true;
        this.disableSaveToStorageFaildMsg = true;
      
        this.disableLoadFromStorageConfirmMsg = true;
        this.disableReloadFromStorageSuccessMsg = true;
        this.disableReloadFromStorageFailedMsg = true;
      } break;
      case StorageComponent.STR_CARD_MODIFIED: {
        // Save avaiable
        this.btnDisabledSaveScenario = false;
        this.btnDisabledReloadScenario = false;
      
        this.disableSaveToStorageMsg = true;
        this.disableSaveToStorageSuccessMsg = true;
        this.disableSaveToStorageFaildMsg = true;
      
        this.disableLoadFromStorageConfirmMsg = true;
        this.disableReloadFromStorageSuccessMsg = true;
        this.disableReloadFromStorageFailedMsg = true;
      } break;
      case 'savescenario': {
        const cards: Card[] = this.cardService.getCards();
        this.storageService.saveCard(cards);
        this.disableSaveToStorageMsg = true;
        this.disableLoadFromStorageConfirmMsg = true;
        this.btnDisabledSaveScenario = true;
        this.btnDisabledReloadScenario = false;
      } break;
      case 'reloadscenario': {
        this.disableSaveToStorageMsg = true;
        this.disableLoadFromStorageConfirmMsg = false;
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
    }
    this.sendNotifyParent(btnLabel);
  }
}
