import { Component, OnInit, OnDestroy, Output, EventEmitter, ViewChild } from '@angular/core';
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

@Component({
  selector: 'app-steps'
  , templateUrl: './steps.component.html'
  , styleUrls: ['./steps.component.css']
})
export class StepsComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED  = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED  = AppSettings.STR_STEP_UPDATED;

  readonly c = StepsComponent.name;

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

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private selectionService: SelectionService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

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

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  loadTranslations() {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.stepsDataTable.messages['emptyMessage'] = this.translate.instant('&steps_dg_footer_emptymessage');
    this.stepsDataTable.messages['selectedMessage'] = this.translate.instant('&steps_dg_footer_selectedmessage');
    this.stepsDataTable.messages['totalMessage'] = this.translate.instant('&steps_dg_footer_totalmessage');
  }

  private getStateStr(state: StepType): string {
    const f = 'getStateStr';
    console.log(this.c, f);
    console.log(this.c, f, state);
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

    const card: Card = this.cardService.getCards([this.selectedCardName])[0];

    if ( null != card ) {
      const steps = card.steps;
      if ( steps.length > 0 ) {
        steps.forEach((item, index) => {
          const dtStep: DatatableStep = new DatatableStep(
            StepSettings.STR_STEP_PREFIX + item.step
            , StepSettings.STR_GEO_PREFIX + item.equipment.geo
            , StepSettings.STR_FUNC_PREFIX + item.equipment.func
            , item.equipment.eqplabel
            , item.equipment.pointlabel
            , StepSettings.STR_VALUE_PREFIX + item.equipment.ev[0].value.start
            , StepSettings.STR_DELAY_PREFIX + item.delay
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
        this.selectedCardName = this.selectionService.getSelectedCardIds()[0];
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
