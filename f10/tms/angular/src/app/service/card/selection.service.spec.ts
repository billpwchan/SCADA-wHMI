import { TestBed, inject } from '@angular/core/testing';

import { SelectionService } from './selection.service';

describe('CardSelectionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SelectionService]
    });
  });

  it('should be created', inject([SelectionService], (service: SelectionService) => {
    expect(service).toBeTruthy();
  }));
});
