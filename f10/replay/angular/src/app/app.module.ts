import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AgGridModule } from 'ag-grid-angular';
import { CalendarModule } from 'primeng/calendar';

import { AppComponent } from './app.component';
import { RecordDisplayComponent } from './record-display/record-display.component';
import { RecordSearchComponent } from './record-search/record-search.component';
import { ReplayActionComponent } from './replay-action/replay-action.component';
import { ReplayService } from './service/replay.service';
import { SettingsService } from './service/settings.service';
import { UtilsHttpModule } from './service/utils-http/utils-http.module';
import { ScadagenReplayService } from './service/scadagen/scadagen-replay.service';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    RecordDisplayComponent,
    RecordSearchComponent,
    ReplayActionComponent
  ],
  imports: [
    BrowserModule
    , BrowserAnimationsModule
    , FormsModule
    , AgGridModule.withComponents(
      [RecordDisplayComponent]
    )
    , UtilsHttpModule
    , HttpModule
    , HttpClientModule
    , TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    })
    , CalendarModule

  ],
  providers: [
    SettingsService
    , {
      provide: APP_INITIALIZER,
      useFactory: (settingService: SettingsService) => function() { return settingService.load(); },
      deps: [SettingsService],
      multi: true
    },
    ReplayService,
    ScadagenReplayService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
