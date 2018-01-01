import { Injectable } from '@angular/core';
import { UtilsHttpModule } from '../utils-http/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { DbmSettings } from './dbm-settings';
import { AppSettings } from '../../app-settings';
import { Observable } from 'rxjs/Observable';
import { SubInfo, EqpSubInfo, DbmPollingServiceType, DbmNotify, DbmPollingSettings } from './dbm-polling-settings';
import { Card } from '../../model/Scenario';
import { Subscription } from 'rxjs/Subscription';
import { SettingsService } from '../settings.service';

@Injectable()
export class DbmPollingService {

  private interval = 500;
  private useComputedMessage = true;

  private subscriptions: Map<string, Subscription> = new Map<string, Subscription>();

  private computedMessages: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();
  private values: Map<string, Map<string, any>> = new Map<string, Map<string, any>>();

  readonly c = DbmPollingService.name;

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

    const service: string = DbmPollingService.name;
    this.interval = this.settingsService.getSetting(this.c, f, service, DbmPollingSettings.STR_INTERVAL);
    this.useComputedMessage = this.settingsService.getSetting(this.c, f, service, DbmPollingSettings.STR_USE_COMPUTED_MESSAGE);
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

  unsubscribe(card: Card) {
    const f = 'unsubscribe';
    console.log(this.c, f);
    if ( this.subscriptions.has(card.name) ) {
      this.subscriptions.get(card.name).unsubscribe();
      this.subscriptions.delete(card.name);
    }
  }

  subscribe(card: Card) {
    const f = 'subscribe';
    console.log(this.c, f);

    this.unsubscribe(card);

    if ( this.useComputedMessage ) {
      this.readComputedMessage(card);
    } else {
      this.readValue(card);
    }

    console.log(this.c, f, 'interval', this.interval, 'card.name', card.name);

    this.subscriptions.set(
      card.name
      , Observable.interval(this.interval).map((x) => {
        console.log(this.c, f, 'interval map', 'card.name');

      }).subscribe((x) => {
        console.log(this.c, f, 'interval subscribe', 'card.name', card.name);

        if ( this.useComputedMessage ) {
          this.readComputedMessage(card);
        } else {
          this.readValue(card);
        }
      })
    );
  }

  readValue(card: Card) {
    const f = 'readValue';
    console.log(this.c, f);

    card.steps.forEach( item => {
      const urls: string [] = [];
      urls.push(DbmSettings.STR_ALIAS + item.equipment.univname + DbmSettings.STR_ATTR_VALUE);

      if ( DbmSettings.INT_DCI_TYPE === item.equipment.classId ) {
        urls.push(DbmSettings.STR_ALIAS + item.equipment.univname + DbmSettings.STR_VALUETABLE_LABEL);
        urls.push(DbmSettings.STR_ALIAS + item.equipment.univname + DbmSettings.STR_VALUETABLE_VALUE);
      } else {
        urls.push(DbmSettings.STR_ALIAS + item.equipment.univname + DbmSettings.STR_ATTR_UNIT);
      }

      const url = item.equipment.connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

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
              console.log(this.c, f, 'card.name', card.name, 'dbvalue', dbvalue);

              this.values.set(item.equipment.connAddr, new Map<string, any>().set(item.equipment.univname, dbvalue));

              let changed = false;
              card.steps.forEach( item2 => {
                if ( item2.equipment.univname === item.equipment.univname ) {
                  if ( item2.equipment.reallabel != dbvalue[0] ) {
                    if ( DbmSettings.INT_DCI_TYPE === item2.equipment.classId ) {
                      const labels: string[] = dbvalue[1] as string[];
                      const values: number[] = dbvalue[2] as number[];
                      for ( let i = 0 ; i < values.length ; ++i ) {
                        if ( dbvalue[0] === values[i]) {
                          item2.equipment.reallabel = labels[i];
                          break;
                        }
                      }
                    } else {
                      item2.equipment.reallabel = dbvalue[0] + dbvalue[1];
                    }
                    changed = true;
                  }
                }
              });

              if ( changed ) {
                console.log(this.c, f, 'card.name', card.name, 'changed');
                this.dbmPollingChanged(card.name);
              }
            }
            , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
            , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
        );
    });
  }

  readComputedMessage(card: Card) {
    const f = 'readComputedMessage';
    console.log(this.c, f);

    card.steps.forEach( item => {
      const urls: string [] = [];
      urls.push(DbmSettings.STR_ALIAS + item.equipment.univname + DbmSettings.STR_ATTR_COMPUTED_MESSAGE);

      const url = item.equipment.connAddr + DbmSettings.STR_URL_MULTIREAD + JSON.stringify(urls);

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
              console.log(this.c, f, 'card.name', card.name, 'dbvalue', dbvalue);

              this.computedMessages.set(item.equipment.connAddr, new Map<string, any>().set(item.equipment.univname, dbvalue));

              let changed = false;
              card.steps.forEach( item2 => {
                if ( item2.equipment.univname === item.equipment.univname ) {
                  if ( item2.equipment.reallabel != dbvalue[0] ) {
                    item2.equipment.reallabel = dbvalue[0];
                    changed = true;
                  }
                }
              });

              if ( changed ) {
                console.log(this.c, f, 'card.name', card.name, 'changed');
                this.dbmPollingChanged(card.name);
              }
            }
            , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
            , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
        );
    });
  }
}
