import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { SelectionServiceType } from './selection-settings';

@Injectable()
export class SelectionService {

  readonly c = SelectionService.name;

  // Observable source
  private selectionSource = new BehaviorSubject<SelectionServiceType>(SelectionServiceType.UNKNOW);

  // Observable item stream
  selectionItem = this.selectionSource.asObservable();

  private selectedCards: string[];
  private selectedSteps: number[];

  constructor() {}

  // Service command
  selectionChanged(selectionServiceType: SelectionServiceType) {
    const f = 'selectionChanged';
    console.log(this.c, f, selectionServiceType);
    this.selectionSource.next(selectionServiceType);
  }

  notifyUpdate(selectionServiceType: SelectionServiceType): void {
    const f = 'notifyUpdate';
    console.log(this.c, f);
    console.log(this.c, f, selectionServiceType);

    switch (selectionServiceType) {
      case SelectionServiceType.CARD_SELECTED: {
        this.selectionChanged(SelectionServiceType.CARD_SELECTED);
      } break;
      case SelectionServiceType.STEP_SELECTED: {
        this.selectionChanged(SelectionServiceType.STEP_SELECTED);
      } break;
    }
  }

  getSelectedCardIds(): string[] { return this.selectedCards; }
  getSelectedCardId(): string { return this.getSelectedCardIds()[0]; }
  setSelectedCardIds(selectedCards: string[]): void {
    const f = 'setSelectedCardIdentifys';
    console.log(this.c, f, this.selectedCards);
    this.selectedCards = selectedCards;
    console.log(this.c, f, 'SelectionServiceType.SET_SELECT_CARD', SelectionServiceType.CARD_SELECTED);
    this.selectionChanged(SelectionServiceType.CARD_SELECTED);
  }

  getSelectedStepIds(): number[] { return this.selectedSteps; }
  getSelectedStepId(): number{ return this.getSelectedStepIds()[0]; }
  setSelectedStepIds(selectedSteps: number[]): void {
    console.log(this.c, 'setSelectedSteps', this.selectedCards);
    this.selectedSteps = selectedSteps;
    this.selectionChanged(SelectionServiceType.STEP_SELECTED);
  }

}
