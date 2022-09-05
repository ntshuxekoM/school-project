import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponce } from '../model/login-responce';

@Injectable({
  providedIn: 'root'
})
export class SearchService {


  private appUrl = environment.appUrl;

  constructor(private http: HttpClient) { }

  validateUrl(url: string, user: LoginResponce): Observable<any> {
    console.log(user.token);
    let headers = new HttpHeaders({'Content-Type': 'application/json', 'Authorization': 'Bearer ' + user.token});
    let options = { headers: headers };
    let jsonObject = this.prepareJsonObject(url, user.id);
    return this.http.post<any>(this.appUrl + '/api/phishing-detector/validate-url', jsonObject, options);
  }

  private prepareJsonObject(url: string, id: number) {
    const object = {
      userId: id,
      url: url
    }
    return JSON.stringify(object);
  }
}
