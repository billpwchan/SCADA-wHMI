import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../utils/utils-http.module';
import { DbmService } from './dbm.service';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { MultiReadResult, MultiReadResultType } from './multi-read-interface';

@Injectable()
export class MultiReadService {

  readonly c = 'SgMultiReadService';

  // Observable source
  private dbmSource = new BehaviorSubject<MultiReadResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  private conditions: Map<string, Map<string, number>> = new Map<string, Map<string, number>>();

  // Service command
  dbmChanged(result: MultiReadResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private dbmService: DbmService
  ) { }

  read(connAddr: string, dbaddress: string[], key: string) {
    const f = 'read';
    console.log(this.c, f);
    const obs = Array<Observable<any>>();

    for (let n = 0; n < dbaddress.length; ++n) {
      const attributes: string[] = new Array<string>();
      attributes.push(dbaddress[n]);
      obs.push(this.dbmService.getAttributes(connAddr, attributes));
    }

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'next res[' + res + ']');

      const multiReadResult: MultiReadResult = new MultiReadResult();
      multiReadResult.key = key;
      multiReadResult.result = MultiReadResultType.NEXT;
      multiReadResult.connAddr = connAddr;
      multiReadResult.dbaddress = dbaddress;
      multiReadResult.dbvalue = res;

      this.dbmChanged(multiReadResult);
    }
    , (err: HttpErrorResponse) => {
      const multiReadResult: MultiReadResult = new MultiReadResult();
      multiReadResult.key = key;
      multiReadResult.result = MultiReadResultType.ERROR;
      multiReadResult.connAddr = connAddr;
      multiReadResult.dbaddress = dbaddress;

      this.dbmChanged(multiReadResult);

      console.log(this.c, f, 'error err[' + err + ']');

      this.utilsHttp.httpClientHandlerError(f, err);
    }
    , () => {

      const multiReadResult: MultiReadResult = new MultiReadResult();
      multiReadResult.key = key;
      multiReadResult.result = MultiReadResultType.COMPLETE;

      this.dbmChanged(multiReadResult);

      console.log(this.c, f, 'complete');

      this.utilsHttp.httpClientHandlerComplete(f);
    });
  }

}
