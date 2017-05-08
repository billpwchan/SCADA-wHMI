import { Component, AfterContentInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Operator } from '../type/operator';
import { OperatorService } from '../service/operator.service';

import { Profile } from '../type/profile';
import { ProfileService } from '../service/profile.service';

@Component({
    selector: 'app-operator-management-remove',
    templateUrl: './operator-management-remove.component.html'
})
export class OperatorManagementRemoveComponent implements AfterContentInit {
    operators: Operator[];
    selectedOperators: Operator[];

    operatorsToRemove: Operator[];
    operatorsToRemoveIndex: number;
    operatorsToRemoveTotal: number;

    constructor(
        private translate: TranslateService,
        private operatorService: OperatorService,
        private profileService: ProfileService
    ) { }

    public getOperators(): void {
        this.operatorService.getAll().then(
            operators => {
                this.operators = operators ? operators.sort(this.operatorService.compareOperator) : [];
                console.log(
                    '{OperatorManagementRemoveComponent}',
                    '[getOperators]',
                    'Operator#:',
                    this.operators ? this.operators.length : 0
                );
            },
            failure => {
                console.error(
                    '{OperatorManagementRemoveComponent}',
                    '[getOperators]',
                    'Failed:', failure
                );
            }
        );
    }

    public onSelect(operator: Operator, event: MouseEvent): void {
        const selectedLen = this.selectedOperators.length;

        if (
            0 >= selectedLen /* no selection yet */ ||
            !(event.ctrlKey || event.shiftKey) /* no modifiers */
        ) {
            // use the current operator as the only selection
            this.selectedOperators = [operator];
        } else if (event.ctrlKey) {
            // single add / remove
            const index = this.selectedOperators.indexOf(operator);
            if (0 <= index) {
                // remove from selection
                this.selectedOperators.splice(index, 1);
            } else {
                // add to selection
                this.selectedOperators.push(operator);
            }
        } else if (event.shiftKey) {
            // range add / remove
            const startIndex = this.operators.indexOf(this.selectedOperators[0]);
            const endIndex = this.operators.indexOf(operator);
            if (0 <= startIndex && 0 <= endIndex) {
                this.selectedOperators = [];
                for (let i = Math.min(startIndex, endIndex); i <= Math.max(startIndex, endIndex); ++i) {
                    this.selectedOperators.push(this.operators[i]);
                }
            } else {
                console.error(
                    '{OperatorManagementRemoveComponent}',
                    '[onSelect]',
                    'Internal Logic Error',
                    'selectedOperators:', this.selectedOperators,
                    'operator:', operator,
                    'event:', event
                );
                return;
            }
        } else {
            console.error(
                '{OperatorManagementRemoveComponent}',
                '[onSelect]',
                'Internal Logic Error',
                'selectedOperators:', this.selectedOperators,
                'operator:', operator,
                'event:', event
            );
            return;
        }
        this.selectedOperators.sort(this.operatorService.compareOperator);
        console.log(
            '{OperatorManagementRemoveComponent}',
            '[onSelect]',
            'Count:', this.selectedOperators.length
        );
    }

    public onConfirm(): void {
        this.operatorsToRemove = this.selectedOperators;
        this.selectedOperators = [];
        this.operatorsToRemoveIndex = 0;
        this.operatorsToRemoveTotal = this.operatorsToRemove.length;

        const remove = () => {
            if (this.operatorsToRemoveIndex < this.operatorsToRemoveTotal) {
                const operator = this.operatorsToRemove[this.operatorsToRemoveIndex];
                console.log(
                    '{OperatorManagementRemoveComponent}',
                    '[onConfirm]',
                    'Removing operator [' + operator.id + '/' + operator.name + ']',
                    '[' + (this.operatorsToRemoveIndex + 1) + '/' + this.operatorsToRemoveTotal + ']'
                );

                this.operatorService.delete(operator.id).then(
                    success => {
                        console.log(
                            '{OperatorManagementRemoveComponent}',
                            '[onConfirm]',
                            'Removed operator:',
                            operator
                        );
                        ++this.operatorsToRemoveIndex;
                        remove();
                    },
                    failure => {
                        console.error(
                            '{OperatorManagementRemoveComponent}',
                            '[onConfirm]',
                            'Failed to remove operator:',
                            operator
                        );
                        this.onInit();
                    }
                );
            } else {
                console.log(
                    '{OperatorManagementRemoveComponent}',
                    '[onConfirm]',
                    'Successfully removed all operators'
                );
                this.onInit();
            }
        };
        remove();
    }

    public onCancel(): void {
        this.selectedOperators = [];
        console.log(
            '{OperatorManagementRemoveComponent}',
            '[onCacnel]'
        );
    }

    private onInit(): void {
        this.operators = undefined;
        this.selectedOperators = [];
        this.operatorsToRemove = [];
        this.operatorsToRemoveIndex = 0;
        this.operatorsToRemoveTotal = 0;
        this.getOperators();
    }

    public ngAfterContentInit(): void {
        this.onInit();
    }
}
