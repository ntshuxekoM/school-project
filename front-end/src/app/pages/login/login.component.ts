import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthServiceService } from '../../service/auth-service.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  constructor(private service: AuthServiceService,
              private toastr: ToastrService,
              private router: Router) {}

  ngOnInit() {
  }
  ngOnDestroy() {
  }

  onClickSubmit(data: any) {
    this.service.login(data).subscribe({
      next:(results) => {
        this.service.saveLoggedUser(results);
        this.router.navigateByUrl("/dashboard");
      },
      error: (error) => {
        this.toastr.error('Invalid login details , Please enter the correct details');
      }
    })
  }

}
