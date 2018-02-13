import { Component, OnInit, OnDestroy, OnChanges, SimpleChanges, Input, EventEmitter, Output, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { AlarmSummaryConfig, Env, AlarmSummarySettings } from './alarm-summary-settings';
import { AppSettings } from '../../../app-settings';
import { MatrixComponent } from '../Matrix/matrix.component';
import { DbmSettings } from '../../../service/scadagen/dbm/dbm-settings';
import { DbmMultiReadAttrService } from '../../../service/scadagen/dbm/dbm-multi-read-attr.service';
import { AlarmServerity } from '../../../service/scs/ava/dbm-ava-settings';
import { HttpAccessResultType, HttpAccessResult } from '../../../service/scadagen/access/http/Access-interface';
import { DbmMultiWriteAttrService } from '../../../service/scadagen/dbm/dbm-multi-write-attr.service';

@Component({
  selector: 'app-alarm-summary',
  templateUrl: './alarm-summary.component.html',
  styleUrls: ['./alarm-summary.component.css']
})
export class AlarmSummaryComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NOTIFY_FROM_PARENT = AppSettings.STR_NOTIFY_FROM_PARENT;

  readonly c = 'AlarmSummaryComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  private cfg: AlarmSummaryConfig;
  @Input() config: AlarmSummaryConfig;

  private env: string;
  private index: number;
  private data: Map<number, Map<number, number>>;

  @Input()
  set updateAlarmEnv(env: string) {
    const f = 'updateAlarmEnv';
    console.log(this.c, f);
    this.env = env;
  }

  private univnames: string[];
  get getUnivnames(): string[] {return this.univnames; }
  @Input()
  set updateAlarmUnivname(univnames: string[]) {
    const f = 'updateAlarmUnivname';
    console.log(this.c, f);
    this.univnames = univnames;
  }

  private preview: number[][];
  private updated: number[][];
  @Input()
  set updateAlarmSummary(index: number) {
    const f = 'updateAlarmSummary';
    console.log(this.c, f);
    this.index = index;
    if (null != this.index) {
      this.readAlarm(this.env, this.univnames, this.index);
    }
    this.disableButtons(true);
  }
  @Output() onUpdatedAlarmSummary = new EventEmitter<number[][]>();

  notifyMatrix: string;

  multiReadSubscription: Subscription;
  multiWriteSubscription: Subscription;

  btnApply: boolean;
  btnCancel: boolean;

  updateMatrix: any;

  private alarms: Map<string, Map<string, AlarmServerity>> = new Map<string, Map<string, AlarmServerity>>();

  constructor(
    private translate: TranslateService
    , private dbmMultiReadAttrService: DbmMultiReadAttrService
    , private dbmMultiWriteAttrService: DbmMultiWriteAttrService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.multiReadSubscription = this.dbmMultiReadAttrService.dbmItem
      .subscribe( res => {
        if ( null != res ) {
          if ( HttpAccessResultType.NEXT === res.method ) {
            if ( AlarmSummarySettings.STR_READ_ALARM === res.key ) {

              let envAlarms = this.alarms.get(res.connAddr);
              if ( null == envAlarms ) {
                this.alarms.set(res.connAddr, new Map<string, AlarmServerity>());
              }
              envAlarms = this.alarms.get(res.connAddr);

              const data: Map<number, Map<number, number>> = new Map<number, Map<number, number>>();

              for ( let i = 0, j = 0 ; i < res.dbAddresses.length / 3 ; ++i, j = i * 3 ) {

                const dbAddressLevel = res.dbAddresses[j];
                const alarmServerity = new AlarmServerity();
                alarmServerity.level = Number(res.dbValues[j++]);
                alarmServerity.geo = Number(res.dbValues[j++]);
                alarmServerity.func = Number(res.dbValues[j++]);

                let data1 = data.get(alarmServerity.geo);
                if ( null == data1 ) {
                  data.set(alarmServerity.geo, new Map<number, number>());
                  data1 = data.get(alarmServerity.geo);
                }
                data1.set(alarmServerity.func, alarmServerity.level);

                envAlarms.set(dbAddressLevel, alarmServerity);
              }

              this.updateMatrix = data;

              this.disableButtons(true);
            }
          }
        }
      });

    this.multiWriteSubscription = this.dbmMultiWriteAttrService.dbmItem
      .subscribe( (res: HttpAccessResult) => {
        console.log(this.c, f, 'dbmWriteAvaSupSubscription', res);

        if (null != res ) {
          if ( HttpAccessResultType.NEXT === res.method) {
            if ( AlarmSummarySettings.STR_WRITE_ALARM === res.key ) {
              this.disableButtons(true);
            }
          }

        } else {
          console.warn(this.c, f, 'env IS INVALID');
        }
      });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.multiReadSubscription.unsubscribe();
    this.multiWriteSubscription.unsubscribe();
  }

  onParentChange(change: string): void {
    const f = 'onParentChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    if (changes[AlarmSummaryComponent.STR_NOTIFY_FROM_PARENT]) {
      this.onParentChange(changes[AlarmSummaryComponent.STR_NOTIFY_FROM_PARENT].currentValue);
    }
    if (changes[AlarmSummarySettings.STR_CONFIG]) {
      this.cfg = changes[AlarmSummarySettings.STR_CONFIG].currentValue as AlarmSummaryConfig;
    }
  }

  getNotification(evt) {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);
  }

  sendNotifyParent(str: string): void {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);
  }

  sendNotifyOnUpdated(data: number[][]) {
    const f = 'sendNotifyOnUpdated';
    console.log(this.c, f);
    console.log(this.c, f, data);
    this.onUpdatedAlarmSummary.emit(data);
  }

  onUpdatedMatrix(data: Map<number, Map<number, number>>) {
    const f = 'onUpdatedMatrix';
    console.log(this.c, f);
    console.log(this.c, f, data);
    this.data = data;

    this.disableButtons(false);
  }

  writeAlarm(env: string, univnames: string[], index: number): void {
    const f = 'writeAlarm';
    console.log(this.c, f);

    const values = {};
    this.data.forEach ( (data1, key1: number) => {
      data1.forEach( (value2, key2: number) => {
        let found = false;
        for ( let i = 0 ; i < this.univnames.length ; ++i ) {
          if ( found ) {
            break;
          }
          const alias = univnames[i]
                        + DbmSettings.STR_ATTR_LEVEL
                        + DbmSettings.STR_OPEN_PARENTHESIS + index + DbmSettings.STR_CLOSE_PARENTHESIS;
          const alarmServerity: AlarmServerity = this.alarms.get(env).get(alias);
          if ( null != alarmServerity ) {
            if ( alarmServerity.geo === key1 && alarmServerity.func === key2 ) {
              values[alias] = value2;
              found = true;
            }
          }
        }
      });
    });

    this.dbmMultiWriteAttrService.write(env, values, AlarmSummarySettings.STR_WRITE_ALARM);
  }

  readAlarm(connAddr: string, univnames: string[], index: number): void {
    const f = 'readAlarm';
    console.log(this.c, f);
    console.log(this.c, f, connAddr, univnames, index);

    const dbaddress: string[] = new Array<string>();
    univnames.forEach( univname => {
      dbaddress.push(
        univname
        + DbmSettings.STR_ATTR_LEVEL
        + DbmSettings.STR_OPEN_PARENTHESIS + index + DbmSettings.STR_CLOSE_PARENTHESIS);
      dbaddress.push(univname + DbmSettings.STR_ATTR_GEO);
      dbaddress.push(univname + DbmSettings.STR_ATTR_FUNC);
    });

    this.dbmMultiReadAttrService.read(connAddr, dbaddress, AlarmSummarySettings.STR_READ_ALARM);
  }

  private disableButtons(disable: boolean) {
    const f = 'disableButtons';
    console.log(this.c, f);

    this.btnApply = disable;
    this.btnCancel = disable;
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.disableButtons(true);
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case AlarmSummaryComponent.STR_INIT: {
        this.init();
      } break;
      case 'apply': {
        if (null != this.index) {
          this.writeAlarm(this.env, this.univnames, this.index);
        } else {
          console.warn(this.c, f, 'index IS NULL');
        }
      } break;
      case 'cancel': {
        if (null != this.index) {
          this.readAlarm(this.env, this.univnames, this.index);
        } else {
          console.warn(this.c, f, 'index IS NULL');
        }
      } break;
    }
  }

}
