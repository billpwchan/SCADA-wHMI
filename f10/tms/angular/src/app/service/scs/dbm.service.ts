import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AppSettings } from '../../app-settings';
import { DbmSettings } from './dbm-settings';

@Injectable()
export class DbmService {

  static readonly c = DbmService.name;

  // Observable source
  private dbmSource = new BehaviorSubject<string>('');

  // Observable dbmItem stream
  dbmItem = this.dbmSource.asObservable();

  private retriveAciData: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();

  private retriveDciData: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();

  private retriveAcquiredDataAttributeFormulasData: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();

  private retriveClassIdData: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();

  // Service command
  dbmChanged(str: string) {
    this.dbmSource.next(str);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) {}


  getRetriveAciData(connAddr: string, univname: string): any {
    return this.retriveAciData.get(connAddr).get(univname);
  }
  retriveAci(connAddr: string, univname: string): void {
    const func = 'retriveAci';
    console.log(func);
    console.log(func, 'connAddr', connAddr, 'univname', univname);

    const urls: string [] = [];
    urls.push(DbmSettings.STR_ALIAS + univname + DbmSettings.STR_AAC + DbmSettings.STR_VETABLE_VENAME);
    urls.push(DbmSettings.STR_ALIAS + univname + DbmSettings.STR_INITVALUE);

    const url = connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

    console.log(func, 'url', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(func, res);
          const json = res;
          const dbvalue = json[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_DBVALUE];
          console.log(func, 'dbvalue', dbvalue);

          this.retriveAciData.set(connAddr, new Map<string, any>().set(univname, dbvalue));

          this.dbmChanged('retriveAci');
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The GET observable is now completed.'); }
    );
  }


  getRetriveDciData(connAddr: string, univname: string): any {
    return this.retriveDciData.get(connAddr).get(univname);
  }
  retriveDci(connAddr: string, univname: string): void {
    const func = 'retriveDci';
    console.log(func);
    console.log(func, 'stconnAddr', connAddr);
    console.log(func, 'stunivname', univname);

    const urls: string [] = [];
    urls.push(DbmSettings.STR_ALIAS + univname + DbmSettings.STR_DAC + DbmSettings.STR_VETABLE_VENAME);
    urls.push(DbmSettings.STR_ALIAS + univname + DbmSettings.STR_INITVALUE);
    urls.push(DbmSettings.STR_ALIAS + univname + DbmSettings.STR_VALUETABLE_LABEL);
    urls.push(DbmSettings.STR_ALIAS + univname + DbmSettings.STR_VALUETABLE_VALUE);

    const url = connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

    console.log(func, 'sturl', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(func, res);

          const dbvalue = res[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_DBVALUE];
          console.log(func, 'dbvalue', dbvalue);
          if ( null != dbvalue ) {

            this.retriveDciData.set(connAddr, new Map<string, any>().set(univname, dbvalue));

            this.dbmChanged('retriveDci');
          }
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The GET observable is now completed.'); }
    );
  }

  getRetriveAcquiredDataAttributeFormulasData(connAddr: string, univname: string): any {
    return this.retriveAcquiredDataAttributeFormulasData.get(connAddr).get(univname);
  }
  private retriveAcquiredDataAttributeFormulas(connAddr: string, univname: string, classId: number ): void {
    const func = 'retriveAcquiredDataAttributeFormulas';
    console.log(func);

    let url = connAddr;
    url += DbmSettings.STR_URL_GETATTRIBUTEFORMULAS;
    url += DbmSettings.STR_ALIAS + univname;

    if ( DbmSettings.INT_ACI_TYPE === classId ) {
      url += DbmSettings.STR_AAC_ACQVALUE;
    } else if ( DbmSettings.INT_DCI_TYPE === classId ) {
      url += DbmSettings.STR_DAC_ACQVALUE;
    }

    console.log(func, 'url', url);

    // Get Class ID
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(func, res);

          const formulas: string[] = res[AppSettings.STR_RESPONSE][DbmSettings.STR_FORMULAS];

          this.retriveAcquiredDataAttributeFormulasData.set(connAddr, new Map<string, any>().set(univname, formulas));

          this.dbmChanged('retriveAcquiredDataAttributeFormulas');
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The GET observable is now completed.'); }
    );

  }

  getRetriveClassIdData(connAddr: string, univname: string): any {
    return this.retriveClassIdData.get(connAddr).get(univname);
  }
  retriveClassId(connAddr: string, univname: string): void {
    const func = 'retriveClassId';
    console.log(func);
    console.log(func, 'connAddr' , connAddr);
    console.log(func, 'univname' , univname);

    const url = connAddr + DbmSettings.STR_URL_GETCLASSID + DbmSettings.STR_ALIAS + univname;

    // Get Class ID
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(func, res);
          const classId = res[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_CLASSID];
          console.log(func, 'classId', classId);

          this.retriveClassIdData.set(connAddr, new Map<string, any>().set(univname, classId));

          this.dbmChanged('retriveClassId');

          this.retriveAcquiredDataAttributeFormulas(connAddr, univname, classId);

        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(func, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(func, 'The GET observable is now completed.'); }
    );
  }
}
