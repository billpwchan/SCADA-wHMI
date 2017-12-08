import { TestBed, inject } from '@angular/core/testing';

import { SettingService } from './setting.service';

describe('DacSimService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SettingService]
    });
  });

  it('should be created', inject([SettingService], (service: SettingService) => {
    expect(service).toBeTruthy();
  }));
});
