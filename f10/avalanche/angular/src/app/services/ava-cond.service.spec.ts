import { TestBed, inject } from '@angular/core/testing';

import { AvaCondService } from './ava-cond.service';

describe('AvaCondService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AvaCondService]
    });
  });

  it('should be created', inject([AvaCondService], (service: AvaCondService) => {
    expect(service).toBeTruthy();
  }));
});
