import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpAccessResult, HttpAccessResultType } from '../Access-interface';
import { HttpErrorResponse } from '@angular/common/http';
import { MultiResult } from './multi-settings';

@Injectable()
export class HttpMultiAccessService {

  readonly c = 'HttpMultiAccessService';

  // Observable source
  private accessSource = new BehaviorSubject<MultiResult>(null);

  // Observable cardItem stream
  accessItem = this.accessSource.asObservable();

  // Service command
  accessChanged(result: MultiResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.accessSource.next(result);
  }

  constructor() {}

  access(connAddr: string, dbAddress, key: string, obs: Array<Observable<any>>, caller: string) {
    const f = 'access';
    console.log(this.c, f);

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'next res[' + res + ']');

      const multiAccessResult: HttpAccessResult = new HttpAccessResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.NEXT;
      multiAccessResult.connAddr = connAddr;
      multiAccessResult.dbAddresses = dbAddress;
      multiAccessResult.dbValues = new Array<string>();
      for ( let n = 0; n < res.length; ++n) {
        const v = res[n];
        const r = ( null != v && v.length > 0 ? v[0] : null);
        multiAccessResult.dbValues.push(r);
      }

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    }
    , (err: HttpErrorResponse) => {
      console.log(this.c, f, 'error err[' + err + ']');

      const multiAccessResult: HttpAccessResult = new HttpAccessResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.ERROR;
      multiAccessResult.connAddr = connAddr;
      multiAccessResult.dbAddresses = dbAddress;

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    }
    , () => {
      // console.log(this.c, f, 'complete');

      const multiAccessResult: HttpAccessResult = new HttpAccessResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.COMPLETE;

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    });
  }

}
