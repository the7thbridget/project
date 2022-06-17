import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../common/order';
import { map } from 'rxjs/operators';
import { OrderList } from '../common/order-list';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl = 'http://localhost:9090/api/order/';
  private localFile = '../../assets/order.json';
  private krogerFile = '../../assets/order_kroger.json';

  constructor(private httpClient: HttpClient) { }

  // GET request with params
  getOrderList(storeName: string): Observable<GetResponse> {
    const params = new HttpParams().set('storeName', storeName);

    return this.httpClient.get<GetResponse>(this.baseUrl + 'all', { params: params });

    // return this.httpClient.get<GetResponse>(this.baseUrl + 'all', { params: params}).pipe(
    //   map(response => response.orders)
    // );
  }

  // GET order for logged customer
  getOrderListForCustomer(storeName: string, username: string): Observable<GetResponse> {
    const params = new HttpParams()
      .set('storeName', storeName)
      .set('username', username);
    console.log(params)

    let orderData = this.httpClient.get<GetResponse>(this.baseUrl + 'showCustomerOrder', { params: params });
    console.log('before')
    console.log(orderData)
    console.log('after')
    return orderData;
  }

  // GET order details for a specific order
  /*
  getOrderDetailService(storeName: string, orderId: number): Observable<Order> {
    const params = new HttpParams().set('storeName', storeName);

    let thisOrder = this.httpClient.get<GetResponse>(this.krogerFile).pipe( // TODO: Replace this.localFile with this.baseUrl
      map(response => response.orders)
    );

    return thisOrder.find((i: { order_id: number; }) => i.order_id === orderId)
  }
  */

  // POST: Add new order
  addOrderService(body: any): Observable<GetPostResponse> {
    console.log(body);
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'add', body);
  }

  // POST: add item for an order
  addOrderItemService(body: any): Observable<GetPostResponse> {
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const params = new HttpParams()
      .set('storeName', body['storeName'])
      .set('orderName', body['orderName'])
      .set('itemName', body['itemName'])
      .set('quantity', body['quantity'])
      .set('unitPrice', body['unitPrice']);

    console.log(params);

    // NEED HEADER!!!
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'request', params, {headers: myheader});
  }

  // POST: cancel order
  cancelOrderService(body: any): Observable<GetPostResponse> {
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const params = new HttpParams()
      .set('storeName', body['storeName'])
      .set('orderName', body['orderName']);

    console.log(params);

    // NEED HEADER!!!
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'cancel', params, {headers: myheader});
  }

  // POST: cancel order
  purchaseOrderService(body: any): Observable<GetPostResponse> {
    const myheader = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const params = new HttpParams()
      .set('storeName', body['storeName'])
      .set('orderName', body['orderName']);

    console.log(params);

    // NEED HEADER!!!
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'purchase', params, {headers: myheader});
  }

  // getKrogerList(): Observable<Order[]> {
  //   const params = new HttpParams();

  //   return this.httpClient.get<GetResponse>(this.krogerFile).pipe( // TODO: Replace this.localFile with this.baseUrl
  //     map(response => response.orders)
  //   );
  // }

}

interface GetResponse {
  msg: string;
  code: number;
  orders: OrderList;
}

interface GetPostResponse {
  msg: string;
  code: number
}

interface Get500Response {
  timestamp: string;
  status: number;
  error:string;
  message:string;
  path: string;
}
