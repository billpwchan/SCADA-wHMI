import { Schedule } from './schedule';

export class ScheduleItem {
    key: string;            // primary key to search scheduleItem
    scheduleKey: string;    // primary key to search schedule
    scheduleType: string;   // from Name
    scheduleId: number;     // from Name
    eqtLabel: string;       // from Name
    eqtAlias: string;       // from Name
    eqtPointAtt: string;    // from Name
    eqtDescription: string; // from Description
    geoCat: number;         // from Name
    funcCat: number;        // from Name
    onTime: string;         // from Filter
    offTime: string;        // from Filter
    daygroup: string;       // from Filter   
    taskName1: string;      // for writing back to tsc
    taskName2: string;      // for writing back to tsc
    filter1: string;        // for writing back to tsc
    filter2: string;        // for writing back to tsc
    enableFlag1: number;    // from Inhibition Flag
    enableFlag2: number;    // from Inhibition Flag
}