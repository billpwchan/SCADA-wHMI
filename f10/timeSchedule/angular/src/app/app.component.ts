import { Component } from '@angular/core';
import { Router, Route } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Cookie } from 'ng2-cookies';
import { ConfigService } from './service/config.service';
import { ScheduleService } from './service/schedule.service';
import { LoadingService } from './service/loading.service';
@Component({
    selector: 'app-tsc-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})
export class AppComponent {
    private static defaultLanguage = 'en';
    public title = 'Time Schedule';
    constructor( private configService: ConfigService, private translate: TranslateService,
                private loadingService: LoadingService, private scheduleService: ScheduleService,
                private router: Router) {
        const defaultLanguage = configService.config.getIn(['i18n', 'default_lang']);
        const disablePeriodicSchedulePlanning = configService.config.getIn(['disable_periodic_schedule_planning']);
        const preferedLanguage = this.getPreferedLanguage();
        console.log(
            '{AppComponent}',
            '[Language]',
            'Default:', defaultLanguage,
            'Prefered:', preferedLanguage
        );
        translate.setDefaultLang(defaultLanguage);
        if (preferedLanguage) {
            translate.use(preferedLanguage);
            console.log('{AppComponent}', '[Language]', 'use preferred language ', preferedLanguage);
        }
        if (disablePeriodicSchedulePlanning) {
            this.removePeriodicSchedulePlanningRoute();
            console.log('{AppComponent}', '[loadConfig]', 'disable periodic schedule planning ', disablePeriodicSchedulePlanning);
        }
        scheduleService.load();
    }
    private getPreferedLanguage(): string {
        if (this.configService.config.getIn(['i18n', 'resolve_by_browser_lang'])) {
            console.log('{AppComponent}', 'Resolve prefered language by browser\'s language');
            let browserLanguage = this.translate.getBrowserCultureLang();
            if (!this.configService.config.getIn(['i18n', 'use_culture_lang'])) {
                browserLanguage = this.translate.getBrowserLang();
            }
            return browserLanguage;
        } else if (this.configService.config.getIn(['i18n', 'resolve_by_browser_cookie'])) {
            const cookieName = this.configService.config.getIn(['i18n', 'use_cookie_name']);
            console.log('{AppComponent}', 'Resolve prefered language by browser\'s cookie:', cookieName);
            return Cookie.get(this.configService.config.getIn(['i18n', 'use_cookie_name']));
        } else {
            console.log('{AppComponent}', 'No defined way to obtain prefered language');
        }
        return undefined;
    }

    private removePeriodicSchedulePlanningRoute() {
        const routeConfig = this.router.config;
        const newConfig = Array<Route>();
        for (const r of routeConfig) {
            if (r.path !== 'schedule-planning') {
            newConfig.push(r);
            }
        }
        console.log('{AppRoutingModule}', 'update router config to ', newConfig);
        this.router.resetConfig(newConfig);
    }
}
