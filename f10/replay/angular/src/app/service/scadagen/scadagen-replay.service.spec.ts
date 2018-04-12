import { TestBed, inject } from '@angular/core/testing';

import { ScadagenReplayService } from './scadagen-replay.service';

describe('ScadagenReplayService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScadagenReplayService]
    });
  });

  it('should be created', inject([ScadagenReplayService], (service: ScadagenReplayService) => {
    expect(service).toBeTruthy();
  }));
});
