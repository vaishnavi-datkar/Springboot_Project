import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Prescriptionform } from './prescriptionform';

describe('Prescriptionform', () => {
  let component: Prescriptionform;
  let fixture: ComponentFixture<Prescriptionform>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Prescriptionform]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Prescriptionform);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
