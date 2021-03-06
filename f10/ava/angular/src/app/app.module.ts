import { NgModule, APP_INITIALIZER } from '@angular/core';
import { NgModel, FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import { HttpModule } from '@angular/http';
import { CardsComponent } from './component/card/cards/cards.component';
import { StepsComponent } from './component/step/steps/steps.component';
import { OlsService } from './service/scs/ols.service';
import { StepEditComponent } from './component/step/step-edit/step-edit.component';
import { SettingsService } from './service/settings.service';
import { AdminComponent } from './route/admin/admin.component';
import { PageNotFoundComponent } from './route/page-not-found/page-not-found.component';
import { DbmPollingService } from './service/scs/dbm-polling.service';
import { HotTableModule } from 'angular-handsontable';
import { MatrixComponent } from './component/alarm/Matrix/matrix.component';
import { TitleComponent } from './component/card/title/title.component';
import { AlarmSummaryComponent } from './component/alarm/alarm-summary/alarm-summary.component';
import { GetInstancesByClassNameService } from './service/scs/ava/dbm/get-instances-by-class-name.service';
import { GetChildrenAliasesService } from './service/scs/ava/dbm/get-children-aliases.service';
import { ReadWriteCEService } from './service/scs/ava/dbm/read-write-ce.service';
import { RenameComponent } from './component/card/rename/rename.component';
import { DbmMultiReadAttrService } from './service/scadagen/dbm/dbm-multi-read-attr.service';
import { UtilsHttpModule } from './service/scadagen/utils/utils-http.module';
import { DbmService } from './service/scadagen/dbm/dbm.service';
import { HttpMultiAccessService } from './service/scadagen/access/http/multi/http-multi-access.service';
import { DbmMultiWriteAttrService } from './service/scadagen/dbm/dbm-multi-write-attr.service';
import { CardSummaryComponent } from './component/card/card-summary/card-summary.component';
import { StepSummaryComponent } from './component/step/step-summary/step-summary.component';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

const appRoutes: Routes = [
  { path: 'admin', component: AdminComponent }
  , { path: 'pagenotfound', component: PageNotFoundComponent }
  , { path: '', redirectTo: '/pagenotfound', pathMatch: 'full' }
  , { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent
    , CardsComponent
    , StepsComponent
    , StepEditComponent
    , AdminComponent
    , PageNotFoundComponent
    , MatrixComponent
    , TitleComponent
    , AlarmSummaryComponent
    , RenameComponent
    , CardSummaryComponent
    , StepSummaryComponent
  ],
  imports: [
    BrowserModule
    , FormsModule
    , NgxDatatableModule
    , HotTableModule
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
    , RouterModule.forRoot(
      appRoutes
      , { useHash: true }
      // ,{ enableTracing: true } // <-- debugging purposes only
    )
  ],
  providers: [
    SettingsService
    , {
      provide: APP_INITIALIZER,
      useFactory: (settingService: SettingsService) => function() { return settingService.load(); },
      deps: [SettingsService],
      multi: true
    }
    , OlsService
    , DbmService
    , DbmPollingService
    , GetInstancesByClassNameService
    , GetChildrenAliasesService
    , HttpMultiAccessService
    , DbmMultiReadAttrService
    , DbmMultiWriteAttrService
    , ReadWriteCEService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
