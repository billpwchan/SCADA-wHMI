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

import { ConfigService } from '../service/config.service';

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

    @Input()
    public readonly: boolean;

    private originalProfile: Profile;
    public profile: Profile;
    private maskTable: Mask[][]; // Mask table map to the display [row][col] -> Mask

    private pesudoMaskToSourceMap: StringMap<Mask>; // locId,funId -> Mask
    private sourceToPesudoMaskMap: NumberMap<Mask[]>; // maskId -> Mask[]

    public updated: boolean;
    public masksToApplyIndex: number;
    public masksToApplyTotal: number;

    public functions: Function[];
    private funCatToColMap: NumberMap<number>;
    private colToFunCatMap: NumberMap<Function>;

    public locations: Location[];
    private locCatToRowMap: NumberMap<number>;
    private rowToLocCatMap: NumberMap<Location>;

    public actions: Action[];
    private maskValueSortTemplate: string[];

    public selectedMasks: Mask[];

    public Math = Math;

    constructor(
        private translate: TranslateService,
        private configService: ConfigService,
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

    private createNewMask(locId: number, funcId: number) {
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

    private filterUpdatedMasks() {
        let newCreate = 0;
        let existingChanged = 0;
        let existingPesudo = 0;
        const processedSourceMasks: number[] = [];
        const filteredMask: Mask[] = [];
        for (let i = 0; i < this.maskTable.length; ++i) {
            const masks = this.maskTable[i];
            for (let j = 0; j < masks.length; ++j) {
                const mask = masks[j];
                const maskType = this.getMaskType(mask);
                if (1 === maskType) {
                    const sourceMask = this.getPesudoMaskSource(mask);
                    if (undefined !== sourceMask) {
                        // pesudoMask
                        if (0 <= processedSourceMasks.indexOf(sourceMask.id)) {
                            // already processed, skip
                            continue;
                        }
                        const pesudoMasks = this.sourceToPesudoMaskMap[sourceMask.id];
                        let isAllPesudoMaskSame = true;
                        for (let k = 0; k < pesudoMasks.length - 1; ++k) {
                            if (!this.maskService.isSameMaskValues(pesudoMasks[k], pesudoMasks[k + 1])) {
                                isAllPesudoMaskSame = false;
                                break;
                            }
                        }
                        if (isAllPesudoMaskSame) {
                            // all pesudo masks are same, only need, if changed, to update the source one
                            if (!this.maskService.isSameMaskValues(sourceMask, pesudoMasks[0])) {
                                // updated, add to list
                                // after updated the source mask here, other iterations will not add it
                                // as the source mask will then be the same as the pesudo masks
                                this.maskService.copyMaskValues(pesudoMasks[0], sourceMask);
                                filteredMask.push(sourceMask);
                                ++existingPesudo;
                            } // else... not updated, no need to add to list
                        } else {
                            // not all pesudo masks are the same, need to delete the source one, and add the pesudo mask one by one
                            for (let k = 0; k < pesudoMasks.length; ++k) {
                                const pesudoMask = pesudoMasks[k];
                                const pesudoMaskType = this.getMaskType(pesudoMask);
                                if (2 === pesudoMaskType) {
                                    // newly created emptyMask
                                    continue;
                                } else {
                                    filteredMask.push(pesudoMask);
                                    ++existingPesudo;
                                }
                            }
                            this.maskService.setMaskValues(sourceMask, '');
                            filteredMask.push(sourceMask);
                            ++existingPesudo;
                        }
                        // add to processed mask, so that we will not process it again
                        processedSourceMasks.push(sourceMask.id);
                    } else {
                        // newly created mask
                        filteredMask.push(mask);
                        ++newCreate;
                    }
                } else if (2 === maskType) {
                    // newly created empty mask
                } else if (3 === maskType || 4 === maskType) {
                    // existing mask
                    for (let k = 0; k < this.originalProfile.masks.length; ++k) {
                        const originalMask = this.originalProfile.masks[k];
                        if (originalMask.id !== mask.id) { continue; }
                        if (!this.maskService.isSameMaskValues(originalMask, mask)) {
                            // changed
                            filteredMask.push(mask);
                            ++existingChanged;
                        } // else... unchanged
                        break;
                    }
                } else {
                    console.error(
                        '{ProfileManagementDetailComponent}',
                        '[filterUpdatedMasks]',
                        'Invalid MaskType:', maskType,
                        'Mask:', mask
                    );
                }
            }
        }
        console.log(
            '{ProfileManagementDetailComponent}',
            '[filterUpdatedMasks]',
            'Total:', filteredMask.length,
            'newCreate:', newCreate,
            'existingChanged:', existingChanged,
            'existingPesudo:', existingPesudo
        );
        return filteredMask;
    }

    public onSelectCell(row: number, column: number, event: MouseEvent): void {
        if (
            !(
                (this.createProfile && this.profile.name) ||
                (!this.createProfile && this.profile)
            )
        ) {
            return;
        }
        console.log(
            '{ProfileManagementDetailComponent}',
            '[onSelectCell]',
            'row:', row,
            'column:', column,
            'event:', event
        );

        const mask = this.maskTable[row][column];
        if (
            0 >= this.selectedMasks.length ||
            !(event.ctrlKey || event.shiftKey)
        ) {
            this.selectedMasks = [mask];
        } else if (event.ctrlKey) {
            const index = this.selectedMasks.indexOf(mask);
            if (0 <= index) {
                this.selectedMasks.splice(index, 1);
            } else {
                this.selectedMasks.push(mask);
            }
        } else if (event.shiftKey) {
            const startCol = this.funCatToColMap[this.selectedMasks[0].function.id];
            const startRow = this.locCatToRowMap[this.selectedMasks[0].location.id];
            this.selectedMasks = [];
            for (let i = Math.min(startRow, row); i <= Math.max(startRow, row); ++i) {
                for (let j = Math.min(startCol, column); j <= Math.max(startCol, column); ++j) {
                    this.selectedMasks.push(this.maskTable[i][j]);
                }
            }
        } else {
            console.error(
                '{ProfileManagementDetailComponent}',
                '[onSelectCell]',
                'Internal Logic Error',
                'selectedMasks:', this.selectedMasks,
                'event:', event
            );
            return;
        }

        console.log(
            '{ProfileManagementDetailComponent}',
            '[onSelectCell]',
            'Mask#:', this.selectedMasks.length,
            'Masks:', this.selectedMasks
        );
    }

    public isSelectedCell(row: number, column: number): boolean {
        if (
            !(
                (this.createProfile && this.profile.name) ||
                !this.createProfile
            )
        ) {
            return false;
        }

        if (!this.maskTable) {
            return false;
        }

        const mask = this.maskTable[row][column];
        let locCat = undefined;
        let funCat = undefined;
        if (!mask.id) {
            locCat = this.rowToLocCatMap[row].id;
            funCat = this.colToFunCatMap[column].id;
        }
        for (let i = 0; i < this.selectedMasks.length; ++i) {
            const selectedMask = this.selectedMasks[i];
            if (!mask.id) {
                if (
                    selectedMask.function.id === funCat &&
                    selectedMask.location.id === locCat
                ) {
                    return true;
                }
            } else if (mask.id === selectedMask.id) {
                return true;
            }
        }
        return false;
    }

    public onApply(): void {
        const masksToApply = this.filterUpdatedMasks();
        this.masksToApplyIndex = 0;
        this.masksToApplyTotal = masksToApply.length;

        const apply = () => {
            if (this.masksToApplyIndex < this.masksToApplyTotal) {
                const mask = masksToApply[this.masksToApplyIndex++];

                const maskType = this.getMaskType(mask);
                if (1 === maskType) {
                    // newly created mask
                    console.log(
                        '{ProfileManagementDetailComponent}',
                        '[onApply]',
                        'Creating Mask',
                        'ProfileId:', this.profile.id,
                        'ProfileName:', this.profile.name,
                        'Mask:', mask,
                        'Remaining:', this.masksToApplyTotal - this.masksToApplyIndex
                    );
                    this.maskService.createForProfile(mask, this.profile).then(
                        success => {
                            console.log(
                                '{ProfileManagementDetailComponent}',
                                '[onApply]',
                                'Created Mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            apply();
                        },
                        failure => {
                            console.error(
                                '{ProfileManagementDetailComponent}',
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
                        '{ProfileManagementDetailComponent}',
                        '[onApply]',
                        'Invalid maskType:', maskType,
                        'Mask:', mask
                    );
                    this.onReset();
                } else if (3 === maskType) {
                    // update existing mask
                    console.log(
                        '{ProfileManagementDetailComponent}',
                        '[onApply]',
                        'Updating Mask',
                        'ProfileId:', this.profile.id,
                        'ProfileName:', this.profile.name,
                        'Mask:', mask,
                        'Remaining:', this.masksToApplyTotal - this.masksToApplyIndex
                    );
                    this.maskService.updateForProfile(mask, this.profile).then(
                        success => {
                            console.log(
                                '{ProfileManagementDetailComponent}',
                                '[onApply]',
                                'Updated Mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            apply();
                        },
                        failure => {
                            console.error(
                                '{ProfileManagementDetailComponent}',
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
                        '{ProfileManagementDetailComponent}',
                        '[onApply]',
                        'Deleting Mask',
                        'ProfileId:', this.profile.id,
                        'ProfileName:', this.profile.name,
                        'Mask:', mask,
                        'Remaining:', masksToApply.length - this.masksToApplyIndex
                    );
                    this.maskService.delete(mask).then(
                        success => {
                            console.log(
                                '{ProfileManagementDetailComponent}',
                                '[onApply]',
                                'Deleted Mask',
                                'ProfileId:', this.profile.id,
                                'ProfileName:', this.profile.name,
                                'Mask:', mask
                            );
                            apply();
                        },
                        faliure => {
                            console.error(
                                '{ProfileManagementDetailComponent}',
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
                        '{ProfileManagementDetailComponent}',
                        '[onApply]',
                        'Invalid maskType:', maskType,
                        'Mask:', mask
                    );
                    this.onReset();
                }
            } else {
                // applied all masks successfully
                console.log(
                    '{ProfileManagementDetailComponent}',
                    '[onApply]',
                    'Successfully applied all masks'
                );
                this.onReset();
            }
        };
        console.log(
            '{ProfileManagementDetailComponent}',
            '[onApply]',
            'masksToApplyTotal:', this.masksToApplyTotal
        );
        apply();
    }

    public onCreate(): void {
        console.log(
            '{ProfileManagementDetailComponent}',
            '[onCreate]',
            'Profile Name:', this.profile.name
        );
        const create = () => {
            console.log(
                '{ProfileManagementDetailComponent}',
                '[onCreate]',
                'Creating Profile',
                'Profile Name:', this.profile.name,
                'Profile Description:', this.profile.description
            );
            this.profileService.create(this.profile).then(
                res => {
                    this.profile.id = res.id;
                    console.log(
                        '{ProfileManagementDetailComponent}',
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
                        '{ProfileManagementDetailComponent}',
                        '[onCreate]',
                        'Failed to create Profile',
                        'Profile Name:', this.profile.name,
                        'Profile Description:', this.profile,
                        'Res:', res
                    );
                }
            );
        };

        create();
    }

    public onReset(): void {
        console.log(
            '{ProfileManagementDetailComponent}',
            '[onReset]'
        );
        this.profile = undefined;
        this.originalProfile = undefined;
        this.selectedMasks = [];
        this.updated = false;
        this.masksToApplyIndex = 0;
        this.masksToApplyTotal = 0;

        this.maskTable = undefined;

        if (this.profileId) {
            this.getProfileById(this.profileId);
        } else if (this.createProfile) {
            this.profile = new Profile();
            this.buildMaskTable();
        }
    }

    public toggleMask(newMask: string, maskType = 1): void {
        if (0 >= this.selectedMasks.length) { return; }

        const hasNewMaskValue = this.maskService.isMaskValueSetById(this.selectedMasks[0], maskType, newMask);
        const toggleAction = hasNewMaskValue ?
            (mask: Mask, id: number, value: string) => this.maskService.removeMaskValueById(mask, id, value) :
            (mask: Mask, id: number, value: string) => this.maskService.addMaskValueById(mask, id, value, this.maskValueSortTemplate);

        this.selectedMasks.forEach(mask => {
            toggleAction(mask, maskType, newMask);
        });

        this.updated = true;
        console.log(
            '{ProfileManagementDetailComponent}',
            '[toggleMask]',
            this.createProfile ? 'Profile Name:' : 'Profile Id',
            this.createProfile ? this.profile.name : this.profile.id,
            'Action:', hasNewMaskValue ? 'Remove' : 'Add',
            'Value:', newMask,
            'selectedMasks:', this.selectedMasks
        );
    }

    public isActionSelected(maskId: number, action: string): boolean {
        if (0 >= this.selectedMasks.length) { return false; }
        for (let i = 0; i < this.selectedMasks.length; ++i) {
            const mask = this.selectedMasks[i];
            const maskValue = this.maskService.getMaskValueById(mask, maskId);
            if (maskValue && (maskValue.indexOf(action) >= 0)) {
                return true;
            }
        }
        return false;
    }

    public isActionSelectedConsistent(maskId: number, action: string): boolean {
        if (0 >= this.selectedMasks.length) { return true; }
        const maskValue = this.maskService.getMaskValueById(this.selectedMasks[0], maskId);
        const checked = !maskValue ? false : (0 <= maskValue.indexOf(action));
        for (let i = 1; i < this.selectedMasks.length; ++i) {
            const mask = this.selectedMasks[i];
            const testMaskValue = this.maskService.getMaskValueById(mask, maskId);
            const testChecked = !testMaskValue ? false : (0 <= testMaskValue.indexOf(action));
            if (checked !== testChecked) {
                return false;
            }
        }
        return true;
    }

    public isMasksSelected(): boolean {
        return 0 < this.selectedMasks.length;
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
        this.profileService.get(id).then(
            profile => {
                if (profile) {
                    if (profile.masks) {
                        for (let i = 0; i < profile.masks.length; ++i) {
                            const mask = profile.masks[i];
                            if (!mask) { continue; }
                            this.maskService.sortMaskValues(mask, this.maskValueSortTemplate);
                        }
                    }
                    this.originalProfile = new Profile(profile);
                    this.profile = profile;
                    this.buildMaskTable();
                }
            }
        );
    }

    private createPesudoMask(locId: number, funId: number, mask: Mask): Mask {
        const newMask = this.createNewMask(locId, funId);
        this.maskService.copyMaskValues(mask, newMask);
        this.pesudoMaskToSourceMap[locId + ',' + funId] = mask;
        if (this.sourceToPesudoMaskMap[mask.id]) {
            this.sourceToPesudoMaskMap[mask.id].push(newMask);
        } else {
            this.sourceToPesudoMaskMap[mask.id] = [newMask];
        }
        return newMask;
    }
    private getPesudoMaskSource(pesudoMask: Mask): Mask {
        const key = pesudoMask.location.id + ',' + pesudoMask.function.id;
        return this.pesudoMaskToSourceMap[key];
    }
    private buildMaskTable(): void {
        const profile = this.profile;
        const locations = this.locations;
        const functions = this.functions;

        if (!profile || !locations || !functions) {
            console.log(
                '{ProfileManagementDetailComponent}',
                '[buildMaskTable]',
                'profile:', profile ? 'Ready' : 'Not Ready',
                'locations:', locations ? 'Ready' : 'Not Ready',
                'functions:', functions ? 'Ready' : 'Not Ready'
            );
            return;
        }

        const masks = profile.masks;
        if (!masks) {
            console.log(
                '{ProfileManagementDetailComponent}',
                '[buildMaskTable]',
                'masks: Not Ready'
            );
        }

        // initialize maskTable
        this.maskTable = [];
        for (let i = 0; i < locations.length; ++i) {
            this.maskTable[i] = [];
            for (let j = 0; j < functions.length; ++j) {
                this.maskTable[i][j] = undefined;
            }
        }

        // initialize pesudoMaskMap
        this.pesudoMaskToSourceMap = {};
        this.sourceToPesudoMaskMap = {};

        // populate maskTable with profile's masks
        for (let i = 0; i < masks.length; ++i) {
            const mask = masks[i];
            if (!mask) {
                console.error(
                    '{ProfileManagementDetailComponent}',
                    '[buildMaskTable]',
                    '[' + i + ']',
                    'Mask is null'
                );
                continue;
            }

            if (!mask.function) {
                console.error(
                    '{ProfileManagementDetailComponent}',
                    '[buildMaskTable]',
                    'Invalid mask content:',
                    'Mask:', mask,
                    'Function?', mask.function ? '[' +
                        mask.function.id + '/' + mask.function.name + '/' + mask.function.description +
                    ']' : 'NOK',
                    'Location?', mask.location ? '[' +
                        mask.location.id + '/' + mask.location.name + '/' + mask.location.description +
                    ']' : 'NOK'
                );
                continue;
            }

            if (!mask.location) {
                // empty location means profiles have rights for all locations
                const funcId = mask.function.id;
                this.locations.forEach(loc => {
                    const newMask = this.createPesudoMask(loc.id, funcId, mask);

                    const row = this.locCatToRowMap[loc.id];
                    const col = this.funCatToColMap[funcId];
                    if (undefined === row || undefined === col) {
                        console.error(
                            '{ProfileManagementDetailComponent}',
                            '[buildMaskTable]',
                            'Invalid Mask:', mask
                        );
                    } else {
                        this.maskTable[row][col] = newMask;
                    }
                });
                console.log(
                    '{ProfileManagementDetailComponent}',
                    '[buildMaskTable]',
                    'Function: [' + mask.function.id + '/' + mask.function.name + '/' + mask.function.description + ']',
                    'Generated pesudo masks for mask:', mask
                );
            } else {
                const row = this.locCatToRowMap[mask.location.id];
                const col = this.funCatToColMap[mask.function.id];
                if (undefined === row || undefined === col) {
                    console.error(
                        '{ProfileManagementDetailComponent}',
                        '[buildMaskTable]',
                        'Invalid Mask:', mask
                    );
                } else {
                    this.maskTable[row][col] = mask;
                }
            }
        }

        // create missing masks
        for (let i = 0; i < locations.length; ++i) {
            const locCat = this.rowToLocCatMap[i].id;
            for (let j = 0; j < functions.length; ++j) {
                if (undefined === this.maskTable[i][j]) {
                    const funCat = this.colToFunCatMap[j].id;
                    this.maskTable[i][j] = this.createNewMask(locCat, funCat);
                }
            }
        }

        console.log(
            '{ProfileManagementDetailComponent}',
            '[buildMaskTable]',
            'Completed'
        );
    }

    public getMaskTableItem(row: number, column: number): Mask {
        return undefined;
    }

    public getFunctionName(f: Function): string {
        return f[this.configService.config.getIn(['profile_management', 'profile_detail', 'function_name_field'])];
    }

    public getFunctionTooltip(f: Function): string {
        return f[this.configService.config.getIn(['profile_management', 'profile_detail', 'function_tooltip_field'])];
    }

    public getLocationName(l: Location): string {
        return l[this.configService.config.getIn(['profile_management', 'profile_detail', 'location_name_field'])];
    }

    public getLocationTooltip(l: Location): string {
        return l[this.configService.config.getIn(['profile_management', 'profile_detail', 'location_tooltip_field'])];
    }

    private getFunctions(): void {
        this.functionService.getAll().then(
            functions => {
                this.functions = functions || [];
                this.buildFuncCatMap(this.functions);
            },
            failure => {
                console.error(
                    '{ProfileManagementDetailComponent}',
                    '[getFunctions]',
                    'Failed:', failure
                );
            }
        );
    }
    private buildFuncCatMap(functions: Function[]): void {
        let colId = 0;
        this.funCatToColMap = {};
        this.colToFunCatMap = {};
        functions.forEach(fun => {
            this.funCatToColMap[fun.id] = colId;
            this.colToFunCatMap[colId] = fun;
            ++colId;
        });
        console.log(
            '{ProfileManagementDetailComponent}',
            '[buildFuncCatMap]',
            'Completed'
        );
        this.buildMaskTable();
    }

    private getLocations(): void {
        this.locationService.getAll().then(
            locations => {
                this.locations = locations || [];
                this.buildLocCatMap(this.locations);
            },
            failure => {
                console.error(
                    '{ProfileManagementDetailComponent}',
                    '[getLocations]',
                    'Failed:', failure
                );
            }
        );
    }
    private buildLocCatMap(locations: Location[]): void {
        let rowId = 0;
        this.locCatToRowMap = {};
        this.rowToLocCatMap = {};
        locations.forEach(loc => {
            this.locCatToRowMap[loc.id] = rowId;
            this.rowToLocCatMap[rowId] = loc;
            ++rowId;
        });
        console.log(
            '{ProfileManagementDetailComponent}',
            '[buildLocCatMap]',
            'Completed'
        );
        this.buildMaskTable();
    }

    private getActions(): void {
        this.actionService.getAll().then(
            actions => {
                this.actions = actions || [];
                this.maskValueSortTemplate = [];
                this.actions.forEach(a => this.maskValueSortTemplate.push(a.abbrev));
            },
            failure => {
                console.error(
                    '{ProfileManagementDetailComponent}',
                    '[getActions]',
                    'Failed:', failure
                );
            }
        );
    }
}
