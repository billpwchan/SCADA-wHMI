import { NgModule, APP_INITIALIZER } from '@angular/core';
import { NgModel, FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { AppComponent } from './app.component';
import { UtilsHttpModule } from './service/utils-http/utils-http.module';

import { CardService } from './service/card/card.service';
import { CardsComponent } from './component/card/cards/cards.component';
import { StepsComponent } from './component/step/steps/steps.component';
import { StorageComponent } from './component/storage/storage/storage.component';
import { ImportExportComponent } from './component/storage/import-export/import-export.component';
import { OlsService } from './service/scs/ols.service';
import { DbmService } from './service/scs/dbm.service';
import { DacSimService } from './service/scs/dac-sim.service';
import { StorageService } from './service/card/storage.service';
import { CardControllerComponent } from './component/card/card-controller/card-controller.component';
import { StepEditComponent } from './component/step/step-edit/step-edit.component';
import { CardEditComponent } from './component/card/card-edit/card-edit.component';
import { SelectionService } from './service/card/selection.service';
import { StepControllerComponent } from './component/step/step-controller/step-controller.component';
import { SettingsService } from './service/settings.service';
import { CsvToCardsPipe } from './pipe/csv/csv-to-cards.pipe';
import { CardsToCsvPipe } from './pipe/csv/cards-to-csv.pipe';
import { HttpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { TrainerComponent } from './route/trainer/trainer.component';
import { TrainerAdminComponent } from './route/trainer-admin/trainer-admin.component';
import { TraineeComponent } from './route/trainee/trainee.component';
import { PageNotFoundComponent } from './route/page-not-found/page-not-found.component';
import { StepEditControllerComponent } from './component/step/step-edit-controller/step-edit-controller.component';
import { CardEditControllerComponent } from './component/card/card-edit-controller/card-edit-controller.component';
import { DbmPollingService } from './service/scs/dbm-polling.service';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

const appRoutes: Routes = [
  { path: 'trainer', component: TrainerComponent }
  , { path: 'traineradmin', component: TrainerAdminComponent }
  , { path: 'trainee', component: TraineeComponent }
  , { path: 'pagenotfound', component: PageNotFoundComponent }
  , { path: '', redirectTo: '/pagenotfound', pathMatch: 'full' }
  , { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent
    , CardsComponent
    , CardControllerComponent
    , StepsComponent
    , ImportExportComponent
    , StorageComponent
    , StepEditComponent
    , CardEditComponent
    , StepControllerComponent
    , CsvToCardsPipe
    , CardsToCsvPipe
    , TrainerAdminComponent
    , TrainerComponent
    , TraineeComponent
    , PageNotFoundComponent
    , StepEditControllerComponent
    , CardEditControllerComponent
  ],
  imports: [
    BrowserModule
    , FormsModule
    , NgxDatatableModule
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
      appRoutes,
      // { enableTracing: true } // <-- debugging purposes only
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
    , DacSimService
    , CardService
    , SelectionService
    , StorageService
    , DbmPollingService

  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
