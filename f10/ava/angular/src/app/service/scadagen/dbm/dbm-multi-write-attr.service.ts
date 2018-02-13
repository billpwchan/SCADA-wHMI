import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../utils/utils-http.module';
import { DbmService } from './dbm.service';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpAccessReadResult } from '../access/http/Access-interface';
import { HttpMultiAccessService } from '../access/http/multi/http-multi-access.service';
import { Subscription } from 'rxjs/Subscription';
import { DbmSettings } from './dbm-settings';

@Injectable()
export class DbmMultiWriteAttrService {

  readonly c = 'DbmMultiWriteAttrService';

  // Observable source
  private dbmSource = new BehaviorSubject<HttpAccessReadResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  private httpMultiAccessSubscription: Subscription;

  // Service command
  dbmChanged(res: HttpAccessReadResult) {
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
    const f = 'read';
    console.log(this.c, f);
    const obs = Array<Observable<any>>();

    const keys = Object.keys(values);
    for (let n = 0; n < keys.length; ++n) {
      const obj = {};
      obj[keys[n]] = values[keys[n]];
      obs.push(this.dbmService.setAttributes(env, obj));
    }

    this.httpMultiAccessService.write(env, values, key, obs, this.c);
  }

}
