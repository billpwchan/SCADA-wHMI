import { Component, OnInit, OnDestroy } from '@angular/core';
import { CardService } from './service/card/card.service';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';
import { StepsComponent } from './component/steps/steps.component';
import { StepEditComponent } from './component/step-edit/step-edit.component';
import { SelectionService } from './service/card/selection.service';
import { SettingsService } from './service/settings.service';
import { StepEditSettings } from './component/step-edit/step-edit-settings';
import { CardServiceType } from './service/card/card-settings';
import { AppSettings } from './app-settings';
import { Cookie } from 'ng2-cookies';
import { I18nSettings } from './i18n-settings';

@Component({
  selector: 'app-root'
  , templateUrl: './app.component.html'
  , styleUrls: [
    './app.component.css'
  ]
})
export class AppComponent implements OnInit, OnDestroy {

  readonly c = AppComponent.name;

  cardSubscription: Subscription;

  cardUpdateValue: string;
  stepEditUpdateValue: string;

  title = 'TMS';

  constructor(
    private translate: TranslateService
    , private cardService: CardService
    , private settingService: SettingsService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    console.log(this.c, f, 'translate.getBrowserCultureLang()[' + translate.getBrowserCultureLang() + ']');
    console.log(this.c, f, 'translate.getBrowserLang()[' + translate.getBrowserLang() + ']');
    console.log(this.c, f, 'translate.getDefaultLang()[' + translate.getDefaultLang() + ']');
    console.log(this.c, f, 'translate.getLangs()[' + translate.getLangs() + ']');

    const setting = this.settingService.getSettings();
    const i18n = setting[I18nSettings.STR_I18N];
    const defaultLanguage = i18n[I18nSettings.STR_DEFAULT_LANG];
    const preferedLanguage = this.getPreferedLanguage();
    console.log(this.c, f,
                          '[Language]',
                          'Default:', defaultLanguage,
                          'Prefered:', preferedLanguage
    );
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang(defaultLanguage);
    if (preferedLanguage) {
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        translate.use(preferedLanguage);
        console.log(this.c, f, 'use preferred language ', preferedLanguage);
    }
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => this.changeCard(item));
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
  }

  changeCard(serviceType: CardServiceType) {
    const f = 'changeCard';
    console.log(this.c, f, 'serviceType', serviceType);
  }

  getNotification(evt) {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);
  }

  private getPreferedLanguage(): string {
    const f = 'getPreferedLanguage';
    const setting = this.settingService.getSettings();
    const i18n = setting[I18nSettings.STR_I18N];
    if (i18n[I18nSettings.STR_RESOLVE_BY_BROWSER_LANG]) {
        console.log(this.c, f, 'Resolve prefered language by browser\'s language');
        let browserLanguage = this.translate.getBrowserCultureLang();
        if (!i18n[I18nSettings.STR_USE_CULTURE_LANG]) {
            browserLanguage = this.translate.getBrowserLang();
        }
        return browserLanguage;
    } else if (i18n[I18nSettings.STR_RESOLVE_BY_BROWSER_COOKIE]) {
        const cookieName = i18n[I18nSettings.STR_USE_COOKIE_NAME];
        console.log(this.c, f, 'Resolve prefered language by browser\'s cookie:', cookieName);
        return Cookie.get(cookieName);
    } else {
        console.log(this.c, f, 'No defined way to obtain prefered language');
    }
    return undefined;
  }
}
