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
import { ReadWriteCEService, ReadWriteCEResult } from '../../service/scs/ava/dbm/read-write-ce.service';
import { DatatableStep } from '../../model/DatatableScenario';
import { DataScenarioHelper } from '../../model/DataScenarioHelper';
import { AppSettings } from '../../app-settings';
import { DbmPollingService } from '../../service/scs/dbm-polling.service';
import { AlarmSummarySettings, Env } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { AVASummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { CardSummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { StepSummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { AlarmSummaryConfig } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { StepsSettings } from '../../component/step/steps/step-settings';
import { DbmMultiReadAttrService } from '../../service/scadagen/dbm/dbm-multi-read-attr.service';
import { DbmSettings } from '../../service/scadagen/dbm/dbm-settings';
import { HttpAccessResult, HttpAccessResultType } from '../../service/scadagen/access/http/Access-interface';
import { DbmMultiWriteAttrService } from '../../service/scadagen/dbm/dbm-multi-write-attr.service';
import { CardsSettings } from '../../component/card/cards/cards-settings';
import { StepSummarySettings } from '../../component/step/step-summary/step-summary-settings';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  readonly c: string = 'AdminComponent';

  multiReadSubscription: Subscription;
  readWriteCESubscription: Subscription;
  multiWriteSubscription: Subscription;

  notifyCardSummary: string;
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

  btnEnableStateEnable: boolean;
  btnEnableStateDisable: boolean;

  btnEnableRename: boolean;
  updateNames: string[];
  updateName: string;
  renameEnable: Date;

  updateTitle: string;

  btnEnableNewStep: boolean;
  btnEnableDeleteStep: boolean;

  updateSteps: Step[];

  updateStepEdit: Step[];
  enableStepEdit: Date;

  updateAlarmEnv: string;
  updateAlarmUnivname: string[];

  updateCardEnv: string;

  avaSummaryCfg: AVASummaryConfig;

  cardSummaryCfg: CardSummaryConfig;
  renewCardSummary: Date;
  updateCardSummary: Date;

  stepSummaryCfg: StepSummaryConfig;

  alarmSummaryCfg: AlarmSummaryConfig;
  updateAlarmSummary: number;

  matrixCfg: MatrixConfig;
  updateMatrix: number;

  private env: string;

  private steps: Step[];
  private stepIdsSelected: number[];

  private cards: Card[];
  private cardsSelected: number[];
  private avarAlias: string;
  private avasAliasList: string[];

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private dbmMultiReadAttrService: DbmMultiReadAttrService
    , private dbmMultiWriteAttrService: DbmMultiWriteAttrService
    , private readWriteCEService: ReadWriteCEService
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

    this.multiReadSubscription = this.dbmMultiReadAttrService.dbmItem
      .subscribe( (res: HttpAccessResult) => {
        console.log(this.c, f, 'multiReadSubscription', res);
        if ( null != res ) {
          if ( HttpAccessResultType.NEXT === res.method ) {
            if ( StepsSettings.STR_READ_STEP === res.key ) {
              if ( null != res.values.length && res.values.length > 0 ) {
                this.renewStep(res.env, res.values);
              }
            }
          }
        }
      });

    this.readWriteCESubscription = this.readWriteCEService.dbmItem
      .subscribe( res => {
        console.log(this.c, f, 'readWriteCESubscription', res);
        if ( null != res ) {

          if ( res.length > 0 ) {
            if (StepSummarySettings.STR_READ_CONDITIONS === res[0].key) {

              this.readStep(res);

            } else if (StepSummarySettings.STR_WRITE_CONDITIONS === res[0].key) {

              const card: Card = this.getCardsSelected();
              const index: number = card.index;
              this.readConditions(this.env, index);
            }
          }
        }
      });

    this.multiWriteSubscription = this.dbmMultiWriteAttrService.dbmItem
      .subscribe( res => {
        console.log(this.c, f, 'multiWriteSubscription', res);
        if ( null != res ) {
          if ( HttpAccessResultType.NEXT === res.method ) {
            if (  CardsSettings.STR_WRITE_CARD_STATE === res.key
                  || CardsSettings.STR_WRITE_CARD_NAME === res.key ) {
              if ( null != res.values ) {
                this.reloadCardSummary();
              }
            }
          }
        }
      });

    this.initGui();
    this.env = this.avaSummaryCfg.envs[0].value;
    this.initCardSummary(this.env);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.readWriteCESubscription.unsubscribe();
    this.multiReadSubscription.unsubscribe();
    this.multiWriteSubscription.unsubscribe();
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

          for ( let  i = 0 ; i < StepsSettings.STR_READ_STEP_ATTR_LIST.length ; ++i ) {
            dbAddresses.push(fullPath + StepsSettings.STR_READ_STEP_ATTR_LIST[i]);
          }
        }
      }
    }

    if ( null != dbAddresses && dbAddresses.length > 0 ) {
      this.dbmMultiReadAttrService.read(this.env, dbAddresses, StepsSettings.STR_READ_STEP);
    } else {
      // Renew with No Conditions
      this.updateSteps = this.steps = [];
    }

    this.updateStepButton();
  }

  private renewStep(connAddr: string, dbValue) {
    const f = 'renewStep';
    console.log(this.c, f);

    const conditions: Map<string, number> = this.readWriteCEService.getConditions(connAddr);

    this.steps = new Array<Step>();
    for ( let i = 0; i < dbValue.length / StepsSettings.STR_READ_STEP_ATTR_LIST.length; ++i ) {
      const base: number = i * StepsSettings.STR_READ_STEP_ATTR_LIST.length;
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

      const step: Step = new Step(i);
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
    this.updateSteps = this.steps;
    this.stepIdsSelected = [];

    this.updateStepButton();
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

    const avarAlias = this.getAvarAlias();
    const ruleBase = this.stepSummaryCfg.ruleBase;
    const cStart = this.stepSummaryCfg.conditionBeginId;
    const cEnd = this.stepSummaryCfg.conditionEndId;
    const alias: string =
                              avarAlias + DbmSettings.STR_COLON
                              + DbmSettings.STR_RULE + (DbmSettings.STR_THREE_ZERO + (index + ruleBase)).slice(-4);
    this.readWriteCEService.readConditions(env, alias, cStart, cEnd);
  }

  private writeConditions(env: string, steps: Step[], index: number) {
    const f = 'writeConditions';
    console.log(this.c, f);

    const avarAlias = this.getAvarAlias();
    const ruleBase = this.stepSummaryCfg.ruleBase;
    const cStart = this.stepSummaryCfg.conditionBeginId;
    const cEnd = this.stepSummaryCfg.conditionEndId;
    const formulaDefaultVal: number = this.stepSummaryCfg.formulaDefaultVal;
    const formulaZeroDefaultVal: number = this.stepSummaryCfg.formulaZeroDefaultVal;
    const alias: string =
                              avarAlias + DbmSettings.STR_COLON
                              + DbmSettings.STR_RULE + (DbmSettings.STR_THREE_ZERO + (index + ruleBase)).slice(-4);
    this.readWriteCEService.writeConditions(env, alias, cStart, cEnd, steps, formulaDefaultVal, formulaZeroDefaultVal);
  }

  private initCardSummary(env): void {
    const f = 'initCardSummary';
    this.updateCardEnv = env;
    this.renewCardSummary = new Date();
  }

  private reloadCardSummary(): void {
    const f = 'reloadCardSummary';

    this.updateCardSummary = new Date();
  }

  private reloadAlarmSummary(index: number): void {
    const f = 'reloadAlarmSummary';
    console.log(this.c, f);

    this.updateAlarmEnv = this.env;
    this.updateAlarmUnivname = this.getAvasAliasList();
    this.updateAlarmSummary = index;
  }

  private setCardsSelected(ids: number[]): void {
    const f = 'setCardsSelected';
    console.log(this.c, f);
    this.cardsSelected = ids;
  }
  private getCardsSelected(): Card {
    const f = 'getCardsSelected';
    console.log(this.c, f);
    let card: Card = null;
    if (null != this.cardsSelected && this.cardsSelected.length > 0) {
      card = this.getCard(this.cardsSelected[0]);
    }
    return card;
  }

  private updateStepButton(cards?: number[], steps?: Step[], step?: Step) {
    const f = 'updateStepButton';
    console.log(this.c, f);

    if ( null == cards )  { cards = this.cardsSelected; }
    if ( null == steps )  { steps = this.steps; }
    if ( null == step )   { step  = this.getStepSelected(); }

    const maxStep: number = this.stepSummaryCfg.conditionEndId - this.stepSummaryCfg.conditionBeginId;
    this.btnEnableNewStep = ((null != cards && null == steps) || (null != cards && null != steps && steps.length < (maxStep + 1)));
    this.btnEnableDeleteStep = ((null != steps && steps.length > 0) && null != step );
  }

  onUpdatedCardsSelect(ids: number[]) {
    const f = 'onUpdatedCardsSelect';
    console.log(this.c, f);

    this.setCardsSelected(ids);

    const card: Card = this.getCardsSelected();
    if ( null != card ) {

      // Renew State Button
      this.btnEnableStateEnable = (!card.state);
      this.btnEnableStateDisable = card.state;

      // Renew Rename Button
      this.btnEnableRename = true;
      this.updateTitle = '';

      // Renew Title
      this.updateTitle = card.name;

      // Reload conditions
      this.readConditions(this.env, card.index);

      // Reload alarm summary
      this.reloadAlarmSummary(card.index);
    } else {

      this.initGui();
    }
  }

  onUpdatedName(name: string) {
    const f = 'onUpdatedName';
    console.log(this.c, f);

    const card = this.getCardsSelected();
    card.name = name;
    this.updateCardName(card);
  }

  onUpdatedSteps(steps: Step[]) {
    const f = 'onUpdatedSteps';
    console.log(this.c, f);

    this.steps = steps;
  }

  onUpdatedStepSelection(stepIds: number[]) {
    const f = 'onUpdatedStepSelection';
    console.log(this.c, f);

    this.stepIdsSelected = stepIds;

    this.updateStepButton();
  }

  onUpdatedStepEdit(steps: Step[]) {
    const f = 'onUpdatedStepEdit';
    console.log(this.c, f);

    this.steps = steps;
    const cardSelected: Card = this.getCardsSelected();
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
    cfg.avasuppression = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_AVASUPPRESSION) as string;

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

  private updateCardName(card: Card) {
    const f = 'updateCardName';
    console.log(this.c, f);

    const values = {};
    const univname = card.univname;
    values[DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_ATTR_LABEL] = card.name;

    this.dbmMultiWriteAttrService.write(this.env, values, CardsSettings.STR_WRITE_CARD_NAME);
  }

  private updateCardState(card: Card) {
    const f = 'updateCardState';
    console.log(this.c, f);

    const values = {};
    const univname = card.univname;
    values[DbmSettings.STR_URL_ALIAS + univname + DbmSettings.STR_ATTR_ENABLE] = (card.state ? 1 : 0);

    this.dbmMultiWriteAttrService.write(this.env, values, CardsSettings.STR_WRITE_CARD_STATE);
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

  onUpdatedCards(cards: Card[]): void {
    const f = 'onUpdatedCards';
    console.log(this.c, f);
    this.cards = cards;
  }
  private getCards(): Card[] {
    const f = 'getCards';
    console.log(this.c, f);
    return this.cards;
  }
  private getCard(id: number): Card {
    const f = 'getCard';
    console.log(this.c, f);
    let card = null;
    const cards = this.getCards();
    if ( null != cards ) {
      cards.forEach(element => {
        if ( id === element.index) {
          card = element;
        }
      });
    }
    return card;
  }

  onUpdatedAvarAlias(avarAlias: string): void {
    const f = 'onUpdatedAvarAlias';
    console.log(this.c, f);
    this.avarAlias = avarAlias;
  }
  private getAvarAlias(): string {
    const f = 'getAvarAlias';
    console.log(this.c, f);
    return this.avarAlias;
  }

  onUpdatedAvasAliasList(avasAliasList: string[]): void {
    const f = 'onUpdatedAvasAliasList';
    console.log(this.c, f);
    this.avasAliasList = avasAliasList;
  }
  private getAvasAliasList(): string[] {
    const f = 'getAvasAliasList';
    console.log(this.c, f);
    return this.avasAliasList;
  }

  btnClicked(btnLabel: string, event?: Event): void {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case AdminComponent.STR_INIT: {
      } break;
      case 'enable': {
        const cardSelected = this.getCardsSelected();
        if ( null != cardSelected ) {
          cardSelected.state = true;
          this.updateCardState(cardSelected);
        }
      } break;
      case 'disable': {
        const cardSelected = this.getCardsSelected();
        if ( null != cardSelected ) {
          cardSelected.state = false;
          this.updateCardState(cardSelected);
        }
      } break;
      case 'rename': {
        const names: string[] = new Array<string>();
        const cards: Card[] = this.getCards();
        for ( let i = 0 ; i < cards.length ; ++i ) {
          names.push(cards[i].name);
        }
        this.updateNames = names;

        const cardSelected = this.getCardsSelected();
        this.updateName = cardSelected.name;
        this.renameEnable = new Date();
      } break;
      case 'newstep': {
        const cardSelected: Card = this.getCardsSelected();
        const stepSelected: Step = this.getStepSelected();
        this.updateStepEdit = this.steps;
        this.enableStepEdit = new Date();
      } break;
      case 'deletestep': {
        const cardSelected: Card = this.getCardsSelected();
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
