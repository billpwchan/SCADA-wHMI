import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StepEditComponent } from './step-edit.component';

describe('StepEditComponent', () => {
  let component: StepEditComponent;
  let fixture: ComponentFixture<StepEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StepEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StepEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
