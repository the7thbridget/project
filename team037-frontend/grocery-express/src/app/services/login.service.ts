import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '../common/store';
import { map } from 'rxjs/operators';
import { User } from '../common/user';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private baseUrl = 'http://localhost:9090/api/user/';

  constructor(private httpClient: HttpClient) { }

  loginService(body:any): Observable<GetLoginResponse> {
    return this.httpClient.post<GetLoginResponse>(this.baseUrl + '/login', body);
  }

  registerService(body:any): Observable<GetRegisterResponse> {
    return this.httpClient.post<GetRegisterResponse>(this.baseUrl + '/register', body);
  }
}

interface GetLoginResponse {
  msg: string;
  code: number;
  functions: any;
}

interface GetRegisterResponse {
  msg: string;
  code: number;
}