import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { OperatorManagementComponent } from './operator-management.component';
import { OperatorManagementModifyComponent } from './operator-management-modify.component';
import { OperatorManagementAddComponent } from './operator-management-add.component';
import { OperatorManagementRemoveComponent } from './operator-management-remove.component';
import { OperatorManagementDetailComponent } from './operator-management-detail.component';
import { OperatorManagementChangePasswordComponent } from './operator-management-change-password.component';
import { OperatorManagementPasswordComponent } from './operator-management-password.component';

import { AppRoutingModule } from './operator-management.routes';

import { ProfileService } from '../service/profile.service';
import { OperatorService } from '../service/operator.service';
import { StringMap, NumberMap } from '../service/util.service';

export function translateHttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/');
}

@NgModule({
  declarations: [
    OperatorManagementComponent,
    OperatorManagementModifyComponent,
    OperatorManagementAddComponent,
    OperatorManagementRemoveComponent,
    OperatorManagementDetailComponent,
    OperatorManagementChangePasswordComponent,
    OperatorManagementPasswordComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
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
    OperatorService,
    StringMap, NumberMap
  ],
  exports: [OperatorManagementComponent]
})
export class OperatorManagementModule { }
