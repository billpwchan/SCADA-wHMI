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

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {

  @ViewChild(CardEditComponent) cardEditChildView: CardEditComponent;
  @ViewChild(StepEditComponent) stepEditChildView: StepEditComponent;
  @ViewChild(StepEditControllerComponent) stepEditControllerChildView: StepEditComponent;
  @ViewChild(MatrixComponent) matrixComponent: MatrixComponent;

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

  matrixCfg: MatrixConfig;
  updateMatrix: number[][];

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private cardService: CardService
    , private selectionService: SelectionService
    , private storageService: StorageService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });

    this.matrixCfg = this.loadMatrixCfgs();
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => {
        console.log(this.c, f, 'cardSubscription', item);
        switch (item) {
          case CardServiceType.CARD_RELOADED: {
            // this.btnClicked(StepsComponent.STR_CARD_RELOADED);
            this.matrixComponent.updateMatrix = null;
          } break;
        }
      });

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            // this.btnClicked(StepsComponent.STR_CARD_SELECTED);
            const selCardId: string = this.selectionService.getSelectedCardId();
            if ( null != selCardId ) {
              const card = this.cardService.getCard([selCardId]);
              this.updateMatrix = card.alarms;
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

  onUpdatedMatrix(data: number[][]): void {
    const f = 'onUpdatedMatrix';
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

  private loadMatrixCfgs(): MatrixConfig {
    const f = 'loadMatrixCfgs';
    console.log(this.c, f);

    const c = 'MatrixComponent';
    const cfg: MatrixConfig = new MatrixConfig();

    cfg.spreadsheet_height   = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_SPREADSHEET_HEIGHT);
    cfg.spreadsheet_width    = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_SPREADSHEET_WIDTH);
    cfg.spreadsheet_visible_rows = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_SPREADSHEET_VISIBLE_ROW);

    cfg.row_header_prefix  = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_ROW_HEADER_PREFIX);
    cfg.row_header_ids     = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_ROW_HEADER_IDS);
    cfg.row_header_width   = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_ROW_HEADER_WIDTH);

    cfg.col_header_prefix  = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_COL_HEADER_PREFIX);
    cfg.col_header_ids     = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_COL_HEADER_IDS);
    cfg.col_width          = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_COL_WIDTH);

    cfg.default_value      = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_DEFAULT_VALUE);

    cfg.matrixes           = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_MATRIXES) as Matrix[];

    return cfg;
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.updateMatrix = null;
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
