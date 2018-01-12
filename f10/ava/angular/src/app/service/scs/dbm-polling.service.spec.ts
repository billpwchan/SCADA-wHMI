import { TestBed, inject } from '@angular/core/testing';

import { DbmPollingService } from './dbm-polling.service';

describe('DbmPollingService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmPollingService]
    });
  });

  it('should be created', inject([DbmPollingService], (service: DbmPollingService) => {
    expect(service).toBeTruthy();
  }));
});
