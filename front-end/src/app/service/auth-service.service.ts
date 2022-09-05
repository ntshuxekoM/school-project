import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginResponce } from '../model/login-responce';

const USER_SESSION = "user-session";

@Injectable({
  providedIn: 'root'
})

export class AuthServiceService {

  private appUrl = environment.appUrl;

  

  constructor(private http: HttpClient) { }

  registerUser(data: any): Observable<any> {
    let headers = new HttpHeaders({'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    return this.http.post<any>(this.appUrl + '/api/auth/signup', jsonObject, options);
  }

  login(data: any): Observable<LoginResponce> {
    let headers = new HttpHeaders({'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = JSON.stringify(data);
    return this.http.post<any>(this.appUrl + '/api/auth/signin', jsonObject, options);
  }

  saveLoggedUser(user: LoginResponce) {
    window.sessionStorage.removeItem(USER_SESSION);
    window.sessionStorage.setItem(USER_SESSION, JSON.stringify(user));
  }

  public getLoggedUser(): LoginResponce | null {
    const user = window.sessionStorage.getItem(USER_SESSION);
    if (user) {
      return JSON.parse(user);
    }
    return null;
  }

}
