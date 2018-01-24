import { TestBed, inject } from '@angular/core/testing';

import { MultiWriteService } from './multi-write.service';

describe('MultiWriteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MultiWriteService]
    });
  });

  it('should be created', inject([MultiWriteService], (service: MultiWriteService) => {
    expect(service).toBeTruthy();
  }));
});
