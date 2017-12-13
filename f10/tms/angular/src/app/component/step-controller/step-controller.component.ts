import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { SelectionService } from '../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardService } from '../../service/card/card.service';
import { AppSettings } from '../../app-settings';
import { Step, Card, StepType } from '../../model/Scenario';
import { DacSimExecType } from '../../service/scs/dac-sim-settings';
import { CardServiceType } from '../../service/card/card-settings';
import { SelectionServiceType } from '../../service/card/selection-settings';

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
        case CardServiceType.STEP_RELOADED: {
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
    const f = 'ngOnDestory';
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
        //case StepEditControllerComponent.STR_NEWSTEP: {
        //} break;
      }
    }
  }

  private widgetControl(): void {
    const f = 'widgetControl';
    console.log(this.c, f);

    const step: Step = this.cardService.getStep(this.cardSelected, [this.stepSelected]);
    if ( null != step ) {
      if ( StepType.STOP == step.state ) {
        this.btnDisabledStartStep = false;
        this.btnDisabledStopStep = true;
      } else if ( StepType.START == step.state ) {
        this.btnDisabledStartStep = true;
        this.btnDisabledStopStep = false;
      }
    } else {
      this.btnDisabledStartStep = true;
      this.btnDisabledStopStep = true;
    }
  }

  btnClicked(str: string): void {
    const f = 'btnClicked';
    console.log(this.c, f);

    switch (str) {
      case StepControllerComponent.STR_INIT: {
        this.btnDisabledStartStep = true;
        this.btnDisabledStopStep = true;
      } break;
      case StepControllerComponent.STR_STEP_SELECTED: {
        this.cardSelected = this.selectionService.getSelectedCardId();
        this.stepSelected = this.selectionService.getSelectedStepId();
        this.widgetControl();
      } break;
      case 'startstep': {
        this.cardService.executeStep(
          this.cardSelected
          , this.stepSelected
          , DacSimExecType.START);
      } break;
      case 'stopstep': {
        this.cardService.executeStep(
          this.cardSelected
          , this.stepSelected
          , DacSimExecType.STOP);
      } break;
    }
  }
}
