import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReplayActionComponent } from './replay-action.component';

describe('ReplayActionComponent', () => {
  let component: ReplayActionComponent;
  let fixture: ComponentFixture<ReplayActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReplayActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReplayActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
