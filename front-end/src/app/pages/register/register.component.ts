import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from '../../service/auth-service.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  constructor(private service: AuthServiceService,private toastr: ToastrService , private router: Router) { }
  
  ngOnInit() {

  }

  onClickSubmit(data: any) {
    console.log("Registering user: "+JSON.stringify(data));
    this.service.registerUser(data).subscribe({
      next:(results) => {
        this.toastr.info(results.message);
        this.router.navigateByUrl("/login");
      },
      error: (error) => {
        console.log("Error: "+JSON.stringify(error));
        this.toastr.error(error.error.message);
      }
    })
  }

}
