import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaCondComponent } from './ava-cond.component';

describe('AvaCondComponent', () => {
  let component: AvaCondComponent;
  let fixture: ComponentFixture<AvaCondComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AvaCondComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvaCondComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
