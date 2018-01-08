import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StepEditControllerComponent } from './step-edit-controller.component';

describe('StepEditControllerComponent', () => {
  let component: StepEditControllerComponent;
  let fixture: ComponentFixture<StepEditControllerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StepEditControllerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StepEditControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
