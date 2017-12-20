import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppComponent } from './app.component';
import { AvaCondService } from './services/ava-cond.service';
import { ConfigService } from './services/config.service';
import { AvaCondModule } from './ava-cond/ava-cond.module';
import { AvaCondDetailModule } from './ava-cond-detail/ava-cond-detail.module';

export function translateHttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
export function configFactory(config: ConfigService) {
  return () => config.load();
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
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
    AvaCondModule,
    AvaCondDetailModule
    ],
  providers: [
    AvaCondService,
    ConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    },
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
