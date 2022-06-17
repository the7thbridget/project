import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Item } from 'src/app/common/item';
import { ItemService } from 'src/app/services/item.service';
import { OrderService } from 'src/app/services/order.service';
import { SettingsService } from 'src/app/settings.service';

@Component({
  selector: 'app-item-kroger',
  templateUrl: './item-kroger.component.html',
  styleUrls: ['./item-kroger.component.css']
})
export class ItemKrogerComponent implements OnInit {

  items!: Item[];
  storeName!: string;
  errCode = 0;
  errMsg!: string;
  thisItemName!: string;
  loggedInType = localStorage.getItem('loggedInType');

  constructor(private itemService: ItemService, 
    private settingsService: SettingsService,
    private readonly router: Router,
    private orderService:OrderService) { }

  ngOnInit(): void {
    this.settingsService.currentStore.subscribe(res=> this.storeName = res);
    console.log(this.storeName)

    this.listItems(this.storeName);
  }

  listItems(storeName: string) {
    this.itemService.getItemList(storeName).subscribe(
      (data) => {
        
        this.items = data.items;
        console.log(this.items)
      }
    )
  }

  addItem(form: any) {

    let newItem = {
      storeName: this.storeName,
      itemName: form.value.itemName,
      weight: parseInt(form.value.weight)
    }
    console.log(newItem);

    // Add new item
    this.itemService.addItemService(newItem).subscribe(
      (data) => {
        //console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    )
  }

  // Add item for an order
  addItemToOrder(form: any) {
    let newItem = {
      storeName: this.storeName,
      orderName: form.value.orderName,
      itemName: this.thisItemName,
      quantity: parseInt(form.value.quantity),
      unitPrice: parseInt(form.value.unitPrice)
    }

    //  const storeName = form.value.storeName;
    //  const orderName = form.value.orderName;
    //  const itemName = form.value.orderName;
    //  const quantity = parseInt(form.value.quantity);
    //  const unitPrice = parseInt(form.value.unitPrice);    
    
    console.log(newItem);

    
    // Add new item
    this.orderService.addOrderItemService(newItem).subscribe(
      (data) => {
        //console.log(data.status);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    )
    

  }

  // Update current store
  sendCurrentStore(newStore:string) {
    console.log(this.storeName);
    // console.log(newStore)
    this.settingsService.updateCurrentStore(newStore);
    this.reloadComponent();
  }

  resetForm(form: any) {
    form.resetForm();
    this.errCode = 0;
    this.errMsg = '';
  }

  reloadComponent() {
    let currentUrl = this.router.url;
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([currentUrl]);
    }

    setThisItem(itemName:string) {
      console.log(itemName);
      this.thisItemName = itemName;
      console.log('this item is: ' + this.thisItemName)
    }

}
