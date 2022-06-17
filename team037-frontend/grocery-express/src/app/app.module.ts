import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { StoreListComponent } from './components/store-list/store-list.component';
import { StoreService } from './services/store.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Routes, RouterModule } from '@angular/router';

import { PilotListComponent } from './components/pilot-list/pilot-list.component';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { ItemListComponent } from './components/item-list/item-list.component';

import { ItemKrogerComponent } from './components/item-kroger/item-kroger.component';
import { ItemTargetComponent } from './components/item-target/item-target.component';
import { DroneKrogerComponent } from './components/drone-kroger/drone-kroger.component';
import { OrderKrogerComponent } from './components/order-kroger/order-kroger.component';
import { LoginComponent } from './components/login/login.component';
import { SettingsComponent } from './components/settings/settings.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StorageServiceModule } from 'ngx-webstorage-service';
import { LOCAL_STORAGE, StorageService } from 'ngx-webstorage-service';
//import { Ng2WebStorage } from 'ngx-webstorage';


const routes: Routes = [
  { path: 'stores', component: StoreListComponent },
  { path: 'pilots', component: PilotListComponent },
  { path: 'customers', component: CustomerListComponent },
  { path: 'items', component: ItemListComponent },
  { path: 'items/details', component: ItemKrogerComponent },
  { path: 'orders/details', component: OrderKrogerComponent },
  { path: 'drones/details', component: DroneKrogerComponent },
  { path: 'login', component: LoginComponent },
  { path: 'settings', component: SettingsComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/stores', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    StoreListComponent,
    PilotListComponent,
    CustomerListComponent,
    ItemListComponent,
    ItemKrogerComponent,
    ItemTargetComponent,
    DroneKrogerComponent,
    OrderKrogerComponent,
    LoginComponent,
    SettingsComponent
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule
  ],
  providers: [StoreService],
  bootstrap: [AppComponent]
})
export class AppModule { }
