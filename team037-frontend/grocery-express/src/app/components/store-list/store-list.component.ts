import { Component, OnInit } from '@angular/core';
import { Store } from 'src/app/common/store';
import { StoreService } from 'src/app/services/store.service';
import { SettingsService } from 'src/app/settings.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-store-list',
  templateUrl: './store-list.component.html',
  //templateUrl: './store-list-table.component.html',
  styleUrls: ['./store-list.component.css']
})
export class StoreListComponent implements OnInit {
  loggedInType = localStorage.getItem('loggedInType')

  stores!: Store[];
  isShowing = false;
  errCode = 0;
  errMsg!: string;

  currentUser = localStorage.getItem("loggedInType");

  constructor(private storeService: StoreService, private settingsService: SettingsService,
     private readonly router: Router) { }

  ngOnInit(): void {
    this.listStores();
  }

  listStores() {
    this.storeService.getStoreList().subscribe(
      (data) => {
        this.stores = data;
      }
    )
  }

  addStore(form: any) {

    let newStore = {
      storeName: form.value.storeName,
      revenue: parseInt(form.value.revenue)
    }
    console.log(newStore);

    // Add new store
    this.storeService.addStoreService(newStore).subscribe(
      (data) => {
        //console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    )
  }

  // Update current store
  sendCurrentStore(newStore:string) {
    this.settingsService.updateCurrentStore(newStore);
  }

  resetForm(form: any) {
    form.resetForm();
    this.errCode = 0;
    this.errMsg = '';

    this.reloadComponent();
  }

  reloadComponent() {
    let currentUrl = this.router.url;
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([currentUrl]);
    }
}
