import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { SettingsService } from '../../../service/settings.service';
import { CardService } from '../../../service/card/card.service';
import { Subscription } from 'rxjs/Subscription';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { SelectionService } from '../../../service/card/selection.service';
import { StepEditControllerSettings } from './step-edit-controller-settings';
import { debug } from 'util';

@Component({
  selector: 'app-step-edit-controller',
  templateUrl: './step-edit-controller.component.html',
  styleUrls: ['./step-edit-controller.component.css']
})
export class StepEditControllerComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_UPDATED = AppSettings.STR_STEP_UPDATED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NEWSTEP = 'newstep';

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'StepEditControllerComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  selectedCardId: string;
  selectedStepId: number;

  // GUI Data Binding
  btnDisabledNewStep: boolean;
  btnDisabledDeleteStep: boolean;

  constructor(
    private cardService: CardService
    , private selectionService: SelectionService
    , private settingsService: SettingsService
  ) { }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => {
        console.log(this.c, f, 'cardSubscription', item);
        switch (item) {
          case CardServiceType.CARD_RELOADED: {
            this.btnClicked(StepEditControllerComponent.STR_CARD_RELOADED);
          } break;
          case CardServiceType.CARD_UPDATED: {
            this.btnClicked(StepEditControllerComponent.STR_CARD_UPDATED);
          } break;
          case CardServiceType.STEP_RELOADED: {
            this.btnClicked(StepEditControllerComponent.STR_STEP_RELOADED);
          } break;
          case CardServiceType.STEP_UPDATED: {
            this.btnClicked(StepEditControllerComponent.STR_STEP_UPDATED);
          } break;
        }
      }
    );

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            this.btnClicked(StepEditControllerComponent.STR_CARD_SELECTED);
          } break;
          case SelectionServiceType.STEP_SELECTED: {
            this.btnClicked(StepEditControllerComponent.STR_STEP_SELECTED);
          } break;
        }
      }
    );

    this.btnClicked(StepEditControllerComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);

    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();

  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if ( changes[StepEditControllerComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[StepEditControllerComponent.STR_NORIFY_FROM_PARENT].currentValue) {
        case StepEditControllerComponent.STR_NEWSTEP: {
        } break;
      }
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

    const component = StepEditControllerComponent.name;

  }

  private updateSelection() {
    const f = 'updateSelection';
    console.log(this.c, f);
    this.selectedCardId = this.selectionService.getSelectedCardId();
    this.selectedStepId = this.selectionService.getSelectedStepId();

    console.log(this.c, f, 'this.selectedCardId[' + this.selectedCardId + '] this.selectedStepId[' + this.selectedStepId + ']');
  }

  private widgetController() {
    const f = 'widgetController';
    console.log(this.c, f);

    // this.btnDisabledNewStep = true;
    // this.btnDisabledDeleteStep = true;
    // if ( ! this.cardService.isRunning() ) {
    //   if ( null != this.selectedCardId ) {
    //     this.btnDisabledNewStep = false;
    //     if ( null != this.selectedStepId ) {
    //       this.btnDisabledDeleteStep = false;
    //     }
    //   }
    // }

    console.log(this.c, f, 'this.selectedCardId[' + this.selectedCardId + '] this.selectedStepId[' + this.selectedStepId + ']');
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnDisabledNewStep = true;
    this.btnDisabledDeleteStep = true;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case StepEditControllerComponent.STR_INIT: {
        this.init();
      } break;
      case StepEditControllerComponent.STR_CARD_RELOADED: {
        this.updateSelection();
        this.widgetController();
        // this.init();
      } break;
      case StepEditControllerComponent.STR_CARD_SELECTED: {
        this.updateSelection();
        this.widgetController();
        // this.selectedCardId = this.selectionService.getSelectedCardId();
        // this.btnDisabledNewStep = false;
        // this.btnDisabledDeleteStep = true;
      } break;
      case StepEditControllerComponent.STR_CARD_UPDATED: {
        this.updateSelection();
        this.widgetController();
      } break;
      case StepEditControllerComponent.STR_STEP_UPDATED: {
        this.updateSelection();
        this.widgetController();
        // this.btnDisabledNewStep = false;
      } break;
      case StepEditControllerComponent.STR_STEP_RELOADED: {
        this.updateSelection();
        this.widgetController();
        // this.btnDisabledNewStep = false;
      } break;
      case StepEditControllerComponent.STR_STEP_SELECTED: {
        this.updateSelection();
        this.widgetController();
        // this.selectedStepId = this.selectionService.getSelectedStepId();
        // this.btnDisabledNewStep = false;
        // this.btnDisabledDeleteStep = false;
      } break;
      case 'newstep': {
        this.btnClicked(StepEditControllerComponent.STR_INIT);
        this.sendNotifyParent(StepEditControllerSettings.STR_STEP_EDIT_ENABLE);
      } break;
      case 'deletestep': {
        this.cardService.deleteStep(this.selectedCardId, [this.selectedStepId]);
        this.cardService.notifyUpdate(CardServiceType.STEP_RELOADED);
        this.cardService.notifyUpdate(CardServiceType.STEP_EDITED);
      } break;
      default: {
        console.log(this.c, f, 'default btnLabel[' + btnLabel + ']');
      }
    }

    console.log(this.c, f, 'end btnLabel[' + btnLabel + ']');

    // this.sendNotifyParent(btnLabel);
  }
}
