import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Order } from 'src/app/common/order';
import { OrderArchived } from 'src/app/common/order-archived';
import { OrderList } from 'src/app/common/order-list';
import { DroneService } from 'src/app/services/drone.service';
import { OrderService } from 'src/app/services/order.service';
import { SettingsService } from 'src/app/settings.service';

@Component({
  selector: 'app-order-kroger',
  templateUrl: './order-kroger.component.html',
  styleUrls: ['./order-kroger.component.css']
})
export class OrderKrogerComponent implements OnInit {

  orders!: OrderList;
  archivedOrders!: OrderArchived[];
  currentOrders!: Order[];
  storeName!: string;
  isShowing = true;
  errCode!: number;
  errMsg!: string;
  showCurrentData = true;
  showArchiveData = false;
  currentOrderId!: number;
  thisOrderTotalPrice = 0;
  thisOrderTotalWeight = 0;
  showDetails = false;
  showCurrentDetails = false;
  showArchivedDetails = false;
  showCheckoutMsg = false;
  showCancelMsg = false;
  hideloader = false;
  hideReload = true;
  showAddOrder = false;

  isCustomer = localStorage.getItem('loggedInType') == '3';
  userName = localStorage.getItem('loggedUser')

  constructor(private orderService: OrderService, private droneService: DroneService, private settingsService: SettingsService, private readonly router: Router) { }

  ngOnInit(): void {

    this.settingsService.currentStore.subscribe(res => this.storeName = res);
    //console.log('current order store is: ' + this.storeName)
    if (this.isCustomer) {
      this.listOrdersForCustomer(this.storeName, this.userName);
    } else {
      this.listOrders(this.storeName);
    }
  }

  listOrders(storeName: string) {
    this.orderService.getOrderList(storeName).subscribe(
      (data) => {
        if (data) {
          this.hideloader = true;
        }

        // Reload component if no data is loaded
        if (typeof data.orders == 'undefined') {
          this.hideReload = false;
        }

        this.errCode = data.code;
        this.orders = data.orders;
        this.archivedOrders = this.orders.archivedOrders;
        this.currentOrders = this.orders.currentOrders;
      }
    )
  }

  // getOrderListForCustomer
  listOrdersForCustomer(storeName: string, userName: any) {
    this.orderService.getOrderListForCustomer(storeName, userName).subscribe(
      (data) => {
        console.log(data.code)
        if (data) {
          this.hideloader = true;
        }

        console.log('customer order loaded')
        // Reload component if no data is loaded
        if (typeof data.orders == 'undefined') {
          this.hideReload = false;
        }

        this.errCode = data.code;
        this.orders = data.orders
        console.log(this.orders)
        this.archivedOrders = this.orders.archivedOrders;
        this.currentOrders = this.orders.currentOrders;
      }
    )
  }

  calculateCurrentOrder() {
    // loop through all current orders to find the correct order
    this.currentOrders.forEach((element) => {
      if (element.order_id == this.currentOrderId) {
        // inside the target order, loop through all items
        element.items.forEach((thisItem) => {
          this.thisOrderTotalPrice = thisItem.total_price + this.thisOrderTotalPrice;
          this.thisOrderTotalWeight = thisItem.total_weight + this.thisOrderTotalWeight;
        })

      }
    })

  }

  calculateArchiveOrder() {
    // loop through all current orders to find the correct order
    this.archivedOrders.forEach((element) => {
      if (element.archive_order_id == this.currentOrderId) {
        // inside the target order, loop through all items
        element.items.forEach((thisItem) => {
          this.thisOrderTotalPrice = thisItem.total_price + this.thisOrderTotalPrice;
          this.thisOrderTotalWeight = thisItem.total_weight + this.thisOrderTotalWeight;
        })

      }
    })

  }

  updateView(form: any) {

  }

  setCurrentOrder(orderId: number) {
    this.currentOrderId = orderId;
    console.log(this.currentOrderId)
  }

  // Update current store in service
  sendCurrentStore(newStore: string) {
    console.log(this.storeName);
    // console.log(newStore)
    this.settingsService.updateCurrentStore(newStore);
    //this.reloadComponent();
    this.router.navigate(['items/details']);
  }

  setCurrentViewDetails() {
    this.showDetails = true;
    this.showCurrentDetails = true;
  }

  seArchivedViewDetails() {
    this.showDetails = true;
    this.showArchivedDetails = true;
  }

  reloadComponent() {
    let currentUrl = this.router.url;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([currentUrl]);
  }

  addOrder(form: any) {
    this.showAddOrder = true;

    //console.log(form.value)
    let newOrder = {
      storeName: this.storeName,
      orderName: form.value.orderName,
      droneIdentifier: form.value.droneIdentifier,
      customerUsername: form.value.customerUsername
    }

    //console.log(newOrder);

    // Add new order
    this.orderService.addOrderService(newOrder).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    );

  }

  resetForm(form: any) {
    form.resetForm();
    this.errCode = 0;
    this.errMsg = '';
    this.reloadComponent();
  }

  reloadThis() {

  }

  cancelOrder(storeName: string, orderName: string) {
    this.showCancelMsg = true;

    let orderToRemove = {
      storeName: storeName,
      orderName: orderName
    }

    //console.log(storeName, orderName)
    this.orderService.cancelOrderService(orderToRemove).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
        if (this.errCode == 200) {
          this.reloadComponent();
        }
      }
    )
  }

  checkoutOrder(storeName: string, orderName: string) {
    this.showCheckoutMsg = true;

    let orderToPurchase = {
      storeName: storeName,
      orderName: orderName
    }

    //console.log(storeName, orderName)
    this.orderService.purchaseOrderService(orderToPurchase).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
        // if (this.errCode==200) {
        //   this.reloadComponent();
        // }
      }
    )

  }

  reassignDrone(form: any) {
    this.errCode = 0;
    this.errMsg = '';

    let newDrone = {
      userId: this.currentOrderId,
      droneId: form.value.droneId
    }

    this.droneService.reassignDroneService(newDrone).subscribe(
      (data) => {
        console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      })

    if (this.errCode != 200) {
      console.log('wrong');
      this.errCode = 500;
    }

    if (this.errCode == 200) {
      form.resetForm();
    }
  }

}
