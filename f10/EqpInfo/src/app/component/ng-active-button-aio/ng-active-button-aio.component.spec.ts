import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgActiveButtonAioComponent } from './ng-active-button-aio.component';

describe('NgActiveButtonAioComponent', () => {
  let component: NgActiveButtonAioComponent;
  let fixture: ComponentFixture<NgActiveButtonAioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgActiveButtonAioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgActiveButtonAioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
