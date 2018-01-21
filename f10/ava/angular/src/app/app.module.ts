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
import { UtilsHttpModule } from './service/utils-http/utils-http.module';
import { CardService } from './service/card/card.service';
import { CardsComponent } from './component/card/cards/cards.component';
import { StepsComponent } from './component/step/steps/steps.component';
import { StorageComponent } from './component/storage/storage/storage.component';
import { OlsService } from './service/scs/ols.service';
import { DbmService } from './service/scs/dbm.service';
import { StorageService } from './service/card/storage.service';
import { StepEditComponent } from './component/step/step-edit/step-edit.component';
import { CardEditComponent } from './component/card/card-edit/card-edit.component';
import { SelectionService } from './service/card/selection.service';
import { SettingsService } from './service/settings.service';
import { AdminComponent } from './route/admin/admin.component';
import { PageNotFoundComponent } from './route/page-not-found/page-not-found.component';
import { StepEditControllerComponent } from './component/step/step-edit-controller/step-edit-controller.component';
import { CardEditControllerComponent } from './component/card/card-edit-controller/card-edit-controller.component';
import { DbmPollingService } from './service/scs/dbm-polling.service';
import { HotTableModule } from 'angular-handsontable';
import { MatrixComponent } from './component/alarm/Matrix/matrix.component';
import { CardTitleComponent } from './component/card/card-title/card-title.component';
import { CardStateControllerComponent } from './component/card/card-state-controller/card-state-controller.component';
import { OlsAvaSupService } from './service/scs/ava/ols-ava-sup.service';
import { DbmReadAvaSupService } from './service/scs/ava/dbm-read-ava-sup.service';
import { DbmWriteAvaSupService } from './service/scs/ava/dbm-write-ava-sup.service';
import { AlarmSummaryComponent } from './component/alarm/alarm-summary/alarm-summary.component';
import { DbmCacheAvaSupService } from './service/scs/ava/dbm-cache-ava-sup.service';
import { GetInstancesByClassNameService } from './service/scs/ava/dbm/get-instances-by-class-name.service';
import { GetChildrenAliasesService } from './service/scs/ava/dbm/get-children-aliases.service';
import { MultiReadService } from './service/scs/ava/dbm/multi-read.service';

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
    , StorageComponent
    , StepEditComponent
    , CardEditComponent
    , AdminComponent
    , PageNotFoundComponent
    , StepEditControllerComponent
    , CardEditControllerComponent
    , MatrixComponent
    , CardTitleComponent
    , CardStateControllerComponent
    , AlarmSummaryComponent
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
    , CardService
    , SelectionService
    , StorageService
    , DbmPollingService
    , OlsAvaSupService
    , DbmCacheAvaSupService
    , DbmReadAvaSupService
    , DbmWriteAvaSupService
    , GetInstancesByClassNameService
    , GetChildrenAliasesService
    , MultiReadService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
