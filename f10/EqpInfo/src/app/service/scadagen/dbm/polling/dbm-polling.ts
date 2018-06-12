import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { DbmPollingServiceType, DbmNotify, DbmPollingSettings, DbmPollingCfg } from './dbm-polling-settings';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';
import { SettingsService } from '../../../settings.service';
import { DbmSettings } from '../dbm-settings';
import { UtilsHttpModule } from '../../common/utils-http.module';
import { EnvironmentMappingService } from '../../envs/environment-mapping.service';

export class DbmPolling {

  private interval: number;

  private subscriptions: Map<string, Subscription> = new Map<string, Subscription>();

  readonly c = 'DbmPolling';

  // Observable source
  private dbmPollingSource = new BehaviorSubject<string>(null);

  // Observable dbmPollingItem stream
  dbmPollingItem = this.dbmPollingSource.asObservable();

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
  }

  setSettings(cfg: DbmPollingCfg): void {
    const f = 'setSettings';
    console.log(this.c, f);

    this.interval = cfg.interval;

    console.log(this.c, f, 'this.interval', this.interval);
  }

  // Service command
  dbmPollingChanged(obj: any) {
    const f = 'dbmPollingChanged';
    console.log(this.c, f);
    console.log(this.c, f, obj);
    this.dbmPollingSource.next(obj);
  }

  notifyUpdate(serviceType: DbmPollingServiceType): void {
    const f = 'notifyUpdate';
    console.log(this.c, f);
    console.log(this.c, f, serviceType);
  }

  unsubscribe(key: string): void {
    const f = 'unsubscribe';
    console.log(this.c, f);
    if ( this.subscriptions.has(key) ) {
      this.subscriptions.get(key).unsubscribe();
      this.subscriptions.delete(key);
    }
  }

  subscribe(key: string, env: string, univnames: string[]): void {
    const f = 'subscribe';
    console.log(this.c, f);

    this.unsubscribe(key);

    this.readValue(key, env, univnames);

    console.log(this.c, f, 'interval', this.interval, 'env', env);

    this.subscriptions.set(
      env
      , Observable.interval(this.interval).map((x) => {
        console.log(this.c, f, 'interval map', 'env', env);
      }).subscribe((x) => {
        console.log(this.c, f, 'interval subscribe', 'env', env);
        this.readValue(key, env, univnames);
      })
    );

  }

  readValue(key: string, env: string, univnames: string[]): void {
    const f = 'readValue';
    console.log(this.c, f);

    const urls: string [] = [];
    univnames.forEach( univname => {
      urls.push(univname);
    });

    const connAddr = this.environmentMappingService.getEnvs(env);
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
            this.dbmPollingChanged(entity);
          } else {
            console.warn(this.c, f, 'res IS NULL, url[' + url + ']');
          }
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }
}
