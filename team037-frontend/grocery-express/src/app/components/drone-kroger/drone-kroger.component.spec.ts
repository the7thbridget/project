import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DroneKrogerComponent } from './drone-kroger.component';

describe('DroneKrogerComponent', () => {
  let component: DroneKrogerComponent;
  let fixture: ComponentFixture<DroneKrogerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DroneKrogerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DroneKrogerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
