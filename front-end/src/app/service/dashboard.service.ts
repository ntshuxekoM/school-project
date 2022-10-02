import { Injectable } from '@angular/core';
import { LoginResponce } from '../model/login-responce';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { DashboardData } from '../model/dashboard-data';
import { BlacklistSite } from '../model/blacklist-site';
import { UserDetails } from '../model/user-details';
import { UserUrlRequest } from '../model/user-url-request';
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

  getAllBlackListedSites(user: LoginResponce) : Observable<any>{
    console.log("Getting All blacklisted sites");
   
    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token });
    let options = { headers: headers};

    return this.http.get<any>(this.appUrl + '/api/phishing-detector/find_all_blacklisted_sites', options);
  }


  getAllUsers(user: LoginResponce) : Observable<any>{
    console.log("Getting All users");
   
    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token });
    let options = { headers: headers};

    return this.http.get<any>(this.appUrl + '/api/user/find_all_users', options);
  }

  getAllUserUrlRequest(user: LoginResponce) : Observable<any>{
    console.log("Getting All users");
   
    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token });
    let options = { headers: headers};

    return this.http.get<any>(this.appUrl + '/api/phishing-detector/find_all_user_url_request/'+user.id, options);
  }
}
