import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  currentUser: any;

  constructor(private service: AuthServiceService,
    private toastr: ToastrService,
    private router: Router) { }

  ngOnInit() {
    this.currentUser = this.service.getLoggedUser();
  }

  onClickSubmit(data: any) {
    let user = this.currentUser;
    this.service.changePassword(data , user).subscribe({
      next:(results) => {
        console.log(JSON.stringify(results));
        this.toastr.show(JSON.stringify(results));
      },
      error: (error) => {
        console.log(JSON.stringify(error));
        this.toastr.error(error.error.message);
      }
    })
  }

  
  onClickSubmitEdit(data: any){
    let user = this.service.getLoggedUser();
    this.service.updateProfile(data, user).subscribe({
      next:(results) => {
        console.log(JSON.stringify(results));
        this.toastr.show(JSON.stringify(results));
      },
      error: (error) => {
        console.log(JSON.stringify(error));
        this.toastr.error(error.error.message);
      }
    })
  }

}
