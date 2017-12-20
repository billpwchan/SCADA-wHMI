export class AvaCond {
    id: number;
    name: string;
    isEnabled: number;
    isTriggered: number;
    triggerCondList: Array<AvaTriggerCond>;
    suppressAlarmList: Array<SuppressAlarm>;

    constructor() {
        this.id = 0;
        this.name = '';
        this.isEnabled = 0;
        this.isTriggered = 0;
        this.triggerCondList = [];
        this.suppressAlarmList = [];
    }
}

export class AvaTriggerCond {
    id: number;
    geoCat: string;
    funcCat: string;
    equipment: string;
    point: string;
    pointStatus: any;
}

export class SuppressAlarm {
    geoCat: string;
    funcCat: string;
    severity: number;
    userField1: any;
    userField2: any;
}
