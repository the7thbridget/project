import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pilot } from '../common/pilot';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PilotService {

  private baseUrl = 'http://localhost:9090/api/pilot/';

  constructor(private httpClient: HttpClient) { }

  // add new pilot
  testPost(body:any): Observable<GetPostResponse>{
    return this.httpClient.post<GetPostResponse>(this.baseUrl + 'add', body);
  }


  getPilotList(): Observable<Pilot[]> {
    return this.httpClient.get<GetResponse>(this.baseUrl + 'all').pipe(
      map(response => response.pilots)
    );
  }
}

interface GetResponse {
  msg: string;
  code: number;
  pilots: Pilot[];
}

interface GetPostResponse {
  msg: string;
  code: number;
}