import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, ViewChild, SimpleChanges, Input } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { Subscription } from 'rxjs/Subscription';
import { DatatableStep } from '../../../model/DatatableScenario';
import { Card, Step, Equipment } from '../../../model/Scenario';
import { AppSettings } from '../../../app-settings';
import { StepsSettings } from './step-settings';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { SettingsService } from '../../../service/settings.service';
import { RowHeightCache } from '@swimlane/ngx-datatable/release/utils';
import { Observable } from 'rxjs/Observable';
import { DbmPollingService } from '../../../service/scs/dbm-polling.service';
import { Subscribable } from 'rxjs/Observable';
import { DataScenarioHelper } from '../../../model/DataScenarioHelper';

@Component({
  selector: 'app-steps'
  , templateUrl: './steps.component.html'
  , styleUrls: ['./steps.component.css']
})
export class StepsComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NORIFY_FROM_PARENT = AppSettings.STR_NOTIFY_FROM_PARENT;

  readonly c = 'StepsComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  private updated: Step[];
  @Input()
  set updateSteps(steps: Step[]) {
    const f = 'updateSteps';
    console.log(this.c, f);

    this.updated = steps;

    if ( null != steps ) {
      this.reloadData();
    } else {
      console.warn(this.c, f, 'data IS INVALID');
    }
  }
  @Output() onUpdatedSteps = new EventEmitter<Step[]>();
  @Output() onUpdatedStepSelection = new EventEmitter<number[]>();

  selectedStepStep: number[];

  // Datatable
  @ViewChild('stepsDataTable') stepsDataTable: DatatableComponent;

  rows_step = new Array<DatatableStep>();
  selected_step = new Array<DatatableStep>();

  // properties for ngx-datatable
  public messages = {};

  private stepPrefix: string;
  private stepBase: number;

  private geoPrefix: string;
  private funcPrefix: string;

  private eqplabelPrefix: string;
  private pointlabelPrefix: string;
  private valuePrefix: string;

  private subscriptionCard: Card;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if (changes[StepsComponent.STR_NORIFY_FROM_PARENT]) {
      if (changes[StepsComponent.STR_NORIFY_FROM_PARENT].currentValue) {
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

    this.stepPrefix = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_STEP_PREFIX);
    this.stepBase = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_STEP_BASE);

    this.geoPrefix = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_GEO_PREFIX);
    this.funcPrefix = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_FUNC_PREFIX);
    this.eqplabelPrefix = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_EQPLABEL_PREFIX);
    this.pointlabelPrefix = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_POINTLABEL_PREFIX);
    this.valuePrefix = this.settingsService.getSetting(this.c, f, this.c, StepsSettings.STR_VALUE_PREFIX);
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.stepsDataTable.messages['emptyMessage'] = this.translate.instant('&steps_dg_footer_emptymessage');
    this.stepsDataTable.messages['selectedMessage'] = this.translate.instant('&steps_dg_footer_selectedmessage');
    this.stepsDataTable.messages['totalMessage'] = this.translate.instant('&steps_dg_footer_totalmessage');
  }

  private reloadData(): void {
    const f = 'reloadData';
    console.log(this.c, f);

    this.rows_step = [];

    if ( null != this.updated ) {
      if (this.updated.length > 0) {
        this.updated.forEach((item, index) => {
          if ( null != item ) {

            const dtStep: DatatableStep = new DatatableStep(
              '' + index
              , this.geoPrefix + item.equipment.geo
              , this.funcPrefix + item.equipment.func
              , this.eqplabelPrefix + item.equipment.eqplabel
              , this.pointlabelPrefix + item.equipment.pointlabel
              , this.valuePrefix + item.equipment.valuelabel
              , this.stepPrefix + (+index + +this.stepBase)
              , new Date()
            );
            this.rows_step.push(dtStep);
          }
        });
      } else {
        console.warn(this.c, f, 'steps IS ZERO LENGTH');
      }
    } else {
      console.warn(this.c, f, 'steps IS NULL');
    }

    this.onUpdatedSteps.emit(this.updated);
  }

  private emptySteps() {
    const f = 'emptySteps';
    console.log(this.c, f);
    this.rows_step = [];
    this.rows_step = [...this.rows_step];
  }

  private setSelectedRow() {
    const f = 'setSelectedRow';
    console.log(this.c, f, 'name', name);

    this.selectedStepStep = new Array<number>();
      this.selected_step.forEach(item => {
        this.selectedStepStep.push(Number(item.step));
    });
    this.onUpdatedStepSelection.emit(this.selectedStepStep);
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
    if ('checkbox' === event.type) {
      // Stop event propagation and let onSelect() work
      console.log('Checkbox Selected', event);
      event.event.stopPropagation();
    } else if ('click' === event.type && event.cellIndex !== checkboxCellIndex) {
      // Do somethings when you click on row cell other than checkbox
      console.log('Row Clicked', event.row); /// <--- object is in the event row variable
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case StepsComponent.STR_INIT: {
        this.init();
      } break;
    }
  }
}
