import { TestBed, inject } from '@angular/core/testing';

import { EnvironmentMappingService } from './environment-mapping.service';

describe('EnvironmentMappingService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EnvironmentMappingService]
    });
  });

  it('should be created', inject([EnvironmentMappingService], (service: EnvironmentMappingService) => {
    expect(service).toBeTruthy();
  }));
});
