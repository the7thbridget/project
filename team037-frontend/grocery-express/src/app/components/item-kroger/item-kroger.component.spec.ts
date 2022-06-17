import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemKrogerComponent } from './item-kroger.component';

describe('ItemKrogerComponent', () => {
  let component: ItemKrogerComponent;
  let fixture: ComponentFixture<ItemKrogerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemKrogerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemKrogerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
