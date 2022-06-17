import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoginService } from 'src/app/services/login.service';
import { SettingsService } from 'src/app/settings.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public userType!: number;
  public subscription!: Subscription;

  showLogin = true;
  showRegister = false;
  showMock = false;
  loggedInType = localStorage.getItem('loggedInType')
  isLoggedin = localStorage.getItem('isLoggedin') == 'true'
  loggedUser = localStorage.getItem('loggedUser')
  loggedType = localStorage.getItem('loggedType')

  errCode = 0;
  errMsg!: string;
  loginClicked = false;
  logintype!: string;
  registerClicked = false;
  registertype!: string;
  registerSuccess = false;

  constructor(
    private loginService: LoginService,
    private settingsService: SettingsService // inject service
  ) { }

  public ngOnDestroy(): void {
    this.subscription.unsubscribe(); // onDestroy cancels the subscribe request
  }

  public ngOnInit(): void {
    // set subscribe to message service
    this.subscription = this.settingsService.getUserType().subscribe(msg => this.userType = msg);
  }

  login(form: any) {
    this.registerClicked = false;
    this.loginClicked = true;

    let loginForm = {
      username: form.value.username,
      password: form.value.password,
      userType: parseInt(form.value.logintype)
    }
    console.log(loginForm)

    this.loginService.loginService(loginForm).subscribe(
      (data) => {
        this.errCode = data.code;
        this.errMsg = data.msg;
        console.log(this.errCode)

        if (this.errCode == 200) {
          this.showLogin = false;
          let thisUserType = this.getUserType(loginForm.userType);
          switch (loginForm.userType) {
            case 1:
              this.loginAsAdmin();
              break;
            case 2:
              this.loginAsEmployee();
              break;
            case 3:
              this.loginAsCustomer();
              break;
          }
          localStorage.setItem('loggedUser', loginForm.username);
          localStorage.setItem('loggedType', thisUserType);
          this.loggedUser = localStorage.getItem('loggedUser')
          this.loggedType = localStorage.getItem('loggedType')

          window.location.reload();
        }
      }
    )

    //window.location.reload();
  }
  
  getUserType(idx: number): string {
    switch (idx) {
      case 1:
        return 'Administrator';
      case 2: this.loginAsEmployee();
        return 'Employee';

      case 3:
        return 'Customer';
      default:
        return 'Undefined user type';
    }
  }

  register(form: any) {
    this.loginClicked = false;
    this.registerClicked = true;

    let loginForm = {
      username: form.value.username,
      password: form.value.password,
      userType: parseInt(form.value.logintype)
    }
    console.log(form.value)
    console.log(loginForm)

    this.loginService.registerService(loginForm).subscribe(
      (data) => {
        this.errCode = data.code;
        this.errMsg = data.msg;
        console.log(this.errCode)

        if (this.errCode == 200) {
          this.registerSuccess = true;
        }
      }
    )

  }

  logout() {
    this.isLoggedin = false;
    localStorage.setItem('isLoggedin', 'false');
    localStorage.setItem('loggedInType', '');
    localStorage.setItem('loggedUser', '');
    localStorage.setItem('loggedType', '');
    this.settingsService.setUserType(0);
    console.log('logout');
  }

  loginAsAdmin() {
    this.isLoggedin = true;
    localStorage.setItem('isLoggedin', 'true');
    localStorage.setItem('loggedInType', '1');
    this.settingsService.setUserType(1);
    console.log(this.userType);
  }

  loginAsEmployee() {
    this.isLoggedin = true;
    localStorage.setItem('isLoggedin', 'true');
    localStorage.setItem('loggedInType', '2');
    this.settingsService.setUserType(2);
    console.log(this.userType);
  }

  loginAsCustomer() {
    this.isLoggedin = true;
    localStorage.setItem('isLoggedin', 'true');
    localStorage.setItem('loggedInType', '3');
    this.settingsService.setUserType(3);
    console.log(this.userType);
  }

  loadRegister() {
    this.loginClicked = false;
    this.registerClicked = false;

    this.showLogin = false;
    this.showRegister = true;
  }

  loadLogin() {
    this.loginClicked = false;
    this.registerClicked = false;

    this.showLogin = true;
    this.showRegister = false;
  }

  reloadpage() {
    console.log(this.loggedUser)
    //window.location.reload();
  }
}
