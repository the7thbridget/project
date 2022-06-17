import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '../common/store';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StoreService {

  private baseUrl = 'http://localhost:9090/api/store/';
  private localFile = '../../assets/store_list.json';

  constructor(private httpClient: HttpClient) { }

  getStoreList(): Observable<Store[]> {
    return this.httpClient.get<GetResponse>(this.baseUrl + 'all').pipe( // TODO: Replace this.localFile with this.baseUrl 
      map(response => response.stores)
    );
  }

  // POST request to add new store
  addStoreService(body: any): Observable<GetPostResponse> {
    //console.log(body)
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'add', body);
  }
}

interface GetResponse {
  msg: string;
  code: number;
  stores: Store[];
}

interface GetPostResponse {
  msg: string;
  code: number;
}