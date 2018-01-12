import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardEditControllerComponent } from './card-edit-controller.component';

describe('CardEditControllerComponent', () => {
  let component: CardEditControllerComponent;
  let fixture: ComponentFixture<CardEditControllerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardEditControllerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardEditControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
