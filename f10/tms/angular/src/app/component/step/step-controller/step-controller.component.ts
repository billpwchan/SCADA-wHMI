import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { SelectionService } from '../../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardService } from '../../../service/card/card.service';
import { AppSettings } from '../../../app-settings';
import { Step, Card, StepType, CardType } from '../../../model/Scenario';
import { DacSimExecType } from '../../../service/scs/dac-sim-settings';
import { CardServiceType, StepExistsResult } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';

@Component({
  selector: 'app-step-controller',
  templateUrl: './step-controller.component.html',
  styleUrls: ['./step-controller.component.css']
})
export class StepControllerComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED = AppSettings.STR_STEP_UPDATED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = StepControllerComponent.name;

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  selectionSubscription: Subscription;
  cardsSubscription: Subscription;

  btnDisabledStartStep: boolean;
  btnDisabledStopStep: boolean;

  cardSelected: string;
  stepSelected: number;

  constructor(
    private cardService: CardService
    , private selectionService: SelectionService
  ) {}

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardsSubscription = this.cardService.cardItem
    .subscribe( item => {
      switch ( item ) {
        case CardServiceType.CARD_RELOADED: {
          this.btnClicked(StepControllerComponent.STR_INIT);
        } break;
        case CardServiceType.CARD_UPDATED: {
          this.btnClicked(StepControllerComponent.STR_INIT);
        } break;
        case CardServiceType.STEP_RELOADED: {
          this.btnClicked(StepControllerComponent.STR_INIT);
        } break;
        case CardServiceType.STEP_UPDATED: {
          this.btnClicked(StepControllerComponent.STR_INIT);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe( item => {
      console.log(this.c, f, 'selectionSubscription', item);
      switch (item) {
        case SelectionServiceType.STEP_SELECTED: {
          this.btnClicked(StepControllerComponent.STR_STEP_SELECTED);
        } break;
      }
    });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardsSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[StepControllerComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[StepControllerComponent.STR_NORIFY_FROM_PARENT].currentValue) {
      }
    }
  }

  private updateSelection() {
    const f = 'updateSelection';
    console.log(this.c, f);

    this.cardSelected = this.selectionService.getSelectedCardId();
    this.stepSelected = this.selectionService.getSelectedStepId();
  }

  private widgetControl(): void {
    const f = 'widgetControl';
    console.log(this.c, f);

    this.btnDisabledStartStep = true;
    this.btnDisabledStopStep = true;
    const stepExists = this.cardService.stepExists(this.cardSelected, this.stepSelected);
    if ( StepExistsResult.STEP_FOUND === stepExists) {
      const step: Step = this.cardService.getStep(this.cardSelected, [this.stepSelected]);

      if ( null != step ) {

        if ( this.cardService.isRunning() ) {
          this.btnDisabledStartStep = true;
          this.btnDisabledStopStep = true;
        } else {
          if ( StepType.STOPPED === step.state ) {
            this.btnDisabledStartStep = false;
          } else if ( StepType.START === step.state ) {
            this.btnDisabledStopStep = false;
          }
        }
      }
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnDisabledStartStep = true;
    this.btnDisabledStopStep = true;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case StepControllerComponent.STR_INIT: {
        this.init();
      } break;
      case StepControllerComponent.STR_STEP_RELOADED: {
        this.updateSelection();
        this.widgetControl();
      } break;
      case StepControllerComponent.STR_STEP_UPDATED: {
        this.init();
      } break;
      case StepControllerComponent.STR_STEP_SELECTED: {
        this.updateSelection();
        this.widgetControl();
      } break;
      case 'startstep': {
        this.cardService.getCard([this.cardSelected]).state = CardType.STARTED;
        this.cardService.executeStep(
          this.cardSelected
          , this.stepSelected
          , DacSimExecType.START);
      } break;
      case 'stopstep': {
        this.cardService.getCard([this.cardSelected]).state = CardType.STOPPED;
        this.cardService.executeStep(
          this.cardSelected
          , this.stepSelected
          , DacSimExecType.STOP);
      } break;
    }
  }
}
