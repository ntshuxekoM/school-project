import { Routes } from '@angular/router';

import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { IconsComponent } from '../../pages/icons/icons.component';
import { MapsComponent } from '../../pages/maps/maps.component';
import { UserProfileComponent } from '../../pages/user-profile/user-profile.component';
import { TablesComponent } from '../../pages/tables/tables.component';
import { SearchByUrlComponent } from 'src/app/pages/search/search-by-url/search-by-url.component';
import { AllBlacklistedSitesComponent } from 'src/app/pages/all-blacklisted-sites/all-blacklisted-sites.component';
import { AllUsersComponent } from 'src/app/pages/all-users/all-users.component';
import { SearchHistoryComponent } from 'src/app/pages/search-history/search-history.component';

export const AdminLayoutRoutes: Routes = [
    { path: 'dashboard',      component: DashboardComponent },
    { path: 'user-profile',   component: UserProfileComponent },
    { path: 'tables',         component: TablesComponent },
    { path: 'icons',          component: IconsComponent },
    { path: 'maps',           component: MapsComponent },
    { path: 'all-blacklisted-sites',  component: AllBlacklistedSitesComponent },
    { path: 'all-users',  component: AllUsersComponent },
    { path: 'search-history',  component: SearchHistoryComponent },
    { path: 'search-by-url',  component: SearchByUrlComponent }
];
