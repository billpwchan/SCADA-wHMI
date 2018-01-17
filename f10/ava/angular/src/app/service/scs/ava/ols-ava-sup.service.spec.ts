import { TestBed, inject } from '@angular/core/testing';

import { OlsAvaSupService } from './ols-ava-sup.service';

describe('AvaSupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OlsAvaSupService]
    });
  });

  it('should be created', inject([OlsAvaSupService], (service: OlsAvaSupService) => {
    expect(service).toBeTruthy();
  }));
});
