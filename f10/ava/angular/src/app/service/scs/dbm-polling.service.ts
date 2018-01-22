import { Injectable } from '@angular/core';
import { UtilsHttpModule } from '../utils-http/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { DbmSettings } from './dbm-settings';
import { AppSettings } from '../../app-settings';
import { SubInfo, EqpSubInfo, DbmPollingServiceType, DbmNotify, DbmPollingSettings } from './dbm-polling-settings';
import { Card } from '../../model/Scenario';
import { Subscription } from 'rxjs/Subscription';
import { SettingsService } from '../settings.service';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';

@Injectable()
export class DbmPollingService {

  private interval = 500;

  private subscriptions: Map<string, Subscription> = new Map<string, Subscription>();

  private values: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();

  readonly c = 'DbmPollingService';

  // Observable source
  private dbmPollingSource = new BehaviorSubject<string>('');

  // Observable dbmPollingItem stream
  dbmPollingItem = this.dbmPollingSource.asObservable();

  constructor(
    private settingsService: SettingsService
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) {
    this.loadSettings();
  }

  loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.interval = this.settingsService.getSetting(this.c, f, this.c, DbmPollingSettings.STR_INTERVAL);
  }

  // Service command
  dbmPollingChanged(str: string) {
    const f = 'dbmPollingChanged';
    console.log(this.c, f);
    console.log(this.c, f, str);
    this.dbmPollingSource.next(str);
  }

  notifyUpdate(serviceType: DbmPollingServiceType): void {
    const f = 'notifyUpdate';
    console.log(this.c, f);
    console.log(this.c, f, serviceType);
  }

  unsubscribe(connAddr: string) {
    const f = 'unsubscribe';
    console.log(this.c, f);
    if ( this.subscriptions.has(connAddr) ) {
      this.subscriptions.get(connAddr).unsubscribe();
      this.subscriptions.delete(connAddr);
    }
  }

  subscribe(connAddr: string, univnames: string[]) {
    const f = 'subscribe';
    console.log(this.c, f);

    this.unsubscribe(connAddr);

    this.readValue(connAddr, univnames);

    console.log(this.c, f, 'interval', this.interval, 'connAddr', connAddr);

    this.subscriptions.set(
      connAddr
      , Observable.interval(this.interval).map((x) => {
        console.log(this.c, f, 'interval map', 'card.name');

      }).subscribe((x) => {
        console.log(this.c, f, 'interval subscribe', 'connAddr', connAddr);

      this.readValue(connAddr, univnames);

      })
    );
  }

  readValue(connAddr: string, univnames: string[]) {
    const f = 'readValue';
    console.log(this.c, f);

    const urls: string [] = [];
    univnames.forEach( univname => {
      urls.push(univname);
    });
    const url = connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

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

        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

}
