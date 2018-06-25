import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { DbmSettings } from '../../dbm-settings';
import { UtilsHttpModule } from '../../../common/utils-http.module';
import { EnvironmentMappingService } from '../../../envs/environment-mapping.service';

export class DbmRead {

  private interval: number;

  readonly c = 'DbmRead';

  // Observable source
  private dbmReadSource = new BehaviorSubject<string>(null);

  // Observable dbmPollingItem stream
  dbmReadItem = this.dbmReadSource.asObservable();

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) { }

  // Service command
  dbmChanged(obj: any) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    console.log(this.c, f, obj);
    this.dbmReadSource.next(obj);
  }

  readValue(key: string, env: string, univnames: string[]): void {
    const f = 'readValue';
    console.log(this.c, f);

    const urls: string [] = [];
    univnames.forEach( univname => {
      urls.push(univname);
    });

    const connAddr = this.environmentMappingService.getEnv(env);
    console.log(this.c, f, 'connAddr', connAddr, 'env', env);
    const url = connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

    console.log(this.c, f, 'url', url);

    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          if (null != res) {
            const dbvalue = res[DbmSettings.STR_RESPONSE][DbmSettings.STR_ATTR_DBVALUE];
            const entity = {};
            for ( let i = 0; i < univnames.length; ++i) {
              entity[univnames[i]] = dbvalue[i];
            }
            this.dbmChanged(entity);
          } else {
            console.warn(this.c, f, 'res IS NULL, url[' + url + ']');
          }
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }
}
