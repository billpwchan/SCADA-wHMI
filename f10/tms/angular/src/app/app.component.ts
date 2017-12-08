import { Component } from '@angular/core';
import { CardService } from './service/card/card.service';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';
import { OnInit, OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { StepsComponent } from './component/steps/steps.component';
import { StepEditComponent } from './component/step-edit/step-edit.component';
import { SelectionService } from './service/card/selection.service';

@Component({
  selector: 'app-root'
  , templateUrl: './app.component.html'
  , styleUrls: [
    './app.component.css'
  ]
})
export class AppComponent implements OnInit, OnDestroy {

  readonly c = 'AppComponent';

  cardSubscription: Subscription;

  cardUpdateValue: string;
  stepEditUpdateValue: string;

  title = 'TMS';

  constructor(
    private translate: TranslateService
    , private cardService: CardService
  ) {
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => this.changeCard(item));
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestory';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
  }

  changeCard(str: string) {
    const f = 'changeCard';
    console.log(this.c, f, 'str', str);
  }

  getNotification(evt) {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);
  }

  // getLocations() {
  //   return this.httpClient
  //     .get('./assets/countries.json')
  //     .flatMap((data: any) => Observable
  //     .forkJoin(data.countries
  //     .map((country: string) => this.httpClient
  //     .get(`./assets/${country}.json`)
  //     .map((locations: any) => {
  //       return {country: country.toUpperCase(), cities: locations.cities};
  //      })))
  //     ).catch(e => Observable.of({failure: e}));
  // }

  // // reading from pages and fire the page
  // private getRequest() {
  //   this.httpClient.get('/api/pages/')
  //   .map((res: any) => res.json())
  //   .mergeMap((pages: any[]) => {
  //     if (pages.length > 0) {
  //       return Observable.forkJoin(
  //         pages.map((page: any, i) => {
  //           return this.httpClient.get('/api/sections/' + i)
  //             .map((res: any) => {
  //               const section: any = res.json();
  //               // do your operation and return
  //               return section;
  //             });
  //         })
  //       );
  //     }
  //     return Observable.of([]);
  //   });
  // }
}
