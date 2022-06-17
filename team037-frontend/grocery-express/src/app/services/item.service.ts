import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Item } from '../common/item';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  private baseUrl = 'http://localhost:9090/api/item/';
  private localFile = '../../assets/item_list.json';
  private krogerFile = '../../assets/item_list_kroger.json';
  private targetFile = '../../assets/item_list_target.json';

  constructor(private httpClient: HttpClient) { }

  // GET request with params
  getItemList(storeName: string): Observable<GetResponse> {
    const params = new HttpParams().set('storeName', storeName);
    console.log(params)

    return this.httpClient.get<GetResponse>(this.baseUrl + 'all', { params: params});
  }

  // getKrogerList(): Observable<Item[]> {
  //   const params = new HttpParams();
    
  //   return this.httpClient.get<GetResponse>(this.krogerFile).pipe( // TODO: Replace this.localFile with this.baseUrl 
  //     map(response => response.items)
  //   );
  // }

  getTargetList(): Observable<Item[]> {
    const params = new HttpParams();
    
    return this.httpClient.get<GetResponse>(this.targetFile).pipe( // TODO: Replace this.localFile with this.baseUrl 
      map(response => response.items)
    );
  }

  // POST request to add new item
  addItemService(body: any): Observable<GetPostResponse> {
    //console.log(body)
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'add', body);
  }
  
}

interface GetResponse {
  msg: string;
  code: number;
  items: Item[];
}

interface GetPostResponse {
  msg: string;
  code: number;
}