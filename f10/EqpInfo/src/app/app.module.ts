import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { NgActiveTextComponent } from './component/ng-active-text/ng-active-text.component';
import { SettingsService } from './service/settings.service';
import { HttpMultiAccessService } from './service/scadagen/access/http/multi/http-multi-access.service';
import { DbmMultiReadAttrService } from './service/scadagen/dbm/dbm-multi-read-attr.service';
import { HttpModule } from '@angular/http';
import { NgActiveBackdropComponent } from './component/ng-active-backdrop/ng-active-backdrop.component';
import { DbmPollingService } from './service/scadagen/dbm/polling/dbm-polling.service';
import { DbmService } from './service/scadagen/dbm/dbm.service';
import { NgActiveNumberComponent } from './component/ng-active-number/ng-active-number.component';
import { UtilsHttpModule } from './service/scadagen/common/utils-http.module';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    NgActiveTextComponent,
    NgActiveBackdropComponent,
    NgActiveNumberComponent
  ],
  imports: [
    BrowserModule
    , HttpModule
    , HttpClientModule
    , TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    })
    , UtilsHttpModule
  ],
  providers: [
    SettingsService
    , {
      provide: APP_INITIALIZER,
      useFactory: (settingService: SettingsService) => function() { return settingService.load(); },
      deps: [SettingsService],
      multi: true
    }
    , HttpMultiAccessService
    , DbmService
    , DbmMultiReadAttrService
    , DbmPollingService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
