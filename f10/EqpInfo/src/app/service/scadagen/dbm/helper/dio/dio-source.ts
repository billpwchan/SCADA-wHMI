import { Subscription } from 'rxjs/Subscription';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DioGui } from './dio-gui';
import { TranslateService } from '@ngx-translate/core';
import { DioSourceUtil } from './dio-source-util';
import { DbmRead } from '../../simple/read/dbm-read';
import { DbmPolling } from '../../simple/polling/dbm-polling';
import { NgActiveButtonDbmCfg } from '../../../../../component/ng-active-button/ng-active-button-settings';
import { UtilsHttpModule } from '../../../common/utils-http.module';
import { EnvironmentMappingService } from '../../../envs/environment-mapping.service';
import { DbmPollingCfg } from '../../simple/polling/dbm-polling-settings';

export class DioSource {

  readonly c = 'DioSource';

  // Observable source
  private dioSource = new BehaviorSubject<any>(null);

  // Observable cardItem stream
  dioItem = this.dioSource.asObservable();

  private dbmRead: DbmRead;
  private dbmPolling: DbmPolling;

  dbmReadSubscription: Subscription;
  dbmPollingSubscription: Subscription;

  private dbmCfg: NgActiveButtonDbmCfg;

  // Caches
  private staticData;
  private dynamicData;

  private sourceUtil: DioSourceUtil;

  // Service command
  dioSourceChanged(result: any) {
    const f = 'dioSourceChanged';
    console.log(this.c, f);
    this.dioSource.next(result);
  }

  constructor(
    private translate: TranslateService
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
    const f = 'constructor';

    this.sourceUtil = new DioSourceUtil();

    this.dbmRead = new DbmRead(httpClient, utilsHttp, environmentMappingService);

    this.dbmPolling = new DbmPolling(httpClient, utilsHttp, environmentMappingService);
    const dbmPollingCfg = new DbmPollingCfg();
    dbmPollingCfg.interval = 2000;
    this.dbmPolling.setSettings(dbmPollingCfg);

    this.dbmReadSubscription = this.dbmRead.dbmReadItem
      .subscribe(result => {
        console.log(this.c, f, 'dbmReadSubscription', result);
        this.onDbmReadUpdate(result);
    });

    this.dbmPollingSubscription = this.dbmPolling.dbmPollingItem
      .subscribe(result => {
        console.log(this.c, f, 'dbmPollingSubscription', result);
        this.onDbmPollingUpdate(result);
    });
  }

  onDbmReadUpdate(result: any): void {
    const f = 'onDbmReadUpdate';
    console.log(this.c, f);
    if ( null != result ) {
console.log(this.c, f, 'result', result);
      const attributes = this.convertDioPointLvAttribute(result, this.dbmCfg);
console.log(this.c, f, 'attributes', attributes);
      const valueTable = this.convertDioPointLvValueTable(result, this.dbmCfg);
console.log(this.c, f, 'valueTable', valueTable);
      const addr = this.convertDioPointLvAddress(valueTable);
      this.connectDioInternalLv(addr);

      const data = [];
      data['source'] = 'DbmRead';
      data['result'] = result;
      data['valueTable'] = valueTable;
      data['attributes'] = attributes;
      this.setStaticData(result);
      this.dioSourceChanged(data);
    }
  }

  onDbmPollingUpdate(result: any): void {
    const f = 'onDbmPollingUpdate';
    console.log(this.c, f);
    if ( null != result ) {
      console.log(this.c, f, 'result', result);
      const data = [];
      data['source'] = 'DbmPolling';
      data['result'] = result;
      this.setDynamicData(result);
      this.dioSourceChanged(data);
    }
  }

  disconnect() {
    const f = 'disconnect';

    this.dbmPolling.unsubscribe();

    this.dbmReadSubscription.unsubscribe();

    this.dioSource.unsubscribe();
  }

  connect(dbmCfg: NgActiveButtonDbmCfg) {
    const f = 'connect';
    console.log(this.c, f);

    this.readDio(dbmCfg);
  }

  readDio(dbmCfg) {
    const f = 'readDio';
    console.log(this.c, f);

    this.dbmCfg = dbmCfg;

    const dbAddr: string[] = new Array();
    const attributes1 = [];
    const attributes2 = [
        '.label'
      , '.computedMessage'
      , '.valueTable(0:$,dovname)'
      , '.valueTable(0:$,label)'
      , '.valueTable(0:$,value)'
    ];
    const point = dbmCfg.attributes['point'];
    attributes2.forEach(element => {
      const attribute = dbmCfg.attributes[element];
      const alias = dbmCfg.alias + point + attribute;
console.log(this.c, f, 'alias', alias);
      dbAddr.push(alias);
    });

    const key = dbAddr.join().trim();

    // Dbm Read
    this.dbmRead.readValue(key, dbmCfg.env, dbAddr);
  }

  convertDioPointLvAttribute(result: any, dbm): any {
    const f = 'convertDioPointLvAttribute';
    const ret = {};

    const label = this.sourceUtil.getPointLvAttributeValue(result, dbm, '.label');
    const computedMessage = this.sourceUtil.getPointLvAttributeValue(result, dbm, '.computedMessage');

    ret['label'] = label;
    ret['computedMessage'] = computedMessage;

    return ret;
  }

  convertDioPointLvValueTable(result: any, dbm: NgActiveButtonDbmCfg): any {
    const f = 'convertDioPointLvValueTable';

    const valueTable = [];
    valueTable['name']  = this.getPointLvValueTableColumn(result, dbm, '.valueTable(0:$,dovname)');
    valueTable['label'] = this.getPointLvValueTableColumn(result, dbm, '.valueTable(0:$,label)');
    valueTable['value'] = this.getPointLvValueTableColumn(result, dbm, '.valueTable(0:$,value)');

    return valueTable;
  }

  getPointLvValueTableColumn(result, dbm, attribute): any {
    const f = 'getPointLvValueTableColumn';
    console.log(this.c, f);
    const ret: string[] = [];
    const values = this.sourceUtil.getPointLvAttributeValue(result, dbm, attribute);
    console.log(this.c, f, 'values', values);
    values.forEach(element => {
        ret.push(element);
    });
    return ret;
  }

  getDioPointLvAddress(selectValues): any {
    const f = 'getDioPointLvAddress';
    console.log(this.c, f);
    const ret = {};
    const point = this.dbmCfg.attributes['point'];

console.log(this.c, f, 'selectValues', selectValues);
    const attributes1 = ['.execStatus'];
    attributes1.forEach(element => {
      const alias = this.dbmCfg.alias + ':' + point + element;
      console.log(this.c, f, 'alias', alias);
      ret[element] = alias;
    });
    return ret;
  }

  getDioInternalLvAddress(selectValues): any {
    const f = 'getDioInternalLvAddress';
    console.log(this.c, f);
    const ret = {};
    const point = this.dbmCfg.attributes['point'];

console.log(this.c, f, 'selectValues', selectValues);
    const attributes2 = ['.initCondGL'];

    const aliases = [];
    attributes2.forEach(element => {
      const attribute = this.dbmCfg.attributes[element];

      const names = selectValues['name'];
      for (let i = 0; names.length > i; ++i) {
        const name = names[i];
        console.log(this.c, f, 'name', name);

        if (null != name && name.length > 0) {
          const alias = this.dbmCfg.alias + ':' + point + ':' + name + attribute;
          console.log(this.c, f, 'alias', alias);
          aliases.push(alias);
        }
      }
      ret[element] = aliases;
    });
console.log(this.c, f, 'ret', ret);
    return ret;
  }

  convertDioPointLvAddress(selectValues): string[] {
    const f = 'convertDioPointLvAddress';
    console.log(this.c, f);
    const addr = [];
    const pointLvAddress = this.getDioPointLvAddress(selectValues);
    {
      const keys = Object.keys(pointLvAddress);
      keys.forEach(element => {
        const value = pointLvAddress[element];
        if (Array.isArray(value)) {
          value.forEach(element2 => {
            addr.push(element2);
          });
        } else {
          addr.push(value);
        }
      });
    }
    {
      const internalLvAddress = this.getDioInternalLvAddress(selectValues);
      const keys = Object.keys(internalLvAddress);
      keys.forEach(element => {
        const value = internalLvAddress[element];
        if (Array.isArray(value)) {
          value.forEach(element2 => {
            addr.push(element2);
          });
        } else {
          addr.push(value);
        }
      });
    }
    console.log(this.c, f, 'addr', addr);
    return addr;
  }

  connectDioInternalLv(dbAddr) {
    const f = 'connectDioInternalLv';
    console.log(this.c, f);
console.log(this.c, f, 'dbAddr', dbAddr);
    const key = dbAddr.join().trim();
    // Dbm Read
    this.dbmPolling.subscribe(key, this.dbmCfg.env, dbAddr);
  }

  getDioPointLvValue() {
    const f = 'getDioPointLvValue';
    console.log(this.c, f);
    const result = this.getStaticData();
    const valueTable = this.convertDioPointLvValueTable(result, this.dbmCfg);
    console.log(this.c, f, 'valueTable', valueTable);
    const addr = this.getDioPointLvAddress(valueTable);
    return addr;
  }

  getDioInternalLvValue() {
    const f = 'getDioInternalLvValue';
    console.log(this.c, f);
    const result = this.getStaticData();
    const valueTable = this.convertDioPointLvValueTable(result, this.dbmCfg);
    console.log(this.c, f, 'valueTable', valueTable);
    const addr = this.getDioInternalLvAddress(valueTable);
    return addr;
  }

  setStaticData(staticData): void { this.staticData = staticData; }
  getStaticData(): any { return this.staticData; }
  setDynamicData(dynamicData): any { this.dynamicData = dynamicData; }
  getDynamicData(): any { return this.dynamicData; }
}
