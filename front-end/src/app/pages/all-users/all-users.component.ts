import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { DashboardService } from 'src/app/service/dashboard.service';

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.scss']
})
export class AllUsersComponent implements OnInit {

  constructor(private service: DashboardService, private authSevice: AuthServiceService, private toastr: ToastrService) { }
  public userList: any;
  ngOnInit() {
    this.service.getAllUsers(this.authSevice.getLoggedUser()).subscribe({
      next: (results) => {
        this.userList = results;   
        console.log("User List: " + this.userList);  
      },
      error: (error) => {
        console.log(JSON.stringify(error));
        this.toastr.error('Servie unavailable');
      }
    })
  }
}
