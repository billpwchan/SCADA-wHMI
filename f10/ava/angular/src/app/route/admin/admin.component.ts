import { Component, OnInit, OnDestroy, ViewChild, SimpleChanges } from '@angular/core';
import { StepEditControllerSettings } from '../../component/step/step-edit-controller/step-edit-controller-settings';
import { StepEditSettings } from '../../component/step/step-edit/step-edit-settings';
import { CardEditControllerSettings } from '../../component/card/card-edit-controller/card-edit-controller-settings';
import { CardEditSettings } from '../../component/card/card-edit/card-edit-settings';
import { CardEditComponent } from '../../component/card/card-edit/card-edit.component';
import { StepEditComponent } from '../../component/step/step-edit/step-edit.component';
import { StepEditControllerComponent } from '../../component/step/step-edit-controller/step-edit-controller.component';
import { MatrixComponent } from '../../component/alarm/Matrix/matrix.component';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { SettingsService } from '../../service/settings.service';
import { CardService } from '../../service/card/card.service';
import { SelectionService } from '../../service/card/selection.service';
import { CardServiceType } from '../../service/card/card-settings';
import { SelectionServiceType } from '../../service/card/selection-settings';
import { MatrixSettings, Matrix, MatrixConfig } from '../../component/alarm/Matrix/matrix-settings';
import { Step, Card, Equipment } from '../../model/Scenario';
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

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {

  @ViewChild(CardEditComponent) cardEditChildView: CardEditComponent;
  @ViewChild(StepEditComponent) stepEditChildView: StepEditComponent;
  @ViewChild(StepEditControllerComponent) stepEditControllerChildView: StepEditComponent;

  @ViewChild(StepsComponent) stepsComponent: StepsComponent;

  readonly c: string = 'AdminComponent';

  cardSubscription: Subscription;
  // selectionSubscription: Subscription;

  getInstancesByClassNameSubscription: Subscription;
  getChildrenAliasSubscription: Subscription;
  multiReadSubscription: Subscription;
  readWriteCESubscription: Subscription;

  notifyCards: string;
  notifyCardController: string;
  notifyCardEdit: string;

  notifySteps: string;
  notifyStepController: string;
  notifyStepEdit: string;

  notifyStorage: string;
  notifyCsvInterpret: string;

  updateCards: Card[];

  updateSteps: DatatableStep[];

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
  private steps: Step[];

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    // , private cardService: CardService
    // , private selectionService: SelectionService
    , private getInstancesByClassNameService: GetInstancesByClassNameService
    , private getChildrenAliasService: GetChildrenAliasesService
    , private multiReadService: MultiReadService
    , private readWriteCEService: ReadWriteCEService
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

    // this.cardSubscription = this.cardService.cardItem
    //   .subscribe(item => {
    //     console.log(this.c, f, 'cardSubscription', item);
    //     switch (item) {
    //       case CardServiceType.CARD_RELOADED: {
    //         this.updateAlarmSummary = null;
    //       } break;
    //       case CardServiceType.STEP_RELOADED: {
    //         // const selCardId: string = this.selectionService.getSelectedCardId();
    //         // if ( null != selCardId ) {
    //         //   const card = this.cardService.getCard([selCardId]);
    //         //   this.alarmSummaryComponent.updateAlarmSummary = card.index;
    //         // }
    //       } break;
    //       case CardServiceType.STEP_UPDATED: {
    //         // const selCardId: string = this.selectionService.getSelectedCardId();
    //         // if ( null != selCardId ) {
    //         //   const card = this.cardService.getCard([selCardId]);
    //         //   this.alarmSummaryComponent.updateAlarmSummary = card.index;
    //         // }
    //       } break;
    //     }
    //   });

    // this.selectionSubscription = this.selectionService.selectionItem
    //   .subscribe(item => {
    //     console.log(this.c, f, 'selectionSubscription', item);
    //     switch (item) {
    //       case SelectionServiceType.CARD_SELECTED: {
    //         const selCardId: string = this.selectionService.getSelectedCardId();
    //         if ( null != selCardId ) {
    //           const card: Card = this.cardService.getCard([selCardId]);
    //           if ( null != card ) {
    //             this.updateAlarmSummary = card.index;
    //           } else {
    //             console.warn(this.c, f, 'card IS NULL');
    //           }
    //         }
    //       } break;
    //     }
    //   });

    // this.getInstancesByClassNameSubscription = this.getInstancesByClassNameService.dbmItem
    // .subscribe(item => {
    //   console.log(this.c, f, 'selectionSubscription', item);
    //   if ( '' !== item ) {
    //     this.instances = this.getInstancesByClassNameService.getData(this.env, this.className);
    //     if ( null == this.avas ) {
    //       this.avas = new Array<string>();
    //     }
    //     for ( let i = 0 ; i < this.instances.length ; ++i ) {
    //       const className: string = this.instances[i];
    //       const avapPrefix = DbmSettings.STR_ALIAS + this.className + AlarmSummarySettings.STR_AVAP_PREFIX;
    //       if (className.startsWith(avapPrefix)) {
    //         this.avap = this.instances[i];
    //       }
    //       const avasPrefix = DbmSettings.STR_ALIAS + this.className + AlarmSummarySettings.STR_AVAP_PREFIX;
    //       if (className.startsWith(avasPrefix)) {
    //         this.avas.push(this.instances[i]);
    //       }
    //     }
    //     this.getChildrenService.readData(this.env, this.avap);
    //   }
    // });

    this.getChildrenAliasSubscription = this.getChildrenAliasService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'getChildrenAliasSubscription', result);
      if ( null != result ) {
        if ( null != this.instanceRoot ) {
          const root: string = DbmSettings.STR_URL_ALIAS + this.instanceRoot;
          if ( root === result.dbAddress ) {
            if ( null == this.avasList ) {
              this.avasList = new Array<string>();
            }
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
            for ( let i = 0 ; i < result.data.length ; ++i ) {
              const alias = result.data[i];
              const classNames = alias.split(':');
              const className = classNames [classNames.length - 1];
               if ( className.startsWith(AlarmSummarySettings.STR_RULE_PREFIX) ) {
                this.ruleList.push(alias);
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
            const dbAddresses: string[] = new Array<string>();
            for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
              for ( let j = 0 ; j < AlarmSummarySettings.RULE_ATTR_LIST.length ; ++j ) {
                const dbAttribute = this.ruleList[i] + AlarmSummarySettings.RULE_ATTR_LIST[j];
                dbAddresses.push(dbAttribute);
              }
            }
            this.multiReadService.readData(this.env, dbAddresses);
          }
        }
      }
    });

    this.multiReadSubscription = this.multiReadService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'multiReadSubscription', result);
      if ( null != result ) {

        if ( 0 === (result.dbValue.length % 4) ) {
          this.cards = new Array<Card>();
          for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
            const base: number = i * AlarmSummarySettings.RULE_ATTR_LIST.length;
            const card: Card = new Card(
                                        result.dbValue[base + 0]
                                        , result.dbValue[base + 1]
                                        , result.dbValue[base + 2]
                                        , result.dbValue[base + 3]
            );
            this.cards.push(card);
          }
          this.updateCards = this.cards;
        } else if (0 === (result.dbValue.length % 7)) {
          this.steps = new Array<Step>();
          for ( let i = 0; i < result.dbValue.length / 7; ++i ) {
            const index: number = i * 7;
            const geo: number         = result.dbValue[index + 0];
            const func: number        = result.dbValue[index + 1];
            const equipment: string   = result.dbValue[index + 2];
            const point: string       = result.dbValue[index + 3];
            const value: number       = result.dbValue[index + 4];
            const values: number[]    = result.dbValue[index + 5];
            const labels: string[]    = result.dbValue[index + 6];

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
                                            , ''
                                            , 0
                                            ,  geo
                                            ,  func
                                            , equipment
                                            , point
                                            , strLabel
                                          );
            this.steps.push(step);
          }

          // Renew StepsComponent
          const dtSteps: DatatableStep[] = new Array<DatatableStep>();
          for ( let i = 0 ; i < this.steps.length ; ++i ) {
            const step: Step = this.steps[i];
            if ( null != step ) {
              dtSteps.push(DataScenarioHelper.convertToDatatableStep(step));
            }
          }
          this.updateSteps = dtSteps;
        } else {
          // Renew with No Conditions
          this.updateSteps = this.steps = [];
        }
      }
    });

    this.readWriteCESubscription = this.readWriteCEService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'readWriteCESubscription', result);
      if ( null != result ) {

        const dbAddresses: string[] = new Array<string>();

        for ( let n = 0; n < result.length; ++n ) {
          const readWriteCEResult: ReadWriteCEResult = result[n];
          if ( null != readWriteCEResult ) {
            const base: string = readWriteCEResult.base;
            const targetAlias: string = readWriteCEResult.targetAlias;
            const targetValue: string = readWriteCEResult.targetValue;
            if ( null != targetAlias && null != targetValue ) {

              const alias: string = targetAlias.toString().match(/^(.*?)\./)[1];

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
      }
    });

    this.env = this.alarmSummaryCfg.envs[0].value;
    this.instanceRoot = this.alarmSummaryCfg.instanceRoot;
    this.getChildrenAliasService.readData(this.env, DbmSettings.STR_URL_ALIAS + this.instanceRoot);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    // this.selectionSubscription.unsubscribe();
    this.cardSubscription.unsubscribe();
    this.getInstancesByClassNameSubscription.unsubscribe();
  }

  onUpdatedCardSelection(cardIds: number[]) {
    const f = 'onUpdatedCardSelection';
    console.log(this.c, f);

    if ( null != cardIds ) {
      if ( cardIds.length > 0 ) {
        const cardId: number = cardIds[0];

        let cardSelected: Card = null;
        for ( let i = 0 ; i < this.cards.length ; ++i ) {
          const card: Card = this.cards[i];
          if ( cardId === card.index ) {
            cardSelected = card;
            break;
          }
        }

        if ( null != cardSelected ) {

          // Renew conditions
          const ruleBase = 1;
          const cStart = 1;
          const cEnd = 8;
          const index: number = cardSelected.index;
          const univname: string = this.avar + DbmSettings.STR_COLON + DbmSettings.STR_RULE + ('000' + (index + ruleBase)).slice(-4);
          this.readWriteCEService.readConditions(this.env, univname, cStart, cEnd);

          // Renew alarm summary
          this.updateAlarmSummary = cardSelected.index;
        }
      }
    }
  }

  onUpdatedStepSelection(stepIds: number[]) {
    const f = 'onUpdatedStepSelection';
    console.log(this.c, f);

    if ( null != stepIds ) {
      if ( stepIds.length > 0 ) {
        const stepId: number = stepIds[0];

        let stepSelected: Step = null;
        for ( let i = 0 ; i < this.steps.length ; ++i ) {
          const step: Step = this.steps[i];
          if ( stepId === step.step ) {
            stepSelected = step;
            break;
          }
        }
      }
    }
  }

  // onUpdatedSteps(data: Step[]): void {
  //   const f = 'onUpdatedSteps';
  //   console.log(this.c, f);
  //   const selCardId: string = this.selectionService.getSelectedCardId();
  //   if ( null != selCardId ) {
  //     const card = this.cardService.getCard([selCardId]);
  //     if ( null != card ) {
  //       card.steps = JSON.parse(JSON.stringify(data));
  //     } else {
  //       card.steps = null;
  //     }
  //   }
  // }

  onUpdatedAlarmSummary(data: number[][]): void {
    const f = 'onUpdatedAlarmSummary';
    console.log(this.c, f);
    // const selCardId: string = this.selectionService.getSelectedCardId();
    // if ( null != selCardId ) {
    //   const card = this.cardService.getCard([selCardId]);
    //   if ( null != card ) {
    //     card.alarms = JSON.parse(JSON.stringify(data));
    //   } else {
    //     card.alarms = null;
    //   }
    // }
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
      case StepEditControllerSettings.STR_STEP_EDIT_ENABLE: {
        this.stepEditChildView.onParentChange(StepEditSettings.STR_STEP_EDIT_ENABLE);
      } break;
      case CardEditControllerSettings.STR_CARD_EDIT_CONTROLLER_ADD_ENABLE: {
        this.cardEditChildView.onParentChange(CardEditSettings.STR_CARD_EDIT_ADD_ENABLE);
      } break;
      case CardEditControllerSettings.STR_CARD_EDIT_CONTROLLER_RENAME_ENABLE: {
        this.cardEditChildView.onParentChange(CardEditSettings.STR_CARD_EDIT_MODIFY_ENABLE);
      } break;
      case CardEditControllerSettings.STR_CARD_EDIT_CONTROLLER_MODIFY_ENABLE: {
        this.stepEditControllerChildView.onParentChange(StepEditControllerSettings.STR_STEP_EDIT_ENABLE);
      } break;
    }
  }

}
