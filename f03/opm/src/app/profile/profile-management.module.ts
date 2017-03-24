import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule, Routes} from '@angular/router';

import {ProfileManagementComponent} from './profile-management.component';
import {ProfileManagementModifyComponent} from './profile-management-modify.component';
import {ProfileManagementAddComponent} from './profile-management-add.component';
import {ProfileManagementRemoveComponent} from './profile-management-remove.component';
import {ProfileManagementDetailComponent} from './profile-management-detail.component';

import {ProfileService} from '../service/profile.service';
import {FunctionService} from '../service/function.service';
import {LocationService} from '../service/location.service';
import {MaskService} from '../service/mask.service';
import {ActionService} from '../service/action.service';
import {UtilService, StringMap, NumberMap} from '../service/util.service';

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
    RouterModule
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
