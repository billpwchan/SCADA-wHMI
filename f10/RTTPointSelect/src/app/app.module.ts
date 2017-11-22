import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { HttpModule, Http } from '@angular/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { ColorPickerModule } from 'angular4-color-picker';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { PageNotFoundComponent } from './not-found.component';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { RttComponent } from './rtt/rtt.component';
import { ScsOlsService } from './service/scs-ols.service';
import { ConfigService } from './service/config.service';
import { OlsdataService } from './service/olsdata.service';
import { RttTrendService } from './service/rtt-trend.service';
import { ColorPickerService } from 'angular4-color-picker';
import { RttFilterPipe } from './rtt/rtt-filter.pipe';
export function translateHttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
export function configFactory(config: ConfigService) {
  return () => config.load();
}
@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    RttComponent,
    RttFilterPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    ColorPickerModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateHttpLoaderFactory,
        deps: [Http]
      }
    })
  ],
  providers: [
    ScsOlsService,
    OlsdataService,
    ConfigService,
    RttTrendService,
    ColorPickerService,
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
