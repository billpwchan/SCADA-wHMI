import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { AppSettings } from '../../app-settings';
import { DbmSettings } from './dbm-settings';

@Injectable()
export class DbmService {

  readonly c = 'DbmService';

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
    const f = 'retriveAci';
    console.log(this.c, f);
    console.log(this.c, f, 'connAddr', connAddr, 'univname', univname);

    const urls: string [] = [];
    urls.push(DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_AAC + DbmSettings.STR_VETABLE_VENAME);
    urls.push(DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_INITVALUE);

    const url = connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

    console.log(this.c, f, 'url', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          const json = res;
          const dbvalue = json[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_DBVALUE];
          console.log(this.c, f, 'dbvalue', dbvalue);

          this.retriveAciData.set(connAddr, new Map<string, any>().set(univname, dbvalue));

          this.dbmChanged('retriveAci');
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }


  getRetriveDciData(connAddr: string, univname: string): any {
    return this.retriveDciData.get(connAddr).get(univname);
  }
  retriveDci(connAddr: string, univname: string): void {
    const f = 'retriveDci';
    console.log(this.c, f);
    console.log(this.c, f, 'connAddr', connAddr);
    console.log(this.c, f, 'univname', univname);

    const urls: string [] = [];
    urls.push(DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_DAC + DbmSettings.STR_VETABLE_VENAME);
    urls.push(DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_INITVALUE);
    urls.push(DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_VALUETABLE_LABEL);
    urls.push(DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_VALUETABLE_VALUE);

    const url = connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

    console.log(this.c, f, 'url', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);

          const dbvalue = res[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_DBVALUE];
          console.log(this.c, f, 'dbvalue', dbvalue);
          if ( null != dbvalue ) {

            this.retriveDciData.set(connAddr, new Map<string, any>().set(univname, dbvalue));

            this.dbmChanged('retriveDci');
          }
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

  getRetriveAcquiredDataAttributeFormulasData(connAddr: string, univname: string): any {
    return this.retriveAcquiredDataAttributeFormulasData.get(connAddr).get(univname);
  }
  private retriveAcquiredDataAttributeFormulas(connAddr: string, univname: string, classId: number ): void {
    const f = 'retriveAcquiredDataAttributeFormulas';
    console.log(this.c, f);

    let url = connAddr;
    url += DbmSettings.STR_URL_GETATTRIBUTEFORMULAS;
    url += DbmSettings.STR_URL_ALIAS + univname;

    if ( DbmSettings.INT_ACI_TYPE === classId ) {
      url += DbmSettings.STR_AAC_ACQVALUE;
    } else if ( DbmSettings.INT_DCI_TYPE === classId ) {
      url += DbmSettings.STR_DAC_ACQVALUE;
    }

    console.log(this.c, f, 'url', url);

    // Get Class ID
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);

          const formulas: string[] = res[AppSettings.STR_RESPONSE][DbmSettings.STR_FORMULAS];

          this.retriveAcquiredDataAttributeFormulasData.set(connAddr, new Map<string, any>().set(univname, formulas));

          this.dbmChanged('retriveAcquiredDataAttributeFormulas');
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );

  }

  getRetriveClassIdData(connAddr: string, univname: string): any {
    return this.retriveClassIdData.get(connAddr).get(univname);
  }
  retriveClassId(connAddr: string, univname: string): void {
    const f = 'retriveClassId';
    console.log(this.c, f);
    console.log(this.c, f, 'connAddr' , connAddr);
    console.log(this.c, f, 'univname' , univname);

    const url = connAddr + DbmSettings.STR_URL_GETCLASSID + DbmSettings.STR_URL_ALIAS + univname;

    // Get Class ID
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          const classId = res[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_CLASSID];
          console.log(this.c, f, 'classId', classId);

          this.retriveClassIdData.set(connAddr, new Map<string, any>().set(univname, classId));

          this.dbmChanged('retriveClassId');

          this.retriveAcquiredDataAttributeFormulas(connAddr, univname, classId);

        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

  readFormulas(connAddr: string, univname: string): Observable<any> {
    const f = 'readFormulas';
    console.log(this.c, f);

    let url = connAddr;
    url += DbmSettings.STR_URL_GETATTRIBUTEFORMULAS;
    url += DbmSettings.STR_QUOTE + univname + DbmSettings.STR_QUOTE;

    return this.httpClient.get(url)
      .map(this.extractFormulas);
  }

  extractFormulas(res: string) {
    return res[AppSettings.STR_RESPONSE][DbmSettings.STR_FORMULAS];
  }

  writeFormulaStr(connAddr: string, univname: string, formulaStr: string): Observable<any>  {
    const f = 'writeFormulaStr';
    console.log(this.c, f);

    let url = connAddr;
    url += DbmSettings.STR_URL_SETATTRIBUTEFORMULA;
    url += DbmSettings.STR_QUOTE + DbmSettings.STR_ALIAS + univname + DbmSettings.STR_QUOTE;
    url += DbmSettings.STR_FORMULA_OPTION + DbmSettings.STR_QUOTE + formulaStr + DbmSettings.STR_QUOTE;
    return this.httpClient.get(url).map(this.extractResponse);
  }

  extractResponse(res: string) {
    return res[AppSettings.STR_RESPONSE];
  }

  writeFormulaNum(connAddr: string, univname: string, formulaNum: number): Observable<any>  {
    const f = 'writeFormulaStr';
    console.log(this.c, f);

    let url = connAddr;
    url += DbmSettings.STR_URL_SETATTRIBUTEFORMULA;
    url += DbmSettings.STR_QUOTE + DbmSettings.STR_ALIAS + univname + DbmSettings.STR_QUOTE;
    url += DbmSettings.STR_FORMULA_OPTION + formulaNum;
    return this.httpClient.get(url).map(this.extractResponse);
  }

  getAttributes(connAddr: string, attributes: string[]) {
    const f = 'getAttributes';
    console.log(this.c, f);

    let url = connAddr;
    url += DbmSettings.STR_URL_MULTIREAD;
    url += JSON.stringify(attributes);
    return this.httpClient.get(url).map(this.extractAttributes);
  }

  extractAttributes(res: string) {
    return res[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_DBVALUE];
  }

  setAttributes(connAddr: string, attributeValueMap: Map<string, string|number>) {
    const f = 'setAttributes';
    console.log(this.c, f);

    let url = connAddr;
    url += DbmSettings.STR_URL_MULTIWRITE;
    url += JSON.stringify(attributeValueMap);
    return this.httpClient.get(url).map(this.extractResponse);
  }
}
