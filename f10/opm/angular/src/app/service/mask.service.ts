import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Mask } from '../type/mask';
import { Profile } from '../type/profile';
import { ConfigService } from './config.service';

@Injectable()
export class MaskService {
    private static headers = new Headers({
        'Content-Type': 'application/json;charset=UTF-8'
    });
    private static maskValueIdMax = 4;
    private url = this.configService.config.getIn(['opmmgr_url']) + '/opm/masks';
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getAll(): Promise<Mask[]> {
        return this.http.get(this.url).toPromise().then(
            response => response.json() as Mask[]
        ).catch(this.handleError);
    }

    public get(id: number): Promise<Mask> {
        const url = `${this.url}/${id}`;
        return this.http.get(url).toPromise().then(
            response => response.json() as Mask
        ).catch(this.handleError);
    }

    public createForProfile(mask: Mask, profile: Profile): Promise<boolean> {
        const url = this.url;
        const tempMask = new Mask(mask);
        tempMask.profile = new Profile(profile);
        tempMask.profile.masks = [];
        const jsonMask = JSON.stringify(tempMask);
        return this.http.post(url, jsonMask, {headers: MaskService.headers}).toPromise().then(
            success => success.json() as boolean, failure => false
        ).catch(this.handleError);
    }

    public delete(mask: Mask): Promise<boolean> {
        const url = `${this.url}/${mask.id}`;
        return this.http.delete(url, {headers: MaskService.headers}).toPromise().then(
            success => true, failure => false
        ).catch(this.handleError);
    }

    public updateForProfile(mask: Mask, profile: Profile): Promise<boolean> {
        const url = `${this.url}/${mask.id}`;
        const tempMask = new Mask(mask);
        tempMask.profile = new Profile(profile);
        tempMask.profile.masks = [];
        const jsonMask = JSON.stringify(tempMask);
        return this.http.put(url, jsonMask, {headers: MaskService.headers}).toPromise().then(
            success => success.json() as boolean, failure => false
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('{MaskService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }

    // common mask functions
    public getMaskValueById(mask: Mask, id: number): string {
        if (0 >= id || MaskService.maskValueIdMax < id) {
            console.error(
                '{MaskService}',
                '[getMaskValueById]',
                'Invalid id:', id,
                'mask:', mask
            );
            return undefined;
        }
        return mask['mask' + id];
    }
    public setMaskValueById(mask: Mask, id: number, value: string): boolean {
        if (0 >= id || MaskService.maskValueIdMax < id) {
            console.error(
                '{MaskService}',
                '[setMaskValueById]',
                'Invalid id:', id,
                'mask:', mask,
                'value:', value
            );
            return false;
        }
        mask['mask' + id] = value;
        return true;
    }
    public setMaskValues(mask: Mask, value: string): void {
        for (let i = 1; i <= MaskService.maskValueIdMax; ++i) {
            this.setMaskValueById(mask, i, '');
        }
    }
    public addMaskValueById(mask: Mask, id: number, value: string, sortTemplate: string[] = undefined): boolean {
        let maskValue = this.getMaskValueById(mask, id);
        if (undefined === maskValue) { return false; }
        if (0 > maskValue.indexOf(value)) {
            maskValue += value;
            return this.setMaskValueById(mask, id, maskValue) &&
                   this.sortMaskValueById(mask, id, sortTemplate);
        }
        return true;
    }
    public removeMaskValueById(mask: Mask, id: number, value: string): boolean {
        let maskValue = this.getMaskValueById(mask, id);
        if (undefined === maskValue) { return false; }
        const pos = maskValue.indexOf(value);
        if (0 <= pos) {
            maskValue = maskValue.replace(value, '');
            return this.setMaskValueById(mask, id, maskValue);
        }
        return true;
    }
    public copyMaskValues(src: Mask, dest: Mask): void {
        for (let i = 1; i <= MaskService.maskValueIdMax; ++i) {
            this.setMaskValueById(dest, i, this.getMaskValueById(src, i));
        }
    }
    public isMaskValueSetById(mask: Mask, id: number, value: string): boolean {
        const maskValue = this.getMaskValueById(mask, id);
        if (undefined === maskValue) { return false; }
        return 0 <= maskValue.indexOf(value);
    }
    public isSameMaskValues(mask1: Mask, mask2: Mask): boolean {
        for (let i = 1; i <= MaskService.maskValueIdMax; ++i) {
            if (this.getMaskValueById(mask1, i) !== this.getMaskValueById(mask2, i)) {
                return false;
            }
        }
        return true;
    }
    public sortMaskValueById(mask: Mask, id: number, sortTemplate: string[] = undefined): boolean {
        if (!sortTemplate) { return true; }
        const maskValue = this.getMaskValueById(mask, id);
        if (!maskValue) { return false; }
        let newMaskValue = '';
        sortTemplate.forEach(a => {
            if (0 <= maskValue.indexOf(a)) {
                newMaskValue += a;
            } // else... action not it maskValue, no need to add
        });
        this.setMaskValueById(mask, id, newMaskValue);
        return true;
    }
    public sortMaskValues(mask: Mask, sortTemplate: string[] = undefined): void {
        for (let i = 1; i <= MaskService.maskValueIdMax; ++i) {
            this.sortMaskValueById(mask, i, sortTemplate);
        }
    }
}
