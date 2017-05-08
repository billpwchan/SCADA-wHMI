import { Component, Input, OnChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Operator } from '../type/operator';
import { OperatorService } from '../service/operator.service';

import { Profile } from '../type/profile';
import { ProfileService } from '../service/profile.service';

@Component({
    selector: 'app-operator-management-detail',
    templateUrl: './operator-management-detail.component.html'
})
export class OperatorManagementDetailComponent implements OnChanges {
    @Input()
    public operatorId: number;

    @Input()
    public createOperator: boolean;

    @Input()
    public readonly: boolean;

    public operator: Operator;
    public newPassword: string;

    public availableProfiles: Profile[];
    public selectedAvailableProfiles: Profile[];
    public assignedProfiles: Profile[];
    public selectedAssignedProfiles: Profile[];

    public updated: boolean;
    public saving: boolean;
    public nameConflict: boolean;

    constructor(
        private translate: TranslateService,
        private profileService: ProfileService,
        private operatorService: OperatorService
    ) {
    }

    private sanityCheck(): boolean {
        // assigned profiles cannot be in available profiles
        // assigned profiles must be in profiles
        for (let i = 0; i < this.assignedProfiles.length; ++i) {
            const p = this.assignedProfiles[i];
            if (0 <= this.availableProfiles.indexOf(p)) {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[sanityCheck]',
                    'assignedProfile can be found in availableProfile'
                );
                return false;
            }
        }

        // available profiles cannot be in assigned profiles
        // available profiles must be in profiles
        for (let i = 0; i < this.availableProfiles.length; ++i) {
            const p = this.availableProfiles[i];
            if (0 <= this.assignedProfiles.indexOf(p)) {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[sanityCheck]',
                    'availableProfile can be found in assignedProfile'
                );
                return false;
            }
        }

        // passed sanity check
        return true;
    }

    public onAssign(): void {
        while (0 < this.selectedAvailableProfiles.length) {
            const p = this.selectedAvailableProfiles.pop();
            this.assignedProfiles.push(p);
            const i = this.availableProfiles.indexOf(p);
            if (0 <= i) {
                this.availableProfiles.splice(i, 1);
            } else {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[onAssign]',
                    'Internal Consistency Error',
                    'Failed to remove profile: ' + '[' +
                    p.id + '/' + p.name + '/' + p.description +
                    '] ' + 'from availableProfiles',
                    '[IGNORED]'
                );
            }
        }
        this.assignedProfiles = this.assignedProfiles.sort(this.profileService.compareProfile);
        this.selectedAssignedProfiles = [];
        this.updated = true;
        console.log(
            '{OperatorManagementDetailComponent}',
            '[onAssign]',
            'Assigned#:', this.assignedProfiles.length,
            'Available#:', this.availableProfiles.length
        );

        if (!this.sanityCheck()) {
            console.error(
                '{OperatorManagementDetailComponent}',
                '[onAssign]',
                'Internal Consistency Error',
                'Failed sanity check'
            );
        }
    }

    public onRemove(): void {
        while (0 < this.selectedAssignedProfiles.length) {
            const p = this.selectedAssignedProfiles.pop();
            this.availableProfiles.push(p);
            const i = this.assignedProfiles.indexOf(p);
            if (0 <= i) {
                this.assignedProfiles.splice(i, 1);
            } else {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[onRemove]',
                    'Internal Consistency Error',
                    'Failed to remove profile: ' + '[' +
                    p.id + '/' + p.name + '/' + p.description +
                    '] ' + 'from assignedProfiles',
                    '[IGNORED]'
                );
            }
        }
        this.availableProfiles = this.availableProfiles.sort(this.profileService.compareProfile);
        this.selectedAvailableProfiles = [];
        this.updated = true;
        console.log(
            '{OperatorManagementDetailComponent}',
            '[onRemove]',
            'Assigned#:', this.assignedProfiles.length,
            'Available#:', this.availableProfiles.length
        );

        if (!this.sanityCheck()) {
            console.error(
                '{OperatorManagementDetailComponent}',
                '[onAssign]',
                'Internal Consistency Error',
                'Failed sanity check'
            );
        }
    }

    public onApply(): void {
        console.log(
            '{OperatorManagementDetailComponent}',
            '[onApply]',
            'Modifying operator: ' + '[' + this.operator.name + ']'
        );
        this.operator.profiles = this.assignedProfiles;
        this.saving = true;
        this.operatorService.update(this.operator).then(
            success => {
                console.log(
                    '{OperatorManagementDetailComponent}',
                    '[onApply]',
                    'Modified operator: ' + '[' + this.operator.name + ']',
                    'Id:',
                    success
                );
                this.onReset();
            },
            failure => {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[onCreate]',
                    'Failed to modify operator: ' + '[' + this.operator.name + ']'
                );
                this.onReset();
            }
        );
    }

    public onCreate(): void {
        console.log(
            '{OperatorManagementDetailComponent}',
            '[onCreate]',
            'Creating operator: ' + '[' + this.operator.name + ']'
        );
        this.operator.profiles = this.assignedProfiles;
        this.saving = true;
        this.operatorService.create(this.operator).then(
            success => {
                console.log(
                    '{OperatorManagementDetailComponent}',
                    '[onCreate]',
                    'Created operator: ' + '[' + this.operator.name + ']',
                    'Id:',
                    success
                );
                this.onReset();
            },
            failure => {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[onCreate]',
                    'Failed to create operator: ' + '[' + this.operator.name + ']'
                );
                this.onReset();
            }
        );
    }

    public onSelect(
        profile: Profile,
        profileSet: Profile[],
        selectedProfiles: Profile[],
        event: MouseEvent
    ): void {
        const emptyProfileSet = function(ps: Profile[]) {
            while (0 < ps.length) { ps.pop(); }
        };
        const selectedLen = selectedProfiles.length;

        if (
            0 >= selectedLen /* no selection yet */ ||
            !(event.ctrlKey || event.shiftKey) /* no modifiers */
        ) {
            // use the current profile as the only selection
            emptyProfileSet(selectedProfiles);
            selectedProfiles.push(profile);
        } else if (event.ctrlKey) {
            // single add / remove
            const index = selectedProfiles.indexOf(profile);
            if (0 <= index) {
                // remove from selection
                selectedProfiles.splice(index, 1);
            } else {
                // add to selection
                selectedProfiles.push(profile);
            }
        } else if (event.shiftKey) {
            // range add / remove
            const startIndex = profileSet.indexOf(selectedProfiles[0]);
            const endIndex = profileSet.indexOf(profile);
            if (0 <= startIndex && 0 <= endIndex) {
                emptyProfileSet(selectedProfiles);
                for (let i = Math.min(startIndex, endIndex); i <= Math.max(startIndex, endIndex); ++i) {
                    selectedProfiles.push(profileSet[i]);
                }
            } else {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[onSelect]',
                    'Internal Logic Error',
                    'selectedProfiles:', selectedProfiles,
                    'profile:', profile,
                    'event:', event
                );
                return;
            }
        } else {
            console.error(
                '{OperatorManagementDetailComponent}',
                '[onSelect]',
                'Internal Logic Error',
                'selectedProfiles:', selectedProfiles,
                'profile:', profile,
                'event:', event
            );
            return;
        }

        console.log(
            '{OperatorManagementDetailComponent}',
            '[onSelect]',
            'Count:', selectedProfiles.length,
        );
    }

    public onNameChange(): void {
        if (0 >= this.operator.name.length) {
            this.nameConflict = false;
            return;
        }
        this.operatorService.getByName(this.operator.name).then(
            success => {
                this.nameConflict = null != success;
                if (this.nameConflict) {
                    console.log(
                        '{OperatorManagementDetailComponent}',
                        '[onNameChange]',
                        'Operator name:', this.operator.name,
                        'is already in use'
                    );
                }
            },
            faliure => {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[onNameChange]',
                    'Failed in checking operator name availability'
                );
            }
        );
    }

    private indexOfProfile(ps: Profile[], p: Profile): number {
        for (let i = 0; i < ps.length; ++i) {
            if (ps[i].id === p.id) { return i; }
        }
        return -1;
    }

    private getProfilesAsAvailable(): void {
        console.log(
            '{OperatorManagementDetailComponent}',
            '[getProfilesAsAvailable]',
            'Retrieving profiles'
        );
        this.profileService.getAll().then(
            profiles => {
                if (profiles) {
                    profiles.forEach(
                        (element) => {
                            if (0 > this.indexOfProfile(this.assignedProfiles, element)) {
                                this.availableProfiles.push(element);
                            }
                        }
                    );
                    this.availableProfiles = this.availableProfiles.sort(this.profileService.compareProfile);
                    console.log(
                        '{OperatorManagementDetailComponent}',
                        '[getProfilesAsAvailable]',
                        'Assigned#:', this.assignedProfiles.length,
                        'Available#:', this.availableProfiles.length
                    );
                } else {
                    console.error(
                        '{OperatorManagementDetailComponent}',
                        '[getProfilesAsAvailable]',
                        'Failed to retrieve profiles'
                    );
                }
            },
            failure => {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[getProfilesAsAvailable]',
                    'Failed to retrieve profiles'
                );
            }
        );
    }

    private getOperator(id: number): void {
        this.operatorService.get(id).then(
            success => {
                if (success) {
                    this.operator = success;
                    if (null == this.operator.profiles) { this.operator.profiles = []; }
                    this.operator.profiles.forEach(p => this.assignedProfiles.push(p));
                    console.log(
                        '{OperatorManagementDetailComponent}',
                        '[getOperator]',
                        'Retrieved operator:', this.operator
                    );
                    this.getProfilesAsAvailable();
                } else {
                    console.error(
                        '{OperatorManagementDetailComponent}',
                        '[getOperator]',
                        'Failed to retrieve operator id:', id
                    );
                }
            },
            failure => {
                console.error(
                    '{OperatorManagementDetailComponent}',
                    '[getOperator]',
                    'Failed to retrieve operator id:', id
                );
            }
        );
    }

    public onReset(): void {
        console.log(
            '{OperatorManagementDetailComponent}',
            '[onReset]'
        );
        this.newPassword = '';
        this.availableProfiles = [];
        this.selectedAvailableProfiles = [];
        this.assignedProfiles = [];
        this.selectedAssignedProfiles = [];
        this.updated = false;
        this.saving = false;
        this.nameConflict = false;

        if (this.operatorId) {
            this.getOperator(this.operatorId);
        } else if (this.createOperator) {
            this.operator = undefined;
            this.getProfilesAsAvailable();
            this.operator = new Operator();
        }
    }

    public ngOnChanges(): void { this.onReset(); }
}
