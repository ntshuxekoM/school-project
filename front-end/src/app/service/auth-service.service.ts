import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginResponce } from '../model/login-responce';
import { Router } from '@angular/router';
import { UserDetails } from '../model/user-details';

const USER_SESSION = "user-session";

@Injectable({
  providedIn: 'root'
})

export class AuthServiceService {

  private appUrl = environment.appUrl;


  constructor(private http: HttpClient, private router: Router) { }

  registerUser(data: any): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    return this.http.post<any>(this.appUrl + '/api/auth/signup', jsonObject, options);
  }

  login(data: any): Observable<LoginResponce> {
    console.log("Loging in... ");
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    return this.http.post<any>(this.appUrl + '/api/auth/signin', jsonObject, options);
  }

  changePassword(data: any, user: LoginResponce): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    return this.http.post<any>(this.appUrl + '/api/user/change_password', jsonObject, options);
  }

  updateProfile(data: any, user: LoginResponce): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    return this.http.post<any>(this.appUrl + '/api/user/update_users', jsonObject, options);
  }

  getUserById(data: LoginResponce): Observable<UserDetails> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("userId", data.id);
    let headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + data.token });
    let options = { headers: headers };
    return this.http.get<UserDetails>(this.appUrl + '/api/user/find_users/' + data.id, options);
  }

  saveLoggedUser(user: LoginResponce, password: string) {
    console.log("Save Logged User: " + JSON.stringify(user));

    var lr: any = {
      token: user.token,
      type: user.type,
      id: user.id,
      username: user.username,
      email: user.email,
      roles: user.roles,
      fullName: user.fullName,
      password: password,
    };

    window.sessionStorage.removeItem(USER_SESSION);
    window.sessionStorage.setItem(USER_SESSION, JSON.stringify(lr));

    console.log("Done saving logged in user");
  }

  public getLoggedUser(): LoginResponce | null {
    const user = window.sessionStorage.getItem(USER_SESSION);
    if (user) {
      return JSON.parse(user);
    }
    // return to login
    this.router.navigateByUrl("/dashboard");
    return null;

  }

  forgotPassword(data: any): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    console.log("jsonObject: " + JSON.stringify(jsonObject));
    return this.http.post<any>(this.appUrl + '/api/auth/forgot-password', jsonObject, options);
  }

}
