import { Component, AfterContentInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Profile } from '../type/profile';
import { ProfileService } from '../service/profile.service';

import { MaskService } from '../service/mask.service';

@Component({
    selector: 'app-profile-management-remove',
    templateUrl: './profile-management-remove.component.html'
})
export class ProfileManagementRemoveComponent implements AfterContentInit {
    profiles: Profile[];
    selectedProfiles: Profile[];

    profilesToRemove: Profile[];
    profilesToRemoveIndex: number;
    profilesToRemoveTotal: number;

    constructor(
        private translate: TranslateService,
        private profileService: ProfileService,
        private maskService: MaskService
    ) { }

    public getProfiles(): void {
        this.profileService.getAll().then(
            profiles => {
                this.profiles = profiles ? profiles.sort(this.profileService.compareProfile) : [];
                console.log(
                    '{ProfileManagementRemoveComponent}',
                    '[getProfiles]',
                    'Profiles#:',
                    this.profiles ? this.profiles.length : 0
                );
            },
            failure => {
                console.error(
                    '{ProfileManagementRemoveComponent}',
                    '[getProfiles]',
                    'Failed:', failure
                );
            }
        );
    }

    public onSelect(profile: Profile, event: MouseEvent): void {
        const selectedLen = this.selectedProfiles.length;

        if (
            0 >= selectedLen /* no selection yet */ ||
            !(event.ctrlKey || event.shiftKey) /* no modifiers */
        ) {
            // use the current profile as the only selection
            this.selectedProfiles = [profile];
        } else if (event.ctrlKey) {
            // single add / remove
            const index = this.selectedProfiles.indexOf(profile);
            if (0 <= index) {
                // remove from selection
                this.selectedProfiles.splice(index, 1);
            } else {
                // add to selection
                this.selectedProfiles.push(profile);
            }
        } else if (event.shiftKey) {
            // range add / remove
            const startIndex = this.profiles.indexOf(this.selectedProfiles[0]);
            const endIndex = this.profiles.indexOf(profile);
            if (0 <= startIndex && 0 <= endIndex) {
                this.selectedProfiles = [];
                for (let i = Math.min(startIndex, endIndex); i <= Math.max(startIndex, endIndex); ++i) {
                    this.selectedProfiles.push(this.profiles[i]);
                }
            } else {
                console.error(
                    '{ProfileManagementRemoveComponent}',
                    '[onSelect]',
                    'Internal Logic Error',
                    'selectedProfiles:', this.selectedProfiles,
                    'profile:', profile,
                    'event:', event
                );
                return;
            }
        } else {
            console.error(
                '{ProfileManagementRemoveComponent}',
                '[onSelect]',
                'Internal Logic Error',
                'selectedProfiles:', this.selectedProfiles,
                'profile:', profile,
                'event:', event
            );
            return;
        }
        this.selectedProfiles.sort(this.profileService.compareProfile);
        console.log(
            '{ProfileManagementRemoveComponent}',
            '[onSelect]',
            'Count:', this.selectedProfiles.length
        );
    }

    public onConfirm(): void {
        this.profilesToRemove = this.selectedProfiles;
        this.selectedProfiles = [];
        this.profilesToRemoveIndex = 0;
        this.profilesToRemoveTotal = this.profilesToRemove.length;

        const remove = () => {
            if (this.profilesToRemoveIndex < this.profilesToRemoveTotal) {
                const profile = this.profilesToRemove[this.profilesToRemoveIndex];
                console.log(
                    '{ProfileManagementRemoveComponent}',
                    '[onConfirm]',
                    'Removing profile [' + profile.id + '/' + profile.name + ']',
                    '[' + (this.profilesToRemoveIndex + 1) + '/' + this.profilesToRemoveTotal + ']'
                );
                const masks = profile.masks;
                if (masks && 0 < masks.length) {
                    const mask = masks.pop();
                    console.log(
                        '{ProfileManagementRemoveComponent}',
                        '[onConfirm]',
                        'Removing mask [' + mask.id + '] of profile [' + profile.id + '/' + profile.name + ']'
                    );
                    this.maskService.delete(mask).then(
                        success => {
                            console.log(
                                '{ProfileManagementRemoveComponent}',
                                '[onConfirm]',
                                'Removed mask [' + mask.id + '] of profile [' + profile.id + '/' + profile.name + ']'
                            );
                            remove();
                        },
                        failure => {
                            console.error(
                                '{ProfileManagementRemoveComponent}',
                                '[onConfirm]',
                                'Failed to remove mask [' + mask.id + '] of profile [' + profile.id + '/' + profile.name + ']'
                            );
                            this.onInit();
                        }
                    );
                } else {
                    this.profileService.delete(profile).then(
                        success => {
                            console.log(
                                '{ProfileManagementRemoveComponent}',
                                '[onConfirm]',
                                'Removed profile:',
                                profile
                            );
                            ++this.profilesToRemoveIndex;
                            remove();
                        },
                        failure => {
                            console.error(
                                '{ProfileManagementRemoveComponent}',
                                '[onConfirm]',
                                'Failed to remove profile:',
                                profile
                            );
                            this.onInit();
                        }
                    );
                }
            } else {
                console.log(
                    '{ProfileManagementRemoveComponent}',
                    '[onConfirm]',
                    'Successfully removed all profiles'
                );
                this.onInit();
            }
        };
        remove();
    }

    public onCancel(): void {
        this.selectedProfiles = [];
        console.log(
            '{ProfileManagementRemoveComponent}',
            '[onCacnel]'
        );
    }

    public onInit(): void {
        this.profiles = undefined;
        this.selectedProfiles = [];
        this.profilesToRemove = [];
        this.profilesToRemoveIndex = 0;
        this.profilesToRemoveTotal = 0;
        this.getProfiles();
    }

    public ngAfterContentInit(): void {
        this.onInit();
    }
}
