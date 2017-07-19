export class Schedule {
    id: string;             // require field for object in ng2-select
    scheduleType: string;   // e.g. oneshot, predefined, userdefined
    scheduleId: number;     // e.g. 1 - 8
    scheduleDescription: string;    // e.g. oneshot, predefined, userdefined
    text: string;           // required field for object in ng2-select, for title e.g. weekday
    runningStatus: string;  // e.g. started, stopped
    visibility: string;     // e.g. visible, invisible
    periodic: boolean;      // e.g. true or false
    titleReadOnly: boolean;
    timeReadOnly: boolean;
    eqtListReadOnly: boolean;
    taskName: string;       // for writing to tsc
    public toString(): string {
        const str = this.scheduleDescription + ',' +
                this.text + ',' +
                this.runningStatus + ',' +
                this.visibility + ',' +
                (this.periodic ? 'true' : 'false') + ',' +
                (this.titleReadOnly ? 'true' : 'false') + ',' +
                (this.timeReadOnly ? 'true' : 'false') + ',' +
                (this.eqtListReadOnly ? 'true' : 'false');
        //console.log('{Schedule}', 'toString', str);
        return str;
    }
}
