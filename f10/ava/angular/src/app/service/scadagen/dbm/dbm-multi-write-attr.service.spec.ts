import { TestBed, inject } from '@angular/core/testing';

import { DbmMultiWriteAttrService } from './dbm-multi-write-attr.service';

describe('DbmMultiWriteAttrService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DbmMultiWriteAttrService]
    });
  });

  it('should be created', inject([DbmMultiWriteAttrService], (service: DbmMultiWriteAttrService) => {
    expect(service).toBeTruthy();
  }));
});
