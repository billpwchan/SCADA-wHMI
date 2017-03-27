import {Injectable} from '@angular/core'

import {Mask} from '../type/mask';
import {Profile} from '../type/profile';
import {Location} from '../type/location';
import {Function} from '../type/function';
import {Action} from '../type/action';

@Injectable()
export class UtilService{
    public compareProfile(p1: Profile, p2: Profile): number{
        if(p1.name < p2.name){return -1;}
        if(p1.name > p2.name){return 1;}
        if(p1.id < p2.id){return -1;}
        if(p1.id > p2.id){return 1;}
        return 0;
    }

    public cloneMaskWithProfileIdAndName(
        mask: Mask,
        profileId: number,
        profileName: string
    ): Mask{
        let clonedMask = new Mask(mask);
        return clonedMask;
    }
}

@Injectable()
export class StringMap<T>{
    [index: string]: T;
}

@Injectable()
export class NumberMap<T>{
    [index: string]: T;
}