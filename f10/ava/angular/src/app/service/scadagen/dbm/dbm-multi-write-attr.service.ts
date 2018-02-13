import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../utils/utils-http.module';
import { DbmService } from './dbm.service';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpAccessResult } from '../access/http/Access-interface';
import { HttpMultiAccessService } from '../access/http/multi/http-multi-access.service';
import { Subscription } from 'rxjs/Subscription';
import { DbmSettings } from './dbm-settings';

@Injectable()
export class DbmMultiWriteAttrService {

  readonly c = 'DbmMultiWriteAttrService';

  // Observable source
  private dbmSource = new BehaviorSubject<HttpAccessResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  private httpMultiAccessSubscription: Subscription;

  private threshold = 2000;
  private encode = {'#': '%23', '&': '%26'};

  setThreshold(threshold: number) {
    this.threshold = threshold;
  }

  setEncode(encode) {
    this.encode = encode;
  }

  // Service command
  dbmChanged(res: HttpAccessResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(res);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private dbmService: DbmService
    , private httpMultiAccessService: HttpMultiAccessService
  ) {

    this.httpMultiAccessSubscription = this.httpMultiAccessService.accessItem
      .subscribe( res => {
        const f = 'httpMultiAccessSubscription';
        console.log(this.c, f);
        if ( null != res ) {
          if ( this.c === res.caller ) {
            this.dbmChanged(res.httpAccessResult);
          }
        }
      });

  }

  write(env: string, values, key: string) {
    const f = 'write';
    console.log(this.c, f);

    const obs = Array<Observable<any>>();
    const addrs = Array<any>();
    const keys = Object.keys(values);
    const keysLen = keys.length;
    let m = 0;
    while ( m < keysLen ) {
      const attributes = {};
      const addresses = {};

      let strLen = 0;
      while ( strLen < this.threshold && m < keysLen ) {
        const k = keys[m];
        let v = values[k];
        // v = encodeURI(v);
        // v = encodeURIComponent(v);
        if ( typeof v === 'string') {
          if ( null != this.encode ) {
            Object.keys(this.encode).forEach( encodeKey => {
              v = v.split(encodeKey).join(this.encode[encodeKey]);
            });
          }
          // v = v.split('#').join('%23');
          // v = v.split('&').join('%26');
        }

        attributes[k] = v;
        addresses[k] = v;

        const str = JSON.stringify(attributes);
        strLen = str.length;
        ++m;
      }
      obs.push(this.dbmService.setAttributes(env, attributes));
      addrs.push(addresses);
    }

    this.httpMultiAccessService.access(env, addrs, key, obs, this.c);
  }

}
