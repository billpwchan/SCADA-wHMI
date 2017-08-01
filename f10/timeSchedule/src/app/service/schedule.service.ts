import { Injectable, OnDestroy } from '@angular/core';
import 'rxjs/add/operator/toPromise';
import { Observable, BehaviorSubject } from 'rxjs/Rx';
import { ScsTscService } from './scs-tsc.service';
import { ConfigService } from '../service/config.service';
import { Schedule } from '../type/schedule';
import { ScheduleItem } from '../type/schedule-item';
import { ScheduleDef } from './schedule-def';
import { DayGroup } from '../type/daygroup';
import { UtilService } from '../service/util.service';
@Injectable()
export class ScheduleService implements OnDestroy {
    // schedule task cut off time
    private cutoffTime: string;
    // display offset in on/off time
    private displayOffset = true;
    // array to store all schedule tables
    private schedules: Schedule[] = [];

    // array to store all schedule tasks
    private scheduleItems: ScheduleItem[] = [];

    // array to store running schedules
    private runningSchedules: Schedule[];

    private weeklySchedules: Schedule[];

    private scheduleKeyMap = new Map<string, Schedule>();
    private dayGroupConfig: any;

    private dayGroupIdMap = new Map<string, DayGroup>();
    private dayGroupCnt = 0;
    private dayGroupReadyCnt = 0;

    private currentIsPeriodic = false;
    private unavailableOnOffTime: string;
    private inhibitedOnOffTime: string;

    private clientName: string;

    private subjSchedulesByPeriodic = new BehaviorSubject(new Array<Schedule>());
    private subjScheduleItemsByPeriodic = new BehaviorSubject(new Array<ScheduleItem>());
    private subjPeriodicSchedules = new BehaviorSubject(new Array<Schedule>());
    private subjWeeklySchedules = new BehaviorSubject(new Array<Schedule>());

    private oneshotStarted = false;
    private periodicStarted = false;

    constructor(
        private configService: ConfigService, private scsTscService: ScsTscService
    ) { }
    ngOnDestroy() {
    }
    public load() {
        this.loadConfig();
        this.loadData();
    }
    private loadConfig() {
        this.cutoffTime = this.configService.config.getIn(['schedule_table', 'cutoff_time']);
        console.log('{schedule-table}', '[loadConfig]', 'cutoff_time=', this.cutoffTime);

        this.displayOffset = this.configService.config.getIn(['schedule_table', 'show_cutoff_offset']);
        console.log('{schedule-table}', '[loadConfig]', 'show_cutoff_offset=', this.displayOffset);

        this.unavailableOnOffTime = this.configService.config.getIn(['schedule_table', 'unavailable_on_off_time']);
        console.log('{schedule-table}', '[loadConfig]', 'unavailable_on_off_time=', this.unavailableOnOffTime);

        this.inhibitedOnOffTime = this.configService.config.getIn(['schedule_table', 'inhibited_on_off_time']);
        console.log('{schedule-table}', '[loadConfig]', 'inhibited_on_off_time=', this.inhibitedOnOffTime);

        this.dayGroupConfig = this.configService.config.getIn(['schedule_table', 'schedule_daygroup']);
        console.log('{schedule-table}', '[loadConfig]', 'schedule_daygroup=', this.dayGroupConfig);
    }
    public loadData() {
        this.readTscTasks();
        this.readDayGroups();
    }
    private readTscTasks() {
        console.log('{ScheduleService}', '[readTscTasks]', 'begin');
        const subGetTaskNames = this.scsTscService.getTaskNames().subscribe(
            (taskNames: any[]) => {
                console.log('{ScheduleService}', '[readTscTasks]', taskNames);
                this.extractScheduleTasks(taskNames);
                const subGetDescFilterEnable = this.scsTscService.getDescFilterEnable(taskNames).subscribe(
                    (data: any[]) => {
                        this.extractDescFilterEnable(taskNames, data);

                        console.log('{ScheduleService}', '[readTscTasks]', 'currentIsPeriodic', this.currentIsPeriodic);
                        this.updateSchedulesByPeriodic(this.currentIsPeriodic);
                        this.updateScheduleItemsByPeriodic(this.currentIsPeriodic);

                        console.log('{ScheduleService}', '[readTscTasks]', 'end');
                        subGetDescFilterEnable.unsubscribe();
                    }
                )

                subGetTaskNames.unsubscribe();
            }
        )
    }
    private extractScheduleTasks(taskNames: string[]) {
        console.log('{ScheduleService}', '[extractScheduleTasks]', 'begin', 'taskName count=', taskNames.length);
        for (let i = 0; i < taskNames.length; i++) {
            const columns: string[] = taskNames[i].split(',');
            const header = columns[ScheduleDef.SCHEDULE_HEADER_COL];
            if (header != null && header === ScheduleDef.SCHEDULE_TABLE_HEADER) {
                this.extractScheduleTable(columns);
            } else if (header != null && header === ScheduleDef.SCHEDULE_TASK_HEADER) {
                this.extractScheduleTask(taskNames[i], columns);
            } else {
                console.error('{ScheduleService}', '[extractScheduleTasks]', 'unrecognized header in taskname', taskNames[i]);
            }
        }
        console.log('{ScheduleService}', '[extractScheduleTasks]', 'end');
    }
    private extractScheduleTable(columns: string[]) {
        console.log('{ScheduleService}', '[extractScheduleTable]', 'begin');
        const scheduleType = columns[ScheduleDef.SCHEDULE_TYPE_COL];
        const scheduleId = +columns[ScheduleDef.SCHEDULE_ID_COL];
        let sch = this.schedules.find( r =>
            r.scheduleType === scheduleType &&
            r.scheduleId === scheduleId);
        if (!sch) {
            sch = new Schedule();
            sch.scheduleType = scheduleType;
            sch.scheduleId = scheduleId;
            sch.id = scheduleType + scheduleId;
            this.schedules.push(sch);
            this.scheduleKeyMap.set(sch.id, sch);
            console.log('{ScheduleService}', '[extractScheduleTable]', 'new schedule table created.');
        } else {
            console.warn('{ScheduleService}', '[extractScheduleTable]', 'schedule table already exists.');
        }
        console.log('{ScheduleService}', '[extractScheduleTable]', 'end');
    }
    private extractScheduleTask(taskName: string, columns: string[]) {
        console.log('{ScheduleService}', '[extractScheduleTask]', 'begin');
        for (let i = 0; i < columns.length; i++) {
            // extract fields from task name
            const scheduleType = columns[ScheduleDef.SCHEDULE_TYPE_COL];
            const scheduleId = +columns[ScheduleDef.SCHEDULE_ID_COL];
            const eqtAlias = columns[ScheduleDef.SCHEDULE_EQT_ALIAS_COL];
            const eqtPointAtt = columns[ScheduleDef.SCHEDULE_EQT_POINT_ATT_COL];
            const targetState = columns[ScheduleDef.SCHEDULE_EQT_TARGET_STATE];
            const rowFound = this.scheduleItems.find( r =>
                r.scheduleType === scheduleType &&
                r.scheduleId === scheduleId &&
                r.eqtAlias === eqtAlias &&
                r.eqtPointAtt === eqtPointAtt);
            if (!rowFound) {
                const key = this.scheduleItems.length;
                const eqtLabel = columns[ScheduleDef.SCHEDULE_EQT_LABEL_COL];
                const funcCat = +columns[ScheduleDef.SCHEDULE_FUNC_COL];
                const geoCat = +columns[ScheduleDef.SCHEDULE_GEO_COL];
                const row = this.createScheduleItem(key, scheduleType, scheduleId, eqtLabel, eqtAlias, eqtPointAtt,
                                                    funcCat, geoCat, targetState, taskName);
                this.scheduleItems.push(row);
                console.log('{ScheduleService}', '[extractScheduleTask]', 'new schedule item created.');
            } else {
                if (targetState === 'OFF') {
                    rowFound.taskName2 = taskName;
                } else {
                    rowFound.taskName1 = taskName;
                }
            }
        }
        console.log('{ScheduleService}', '[extractScheduleTask]', 'end');
    }
    private createScheduleItem(key: number, scheduleType: string, scheduleId: number, eqtLabel: string, eqtAlias: string,
                                    eqtPointAtt: string, funcCat: number, geoCat: number, targetState: string, taskName: string): ScheduleItem {
        console.log('{ScheduleService}', '[createScheduleItem]', 'begin');
        const t = new ScheduleItem();
        t.scheduleType = scheduleType;
        t.scheduleId = scheduleId;
        t.scheduleKey = scheduleType + scheduleId;
        t.eqtLabel = eqtLabel;
        t.eqtDescription = '';
        t.eqtAlias = eqtAlias;
        t.eqtPointAtt = eqtPointAtt;
        t.funcCat = funcCat;
        t.geoCat = geoCat;
        t.onTime = this.unavailableOnOffTime;
        t.offTime = this.unavailableOnOffTime;
        t.onTimeDisplay = this.unavailableOnOffTime;
        t.offTimeDisplay = this.unavailableOnOffTime;
        t.enableFlag1 = 0;
        t.enableFlag2 = 0;
        if (targetState === 'OFF') {
            t.taskName2 = taskName;
        } else {
            t.taskName1 = taskName;
        }
        return t;
    }
    private extractDescFilterEnable(taskNames: string[], data: any[]) {
        console.log('{ScheduleService}', '[extractDescFilterEnable]', 'begin');
        for (let i = 0; i < taskNames.length; i++) {
            const taskColumns: string[] = taskNames[i].split(',');
            const header = taskColumns[ScheduleDef.SCHEDULE_HEADER_COL];
            const scheduleType = taskColumns[ScheduleDef.SCHEDULE_TYPE_COL];
            const scheduleId = +taskColumns[ScheduleDef.SCHEDULE_ID_COL];
            if (header != null && header === ScheduleDef.SCHEDULE_TABLE_HEADER) {
//                console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule table process ', taskNames[i]);
                const schedule = this.schedules.find(s =>
                    s.scheduleType === scheduleType &&
                    s.scheduleId === scheduleId);
                if (schedule != null) {
                    const schTableData: string = data[3 * i];
                    const dataColumns = schTableData.split(',');
                    const scheduleDescription = dataColumns[ScheduleDef.SCHEDULE_TABLE_TYPE_DESC_COL];
                    const scheduleTitle = dataColumns[ScheduleDef.SCHEDULE_TABLE_TITLE_COL];
                    const runningStatus = dataColumns[ScheduleDef.SCHEDULE_RUNNING_STATUE_COL];
                    const visibility = dataColumns[ScheduleDef.SCHEDULE_VISIBILITY_COL];
                    const periodic = dataColumns[ScheduleDef.SCHEDULE_PERIODIC_COL];
                    const titleReadOnly = dataColumns[ScheduleDef.SCHEDULE_TITLE_READ_ONLY_COL];
                    const timeReadOnly = dataColumns[ScheduleDef.SCHEDULE_TIME_READ_ONLY_COL];
                    const eqtListReadOnly = dataColumns[ScheduleDef.SCHEDULE_EQT_LIST_READ_ONLY_COL];
                    schedule.scheduleDescription = scheduleDescription;
                    schedule.text = scheduleTitle;
                    schedule.runningStatus = runningStatus;
                    schedule.visibility = visibility;
                    schedule.periodic = (periodic === 'true');
                    schedule.titleReadOnly = (titleReadOnly === 'true');
                    schedule.timeReadOnly = (timeReadOnly === 'true');
                    schedule.eqtListReadOnly = (eqtListReadOnly === 'true');
                    schedule.taskName = taskNames[i];
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule description updated',  schedule.scheduleDescription);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule title updated ',  schedule.text);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule runningStatus updated ',  schedule.runningStatus);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule periodic updated ', schedule.periodic);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule titleReadOnly updated ', schedule.titleReadOnly);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule timeReadOnly updated ', schedule.timeReadOnly);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule eqtListReadOnly updated ', schedule.eqtListReadOnly);
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule table updated ');
                } else {
                    console.warn('{ScheduleService}', '[extractDescFilterEnable]', ' schedule table not found ');
                }
            } else if (header != null && header === ScheduleDef.SCHEDULE_TASK_HEADER) {
//                console.log('{ScheduleService}', '[extractDescFilterEnable]', ' schedule task process ', taskNames[i]);
                const alias = taskColumns[ScheduleDef.SCHEDULE_EQT_ALIAS_COL];
                const pointAtt = taskColumns[ScheduleDef.SCHEDULE_EQT_POINT_ATT_COL];
                const targetState = taskColumns[ScheduleDef.SCHEDULE_EQT_TARGET_STATE];
                const filter = data[3 * i + 1];
                const enableFlag = +data[3 * i + 2];
                const scheduleItem = this.scheduleItems.find( r =>
                    r.scheduleType === scheduleType &&
                    r.scheduleId === scheduleId &&
                    r.eqtAlias === alias &&
                    r.eqtPointAtt === pointAtt);
                if (scheduleItem != null) {
                    scheduleItem.eqtDescription = data[3 * i];
                    const onOffTime = this.getOnOffTime(filter);
                    const onOffTimeDisplay = onOffTime;

                    if (targetState === 'ON') {
                        scheduleItem.onTime = onOffTime;
                        scheduleItem.onTimeDisplay = onOffTimeDisplay;
                        scheduleItem.filter1 = filter;
                        scheduleItem.enableFlag1 = enableFlag;
                    } else if (targetState === 'OFF') {
                        scheduleItem.offTime = onOffTime;
                        scheduleItem.offTimeDisplay = onOffTimeDisplay;
                        scheduleItem.filter2 = filter;
                        scheduleItem.enableFlag2 = enableFlag;
                    }
                    console.log('{ScheduleService}', '[extractDescFilterEnable]', 'update scheduleItem description, filter, enableFlag ',
                                scheduleItem.eqtDescription, filter, enableFlag);
                } else {
                    console.warn('{ScheduleService}', '[extractDescFilterEnable]', 'scheduleItem not found ', scheduleType, scheduleId, alias, pointAtt);
                }
            } else {
                console.error('{ScheduleService}', '[extractDescFilterEnable]', 'unrecognized header in taskname', taskNames[i]);
            }
        }
        console.log('{ScheduleService}', '[extractDescFilterEnable]', 'end');
    }
    private getOnOffTime(filter: string) {
        let dg_time: string[];
        dg_time = filter.split(' ');
        let hh: string = dg_time[4];
        let mm: string = dg_time[5];
        const hr: number = +hh;
        const min: number = +mm;
        let hhmm: string;
        if (hh.length === 1) {
            hh = '0' + hh;
        }
        if (mm.length === 1) {
            mm = '0' + mm;
        }
        hhmm = hh + ':' + mm;

        console.log('{schedule-table}', '[getOnOffTime]', 'hhmm=', hhmm);
        return hhmm;
    }

    private getPeriodicOnOffTimeDisplay(filter: string) {
        let dg_time: string[];
        dg_time = filter.split(' ');
        const dg: string = dg_time[0];
        let hh: string = dg_time[4];
        let mm: string = dg_time[5];
        const hr: number = +hh;
        const min: number = +mm;
        let hhmm: string;

        if (hh.length === 1) {
            hh = '0' + hh;
        }
        if (mm.length === 1) {
            mm = '0' + mm;
        }
        if (this.showNextDayOffset(hr, min)) {
            hhmm = (hr + 24) + ':' + mm;
        } else {
            hhmm = hh + ':' + mm;
        }
        console.log('{schedule-table}', '[getPeridoicOnOffTimeDisplay]', 'hhmm=', hhmm);
        return hhmm;
    }
    private updateOneshotOnOffTimeDisplay(scheduleItem: ScheduleItem) {
        if (scheduleItem.enableFlag1 === 0) {
            scheduleItem.onTimeDisplay = this.inhibitedOnOffTime;
        } else {
            const scheduleRunDaygroup = this.getRunDayGroupId(scheduleItem.scheduleKey);
            // Change onTimeDisplay to blank if time has passed
            if (scheduleItem.onTime !== this.unavailableOnOffTime && scheduleItem.onTime.length > 0) {
                const hh1 = +scheduleItem.onTime.split(':')[0];
                const mm1 = +scheduleItem.onTime.split(':')[1];
                const taskDaygroup = scheduleItem.filter1.split(' ')[0];
                console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'onTime', scheduleItem.onTime,
                    'hour', hh1, 'minute', mm1, 'schedule daygroup', scheduleRunDaygroup, 'taskDaygroup', taskDaygroup);
                if (+taskDaygroup === +scheduleRunDaygroup && UtilService.isTimeExpired(hh1, mm1)) {
                    scheduleItem.onTimeDisplay = '';
                    scheduleItem.onTime = '';
                }
            }
        }

        if (scheduleItem.enableFlag2 === 0) {
            scheduleItem.offTimeDisplay = this.inhibitedOnOffTime;
        } else {
            const scheduleRunDaygroup = this.getRunDayGroupId(scheduleItem.scheduleKey);
            // Change offTimeDisplay to blank if time has passed
            if (scheduleItem.offTime !== this.unavailableOnOffTime && scheduleItem.offTime.length > 0) {
                const hh2 = +scheduleItem.offTime.split(':')[0];
                const mm2 = +scheduleItem.offTime.split(':')[1];
                const taskDaygroup = scheduleItem.filter2.split(' ')[0];
                console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'offTime', scheduleItem.offTime,
                    'hour', hh2, 'minute', mm2, 'schedule daygroup', scheduleRunDaygroup, 'taskDaygroup', taskDaygroup);
                if (+taskDaygroup === +scheduleRunDaygroup && UtilService.isTimeExpired(hh2, mm2)) {
                    scheduleItem.offTimeDisplay = '';
                    scheduleItem.offTime = '';
                }
            }
        }
    }
    public getSchedulesByPeriodic(isPeriodic: boolean): Observable<Schedule[]> {
        console.log('{ScheduleService}', '[getSchedulesByPeriodic]', 'isPeriodic', isPeriodic);
        this.currentIsPeriodic = isPeriodic;

        this.updateSchedulesByPeriodic(isPeriodic);
        return this.subjSchedulesByPeriodic;
    }
    public getScheduleItemsByPeriodic(isPeriodic: boolean): Observable<ScheduleItem[]> {
        console.log('{ScheduleService}', '[getScheduleItemsByPeriodic]', 'isPeriodic', isPeriodic);

        this.updateScheduleItemsByPeriodic(isPeriodic);
        return this.subjScheduleItemsByPeriodic;
    }
    public updateSchedulesByPeriodic(isPeriodic: boolean) {
        console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'isPeriodic', isPeriodic);
        if (this.schedules) {
            console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'schedules count=', this.schedules.length, );
        } else {
            console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'schedules not defined');
        }

        const schedulesByPeriodic = new Array<Schedule>();
        for (const s of this.schedules) {
            console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'compare periodic', isPeriodic, s.periodic);
            if (s.visibility === ScheduleDef.VISIBLE) {
                if ((isPeriodic && s.periodic) || (!isPeriodic && !s.periodic)) {
                    schedulesByPeriodic.push(s);
                    if (s.runningStatus === ScheduleDef.STARTED) {
                        if (isPeriodic) {
                            this.periodicStarted = true;
                        } else {
                            this.oneshotStarted = true;
                        }
                    }
                    console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', '*** schedule is pushed to schedulesByPeriodic');
                }
            }
        }
        this.subjSchedulesByPeriodic.next(schedulesByPeriodic);
    }
    public updateScheduleItemsByPeriodic(isPeriodic: boolean) {
        console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'isPeriodic', isPeriodic);
        if (this.scheduleItems) {
            console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'scheduleItems count=', this.scheduleItems.length);
        } else {
            console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'scheduleItems not defined');
        }

        const scheduleItemsByPeriodic = new Array<ScheduleItem>();
        for (const s of this.scheduleItems) {
            const speriodic: boolean = this.scheduleKeyMap.get(s.scheduleKey).periodic;
            const svisible: boolean = this.scheduleKeyMap.get(s.scheduleKey).visibility === ScheduleDef.VISIBLE;
            console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'compare periodic', isPeriodic, speriodic);

            if (svisible) {
                if ((isPeriodic && speriodic) || (!isPeriodic && !speriodic)) {
                    if (isPeriodic) {
                        if (s.enableFlag1 === 0) {
                            console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag1 is 0');
                            s.onTimeDisplay = this.inhibitedOnOffTime;
                        } else {
                            console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag1 is 1');
                            s.onTimeDisplay = this.getPeriodicOnOffTimeDisplay(s.filter1);
                        }
                        if (s.enableFlag2 === 0) {
                            console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag2 is 0');
                            s.offTimeDisplay = this.inhibitedOnOffTime;
                        } else {
                            console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag2 is 1');
                            s.offTimeDisplay = this.getPeriodicOnOffTimeDisplay(s.filter2);
                        }
                    } else {
                        this.updateOneshotOnOffTimeDisplay(s);
                    }
                    scheduleItemsByPeriodic.push(s);
                    console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', '*** scheduleItem is pushed to scheduleItemsByPeriodic');
                }
            }
        }
        this.subjScheduleItemsByPeriodic.next(scheduleItemsByPeriodic);
    }
    public setClientName(clientName: string) {
        this.clientName = clientName;
    }
    public setFilter(taskName: string, filter: string) {
        const subSetFilter = this.scsTscService.setFilter(taskName, filter, this.clientName).subscribe(res => {
            console.log('{ScheduleService}', '[setFilter]', res);
            this.loadData();
            subSetFilter.unsubscribe();
        });
    }
    private showNextDayOffset(hr, min) {
        console.log('{ScheduleService}', '[showNextDayOffset]', 'begin');
        if (this.displayOffset) {
            const cutoffHr = +this.cutoffTime.split(':')[0];
            const cutoffMin = +this.cutoffTime.split(':')[1];
            if (hr < cutoffHr || (hr === cutoffHr && min < cutoffMin)) {
                return true;
            }
        }
        return false;
    }
    public setScheduleTitle(id: string, scheduleTitle: string) {
        console.log('{ScheduleService}', '[setScheduleTitle]', id, scheduleTitle);
        const schedule = this.schedules.find(s => s.id === id);
        if (schedule) {
            const desc = schedule.toString();
            const taskName = schedule.taskName;
            schedule.text = scheduleTitle;
            const subSetTitle = this.scsTscService.setDescription(taskName, desc, this.clientName).subscribe(res => {
                console.log('{ScheduleService}', '[setScheduleTitle]', res);
                subSetTitle.unsubscribe();
            });
        } else {
            console.warn('{ScheduleService}', '[setScheduleTitle]', 'schedule not found', id);
        }
    }
    public enableTask(taskName: string) {
        const subEnableTask = this.scsTscService.enableTask(taskName, 1, this.clientName).subscribe(res => {
            console.log('{ScheduleService}', '[enableTask]', res);
            subEnableTask.unsubscribe();
        });
    }
    public disableTask(taskName: string) {
        const subDisableTask = this.scsTscService.enableTask(taskName, 0, this.clientName).subscribe(res => {
            console.log('{ScheduleService}', '[disableTask]', res);
            subDisableTask.unsubscribe();
        });
    }

    public startOneshotSchedule(scheduleKey) {
        const runDayGroupId = this.getRunDayGroupId(scheduleKey);
        const runNextDayGroupId = this.getRunNextDayGroupId(scheduleKey);

        const d = new Date();
        console.log('{ScheduleService}', '[startOneshotSchedule]', 'current Date', d);
        const dd = new Date(d.getFullYear(), d.getMonth(), d.getDate());
        console.log('{ScheduleService}', '[startOneshotSchedule]', 'current Date', dd, dd.getTime() / 1000);

        const curDateList = new Array<string>();
        // convert from milli-sec to seconds
        curDateList.push((dd.getTime() / 1000).toString());
        console.log('{ScheduleService}', '[startOneshotSchedule]', 'current Date', d.getFullYear(), d.getMonth() + 1, d.getDate());

        const nextDateList = new Array<string>();
        const nn = new Date(dd.getTime() + 86400000);
        nextDateList.push((nn.getTime() / 1000).toString());

        // Set dateList to running daygroup to implement start schedule
        const subDates = this.scsTscService.setDates(runDayGroupId, curDateList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[startOneshotSchedule]', 'setDates', res);

                // Reload day groups to update running schedules
                this.readDayGroups();
                subDates.unsubscribe();
            }
        );
        const subDates2 = this.scsTscService.setDates(runNextDayGroupId, nextDateList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[startOneshotSchedule]', 'setDates', res);

                // Update running status in schedule task description
                const schedule = this.scheduleKeyMap.get(scheduleKey);
                if (schedule) {
                    schedule.runningStatus = ScheduleDef.STARTED;
                    const taskDesc = schedule.toString();
                    const subSetScheduleStart = this.scsTscService.setDescription(schedule.taskName, taskDesc, this.clientName).subscribe(
                        res2 => {
                            console.log('{ScheduleService}', '[startOneshotSchedule]', 'setDates', res2);
                            subSetScheduleStart.unsubscribe();
                        }
                    );
                }
                subDates2.unsubscribe();
            }
        );
    }
    public startPeriodicSchedules() {
        for (const s of this.schedules) {
            if (s.periodic && this.isScheduleVisible(s.id)) {
                // Get planning daygroup id for this schedule from config
                const planDayGroupId = this.getPlanDayGroupId(s.id);
                const planNextDayGroupId = this.getPlanNextDayGroupId(s.id);
                const runDayGroupId = this.getRunDayGroupId(s.id);
                const runNextDayGroupId = this.getRunNextDayGroupId(s.id);

                // Get current dateList from planning daygroup
                if (planDayGroupId && runDayGroupId) {
                    const subGetDates = this.scsTscService.getDates(planDayGroupId).subscribe(
                        datesList => {
                            console.log('{ScheduleService}', '[startPeriodicSchedules]', 'getDates', datesList);

                            // Set dateList to running daygroup to implement start schedule
                            const subSetDates = this.scsTscService.setDates(runDayGroupId, datesList, this.clientName).subscribe(
                                res => {
                                    console.log('{ScheduleService}', '[startPeriodicSchedules]', 'setDates', res);

                                    // Reload day groups to update running schedules
                                    this.readDayGroups();
                                    subSetDates.unsubscribe();
                                }
                            );
                            subGetDates.unsubscribe();
                        }
                    );
                } else {
                    console.error('{ScheduleService}', '[startPeriodicSchedules]', 'planDayGroupId/runDayGroupId not found in config')
                }

                // Get next dateList from planning daygroup
                if (planNextDayGroupId && runNextDayGroupId) {
                    const subGetDates = this.scsTscService.getDates(planNextDayGroupId).subscribe(
                        datesList => {
                            console.log('{ScheduleService}', '[startPeriodicSchedules]', 'getDates', datesList);

                            // Set dateList to running daygroup to implement start schedule
                            const subSetDates = this.scsTscService.setDates(runNextDayGroupId, datesList, this.clientName).subscribe(
                                res => {
                                    console.log('{ScheduleService}', '[startPeriodicSchedules]', 'setDates', res);

                                    // Update running status in schedule task description
                                    const schedule = this.scheduleKeyMap.get(s.id);
                                    if (schedule) {
                                        schedule.runningStatus = ScheduleDef.STARTED;
                                        const taskDesc = schedule.toString();
                                        const subSetScheduleStart = this.scsTscService.setDescription(schedule.taskName, taskDesc, this.clientName).subscribe(
                                            res2 => {
                                                console.log('{ScheduleService}', '[startPeriodicSchedules]', 'setDates', res2);
                                                subSetScheduleStart.unsubscribe();
                                            }
                                        );
                                    }
                                    subSetDates.unsubscribe();
                                }
                            );
                            subGetDates.unsubscribe();
                        }
                    );
                } else {
                    console.error('{ScheduleService}', '[startPeriodicSchedules]', 'planNextDayGroupId/runNextDayGroupId not found in config')
                }
            }
        }
    }

    public stopPeriodicSchedules() {
        for (const s of this.schedules) {
            if (s.periodic) {
                this.stopSchedule(s.id);
            }
        }
    }

    public stopSchedule(scheduleKey) {
        const runDayGroupId = this.getRunDayGroupId(scheduleKey);
        const runNextDayGroupId = this.getRunNextDayGroupId(scheduleKey);

        // Set empty dateList to running daygroup to implement stop schedule
        const datesList = new Array<string>();
        const subSetDates = this.scsTscService.setDates(runDayGroupId, datesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[stopSchedule]', 'setDates', res);

                // Reload day groups to update running schedules
                this.readDayGroups();

                subSetDates.unsubscribe();
            }
        );
        const subSetDates2 = this.scsTscService.setDates(runNextDayGroupId, datesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[stopSchedule]', 'setDates', res);

                subSetDates2.unsubscribe();
            }
        );

        // Update running status in schedule task description
        const schedule = this.scheduleKeyMap.get(scheduleKey);
        if (schedule) {
            schedule.runningStatus = ScheduleDef.STOPPED;
            const taskDesc = schedule.toString();
            const subSetScheduleStop = this.scsTscService.setDescription(schedule.taskName, taskDesc, this.clientName).subscribe(
                res => {
                    console.log('{ScheduleService}', '[stopSchedule]', 'setDates', res);

                    subSetScheduleStop.unsubscribe();
                }
            );
        }
    }

    public getDayGroupId(scheduleKey, dayGroupType) {
        const schedule = this.scheduleKeyMap.get(scheduleKey);
        if (schedule && this.dayGroupConfig) {
            console.log('{ScheduleService}', '[getDayGroupId]', 'scheduleKey', scheduleKey, 'dayGroupType', dayGroupType, this.dayGroupConfig);

            if (this.dayGroupConfig.get(schedule.scheduleType)) {
                if (this.dayGroupConfig.get(schedule.scheduleType).get(schedule.scheduleId.toString())) {
                    if (this.dayGroupConfig.get(schedule.scheduleType)) {
                        return this.dayGroupConfig.get(schedule.scheduleType).get(schedule.scheduleId.toString()).get(dayGroupType);
                    } else {
                        console.log('{ScheduleService}', '[getDayGroupId]', 'this.dayGroupConfig.scheduleType.schedule.dayGroupType is null');
                    }
                } else {
                    console.log('{ScheduleService}', '[getDayGroupId]', 'this.dayGroupConfig.scheduleType.schedule is null');
                }
            } else {
                console.log('{ScheduleService}', '[getDayGroupId]', 'this.dayGroupConfig.scheduleType is null');
            }
        }
        return null;
    }

    public getPlanDayGroupId(scheduleKey): string {
        return this.getDayGroupId(scheduleKey, ScheduleDef.PLAN_DAY_GROUP);
    }

    public getPlanNextDayGroupId(scheduleKey): string {
        return this.getDayGroupId(scheduleKey, ScheduleDef.PLAN_NEXT_DAY_GROUP);
    }

    public getRunDayGroupId(scheduleKey): string {
        return this.getDayGroupId(scheduleKey, ScheduleDef.RUN_DAY_GROUP);
    }

    public getRunNextDayGroupId(scheduleKey): string {
        return this.getDayGroupId(scheduleKey, ScheduleDef.RUN_NEXT_DAY_GROUP);
    }

    public getRunningSchedules(): Observable<any> {
        console.log('{ScheduleService}', '[getRunningSchedules]');
        this.subjPeriodicSchedules.next(this.runningSchedules);

        return this.subjPeriodicSchedules;
    }

    public getWeeklySchedules(): Observable<any> {
        this.subjWeeklySchedules.next(this.weeklySchedules);

        return this.subjWeeklySchedules;
    }

    public readDayGroups() {
        const subDayGroup = this.scsTscService.getDayGroups().subscribe(dayGroupList => {
            this.dayGroupCnt = dayGroupList.length;
            let dayGroupReadyCnt = 0;
            for (const dg of dayGroupList) {
                 console.log('{ScheduleService}', '[readDayGroups]', 'names:', dg.names, 'ids:', dg.ids);
                 const daygroup = new DayGroup();
                 daygroup.ids = dg.ids;
                 daygroup.names = dg.names;
                 this.dayGroupIdMap.set(dg.ids, daygroup);
                 const subDates = this.scsTscService.getDates(dg.ids).subscribe(
                     datesList => {
                         daygroup.datesList = datesList;
                         dayGroupReadyCnt++;

                         console.log('{ScheduleService}', '[readDayGroups]', 'dayGroupCnt', this.dayGroupCnt, 'dayGroupReadyCnt:', dayGroupReadyCnt);
                         if (this.dayGroupCnt === dayGroupReadyCnt) {
                             this.updateRunningSchedules();
                             this.updateWeeklySchedules();
                         }
                         subDates.unsubscribe();
                     }
                 )
            }
            subDayGroup.unsubscribe();
        })
    }

    public updateRunningSchedules() {
        console.log('{ScheduleService}', '[updateRunningSchedules]', 'schedules count', this.schedules.length);
        this.runningSchedules = Array<Schedule>();
        // Get schedules running day group from config
        for (const s of this.schedules) {
            if (s.periodic) {
                if (this.isPeriodicScheduleRunning(s)) {
                    // Add schedule to running schedules
                    this.runningSchedules.push(s);
                    console.log('{ScheduleService}', '[updateRunningSchedules]', 'add to running schedules', s.id);
                }
            } else {
                if (this.isOneshotScheduleRunning(s)) {
                    // Add schedule to running schedules
                    this.runningSchedules.push(s);
                    console.log('{ScheduleService}', '[updateRunningSchedules]', 'add to running schedules', s.id);
                }
            }
        }
        this.subjPeriodicSchedules.next(this.runningSchedules);
    }

    public isPeriodicScheduleRunning(schedule: Schedule): boolean {
        const daygroupId = this.getRunDayGroupId(schedule.id);
        console.log('{ScheduleService}', '[isPeriodicScheduleRunning]', 'schedules', schedule.id, 'daygroupId', daygroupId);
        if (daygroupId) {
            const daygroup = this.dayGroupIdMap.get(daygroupId);
            console.log('{ScheduleService}', '[isPeriodicScheduleRunning]', 'daygroupId', daygroupId, 'daygroup', daygroup);
            if (daygroup && daygroup.datesList) {
                for (const d of daygroup.datesList) {
                    // Check if the day group dateslist contain current date
                    if (UtilService.isCurrentDate(d)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public isOneshotScheduleRunning(schedule: Schedule): boolean {
        const daygroupId = this.getRunDayGroupId(schedule.id);
        console.log('{ScheduleService}', '[isPeriodicScheduleRunning]', 'schedules', schedule.id, 'daygroupId', daygroupId);
        if (daygroupId) {
            const daygroup = this.dayGroupIdMap.get(daygroupId);
            console.log('{ScheduleService}', '[isPeriodicScheduleRunning]', 'daygroupId', daygroupId, 'daygroup', daygroup);
            if (daygroup && daygroup.datesList) {
                for (const d of daygroup.datesList) {
                    // Check if the day group dateslist contain current date or yesterday
                    if (UtilService.isCurrentDate(d) || UtilService.isYesterday(d)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public isOneshotScheduleStarted(): boolean {
        return this.oneshotStarted;
    }

    public isPeriodicScheduleStarted(): boolean {
        return this.periodicStarted;
    }

    public updateWeeklySchedules() {
        this.weeklySchedules = Array<Schedule>(7);
        const tempWeekdaySchedules = Array<Schedule>(7);

        // Get schedules planning day group from config
        for (const s of this.schedules) {
            const daygroupId = this.getPlanDayGroupId(s.id);

            if (daygroupId) {
                console.log('{ScheduleService}', '[updateWeeklySchedules]', 'schedule', s.id, 'daygroup id', daygroupId);
                const daygroup = this.dayGroupIdMap.get(daygroupId);

                if (daygroup && daygroup.datesList) {
                    console.log('{ScheduleService}', '[updateWeeklySchedules]', 'daygroup', daygroup.ids);
                    for (const d of daygroup.datesList) {
                        console.log('{ScheduleService}', '[updateWeeklySchedules]', 'date', d);

                        for (let i = 0; i < 7; i++) {
                            if (!tempWeekdaySchedules[i] && UtilService.includesComingDayOfWeek(d, i)) {
                                console.log('{ScheduleService}', '[updateWeeklySchedules]', 'weekday', i, 'schedule', s.id);
                                tempWeekdaySchedules[i] = s;
                            }
                        }
                    }
                }
            }
        }

        this.weeklySchedules = tempWeekdaySchedules;

        this.subjWeeklySchedules.next(this.weeklySchedules);
    }

    public setSchedulePlanDates(scheduleId, datesList, nextDatesList) {
        const daygroupId = this.getPlanDayGroupId(scheduleId);
        const nextDaygroupId = this.getPlanNextDayGroupId(scheduleId);

        const subdaygroup = this.scsTscService.setDates(daygroupId, datesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[setSchedulePlanDates]', 'response', res);
                subdaygroup.unsubscribe();
            }
        )
        const subdaygroup2 = this.scsTscService.setDates(nextDaygroupId, nextDatesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[setSchedulePlanDates]', 'response', res);
                subdaygroup2.unsubscribe();
            }
        )
    }

    public setScheduleRunDates(scheduleId, datesList, nextDatesList) {
        const daygroupId = this.getRunDayGroupId(scheduleId);
        const nextDaygroupId = this.getRunNextDayGroupId(scheduleId);

        const subdaygroup = this.scsTscService.setDates(daygroupId, datesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[setScheduleRunDates]', 'response', res);
                subdaygroup.unsubscribe();
            }
        )
        const subdaygroup2 = this.scsTscService.setDates(nextDaygroupId, nextDatesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[setScheduleRunDates]', 'response', res);
                subdaygroup2.unsubscribe();
            }
        )
    }


    public getUnusedSchedules(): Schedule[] {
        const schedules = Array<Schedule>();
        for (const s of this.schedules) {
            if (s.periodic && !s.titleReadOnly && s.visibility === ScheduleDef.INVISIBLE) {
                schedules.push(s);
            }
        }
        return schedules;
    }

    public addSchedule(scheduleKey): Observable<any> {
        console.log('{ScheduleService}', '[addSchedule]', scheduleKey);
        const s = this.scheduleKeyMap.get(scheduleKey);
        if (s) {
            s.visibility = ScheduleDef.VISIBLE;
            if (s.periodic) {
                s.runningStatus = this.periodicStarted ? ScheduleDef.STARTED : ScheduleDef.STOPPED;
            } else {
                s.runningStatus = this.oneshotStarted ? ScheduleDef.STARTED : ScheduleDef.STOPPED;
            }
            const desc = s.toString();
            return this.scsTscService.setDescription(s.taskName, desc, this.clientName).map(
                res => {
                    console.log('{ScheduleService}', '[addSchedule]', 'return', res);
                    this.updateSchedulesByPeriodic(this.currentIsPeriodic);
                    this.updateScheduleItemsByPeriodic(this.currentIsPeriodic);
                }
            )
        }
    }

    public deleteSchedule(scheduleKey): Observable<any> {
        console.log('{ScheduleService}', '[deleteSchedule]', scheduleKey);
        const s = this.scheduleKeyMap.get(scheduleKey);
        if (s) {
            s.visibility = ScheduleDef.INVISIBLE;
            const desc = s.toString();
            return this.scsTscService.setDescription(s.taskName, desc, this.clientName).map(
                res => {
                    console.log('{ScheduleService}', '[deleteSchedule]', 'return', res);
                    this.updateSchedulesByPeriodic(this.currentIsPeriodic);
                    this.updateScheduleItemsByPeriodic(this.currentIsPeriodic);
                }
            )
        }
    }

    public isScheduleRunning(scheduleKey): boolean {
        if (this.runningSchedules) {
            for (const s of this.runningSchedules) {
                if (s && s.id && s.id === scheduleKey) {
                    return true;
                }
            }
        }
        return false;
    }

    public isScheduleAssigned(scheduleKey): boolean {
        if (this.weeklySchedules) {
            for (const s of this.weeklySchedules) {
                if (s && s.id && s.id === scheduleKey) {
                    return true;
                }
            }
        }
        return false;
    }

    public isScheduleVisible(scheduleKey): boolean {
        const schedule = this.scheduleKeyMap.get(scheduleKey);
        if (schedule) {
            return schedule.visibility === ScheduleDef.VISIBLE;
        }
        return false;
    }
}
