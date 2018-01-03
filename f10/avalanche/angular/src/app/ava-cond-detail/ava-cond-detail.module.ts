import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ConfigService } from '../services/config.service';
import { AvaCondDetailComponent } from './ava-cond-detail.component';
import { AvaCondNameComponent } from './ava-cond-name/ava-cond-name.component';
import { AvaTriggerCondComponent } from './ava-trigger-cond/ava-trigger-cond.component';
import { AvaSuppressAlarmComponent } from './ava-suppress-alarm/ava-suppress-alarm.component';

export function translateHttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
export function configFactory(config: ConfigService) {
  return () => config.load();
}

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: translateHttpLoaderFactory,
            deps: [HttpClient]
        }
    }),
    NgxDatatableModule
  ],
  declarations: [
    AvaCondDetailComponent,
    AvaCondNameComponent,
    AvaTriggerCondComponent,
    AvaSuppressAlarmComponent
  ],
  exports: [
    AvaCondDetailComponent,
    AvaCondNameComponent,
    AvaTriggerCondComponent,
    AvaSuppressAlarmComponent
  ]
})
export class AvaCondDetailModule { }
