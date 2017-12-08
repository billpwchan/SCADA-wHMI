import { TestBed, inject } from '@angular/core/testing';

import { DacSimService } from './dac-sim.service';

describe('DacSimService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DacSimService]
    });
  });

  it('should be created', inject([DacSimService], (service: DacSimService) => {
    expect(service).toBeTruthy();
  }));
});
