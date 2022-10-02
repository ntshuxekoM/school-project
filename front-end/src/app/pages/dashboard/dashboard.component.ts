import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { DashboardData } from 'src/app/model/dashboard-data';
import { DashboardService } from 'src/app/service/dashboard.service';
// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2
} from "../../variables/charts";


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  public dashboardData: DashboardData;

  public showUserTable: boolean;

  public showSearchHistoty: boolean;

  constructor(private service: DashboardService, private authSevice: AuthServiceService, private toastr: ToastrService) { }

  ngOnInit() {
    this.service.getDashboardData(this.authSevice.getLoggedUser()).subscribe({
      next: (results) => {
        this.dashboardData = results;

        this.showUserTable = this.dashboardData.userDetailsList != null && this.dashboardData.userDetailsList.length > 0;
        this.showSearchHistoty = this.dashboardData.userUrlRequestList != null && this.dashboardData.userUrlRequestList.length > 0;
        
      },
      error: (error) => {
        console.log("Error: " + error);
        this.toastr.error('Servie unavailable');
      }
    })
  }


}


