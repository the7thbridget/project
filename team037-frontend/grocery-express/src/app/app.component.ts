import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { SettingsService } from './settings.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'grocery-express';
  currentUser = 'admin'; //NOTE: CHANGE THIS TO TOGGLE ADMIN VIEWS. ACCEPTABLE VALUES: admin, employee, none
  //userType = 0; // 0: not login; 1: admin; 2: employee; 3:customer

  public userType = 0;
  public subscription!: Subscription;
  public loggedInType = localStorage.getItem('loggedInType')
  public loggedUser = localStorage.getItem('loggedUser')
  public isLoggedin = localStorage.getItem('isLoggedin')

  constructor(
    private settingsService: SettingsService, // inject service
    private readonly router: Router
  ) { }

  public ngOnDestroy(): void {
    this.subscription.unsubscribe(); // onDestroy cancels the subscribe request
  }

  public ngOnInit(): void {
    // set subscribe to message service
    this.subscription = this.settingsService.getUserType().subscribe(msg => this.userType = msg);

    //console.log(this.isLoggedin)
    //console.log(this.loggedInType)
    if (this.isLoggedin == 'true' && this.loggedInType != null) {
      switch (parseInt(this.loggedInType)) {
        case 1:
          this.setAdmin();
          break;
        case 2: this.setEmployee();
          break;
        case 3: this.setCustomer();
          break;
      }
    }
  }



  logout() {
    localStorage.setItem('isLoggedin', 'false');
    localStorage.setItem('loggedInType', '');
    localStorage.setItem('loggedUser', '');
    localStorage.setItem('loggedType', '');
    this.settingsService.setUserType(0);
    console.log(this.userType);
    this.router.navigate(['/login']);
    //window.location.reload();
  }

  loginAsAdmin() {
    localStorage.setItem('isLoggedin', 'true');
    localStorage.setItem('loggedInType', '1');
    this.settingsService.setUserType(1);
    console.log(this.userType);
  }

  loginAsEmployee() {
    localStorage.setItem('isLoggedin', 'true');
    localStorage.setItem('loggedInType', '2');
    this.settingsService.setUserType(2);
    console.log(this.userType);
  }

  loginAsCustomer() {
    localStorage.setItem('isLoggedin', 'true');
    localStorage.setItem('loggedInType', '3');
    this.settingsService.setUserType(3);
    console.log(this.userType);
  }

  setAdmin() {
    this.settingsService.setUserType(1);
    console.log(this.userType);
  }

  setEmployee() {
    this.settingsService.setUserType(2);
    console.log(this.userType);
  }

  setCustomer() {
    this.settingsService.setUserType(3);
    console.log(this.userType);
  }
}
