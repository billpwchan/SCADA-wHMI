import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgActiveButtonDioComponent } from './ng-active-button-dio.component';

describe('NgActiveButtonDioComponent', () => {
  let component: NgActiveButtonDioComponent;
  let fixture: ComponentFixture<NgActiveButtonDioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgActiveButtonDioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgActiveButtonDioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
