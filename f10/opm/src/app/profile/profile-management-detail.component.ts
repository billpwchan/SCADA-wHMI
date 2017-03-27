import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { Profile } from '../type/profile';
import { ProfileService } from '../service/profile.service';

import { Function } from '../type/function';
import { FunctionService } from '../service/function.service';

import { Location } from '../type/location';
import { LocationService } from '../service/location.service';

import { Mask } from '../type/mask';
import { MaskService } from '../service/mask.service';

import { Action } from '../type/action';
import { ActionService } from '../service/action.service';

import {StringMap, NumberMap} from '../service/util.service';

@Component({
    selector: 'app-profile-management-detail',
    templateUrl: './profile-management-detail.component.html'
})
export class ProfileManagementDetailComponent implements OnChanges, OnInit {
    @Input()
    public profileId: number;

    @Input()
    public createProfile: boolean;

    private originalProfile: Profile;
    public profile: Profile;
    private maskMap: StringMap<Mask>; // (funcId + '/' + locId) -> Mask

    private pesudoMaskIds: boolean[]; // maskId -> Mask
    private pesudoMasks: NumberMap<Mask[]>; // maskId -> Mask[]

    public updated: boolean;
    public masksToApplyIndex: number;
    public masksToApplyTotal: number;

    private functions: Function[];
    private locations: Location[];
    public actions: Action[];

    public selectedMask: Mask;

    public Math = Math;

    constructor(
        private translate: TranslateService,
        private profileService: ProfileService,
        private functionService: FunctionService,
        private locationService: LocationService,
        private actionService: ActionService,
        private maskService: MaskService
    ) {
    }

    public ngOnChanges(): void {
        this.onReset();
    }

    public ngOnInit(): void {
        this.getFunctions();
        this.getLocations();
        this.getActions();
    }

    private createNewMask(funcId: number, locId: number) {
        const mask = new Mask();
        mask.function.id = funcId;
        mask.location.id = locId;
        return mask;
    };

    private getMaskType(mask: Mask): number {
        if (!mask.id) {
            if (mask.mask1 || mask.mask2 || mask.mask3 || mask.mask4) {
                // newly created mask
                return 1;
            } else {
                // newly created empty mask
                return 2;
            }
        } else {
            if (mask.mask1 || mask.mask2 || mask.mask3 || mask.mask4) {
                // existing mask
                return 3;
            } else {
                // existing empty mask
                return 4;
            }
        }
    };

    private filterUpdatedMasks(masks: Mask[]) {
        const filteredMask: Mask[] = [];
        for (let i = 0; i < masks.length; ++i) {
            const mask = masks[i];
            const maskType = this.getMaskType(mask);
            if (2 === maskType) {
                // newly created empty mask
                continue;
            } else if (3 === maskType) {
                const isPesudoMask = this.pesudoMaskIds[mask.id];
                const pesudoMasks = this.pesudoMasks[mask.id];
                if (isPesudoMask && pesudoMasks && 0 < pesudoMasks.length) {
                    const mask1 = pesudoMasks[0].mask1;
                    const mask2 = pesudoMasks[0].mask2;
                    const mask3 = pesudoMasks[0].mask3;
                    const mask4 = pesudoMasks[0].mask4;

                    let allSame = true;
                    for (let k = 1; k < pesudoMasks.length; ++k) {
                        const curMask = pesudoMasks[k];
                        if (
                            curMask.mask1 !== mask1 ||
                            curMask.mask2 !== mask2 ||
                            curMask.mask3 !== mask3 ||
                            curMask.mask4 !== mask4
                        ) {
                            allSame = false;
                            break;
                        }
                    }
                    if (allSame) {
                        // all pesudo masks are same, only need, if changed, to update the master one
                        if (mask.mask1 !== mask1 || mask.mask2 !== mask2 || mask.mask3 !== mask3 || mask.mask4 !== mask4) {
                            // updated, add to list
                            filteredMask.push(mask);
                        } // else... not updated, no need to add to list
                    } else {
                        // not all pesudo masks are the same, need to delete the master one, and add the pesudo mask one by one
                        for (let l = 0; l < pesudoMasks.length; ++l) {
                            const pesudoMask = pesudoMasks[l];
                            const pesudoMaskType = this.getMaskType(pesudoMask);
                            if (2 === pesudoMaskType) {
                                // newly created emptyMask
                                continue;
                            } else {
                                filteredMask.push(pesudoMask);
                            }
                        }
                        mask.mask1 = mask.mask2 = mask.mask3 = mask.mask4 = '';
                        filteredMask.push(mask);
                    }
                    continue;
                } else {
                    // existing mask
                    let unchanged = false;
                    for (let j = 0; j < this.originalProfile.masks.length; ++j) {
                        const originalMask = this.originalProfile.masks[j];
                        if (originalMask.id !== mask.id) { continue; }
                        if (
                            originalMask.mask1 === mask.mask1 &&
                            originalMask.mask2 === mask.mask2 &&
                            originalMask.mask3 === mask.mask3 &&
                            originalMask.mask4 === mask.mask4
                        ) {
                            // unchanged
                            unchanged = true;
                        } // else... changed
                        break;
                    }
                    if (unchanged) { continue; }
                }
            }
            filteredMask.push(mask);
        }
        return filteredMask;
    }

    public getMaskByFuncAndLoc(funcId: number, locId: number): Mask {
        if (!this.profile) { return this.createNewMask(funcId, locId); }

        const maskKey = funcId + '/' + locId;
        const mask = this.maskMap[maskKey];
        if (mask) {
            return mask;
        } else {
            const newMask = this.createNewMask(funcId, locId);
            this.maskMap[maskKey] = newMask;
            this.profile.masks.push(newMask);
            return newMask;
        }
    }

    public onSelectCell(funcId: number, locId: number): void {
        console.log(
            '[onSelectCell]',
            'funcId:', funcId,
            'locId:', locId
        );
        this.selectedMask = this.getMaskByFuncAndLoc(funcId, locId);
        console.log(
            '[onSelectCell]',
            'MaskId:', this.selectedMask.id,
            'Mask1:', this.selectedMask.mask1,
            'Mask2:', this.selectedMask.mask2,
            'Mask3:', this.selectedMask.mask3,
            'Mask4:', this.selectedMask.mask4
        );
    }

    public onApply(): void {
        const masksToApply = this.filterUpdatedMasks(this.profile.masks);
        this.masksToApplyIndex = 0;
        this.masksToApplyTotal = masksToApply.length;

        const apply = () => {
            if (this.masksToApplyIndex < this.masksToApplyTotal) {
                const mask = masksToApply[this.masksToApplyIndex++];

                const maskType = this.getMaskType(mask);
                if (1 === maskType) {
                    // newly created mask
                    console.log(
                        '[onApply]',
                        'Creating Mask',
                        'ProfileId:', this.profile.id,
                        'ProfileName:', this.profile.name,
                        'Mask:', mask,
                        'Remaining:', this.masksToApplyTotal - this.masksToApplyIndex
                    );
                    this.maskService.createMaskForProfile(mask, this.profile).then(
                        res => {
                            console.log(
                                '[onApply]',
                                'Created Mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            apply();
                        },
                        res => {
                            console.error(
                                '[onApply]',
                                'Failed to create mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            this.onReset();
                        }
                    );
                } else if (2 === maskType) {
                    // internal logic error!
                    console.error(
                        '[onApply]',
                        'Invalid maskType:', maskType,
                        'Mask:', mask
                    );
                    this.onReset();
                } else if (3 === maskType) {
                    // update existing mask
                    console.log(
                        '[onApply]',
                        'Updating Mask',
                        'ProfileId:', this.profile.id,
                        'ProfileName:', this.profile.name,
                        'Mask:', mask,
                        'Remaining:', this.masksToApplyTotal - this.masksToApplyIndex
                    );
                    this.maskService.updateMaskForProfile(mask, this.profile).then(
                        res => {
                            console.log(
                                '[onApply]',
                                'Updated Mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            apply();
                        },
                        res => {
                            console.error(
                                '[onApply]',
                                'Failed to update mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            this.onReset();
                        }
                    );
                } else if (4 === maskType) {
                    // delete existing mask
                    console.log(
                        '[onApply]',
                        'Deleting Mask',
                        'ProfileId:', this.profile.id,
                        'ProfileName:', this.profile.name,
                        'Mask:', mask,
                        'Remaining:', masksToApply.length - this.masksToApplyIndex
                    );
                    this.maskService.deleteMask(mask).then(
                        res => {
                            console.log(
                                '[onApply]',
                                'Deleted Mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            apply();
                        },
                        res => {
                            console.error(
                                '[onApply]',
                                'Failed to delete mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            this.onReset();
                        }
                    );
                } else {
                    // internal logic error!
                    console.error(
                        '[onApply]',
                        'Invalid maskType:', maskType,
                        'Mask:', mask
                    );
                    this.onReset();
                }
            } else {
                // applied all masks successfully
                console.log(
                    '[onApply]',
                    'Successfully applied all masks'
                );
                this.onReset();
            }
        };
        apply();
    }

    public onCreate(): void {
        console.log(
            '[onCreate]',
            'Profile Name:', this.profile.name
        );
        const createProfile = () => {
            console.log(
                '[onCreate]',
                'Creating Profile',
                'Profile Name:', this.profile.name,
                'Profile Description:', this.profile.description
            );
            this.profileService.createProfile(this.profile).then(
                res => {
                    this.profile.id = res.id;
                    console.log(
                        '[onCreate]',
                        'Created Profile',
                        'Profile Name:', this.profile.name,
                        'Profile Description:', this.profile,
                        'Profile Id:', this.profile.id,
                        'Response:', res
                    );
                    this.onApply();
                },
                res => {
                    console.error(
                        '[onCreate]',
                        'Failed to create Profile',
                        'Profile Name:', this.profile.name,
                        'Profile Description:', this.profile,
                        'Res:', res
                    );
                }
            );
        };

        createProfile();
    }

    public onReset(): void {
        console.log('[onReset]');
        this.profile = undefined;
        this.originalProfile = undefined;
        this.maskMap = {};
        this.selectedMask = undefined;
        this.updated = false;
        this.masksToApplyIndex = 0;
        this.masksToApplyTotal = 0;

        this.pesudoMaskIds = [];
        this.pesudoMasks = {};

        if (this.profileId) {
            this.getProfileById(this.profileId);
        } else if (this.createProfile) {
            this.profile = new Profile();
        }
    }

    public toggleMask(newMask: string, maskType = 1): void {
        const toggle = function(mask: string, newAction: string): string {
            if (!mask) { return newAction; }
            if (mask.indexOf(newAction) >= 0) {
                mask = mask.replace(newAction, '');
            } else {
                mask += newAction;
            }
            return mask;
        };
        if (1 === maskType) {
            this.selectedMask.mask1 = toggle(this.selectedMask.mask1, newMask);
        } else if (2 === maskType) {
            this.selectedMask.mask2 = toggle(this.selectedMask.mask2, newMask);
        } else if (3 === maskType) {
            this.selectedMask.mask3 = toggle(this.selectedMask.mask3, newMask);
        } else if (4 === maskType) {
            this.selectedMask.mask4 = toggle(this.selectedMask.mask4, newMask);
        }

        this.updated = true;
        console.log(
            '[toggleMask]',
            this.createProfile ? 'Profile Name:' : 'Profile Id',
            this.createProfile ? this.profile.name : this.profile.id,
            'Mask Id:', this.selectedMask.id,
            'Func Id:', this.selectedMask.function.id,
            'Loc Id:', this.selectedMask.location.id,
            'Mask1:', this.selectedMask.mask1,
            'Mask2:', this.selectedMask.mask2,
            'Mask3:', this.selectedMask.mask3,
            'Mask4:', this.selectedMask.mask4
        );
    }

    public getActionByAbbrev(abbrev: string): Action {
        for (let i = 0; i < this.actions.length; ++i) {
            const action = this.actions[i];
            if (action.abbrev === abbrev) {
                return action;
            }
        }
        return undefined;
    }

    public getProfileById(id: number): void {
        this.profileService.getProfile(id).then(
            profile => {
                this.originalProfile = new Profile(profile);
                this.profile = profile;
                for (let i = 0; i < this.profile.masks.length; ++i ) {
                    const mask = this.profile.masks[i];
                    if (!mask.function) {
                        console.error(
                            '[getProfileById]',
                            'Invalid profile\'s mask content:',
                            'Profile: [' + this.profile.id + '/' + this.profile.name + '/' + this.profile.description + ']',
                            'Mask: [' + mask.id + '/' + mask.mask1 + '/' + mask.mask2 + '/' + mask.mask3 + '/' + mask.mask4 + ']',
                            'Function?', mask.function ? '[' +
                                mask.function.id + '/' + mask.function.name + '/' + mask.function.description +
                            ']' : 'NOK',
                            'Location?', mask.location ? '[' +
                                mask.location.id + '/' + mask.location.name + '/' + mask.location.description +
                            ']' : 'NOK'
                        );
                    } else if (!mask.location) {
                        const funcId = mask.function.id;
                        this.pesudoMaskIds[mask.id] = true;
                        this.pesudoMasks[mask.id] = [];
                        for (let j = 0; j < this.locations.length; ++j) {
                            const loc = this.locations[j];
                            const newMask = this.createNewMask(funcId, loc.id);
                            newMask.mask1 = mask.mask1;
                            newMask.mask2 = mask.mask2;
                            newMask.mask3 = mask.mask3;
                            newMask.mask4 = mask.mask4;
                            this.pesudoMasks[mask.id].push(newMask);

                            const maskKey = funcId + '/' + loc.id;
                            this.maskMap[maskKey] = newMask;
                        }
                        console.log(
                            '[getProfileById]',
                            'Function: [' + mask.function.id + '/' + mask.function.name + '/' + mask.function.description + ']',
                            'Generated pesudo masks for mask: [' +
                                mask.id + '/' + mask.mask1 + '/' + mask.mask2 + '/' + mask.mask3 + '/' + mask.mask4 +
                            ']'
                        );
                    } else {
                        const maskKey = mask.function.id + '/' + mask.location.id;
                        this.maskMap[maskKey] = mask;
                    }
                }
            }
        );
    }

    private getFunctions(): void {
        this.functionService.getFunctions().then(
            functions => this.functions = functions
        );
    }

    private getLocations(): void {
        this.locationService.getLocations().then(
            locations => this.locations = locations
        );
    }

    private getActions(): void {
        this.actionService.getActions().then(
            actions => this.actions = actions
        );
    }
}
