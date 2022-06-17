import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  // GLOBALLY-ACCESSIBLE DATA
  private userType = new Subject<number>();

  public currentStore = new BehaviorSubject<any>([]);
  public storeName!: string;
  private currentUser = new BehaviorSubject<any>([]);
  private currentSettings = new BehaviorSubject<any>([]);

  storeSource = this.currentStore.asObservable();
  userSource = this.currentUser.asObservable();
  settingsSource = this.currentSettings.asObservable();

  constructor(private httpClient: HttpClient) { }

  // Update current store globally
  updateCurrentStore(data:any) {
    //console.log(data)
    this.currentStore.next(data);
    this.storeName = data;
    //console.log(this.storeName)
  }

  // GETTERS FOR GLOBALLY-ACCESSIBLE DATA
  public getUserType(): Observable<number> {
    return this.userType.asObservable();
  }

  // SETTERS FOR GLOBALLY-ACCESSIBLE DATA
  public setUserType(userType: number): void { 
    this.userType.next(userType);
  }
  
}
