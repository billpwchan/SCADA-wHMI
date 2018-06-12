import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
// tslint:disable-next-line:max-line-length
import { NgActiveNumberCfg, NgActiveNumberSettings, NgActiveNumberClassCfg, NgActiveNumberDbmCfg, NgActiveNumberUpdate } from './ng-active-number-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { SettingsService } from '../../service/settings.service';
import { DbmMultiReadAttrService } from '../../service/scadagen/dbm/dbm-multi-read-attr.service';
import { DbmPollingService } from '../../service/scadagen/dbm/polling/dbm-polling.service';
import { Subscription } from 'rxjs/Subscription';
import { DbmPolling } from '../../service/scadagen/dbm/polling/dbm-polling';
import { HttpClient } from '@angular/common/http';
import { DbmPollingSettings, DbmPollingCfg } from '../../service/scadagen/dbm/polling/dbm-polling-settings';
import { UtilsHttpModule } from '../../service/scadagen/common/utils-http.module';

@Component({
  selector: 'app-ng-active-number',
  templateUrl: './ng-active-number.component.html',
  styleUrls: ['./ng-active-number.component.css']
})
export class NgActiveNumberComponent implements OnInit, OnDestroy {

  readonly c = 'NgActiveNumberComponent';

  spanNumber: string;
  spanClass: string;

  private cfg: NgActiveNumberCfg;

  private dbmPolling: DbmPolling;

  @Input()
  set setCfg(cfg: NgActiveNumberCfg) {
    const f = 'setCfg';
    console.log(this.c, f);

    this.cfg = cfg;

    if (null != this.cfg) {
      if (null != this.cfg.class) { this.spanClass = this.cfg.class.span; }
      if (null != this.cfg.dbm) {
        const dbAddr: string[] = new Array();
        this.cfg.dbm.point.forEach(point => {
          dbAddr.push(this.cfg.dbm.alias + point);
        });

        const key = dbAddr.join().trim();

        // // Dbm Polling Service
        // this.dbmPollingService.subscribe(this.cfg.dbm.env, dbAddr);

        // Dbm Polling
        this.dbmPolling.subscribe(key, this.cfg.dbm.env, dbAddr);
      }
    }
  }
  @Input()
  set setNumber(str: string) {
    const f = 'setNumber';
    console.log(this.c, f);
    console.log(this.c, f, 'str', str);
    if (null != str) { this.spanNumber = str; }
  }

  @Output() notifyParent: EventEmitter<any> = new EventEmitter();

  // dbmPollingServiceSubscription: Subscription;

  dbmPollingSubscription: Subscription;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    // , private dbmMultiReadAttrService: DbmMultiReadAttrService
    // , private dbmPollingService: DbmPollingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });

    this.dbmPolling = new DbmPolling(httpClient, utilsHttp);
    const dbmPollingCfg = new DbmPollingCfg();
    dbmPollingCfg.interval = 4000;
    dbmPollingCfg.envs = {'M100': 'http://127.0.0.1:8990'};
    this.dbmPolling.setSettings(dbmPollingCfg);
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    // this.dbmPollingServiceSubscription = this.dbmPollingService.dbmPollingItem
    //   .subscribe(result => {
    //   console.log(this.c, f, 'dbmPollingServiceSubscription', result);
    //   this.onUpdate(result);
    // });

    this.dbmPollingSubscription = this.dbmPolling.dbmPollingItem
      .subscribe(result => {
      console.log(this.c, f, 'dbmPollingSubscription', result);
      this.onDbmPollingUpdate(result);
    });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);
  }

  onDbmPollingUpdate(result: any): void {
    const f = 'onUpdate';
    console.log(this.c, f);
    if ( null != result ) {

      const alias = this.cfg.dbm.alias + this.cfg.dbm.point;
      const value = result[alias];
      console.log(this.c, f, alias, value);
      if (null != this.cfg.format) { this.setNumber = this.format(value, this.cfg.format); }

      result['env'] = this.cfg.dbm.env;
      result['alias'] = this.cfg.dbm.alias;
      result['point'] = this.cfg.dbm.point;
      this.notifyParent.emit([result]);
    }
  }

  format(str: string, ...replacements: string[]): string {
    const args = arguments;
    return str.replace(/{(\d+)}/g, function(match, number) {
      return typeof args[number] !== 'undefined'
        ? args[number]
        : match
      ;
    });
  }

}
