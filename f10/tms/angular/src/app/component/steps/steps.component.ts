import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CardService } from '../../service/card/card.service';
import { Subscription } from 'rxjs/Subscription';
import { DatatableStep } from '../../model/DatatableScenario';
import { Card, Step, StepType } from '../../model/Scenario';
import { AppSettings } from '../../app-settings';
import { SelectionService } from '../../service/card/selection.service';
import { DacSimService } from '../../service/scs/dac-sim.service';
import { StepSettings } from './step-settings';

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
  columns_step = [
    { prop: 'step' }
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

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private selectionService: SelectionService
  ) {
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscription = this.cardService.cardItem
    .subscribe(item => {
      console.log(this.c, f, 'cardSubscription', item);
      switch (item) {
        case CardService.STR_STEP_RELOADED: {
          this.btnClicked(StepsComponent.STR_STEP_RELOADED);
        } break;
        case CardService.STR_STEP_UPDATED: {
          this.btnClicked(StepsComponent.STR_STEP_UPDATED);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe(item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionService.STR_CARD_SELECTED: {
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
            '' + item.step
            , StepSettings.STR_GEO_PREFIX + item.equipment.geo
            , StepSettings.STR_FUNC_PREFIX + item.equipment.func
            , item.equipment.eqplabel
            , item.equipment.pointlabel
            , '' + item.equipment.ev[0].value.start
            , '' + item.delay
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
      case CardService.STR_CARD_RELOADED: {
        this.selectedCardName = '';
        this.txtCardName = this.selectedCardName;
        this.reloadSteps();
      } break;
      case SelectionService.STR_CARD_SELECTED: {
        this.selectedCardName = this.selectionService.getSelectedCardIds()[0];
        this.txtCardName = this.selectedCardName;
        this.reloadSteps();
      } break;
      case CardService.STR_STEP_RELOADED: {
        this.reloadSteps();
      } break;
      case CardService.STR_STEP_UPDATED: {
        this.reloadSteps(false);
      } break;
    }
    this.sendNotifyParent(btnLabel);
  }
}
