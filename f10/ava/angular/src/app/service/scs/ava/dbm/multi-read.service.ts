import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../utils-http/utils-http.module';
import { DbmSettings } from '../../dbm-settings';
import { AppSettings } from '../../../../app-settings';

export class MultiReadResult {
  public env: string;
  public dbAddresses: string[];
  public dbValue;
}

@Injectable()
export class MultiReadService {

  readonly c = 'MultiReadService';

  // Observable source
  private dbmSource = new BehaviorSubject<MultiReadResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  // Service command
  dbmChanged(result: MultiReadResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    // , private dbmCacheAvaSupService: DbmCacheAvaSupService
  ) { }

  readData(env: string, dbAddresses: string[]) {
    const f = 'readData';
    console.log(this.c, f);

    let url = null;

    const urls: string [] = [];
    dbAddresses.forEach( dbAddress => {
      urls.push(dbAddress);
    });
    url = env + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);
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
          console.log(this.c, f, 'env', env, 'url', url);

          const result: MultiReadResult = new MultiReadResult();
          result.env = env;
          result.dbAddresses = dbAddresses;
          result.dbValue = dbvalue;
          this.dbmChanged(result);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

}
