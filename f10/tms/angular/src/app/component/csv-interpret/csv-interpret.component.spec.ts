import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CsvInterpretComponent } from './csv-interpret.component';

describe('CsvInterpretComponent', () => {
  let component: CsvInterpretComponent;
  let fixture: ComponentFixture<CsvInterpretComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CsvInterpretComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CsvInterpretComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
