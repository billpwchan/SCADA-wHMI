import { Component, OnInit, OnDestroy, AfterContentInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';

import { Operator } from '../type/operator';
import { OperatorService } from '../service/operator.service';

@Component({
    selector: 'app-operator-management-modify',
    templateUrl: './operator-management-modify.component.html'
})
export class OperatorManagementModifyComponent implements OnInit, OnDestroy, AfterContentInit {
    public operators: Operator[];
    public selectedOperator: Operator;
    public readonly: boolean;
    private queryParamsSub: Subscription;

    constructor(
        private route: ActivatedRoute,
        private translateService: TranslateService,
        private operatorService: OperatorService
    ) { }

    public getOperators(): void {
        this.operatorService.getAll().then(
            operators => {
                this.operators = operators ? operators.sort(this.operatorService.compareOperator) : [];
                console.log(
                    '{OperatorManagementModifyComponent}',
                    '[getOperators]',
                    'Operators#:',
                    this.operators ? this.operators.length : 0
                );
            },
            failure => {
                console.error(
                    '{OperatorManagementModifyComponent}',
                    '[getOperators]',
                    'Failed:', failure
                );
            }
        );
    }

    public onSelect(operator: Operator): void {
        this.selectedOperator = operator;
        console.log(
            '{OperatorManagementModifyComponent}',
            '[onSelect]',
            'OperatorId:', this.selectedOperator.id,
            'OperatorName:', this.selectedOperator.name,
            'OperatorDescription:', this.selectedOperator.description
        );
    }

    public ngOnInit(): void {
        this.queryParamsSub = this.route.queryParams.subscribe(
            params => {
                this.readonly = (1 === +params['readonly']) || false;
                console.log(
                    '{OperatorManagementModifyComponent}',
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

    public ngAfterContentInit(): void {
        this.operators = undefined;
        this.selectedOperator = undefined;
        this.getOperators();
    }
}
