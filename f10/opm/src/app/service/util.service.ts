import { Injectable } from '@angular/core';

@Injectable()
export class StringMap<T> {
    [index: string]: T;
}

@Injectable()
export class NumberMap<T> {
    [index: number]: T;
}
