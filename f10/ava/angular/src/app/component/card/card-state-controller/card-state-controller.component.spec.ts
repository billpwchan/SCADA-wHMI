import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardStateControllerComponent } from './card-state-controller.component';

describe('CardStateControllerComponent', () => {
  let component: CardStateControllerComponent;
  let fixture: ComponentFixture<CardStateControllerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardStateControllerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardStateControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
