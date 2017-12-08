import { Component, OnInit, OnDestroy } from '@angular/core';
import { SelectionService } from '../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardService } from '../../service/card/card.service';
import { AppSettings } from '../../app-settings';
import { Step, Card, StepType } from '../../model/Scenario';

@Component({
  selector: 'app-step-controller',
  templateUrl: './step-controller.component.html',
  styleUrls: ['./step-controller.component.css']
})
export class StepControllerComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED = AppSettings.STR_STEP_UPDATED;

  readonly c = StepControllerComponent.name;

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
        case CardService.STR_CARD_RELOADED: {
          this.btnClicked(StepControllerComponent.STR_INIT);
        } break;
        case CardService.STR_STEP_RELOADED: {
          this.btnClicked(StepControllerComponent.STR_INIT);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe( item => {
      console.log(this.c, f, 'selectionSubscription', item);
      if ( SelectionService.STR_STEP_SELECTED === item ) {
        this.btnClicked(StepControllerComponent.STR_STEP_SELECTED);
      }
    });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);

    this.cardsSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  private widgetControl(): void {
    const f = 'widgetControl';
    console.log(this.c, f);

    const step: Step = this.cardService.getSteps(this.cardSelected, [this.stepSelected])[0];
    if ( null != step ) {
      if ( StepType.stop == step.state ) {
        this.btnDisabledStartStep = false;
        this.btnDisabledStopStep = true;
      } else if ( StepType.start == step.state ) {
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
        this.cardSelected = this.selectionService.getSelectedCardIds()[0];
        this.stepSelected = this.selectionService.getSelectedStepIds()[0];
        this.widgetControl();
      } break;
      case 'startstep': {
        this.cardService.executeStep(
          this.cardSelected
          , this.stepSelected
          , CardService.INT_EXEC_TYPE_START);
      } break;
      case 'stopstep': {
        this.cardService.executeStep(
          this.cardSelected
          , this.stepSelected
          , CardService.INT_EXEC_TYPE_STOP);
      } break;
    }
  }
}
