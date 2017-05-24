import { Component, Input, OnChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Operator } from '../type/operator';
import { OperatorService } from '../service/operator.service';

@Component({
    selector: 'app-operator-management-password',
    templateUrl: './operator-management-password.component.html'
})
export class OperatorManagementPasswordComponent implements OnChanges {
    @Input()
    public operatorId: number;

    public operator: Operator;
    public oldPassword: string;
    public newPassword: string;
    public newPassword2nd: string;
    public saving: boolean;
    public updated: boolean;
    public updateSuccess: boolean;

    constructor(
        private translate: TranslateService,
        private operatorService: OperatorService
    ) { }

    public onApply(): void {
        console.log(
            '{OperatorManagementPasswordComponent}',
            '[onApply]',
            'Modifying operator password: ' + '[' + this.operator.name + ']'
        );
        this.saving = true;
        this.operatorService.changePassword(this.operatorId, this.oldPassword, this.newPassword).then(
            success => {
                if (success) {
                    this.updateSuccess = true;
                    console.log(
                        '{OperatorManagementPasswordComponent}',
                        '[onApply]',
                        'Updated operator password'
                    );
                } else {
                    console.error(
                        '{OperatorManagementPasswordComponent}',
                        '[onApply]',
                        'Failed to updated operator password (Incorrect old password?)'
                    );
                }
                this.updated = true;
                this.clearPasswords();
            },
            failure => {
                console.error(
                    '{OperatorManagementPasswordComponent}',
                    '[onApply]',
                    'Failed to updated operator password (Server error?)'
                );
                this.updated = true;
                this.clearPasswords();
            }
        );
    }

    public getOperator(id: number): void {
        this.operatorService.get(id).then(
            operator => {
                if (operator) {
                    this.operator = operator;
                    console.log(
                        '{OperatorManagementPasswordComponent}',
                        '[getOperator]',
                        'Retrieved operator:', this.operator
                    );
                } else {
                    console.error(
                        '{OperatorManagementPasswordComponent}',
                        '[getOperator]',
                        'Failed to retrieve operator id:', id
                    );
                }
            },
            failure => {
                console.error(
                    '{OperatorManagementPasswordComponent}',
                    '[getOperator]',
                    'Failed to retrieve operator id:', id
                );
            }
        );
    }

    private clearPasswords(): void {
        this.saving = false;
        this.oldPassword = '';
        this.newPassword = '';
        this.newPassword2nd = '';
    }

    public onReset(): void {
        console.log(
            '{OperatorManagementPasswordComponent}',
            '[onReset]'
        );
        this.operator = undefined;
        this.saving = false;
        this.updated = false;
        this.updateSuccess = false;
        this.clearPasswords();

        if (this.operatorId) {
            this.getOperator(this.operatorId);
        }
    }

    public ngOnChanges(): void { this.onReset(); }
}
