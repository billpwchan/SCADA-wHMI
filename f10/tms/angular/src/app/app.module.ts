import { NgModule } from '@angular/core';
import { NgModel, FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { AppComponent } from './app.component';
import { UtilsHttpModule } from './service/utils-http/utils-http.module';

import { CardService } from './service/card/card.service';
import { CardsComponent } from './component/cards/cards.component';
import { StepsComponent } from './component/steps/steps.component';
import { StorageComponent } from './component/storage/storage.component';
import { CsvInterpretComponent } from './component/csv-interpret/csv-interpret.component';
import { OlsService } from './service/scs/ols.service';
import { DbmService } from './service/scs/dbm.service';
import { DacSimService } from './service/scs/dac-sim.service';
import { StorageService } from './service/card/storage.service';
import { CardsControllerComponent } from './component/cards-controller/cards-controller.component';
import { StepEditComponent } from './component/step-edit/step-edit.component';
import { CardEditComponent } from './component/card-edit/card-edit.component';
import { SelectionService } from './service/card/selection.service';
import { StepControllerComponent } from './component/step-controller/step-controller.component';
import { SettingService } from './service/setting.service';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent
    , CardsComponent
    , CardsControllerComponent
    , StepsComponent
    , CsvInterpretComponent
    , StorageComponent
    , StepEditComponent
    , CardEditComponent
    , StepControllerComponent
  ],
  imports: [
    BrowserModule
    , FormsModule
    , NgxDatatableModule
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
    SettingService
    , OlsService
    , DbmService
    , DacSimService
    , CardService
    , SelectionService
    , StorageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
