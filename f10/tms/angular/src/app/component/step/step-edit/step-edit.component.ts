import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, SimpleChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { DatatableStep } from '../../../model/DatatableScenario';
import { Card, Step, Equipment, Execution, StepType, ExecType } from '../../../model/Scenario';
import { AppSettings } from '../../../app-settings';
import { StepEditSettings } from './step-edit-settings';
import { Subscription } from 'rxjs/Subscription';
import { DacSimSettings, DacSimExecType } from '../../../service/scs/dac-sim-settings';
import { Subscribable } from 'rxjs/Observable';
import { SelectionService } from '../../../service/card/selection.service';
import { StepSettings } from '../steps/step-settings';
import { SettingsService } from '../../../service/settings.service';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { IPointSelection, SelOptStr } from 'point-select';

@Component({
  selector: 'app-step-edit',
  templateUrl: './step-edit.component.html',
  styleUrls: ['./step-edit.component.css']
})
export class StepEditComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'StepEditComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;
  olsSubscription: Subscription;
  dbmSubscription: Subscription;

  selectedCardId: string;
  selectedStepId: number;

  // GUI Data Binding
  editEnableNewStep: boolean;
  enablePointSelect: boolean;
  editEnableCancelStep: boolean;
  btnDisabledAddCancelStep: boolean;

  private geoPrefix: string;
  private funcPrefix: string;
  private envs: Array<SelOptStr>;

  private delayRangeStart: number;
  private delayRangeEnd: number;
  private delayRangeStep: number;
  private delayRangePrefix: string;
  private delayDefaultValue: number;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private cardService: CardService
    , private selectionService: SelectionService
  ) {

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
            this.btnClicked(StepEditComponent.STR_CARD_RELOADED);
          } break;
          case CardServiceType.STEP_RELOADED: {
            this.btnClicked(StepEditComponent.STR_STEP_RELOADED);
          } break;
        }
      }
    );

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            this.btnClicked(StepEditComponent.STR_CARD_SELECTED);
          } break;
          case SelectionServiceType.STEP_SELECTED: {
            this.btnClicked(StepEditComponent.STR_STEP_SELECTED);
          } break;
        }
      }
    );

    this.btnClicked('init');
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
  }

  onParentChange(change: string): void {
    const f = 'onParentChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);

    switch (change) {
      case StepEditSettings.STR_STEP_EDIT_ENABLE: {
        this.editEnableNewStep = true;
      } break;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if ( changes[StepEditComponent.STR_NORIFY_FROM_PARENT] ) {
      this.onParentChange(changes[StepEditComponent.STR_NORIFY_FROM_PARENT].currentValue);
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

    this.geoPrefix = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_GEO_PREFIX);
    this.funcPrefix = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_FUNC_PREFIX);
    this.envs = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_ENVS);

    this.delayRangeStep = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_DELAY_RANGE_STEP);
    this.delayRangeStart = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_DELAY_RANGE_START);
    this.delayRangeEnd = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_DELAY_RANGE_END);
    this.delayRangePrefix = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_DELAY_RANGE_PREFIX);

    this.delayDefaultValue = this.settingsService.getSetting(this.c, f, this.c, StepEditSettings.STR_DELAY_DEFAULT_VALUE);
  }

  private newStep(step: Step): void {
    const f = 'newStep';
    console.log(this.c, f);
    const card = this.cardService.getCard([this.selectedCardId]);
    if ( null != card ) {
      console.log(this.c, f, 'card.name', card.name);
      card.steps.push(step);
    } else {
      console.warn(this.c, f, 'card IS NULL');
    }
  }

  private addStep(ptSel: IPointSelection): void {
    const f = 'addStep';
    console.log(this.c, f, 'addStep');

    const card = this.cardService.getCard(this.selectionService.getSelectedCardIds());
    const step: Step = new Step(
      card.steps.length
      , StepType.STOPPED
      , ptSel.delay
      , true
    );

    step.equipment = new Equipment(
      ptSel.connAddr
      , ptSel.envlabel
      , ptSel.univname
      , ptSel.classId
      , ptSel.geo
      , ptSel.func
      , ptSel.eqplabel
      , ptSel.pointlabel
      , ptSel.initlabel
      , ptSel.valuelabel
      , ptSel.currentlabel
    );

    if ( ! step.equipment.phases ) {
      step.equipment.phases = new Array<Array<Execution>>();
      for ( const dacSimExecType in DacSimExecType ) {
        if ( ! isNaN(Number(dacSimExecType) ) ) {
          const nDacSimExecType: number = Number(DacSimExecType);
          step.equipment.phases[nDacSimExecType] = new Array<Execution>();
        }
      }
    }

    step.equipment.phases[DacSimExecType.START].push(new Execution(
      ExecType.DACSIM
      , ptSel.evname
      , ptSel.value
    ));

    step.equipment.phases[DacSimExecType.STOP].push(new Execution(
      ExecType.DACSIM
      , ptSel.evname
      , ptSel.initvalue
    ));

    this.newStep(step);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.editEnableNewStep = false;

    this.editEnableCancelStep = true;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case StepEditComponent.STR_INIT: {
        this.init();
      } break;
      case StepEditComponent.STR_CARD_RELOADED: {
        this.init();
      } break;
      case StepEditComponent.STR_CARD_SELECTED: {
        this.init();
        this.selectedCardId = this.selectionService.getSelectedCardId();
      } break;
      case StepEditComponent.STR_STEP_RELOADED: {
        this.init();
      } break;
      case StepEditComponent.STR_STEP_SELECTED: {
        this.selectedStepId = this.selectionService.getSelectedStepId();
      } break;
      case 'addcancelstep': { // User clicked cancel button
        this.init();
      } break;
    }
  }

  getNotification(evt: string): void {
    const f = 'getNotification';
    console.log(this.c, f);
    switch (evt) {
      case 'addcancelstep': { // User clicked point-select aci/dci cancel button
        this.init();
      } break;
      case 'aciselected': { // User selected point-select aci point
        this.editEnableCancelStep = false;
      } break;
      case 'dciselected': { // User selected point-select dci point
        this.editEnableCancelStep = false;
      } break;
    }
  }

  getPointSelection(ptSel: IPointSelection): void {
    const f = 'getPointSelection';
    console.log(this.c, f);
    this.addStep(ptSel);
    this.cardService.notifyUpdate(CardServiceType.STEP_RELOADED);
    this.cardService.notifyUpdate(CardServiceType.STEP_EDITED);
    this.init();
  }
}
