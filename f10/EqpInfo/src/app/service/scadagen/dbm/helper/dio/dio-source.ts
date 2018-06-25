import { Subscription } from 'rxjs/Subscription';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
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
  private source = new BehaviorSubject<any>(null);

  // Observable cardItem stream
  item = this.source.asObservable();

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
  sourceChanged(result: any) {
    const f = 'sourceChanged';
    console.log(this.c, f);
    this.source.next(result);
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
      const attributes = this.convertPointLvAttribute(result, this.dbmCfg);
console.log(this.c, f, 'attributes', attributes);
      const valueTable = this.convertPointLvValueTable(result, this.dbmCfg);
console.log(this.c, f, 'valueTable', valueTable);
      const addr = this.convertPointLvAddress(valueTable);
      this.connectInternalLv(addr);

      const data = [];
      data['source'] = 'DbmRead';
      data['result'] = result;
      data['valueTable'] = valueTable;
      data['attributes'] = attributes;
      this.setStaticData(result);
      this.sourceChanged(data);
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
      this.sourceChanged(data);
    }
  }

  disconnect() {
    const f = 'disconnect';

    this.dbmPolling.unsubscribe();

    this.dbmReadSubscription.unsubscribe();

    this.source.unsubscribe();
  }

  connect(dbmCfg: NgActiveButtonDbmCfg) {
    const f = 'connect';
    console.log(this.c, f);

    this.read(dbmCfg);
  }

  read(dbmCfg) {
    const f = 'read';
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

  convertPointLvAttribute(result: any, dbm): any {
    const f = 'convertPointLvAttribute';
    const ret = {};

    const label = this.sourceUtil.getPointLvAttributeValue(result, dbm, '.label');
    const computedMessage = this.sourceUtil.getPointLvAttributeValue(result, dbm, '.computedMessage');

    ret['label'] = label;
    ret['computedMessage'] = computedMessage;

    return ret;
  }

  convertPointLvValueTable(result: any, dbm: NgActiveButtonDbmCfg): any {
    const f = 'convertPointLvValueTable';

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

  getPointLvAddress(selectValues): any {
    const f = 'getPointLvAddress';
    console.log(this.c, f);
    const ret = {};
    const point = this.dbmCfg.attributes['point'];

console.log(this.c, f, 'selectValues', selectValues);
    const attributes1 = ['.execStatus'];
    attributes1.forEach(element => {
      const alias = this.dbmCfg.alias + point + element;
      console.log(this.c, f, 'alias', alias);
      ret[element] = alias;
    });
    return ret;
  }

  getInternalLvAddress(selectValues): any {
    const f = 'getInternalLvAddress';
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

  convertPointLvAddress(selectValues): string[] {
    const f = 'convertPointLvAddress';
    console.log(this.c, f);
    const addr = [];
    const pointLvAddress = this.getPointLvAddress(selectValues);
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
      const internalLvAddress = this.getInternalLvAddress(selectValues);
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

  connectInternalLv(dbAddr) {
    const f = 'connectInternalLv';
    console.log(this.c, f);
console.log(this.c, f, 'dbAddr', dbAddr);
    const key = dbAddr.join().trim();
    // Dbm Read
    this.dbmPolling.subscribe(key, this.dbmCfg.env, dbAddr);
  }

  getPointLvValue() {
    const f = 'getPointLvValue';
    console.log(this.c, f);
    const result = this.getStaticData();
    const valueTable = this.convertPointLvValueTable(result, this.dbmCfg);
    console.log(this.c, f, 'valueTable', valueTable);
    const addr = this.getPointLvAddress(valueTable);
    return addr;
  }

  getInternalLvValue() {
    const f = 'getInternalLvValue';
    console.log(this.c, f);
    const result = this.getStaticData();
    const valueTable = this.convertPointLvValueTable(result, this.dbmCfg);
    console.log(this.c, f, 'valueTable', valueTable);
    const addr = this.getInternalLvAddress(valueTable);
    return addr;
  }

  setStaticData(staticData): void { this.staticData = staticData; }
  getStaticData(): any { return this.staticData; }
  setDynamicData(dynamicData): any { this.dynamicData = dynamicData; }
  getDynamicData(): any { return this.dynamicData; }
}
