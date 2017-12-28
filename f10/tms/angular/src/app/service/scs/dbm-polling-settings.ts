import { Subscription } from 'rxjs/Subscription';

export class DbmPollingSettings {

    public static readonly STR_INTERVAL = 'interval';
    public static readonly STR_USE_COMPUTED_MESSAGE = 'use_computed_message';
}

export class DbmNotify {
    constructor(
        public connAddr: string
        , public key: string
    ) {}
}

export class EqpSubInfo {
    public classId: number;
    public unviname: string;
}

export class SubInfo {
    public timer: Subscription;
    public eqpSubInfo: EqpSubInfo[];
}

export enum DbmPollingServiceType {

}
