import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule, Routes} from '@angular/router';

import {OperatorManagementComponent} from './operator-management.component';
import {OperatorManagementModifyComponent} from './operator-management-modify.component';
import {OperatorManagementAddComponent} from './operator-management-add.component';
import {OperatorManagementRemoveComponent} from './operator-management-remove.component';

import {ProfileService} from '../service/profile.service';

@NgModule({
  declarations: [
    OperatorManagementComponent,
    OperatorManagementModifyComponent,
    OperatorManagementAddComponent,
    OperatorManagementRemoveComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule
  ],
  providers: [ProfileService],
  exports: [OperatorManagementComponent]
})
export class OperatorManagementModule { }
