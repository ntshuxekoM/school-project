import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthServiceService } from 'src/app/service/auth-service.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  constructor(private service: AuthServiceService,
    private toastr: ToastrService,
    private router: Router) { }

  ngOnInit() {
  }


  onClickSubmit(data: any) {
    console.log("Request: "+JSON.stringify(data));
    this.service.forgotPassword(data).subscribe({
      next: (results) => {
        this.toastr.info(results.message);
        this.router.navigateByUrl("/login"); 
      },
      error: (error) => {
        console.log(JSON.stringify(error));
        this.toastr.error(error.error.message);
      }
    })
  }
}
