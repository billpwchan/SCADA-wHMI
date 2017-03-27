import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule } from '@angular/router';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';


import {OperatorManagementComponent} from './operator-management.component';
import {OperatorManagementModifyComponent} from './operator-management-modify.component';
import {OperatorManagementAddComponent} from './operator-management-add.component';
import {OperatorManagementRemoveComponent} from './operator-management-remove.component';

import {ProfileService} from '../service/profile.service';

export function translateHttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/');
}

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
    RouterModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateHttpLoaderFactory,
        deps: [Http]
      }
    })
  ],
  providers: [ProfileService],
  exports: [OperatorManagementComponent]
})
export class OperatorManagementModule { }
