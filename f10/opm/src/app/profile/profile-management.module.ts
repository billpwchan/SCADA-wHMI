import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule } from '@angular/router';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ProfileManagementComponent } from './profile-management.component';
import { ProfileManagementModifyComponent } from './profile-management-modify.component';
import { ProfileManagementAddComponent } from './profile-management-add.component';
import { ProfileManagementRemoveComponent } from './profile-management-remove.component';
import { ProfileManagementDetailComponent } from './profile-management-detail.component';

import { ProfileService } from '../service/profile.service';
import { FunctionService } from '../service/function.service';
import { LocationService } from '../service/location.service';
import { MaskService } from '../service/mask.service';
import { ActionService } from '../service/action.service';
import { UtilService, StringMap, NumberMap } from '../service/util.service';

export function translateHttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/');
}

@NgModule({
  declarations: [
    ProfileManagementComponent,
    ProfileManagementModifyComponent,
    ProfileManagementAddComponent,
    ProfileManagementRemoveComponent,
    ProfileManagementDetailComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateHttpLoaderFactory,
        deps: [Http]
      }
    })
  ],
  providers: [
    ProfileService,
    FunctionService,
    LocationService,
    MaskService,
    ActionService,
    UtilService, StringMap, NumberMap
  ],
  exports: [ProfileManagementComponent]
})
export class ProfileManagementModule { }
