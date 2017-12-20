import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaCondNameComponent } from './ava-cond-name.component';

describe('AvaCondNameComponent', () => {
  let component: AvaCondNameComponent;
  let fixture: ComponentFixture<AvaCondNameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvaCondNameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvaCondNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
