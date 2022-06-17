import { Component, OnInit } from '@angular/core';
import { Drone } from 'src/app/common/drone';
import { DroneService } from 'src/app/services/drone.service';
import { SettingsService } from 'src/app/settings.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-drone-kroger',
  templateUrl: './drone-kroger.component.html',
  styleUrls: ['./drone-kroger.component.css']
})
export class DroneKrogerComponent implements OnInit {  
  loggedInType = localStorage.getItem('loggedInType')

  drones!: Drone[];
  storeName!: string;
  users: any;
  errCode = 0;
  errMsg!: string;
  hideloader = false;
  thisDroneIdentifier!: string;

  constructor(private droneService: DroneService,
    private settingsService: SettingsService, private readonly router: Router) { }

  ngOnInit(): void {
    this.settingsService.currentStore.subscribe(res => this.storeName = res);
    //console.log(this.settingsService.currentStore)

    this.listDrones(this.storeName);
  }

  listDrones(storeName: string): void {
    //console.log('current is: ' + storeName)

    this.droneService.getDroneList(storeName).subscribe(data => {
      if (data) {
        this.hideloader = true;
      }
      //console.log(data);
      this.drones = data;
    })
  }

  flyDrone(form: any) {
    let newPilot = {
      storeName: this.storeName,
      droneIdentifier: this.thisDroneIdentifier,
      pilotAccount: form.value.pilotAccount
    }
    console.log(newPilot);

    
    this.droneService.flyDroneService(newPilot).subscribe(
      (data) => {
        //console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    )
    
  }

  addDrone(form: any) {

    let newDrone = {
      storeName: this.storeName,
      droneIdentifier: form.value.droneIdentifier,
      maxCapacity: parseInt(form.value.maxCapacity),
      maxTrip: parseInt(form.value.maxTrip)
    }
    console.log(newDrone);

    // Add new drone
    this.droneService.addDroneService(newDrone).subscribe(
      (data) => {
        //console.log(data);
        this.errCode = data.code;
        this.errMsg = data.msg;
      }
    )
  }

  // Update current store
  sendCurrentStore(newStore: string) {
    console.log(this.storeName);
    // console.log(newStore)
    this.settingsService.updateCurrentStore(newStore);
    //this.router.navigate(['/drones/stores']);
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

  setThisDroneIdentifier(droneIdentifier: string) {
    this.thisDroneIdentifier = droneIdentifier;
    console.log(this.thisDroneIdentifier)
  }
  
}
