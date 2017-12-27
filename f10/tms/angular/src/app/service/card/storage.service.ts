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

  readonly c = StorageService.name;

  // Observable source
  private storageSource = new BehaviorSubject<StorageResponse>(0);

  // Observable cardStorageItem stream
  storageItem = this.storageSource.asObservable();

  private utilsHttp: UtilsHttpModule = new UtilsHttpModule();

  uesLocalStorage: boolean;
  localStorageName: string;

  remoteUrl: string;
  uploadUrl: string;
  downloadUrl: string;
  downloadMethod: string;
  remoteFileName: string;

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

    const service: string = StorageService.name;
    this.uesLocalStorage = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_USE_LOCAL_STORAGE);
    this.localStorageName = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_LOCAL_STORAGE_NAME);

    this.remoteUrl = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_REMOTE_URL);
    this.uploadUrl = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_UPLOAD_URL);
    this.downloadUrl = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_DOWNLOAD_URL);
    this.downloadMethod = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_DOWNLOAD_METHOD);
    this.remoteFileName = this.settingsService.getSetting(this.c, f, service, StorageSettings.STR_REMOTE_FILENAME);
  }

  saveCards(cards: Card[]): void {
    const f = 'saveCard';
    console.log(this.c, f);
    if ( this.uesLocalStorage ) {
      const strCards: string = JSON.stringify(cards);
      localStorage.setItem(
        this.localStorageName
      , strCards);
      this.storageChanged(StorageResponse.SAVE_SUCCESS);
    } else {
      this.uploadCard(cards);
    }
  }

  loadCard(): void {
    const f = 'loadCard';
    console.log(this.c, f);
    if ( this.uesLocalStorage ) {
      const strCards: string = localStorage.getItem(this.localStorageName);
      const cards: Card[] = JSON.parse(strCards);
      this.cardService.setCards(cards);
      this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
      this.storageChanged(StorageResponse.LOAD_SUCCESS);
    } else {
      this.downloadCard();
    }
  }

  uploadCard(cards: Card[]): void {
    const f = 'uploadCard';
    console.log(this.c, f);
    const url: string = this.remoteUrl + '/' + this.uploadUrl;
    const filepath: string = this.remoteFileName;
    const strcards: string = JSON.stringify(cards);
    this.postData(url, filepath, strcards);
  }

  downloadCard(): void {
    const f = 'downloadCard';
    console.log(this.c, f);

    // Reset the cards
    this.cardService.setCards([]);

    let url = this.remoteUrl + '/' + this.downloadUrl;
    url += '?' + StorageSettings.STR_FILEPATH + '=' + this.remoteFileName;

    // Handle the data recerived
    this.httpClient.get(
      url
    )
      .subscribe(
        res => {
          console.log(this.c, f, 'res[' + res + ']');

          const filepath = res[StorageSettings.STR_FILEPATH];
          console.log(this.c, f, 'filepath[' + filepath + ']');
          // const json = JSON.parse(res);

          const data = res[StorageSettings.STR_DATA];
          console.log(this.c, f, 'data[' + data + ']');

          this.cardService.setCards(JSON.parse(data));
          const cards = this.cardService.getCards();
          console.log(this.c, f, 'cards[', cards, ']');
          this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
          this.storageChanged(StorageResponse.LOAD_SUCCESS);
        }
        , (err: HttpErrorResponse) => {
          this.storageChanged(StorageResponse.LOAD_FAILED);
          this.utilsHttp.httpClientHandlerError(f, err);
        }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
      );
  }

  postData(url: string, filePath: string, data) {
    const f = 'postData';
    console.log(this.c, f);
    console.log(this.c, f, 'url[' + url + ']');
    console.log(this.c, f, 'STR_FILEPATH[' + StorageSettings.STR_FILEPATH + '] filePath[' + filePath + ']');
    console.log(this.c, this.c, f, 'STR_DATA[' + StorageSettings.STR_DATA + '] data[' + data + ']');

    const bodydata = {};
    bodydata[StorageSettings.STR_FILEPATH] = filePath;
    bodydata[StorageSettings.STR_DATA] = data;

    this.httpClient.post(
        url
        , JSON.stringify(bodydata)
        , {headers: {'Content-Type': 'application/json; charset=utf-8'}}
      ).subscribe(
        res => {
          // console.log(this.c, f, res);
          this.storageChanged(StorageResponse.SAVE_SUCCESS);
        }
        , (err: HttpErrorResponse) => {
          this.storageChanged(StorageResponse.SAVE_FAILED);
          this.utilsHttp.httpClientHandlerError(f, err);
        }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The POST observable is now completed.'); }
      );
  }
}

