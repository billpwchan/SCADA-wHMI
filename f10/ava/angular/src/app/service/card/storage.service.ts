import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { CardService } from './card.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { StorageSettings, StorageResponse } from './storage-settings';
import { Card } from '../../model/Scenario';
import { CardServiceType } from './card-settings';
import { SettingsService } from '../settings.service';

@Injectable()
export class StorageService {

  readonly c = 'StorageService';

  // Observable source
  private storageSource = new BehaviorSubject<StorageResponse>(0);

  // Observable cardStorageItem stream
  storageItem = this.storageSource.asObservable();

  private utilsHttp: UtilsHttpModule = new UtilsHttpModule();

  useLocalStorage: boolean;
  localStorageName: string;

  remoteUrl: string;
  uploadUrl: string;
  downloadUrl: string;
  downloadMethod: string;
  remotePrefix: string;
  remoteExtension: string;

  // Service command
  storageChanged(storageResponse: StorageResponse) {
    const f = 'storageChanged';
    console.log(this.c, f);
    this.storageSource.next(storageResponse);
  }

  constructor(
    private httpClient: HttpClient
    , private settingsService: SettingsService
    , private cardService: CardService
  ) {
    this.loadSettings();
  }

  loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.useLocalStorage = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_USE_LOCAL_STORAGE);
    this.localStorageName = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_LOCAL_STORAGE_NAME);

    this.remoteUrl = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_REMOTE_URL);
    this.uploadUrl = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_UPLOAD_URL);
    this.downloadUrl = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_DOWNLOAD_URL);
    this.downloadMethod = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_DOWNLOAD_METHOD);
    this.remotePrefix = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_REMOTE_FILENAME_PREFIX);
    this.remoteExtension = this.settingsService.getSetting(this.c, f, this.c, StorageSettings.STR_REMOTE_FILENAME_EXTENSION);
  }

  saveCards(cards: Card[]): void {
    const f = 'saveCards';
    console.log(this.c, f);
    if ( this.useLocalStorage ) {
      const strCards: string = JSON.stringify(cards);
      localStorage.setItem(
        this.localStorageName
      , strCards);
      this.storageChanged(StorageResponse.SAVE_SUCCESS);
    } else {
      this.uploadCards(cards);
    }
  }

  loadCards(): void {
    const f = 'loadCards';
    console.log(this.c, f);
    if ( this.useLocalStorage ) {
      const strCards: string = localStorage.getItem(this.localStorageName);
      const cards: Card[] = JSON.parse(strCards);
      this.cardService.setCards(cards);
      this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
      this.storageChanged(StorageResponse.LOAD_SUCCESS);
    } else {
      this.downloadCards(this.processCards);
    }
  }

  uploadCards(cards: Card[]): void {
    const f = 'uploadCard';
    console.log(this.c, f);
    const url: string = this.remoteUrl + '/' + this.uploadUrl;
    const path: string = this.remotePrefix + this.remoteExtension;
    const strcards: string = JSON.stringify(cards);
    this.postData(url
      , path
      , strcards
      , this.storageChanged(StorageResponse.SAVE_SUCCESS)
    );
  }

  // Callback
  processCards(c: string, f: string, cardService: CardService, data: string, cb): void {
    f = 'processCards ' + f;
    console.log(c, f);
    cardService.setCards(JSON.parse(data));
    const cards = cardService.getCards();
    console.log(c, f, 'cards[', cards, ']');
    cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
    if ( null != cb ) {
      cb(StorageResponse.LOAD_SUCCESS);
    }
  }

  downloadCards(cb): void {
    const f = 'downloadCards';
    console.log(this.c, f);

    // Reset the cards
    this.cardService.setCards([]);

    let url = this.remoteUrl + '/' + this.downloadUrl;
    url += '?' + StorageSettings.STR_OPERATION + '=' + StorageSettings.STR_GETFILE;
    url += '&' + StorageSettings.STR_PATH + '=' + this.remotePrefix + this.remoteExtension;

    // Handle the data recerived
    this.httpClient.get(
      url
    )
      .subscribe(
        res => {
          console.log(this.c, f, 'res[' + res + ']');

          const path = res[StorageSettings.STR_PATH];
          console.log(this.c, f, 'path[' + path + ']');

          const data = res[StorageSettings.STR_DATA];
          console.log(this.c, f, 'data[' + data + ']');

          if ( null != cb ) {
            cb(this.c
              , f
              , this.cardService
              , data
              , this.storageChanged(StorageResponse.LOAD_SUCCESS)
            );
          }
        }
        , (err: HttpErrorResponse) => {
          this.storageChanged(StorageResponse.LOAD_FAILED);
          this.utilsHttp.httpClientHandlerError(f, err);
        }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
      );
  }

  postData(url: string, path: string, data: string, cb) {
    const f = 'postData';
    console.log(this.c, f);
    console.log(this.c, f, 'url[' + url + ']');
    console.log(this.c, f, 'STR_PATH[' + StorageSettings.STR_PATH + '] path[' + path + ']');
    console.log(this.c, this.c, f, 'STR_DATA[' + StorageSettings.STR_DATA + '] data[' + data + ']');

    const bodydata = {};
    bodydata[StorageSettings.STR_OPERATION] = StorageSettings.STR_POSTFILE;
    bodydata[StorageSettings.STR_PATH] = path;
    bodydata[StorageSettings.STR_DATA] = data;

    this.httpClient.post(
        url
        , JSON.stringify(bodydata)
        , {headers: {'Content-Type': 'application/json; charset=utf-8'}}
      ).subscribe(
        res => {
          if ( null != cb ) {
            cb();
          }
        }
        , (err: HttpErrorResponse) => {
          this.storageChanged(StorageResponse.SAVE_FAILED);
          this.utilsHttp.httpClientHandlerError(f, err);
        }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The POST observable is now completed.'); }
      );
  }
}

