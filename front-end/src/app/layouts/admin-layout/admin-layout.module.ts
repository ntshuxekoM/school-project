import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClipboardModule } from 'ngx-clipboard';

import { AdminLayoutRoutes } from './admin-layout.routing';
import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { IconsComponent } from '../../pages/icons/icons.component';
import { MapsComponent } from '../../pages/maps/maps.component';
import { UserProfileComponent } from '../../pages/user-profile/user-profile.component';
import { TablesComponent } from '../../pages/tables/tables.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
// import { ToastrModule } from 'ngx-toastr';
import { SearchByUrlComponent } from 'src/app/pages/search/search-by-url/search-by-url.component';
import { AllBlacklistedSitesComponent } from 'src/app/pages/all-blacklisted-sites/all-blacklisted-sites.component';
import { AllUsersComponent } from 'src/app/pages/all-users/all-users.component';
import { SearchHistoryComponent } from 'src/app/pages/search-history/search-history.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    HttpClientModule,
    NgbModule,
    ClipboardModule
  ],
  declarations: [
    DashboardComponent,
    UserProfileComponent,
    TablesComponent,
    IconsComponent,
    MapsComponent,
    SearchByUrlComponent,
    AllBlacklistedSitesComponent,
    AllUsersComponent,
    SearchHistoryComponent
  ]
})

export class AdminLayoutModule {}
