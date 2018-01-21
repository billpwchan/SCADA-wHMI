import { TestBed, inject } from '@angular/core/testing';

import { MultiReadService } from './multi-read.service';

describe('MultiReadRulesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MultiReadService]
    });
  });

  it('should be created', inject([MultiReadService], (service: MultiReadService) => {
    expect(service).toBeTruthy();
  }));
});
