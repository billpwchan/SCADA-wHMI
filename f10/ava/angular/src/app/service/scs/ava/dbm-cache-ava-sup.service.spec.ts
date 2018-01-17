import { TestBed, inject } from '@angular/core/testing';

import { DbmCacheAvaSupService } from './dbm-cache-ava-sup.service';

describe('DbmCacheAvaSupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmCacheAvaSupService]
    });
  });

  it('should be created', inject([DbmCacheAvaSupService], (service: DbmCacheAvaSupService) => {
    expect(service).toBeTruthy();
  }));
});
