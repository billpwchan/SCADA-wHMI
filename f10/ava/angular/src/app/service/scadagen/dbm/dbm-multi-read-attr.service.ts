import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../utils/utils-http.module';
import { DbmService } from './dbm.service';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpAccessResult } from '../access/http/Access-interface';
import { HttpMultiAccessService } from '../access/http/multi/http-multi-access.service';
import { Subscription } from 'rxjs/Subscription';

@Injectable()
export class DbmMultiReadAttrService {

  readonly c = 'DbmMultiReadAttrService';

  // Observable source
  private dbmSource = new BehaviorSubject<HttpAccessResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  private httpMultiAccessSubscription: Subscription;

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

  read(env: string, address: string[], key: string, limit: number = 2000) {
    const f = 'read';
    console.log(this.c, f);
    const obs = Array<Observable<any>>();
    const addrGrp = new Array<Array<string>>();

    let m = 0;
    while ( m < address.length ) {
      const attributes: string[] = new Array<string>();
      const addresses: string[] = new Array<string>();

      let n = 0;
      while ( n < limit && m < address.length ) {
        const addr = address[m];

        attributes.push(addr);
        addresses.push(addr);

        if ( null != addr ) {
          n += addr.length;
        }
        ++m;
      }
      console.log(this.c, f, 'm[' + m + '] n[' + n + '] obs.length[' + obs.length + '] attributes.length[' + attributes.length + ']');
      obs.push(this.dbmService.getAttributes(env, attributes));
      addrGrp.push(addresses);
    }

    this.httpMultiAccessService.access(env, addrGrp, key, obs, this.c);
  }

}
