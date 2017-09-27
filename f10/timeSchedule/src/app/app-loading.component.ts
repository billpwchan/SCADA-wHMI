import { Component, OnInit, OnDestroy } from '@angular/core';
import { ConfigService } from './service/config.service';
import { ScheduleService } from './service/schedule.service';
import { LoadingService } from './service/loading.service';
import { MdProgressSpinnerModule } from '@angular/material';
@Component({
    selector: 'app-loading-component',
    templateUrl: './app-loading.component.html',
    styleUrls: ['./app-loading.component.css'],
})
export class AppLoadingComponent implements OnInit, OnDestroy {
    private isLoading = false;
    private subscription: any;
    public spinnerColor = 'primary';

    constructor( private loadingService: LoadingService) {
    }

    public showLoading(loading) {
        console.log('isLoading', this.isLoading);
        this.isLoading = loading;
    }

    ngOnInit() {
        this.subscription = this.loadingService.loading$.subscribe( loading => {
            this.showLoading(loading);
        })
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    public setSpinnerColor(color) {
        this.spinnerColor = color;
    }
}
