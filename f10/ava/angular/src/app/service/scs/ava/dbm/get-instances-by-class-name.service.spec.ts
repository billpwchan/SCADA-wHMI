import { TestBed, inject } from '@angular/core/testing';

import { GetInstancesByClassNameService } from './get-instances-by-class-name.service';

describe('GetInstancesByClassNameService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GetInstancesByClassNameService]
    });
  });

  it('should be created', inject([GetInstancesByClassNameService], (service: GetInstancesByClassNameService) => {
    expect(service).toBeTruthy();
  }));
});
