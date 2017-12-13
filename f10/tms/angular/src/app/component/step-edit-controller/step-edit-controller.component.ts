import { Component, OnInit, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../app-settings';
import { SettingsService } from '../../service/settings.service';
import { OnDestroy, OnChanges } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
  selector: 'app-step-edit-controller',
  templateUrl: './step-edit-controller.component.html',
  styleUrls: ['./step-edit-controller.component.css']
})
export class StepEditControllerComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NEWSTEP = 'newstep';

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = StepEditControllerComponent.name;
  
  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  // GUI Data Binding
  editEnableNewStep: boolean;
  
    btnDisabledNewStep: boolean;
    btnDisabledDeleteStep: boolean;

  constructor(
    private settingsService: SettingsService
  ) { }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.btnClicked('init');
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestory';
    console.log(this.c, f);

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

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

  }

  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case StepEditControllerComponent.STR_INIT: {
        this.init();
      } break;
      case StepEditControllerComponent.STR_CARD_RELOADED: {
        this.init();
      } break;
      case StepEditControllerComponent.STR_CARD_SELECTED: {
        this.init();
        // this.selectedCardId = this.selectionService.getSelectedCardId();
        this.btnDisabledNewStep = false;
        this.btnDisabledDeleteStep = true;
      } break;
      case StepEditControllerComponent.STR_STEP_RELOADED: {
        this.init();
      } break;      
      case StepEditControllerComponent.STR_STEP_SELECTED: {
        // this.selectedStepId = this.selectionService.getSelectedStepId();
        this.btnDisabledNewStep = false;
        this.btnDisabledDeleteStep = false;
      } break;
      case 'newstep': {
        this.btnClicked(StepEditControllerComponent.STR_INIT);
        this.editEnableNewStep = true;
      } break;
    }

    this.sendNotifyParent(btnLabel);
  }

}
