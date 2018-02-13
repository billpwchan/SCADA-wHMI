import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpAccessReadResult, HttpAccessResultType } from '../Access-interface';
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
  read(connAddr: string, dbAddress: Array<Array<string>>, key: string, obs: Array<Observable<any>>, caller: string) {
    const f = 'read';
    console.log(this.c, f);

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'next res[' + res + ']');

      const multiAccessResult: HttpAccessReadResult = new HttpAccessReadResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.NEXT;
      multiAccessResult.connAddr = connAddr;

      multiAccessResult.dbAddresses = new Array<string>();
      for ( let m = 0; m < dbAddress.length; ++m) {
        const v = dbAddress[m];
        if ( null != v ) {
          for ( let n = 0; n < v.length; ++n) {
            multiAccessResult.dbAddresses.push(v[n]);
          }
        } else {
          multiAccessResult.dbAddresses.push(null);
        }
      }

      multiAccessResult.dbValues = new Array<string>();
      for ( let m = 0; m < res.length; ++m) {
        const v = res[m];
        if ( null != v ) {
          for ( let n = 0; n < v.length; ++n) {
            multiAccessResult.dbValues.push(v[n]);
          }
        } else {
          multiAccessResult.dbValues.push(null);
        }
      }

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    }
    , (err: HttpErrorResponse) => {
      console.log(this.c, f, 'error err[' + err + ']');

      const multiAccessResult: HttpAccessReadResult = new HttpAccessReadResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.ERROR;

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    }
    , () => {
      // console.log(this.c, f, 'complete');

      const multiAccessResult: HttpAccessReadResult = new HttpAccessReadResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.COMPLETE;

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    });
  }

  write(connAddr: string, values: Map<string, string>, key: string, obs: Array<Observable<any>>, caller: string) {
    const f = 'write';
    console.log(this.c, f);

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'next res[' + res + ']');

      const multiAccessResult: HttpAccessReadResult = new HttpAccessReadResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.NEXT;
      multiAccessResult.connAddr = connAddr;

      multiAccessResult.dbAddresses = new Array<string>();
      for ( let m = 0; m < values.size; ++m) {
        const v = values[m];
        if ( null != v ) {
          for ( let n = 0; n < v.length; ++n) {
            multiAccessResult.dbAddresses.push(v[n]);
          }
        } else {
          multiAccessResult.dbAddresses.push(null);
        }
      }

      multiAccessResult.dbValues = new Array<string>();
      for ( let m = 0; m < res.length; ++m) {
        const v = res[m];
        if ( null != v ) {
          for ( let n = 0; n < v.length; ++n) {
            multiAccessResult.dbValues.push(v[n]);
          }
        } else {
          multiAccessResult.dbValues.push(null);
        }
      }

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    }
    , (err: HttpErrorResponse) => {
      console.log(this.c, f, 'error err[' + err + ']');

      const multiAccessResult: HttpAccessReadResult = new HttpAccessReadResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.ERROR;

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    }
    , () => {
      // console.log(this.c, f, 'complete');

      const multiAccessResult: HttpAccessReadResult = new HttpAccessReadResult();
      multiAccessResult.key = key;
      multiAccessResult.method = HttpAccessResultType.COMPLETE;

      const multiResult: MultiResult = new MultiResult();
      multiResult.caller = caller;
      multiResult.httpAccessResult = multiAccessResult;
      this.accessChanged(multiResult);
    });
  }
}
