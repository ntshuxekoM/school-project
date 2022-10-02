import { Component, OnInit, ElementRef } from '@angular/core';
import { SearchService } from 'src/app/service/search.service';
import { AuthServiceService } from 'src/app/service/auth-service.service';
import { ToastrService } from 'ngx-toastr';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';


@Component({
  selector: 'app-search-by-url',
  templateUrl: './search-by-url.component.html',
  styleUrls: ['./search-by-url.component.css']
})
export class SearchByUrlComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  public searchUrl: string;
  public safeSite: boolean;
  public cardToShow: string;

  constructor(private authService: AuthServiceService, private searchService: SearchService, private toastr: ToastrService, private element: ElementRef, location: Location) {
    this.location = location;
  }

  ngOnInit(): void {
    this.cardToShow = "info";
  }

  getTitle() {
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if (titlee.charAt(0) === '#') {
      titlee = titlee.slice(1);
    }

    for (var item = 0; item < this.listTitles.length; item++) {
      if (this.listTitles[item].path === titlee) {
        return this.listTitles[item].title;
      }
    }
    return 'Dashboard';
  }

  onClickSubmit(data: any) {
    this.searchUrl = data.url;
    this.cardToShow = "loading";
    let user = this.authService.getLoggedUser();
    this.searchService.validateUrl(data.url, user).subscribe({
      next: (results) => {
        console.log(JSON.stringify(results));

        if (results.success) {
          this.cardToShow = "safe";
        } else {
          this.cardToShow = "unsafe";
        }

      },
      error: (error) => {
        console.log(JSON.stringify(error));
        this.toastr.error(error.error.message);
        this.cardToShow = "info";
      }
    })
  }

}
