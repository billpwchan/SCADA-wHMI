import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgModel, FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientModule, HttpClient } from '@angular/common/http';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { PointSelectComponent } from './point-select.component';
import { UtilsHttpModule } from './service/utils-http/utils-http.module';
import { OlsService } from './service/scs/ols.service';
import { DbmService } from './service/scs/dbm.service';

@NgModule({
  imports: [
    CommonModule
    , FormsModule
    , NgxDatatableModule
    , HttpModule
    , HttpClientModule
    , UtilsHttpModule
    , TranslateModule
  ],
  declarations: [
    PointSelectComponent
  ],
  providers: [
    OlsService
    , DbmService
  ],
  exports: [
    PointSelectComponent
  ]
})
export class PointSelectModule { }
