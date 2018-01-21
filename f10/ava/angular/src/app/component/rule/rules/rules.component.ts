import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { AlarmSummaryConfig, AlarmSummarySettings } from '../../alarm/alarm-summary/alarm-summary-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { AlarmSummaryComponent } from '../../alarm/alarm-summary/alarm-summary.component';
import { Subscription } from 'rxjs/Subscription';
import { GetInstancesByClassNameService } from '../../../service/scs/ava/dbm/get-instances-by-class-name.service';

@Component({
  selector: 'app-rules',
  templateUrl: './rules.component.html',
  styleUrls: ['./rules.component.css']
})
export class RulesComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  readonly c = 'RulesComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  private cfg: AlarmSummaryConfig;
  @Input() config: AlarmSummaryConfig;

  private env: string;
  private index: number;
  private data: Map<number, Map<number, number>>;

  private preview: number[][];
  private updated: number[][];
  @Input()
  set updateRules(index: number) {
    const f = 'updateAlarmSummary';
    console.log(this.c, f);
    // this.index = index;
    // if ( null != this.index ) {
    //   this.readingOls();
    // }
  }
  @Output() onUpdatedAlarmSummary = new EventEmitter<number[][]>();

  // olsAvaSupSubscription: Subscription;
  // dbmReadAvaSupSubscription: Subscription;
  // dbmWriteAvaSupSubscription: Subscription;

  getInstancesByClassNameSubscription: Subscription;

  updateMatrix: any;
  // @ViewChild('MatrixComponent') matrixComponent: MatrixComponent;

  constructor(
    private translate: TranslateService
    // , private olsAvaSupService: OlsAvaSupService
    // , private dbmCacheAvaSupService: DbmCacheAvaSupService
    // , private dbmReadAvaSupService: DbmReadAvaSupService
    // , private dbmWriteAvaSupService: DbmWriteAvaSupService
    , private getInstancesByClassNameService: GetInstancesByClassNameService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    // this.olsAvaSupSubscription = this.olsAvaSupService.avaSupItem
    // .subscribe(env => {
    //   console.log(this.c, f, 'avaSupSubscription', env);

    //   if ( null != env && '' !== env ) {
    //     if ( null != this.index ) {
    //       console.log(this.c, f, 'this.index', this.index);
    //       const univnames: string[] = this.olsAvaSupService.getUnivname(env);
    //       this.readingDbmData(env, univnames, this.index);
    //     } else {
    //       console.warn(this.c, f, 'index IS INVALID');
    //     }
    //   } else {
    //     console.warn(this.c, f, 'env IS INVALID');
    //   }
    // });

    // this.dbmReadAvaSupSubscription = this.dbmReadAvaSupService.avaSupItem
    // .subscribe(env => {
    //   console.log(this.c, f, 'dbmReadAvaSupSubscription', env);

    //   if ( null != env && '' !== env ) {
    //     console.log(this.c, f, 'Data already read from DBM');

    //     // Update the Matrix Display
    //     const data: Map<number, Map<number, number>> = this.dbmCacheAvaSupService.getAlarmMatrixData(env);
    //     this.updateMatrix = data;

    //   } else {
    //     console.warn(this.c, f, 'env IS INVALID');
    //   }
    // });

    // this.dbmWriteAvaSupSubscription = this.dbmWriteAvaSupService.avaSupItem
    // .subscribe(env => {
    //   console.log(this.c, f, 'dbmWriteAvaSupSubscription', env);

    //   if ( null != env && '' !== env ) {
    //     console.log(this.c, f, 'Data already write to DBM');

    //   } else {
    //     console.warn(this.c, f, 'env IS INVALID');
    //   }
    // });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    // this.olsAvaSupSubscription.unsubscribe();
    // this.dbmReadAvaSupSubscription.unsubscribe();
    // this.dbmWriteAvaSupSubscription.unsubscribe();
  }

  onParentChange(change: string): void {
    const f = 'onParentChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);

    switch (change) {
      // case StepEditSettings.STR_STEP_EDIT_ENABLE: {
      //   this.editEnableNewStep = true;
      // } break;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    if ( changes[AlarmSummarySettings.STR_NORIFY_FROM_PARENT] ) {
      this.onParentChange(changes[AlarmSummarySettings.STR_NORIFY_FROM_PARENT].currentValue);
    }
    if ( changes[AlarmSummarySettings.STR_CONFIG] ) {
      this.cfg = changes[AlarmSummarySettings.STR_CONFIG].currentValue as AlarmSummaryConfig;
    }
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

  onUpdatedRules(data: Map<number, Map<number, number>>) {
    const f = 'onUpdatedRules';
    console.log(this.c, f);
    console.log(this.c, f, data);
    this.data = data;
  }

  // readingOls(): void {
  //   const f = 'readingOls';
  //   console.log(this.c, f);
  //   const envIndex = 0;
  //   if ( null != this.cfg ) {
  //     if ( null != this.cfg.envs ) {
  //       if ( this.cfg.envs.length > envIndex ) {
  //         this.env = this.cfg.envs[envIndex].value;
  //         // const url = this.olsAvaSupService.prepareUrl(this.env);
  //         // this.olsAvaSupService.retriveAvaSup(this.env, url);
  //       } else {
  //         console.warn(this.c, f, 'this.cfg.envs.length IS ZERO');
  //       }
  //     } else {
  //       console.warn(this.c, f, 'this.cfg.envs IS NULL');
  //     }
  //   } else {
  //     console.warn(this.c, f, 'this.cfg IS NULL');
  //   }
  // }

  // readingOlsData(env: Env): void {
  //   const f = 'readingOlsData';
  //   console.log(this.c, f);

  //   const connAddr = env.value;
  //   // const url = this.olsAvaSupService.prepareUrl(connAddr);
  //   // this.olsAvaSupService.retriveAvaSup(connAddr, url);
  // }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case AlarmSummaryComponent.STR_INIT: {
        this.init();
      } break;
      case AlarmSummaryComponent.STR_CARD_RELOADED: {
      } break;
      case AlarmSummaryComponent.STR_CARD_SELECTED: {
      } break;
      // case 'apply': {
      //   this.writeDbm();
      // } break;
      // case 'cancel': {
      //   if ( null != this.index ) {
      //     this.readingOls();
      //   }
      // } break;
    }
  }

}
