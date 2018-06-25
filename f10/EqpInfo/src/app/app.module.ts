import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { NgActiveTextComponent } from './component/ng-active-text/ng-active-text.component';
import { SettingsService } from './service/settings.service';
import { HttpMultiAccessService } from './service/scadagen/access/http/multi/http-multi-access.service';
import { HttpModule } from '@angular/http';
import { NgActiveBackdropComponent } from './component/ng-active-backdrop/ng-active-backdrop.component';
import { DbmPollingService } from './service/scadagen/dbm/simple/polling/dbm-polling.service';
import { NgActiveNumberComponent } from './component/ng-active-number/ng-active-number.component';
import { UtilsHttpModule } from './service/scadagen/common/utils-http.module';
import { NgActiveButtonComponent } from './component/ng-active-button/ng-active-button.component';
import { EnvironmentMappingService } from './service/scadagen/envs/environment-mapping.service';
import { DbmGetChildrenAliasesService } from './service/scadagen/dbm/simple/get-children/dbm-get-children-aliases.service';
import { DbmGetInstancesByClassNameService } from './service/scadagen/dbm/simple/get-instance/dbm-get-instance-by-class-name.service';
import { NgActiveButtonDioComponent } from './component/ng-active-button-dio/ng-active-button-dio.component';
import { NgActiveButtonAioComponent } from './component/ng-active-button-aio/ng-active-button-aio.component';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    NgActiveTextComponent,
    NgActiveBackdropComponent,
    NgActiveNumberComponent,
    NgActiveButtonComponent,
    NgActiveButtonDioComponent,
    NgActiveButtonAioComponent
  ],
  imports: [
    BrowserModule
    , FormsModule
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
    , EnvironmentMappingService
    , DbmPollingService
    , DbmGetChildrenAliasesService
    , DbmGetInstancesByClassNameService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
