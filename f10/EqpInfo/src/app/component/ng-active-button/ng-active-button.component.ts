import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
// tslint:disable-next-line:max-line-length
import { NgActiveButtonCfg, NgActiveButtonSettings, NgActiveButtonClassCfg, NgActiveButtonDbmCfg, NgActiveButtonUpdate } from './ng-active-button-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { SettingsService } from '../../service/settings.service';
import { DbmMultiReadAttrService } from '../../service/scadagen/dbm/dbm-multi-read-attr.service';
import { DbmPollingService } from '../../service/scadagen/dbm/polling/dbm-polling.service';
import { Subscription } from 'rxjs/Subscription';
import { DbmPolling } from '../../service/scadagen/dbm/polling/dbm-polling';
import { HttpClient } from '@angular/common/http';
import { DbmPollingSettings, DbmPollingCfg } from '../../service/scadagen/dbm/polling/dbm-polling-settings';
import { UtilsHttpModule } from '../../service/scadagen/common/utils-http.module';
import { EnvironmentMappingService } from '../../service/scadagen/envs/environment-mapping.service';

@Component({
  selector: 'app-ng-active-button',
  templateUrl: './ng-active-button.component.html',
  styleUrls: ['./ng-active-button.component.css']
})
export class NgActiveButtonComponent implements OnInit, OnDestroy {

  readonly c = 'NgActiveButtonComponent';

  buttonText: string;
  buttonClass: string;

  private cfg: NgActiveButtonCfg;

  private dbmPolling: DbmPolling;

  @Input()
  set setCfg(cfg: NgActiveButtonCfg) {
    const f = 'setCfg';
    console.log(this.c, f);

    this.cfg = cfg;

    if (null != this.cfg) {
      if (null != this.cfg.class) { this.buttonClass = this.cfg.class.button; }
      if (null != this.cfg.dbm) {
        const dbAddr: string[] = new Array();
        this.cfg.dbm.attributes.forEach(point => {
          dbAddr.push(this.cfg.dbm.alias + point);
        });

        const key = dbAddr.join().trim();

        // // Dbm Polling Service
        // this.dbmPollingService.subscribe(key, this.cfg.dbm.env, dbAddr);

        // Dbm Polling
        this.dbmPolling.subscribe(key, this.cfg.dbm.env, dbAddr);
      }
    }

  }
  @Input()
  set setText(str: string) {
    const f = 'setText';
    console.log(this.c, f);
    console.log(this.c, f, 'str', str);
    if (null != str) { this.buttonText = str; }
  }

  @Output() notifyParent: EventEmitter<any> = new EventEmitter();

  // dbmPollingServiceSubscription: Subscription;

  dbmPollingSubscription: Subscription;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
    // , private dbmMultiReadAttrService: DbmMultiReadAttrService
    // , private dbmPollingService: DbmPollingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });

    this.dbmPolling = new DbmPolling(httpClient, utilsHttp, environmentMappingService);
    const dbmPollingCfg = new DbmPollingCfg();
    dbmPollingCfg.interval = 4000;
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

      const alias = this.cfg.dbm.alias + this.cfg.dbm.attributes;
      const value = result[alias];
      console.log(this.c, f, alias, value);
      this.setText = this.translate.instant(value);

      result['env'] = this.cfg.dbm.env;
      result['alias'] = this.cfg.dbm.alias;
      result['point'] = this.cfg.dbm.attributes;
      this.notifyParent.emit([result]);
    }
  }

  onClick() {
    const f = 'onClick';
    console.log(this.c, f);
  }

}
