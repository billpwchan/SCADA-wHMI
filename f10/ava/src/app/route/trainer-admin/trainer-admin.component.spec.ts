import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainerAdminComponent } from './trainer-admin.component';

describe('TrainerAdminComponent', () => {
  let component: TrainerAdminComponent;
  let fixture: ComponentFixture<TrainerAdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrainerAdminComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrainerAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
