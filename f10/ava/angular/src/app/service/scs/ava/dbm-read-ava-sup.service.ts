import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DbmSettings } from '../dbm-settings';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../utils-http/utils-http.module';
import { AppSettings } from '../../../app-settings';
import { AlarmSuppression } from './dbm-ava-settings';
import { DbmCacheAvaSupService } from './dbm-cache-ava-sup.service';

@Injectable()
export class DbmReadAvaSupService {

  readonly c = 'DbmReadAvaSupService';

  // Observable source
  private dbmSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  avaSupItem = this.dbmSource.asObservable();

  // Service command
  dbmChanged(str: string) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(str);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private dbmCacheAvaSupService: DbmCacheAvaSupService
  ) { }

  readData(env: string, univnames: string[], index: number) {
    const f = 'readData';
    console.log(this.c, f);

    let url = null;

    const urls: string [] = [];
    univnames.forEach( univname => {
      urls.push(
        univname
        + DbmSettings.STR_ATTR_LEVEL
        + DbmSettings.STR_OPEN_PARENTHESIS + index + DbmSettings.STR_CLOSE_PARENTHESIS);
      urls.push(univname + DbmSettings.STR_ATTR_GEO);
      urls.push(univname + DbmSettings.STR_ATTR_FUNC);
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

          this.dbmCacheAvaSupService.alarmSuppressions.set(env, new Map<string, AlarmSuppression>());
          const envNode = this.dbmCacheAvaSupService.alarmSuppressions.get(env);
          for ( let i = 0, j = 0 ; i < univnames.length ; ++i ) {
            const univname: string = univnames[i];
            const level = new AlarmSuppression();
            level.level = dbvalue[j++];
            level.geo = dbvalue[j++];
            level.func = dbvalue[j++];
            envNode.set(univname, level);
          }
          this.dbmChanged(env);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

}
