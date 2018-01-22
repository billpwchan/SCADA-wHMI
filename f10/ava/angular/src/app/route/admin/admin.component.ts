import { Component, OnInit, OnDestroy, ViewChild, SimpleChanges } from '@angular/core';
import { StepEditSettings } from '../../component/step/step-edit/step-edit-settings';
import { StepEditComponent } from '../../component/step/step-edit/step-edit.component';
import { MatrixComponent } from '../../component/alarm/Matrix/matrix.component';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { SettingsService } from '../../service/settings.service';
import { MatrixSettings, Matrix, MatrixConfig } from '../../component/alarm/Matrix/matrix-settings';
import { Step, Card, Equipment, Execution, PhasesType, ExecType } from '../../model/Scenario';
import { StepsComponent } from '../../component/step/steps/steps.component';
import { AlarmSummaryConfig, Env, AlarmSummarySettings } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { GetInstancesByClassNameService } from '../../service/scs/ava/dbm/get-instances-by-class-name.service';
import { Subscribable } from 'rxjs/Observable';
import { GetChildrenAliasesService } from '../../service/scs/ava/dbm/get-children-aliases.service';
import { DbmSettings } from '../../service/scs/dbm-settings';
import { MultiReadService } from '../../service/scs/ava/dbm/multi-read.service';
import { DbmService } from '../../service/scs/dbm.service';
import { ReadWriteCEService, ReadWriteCEResult } from '../../service/scs/ava/dbm/read-write-ce.service';
import { DatatableStep } from '../../model/DatatableScenario';
import { DataScenarioHelper } from '../../model/DataScenarioHelper';
import { AppSettings } from '../../app-settings';
import { MultiWriteService } from '../../service/scs/ava/dbm/multi-write.service';
import { DbmPollingService } from '../../service/scs/dbm-polling.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  readonly c: string = 'AdminComponent';

  getInstancesByClassNameSubscription: Subscription;
  getChildrenAliasSubscription: Subscription;
  multiReadSubscription: Subscription;
  readWriteCESubscription: Subscription;
  multiWriteSubscription: Subscription;
  dbmPollingSubscription: Subscription;

  notifyCards: string;
  notifyCardController: string;
  notifyCardEdit: string;

  notifyRename: string;

  notifySteps: string;
  notifyStepController: string;
  notifyStepEdit: string;
  notifyAlarmSummary: string;

  notifyStorage: string;
  notifyCsvInterpret: string;

  updateCards: Card[];
  refreshCards: Card[];
  btnEnableStateEnable: boolean;
  btnEnableStateDisable: boolean;

  btnEnableRename: boolean;
  updateNames: string[];
  updateName: string;

  updateTitle: string;

  btnEnableNewStep: boolean;
  btnEnableDeleteStep: boolean;

  updateSteps: DatatableStep[];

  updateStepEdit: Step[];

  updateAlarmEnv: string;
  updateAlarmUnivname: string[];

  alarmSummaryCfg: AlarmSummaryConfig;
  updateAlarmSummary: number;

  matrixCfg: MatrixConfig;
  updateMatrix: number;

  private env: string;
  private instanceRoot: string;

  private className: string;
  private instances: string[];
  private avar: string;
  private avasList: string[];
  private ruleList: string[];

  private cards: Card[];
  private cardIdsSelected: number[];
  private steps: Step[];
  private stepIdsSelected: number[];

  private subscriptsionStarted = false;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private getChildrenAliasService: GetChildrenAliasesService
    , private multiReadService: MultiReadService
    , private readWriteCEService: ReadWriteCEService
    , private multiWriteService: MultiWriteService
    , private dbmPollingService: DbmPollingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });

    const almConfig: AlarmSummaryConfig = this.loadAlarmSummaryCfgs();
    if ( null != almConfig ) {
      this.alarmSummaryCfg = almConfig;
    } else {
      console.warn(this.c, f, 'loadAlarmSummaryCfgs IS INVALID');
    }
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.getChildrenAliasSubscription = this.getChildrenAliasService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'getChildrenAliasSubscription', result);
      if ( null != result ) {
        if ( null != this.instanceRoot ) {
          const root: string = DbmSettings.STR_URL_ALIAS + this.instanceRoot;
          if ( root === result.dbAddress ) {

            this.avasList = new Array<string>();

            for ( let i = 0 ; i < result.data.length ; ++i ) {
              const alias = result.data[i];
              const classNames = alias.split(DbmSettings.STR_COLON);
              const className = classNames[classNames.length - 1];
              if ( className.startsWith(AlarmSummarySettings.STR_AVAR_PREFIX) ) {
                this.avar = alias;
              }
              if ( className.startsWith(AlarmSummarySettings.STR_AVAS_PREFIX) ) {
                this.avasList.push(alias);
              }
            }
            if ( null != this.avar ) {
              this.getChildrenAliasService.readData(this.env, this.avar);
            }
          }
        }
        if ( null != this.avar ) {
          if ( this.avar === result.dbAddress ) {
            if ( null == this.ruleList ) {
              this.ruleList = new Array<string>();
            }
            for ( let i = 0 ; i < result.data.length; ++i ) {
              const alias = result.data[i];
              const classNames = alias.split(DbmSettings.STR_COLON);
              const className = classNames [classNames.length - 1];
               if ( className.startsWith(AlarmSummarySettings.STR_RULE_PREFIX) ) {

                let found = false;
                const base = 1;
                for ( let n = 0; n < 16; ++n ) {
                  const name = DbmSettings.STR_RULE + (DbmSettings.STR_THREE_ZERO + (n + base)).slice(-4);
                  if ( className === name ) {
                    found = true;
                  }
                }
                if ( found ) {
                  this.ruleList.push(alias);
                }
              }
            }

            // Sorting
            // this.ruleList.sort();
            this.ruleList.sort((a, b) => {
                const A = a.toUpperCase(); // ignore upper and lowercase
                const B = b.toUpperCase(); // ignore upper and lowercase
                if (A < B) {
                  return -1;
                }
                if (A > B) {
                  return 1;
                }
                // equal
                return 0;
            });

            this.reloadCards();
          }
        }
      }
    });

    this.multiReadSubscription = this.multiReadService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'multiReadSubscription', result);
      if ( null != result ) {

        if ( 0 === result.dbValue.length ) {

          // Renew with No Conditions
          this.updateSteps = this.steps = [];

        } else if ( 0 === (result.dbValue.length % AlarmSummarySettings.RULE_ATTR_LIST.length) ) {

          this.renewCard(result.dbValue);

          if ( ! this.subscriptsionStarted ) {
            this.subscriptsionStarted = true;
            const dbaddress: string[] = this.prepareReloadCard();
            this.dbmPollingService.subscribe(this.env, dbaddress);
          }

        } else if (0 === (result.dbValue.length % 8)) {

          this.renewStep(result.dbValue);
        }
      }
    });

    this.readWriteCESubscription = this.readWriteCEService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'readWriteCESubscription', result);
      if ( null != result ) {

        if ( result.length > 0 ) {
          if ( result[0].method === 'readConditions' ) {
            const dbAddresses: string[] = new Array<string>();

            for ( let n = 0; n < result.length; ++n ) {
              const readWriteCEResult: ReadWriteCEResult = result[n];
              if ( null != readWriteCEResult ) {
                const base: string = readWriteCEResult.base;
                const targetAlias: string = readWriteCEResult.targetAlias;
                const targetValue: string = readWriteCEResult.targetValue;
                if ( null != targetAlias && null != targetValue ) {

                  const alias: string = targetAlias.toString().match(/^(.*?)\./)[1];

                  dbAddresses.push(alias + DbmSettings.STR_ATTR_NAME);
                  dbAddresses.push(alias + DbmSettings.STR_ATTR_GEO);
                  dbAddresses.push(alias + DbmSettings.STR_ATTR_FUNC);
                  dbAddresses.push(alias + DbmSettings.STR_ATTR_EQUIPMENT_LABEL);
                  dbAddresses.push(alias + DbmSettings.STR_ATTR_POINT_FUNC);
                  dbAddresses.push(alias + DbmSettings.STR_ATTR_VALUE);
                  dbAddresses.push(alias + DbmSettings.STR_COLON + DbmSettings.STR_VALUETABLE_VALUE);
                  dbAddresses.push(alias + DbmSettings.STR_COLON + DbmSettings.STR_VALUETABLE_LABEL);
                }
              }
            }

            this.multiReadService.readData(this.env, dbAddresses);
          } else if ( result[0].method === 'writeConditions' ) {

            const card: Card = this.getCardSelected();
            const index: number = card.index;
            this.readConditions(index);
          }
        }
      }
    });

    this.multiWriteSubscription = this.multiWriteService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'multiWriteSubscription', result);
      if ( null != result ) {
        this.reloadCards();
      }
    });

    this.dbmPollingSubscription = this.dbmPollingService.dbmPollingItem
    .subscribe(result => {
      console.log(this.c, f, 'dbmPollingSubscription', result);
      if ( null != result ) {
        this.refreshCard(result);
      }
    });

    this.reloadInstance();
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.getInstancesByClassNameSubscription.unsubscribe();
    this.getChildrenAliasSubscription.unsubscribe();
    this.multiReadSubscription.unsubscribe();
    this.readWriteCESubscription.unsubscribe();
    this.multiWriteSubscription.unsubscribe();
    this.dbmPollingSubscription.unsubscribe();
  }

  private renewCard(dbValue: any) {
    const f = 'renewCard';
    console.log(this.c, f);

    this.initGui();

    this.cards = [];
    this.updateCards = [];

    for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
      const base: number = i * AlarmSummarySettings.RULE_ATTR_LIST.length;
      const card: Card = new Card(
                                  dbValue[base + 0]
                                  , dbValue[base + 1]
                                  , dbValue[base + 2]
                                  , dbValue[base + 3]
                                  , dbValue[base + 4]
      );
      this.cards.push(card);
    }
    this.updateCards = this.cards;
    // this.updateCards = [...this.updateCards];
  }

  private renewStep(dbValue: any) {
    const f = 'renewStep';
    console.log(this.c, f);

    this.steps = new Array<Step>();
    for ( let i = 0; i < dbValue.length / 8; ++i ) {
      const index: number = i * 8;
      const univname: string    = dbValue[index + 0];
      const geo: number         = dbValue[index + 1];
      const func: number        = dbValue[index + 2];
      const equipment: string   = dbValue[index + 3];
      const point: string       = dbValue[index + 4];
      const value: number       = dbValue[index + 5];
      const values: number[]    = dbValue[index + 6];
      const labels: string[]    = dbValue[index + 7];

      let strLabel = '';
      for ( let  x = 0 ; x < values.length; ++x ) {
        if ( value === values[x] ) {
          strLabel = labels[x];
          break;
        }
      }

      const step: Step = new Step(index);
      step.equipment = new Equipment(
                                        this.env
                                      , univname
                                      , 0
                                      ,  geo
                                      ,  func
                                      , equipment
                                      , point
                                      , strLabel
                                    );

      step.equipment.phases = new Array<Array<Execution>>();
      for ( let n = 0 ; n < PhasesType.LENGTH ; ++n ) {
        step.equipment.phases[n] = new Array<Execution>();
      }

      step.equipment.phases[PhasesType.SINGLE_EV].push(new Execution(
        ExecType.DACSIM
        , univname
        , Number(value).valueOf()
      ));

      this.steps.push(step);
    }

    // Renew StepsComponent
    this.renewSteps();
  }

  private refreshCard(dbValue: any) {
    const f = 'refreshCard';
    console.log(this.c, f);

    if ( null != dbValue && dbValue.length > 0 ) {
    // this.initGui();

    this.cards = [];
    this.refreshCards = [];

    for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
      const base: number = i * AlarmSummarySettings.RULE_ATTR_LIST.length;
      const card: Card = new Card(
                                  dbValue[base + 0]
                                  , dbValue[base + 1]
                                  , dbValue[base + 2]
                                  , dbValue[base + 3]
                                  , dbValue[base + 4]
      );
      this.cards.push(card);
    }
    this.refreshCards = this.cards;
    // this.updateCards = [...this.updateCards];
    }
  }

  private reloadInstance() {
    const f = 'reloadInstance';
    console.log(this.c, f);
    this.env = this.alarmSummaryCfg.envs[0].value;
    this.instanceRoot = this.alarmSummaryCfg.instanceRoot;
    this.getChildrenAliasService.readData(this.env, DbmSettings.STR_URL_ALIAS + this.instanceRoot);
  }

  private prepareReloadCard(): string[] {
    const f = 'prepareReloadCard';
    console.log(this.c, f);
    const ret: string[] = new Array<string>();
    for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
      for ( let j = 0 ; j < AlarmSummarySettings.RULE_ATTR_LIST.length ; ++j ) {
        const dbAttribute = this.ruleList[i] + AlarmSummarySettings.RULE_ATTR_LIST[j];
        ret.push(dbAttribute);
      }
    }
    return ret;
  }

  private reloadCards() {
    const f = 'reloadCards';
    console.log(this.c, f);
    const dbAddresses: string[] = this.prepareReloadCard();
    this.multiReadService.readData(this.env, dbAddresses);
  }

  private getCard(cardId: number): Card {
    const f = 'getSelectedCard';
    console.log(this.c, f);
    let cardSelected: Card = null;
    for ( let i = 0 ; i < this.cards.length ; ++i ) {
      const card: Card = this.cards[i];
      if ( cardId === card.index ) {
        cardSelected = card;
        break;
      }
    }
    return cardSelected;
  }

  private getCardSelected(): Card {
    let selectedCard: Card = null;
    if ( null != this.cardIdsSelected ) {
      if ( this.cardIdsSelected.length > 0 ) {
        selectedCard = this.getCard(this.cardIdsSelected[0]);
      }
    }
    return selectedCard;
  }

  private getStep(stepId: number): Step {
    const f = 'getStep';
    console.log(this.c, f);
    let stepSelected: Step = null;
    for ( let i = 0 ; i < this.steps.length ; ++i ) {
      const step: Step = this.steps[i];
      if ( stepId === step.step ) {
        stepSelected = step;
        break;
      }
    }
    return stepSelected;
  }

  private getStepSelected(): Step {
    const f = 'getStepSelected';
    console.log(this.c, f);
    let stepSelected: Step = null;
    if ( null != this.stepIdsSelected ) {
      if ( this.stepIdsSelected.length > 0 ) {
        stepSelected = this.getStep(this.stepIdsSelected[0]);
      }
    }
    return stepSelected;
  }

  private renewSteps() {
    const f = 'renewSteps';
    console.log(this.c, f);

    const dtSteps: DatatableStep[] = new Array<DatatableStep>();
    for ( let i = 0 ; i < this.steps.length ; ++i ) {
      const step: Step = this.steps[i];
      if ( null != step ) {
        dtSteps.push(DataScenarioHelper.convertToDatatableStep(step));
      }
    }
    this.updateSteps = dtSteps;
  }

  private initGui() {
    this.btnEnableStateEnable = false;
    this.btnEnableStateDisable = false;
    this.btnEnableRename = false;
    this.updateTitle = '';
    this.btnEnableNewStep = false;
    this.btnEnableDeleteStep = false;
  }

  private readConditions(index: number) {
    const f = 'readConditions';
    console.log(this.c, f);

    const ruleBase = 1;
    const cStart = 1;
    const cEnd = 8;
    const univname: string = this.avar + DbmSettings.STR_COLON + DbmSettings.STR_RULE + ('000' + (index + ruleBase)).slice(-4);
    this.readWriteCEService.readConditions(this.env, univname, cStart, cEnd);
  }

  private writeConditions(index: number) {
    const f = 'writeConditions';
    console.log(this.c, f);

    const ruleBase = 1;
    const cStart = 1;
    const cEnd = 8;
    const univname: string = this.avar + DbmSettings.STR_COLON + DbmSettings.STR_RULE + ('000' + (index + ruleBase)).slice(-4);
    this.readWriteCEService.writeConditions(this.env, univname, cStart, cEnd, this.steps, 0);
  }

  private reloadAlarmSummary(index: number) {
    const f = 'reloadAlarmSummary';
    console.log(this.c, f);

    this.updateAlarmEnv = this.env;
    const univnameSups: string[] = new Array<string>();
    for ( let i = 0 ; i < this.avasList.length ; ++i ) {
      univnameSups.push(this.avasList[i] + DbmSettings.STR_COLON + 'avasuppression');
    }
    this.updateAlarmUnivname = univnameSups;
    this.updateAlarmSummary = index;
  }

  onUpdatedCardSelection(cardIds: number[]) {
    const f = 'onUpdatedCardSelection';
    console.log(this.c, f);

    this.cardIdsSelected = cardIds;

    const cardSelected: Card = this.getCardSelected();
    if ( null != cardSelected ) {

      // Renew State Button
      if ( cardSelected.state ) {
        this.btnEnableStateEnable = false;
        this.btnEnableStateDisable = true;
      } else {
        this.btnEnableStateEnable = true;
        this.btnEnableStateDisable = false;
      }

      // Renew Rename Button
      this.btnEnableRename = true;
      this.updateTitle = '';

      // Renew Title
      this.updateTitle = cardSelected.name;

      this.btnEnableNewStep = true;
      this.btnEnableDeleteStep = false;

      // Reload conditions
      this.readConditions(cardSelected.index);

      // Reload alarm summary
      this.reloadAlarmSummary(cardSelected.index);
    } else {

      this.initGui();
    }
  }

  onUpdatedName(name: string) {
    const f = 'onUpdatedName';
    console.log(this.c, f);

    const cardSelected = this.getCardSelected();
    cardSelected.name = name;
    this.updateCard(cardSelected);
  }

  onUpdatedStepSelection(stepIds: number[]) {
    const f = 'onUpdatedStepSelection';
    console.log(this.c, f);

    this.stepIdsSelected = stepIds;

    const stepSelected: Step = this.getStepSelected();
    if ( null != stepSelected ) {
      this.btnEnableNewStep = true;
      this.btnEnableDeleteStep = true;
    }
  }

  onUpdatedStepEdit(steps: Step[]) {
    const f = 'onUpdatedStepEdit';
    console.log(this.c, f);

    this.steps = steps;
    this.renewSteps();
  }

  onUpdatedAlarmSummary(data: number[][]): void {
    const f = 'onUpdatedAlarmSummary';
    console.log(this.c, f);
  }

  private loadAlarmSummaryCfgs(): AlarmSummaryConfig {
    const f = 'loadAlarmSummaryCfgs';
    console.log(this.c, f);

    const c = 'AlarmSummaryComponent';
    const cfg: AlarmSummaryConfig = new AlarmSummaryConfig();

    cfg.envs = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_ENVS) as Env[];
    cfg.instanceClassName = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_INSTANCE_CLASSNAME) as string;
    cfg.instanceRoot = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_INSTANCE_ROOT) as string;

    return cfg;
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.updateAlarmSummary = null;
  }

  getNotification(evt: string): void {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);

    switch ( evt ) {
    }
  }

  private updateCard(card: Card) {
    const dbAddresses: string[] = new Array<string>();

    const values = {};
    const univname = card.univname;
    values[DbmSettings.STR_URL_ALIAS + univname + AlarmSummarySettings.STR_RULE_NAME] = card.name;
    values[DbmSettings.STR_URL_ALIAS + univname + AlarmSummarySettings.STR_RULE_ENABLE] = (card.state ? 1 : 0);

    this.multiWriteService.writeData(this.env, values);
  }

  private updateCardState(card: Card, state: boolean) {
    if ( null != card ) {
      card.state = state;
    }
  }

  private deleteStep(steps: Step[], stepIds: number[]): void {
    const f = 'deleteStep';
    console.log(this.c, f);
    if ( null != steps ) {
      for ( let x = 0 ; x < stepIds.length ; ++x ) {
        for ( let y = 0 ; y < steps.length ; ++y ) {
          const step: Step = steps[y];
          if ( step.step === stepIds[x] ) {
            steps.splice(y, 1);
          }
        }
      }
    } else {
      console.warn(this.c, f, 'steps IS NULL');
    }
  }

  btnClicked(btnLabel: string, event?: Event): void {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case AdminComponent.STR_INIT: {
      } break;
      case 'enable': {
        const cardSelected = this.getCardSelected();
        this.updateCardState(cardSelected, true);
        this.updateCard(cardSelected);
      } break;
      case 'disable': {
        const cardSelected = this.getCardSelected();
        this.updateCardState(cardSelected, false);
        this.updateCard(cardSelected);
      } break;
      case 'rename': {
        const names: string[] = new Array<string>();
        for ( let i = 0 ; i < this.cards.length ; ++i ) {
          const card = this.cards[i];
          names.push(card.name);
        }
        this.updateNames = names;

        const cardSelected = this.getCardSelected();
        this.updateName = cardSelected.name;
      } break;
      case 'newstep': {
        const cardSelected: Card = this.getCardSelected();
        const stepSelected: Step = this.getStepSelected();
        this.updateStepEdit = this.steps;
        this.writeConditions(cardSelected.index);
      } break;
      case 'deletestep': {
        const cardSelected: Card = this.getCardSelected();
        const stepSelected: Step = this.getStepSelected();
        this.deleteStep(this.steps, this.stepIdsSelected);
        this.writeConditions(cardSelected.index);
      } break;
    }
  }

}
