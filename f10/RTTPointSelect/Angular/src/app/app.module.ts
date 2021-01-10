import { HttpClient, HttpClientModule } from '@angular/common/http';
import { APP_INITIALIZER, ErrorHandler, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { StompRService } from '@stomp/ng2-stompjs';
import { AgGridModule } from 'ag-grid-angular';
import { HighchartsChartModule } from 'highcharts-angular';
import { ColorPickerModule } from 'ngx-color-picker';
import { CookieService } from 'ngx-cookie-service';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ErrorsHandler } from './errors-handler';
import { NotFoundComponent } from './not-found/not-found.component';
import { RttAppDialogComponent } from './rtt-app-dialog/rtt-app-dialog.component';
import { RttAppComponent } from './rtt-app/rtt-app.component';
import { RttComponent } from './rtt/rtt.component';
import { AggridTranslateService } from './service/aggrid-translate.service';
import { ConfigService } from './service/config.service';
import { HighstockDataService } from './service/highstock-data.service';
import { OlsdataService } from './service/olsdata.service';
import { RttTrendService } from './service/rtt-trend.service';
import { ScsOlsService } from './service/scs-ols.service';
import { SelectedOlsdatasService } from './service/selected-olsdatas.service';
import { StompSubscriptionService } from './service/stomp-subscription.service';






export function translateHttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
export function configFactory(config: ConfigService) {
  return () => config.load();
}
@NgModule({
  declarations: [
    AppComponent,
    NotFoundComponent,
    RttComponent,
    RttAppComponent,
    RttAppDialogComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    ColorPickerModule,
    MatButtonModule,
    MatSelectModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatDialogModule,
    MatSliderModule,
    MatProgressSpinnerModule,
    MatIconModule,
    HighchartsChartModule,
    AgGridModule.withComponents([]),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translateHttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  entryComponents: [
    RttAppDialogComponent
  ],
  providers: [
    ScsOlsService,
    OlsdataService,
    ConfigService,
    StompRService,
    CookieService,
    SelectedOlsdatasService,
    HighstockDataService,
    RttTrendService,
    StompSubscriptionService,
    AggridTranslateService,
    {
      provide: ErrorHandler,
      useClass: ErrorsHandler
    },
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
