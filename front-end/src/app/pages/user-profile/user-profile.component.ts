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
  public loginResponse: LoginResponce;


  constructor(private service: AuthServiceService,
    private toastr: ToastrService,
    private router: Router, private dservice: DashboardService) { }

  ngOnInit() {

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
    console.log(JSON.stringify(data));
    if (data.newPassword == data.confirmPassword) {
      let user = this.service.getLoggedUser();
      this.service.changePassword(data, user).subscribe({
        next: (results) => {
           this.toastr.success('Password updated successful!');

          this.loginResponse = this.service.getLoggedUser();
          this.service.saveLoggedUser(this.loginResponse, data.newPassword);

        },
        error: (error) => {
          console.log(JSON.stringify(error));
          this.toastr.error(error.error.message);
        }
      })
    } else {
      this.toastr.error('New password and confirm password do not match!');
    }

  }



  onUpdateProfile(data: any) {
    
    this.loginResponse = this.service.getLoggedUser();
    this.service.updateProfile(data, this.loginResponse).subscribe({
      next: (results) => {
        console.log(JSON.stringify(results));

        let loginRequest = {
          password: this.loginResponse.password,
          username: data.email
        };


        this.service.login(loginRequest).subscribe({

          next: (login_results) => {
            this.service.saveLoggedUser(login_results, this.loginResponse.password);
          },
          error: (error) => {
            console.log("Error: " + JSON.stringify(error));
            this.toastr.error('Unable to refresh token');
          }
        })

        this.toastr.success('User details updated!');


      },
      error: (error) => {
        console.log(JSON.stringify(error));
        this.toastr.error(error.error.message);
      }
    })
  }

}
