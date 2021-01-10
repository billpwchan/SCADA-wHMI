import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RttAppComponent } from './rtt-app.component';

describe('RttAppComponent', () => {
  let component: RttAppComponent;
  let fixture: ComponentFixture<RttAppComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RttAppComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RttAppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
