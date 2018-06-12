import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgActiveButtonComponent } from './ng-active-button.component';

describe('NgActiveButtonComponent', () => {
  let component: NgActiveButtonComponent;
  let fixture: ComponentFixture<NgActiveButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgActiveButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgActiveButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
