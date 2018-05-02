import { TestBed, inject } from '@angular/core/testing';

import { DbmService } from './dbm.service';

describe('DbmService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmService]
    });
  });

  it('should be created', inject([DbmService], (service: DbmService) => {
    expect(service).toBeTruthy();
  }));
});
