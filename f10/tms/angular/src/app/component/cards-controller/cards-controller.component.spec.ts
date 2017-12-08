import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardsControllerComponent } from './cards-controller.component';

describe('CardsControllerComponent', () => {
  let component: CardsControllerComponent;
  let fixture: ComponentFixture<CardsControllerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardsControllerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardsControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
