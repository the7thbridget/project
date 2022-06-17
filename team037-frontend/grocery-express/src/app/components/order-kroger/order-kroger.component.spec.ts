import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderKrogerComponent } from './order-kroger.component';

describe('OrderKrogerComponent', () => {
  let component: OrderKrogerComponent;
  let fixture: ComponentFixture<OrderKrogerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderKrogerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderKrogerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
