import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';

// module for local storage
import { LocalStorageModule } from 'angular-2-local-storage';

// modules for translation
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// components, services, etc of this application
import { AutoLogoutComponent } from './auto-logout.component';
import { ConfigService } from './service/config.service';
import { I18nService } from './service/i18n.service';

// initializer for ConfigService
export function configFactory(config: ConfigService) {
  return () => config.load('./assets/config/settings.json', true)
}

// initializer for I18nService
export function translateHttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/');
}

@NgModule({
  declarations: [
    AutoLogoutComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    LocalStorageModule.withConfig({
      prefix: '',
      storageType: 'localStorage'
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateHttpLoaderFactory,
        deps: [Http]
      }
    })
  ],
  providers: [
    ConfigService,
    I18nService,
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    }
  ],
  bootstrap: [AutoLogoutComponent]
})
export class AutoLogoutModule { }
