import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class SelectionService {

  public static readonly STR_CARD_SELECTED = 'cardselected';
  public static readonly STR_STEP_SELECTED = 'stepselected';

  readonly c = 'SelectionService';

  // Observable source
  private selectionSource = new BehaviorSubject<string>('');

  // Observable item stream
  selectionItem = this.selectionSource.asObservable();

  private selectedCards: string[];
  private selectedSteps: number[];

  constructor() {}

  // Service command
  selectionChanged(str: string) {
    const f = 'selectionChanged';
    console.log(this.c, f, str);
    this.selectionSource.next(str);
  }

  notifyUpdate(str: string): void {
    const f = 'notifyUpdate';
    console.log(this.c, f);
    console.log(this.c, f, str);

    switch (str) {
      case SelectionService.STR_CARD_SELECTED: {
        this.selectionChanged(SelectionService.STR_CARD_SELECTED);
      } break;
      case SelectionService.STR_STEP_SELECTED: {
        this.selectionChanged(SelectionService.STR_STEP_SELECTED);
      } break;
    }
  }

  getSelectedCardIds(): string[] { return this.selectedCards; }
  getSelectedCardId(): string { return this.getSelectedCardIds()[0]; }
  setSelectedCardIds(selectedCards: string[]): void {
    const f = 'setSelectedCardIdentifys';
    console.log(this.c, f, this.selectedCards);
    this.selectedCards = selectedCards;
    console.log(this.c, f, 'SelectionService.STR_SET_SELECT_CARD', SelectionService.STR_CARD_SELECTED);
    this.selectionChanged(SelectionService.STR_CARD_SELECTED);
  }

  getSelectedStepIds(): number[] { return this.selectedSteps; }
  getSelectedStepId(): number{ return this.getSelectedStepIds()[0]; }
  setSelectedStepIds(selectedSteps: number[]): void {
    console.log(this.c, 'setSelectedSteps', this.selectedCards);
    this.selectedSteps = selectedSteps;
    this.selectionChanged(SelectionService.STR_STEP_SELECTED);
  }

}
