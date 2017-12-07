import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-operator-management',
    templateUrl: './operator-management.component.html'
})

export class OperatorManagementComponent implements OnInit, OnDestroy {
    public readonly: boolean;
    private queryParamsSub: Subscription;
    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService
    ) { }

    public ngOnInit(): void {
        this.queryParamsSub = this.route.queryParams.subscribe(
            params => {
                this.readonly = (1 === +params['readonly']) || false;
                console.log(
                    '{OperatorManagementComponent}',
                    '[ngOnInit]',
                    'readonly:', this.readonly
                );
            }
        )
    }

    public ngOnDestroy(): void {
        this.queryParamsSub.unsubscribe();
        this.queryParamsSub = undefined;
    }
}
