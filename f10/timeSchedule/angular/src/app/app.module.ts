import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { HttpModule, Http } from '@angular/http';
import { MdProgressSpinnerModule } from '@angular/material';
import { SelectModule } from 'ng2-select';
import { NgxDatatableModule } from '@swimlane/ngx-datatable/src';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { PageNotFoundComponent } from './not-found.component';
import { AppComponent } from './app.component';
import { AppNavigationComponent } from './app-navigation.component';
import { AppRoutingModule } from './app-routing.module';
import { AppLoadingComponent } from './app-loading.component';
import { ScheduleTableComponent } from './schedule-table/schedule-table.component';
import { SchedulePlanningComponent } from './schedule-planning/schedule-planning.component';
import { ScsTscService } from './service/scs-tsc.service';
import { ConfigService } from './service/config.service';
import { ScheduleService } from './service/schedule.service';
import { LoadingService } from './service/loading.service';

export function translateHttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
export function configFactory(config: ConfigService) {
  return () => config.load();
}
@NgModule({
  declarations: [
    AppComponent,
    AppNavigationComponent,
    PageNotFoundComponent,
    ScheduleTableComponent,
    SchedulePlanningComponent,
    AppLoadingComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    SelectModule,
    NgxDatatableModule,
    BrowserAnimationsModule,
    MdProgressSpinnerModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateHttpLoaderFactory,
        deps: [Http]
      }
    })
  ],
  providers: [
    ScsTscService,
    ConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    },
    ScheduleService,
    LoadingService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
