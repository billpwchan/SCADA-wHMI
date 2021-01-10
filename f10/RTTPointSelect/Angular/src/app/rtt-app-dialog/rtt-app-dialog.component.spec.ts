import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RttAppDialogComponent } from './rtt-app-dialog.component';

describe('RttAppDialogComponent', () => {
  let component: RttAppDialogComponent;
  let fixture: ComponentFixture<RttAppDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RttAppDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RttAppDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
