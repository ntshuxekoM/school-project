import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { DashboardData } from 'src/app/model/dashboard-data';

// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2
} from "../../variables/charts";
import { DashboardService } from 'src/app/service/dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  public dashboardData: DashboardData;

  public showUserTable: boolean;

  constructor(private service: DashboardService, private authSevice: AuthServiceService, private toastr: ToastrService) { }

  ngOnInit() {
    this.service.getDashboardData(this.authSevice.getLoggedUser()).subscribe({
      next: (results) => {
        this.dashboardData = results;
        if(this.dashboardData.userDetailsList !=null && this.dashboardData.userDetailsList.length>0){
          this.showUserTable=true;
        }else {
          this.showUserTable=false;
        }
        console.log("User Size: " + this.dashboardData.siteList.length);


      },
      error: (error) => {
        console.log("Error: "+error);
        this.toastr.error('Servie unavailable');
      }
    })
  }


}


