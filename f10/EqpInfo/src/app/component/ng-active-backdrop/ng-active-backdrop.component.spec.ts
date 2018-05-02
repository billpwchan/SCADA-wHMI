import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgActiveBackdropComponent } from './ng-active-backdrop.component';

describe('NgActiveBackdropComponent', () => {
  let component: NgActiveBackdropComponent;
  let fixture: ComponentFixture<NgActiveBackdropComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgActiveBackdropComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgActiveBackdropComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
