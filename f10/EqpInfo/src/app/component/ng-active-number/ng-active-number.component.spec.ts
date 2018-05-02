import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgActiveNumberComponent } from './ng-active-number.component';

describe('NgActiveNumberComponent', () => {
  let component: NgActiveNumberComponent;
  let fixture: ComponentFixture<NgActiveNumberComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgActiveNumberComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgActiveNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
