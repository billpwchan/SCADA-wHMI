import { TestBed, inject } from '@angular/core/testing';

import { GetChildrenAliasesService } from './get-children-aliases.service';

describe('GetChildrenService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GetChildrenAliasesService]
    });
  });

  it('should be created', inject([GetChildrenAliasesService], (service: GetChildrenAliasesService) => {
    expect(service).toBeTruthy();
  }));
});
