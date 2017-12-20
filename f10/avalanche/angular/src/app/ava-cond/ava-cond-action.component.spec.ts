import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaCondActionComponent } from './ava-cond-action.component';

describe('AvaCondActionComponent', () => {
  let component: AvaCondActionComponent;
  let fixture: ComponentFixture<AvaCondActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvaCondActionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvaCondActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
