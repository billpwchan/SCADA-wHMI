import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { DbmPollingServiceType, DbmNotify, DbmPollingSettings, DbmPollingCfg } from './dbm-polling-settings';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';
import { SettingsService } from '../../../settings.service';
import { DbmSettings } from '../dbm-settings';
import { DbmPolling } from './dbm-polling';
import { UtilsHttpModule } from '../../common/utils-http.module';
import { EnvironmentMappingService } from '../../envs/environment-mapping.service';

@Injectable()
export class DbmPollingService {

  private interval = 1000;

  readonly c = 'DbmPollingService';

  // Observable source
  private dbmPollingSource = new BehaviorSubject<string>(null);

  // Observable dbmPollingItem stream
  dbmPollingItem = this.dbmPollingSource.asObservable();

  private dbmPolling: DbmPolling;
  private dbmPollingSubscription: Subscription;

  constructor(
    private settingsService: SettingsService
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    this.dbmPolling = new DbmPolling(httpClient, utilsHttp, environmentMappingService);
    this.dbmPollingSubscription = this.dbmPolling.dbmPollingItem
    .subscribe( res => {
      const f2 = 'dbmPollingSubscription';
      console.log(this.c, f2);
      if ( null != res ) {
        this.dbmPollingChanged(res);
      }
    });

    this.loadSettings();
  }

  loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.interval = this.settingsService.getSetting(this.c, f, this.c, DbmPollingSettings.STR_INTERVAL);
    const dbmPollingCfg: DbmPollingCfg = new DbmPollingCfg();
    dbmPollingCfg.interval = this.interval;
    if ( null != this.dbmPolling) { this.dbmPolling.setSettings(dbmPollingCfg); }
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
    if ( null != this.dbmPolling) { this.dbmPolling.unsubscribe(key); }
  }

  subscribe(key: string, connAddr: string, univnames: string[]): void {
    const f = 'subscribe';
    console.log(this.c, f);
    if ( null != this.dbmPolling) { this.dbmPolling.subscribe(key, connAddr, univnames); }
  }
}
