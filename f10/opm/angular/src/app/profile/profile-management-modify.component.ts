import { Component, OnInit, OnDestroy, AfterContentInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';

import { Profile } from '../type/profile';
import { ProfileService } from '../service/profile.service';

@Component({
    selector: 'app-profile-management-modify',
    templateUrl: './profile-management-modify.component.html'
})
export class ProfileManagementModifyComponent implements OnInit, OnDestroy, AfterContentInit {
    public profiles: Profile[];
    public selectedProfile: Profile;
    public readonly: boolean;
    private queryParamsSub: Subscription;

    constructor(
        private route: ActivatedRoute,
        private translateService: TranslateService,
        private profileService: ProfileService
    ) { }

    public getProfiles(): void {
        this.profileService.getAll().then(
            profiles => {
                this.profiles = profiles ? profiles.sort(this.profileService.compareProfile) : [];
                console.log(
                    '{ProfileManagementModifyComponent}',
                    '[getProfiles]',
                    'Profiles#:',
                    this.profiles ? this.profiles.length : 0
                );
            },
            failure => {
                console.error(
                    '{ProfileManagementModifyComponent}',
                    '[getProfiles]',
                    'Failed:', failure
                );
            }
        );
    }

    public onSelect(profile: Profile): void {
        this.selectedProfile = profile;
        console.log(
            '{ProfileManagementModifyComponent}',
            '[onSelect]',
            'ProfileId:', this.selectedProfile.id,
            'ProfileName:', this.selectedProfile.name,
            'ProfileDescription:', this.selectedProfile.description
        );
    }

    public ngOnInit(): void {
        this.queryParamsSub = this.route.queryParams.subscribe(
            params => {
                this.readonly = (1 === +params['readonly']) || false;
                console.log(
                    '{ProfileManagementModifyComponent}',
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
        this.profiles = undefined;
        this.selectedProfile = undefined;
        this.getProfiles();
    }
}
