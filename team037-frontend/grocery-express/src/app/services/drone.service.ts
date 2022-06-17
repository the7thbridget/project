import { HttpClient, HttpParams, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { Drone } from '../common/drone';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DroneService {

  private baseUrl = 'http://localhost:9090/api/drone/'; // URL To show all drones in a store
  private testUrl = 'https://jsonplaceholder.typicode.com/users';

  constructor(private httpClient: HttpClient) { }

  // Testing GET requests
  testGetDrone():Observable<Drone[]> {
    const storeName = 'publix';
    const params = new HttpParams().set('storeName', storeName);
    //console.log((this.baseUrl + 'all?storeName=kroger'))

    return this.httpClient.get<GetResponse>(this.baseUrl + 'all', { params: params}).pipe( // pipe (RXJS operator) combines multiple functions into single function
      map(response => response.drones)
    );

    /*
    const params = new HttpParams().set('pagenNum', '2');
    return this.httpClient.get<TestGet>(this.baseUrl + '/all', {params: params})
    */
  }

  // GET request with params
  getDroneList(storeName: string): Observable<Drone[]> {
    const params = new HttpParams().set('storeName', storeName);
    //console.log(params)
    //console.log((this.baseUrl + 'all?storeName=kroger'))

    return this.httpClient.get<GetResponse>(this.baseUrl + 'all', { params: params}).pipe( // pipe (RXJS operator) combines multiple functions into single function
      map(response => response.drones)
    );
  }

  // POST request to add new drone
  addDroneService(body: any): Observable<GetPostResponse> {
    //console.log(body)
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'add', body);
  }

  // POST with params
  flyDroneService(body: any): Observable<GetPostResponse> {
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const params = new HttpParams()
      .set('storeName', body['storeName'])
      .set('droneIdentifier', body['droneIdentifier'])
      .set('pilotAccount', body['pilotAccount']);

    console.log(params);

    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'fly', params, {headers: myheader});
  }

  // POST with params 
  reassignDroneService(body: any): Observable<GetPostResponse> {
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const params = new HttpParams()
      .set('orderId', body['orderId'])
      .set('droneId', body['droneId']);

    //console.log(params);

    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'manualReassignDrone', params, {headers: myheader});

  }

  // handleError(err: HttpErrorResponse): Observable<never> {
  //   return throwError(() => err);
  // }

}

interface GetResponse {
  msg: string;
  code: number;
  drones: Drone[];
}

interface TestGet {
  name: string;
  email: string;
}

interface GetPostResponse {
  msg: string;
  code: number;
}