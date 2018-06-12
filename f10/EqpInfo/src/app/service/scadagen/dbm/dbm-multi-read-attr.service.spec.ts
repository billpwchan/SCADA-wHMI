import { TestBed, inject } from '@angular/core/testing';

import { DbmMultiReadAttrService } from './dbm-multi-read-attr.service';

describe('DbmMultiReadAttrService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmMultiReadAttrService]
    });
  });

  it('should be created', inject([DbmMultiReadAttrService], (service: DbmMultiReadAttrService) => {
    expect(service).toBeTruthy();
  }));
});
