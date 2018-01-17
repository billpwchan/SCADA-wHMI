import { TestBed, inject } from '@angular/core/testing';

import { DbmWriteAvaSupService } from './dbm-write-ava-sup.service';

describe('DbmWriteAvaSupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmWriteAvaSupService]
    });
  });

  it('should be created', inject([DbmWriteAvaSupService], (service: DbmReadAvaSupService) => {
    expect(service).toBeTruthy();
  }));
});
