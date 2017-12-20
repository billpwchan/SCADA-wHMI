import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaTriggerCondComponent } from './ava-trigger-cond.component';

describe('AvaTriggerCondComponent', () => {
  let component: AvaTriggerCondComponent;
  let fixture: ComponentFixture<AvaTriggerCondComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvaTriggerCondComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvaTriggerCondComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
