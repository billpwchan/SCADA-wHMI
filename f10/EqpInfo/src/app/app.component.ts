import { Component, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { NgActiveTextSettings, NgActiveEqpPoint } from './component/ng-active-text/ng-active-text-settings';
import { NgActiveTextCfg, NgActiveEqpPointCfg, NgActiveEqpPointGui } from './component/ng-active-text/ng-active-text-settings';
import { NgActiveTextDbmCfg, NgActiveTextClassCfg } from './component/ng-active-text/ng-active-text-settings';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from './service/settings.service';
import { Cookie } from 'ng2-cookies';
import { I18nSettings } from './service/i18n-settings';
// tslint:disable-next-line:max-line-length
import { NgActiveBackdropCfg, NgActiveBackdropDbmCfg, NgActiveBackdropClassCfg, NgActiveBackdropSettings } from './component/ng-active-backdrop/ng-active-backdrop-settings';
import { DbmUtils } from './service/scadagen/dbm/common/util';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy, OnChanges {

  readonly c = 'AppComponent';

  title = 'app';

  dbmInfo: Map<string, string>;
  readonly DBM_ENV = 'ENV';
  readonly DBM_ALIAS = 'M100';

  points: string[];

  eqpMap = new Map<string, NgActiveEqpPoint>();

  eqpPoints: NgActiveEqpPoint[];

  eqpLblCfg: NgActiveTextCfg;
  eqpLblText: string;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    console.log(this.c, f, 'translate.getBrowserCultureLang()[' + translate.getBrowserCultureLang() + ']');
    console.log(this.c, f, 'translate.getBrowserLang()[' + translate.getBrowserLang() + ']');
    console.log(this.c, f, 'translate.getDefaultLang()[' + translate.getDefaultLang() + ']');
    console.log(this.c, f, 'translate.getLangs()[' + translate.getLangs() + ']');

    const setting = this.settingsService.getSettings();
    const i18n = setting[I18nSettings.STR_I18N];
    const defaultLanguage = i18n[I18nSettings.STR_DEFAULT_LANG];
    const preferedLanguage = this.getPreferedLanguage();
    console.log(this.c, f,
                          '[Language]',
                          'Default:', defaultLanguage,
                          'Prefered:', preferedLanguage
    );
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang(defaultLanguage);
    if (preferedLanguage) {
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        translate.use(preferedLanguage);
        console.log(this.c, f, 'use preferred language ', preferedLanguage);
    }

    this.loadSettings();
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.dbmInfo  = this.getDbmInfo();
    this.points   = this.getDbmPoints();

    this.eqpLblCfg = this.createEqpLabel(this.dbmInfo.get(this.DBM_ENV), this.dbmInfo.get(this.DBM_ALIAS));
    // this.eqpLblText = this.alias;

    const dbmUtil: DbmUtils = new DbmUtils();
    for (let i = 0 ; i < this.points.length; ++i ) {
      const eqpPoint: NgActiveEqpPoint = new NgActiveEqpPoint();
      eqpPoint.cfg = this.createPoint(i, this.dbmInfo.get(this.DBM_ENV), this.dbmInfo.get(this.DBM_ALIAS), this.points[i]);
      eqpPoint.gui = new NgActiveEqpPointGui();

      const alias = dbmUtil.joinDbmAlias(
                                        this.dbmInfo.get(this.DBM_ALIAS)
                                        , this.points[i]);
      console.log(this.c, f, 'alias', alias);
      this.eqpMap.set(alias, eqpPoint);
    }

    this.eqpPoints = new Array();
    this.eqpMap.forEach((value: NgActiveEqpPoint, key: string) => {
      this.eqpPoints.push(value);
    });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    if ( changes[NgActiveTextSettings.STR_NOTIFY_PARENT] ) {
      this.onChildChange(changes[NgActiveTextSettings.STR_NOTIFY_PARENT].currentValue);
    } else if ( changes[NgActiveBackdropSettings.STR_NOTIFY_PARENT] ) {
      this.onChildChange(changes[NgActiveBackdropSettings.STR_NOTIFY_PARENT].currentValue);
    }
  }

  onChildChange(change: any): void {
    const f = 'onChildChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);
  }

  getNotification(child: string, event: any): void {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'child', child, 'event', event);
    const result = event[0];
    console.log(this.c, f, 'child', child, 'result', result);

    if (child === 'Status') {
      this.updateStatus(result.env, result.alias, result);
    }
  }

  private getPreferedLanguage(): string {
    const f = 'getPreferedLanguage';
    const setting = this.settingsService.getSettings();
    const i18n = setting[I18nSettings.STR_I18N];
    if (i18n[I18nSettings.STR_RESOLVE_BY_BROWSER_LANG]) {
        console.log(this.c, f, 'Resolve prefered language by browser\'s language');
        let browserLanguage = this.translate.getBrowserCultureLang();
        if (!i18n[I18nSettings.STR_USE_CULTURE_LANG]) {
            browserLanguage = this.translate.getBrowserLang();
        }
        return browserLanguage;
    } else if (i18n[I18nSettings.STR_RESOLVE_BY_BROWSER_COOKIE]) {
        const cookieName = i18n[I18nSettings.STR_USE_COOKIE_NAME];
        console.log(this.c, f, 'Resolve prefered language by browser\'s cookie:', cookieName);
        return Cookie.get(cookieName);
    } else {
        console.warn(this.c, f, 'No defined way to obtain prefered language');
    }
    return undefined;
  }

  private loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    // this.title = this.settingsService.getSetting(this.c, f, this.c, AppSettings.STR_TITLE);
  }

  private getDbmInfo(): Map<string, string> {
    const env = 'M100';
    const alias = ':TUN:M000001:ECT:TTVF0001';
    const dbmInfo = new Map<string, string>();
    dbmInfo.set(this.DBM_ENV, env);
    dbmInfo.set(this.DBM_ALIAS, alias);
    return dbmInfo;
  }

  private getDbmPoints(): string[] {
    const points: string[] = [];
    points.push(
      'dciECT-LRSTA', 'dciECT-NISERV', 'dciECT-PTW', 'dciECT-STATUS'
    , 'dciECT-STATUS01', 'dciECT-STATUS02', 'dciECT-STATUS03', 'dciECT-STATUS04', 'dciECT-STATUS05'
    , 'dciECT-STATUS06', 'dciECT-STATUS07', 'dciECT-STATUS08', 'dciECT-STATUS09', 'dciECT-STATUS10');
    return points;
  }

  private createEqpLabel(env: string, alias: string): NgActiveTextCfg {
    const attributes = ['.label'];

    const cfg: NgActiveTextCfg = new NgActiveTextCfg();

    cfg.class = new NgActiveTextClassCfg();
    cfg.class.span = null;

    cfg.dbm = new NgActiveTextDbmCfg();
    cfg.dbm.env = env;
    cfg.dbm.alias = alias;
    cfg.dbm.attributes = new Array();
    attributes.forEach(attribute => {
      cfg.dbm.attributes.push(attribute);
    });

    return cfg;
  }

  private createPoint(index: number, env: string, alias: string, point: string): NgActiveEqpPointCfg {
    const f = 'createPoint';
    console.log(this.c, f);

    const aliasPoint = alias + ':' + point;

    const cfg: NgActiveEqpPointCfg = new NgActiveEqpPointCfg();
    cfg.labelCfg  = this.createEqpPointLabel(index, env, alias, point);
    cfg.valueCfg  = this.createEqpPointValue(index, env, alias, point);
    cfg.statusCfg = this.createEqpPointStatus(index, env, alias, point);

    return cfg;
  }

  private createEqpPointLabel(index: number, env: string, alias: string, point: string): NgActiveTextCfg {
    const f = 'createEqpPointLabel';
    console.log(this.c, f);

    const attributes = ['.label'];

    const cfg: NgActiveTextCfg = new NgActiveTextCfg();

    cfg.class = new NgActiveTextClassCfg();
    cfg.class.span = null;

    cfg.dbm = new NgActiveTextDbmCfg();
    cfg.dbm.env = env;
    cfg.dbm.alias = new DbmUtils().joinDbmAlias(alias, point);
    cfg.dbm.attributes = new Array();
    attributes.forEach(attribute => {
      cfg.dbm.attributes.push(attribute);
    });

    return cfg;
  }

  private createEqpPointValue(index: number, env: string, alias: string, point: string): NgActiveTextCfg {
    const f = 'createEqpPointValue';
    console.log(this.c, f);

    const attributes = ['.computedMessage'];

    const cfg: NgActiveTextCfg = new NgActiveTextCfg();

    cfg.class = new NgActiveTextClassCfg();
    cfg.class.span = null;

    cfg.dbm = new NgActiveTextDbmCfg();
    cfg.dbm.env = env;
    cfg.dbm.alias = new DbmUtils().joinDbmAlias(alias, point);
    cfg.dbm.attributes = new Array();

    attributes.forEach(attribute => {
      cfg.dbm.attributes.push(attribute);
    });

    return cfg;
  }

  private createEqpPointStatus(index: number, env: string, alias: string, point: string): NgActiveBackdropCfg {
    const f = 'createEqpPointStatus';
    console.log(this.c, f);

    const attributes = ['.status', ':dfo.forcedStatus', ':dal.valueAlarmVector(1)'];

    const cfg = new NgActiveBackdropCfg();

    cfg.class = new NgActiveBackdropClassCfg();
    cfg.class.span = null;

    cfg.dbm = new NgActiveBackdropDbmCfg();
    cfg.dbm.env = env;
    cfg.dbm.alias = new DbmUtils().joinDbmAlias(alias, point);
    cfg.dbm.attributes = new Array();
    attributes.forEach(attribute => {
      cfg.dbm.attributes.push(attribute);
    });
    return cfg;
   }


  updateStatus(env: string, alias: string, result: any) {
    const f = 'updateStatus';
    console.log(this.c, f, 'env', env, 'alias', alias, 'result', result);

    const css0 = 'badge';
    const css1 = 'statusBackdrop-';
    // css += '.statusBackdrop-';

    const attribute0 = '.status';
    const dbAddr0: string = alias + attribute0;
    const backdrop0: string = result[dbAddr0];
    console.log(this.c, f, 'dbAddr0', dbAddr0, 'backdrop0', backdrop0);

    const attribute1 = ':dfo.forcedStatus';
    const dbAddr1: string = alias + attribute1;
    const backdrop1: string = result[dbAddr1];
    console.log(this.c, f, 'dbAddr1', dbAddr1, 'backdrop1', backdrop1);

    const attribute2 = ':dal.valueAlarmVector(1)';
    const dbAddr2: string = alias + attribute2;
    const backdrop2: string = result[dbAddr2];
    console.log(this.c, f, 'dbAddr2', dbAddr2, 'backdrop2', backdrop2);

    if (null != backdrop0 && null != backdrop1 && null != backdrop2) {

      const status: number = Number.parseInt(backdrop0);
      const forcedStatus: number = Number.parseInt(backdrop1);
      const valueAlarmVector1: number = Number.parseInt(backdrop2);

      let label;
      // tslint:disable-next-line:no-bitwise
      if ((2 & forcedStatus) > 0) {
        label = 2;
      // tslint:disable-next-line:no-bitwise
      } else if ((8 & forcedStatus) > 0) {
        label = 8;
      // tslint:disable-next-line:no-bitwise
      } else if ((512 & forcedStatus) > 0) {
        label = 512;
      } else if (1024 <= status) {
        label = 1024;
      } else if (1 === valueAlarmVector1) {
        label = 0;
      } else {
        label = 1;
      }
      console.log(this.c, f, 'alias', alias, 'label', label);

      const eqp: NgActiveEqpPoint = this.eqpMap.get(alias);
      const gui: NgActiveEqpPointGui = eqp.gui;
      gui.statusText = label;
      // gui.statusBackdrop = css + label;
      gui.statusBackdrop = css0 + ' ' + css1 + label;
    }
  }

}
