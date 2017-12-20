import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { ConfigService } from '../services/config.service';

import { AvaCondComponent } from './ava-cond.component';
import { AvaCondActionComponent } from './ava-cond-action.component';
import { AvaCondDetailModule } from '../ava-cond-detail/ava-cond-detail.module';

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
    NgxDatatableModule,
    AvaCondDetailModule
  ],
  declarations: [
    AvaCondComponent,
    AvaCondActionComponent
  ],
  exports: [
    AvaCondComponent,
    AvaCondActionComponent
  ]
})
export class AvaCondModule { }
