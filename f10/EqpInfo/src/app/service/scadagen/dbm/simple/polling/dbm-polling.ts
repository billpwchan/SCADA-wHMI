import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient } from '@angular/common/http';
import { DbmPollingServiceType, DbmPollingCfg } from './dbm-polling-settings';
import { UtilsHttpModule } from '../../../common/utils-http.module';
import { EnvironmentMappingService } from '../../../envs/environment-mapping.service';
import { DbmRead } from '../read/dbm-read';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/map';

export class DbmPolling {

  private interval: number;

  private subscriptions: Map<string, Subscription> = new Map<string, Subscription>();

  readonly c = 'DbmPolling';

  // Observable source
  private dbmPollingSource = new BehaviorSubject<string>(null);

  // Observable dbmPollingItem stream
  dbmPollingItem = this.dbmPollingSource.asObservable();

  private dbmRead: DbmRead = null;
  private dbmReadSubscription: Subscription;

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
    this.dbmRead = new DbmRead(httpClient, utilsHttp, environmentMappingService);

    this.dbmReadSubscription = this.dbmRead.dbmReadItem
    .subscribe( res => {
        const f2 = 'dbmReadSubscription';
        console.log(this.c, f2);
        if ( null != res ) {
          this.dbmPollingChanged(res);
        }
      });
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

  unsubscribe(key?: string): void {
    const f = 'unsubscribe';
    console.log(this.c, f);
    if (null != key) {
      if ( this.subscriptions.has(key) ) {
        this.subscriptions.get(key).unsubscribe();
        this.subscriptions.delete(key);
      }
    } else {
      this.subscriptions.forEach(element => {
        element.unsubscribe();
      });
      this.subscriptions.clear();
    }
  }

  subscribe(key: string, env: string, univnames: string[]): void {
    const f = 'subscribe';
    console.log(this.c, f);

    this.unsubscribe();

    this.dbmRead.readValue(key, env, univnames);

    console.log(this.c, f, 'interval', this.interval, 'env', env);

    this.subscriptions.set(
      env
      , Observable.interval(this.interval).map((x) => {
        console.log(this.c, f, 'interval map', 'env', env);
      }).subscribe((x) => {
        console.log(this.c, f, 'interval subscribe', 'env', env);
        this.dbmRead.readValue(key, env, univnames);
      })
    );

  }
}
