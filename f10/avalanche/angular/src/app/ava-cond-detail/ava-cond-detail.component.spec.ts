import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaCondDetailComponent } from './ava-cond-detail.component';

describe('AvaCondDetailComponent', () => {
  let component: AvaCondDetailComponent;
  let fixture: ComponentFixture<AvaCondDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvaCondDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvaCondDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
