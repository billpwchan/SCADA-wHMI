import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaSuppressAlarmComponent } from './ava-suppress-alarm.component';

describe('AvaSuppressAlarmComponent', () => {
  let component: AvaSuppressAlarmComponent;
  let fixture: ComponentFixture<AvaSuppressAlarmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvaSuppressAlarmComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvaSuppressAlarmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
