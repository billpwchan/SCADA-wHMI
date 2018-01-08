import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { SelectionServiceType } from './selection-settings';

@Injectable()
export class SelectionService {

  readonly c = 'SelectionService';

  // Observable source
  private selectionSource = new BehaviorSubject<SelectionServiceType>(SelectionServiceType.UNKNOW);

  // Observable item stream
  selectionItem = this.selectionSource.asObservable();

  private selectedCards: string[] = new Array<string>();
  private selectedSteps: number[] = new Array<number>();

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

  // Return by Value
  getSelectedCardIds(): string[] {
    let cloned: string[] = new Array<string>();
    if ( this.selectedCards ) {
      cloned = JSON.parse(JSON.stringify(this.selectedCards));
    }
    return cloned;
  }
  getSelectedCardId(): string {
    const f = 'getSelectedStepId';
    return this.getSelectedCardIds()[0] ? this.getSelectedCardIds()[0] : null;
  }
  setSelectedCardIds(selectedCards: string[]): void {
    const f = 'setSelectedCardIdentifys';
    console.log(this.c, f, this.selectedCards);
    this.selectedCards = selectedCards;
    console.log(this.c, f, 'SelectionServiceType.SET_SELECT_CARD', SelectionServiceType.CARD_SELECTED);
    this.selectionChanged(SelectionServiceType.CARD_SELECTED);
  }

  // Return by Value
  getSelectedStepIds(): number[] {
    let cloned: number[] = new Array<number>();
    if ( this.selectedSteps ) {
      cloned = JSON.parse(JSON.stringify(this.selectedSteps));
    }
    return cloned;
  }
  getSelectedStepId(): number {
    const f = 'getSelectedStepId';
    let id = NaN;
    try {
      id = this.getSelectedStepIds()[0];
    } catch ( err ) {
      console.warn(this.c, f, 'invalid value of this.getSelectedStepIds()[0]');
    }
    return id;
  }
  setSelectedStepIds(selectedSteps: number[]): void {
    const f = 'setSelectedStepIds';
    console.log(this.c, f, 'setSelectedSteps', this.selectedCards);
    this.selectedSteps = selectedSteps;
    this.selectionChanged(SelectionServiceType.STEP_SELECTED);
  }

}
