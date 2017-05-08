import { Component, AfterContentInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Operator } from '../type/operator';
import { OperatorService } from '../service/operator.service';

@Component({
    selector: 'app-operator-management-change-password',
    templateUrl: './operator-management-change-password.component.html'
})
export class OperatorManagementChangePasswordComponent implements AfterContentInit {
    public operators: Operator[];
    public selectedOperator: Operator;

    constructor(
        private translateService: TranslateService,
        private operatorService: OperatorService
    ) { }

    public getOperators(): void {
        this.operatorService.getAll().then(
            operators => {
                this.operators = operators ? operators.sort(this.operatorService.compareOperator) : [];
                console.log(
                    '{OperatorManagementChangePasswordComponent}',
                    '[getOperators]',
                    'Operators#:',
                    this.operators ? this.operators.length : 0
                );
            }
        );
    }

    public onSelect(operator: Operator): void {
        this.selectedOperator = operator;
        console.log(
            '{OperatorManagementChangePasswordComponent}',
            '[onSelect]',
            'OperatorId:', this.selectedOperator.id,
            'OperatorName:', this.selectedOperator.name,
            'OperatorDescription:', this.selectedOperator.description
        );
    }

    public onInit(): void {
        this.operators = undefined;
        this.selectedOperator = undefined;
        this.getOperators();
    }

    public ngAfterContentInit(): void {
        this.onInit();
    }
}
