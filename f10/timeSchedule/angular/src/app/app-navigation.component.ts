import { Component } from '@angular/core';
import { ConfigService } from './service/config.service';

@Component({
    selector: 'app-navigation',
    templateUrl: './app-navigation.component.html',
    styleUrls: ['./app-navigation.component.css'],
})

export class AppNavigationComponent {
    public disableSchedulePlanning = false;

    constructor( private configService: ConfigService) {
        this.disableSchedulePlanning = configService.config.getIn(['disable_periodic_schedule_planning']);
    }
}
