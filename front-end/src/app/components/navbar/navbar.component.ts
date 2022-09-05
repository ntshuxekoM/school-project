import { Component, OnInit, ElementRef } from '@angular/core';
import { ROUTES } from '../sidebar/sidebar.component';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { Router } from '@angular/router';
import { SearchService } from 'src/app/service/search.service';
import { AuthServiceService } from '../../service/auth-service.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  constructor(location: Location,  private element: ElementRef, 
    private router: Router,
    private searchService: SearchService,
    private authService: AuthServiceService,
    private toastr: ToastrService) {
    this.location = location;
  }

  ngOnInit() {
    this.listTitles = ROUTES.filter(listTitle => listTitle);
  }
  getTitle(){
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if(titlee.charAt(0) === '#'){
        titlee = titlee.slice( 1 );
    }

    for(var item = 0; item < this.listTitles.length; item++){
        if(this.listTitles[item].path === titlee){
            return this.listTitles[item].title;
        }
    }
    return 'Dashboard';
  }

  onClickSubmit(data: any) {
    let user = this.authService.getLoggedUser();
    this.searchService.validateUrl(data, user).subscribe({
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
