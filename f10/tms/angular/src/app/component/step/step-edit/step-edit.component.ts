import { Component, OnInit, OnDestroy, OnChanges, Output, EventEmitter, SimpleChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { DatatableStep } from '../../../model/DatatableScenario';
import { Card, Step, Equipment, Execution, StepType, ExecType } from '../../../model/Scenario';
import { AppSettings } from '../../../app-settings';
import { StepEditSettings } from './step-edit-settings';
import { OlsSettings } from '../../../service//scs/ols-settings';
import { DbmSettings } from '../../../service/scs/dbm-settings';
import { OlsService } from '../../../service/scs/ols.service';
import { DbmService } from '../../../service/scs/dbm.service';
import { Subscription } from 'rxjs/Subscription';
import { DacSimSettings } from '../../../service/scs/dac-sim-settings';
import { Subscribable } from 'rxjs/Observable';
import { SelectionService } from '../../../service/card/selection.service';
import { StepSettings } from '../steps/step-settings';
import { SettingsService } from '../../../service/settings.service';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';

class SelOptStr {
  constructor(
    public label: string
    , public value: string
  ) {}
}

class SelOptNum {
  constructor(
    public label: string
    , public value: number
  ) {}
}

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

  public static readonly STR_ACI_SELECTED   = 'aciselected';
  public static readonly STR_DCI_SELECTED   = 'dciselected';

  public static readonly STR_NEWSTEP = 'newstep';

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = StepEditComponent.name;

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

  btnDisabledAddAciStep: boolean;
  btnDisabledAddDciStep: boolean;

  btnDisabledAddAciCancelStep: boolean;
  btnDisabledAddDciCancelStep: boolean;

  hiddenClassId: boolean;
  hiddenUnivname: boolean;
  hiddenEVName: boolean;

  hiddenAciInitValue: boolean;
  hiddenDciInitValue: boolean;

  selEnv: string;
  selOptEnv: Array<SelOptStr> = new Array<SelOptStr>();

  selGeo: number;
  selOptGeo: Array<SelOptNum> = new Array<SelOptNum>();

  selFunc: number;
  selOptFunc: Array<SelOptNum> = new Array<SelOptNum>();

  selEqpLabel: string;
  selOptEqpLabel: Array<SelOptStr> = new Array<SelOptStr>();

  selPointLabel: string;
  selOptPointLabel: Array<SelOptStr> = new Array<SelOptStr>();

  // data binding

  txtClassId: number;
  txtUnivname: string;
  txtEVName: string;

editEnableCancelStep: boolean;

// Step - Form Edit
editEnableAddAciStep: boolean;

  // data binding
  aciInitValue: number;
  aciValue: number;

editEnableAddDciStep: boolean;

  // data binding
  dciInitValue: number;
  dciValue: number;

  selDciValue: number;
  selOptDciValue: Array<SelOptNum> = new Array<SelOptNum>();

editEnableDelay = false;

  selDelay: number;
  selOptDelay: Array<SelOptNum> = new Array<SelOptNum>();

btnDisabledAddCancelStep: boolean;

  private geoPrefix: string;
  private funcPrefix: string;
  private envs: any;

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
    , private olsService: OlsService
    , private dbmService: DbmService
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

    this.olsSubscription = this.olsService.olsItem
      .subscribe(item => {
        console.log(this.c, f, 'olsSubscription', item);
        const eqps: Map<string, Map<number, number[]>> = this.olsService.getEqps();
        if ( undefined !== eqps && null != eqps ) {
          if ( eqps.size > 0 ) {
            this.olsService.getEqps().get(this.selEnv)
              .forEach((value: number[], key: number) => {
                this.selOptGeo.push(
                  new SelOptNum(
                    this.translate.instant(this.geoPrefix + key)
                    , key
                  )
                );
              });
          }
        }
    });

    this.dbmSubscription = this.dbmService.dbmItem
      .subscribe(item => {
        console.log(this.c, f, 'dbmSubscription', item);

        if ( 'retriveClassId' === item ) {
          this.txtClassId = this.dbmService.getRetriveClassIdData(this.selEnv, this.txtUnivname);
        } else if ( 'retriveAcquiredDataAttributeFormulas' === item ) {
          const formulas: string = this.dbmService.getRetriveAcquiredDataAttributeFormulasData(this.selEnv, this.txtUnivname);
          if ( null != formulas && formulas.length > 0 ) {
            // Is acquired data equipment
            if ( DbmSettings.STR_FORMULAS_ACQ_SINGLE === formulas[0] ) {
              if ( DbmSettings.INT_ACI_TYPE === this.txtClassId) {

                this.btnClicked(StepEditComponent.STR_ACI_SELECTED);
              } else if ( DbmSettings.INT_DCI_TYPE === this.txtClassId ) {

                this.btnClicked(StepEditComponent.STR_DCI_SELECTED);
              }
            } else {
              // Prompt the user this is not a single acquired data equipment
              const msg = 'Selected Equipment point is not a single acquired data equipment (Maybe a combine points)';
              console.log(this.c, f, msg);
            }
          } else {
            // Prompt the user this is not a acquired data equipment
            const msg = 'Selected Equipment point is not an acquired data equipment (Without AAC/DAC)';
            console.log(this.c, f, msg);
          }
        } else if ( 'retriveDci' === item ) {
          const dbvalue = this.dbmService.getRetriveDciData(this.selEnv, this.txtUnivname);
          const evname: string = dbvalue[0][0];
          const initValue: number = dbvalue[1];
          const labels: Array<string> = dbvalue[2];
          const values: Array<number> = dbvalue[3];

          if ( null != evname ) {
            this.txtEVName = evname;
          }

          if ( null != labels && null != values ) {

            this.initSelOptDciValue();

            for ( let i = 0 ; i < labels.length ; ++i ) {
              if ( labels[i].length > 0 ) {
                this.selOptDciValue.push(
                  new SelOptNum(
                    this.translate.instant(labels[i])
                    , values[i]
                  )
                );
              }
            }
          }

          if ( null != initValue ) {
            this.dciInitValue = initValue;
          }
        } else if ( 'retriveAci' === item ) {
          const dbvalue = this.dbmService.getRetriveAciData(this.selEnv, this.txtUnivname);
          const evname: string = dbvalue[0][0];
          if ( null != evname ) {
            this.txtEVName = evname;
          }

          const initValue: number = dbvalue[1];
          if ( null != initValue ) {
            this.aciInitValue = initValue;
            this.aciValue = 0;
          }
        }
      });

    this.btnClicked('init');
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.olsSubscription.unsubscribe();
    this.dbmSubscription.unsubscribe();
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

    const component = StepEditComponent.name;
    this.geoPrefix = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_GEO_PREFIX);
    this.funcPrefix = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_FUNC_PREFIX);
    this.envs = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_ENVS);

    this.delayRangeStep = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_DELAY_RANGE_STEP);
    this.delayRangeStart = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_DELAY_RANGE_START);
    this.delayRangeEnd = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_DELAY_RANGE_END);
    this.delayRangePrefix = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_DELAY_RANGE_PREFIX);

    this.delayDefaultValue = this.settingsService.getSetting(this.c, f, component, StepEditSettings.STR_DELAY_DEFAULT_VALUE);
  }

  private initSelOptEnv(): void {
    const f = 'initSelOptEnv';
    console.log(this.c, f);

    this.selOptEnv.length = 0;
    this.selOptEnv.push(new SelOptStr(
      this.translate.instant('&step_lst_env_default')
      , ''
    ));

    console.log(this.c, f, 'this.envs', this.envs);
    if (  this.envs ) {
      this.envs.forEach((item, index) => {
        this.selOptEnv.push(
          new SelOptStr(
            item[StepEditSettings.STR_LABEL]
            , item[StepEditSettings.STR_VALUE]
          )
        );
      });
    }

    this.selEnv = '';
  }

  private initSelOptGeo() {
    this.selOptGeo.length = 0;
    this.selOptGeo.push(new SelOptNum(
      this.translate.instant('&step_lst_geo_default')
      , -1
    ));
    this.selGeo = -1;
  }

  private initSelOptFunc() {
    this.selOptFunc.length = 0;
    this.selOptFunc.push(new SelOptNum(
      this.translate.instant('&step_lst_func_default')
      , -1
    ));
    this.selFunc = -1;
  }

  private initSelOptEqpLabel() {
    this.selOptEqpLabel.length = 0;
    this.selOptEqpLabel.push(new SelOptStr(
      this.translate.instant('&step_lst_eqplabel_default')
      , ''
    ));
    this.selEqpLabel = '';
  }

  private initSelOptPointLabel() {
    this.selOptPointLabel.length = 0;
    this.selOptPointLabel.push(new SelOptStr(
      this.translate.instant('&step_lst_pointlabel_default')
      , ''
    ));
    this.selPointLabel = '';
  }

  private initSelOptDelay() {
    this.selOptDelay.length = 0;
    this.selOptDelay.push(
      new SelOptNum(
        this.translate.instant('&step_lst_delay_placeholder')
        , 0
      )
    );

    for ( let i = this.delayRangeStart
      ; i <= this.delayRangeEnd
      ; i += this.delayRangeStep ) {
      this.selOptDelay.push(
        new SelOptNum(
          this.delayRangePrefix + i
          , i
        )
      );
    }

    this.selDelay = this.delayDefaultValue;
  }

  private initSelOptDciValue(): void {
    this.selOptDciValue.length = 0;
    this.selOptDciValue.push(new SelOptNum(
      this.translate.instant('&step_lst_dcivalue_placeholder')
      , -1
    ));
    this.selDciValue = -1;
  }

  onLstChange(name: string, event) {
    const f = 'onLstChange';
    console.log(this.c, f, 'name[' + name + '] event[' + event + ']');

    switch (name) {
      case 'selEnv': {
        // this.selectEnv = event.target.value;
        console.log(this.c, f, 'selEnv this.selEnv[' + this.selEnv + ']');
        // console.log(this.c, f, 'selEnv this.selectEnv['+this.selectEnv+']');
        this.initSelOptGeo();
        this.initSelOptFunc();
        this.olsService.retriveEquipments(this.selEnv);
      } break;
      case 'selGeo': {

        this.initSelOptFunc();
        console.log(this.c, f, 'this.selEnv[' + this.selEnv + '] this.selGeo[' + this.selGeo + ']');
        const eqps: Map<string, Map<number, number[]>> = this.olsService.getEqps();
        eqps.get(this.selEnv).get(this.selGeo)
        .forEach((value, index) => {
          this.selOptFunc.push(new  SelOptNum(
            this.funcPrefix + value
            , value
          ));
        });
      } break;
      case 'selFunc': {

        this.initSelOptEqpLabel();
        const pointData = this.olsService.getPointData();
        pointData.get(this.selEnv).forEach((item1, index) => {
        // console.log(this.c, f, 'item', item);

          if (
              item1[OlsSettings.STR_ATTR_GEO] === this.selGeo
              && item1[OlsSettings.STR_ATTR_FUNC] === this.selFunc ) {

            const eqplabel = item1[OlsSettings.STR_ATTR_EQPLABEL];
            // console.log(this.c, f, 'Added', eqplabel);

            let found = false;
            this.selOptEqpLabel.forEach( item2 => {
              if ( item2.value === eqplabel ) {
                found = true;
                return;
              }
            } );

            if ( !found ) {
              this.selOptEqpLabel.push(
                new SelOptStr(
                  eqplabel
                  , eqplabel
              ));
            }
          }
        });

      } break;
      case 'selEqpLabel': {

        // console.log(this.c, f, 'item',item);
        this.initSelOptPointLabel();
        const pointData = this.olsService.getPointData();
        pointData.get(this.selEnv).forEach((item1, index) => {

          if (
            item1[OlsSettings.STR_ATTR_GEO] === this.selGeo
            && item1[OlsSettings.STR_ATTR_FUNC] === this.selFunc
            && item1[OlsSettings.STR_ATTR_EQPLABEL] === this.selEqpLabel ) {

            const pointlabel = item1[OlsSettings.STR_ATTR_POINTLABEL];

            let found = false;
            this.selOptPointLabel.forEach( item2 => {
              if ( item2.value === pointlabel ) {
                found = true;
                return;
              }
            } );

            if ( !found ) {
              this.selOptPointLabel.push(
                new SelOptStr(
                  pointlabel
                  , pointlabel
              ));
            }
          }

        });
      } break;
      case 'selPointLabel': {
        console.log(this.c, f, 'this.selEnv[' + this.selEnv + '] this.selGeo[' + this.selGeo + '] this.selFunc[' + this.selFunc + ']');
        console.log('this.selEqpLabel[' + this.selEqpLabel + '] this.selPointLabel[' + this.selPointLabel + ']');
        const pointData = this.olsService.getPointData();
        pointData.get(this.selEnv).forEach((item, index) => {

          if (
            item[OlsSettings.STR_ATTR_GEO] === this.selGeo
            && item[OlsSettings.STR_ATTR_FUNC] === this.selFunc
            && item[OlsSettings.STR_ATTR_EQPLABEL] === this.selEqpLabel
            && item[OlsSettings.STR_ATTR_POINTLABEL] === this.selPointLabel
            ) {

            this.txtUnivname = item[OlsSettings.STR_ATTR_UNIVNAME];

            this.dbmService.retriveClassId(this.selEnv, this.txtUnivname);
          }
        });
      }
      break;

      case 'selDciValue': {
        if ( this.selDciValue !== -1 ) {
          this.btnDisabledAddDciStep = false;
        } else {
          this.btnDisabledAddDciStep = true;
        }
      } break;

    }
  }

  onChange(name: string, event?: Event): void {
    const f = 'onChange';
    console.log(this.c, f);
    if ( name === 'aciValue' ) {
      try {
        if ( +this.aciValue !== NaN ) {
          this.btnDisabledAddAciStep = false;
        } else {
          this.btnDisabledAddAciStep = true;
        }
      } catch (e) {
        console.log(this.c, f, e);
      }
    }
  }

  private newStep(step: Step): void {
    const f = 'newStep';
    console.log(this.c, f);
    const card = this.cardService.getCard([this.selectedCardId]);
    if ( null != card ) {
      console.log(this.c, f, 'card.name', card.name);
      card.steps.push(step);
    } else {
      console.log(this.c, f, 'card IS NULL');
    }
  }

  private addStep(): void {
    const f = 'addStep';
    console.log(this.c, f, 'addStep');

    let initValue: number;
    let value: number;
    let valueLabel: string;
    if ( DbmSettings.INT_ACI_TYPE === this.txtClassId ) {
      initValue = this.aciInitValue;
      value = this.aciValue;
      valueLabel = '' + value;
    } else if (DbmSettings.INT_DCI_TYPE === this.txtClassId ) {
      initValue = this.dciInitValue;
      value = this.selDciValue;
      for ( let i = 0 ; i < this.selOptDciValue.length ; ++i ) {
        if ( this.selOptDciValue[i].value === value ) {
          valueLabel = this.selOptDciValue[i].label;
          break;
        }
      }
    }

    const card = this.cardService.getCard(this.selectionService.getSelectedCardIds());
    const step: Step = new Step(
      card.steps.length
      , StepType.STOPPED
      , Number(this.selDelay).valueOf()
      , new Equipment(
        this.selEnv
        , this.txtUnivname
        , Number(this.txtClassId).valueOf()
        , Number(this.selGeo).valueOf()
        , Number(this.selFunc).valueOf()
        , this.selEqpLabel
        , this.selPointLabel
        , valueLabel
      )
    );

    step.equipment.phaseStop.push(new Execution(
      ExecType.DACSIM
      , this.txtEVName
      , Number(initValue).valueOf()
    ));

    step.equipment.phaseStart.push(new Execution(
      ExecType.DACSIM
      , this.txtEVName
      , Number(value).valueOf()
    ));

    this.newStep(step);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.editEnableNewStep = false;

    this.editEnableCancelStep = true;

    this.editEnableAddAciStep = false;
    this.editEnableAddDciStep = false;

    this.btnDisabledAddAciStep = true;
    this.btnDisabledAddDciStep = true;
    this.btnDisabledAddAciCancelStep = false;
    this.btnDisabledAddDciCancelStep = false;

    this.hiddenClassId = true;
    this.hiddenUnivname = true;
    this.hiddenEVName = true;

    this.hiddenAciInitValue = true;
    this.hiddenDciInitValue = true;

    this.initSelOptEnv();
    this.initSelOptGeo();
    this.initSelOptFunc();
    this.initSelOptEqpLabel();
    this.initSelOptPointLabel();
    this.initSelOptDelay();
    this.initSelOptDciValue();

    this.txtClassId = 0;
    this.txtUnivname = '';
    this.txtEVName = '';

    this.aciInitValue = DacSimSettings.STR_ACI_DEFAULT_VALUE;
    this.aciValue = DacSimSettings.STR_ACI_DEFAULT_VALUE;
    this.dciInitValue = DacSimSettings.STR_DCI_DEFAULT_VALUE;
    this.dciValue = DacSimSettings.STR_DCI_DEFAULT_VALUE;
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
      case StepEditComponent.STR_ACI_SELECTED: {
        this.editEnableCancelStep = false;
        this.editEnableAddAciStep = true;
        this.editEnableAddDciStep = false;
        this.editEnableDelay = true;
        this.dbmService.retriveAci(this.selEnv, this.txtUnivname);
      } break;
      case StepEditComponent.STR_DCI_SELECTED: {
        this.editEnableCancelStep = false;
        this.editEnableAddAciStep = false;
        this.editEnableAddDciStep = true;
        this.editEnableDelay = true;
        this.dbmService.retriveDci(this.selEnv, this.txtUnivname);
      } break;
      case 'addcancelstep': {
        this.init();
      } break;
      case 'addacistep': {
        this.addStep();
        this.cardService.notifyUpdate(CardServiceType.STEP_RELOADED);
        this.init();
      } break;
      case 'adddcistep': {
        this.addStep();
        this.cardService.notifyUpdate(CardServiceType.STEP_RELOADED);
        this.init();
      } break;
      case 'addacicancelstep': {
        this.init();
      } break;
      case 'adddcicancelstep': {
        this.init();
      } break;
    }
    // this.sendNotifyParent(btnLabel);
  }
}
