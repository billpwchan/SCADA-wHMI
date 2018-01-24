import { TestBed, inject } from '@angular/core/testing';

import { ReadWriteCEService } from './read-write-ce.service';

describe('ReadWriteCeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReadWriteCEService]
    });
  });

  it('should be created', inject([ReadWriteCEService], (service: ReadWriteCEService) => {
    expect(service).toBeTruthy();
  }));
});
