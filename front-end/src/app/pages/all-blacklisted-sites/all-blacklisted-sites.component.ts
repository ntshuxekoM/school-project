import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { DashboardService } from 'src/app/service/dashboard.service';

@Component({
  selector: 'app-all-blacklisted-sites',
  templateUrl: './all-blacklisted-sites.component.html',
  styleUrls: ['./all-blacklisted-sites.component.scss']
})
export class AllBlacklistedSitesComponent implements OnInit {

  constructor(private service: DashboardService, private authSevice: AuthServiceService, private toastr: ToastrService) { }
public siteList: any;
  ngOnInit() {
    this.service.getAllBlackListedSites(this.authSevice.getLoggedUser()).subscribe({
      next: (results) => {
        this.siteList = results;   
        console.log("Site List: " + this.siteList);  
      },
      error: (error) => {
        console.log("Error: " + error);
        this.toastr.error('Servie unavailable');
      }
    })
  }

}
