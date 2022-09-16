import { Injectable } from '@angular/core';
import { LoginResponce } from '../model/login-responce';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { DashboardData } from '../model/dashboard-data';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private appUrl = environment.appUrl;
  constructor(private http: HttpClient) { }

  getDashboardData(user: LoginResponce) : Observable<DashboardData>{
    console.log("Getting dashboard data");
    let queryParams = new HttpParams();
    queryParams = queryParams.append("userId", user.id);

    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token });
    let options = { headers: headers};

    return this.http.get<DashboardData>(this.appUrl + '/api/phishing-detector/get-dashboard-data/'+user.id, options);
  }
}
