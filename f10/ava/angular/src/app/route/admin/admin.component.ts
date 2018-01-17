import { Component, OnInit, OnDestroy, ViewChild, SimpleChanges } from '@angular/core';
import { StorageService } from '../../service/card/storage.service';
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
import { Step, Card } from '../../model/Scenario';
import { StepsComponent } from '../../component/step/steps/steps.component';
import { AlarmSummaryConfig, Env, AlarmSummarySettings } from '../../component/alarm/alarm-summary/alarm-summary-settings';
import { AlarmSummaryComponent } from '../../component/alarm/alarm-summary/alarm-summary.component';

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
  @ViewChild(AlarmSummaryComponent) alarmSummaryComponent: AlarmSummaryComponent;

  readonly c: string = 'AdminComponent';

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  notifyCards: string;
  notifyCardController: string;
  notifyCardEdit: string;

  notifySteps: string;
  notifyStepController: string;
  notifyStepEdit: string;

  notifyStorage: string;
  notifyCsvInterpret: string;

  alarmSummaryCfg: AlarmSummaryConfig;
  updateAlarmSummary: number;

  matrixCfg: MatrixConfig;
  updateMatrix: number;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private cardService: CardService
    , private selectionService: SelectionService
    , private storageService: StorageService
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

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => {
        console.log(this.c, f, 'cardSubscription', item);
        switch (item) {
          case CardServiceType.CARD_RELOADED: {
            this.alarmSummaryComponent.updateAlarmSummary = null;
          } break;
          case CardServiceType.STEP_RELOADED: {
            // const selCardId: string = this.selectionService.getSelectedCardId();
            // if ( null != selCardId ) {
            //   const card = this.cardService.getCard([selCardId]);
            //   this.alarmSummaryComponent.updateAlarmSummary = card.index;
            // }
          } break;
          case CardServiceType.STEP_UPDATED: {
            // const selCardId: string = this.selectionService.getSelectedCardId();
            // if ( null != selCardId ) {
            //   const card = this.cardService.getCard([selCardId]);
            //   this.alarmSummaryComponent.updateAlarmSummary = card.index;
            // }
          } break;
        }
      });

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            const selCardId: string = this.selectionService.getSelectedCardId();
            if ( null != selCardId ) {
              const card: Card = this.cardService.getCard([selCardId]);
              if ( null != card ) {
                this.updateAlarmSummary = card.index;
              } else {
                console.warn(this.c, f, 'card IS NULL');
              }
            }
          } break;
        }
      });

    this.storageService.loadCards();
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.selectionSubscription.unsubscribe();
    this.cardSubscription.unsubscribe();
  }

  onUpdatedSteps(data: Step[]): void {
    const f = 'onUpdatedSteps';
    console.log(this.c, f);
    const selCardId: string = this.selectionService.getSelectedCardId();
    if ( null != selCardId ) {
      const card = this.cardService.getCard([selCardId]);
      if ( null != card ) {
        card.steps = JSON.parse(JSON.stringify(data));
      } else {
        card.steps = null;
      }
    }
  }

  onUpdatedAlarmSummary(data: number[][]): void {
    const f = 'onUpdatedAlarmSummary';
    console.log(this.c, f);
    const selCardId: string = this.selectionService.getSelectedCardId();
    if ( null != selCardId ) {
      const card = this.cardService.getCard([selCardId]);
      if ( null != card ) {
        card.alarms = JSON.parse(JSON.stringify(data));
      } else {
        card.alarms = null;
      }
    }
  }

  private loadAlarmSummaryCfgs(): AlarmSummaryConfig {
    const f = 'loadAlarmSummaryCfgs';
    console.log(this.c, f);

    const c = 'AlarmSummaryComponent';
    const cfg: AlarmSummaryConfig = new AlarmSummaryConfig();

    cfg.envs = this.settingsService.getSetting(this.c, f, c, AlarmSummarySettings.STR_ENVS) as Env[];

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
