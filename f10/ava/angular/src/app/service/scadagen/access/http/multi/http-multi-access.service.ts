import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpAccessResult, HttpAccessResultType, HttpAccessWriteResult } from '../Access-interface';
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

  // {
  // "component":"DbmComponent"
  // ,"request":"multiReadValue"
  // ,"parameters":{"dbaddress":["<alias>Scadagen:AVAS0140:avasuppression.level(0)"]}
  // ,"response":{"dbvalue":[0]}
  // }
  // {
  // "component":"DbmComponent"
  // ,"request":"multiWriteValue"
  // ,"parameters":{"values":{"<alias>Scadagen:AVAS0106:avasuppression.level(0)":10}}
  // ,"response":{}
  // }
  access(connAddr: string, dbAddress: Array<Array<string>> | Array<any>, key: string, obs: Array<Observable<any>>, caller: string) {
    const f = 'read';
    console.log(this.c, f);

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'next res[' + res + ']');

      const multiAccessResult: HttpAccessResult = new HttpAccessResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.NEXT;
      multiAccessResult.env = connAddr;

      multiAccessResult.address = new Array<string>();
      for ( let m = 0; m < dbAddress.length; ++m) {
        const v = dbAddress[m];
        if ( null != v ) {
          for ( let n = 0; n < v.length; ++n) {
            multiAccessResult.address.push(v[n]);
          }
        } else {
          multiAccessResult.address.push(null);
        }
      }

      multiAccessResult.values = new Array<string>();
      for ( let m = 0; m < res.length; ++m) {
        const v = res[m];
        if ( null != v ) {
          for ( let n = 0; n < v.length; ++n) {
            multiAccessResult.values.push(v[n]);
          }
        } else {
          multiAccessResult.values.push(null);
        }
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
