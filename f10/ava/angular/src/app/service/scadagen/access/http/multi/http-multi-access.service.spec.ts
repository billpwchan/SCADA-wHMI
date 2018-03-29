import { TestBed, inject } from '@angular/core/testing';

import { HttpMultiAccessService } from './http-multi-access.service';

describe('HttpMultiAccessService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpMultiAccessService]
    });
  });

  it('should be created', inject([HttpMultiAccessService], (service: HttpMultiAccessService) => {
    expect(service).toBeTruthy();
  }));
});
