import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AppSettings } from '../../../app-settings';
import { AlarmSuppression } from './dbm-ava-settings';
import { DbmCacheAvaSupService } from './dbm-cache-ava-sup.service';
import { UtilsHttpModule } from '../../scadagen/utils/utils-http.module';
import { DbmSettings } from '../../scadagen/dbm/dbm-settings';

@Injectable()
export class DbmWriteAvaSupService {

  readonly c = 'DbmWriteAvaSupService';

  // Observable source
  private dbmSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  avaSupItem = this.dbmSource.asObservable();

  alarmSuppressions: Map<string, Map<string, AlarmSuppression>> = new Map<string, Map<string, AlarmSuppression>>();

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

  writeData(env: string, data: Map<number, Map<number, number>>, index: number) {
    const f = 'writeData';
    console.log(this.c, f);

    let url = null;
    const values = {};
    const univnames: string[] = this.dbmCacheAvaSupService.getUnivnames(env);
    data.forEach ( (value1, key1: number) => {
      value1.forEach( (value2, key2: number) => {
        let found = false;
        for ( let i = 0 ; i < univnames.length ; ++i ) {
          if ( found ) {
            break;
          }
          const alarmSuppression: AlarmSuppression = this.dbmCacheAvaSupService.getAlarmSuppression(env, univnames[i]);
          if ( null != alarmSuppression ) {
            if ( alarmSuppression.geo === key1 && alarmSuppression.func === key2 ) {
              const alias = univnames[i]
                            + DbmSettings.STR_ATTR_LEVEL
                            + DbmSettings.STR_OPEN_PARENTHESIS + index + DbmSettings.STR_CLOSE_PARENTHESIS;
              values[alias] = value2;
              found = true;
            }
          }
        }
      });
    });

    url = env + DbmSettings.STR_URL_MULTIWRITE + JSON.stringify(values);
    console.log(this.c, f, 'url', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          const json = res;

          this.dbmChanged(env);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }
}
