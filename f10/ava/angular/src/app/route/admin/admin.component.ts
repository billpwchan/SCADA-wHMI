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
import { GetInstancesByClassNameService } from '../../service/scs/ava/dbm/get-instances-by-class-name.service';
import { Subscribable } from 'rxjs/Observable';
import { GetChildrenAliasesService } from '../../service/scs/ava/dbm/get-children-aliases.service';
import { DbmSettings } from '../../service/scs/dbm-settings';
import { MultiReadService, MultiReadResult } from '../../service/scs/ava/dbm/multi-read.service';
import { DbmService } from '../../service/scs/dbm.service';
import { ReadWriteCEService, ReadWriteCEResult } from '../../service/scs/ava/dbm/read-write-ce.service';
import { DatatableStep } from '../../model/DatatableScenario';
import { DataScenarioHelper } from '../../model/DataScenarioHelper';
import { AppSettings } from '../../app-settings';
import { MultiWriteService } from '../../service/scs/ava/dbm/multi-write.service';
import { DbmPollingService } from '../../service/scs/dbm-polling.service';
import { AlarmSummarySettings, Env } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { AVASummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { CardSummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { StepSummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { AlarmSummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { StepSettings } from '../../component/step/steps/step-settings';

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

  btnEnableRefresh: boolean;

  updateCards: Card[];
  refreshCards: Card[];
  btnEnableStateEnable: boolean;
  btnEnableStateDisable: boolean;

  btnEnableRename: boolean;
  updateNames: string[];
  updateName: string;
  renameEnable: Date;

  updateTitle: string;

  btnEnableNewStep: boolean;
  btnEnableDeleteStep: boolean;

  updateSteps: DatatableStep[];

  updateStepEdit: Step[];
  enableStepEdit: Date;

  updateAlarmEnv: string;
  updateAlarmUnivname: string[];

  avaSummaryCfg: AVASummaryConfig;
  cardSummaryCfg: CardSummaryConfig;
  stepSummaryCfg: StepSummaryConfig;
  alarmSummaryCfg: AlarmSummaryConfig;
  updateAlarmSummary: number;

  matrixCfg: MatrixConfig;
  updateMatrix: number;

  private env: string;
  private instanceRoot: string;

  private className: string;
  private instances: string[];
  private avarAlias: string;
  private avasAliasList: string[];
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

    this.avaSummaryCfg = this.loadAVASummaryCfgs();
    if ( null == this.avaSummaryCfg ) {
      console.warn(this.c, f, 'loadAVASummaryCfgs IS INVALID');
    }

    this.cardSummaryCfg = this.loadCardSummaryCfgs();
    if ( null == this.cardSummaryCfg ) {
      console.warn(this.c, f, 'loadCardSummaryCfgs IS INVALID');
    }

    this.stepSummaryCfg = this.loadStepSummaryCfgs();
    if ( null == this.stepSummaryCfg ) {
      console.warn(this.c, f, 'loadStepSummaryCfgs IS INVALID');
    }

    this.alarmSummaryCfg = this.loadAlarmSummaryCfgs();
    if ( null == this.alarmSummaryCfg ) {
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

            this.avasAliasList = new Array<string>();

            for ( let i = 0 ; i < result.data.length ; ++i ) {
              const alias = result.data[i];
              const classNames = alias.split(DbmSettings.STR_COLON);
              const className = classNames[classNames.length - 1];
              if ( className.startsWith(AlarmSummarySettings.STR_AVAR_PREFIX) ) {
                this.avarAlias = alias;
              }

              let found = false;
              const base = this.alarmSummaryCfg.avarBase;
              const max = this.alarmSummaryCfg.maxAvarNum;
              for ( let n = 0; n < max ; ++n ) {
                const name = AlarmSummarySettings.STR_AVAR_PREFIX + (DbmSettings.STR_THREE_ZERO + (n + base)).slice(-4);
                if ( className === name ) {
                  found = true;
                }
              }
              if ( found ) {
                this.avasAliasList.push(alias);
              }
              // if ( className.startsWith(AlarmSummarySettings.STR_AVAS_PREFIX) ) {
              //   this.avasAliasList.push(alias);
              // }
            }
            if ( null != this.avarAlias ) {
              this.getChildrenAliasService.readData(this.env, this.avarAlias);
            }
          }
        }
        if ( null != this.avarAlias ) {
          if ( this.avarAlias === result.dbAddress ) {
            if ( null == this.ruleList ) {
              this.ruleList = new Array<string>();
            }
            for ( let i = 0 ; i < result.data.length; ++i ) {
              const alias = result.data[i];
              const classNames = alias.split(DbmSettings.STR_COLON);
              const className = classNames [classNames.length - 1];
               if ( className.startsWith(AlarmSummarySettings.STR_RULE_PREFIX) ) {

                let found = false;
                const base = this.cardSummaryCfg.ruleBase;
                const max = this.cardSummaryCfg.maxRuleNum;
                for ( let n = 0; n < max ; ++n ) {
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

            this.reloadCard();
          }
        }
      }
    });

    this.multiReadSubscription = this.multiReadService.dbmItem
    .subscribe( (result: MultiReadResult) => {
      console.log(this.c, f, 'multiReadSubscription', result);
      if ( null != result ) {

        if ( 0 === result.dbValue.length ) {

          // Renew with No Conditions
          this.updateSteps = this.steps = [];

        } else if ( 'readCard' === result.method ) {

          this.renewCard(result.dbValue);

          if ( ! this.subscriptsionStarted ) {
            this.subscriptsionStarted = true;
            const dbaddress: string[] = this.prepareReloadCard();
            this.dbmPollingService.subscribe(this.env, dbaddress);
          }

        } else if ( 'readStep' === result.method ) {

          this.renewStep(result);
        }
      }
    });

    this.readWriteCESubscription = this.readWriteCEService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'readWriteCESubscription', result);
      if ( null != result ) {

        if ( result.length > 0 ) {
          if ( result[0].method === 'readConditions' ) {

            this.readStep(result);

          } else if ( result[0].method === 'writeConditions' ) {

            const card: Card = this.getCardSelected();
            const index: number = card.index;
            this.readConditions(this.env, index);
          }
        }
      }
    });

    this.multiWriteSubscription = this.multiWriteService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'multiWriteSubscription', result);
      if ( null != result ) {
        this.reloadCard();
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

  private readStep(result) {
    const f = 'readStep';
    console.log(this.c, f);

    const dbAddresses: string[] = new Array<string>();

    for ( let n = 0; n < result.length; ++n ) {
      const readWriteCEResult: ReadWriteCEResult = result[n];
      if ( null != readWriteCEResult ) {
        const base: string = readWriteCEResult.base;
        const targetFullpath: string = readWriteCEResult.fullpath;
        const targetValue: number = readWriteCEResult.value;
        if ( null != targetFullpath && null != targetValue ) {

          const fullPath: string = targetFullpath.toString().match(/^(.*?)\./)[1];

          for ( let  i = 0 ; i < StepSettings.STR_READ_STEP_ATTR_LIST.length ; ++i ) {
            dbAddresses.push(fullPath + StepSettings.STR_READ_STEP_ATTR_LIST[i]);
          }
        }
      }
    }

    this.multiReadService.readData(this.env, dbAddresses, 'readStep');
  }

  private renewCard(dbValue: any) {
    const f = 'renewCard';
    console.log(this.c, f);

    this.initGui();

    this.cards = [];
    this.updateCards = [];

    for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
      const base: number = i * AlarmSummarySettings.RULE_ATTR_LIST.length;
      let index = 0;
      const card: Card = new Card(
                                  dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
      );
      this.cards.push(card);
    }
    this.updateCards = this.cards;
    // this.updateCards = [...this.updateCards];
  }

  private renewStep(result: MultiReadResult) {
    const f = 'renewStep';
    console.log(this.c, f);

    const conditions: Map<string, number> = this.readWriteCEService.getConditions(result.env);

    const dbValue = result.dbValue;
    this.steps = new Array<Step>();
    for ( let i = 0; i < dbValue.length / StepSettings.STR_READ_STEP_ATTR_LIST.length; ++i ) {
      const base: number = i * StepSettings.STR_READ_STEP_ATTR_LIST.length;
      let index = 0;
      const univname: string    = dbValue[base + index++];
      const fullpath: string    = dbValue[base + index++];
      const geo: number         = dbValue[base + index++];
      const func: number        = dbValue[base + index++];
      const equipment: string   = dbValue[base + index++];
      const point: string       = dbValue[base + index++];
      const value: number       = dbValue[base + index++];
      const values: number[]    = dbValue[base + index++];
      const labels: string[]    = dbValue[base + index++];

      const targetFullPath: string = fullpath + DbmSettings.STR_ATTR_VALUE;
      const targetValue: number = conditions.get(targetFullPath);

      let strLabel = '';
      for ( let  x = 0 ; x < values.length; ++x ) {
        if ( targetValue === values[x] ) {
          strLabel = labels[x];
          break;
        }
      }

      const step: Step = new Step(base);
      step.equipment = new Equipment(
                                        this.env
                                      , univname
                                      , fullpath
                                      , 0
                                      ,  geo
                                      ,  func
                                      , equipment
                                      , point
                                      , Number(targetValue).valueOf()
                                      , strLabel
                                    );

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
                                  , dbValue[base + 5]
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
    this.env = this.avaSummaryCfg.envs[0].value;
    this.instanceRoot = this.cardSummaryCfg.instanceRoot;
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

  private reloadCard() {
    const f = 'reloadCard';
    console.log(this.c, f);
    const dbAddresses: string[] = this.prepareReloadCard();
    this.multiReadService.readData(this.env, dbAddresses, 'readCard');
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
    const f = 'initGui';
    console.log(this.c, f);

    this.btnEnableRefresh = true;

    this.btnEnableStateEnable = false;
    this.btnEnableStateDisable = false;

    this.btnEnableRename = false;

    this.updateTitle = '';

    this.btnEnableNewStep = false;
    this.btnEnableDeleteStep = false;
  }

  private readConditions(env: string, index: number) {
    const f = 'readConditions';
    console.log(this.c, f);

    const ruleBase = this.stepSummaryCfg.ruleBase;
    const cStart = this.stepSummaryCfg.conditionBeginId;
    const cEnd = this.stepSummaryCfg.conditionEndId;
    const alias: string =
                              this.avarAlias + DbmSettings.STR_COLON
                              + DbmSettings.STR_RULE + (DbmSettings.STR_THREE_ZERO + (index + ruleBase)).slice(-4);
    this.readWriteCEService.readConditions(env, alias, cStart, cEnd);
  }

  private writeConditions(env: string, steps: Step[], index: number) {
    const f = 'writeConditions';
    console.log(this.c, f);

    const ruleBase = this.stepSummaryCfg.ruleBase;
    const cStart = this.stepSummaryCfg.conditionBeginId;
    const cEnd = this.stepSummaryCfg.conditionEndId;
    const formulaDefaultVal: number = this.stepSummaryCfg.formulaDefaultVal;
    const formulaZeroDefaultVal: number = this.stepSummaryCfg.formulaZeroDefaultVal;
    const alias: string =
                              this.avarAlias + DbmSettings.STR_COLON
                              + DbmSettings.STR_RULE + (DbmSettings.STR_THREE_ZERO + (index + ruleBase)).slice(-4);
    this.readWriteCEService.writeConditions(env, alias, cStart, cEnd, steps, formulaDefaultVal, formulaZeroDefaultVal);
  }

  private reloadAlarmSummary(index: number) {
    const f = 'reloadAlarmSummary';
    console.log(this.c, f);

    this.updateAlarmEnv = this.env;
    const supsAlias: string[] = new Array<string>();
    for ( let i = 0 ; i < this.avasAliasList.length ; ++i ) {
      supsAlias.push(this.avasAliasList[i] + DbmSettings.STR_COLON + 'avasuppression');
    }
    this.updateAlarmUnivname = supsAlias;
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
      this.readConditions(this.env, cardSelected.index);

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
    const cardSelected: Card = this.getCardSelected();
    this.writeConditions(this.env, this.steps, cardSelected.index);
  }

  onUpdatedAlarmSummary(data: number[][]): void {
    const f = 'onUpdatedAlarmSummary';
    console.log(this.c, f);
  }

  private loadAVASummaryCfgs(): AVASummaryConfig {
    const f = 'loadAVASummaryCfgs';
    console.log(this.c, f);

    const c = 'AVASummaryComponent';
    const cfg: AVASummaryConfig = new AVASummaryConfig();

    cfg.envs = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_ENVS) as Env[];

    return cfg;
  }

  private loadCardSummaryCfgs(): CardSummaryConfig {
    const f = 'loadCardSummaryCfgs';
    console.log(this.c, f);

    const c = 'CardSummaryComponent';
    const cfg: CardSummaryConfig = new CardSummaryConfig();

    cfg.instanceClassName = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_INSTANCE_CLASSNAME) as string;
    cfg.instanceRoot = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_INSTANCE_ROOT) as string;
    cfg.ruleBase = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_RULE_BASE) as number;
    cfg.maxRuleNum = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_MAX_RULE_NUM) as number;
    return cfg;
  }

  private loadStepSummaryCfgs(): StepSummaryConfig {
    const f = 'loadStepSummaryCfgs';
    console.log(this.c, f);

    const c = 'StepSummaryComponent';
    const cfg: StepSummaryConfig = new StepSummaryConfig();

    cfg.ruleBase = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_RULE_BASE) as number;
    cfg.conditionBeginId = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_CONDITION_BEGIN_ID) as number;
    cfg.conditionEndId = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_CONDITION_END_ID) as number;
    cfg.formulaDefaultVal = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_FORMULA_DEF_VAL) as number;
    cfg.formulaZeroDefaultVal = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_FORMULA_ZERO_DEF_VAL) as number;
    return cfg;
  }

  private loadAlarmSummaryCfgs(): AlarmSummaryConfig {
    const f = 'loadAlarmSummaryCfgs';
    console.log(this.c, f);

    const c = 'AlarmSummaryComponent';
    const cfg: AlarmSummaryConfig = new AlarmSummaryConfig();

    cfg.avarBase = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_AVAR_BASE) as number;
    cfg.maxAvarNum = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_MAX_AVAR_NUM) as number;

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
    values[DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_ATTR_LABEL] = card.name;
    values[DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_ATTR_ENABLE] = (card.state ? 1 : 0);

    this.multiWriteService.writeData(this.env, values);
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
        if ( null != cardSelected ) {
          cardSelected.state = true;
          this.updateCard(cardSelected);
        }
      } break;
      case 'disable': {
        const cardSelected = this.getCardSelected();
        if ( null != cardSelected ) {
          cardSelected.state = false;
          this.updateCard(cardSelected);
        }
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
        this.renameEnable = new Date();
      } break;
      case 'newstep': {
        const cardSelected: Card = this.getCardSelected();
        const stepSelected: Step = this.getStepSelected();
        this.updateStepEdit = this.steps;
        this.enableStepEdit = new Date();
      } break;
      case 'deletestep': {
        const cardSelected: Card = this.getCardSelected();
        const stepSelected: Step = this.getStepSelected();
        this.deleteStep(this.steps, this.stepIdsSelected);
        this.writeConditions(this.env, this.steps, cardSelected.index);
      } break;
      case 'refresh': {
        window.location.reload();
      } break;
    }
  }

}
