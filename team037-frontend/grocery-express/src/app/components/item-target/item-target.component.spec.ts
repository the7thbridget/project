import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemTargetComponent } from './item-target.component';

describe('ItemTargetComponent', () => {
  let component: ItemTargetComponent;
  let fixture: ComponentFixture<ItemTargetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemTargetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemTargetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
