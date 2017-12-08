import { TestBed, inject } from '@angular/core/testing';

import { OlsService } from './ols.service';

describe('OlsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OlsService]
    });
  });

  it('should be created', inject([OlsService], (service: OlsService) => {
    expect(service).toBeTruthy();
  }));
});
