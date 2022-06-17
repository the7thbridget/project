import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../common/customer';
import { map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';
import { CustomerList } from '../common/customer-list';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private baseUrl = 'http://localhost:9090/api/customer/';

  /*
  public showCurrentData = new BehaviorSubject<any>([]);
  public showArchivedData = new BehaviorSubject<any>([]);

  viewCurrent= this.showCurrentData.asObservable();
  viewArchived= this.showArchivedData.asObservable();
  */

  constructor(private httpClient: HttpClient) { }

  addCustomer(body: any): Observable<GetPostResponse> {
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'add', body);
  }

  getCustomerList(): Observable<CustomerList> {
    return this.httpClient.get<GetResponse>(this.baseUrl + 'all').pipe(
      map(response => response.customers)
    );
  }

  updateDataView(body: any): Observable<GetResponse> {

    const params = new HttpParams()
      .set('viewCurrentData', body['viewCurrent'])
      .set('viewArhivedData', body['viewArchived']); // TODO: Change name according to API Doc
    console.log(params)
    return this.httpClient.get<GetResponse>(this.baseUrl + 'all', { params: params });
  }

  changeRatingService(body: any): Observable<GetPostResponse> {
    //console.log(body)
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const params = new HttpParams()
      .set('userId', body['userId'])
      .set('manualRating', body['manualRating']);

    console.log(params);

    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'manualReassignRating', params, {headers: myheader});
  }
  /*
  // Update data view
  updateViewCurrent(data:any) {
    this.showCurrentData.next(data);
  }

  updateViewArchived(data:any) {
    this.showArchivedData.next(data);
  }
  */
}

interface GetResponse {
  msg: string;
  code: number;
  customers: CustomerList;
}

interface GetPostResponse {
  msg: string;
  code: number;
}

interface Get500Response {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}