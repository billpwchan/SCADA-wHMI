import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgActiveTextComponent } from './ng-active-text.component';

describe('NgActiveTextComponent', () => {
  let component: NgActiveTextComponent;
  let fixture: ComponentFixture<NgActiveTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgActiveTextComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgActiveTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
