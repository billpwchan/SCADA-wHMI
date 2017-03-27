import { Component, AfterContentInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Profile } from '../type/profile';
import { ProfileService } from '../service/profile.service';

import { UtilService } from '../service/util.service';

@Component({
    selector: 'app-profile-management-modify',
    templateUrl: './profile-management-modify.component.html'
})
export class ProfileManagementModifyComponent implements AfterContentInit {
    public profiles: Profile[];
    public selectedProfile: Profile;

    constructor(
        private translateService: TranslateService,
        private profileService: ProfileService,
        private utilService: UtilService
    ) { }

    public getProfiles(): void {
        this.profileService.getProfiles().then(
            profiles => {
                this.profiles = profiles.sort(this.utilService.compareProfile);
            }
        );
    }

    public onSelect(profile: Profile): void {
        this.selectedProfile = profile;
        console.log(
            '[onSelect]',
            'ProfileId:', this.selectedProfile.id,
            'ProfileName:', this.selectedProfile.name,
            'ProfileDescription:', this.selectedProfile.description
        );
    }

    public onApply(): void {
    }

    public onReset(): void {
    }

    public onInit(): void {
        this.profiles = undefined;
        this.selectedProfile = undefined;
        this.getProfiles();
        console.log(
            '[onInit]',
            'Profiles#:',
            this.profiles ? this.profiles.length : 0
        );
    }

    public ngAfterContentInit(): void {
        this.onInit();
    }
}
