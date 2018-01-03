import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, ViewChild, SimpleChanges, Input } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { Subscription } from 'rxjs/Subscription';
import { DatatableStep } from '../../../model/DatatableScenario';
import { Card, Step, StepType } from '../../../model/Scenario';
import { AppSettings } from '../../../app-settings';
import { SelectionService } from '../../../service/card/selection.service';
import { DacSimService } from '../../../service/scs/dac-sim.service';
import { StepSettings } from './step-settings';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { SettingsService } from '../../../service/settings.service';
import { StorageService } from '../../../service/card/storage.service';
import { RowHeightCache } from '@swimlane/ngx-datatable/release/utils';
import { Observable } from 'rxjs/Observable';
import { DbmPollingService } from '../../../service/scs/dbm-polling.service';
import { Subscribable } from 'rxjs/Observable';

@Component({
  selector: 'app-steps'
  , templateUrl: './steps.component.html'
  , styleUrls: ['./steps.component.css']
})
export class StepsComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED = AppSettings.STR_STEP_UPDATED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'StepsComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;
  dbmPollingSubscription: Subscription;

  selectedCardName: string;
  selectedCardStep: number[];

  txtCardName: string;

  // Datatable
  @ViewChild('stepsDataTable') stepsDataTable: DatatableComponent;

  rows_step = new Array<DatatableStep>();
  selected_step = new Array<DatatableStep>();
  loadingIndicator: boolean;
  reorderable: boolean;

  // properties for ngx-datatable
  public messages = {};

  private stepPrefix: string;
  private stepBase: number;

  private geoPrefix: string;
  private funcPrefix: string;

  private eqplabelPrefix: string;
  private pointlabelPrefix: string;
  private valuePrefix: string;
  private delayPrefix: string;
  private currentPrefix: string;
  private currentDefault: string;

  private subscriptionCard: Card;

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private selectionService: SelectionService
    , private storageService: StorageService
    , private settingsService: SettingsService
    , private dbmPollingService: DbmPollingService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => {
        console.log(this.c, f, 'cardSubscription', item);
        switch (item) {
          case CardServiceType.CARD_RELOADED: {
            this.btnClicked(StepsComponent.STR_CARD_RELOADED);
          } break;
          case CardServiceType.STEP_RELOADED: {
            this.btnClicked(StepsComponent.STR_STEP_RELOADED);
          } break;
          case CardServiceType.STEP_UPDATED: {
            this.btnClicked(StepsComponent.STR_STEP_UPDATED);
          } break;
        }
      });

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            this.btnClicked(StepsComponent.STR_CARD_SELECTED);
          } break;
        }
      });

    this.dbmPollingSubscription = this.dbmPollingService.dbmPollingItem
      .subscribe(item => {
        console.log(this.c, f, 'dbmPollingSubscription', item);

        if (item === this.selectedCardName) {
          this.btnClicked(StepsComponent.STR_STEP_UPDATED);
        }
      });
  }

  ngOnDestroy(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    // prevent memory leak when component is destroyed
    this.selectionSubscription.unsubscribe();
    this.cardSubscription.unsubscribe();
    this.dbmPollingSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if (changes[StepsComponent.STR_NORIFY_FROM_PARENT]) {
      switch (changes[StepsComponent.STR_NORIFY_FROM_PARENT].currentValue) {
        // case StepsComponent.STR_NEWSTEP: {
        // } break;
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.stepPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_STEP_PREFIX);
    this.stepBase = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_STEP_BASE);

    this.geoPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_GEO_PREFIX);
    this.funcPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_FUNC_PREFIX);
    this.eqplabelPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_EQPLABEL_PREFIX);
    this.pointlabelPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_POINTLABEL_PREFIX);
    this.delayPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_DELAY_PREFIX);
    this.valuePrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_VALUE_PREFIX);
    this.currentPrefix = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_CURRENT_PREFIX);
    this.currentDefault = this.settingsService.getSetting(this.c, f, this.c, StepSettings.STR_CURRENT_DEFAULT);
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.stepsDataTable.messages['emptyMessage'] = this.translate.instant('&steps_dg_footer_emptymessage');
    this.stepsDataTable.messages['selectedMessage'] = this.translate.instant('&steps_dg_footer_selectedmessage');
    this.stepsDataTable.messages['totalMessage'] = this.translate.instant('&steps_dg_footer_totalmessage');
  }

  getRowClass(row) {
    const f = 'getRowClass';
    // console.log(this.c, f);
    return {
      'age-is-ten': (row.age % 10) === 0
    };
  }

  getCellClass({ row, column, value }): any {
    const f = 'getCellClass';
    // console.log(this.c, f);
    return {
      'is-female': value === 'female'
    };
  }

  private getStateStr(state: StepType): string {
    const f = 'getStateStr';
    console.log(this.c, f);
    let ret: string = StepSettings.STR_STEP_GD_STATE_UNKNOW;
    switch (state) {
      case StepType.START: {
        ret = StepSettings.STR_STEP_GD_STATE_STARTED;
      } break;
      case StepType.START_FAILED: {
        ret = StepSettings.STR_STEP_GD_STATE_STARTED_FAILDED;
      } break;
      case StepType.START_RUNNING: {
        ret = StepSettings.STR_STEP_GD_STATE_START_RUNNING;
      } break;
      case StepType.START_SKIPPED: {
        ret = StepSettings.STR_STEP_GD_STATE_START_SKIPPED;
      } break;
      case StepType.STOP_SKIPPED: {
        ret = StepSettings.STR_STEP_GD_STATE_STOP_SKIPPED;
      } break;
      case StepType.STOPPED: {
        ret = StepSettings.STR_STEP_GD_STATE_STOPPED;
      } break;
      case StepType.STOPPED_FAILED: {
        ret = StepSettings.STR_STEP_GD_STATE_STOPPED_FAILDED;
      } break;
      case StepType.STOP_RUNNING: {
        ret = StepSettings.STR_STEP_GD_STATE_STOP_RUNNING;
      } break;
    }
    return ret;
  }

  onChange(name: string, event?: Event): void {
    const f = 'onChange';
    console.log(this.c, f);
  }

  private reloadingSteps(updateOnly: boolean, keepSelection: boolean, cb): void {
    const f = 'reloadingSteps';
    console.log(this.c, f);

    if ( ! updateOnly ) {
      // Reset ScenarioStep
      this.rows_step = [];
    }

    const card: Card = this.cardService.getCard([this.selectedCardName]);

    if (null != card) {
      const steps = card.steps;
      if (steps.length > 0) {
        steps.forEach((item, index) => {

          const step = '' + item.step;
          const location = this.geoPrefix + item.equipment.geo;
          const system = this.funcPrefix + item.equipment.func;
          const equipment = this.eqplabelPrefix + item.equipment.eqplabel;
          const point = this.pointlabelPrefix + item.equipment.pointlabel;
          const value = this.valuePrefix + item.equipment.valuelabel;
          const delay = this.delayPrefix + item.delay;
          const execute = item.execute;
          const status = this.getStateStr(item.state);
          const num = this.stepPrefix + (+item.step + +this.stepBase);
          let current = this.currentPrefix + item.equipment.currentlabel;
          const updated = new Date();
          if ( current === undefined || current === 'undefined' ) {
            current = this.currentDefault;
          }

          if ( ! updateOnly ) {
            const dtStep: DatatableStep = new DatatableStep(
              step
              , location
              , system
              , equipment
              , point
              , value
              , delay
              , execute
              , status
              , num
              , current
              , updated
            );
            this.rows_step.push(dtStep);
          } else {
            this.rows_step[index].step = step;
            this.rows_step[index].location = location;
            this.rows_step[index].system = system;
            this.rows_step[index].equipment = equipment;
            this.rows_step[index].point = point;
            this.rows_step[index].value = value;
            this.rows_step[index].delay = delay;
            this.rows_step[index].execute = execute;
            this.rows_step[index].status = status;
            this.rows_step[index].num = num;
            this.rows_step[index].current = current;
            this.rows_step[index].updated = updated;
          }
        });
      } else {
        console.warn(this.c, f, 'step IS ZERO LENGTH');
      }
    } else {
      console.warn(this.c, f, 'card IS NULL');
    }
    this.rows_step = [...this.rows_step];

    if ( ! keepSelection ) {
      this.selected_step = [];
      this.setSelectedRow();
      this.selectedCardStep = null;
    } else {
      if ( this.selectedCardStep && this.rows_step ) {
        this.selectedCardStep.forEach ( item1 => {
          this.rows_step.forEach ( item2 => {
            if ( '' + item1 == item2.step ) {
              this.selected_step.push(item2);
            }
          });
        });
      }
    }
  }

  private reloadSteps(updateOnly: boolean = false, keepSelection: boolean = false): void {
    const f = 'reloadSteps';
    console.log(this.c, f);
    console.log(this.c, f, updateOnly);

    this.reloadingSteps(updateOnly, keepSelection, (data) => {
      setTimeout(() => { this.loadingIndicator = false; }, 1500);
    });
  }

  selectFn() {
    const f = 'selectFn';
    console.log(this.c, f);
  }

  isCheckedDisabled() {
    return this.cardService.isRunning();
  }

  private emptySteps() {
    const f = 'emptySteps';
    console.log(this.c, f);
    this.rows_step = [];
    this.rows_step = [...this.rows_step];
  }

  private setCheckboxs(value: boolean) {
    const f = 'selectAllCheckbox';
    console.log(this.c, f);

    const card = this.cardService.getCard([this.selectedCardName]);
    card.steps.forEach(step => {
      step.execute = value;
    });

    this.emptySteps();

    const timer = Observable.interval(10).map((x) => {
      console.log(this.c, f, 'interval map');

    }).subscribe((x) => {
      console.log(this.c, f, 'interval subscribe');
      this.reloadSteps(true);
      timer.unsubscribe();
    });

    this.cardService.cardChanged(CardServiceType.STEP_RELOADED);
  }

  onColumnClick(name: string) {
    const f = 'onColumnClick';
    console.log(this.c, f);
    console.log(this.c, f, 'name', name);
  }

  allRowsSelected(): boolean {
    let ret = false;
    const steps: Step[] = this.cardService.getSteps(this.selectedCardName);
    let counter = 0;
    if ( steps ) {
      steps.forEach(step => {
        if (step.execute) {
          counter++;
        }
      });
      ret = (steps.length === counter);
    }
    return ret;
  }

  onCheckboxChange(name: string, event) {
    const f = 'onCheckboxChange';
    console.log(this.c, f);
    console.log(this.c, f, 'event', event);

    if ('cell' === name) {
      this.cardService.getStep(this.selectedCardName, this.selectedCardStep).execute = event.target.checked;
    } else if ('header' === name) {
      this.setCheckboxs(event.target.checked);
    }
  }

  private setSelectedRow() {
    const f = 'setSelectedRow';
    console.log(this.c, f, 'name', name, 'event', event);

    this.selectedCardStep = new Array<number>();
    this.selected_step.forEach(item => {
      this.selectedCardStep.push(+item.step);
    });
    this.selectionService.setSelectedStepIds(this.selectedCardStep);
  }

  onRowSelect(name: string, event: Event) {
    const f = 'onRowSelect';
    console.log(this.c, f, 'name', name, 'event', event);

    this.setSelectedRow();

    // Quick Fix for the ngx-datatable view update
    window.dispatchEvent(new Event('resize'));
  }

  onActivate(name: string, event) {
    const f = 'onActivate';
    console.log(this.c, f, 'name', name, 'event', event);

    const checkboxCellIndex = 1;
    if ('checkbox' == event.type) {
      // Stop event propagation and let onSelect() work
      console.log('Checkbox Selected', event);
      event.event.stopPropagation();
    } else if ('click' == event.type && event.cellIndex != checkboxCellIndex) {
      // Do somethings when you click on row cell other than checkbox
      console.log('Row Clicked', event.row); /// <--- object is in the event row variable
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
    this.txtCardName = this.selectedCardName = '';
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case StepsComponent.STR_INIT: {
        this.init();
      } break;
      case StepsComponent.STR_CARD_RELOADED: {
        this.selectedCardName = '';
        this.txtCardName = this.selectedCardName;
        this.reloadSteps(false, true);
      } break;
      case StepsComponent.STR_CARD_SELECTED: {
        this.selectedCardName = this.selectionService.getSelectedCardId();
        this.txtCardName = this.selectedCardName;
        this.reloadSteps(false, true);
      } break;
      case StepsComponent.STR_STEP_RELOADED: {
        this.reloadSteps(false, true);
      } break;
      case StepsComponent.STR_STEP_UPDATED: {
        this.reloadSteps(true);
      } break;
    }
    // this.sendNotifyParent(btnLabel);
  }
}
