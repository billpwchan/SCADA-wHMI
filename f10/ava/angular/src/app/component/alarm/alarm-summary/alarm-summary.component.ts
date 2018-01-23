import { Component, OnInit, OnDestroy, OnChanges, SimpleChanges, Input, EventEmitter, Output, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { DbmReadAvaSupService } from '../../../service/scs/ava/dbm-read-ava-sup.service';
import { AlarmSummaryConfig, Env, AlarmSummarySettings } from './alarm-summary-settings';
import { AppSettings } from '../../../app-settings';
import { MatrixComponent } from '../Matrix/matrix.component';
import { DbmWriteAvaSupService } from '../../../service/scs/ava/dbm-write-ava-sup.service';
import { DbmCacheAvaSupService } from '../../../service/scs/ava/dbm-cache-ava-sup.service';

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
      this.readingDbmData(this.env, this.univnames, this.index);
    }
    this.disableButtons(true);
  }
  @Output() onUpdatedAlarmSummary = new EventEmitter<number[][]>();

  notifyMatrix: string;

  dbmReadAvaSupSubscription: Subscription;
  dbmWriteAvaSupSubscription: Subscription;

  btnApply: boolean;
  btnCancel: boolean;

  updateMatrix: any;

  constructor(
    private translate: TranslateService
    , private dbmCacheAvaSupService: DbmCacheAvaSupService
    , private dbmReadAvaSupService: DbmReadAvaSupService
    , private dbmWriteAvaSupService: DbmWriteAvaSupService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.dbmReadAvaSupSubscription = this.dbmReadAvaSupService.avaSupItem
      .subscribe(env => {
        console.log(this.c, f, 'dbmReadAvaSupSubscription', env);

        if (null != env && '' !== env) {
          console.log(this.c, f, 'Data already read from DBM');

          // Update the Matrix Display
          const data: Map<number, Map<number, number>> = this.dbmCacheAvaSupService.getAlarmMatrixData(env);
          this.updateMatrix = data;

          this.disableButtons(true);

        } else {
          console.warn(this.c, f, 'env IS INVALID');
        }
      });

    this.dbmWriteAvaSupSubscription = this.dbmWriteAvaSupService.avaSupItem
      .subscribe(env => {
        console.log(this.c, f, 'dbmWriteAvaSupSubscription', env);

        if (null != env && '' !== env) {
          console.log(this.c, f, 'Data already write to DBM');

        } else {
          console.warn(this.c, f, 'env IS INVALID');
        }
      });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.dbmReadAvaSupSubscription.unsubscribe();
    this.dbmWriteAvaSupSubscription.unsubscribe();
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

  writeDbm(): void {
    const f = 'writeDbm';
    console.log(this.c, f);
    this.dbmWriteAvaSupService.writeData(this.env, this.data, this.index);
  }

  readingDbmData(env: string, univnames: string[], index: number): void {
    const f = 'readingDbmData';
    console.log(this.c, f);
    console.log(this.c, f, env, univnames, index);
    this.dbmReadAvaSupService.readData(env, univnames, index);
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
        this.writeDbm();
      } break;
      case 'cancel': {
        if (null != this.index) {
          this.readingDbmData(this.env, this.univnames, this.index);
        }
      } break;
    }
  }

}
