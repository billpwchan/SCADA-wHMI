import {Component, AfterContentInit} from '@angular/core';

import {Profile} from '../type/profile';
import {ProfileService} from '../service/profile.service';

import {UtilService} from '../service/util.service';

@Component({
    selector: 'profile-management-modify',
    templateUrl: './profile-management-modify.component.html',
    styleUrls: ['./profile-management-modify.component.css']
})
export class ProfileManagementModifyComponent implements AfterContentInit{
    profiles: Profile[];
    selectedProfile: Profile;

    constructor(
        private profileService: ProfileService,
        private utilService: UtilService
    ) {}

    getProfiles(): void{
        this.profileService.getProfiles().then(
            profiles => {
                this.profiles = profiles.sort(this.utilService.compareProfile);
            }
        );
    }

    onSelect(profile: Profile): void {
        this.selectedProfile = profile;
        console.log(
            '[onSelect]',
            'ProfileId:', this.selectedProfile.id,
            'ProfileName:', this.selectedProfile.name,
            'ProfileDescription:', this.selectedProfile.description
        );
    }

    onApply(): void{
    }

    onReset(): void{
    }

    onInit(): void{
        this.profiles = undefined;
        this.selectedProfile = undefined;
        this.getProfiles();
        console.debug(
            '[onInit]',
            'Profiles#:',
            this.profiles ? this.profiles.length:0
        );
    }

    ngAfterContentInit(): void {
        this.onInit();
    }
}