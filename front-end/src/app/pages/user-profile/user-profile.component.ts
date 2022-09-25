import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { DashboardData } from 'src/app/model/dashboard-data';
import { UserDetails } from 'src/app/model/user-details';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { DashboardService } from 'src/app/service/dashboard.service';
import { LoginResponce } from 'src/app/model/login-responce';
import { NavbarComponent } from 'src/app/components/navbar/navbar.component';


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  currentUser: any;

  public userDetails: UserDetails;
  public dashboardData: DashboardData;
  public loginResponse :LoginResponce;
  public totalRequest: any;

  constructor(private service: AuthServiceService,
    private toastr: ToastrService,
    private router: Router, private dservice: DashboardService ) { }

  ngOnInit() {
    this.totalRequest = 100; // todo call service
    this.currentUser = this.service.getLoggedUser();
    this.service.getUserById(this.currentUser).subscribe({
      next: (result) => {
        this.userDetails = result;
      },
      error: (error) => {
        console.log("Error: " + error);
        this.toastr.error('Servie unavailable');
      }
    })
    this.dservice.getDashboardData(this.service.getLoggedUser()).subscribe({
      next: (results) => {
        this.dashboardData = results;
      }
    })
  }

  onUpdatePassword(data: any) {
       if (data.newPassword == data.confirmPassword) {
      let user = this.currentUser;
      this.service.changePassword(data, user).subscribe({
        next: (results) => {
          console.log("Change Password request " + JSON.stringify(results));
          this.toastr.success('Password updated successful!');
        },
        error: (error) => {
          console.log(JSON.stringify(error));
          this.toastr.error(error.error.message);
        }
      })
    } else {
      this.toastr.show('New password and confirm password do not match!');
    }

  }



  onUpdateProfile(data: any) {
    console.log( 'User details ' + JSON.stringify(data));
     this.loginResponse = this.service.getLoggedUser();
     this.service.updateProfile(data, this.loginResponse).subscribe({
       next: (results) => {
         console.log(JSON.stringify(results));
         this.toastr.success('User details updated!');
         this.loginResponse.setFullName(data.name + ' ' + data.surname);
       },
       error: (error) => {
         console.log(JSON.stringify(error));
         this.toastr.error(error.error.message);
       }
     })
  }

}
