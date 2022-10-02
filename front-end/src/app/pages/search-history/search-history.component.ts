import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { DashboardService } from 'src/app/service/dashboard.service';

@Component({
  selector: 'app-search-history',
  templateUrl: './search-history.component.html',
  styleUrls: ['./search-history.component.scss']
})
export class SearchHistoryComponent implements OnInit {

  constructor(private service: DashboardService, private authSevice: AuthServiceService, private toastr: ToastrService) { }
public userUrlRequestList: any;
  ngOnInit() {
    this.service.getAllUserUrlRequest(this.authSevice.getLoggedUser()).subscribe({
      next: (results) => {
        this.userUrlRequestList = results;   
        console.log("Site List: " + this.userUrlRequestList);  
      },
      error: (error) => {
        console.log("Error: " + error);
        this.toastr.error('Servie unavailable');
      }
    })
  }
}
