import { TestBed, inject } from '@angular/core/testing';

import { DbmReadAvaSupService } from './dbm-read-ava-sup.service';

describe('DbmReadAvaSupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmReadAvaSupService]
    });
  });

  it('should be created', inject([DbmReadAvaSupService], (service: DbmReadAvaSupService) => {
    expect(service).toBeTruthy();
  }));
});
