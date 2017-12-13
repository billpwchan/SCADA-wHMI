import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, ViewChild, SimpleChanges, Input } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CardService } from '../../service/card/card.service';
import { Subscription } from 'rxjs/Subscription';
import { DatatableStep } from '../../model/DatatableScenario';
import { Card, Step, StepType } from '../../model/Scenario';
import { AppSettings } from '../../app-settings';
import { SelectionService } from '../../service/card/selection.service';
import { DacSimService } from '../../service/scs/dac-sim.service';
import { StepSettings } from './step-settings';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { CardServiceType } from '../../service/card/card-settings';
import { SelectionServiceType } from '../../service/card/selection-settings';
import { SettingsService } from '../../service/settings.service';

@Component({
  selector: 'app-steps'
  , templateUrl: './steps.component.html'
  , styleUrls: ['./steps.component.css']
})
export class StepsComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED  = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED  = AppSettings.STR_STEP_UPDATED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = StepsComponent.name;

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  selectedCardName: string;
  selectedCardStep: number[];

  txtCardName: string;

  // Datatable
  @ViewChild('stepsDataTable') stepsDataTable: DatatableComponent;

  columns_step = [
    { prop: 'step'  }
    , { name: 'Location' }
    , { name: 'System' }
    , { name: 'Equipment' }
    , { name: 'Point' }
    , { name: 'Value' }
    , { name: 'Delay' }
    , { name: 'Status' }
  ];
  rows_step = new Array<DatatableStep>();
  selected_step = new Array<DatatableStep>();

  // properties for ngx-datatable
  public messages = {};

  private geoPrefix: string;
  private funcPrefix: string;

  private eqplabelPrefix: string;
  private pointlabelPrefix: string;
  private valuePrefix: string;
  private delayPrefix: string;

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private selectionService: SelectionService
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

    this.cardSubscription = this.cardService.cardItem
    .subscribe(item => {
      console.log(this.c, f, 'cardSubscription', item);
      switch (item) {
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
  }

  ngOnDestroy(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.selectionSubscription.unsubscribe();
    this.cardSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if ( changes[StepsComponent.STR_NORIFY_FROM_PARENT] ) {
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

    const component: string = StepsComponent.name;
    this.geoPrefix = this.settingsService.getSetting(this.c, f, component, StepSettings.STR_GEO_PREFIX);
    this.funcPrefix = this.settingsService.getSetting(this.c, f, component, StepSettings.STR_FUNC_PREFIX);
    this.eqplabelPrefix = this.settingsService.getSetting(this.c, f, component, StepSettings.STR_EQPLABEL_PREFIX);
    this.pointlabelPrefix = this.settingsService.getSetting(this.c, f, component, StepSettings.STR_POINTLABEL_PREFIX);
    this.delayPrefix = this.settingsService.getSetting(this.c, f, component, StepSettings.STR_DELAY_PREFIX);
    this.valuePrefix = this.settingsService.getSetting(this.c, f, component, StepSettings.STR_VALUE_PREFIX);
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.stepsDataTable.messages['emptyMessage'] = this.translate.instant('&steps_dg_footer_emptymessage');
    this.stepsDataTable.messages['selectedMessage'] = this.translate.instant('&steps_dg_footer_selectedmessage');
    this.stepsDataTable.messages['totalMessage'] = this.translate.instant('&steps_dg_footer_totalmessage');
  }

  getRowClass(row) {
    return {
      'age-is-ten': (row.age % 10) === 0
    };
  }

  getCellClass({ row, column, value }): any {
    return {
      'is-female': value === 'female'
    };
  }

  private getStateStr(state: StepType): string {
    const f = 'getStateStr';
    console.log(this.c, f);
    let ret: string = StepSettings.STR_STEP_UNKNOW;
    switch ( state ) {
      case StepType.START: {
        ret = StepSettings.STR_STEP_START;
      } break;
      case StepType.START_FAILED: {
        ret = StepSettings.STR_STEP_START_FAILD;
      } break;
      case StepType.START_RUNNING: {
        ret = StepSettings.STR_STEP_START_RUNNING;
      } break;
      case StepType.STOP: {
        ret = StepSettings.STR_STEP_STOP;
      } break;
      case StepType.STOP_FAILED: {
        ret = StepSettings.STR_STEP_STOP_FAILD;
      } break;
      case StepType.STOP_RUNNING: {
        ret = StepSettings.STR_STEP_STOP_RUNNING;
      } break;
    }
    return ret;
  }

  private reloadSteps(updateSelection: boolean = true): void {
    const f = 'reloadSteps';
    console.log(this.c, f);

    // Rset ScenarioStep
    this.rows_step = [];

    const card: Card = this.cardService.getCard([this.selectedCardName]);

    if ( null != card ) {
      const steps = card.steps;
      if ( steps.length > 0 ) {
        steps.forEach((item, index) => {
          const dtStep: DatatableStep = new DatatableStep(
            StepSettings.STR_STEP_PREFIX + item.step
            , this.geoPrefix + item.equipment.geo
            , this.funcPrefix + item.equipment.func
            , this.eqplabelPrefix + item.equipment.eqplabel
            , this.pointlabelPrefix + item.equipment.pointlabel
            , this.valuePrefix + item.equipment.valuelabel
            , this.delayPrefix + item.delay
            , this.getStateStr(item.state)
          );
          this.rows_step.push(dtStep);
        });
      } else {
        console.log(this.c, f, 'card IS NULL');
      }
    }

    // this.rows_step = [...this.rows_step];
    this.selected_step = [];

    this.selectedCardStep = null;
  }

  private onRowSelect(name: string, event: Event) {
    const f = 'onRowSelect';
    console.log(this.c, f, 'name', name, 'event', event);

    const stepIds: number [] = new Array<number>();
    this.selected_step.forEach( item => {
      stepIds.push(+item.step);
    });
    this.selectionService.setSelectedStepIds(stepIds);
  }

  private onActivate(name: string, event: Event) {
    const f = 'onActivate';
    console.log(this.c, f, 'name', name, 'event', event);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
    this.txtCardName = this.selectedCardName = '';
  }

  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case StepsComponent.STR_INIT: {
        this.init();
      } break;
      case StepsComponent.STR_CARD_RELOADED: {
        this.selectedCardName = '';
        this.txtCardName = this.selectedCardName;
        this.reloadSteps();
      } break;
      case StepsComponent.STR_CARD_SELECTED: {
        this.selectedCardName = this.selectionService.getSelectedCardId();
        this.txtCardName = this.selectedCardName;
        this.reloadSteps();
      } break;
      case StepsComponent.STR_STEP_RELOADED: {
        this.reloadSteps();
      } break;
      case StepsComponent.STR_STEP_UPDATED: {
        this.reloadSteps(false);
      } break;
    }
    this.sendNotifyParent(btnLabel);
  }
}
