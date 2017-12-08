import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { CardService } from './card.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { StorageSettings } from './storage-settings';
import { Card } from '../../model/Scenario';
import { CardServiceType } from './card-settings';

@Injectable()
export class StorageService {

  public static readonly BOOLEAN_USE_LOCAL_STORAGE = true;

  public static readonly STR_LOCAL_STORAGE_NAME = 'tms_local_storage_name';

  readonly c = StorageService.name;

  // Observable source
  private storageSource = new BehaviorSubject<any>({});

  // Observable cardStorageItem stream
  storageItem = this.storageSource.asObservable();

  private utilsHttp: UtilsHttpModule = new UtilsHttpModule();

  // Service command
  storageChanged(obj: any) {
    const func = 'storageChanged';
    console.log(func);
    this.storageSource.next(obj);
  }

  constructor(
    private httpClient: HttpClient
    , private cardService: CardService
  ) {}

  saveCard(cards: Card[]): void {
    const f = 'saveCard';
    console.log(this.c, f);
    if ( StorageService.BOOLEAN_USE_LOCAL_STORAGE ) {
      const strCards: string = JSON.stringify(cards);
      localStorage.setItem(
      StorageService.STR_LOCAL_STORAGE_NAME
      , strCards);
    } else {
      this.uploadCard(cards);
    }
  }

  loadCard(): void {
    const f = 'loadCard';
    console.log(this.c, f);
    if ( StorageService.BOOLEAN_USE_LOCAL_STORAGE ) {
      const strCards: string = localStorage.getItem(StorageService.STR_LOCAL_STORAGE_NAME);
      const cards: Card[] = JSON.parse(strCards);
      this.cardService.setCards(cards);
      this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
    } else {

    }
  }

  uploadCard(cards: Card[]): void {
    const f = 'uploadCard';
    console.log(this.c, f);
    const url: string = StorageSettings.strUrlfsstorage + '/' + StorageSettings.strUrlUpstreamFile;
    const filepath: string = StorageSettings.STR_TMS_FILENAME_JSON;
    const strcards: string = JSON.stringify(cards);
    this.postData(url, filepath, strcards);
  }

  downloadCard(): void {
    const func = 'downloadCard';
    console.log(func);

    // Reset the cards
    this.cardService.setCards([]);

    let url = StorageSettings.strUrlfsstorage + '/' + StorageSettings.strUrlDownstramFile;
    url += '?' + StorageSettings.STR_FILEPATH + '=' + StorageSettings.STR_TMS_FILENAME_JSON;
    url += '&' + StorageSettings.STR_METHOD + '=' + StorageSettings.STR_STREAM;

    // Handle the data recerived
    {
      this.httpClient.get(
        url
      )
        .subscribe(
          res => {
            console.log(res);

            this.cardService.setCards(JSON.parse(res[StorageSettings.STR_DATA]));
            const cards = this.cardService.getCards();
            console.log(func, 'cards[', cards, ']');
          }
          , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
          , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The GET observable is now completed.'); }
        );
    }
  }

  getData(url: string): string {
    const func = 'getData';
    console.log(func);
    this.httpClient.get(
        url
      )
        .subscribe(
          res => {
            console.log(res);
            return res;
          }
          , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
          , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The GET observable is now completed.'); }
        );
    return null;
  }

  downloadData(): void {
    const func = 'downloadData';
    console.log(func);
    const url = StorageSettings.strUrlfsstorage + '/' + StorageSettings.strUrlDownstramFile
    + '?' + StorageSettings.STR_FILEPATH + '=' + StorageSettings.STR_TMS_FILENAME
    + '&' + StorageSettings.STR_METHOD + '=' + StorageSettings.STR_DOWNLOAD;
    const res = this.getData(url);
  }

  putData(url: string, filePath: string, filedata: File): void {
    const func = 'putData';
    console.log(func);
    console.log(func, 'sturl[' + url + ']');
    console.log(func, 'stSTR_FILEPATH[' + StorageSettings.STR_FILEPATH + '] filePath[' + filePath + ']');
    console.log(func, 'stSTR_DATA[' + StorageSettings.STR_DATA + '] data[' + filedata + ']');

    const bodydata = {};
    bodydata[StorageSettings.STR_FILEPATH] = filePath;
    bodydata[StorageSettings.STR_DATA] = filedata;

    // const bodydata = JSON.stringify({filepath: filePath, data: filedata});
    // console.log('bodydata[' +bodydata+']');

    this.httpClient.put(
      url
      , bodydata
    )
      .subscribe(
          res => {
            console.log(res);
          }
          , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
          , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The PUT observable is now completed.'); }
      );
  }

  postData(url: string, filePath: string, filedata: string) {
    const func = 'postData';
    console.log(func);
    console.log(func, 'url[' + url + ']');
    console.log(func, 'STR_FILEPATH[' + StorageSettings.STR_FILEPATH + '] filePath[' + filePath + ']');
    console.log(func, 'STR_DATA[' + StorageSettings.STR_DATA + '] data[' + filedata + ']');

    const bodydata = {};
    bodydata[StorageSettings.STR_FILEPATH] = filePath;
    bodydata[StorageSettings.STR_DATA] = filedata;

    // const bodydata = JSON.stringify({filepath: filePath, data: filedata});
    // console.log('bodydata[' +bodydata+']');

//  let headers = new HttpHeaders().set('header1', 'hvalue1'); // create header object
//  headers = headers.append('header2', hvalue2); // add a new header, creating a new object

//  let params = new HttpParams().set('filepath', filePath); // create params object
//  params = params.append('param2', value2); // add a new param, creating a new object

    this.httpClient.post(
      url
      , bodydata
//    ,{headers: headers, params: params}
    )
      .subscribe(
        res => {
          console.log(res);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The POST observable is now completed.'); }
      );
  }

  postFile(url: string, filePath: string, filedata: File) {
    const func = 'postData';
    console.log(func);
    console.log(func, 'url[' + url + ']');
    console.log(func, 'STR_FILEPATH[' + StorageSettings.STR_FILEPATH + '] filePath[' + filePath + ']');
    console.log(func, 'STR_DATA[' + StorageSettings.STR_DATA + '] data[' + filedata + ']');

    const bodydata = {};
    bodydata[StorageSettings.STR_FILEPATH] = filePath;
    bodydata[StorageSettings.STR_DATA] = filedata;

    // const bodydata = JSON.stringify({filepath: filePath, data: filedata});
    // console.log('bodydata[' +bodydata+']');

//  let headers = new HttpHeaders().set('header1', 'hvalue1'); // create header object
//  headers = headers.append('header2', hvalue2); // add a new header, creating a new object

//  let params = new HttpParams().set('filepath', filePath); // create params object
//  params = params.append('param2', value2); // add a new param, creating a new object

    this.httpClient.post(
      url
      , bodydata
//    ,{headers: headers, params: params}
    )
      .subscribe(
        res => {
          console.log(res);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The PUT observable is now completed.'); }
      );
  }

}
