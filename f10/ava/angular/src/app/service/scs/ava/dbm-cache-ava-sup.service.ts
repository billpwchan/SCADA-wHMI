import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DbmSettings } from '../dbm-settings';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../utils-http/utils-http.module';
import { AppSettings } from '../../../app-settings';
import { AlarmSuppression } from './dbm-ava-settings';

@Injectable()
export class DbmCacheAvaSupService {

  readonly c = 'DbmCacheAvaSupService';

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
  ) { }

  public getUnivnames(env: string): string[] {
    const f = 'getUnivnames';
    console.log(this.c, f);
    const ret: string[] = [];
    const envNode = this.alarmSuppressions.get(env);
    if ( null != envNode ) {
      if ( envNode.size > 0 ) {
        envNode.forEach( (item: AlarmSuppression, key: string) => {
          ret.push(key);
        });
      } else {
        console.warn(this.c, f, 'envNode SIZE IS ZERO');
      }
    } else {
      console.warn(this.c, f, 'envNode IS NULL');
    }
    return ret;
  }

  public getAlarmSuppression(env: string, univname: string): AlarmSuppression {
    const f = 'getAlarmSuppression';
    console.log(this.c, f);
    let ret: AlarmSuppression = null;
    const envNode = this.alarmSuppressions.get(env);
    if ( null != envNode ) {
      ret = envNode.get(univname);
      if ( null == ret ) {
        console.warn(this.c, f, 'level IS NULL');
      }
    } else {
      console.warn(this.c, f, 'envNode IS NULL');
    }
    return ret;
  }

  public getAlarmMatrixData(env: string): Map<number, Map<number, number>> {
    const f = 'getAlarmMatrixData';
    console.log(this.c, f);
    const ret: Map<number, Map<number, number>> = new Map<number, Map<number, number>>();

    const univnames: string[] = this.getUnivnames(env);
    console.log(this.c, f, 'univnames', univnames);

    for ( let i = 0 ; i < univnames.length ; ++i ) {
      const univname: string = univnames[i];
      console.log(this.c, f, 'univname', univname);
      const alarmSuppression = this.getAlarmSuppression(env, univname);

      const geo: number = Number(alarmSuppression.geo);
      let data1 = ret.get(geo);
      if ( null == data1 ) {
        ret.set(geo, new Map<number, number>());
        data1 = ret.get(geo);
      }
      const func: number = Number(alarmSuppression.func);
      const level: number = Number(alarmSuppression.level);
      data1.set(func, level);
    }
    console.log(this.c, f, ret);

    return ret;
  }

}
