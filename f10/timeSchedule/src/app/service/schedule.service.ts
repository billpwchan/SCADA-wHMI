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
import { LoadingService } from '../service/loading.service';
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
    private runningSchedules: Schedule[] = [];

    private weeklySchedules: Schedule[] = [];

    private scheduleIdMap = new Map<string, Schedule>();
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

    private tscTimeOffset = 0;

    private updateOnOffTimerMap = new Map<string, any>();

    private schTableTaskNames = new Array<string>();

    private runningStatusUpdateStartDelay = 10000;

    private runningStatusUpdatePeriod = 10000;

    private daygroupUpdatePeriod = 60000;

    private oneshotTaskNames = new Array<string>();

    private oneshotScheduleItems = new Array<ScheduleItem>();

    private oneshotTaskToRunCnt = 0;

    private dayGroupIdList: string[];

    constructor(
        private configService: ConfigService, private scsTscService: ScsTscService, private loadingService: LoadingService
    ) { }
    ngOnDestroy() {
    }
    public load() {
        this.loadConfig();
        this.loadData();
    }
    private loadConfig() {
        this.cutoffTime = this.configService.config.getIn(['schedule_table', 'cutoff_time']);
        console.log('{ScheduleServicee}', '[loadConfig]', 'cutoff_time=', this.cutoffTime);

        this.displayOffset = this.configService.config.getIn(['schedule_table', 'show_cutoff_offset']);
        console.log('{ScheduleService}', '[loadConfig]', 'show_cutoff_offset=', this.displayOffset);

        this.unavailableOnOffTime = this.configService.config.getIn(['schedule_table', 'unavailable_on_off_time']);
        console.log('{ScheduleService}', '[loadConfig]', 'unavailable_on_off_time=', this.unavailableOnOffTime);

        this.inhibitedOnOffTime = this.configService.config.getIn(['schedule_table', 'inhibited_on_off_time']);
        console.log('{ScheduleService}', '[loadConfig]', 'inhibited_on_off_time=', this.inhibitedOnOffTime);

        this.dayGroupConfig = this.configService.config.getIn(['schedule_table', 'schedule_daygroup']);
        console.log('{ScheduleService}', '[loadConfig]', 'schedule_daygroup=', this.dayGroupConfig);

        this.tscTimeOffset = this.configService.config.getIn(['tsc_time_offset']);
        console.log('{ScheduleService}', '[loadConfig]', 'tsc_time_offset=', this.tscTimeOffset);

        this.runningStatusUpdateStartDelay = this.configService.config.getIn(['running_status_update_start_delay']);
        console.log('{ScheduleService}', '[loadConfig]', 'running_status_update_start_delay', this.runningStatusUpdateStartDelay);

        this.runningStatusUpdatePeriod = this.configService.config.getIn(['running_status_update_period']);
        console.log('{ScheduleService}', '[loadConfig]', 'running_status_update_period', this.runningStatusUpdatePeriod);

        this.daygroupUpdatePeriod = this.configService.config.getIn(['daygroup_update_period']);
        console.log('{ScheduleService}', '[loadConfig]', 'daygroup_update_period', this.daygroupUpdatePeriod);
    }
    public loadData() {
        this.readTscTaskNames();
    }

    private readTscTaskNames() {
        const subGetTaskNames = this.scsTscService.getTaskNames().subscribe(
            (taskNames: any[]) => {
                console.log('{ScheduleService}', '[readTscTaskNames]', taskNames);
                this.extractScheduleTasks(taskNames);

                if (this.schTableTaskNames && this.schTableTaskNames.length > 0) {
                    this.readSchTableDescription(this.schTableTaskNames);
                } else {
                    console.error('{ScheduleService}', '[readTscTaskNames]', 'No schedule table tasks found');
                }

                subGetTaskNames.unsubscribe();
            }
        )
    }

    private readSchTableDescription(taskNames: string[]) {
        const subGetSchTableDesc = Observable.forkJoin(
            taskNames.map((taskName: any) => {
                return this.scsTscService.getDescription(taskName)
                    .map((desc: any) => {
                        return desc;
                    })
            }))
            .subscribe((data: any[]) => {
                this.extractSchTableDesc(taskNames, data);

                console.log('{ScheduleService}', '[readSchTableDescription]', 'currentIsPeriodic', this.currentIsPeriodic);
                this.updateSchedulesByPeriodic(this.currentIsPeriodic);

                this.updateScheduleItemsByPeriodic(this.currentIsPeriodic);

                this.readDayGroups();

                this.startRunningStatusUpdateTimer(this.runningStatusUpdateStartDelay);

                subGetSchTableDesc.unsubscribe();
                console.log('{ScheduleService}', '[readSchTableDescription]', 'end');
            })
    }

    private extractSchTableDesc(taskNames, data) {
        console.log('{ScheduleService}', '[extractSchTableDesc]', 'begin');
        for (let i = 0; i < taskNames.length; i++) {
            const taskColumns: string[] = taskNames[i].split(',');
            const header = taskColumns[ScheduleDef.SCHEDULE_HEADER_COL];
            const scheduleType = taskColumns[ScheduleDef.SCHEDULE_TYPE_COL];
            const scheduleId = taskColumns[ScheduleDef.SCHEDULE_ID_COL];
            if (header != null && header === ScheduleDef.SCHEDULE_TABLE_HEADER) {
                const schedule = this.schedules.find(s =>
                    s.scheduleType === scheduleType &&
                    s.id === scheduleId);
                if (schedule != null) {
                    const schTableData: string = data[i];
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
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule description updated',  schedule.scheduleDescription);
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule title updated ',  schedule.text);
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule runningStatus updated ',  schedule.runningStatus);
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule periodic updated ', schedule.periodic);
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule titleReadOnly updated ', schedule.titleReadOnly);
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule timeReadOnly updated ', schedule.timeReadOnly);
                    // console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule eqtListReadOnly updated ', schedule.eqtListReadOnly);
                    console.log('{ScheduleService}', '[extractSchTableDesc]', ' schedule table updated ');
                } else {
                    console.warn('{ScheduleService}', '[extractSchTableDesc]', ' schedule table not found ');
                }
            } else {
                console.error('{ScheduleService}', '[extractSchTableDesc]', 'unrecognized header in taskname', taskNames[i]);
            }
        }
        console.log('{ScheduleService}', '[extractSchTableDesc]', 'end');
    }

    private extractScheduleTasks(taskNames: string[]) {
        console.log('{ScheduleService}', '[extractScheduleTasks]', 'begin', 'taskName count=', taskNames.length);
        for (let i = 0; i < taskNames.length; i++) {
            const columns: string[] = taskNames[i].split(',');
            const header = columns[ScheduleDef.SCHEDULE_HEADER_COL];
            if (header != null && header === ScheduleDef.SCHEDULE_TABLE_HEADER) {
                this.extractScheduleTable(taskNames[i], columns);
            } else if (header != null && header === ScheduleDef.SCHEDULE_TASK_HEADER) {
                this.extractScheduleTask(taskNames[i], columns);
            } else {
                console.error('{ScheduleService}', '[extractScheduleTasks]', 'unrecognized header in taskname', taskNames[i]);
            }
        }
        console.log('{ScheduleService}', '[extractScheduleTasks]', 'end');
    }
    private extractScheduleTable(taskName: string, columns: string[]) {
        console.log('{ScheduleService}', '[extractScheduleTable]', 'begin');
        const scheduleType = columns[ScheduleDef.SCHEDULE_TYPE_COL];
        const scheduleId = columns[ScheduleDef.SCHEDULE_ID_COL];
        let sch = this.schedules.find( r =>
            r.scheduleType === scheduleType &&
            r.id === scheduleId);
        if (!sch) {
            sch = this.createSchedule(scheduleType, scheduleId, taskName);
            this.schedules.push(sch);
            this.scheduleIdMap.set(sch.id, sch);
            this.schTableTaskNames.push(taskName);
            // console.log('{ScheduleService}', '[extractScheduleTable]', 'new schedule table created. id =', sch.id);
        } else {
            console.warn('{ScheduleService}', '[extractScheduleTable]', 'schedule table already exists.');
        }
        console.log('{ScheduleService}', '[extractScheduleTable]', 'end');
    }
    private extractScheduleTask(taskName: string, columns: string[]) {
        // console.log('{ScheduleService}', '[extractScheduleTask]', 'begin');

        // extract fields from task name
        const scheduleType = columns[ScheduleDef.SCHEDULE_TYPE_COL];
        const scheduleId = columns[ScheduleDef.SCHEDULE_ID_COL];
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
            const funcCat = +columns[ScheduleDef.SCHEDULE_FUNC_COL];
            const geoCat = +columns[ScheduleDef.SCHEDULE_GEO_COL];
            const row = this.createScheduleItem(key, scheduleType, scheduleId, eqtAlias, eqtPointAtt,
                                                funcCat, geoCat, targetState, taskName);
            this.scheduleItems.push(row);

            if (scheduleType === 'OS') {
                this.oneshotScheduleItems.push(row);
            }
            // console.log('{ScheduleService}', '[extractScheduleTask]', 'new schedule item created.');
        } else {
            if (targetState === 'OFF') {
                rowFound.taskName2 = taskName;
            } else {
                rowFound.taskName1 = taskName;
            }
        }

        if (scheduleType === 'OS') {
            this.oneshotTaskNames.push(taskName);
            // console.log('{ScheduleService}', '[extractScheduleTask]', 'oneshot task pushed', taskName);
        }

        // console.log('{ScheduleService}', '[extractScheduleTask]', 'end');
    }
    private createSchedule(scheduleType: string, scheduleId: string, taskName: string): Schedule {
        const sch = new Schedule();
        sch.scheduleType = scheduleType;
        sch.id = scheduleId;
        sch.taskName = taskName;
        return sch;
    }
    private createScheduleItem(key: number, scheduleType: string, scheduleId: string, eqtAlias: string,
                                    eqtPointAtt: string, funcCat: number, geoCat: number, targetState: string, taskName: string): ScheduleItem {
        // console.log('{ScheduleService}', '[createScheduleItem]', 'begin');
        const t = new ScheduleItem();
        t.scheduleType = scheduleType;
        t.scheduleId = scheduleId;
        t.eqtDescription = '';
        t.eqtLabel = '';
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

    public getScheduleDescFilterEnable(taskName: string) {
        console.log('{ScheduleService}', '[getScheduleDescFilterEnable]', 'taskName=', taskName);
        const taskColumns: string[] = taskName.split(',');
        const header = taskColumns[ScheduleDef.SCHEDULE_HEADER_COL];
        const scheduleType = taskColumns[ScheduleDef.SCHEDULE_TYPE_COL];
        const scheduleId = taskColumns[ScheduleDef.SCHEDULE_ID_COL];
        const alias = taskColumns[ScheduleDef.SCHEDULE_EQT_ALIAS_COL];
        const pointAtt = taskColumns[ScheduleDef.SCHEDULE_EQT_POINT_ATT_COL];
        const targetState = taskColumns[ScheduleDef.SCHEDULE_EQT_TARGET_STATE];
        const scheduleItem = this.scheduleItems.find( r =>
                    r.scheduleType === scheduleType &&
                    r.scheduleId === scheduleId &&
                    r.eqtAlias === alias &&
                    r.eqtPointAtt === pointAtt);
        if (scheduleItem) {
            if (scheduleItem.eqtLabel.length === 0 && scheduleItem.eqtDescription.length === 0) {
                const subGetDesc = this.scsTscService.getDescription(taskName).subscribe((desc) => {
                    console.log('{ScheduleService}', '[getScheduleDescFilterEnable]', 'desc=', desc);
                    scheduleItem.eqtLabel = desc.split(',')[0];
                    scheduleItem.eqtDescription = desc.split(',')[1];
                    subGetDesc.unsubscribe();
                });
            }
            const subGetFilter = this.scsTscService.getFilter(taskName).subscribe((filter) => {
                console.log('{ScheduleService}', '[getScheduleDescFilterEnable]', 'filter=', filter);
                const onOffTime = this.getOnOffTime(filter);
                const onOffTimeDisplay = onOffTime;
                if (targetState === 'ON') {
                    scheduleItem.onTime = onOffTime;
                    scheduleItem.onTimeDisplay = onOffTimeDisplay;
                    scheduleItem.filter1 = filter;
                } else if (targetState === 'OFF') {
                    scheduleItem.offTime = onOffTime;
                    scheduleItem.offTimeDisplay = onOffTimeDisplay;
                    scheduleItem.filter2 = filter;
                }
                this.updateScheduleItemOnOffTimeDisplay(scheduleItem);
                subGetFilter.unsubscribe();
            });
            const subGetEnable = this.scsTscService.getEnableFlag(taskName).subscribe((enableFlag) => {
                console.log('{ScheduleService}', '[getScheduleDescFilterEnable]', 'enableFlag=', enableFlag);
                if (targetState === 'ON') {
                    scheduleItem.enableFlag1 = +enableFlag;
                } else {
                    scheduleItem.enableFlag2 = +enableFlag;
                }
                this.updateScheduleItemOnOffTimeDisplay(scheduleItem);
                subGetEnable.unsubscribe();
            });
        }
    }
    public getScheduleFilter(taskName: string) {
        console.log('{ScheduleService}', '[getScheduleFilter]', 'taskName=', taskName);
        const taskColumns: string[] = taskName.split(',');
        const header = taskColumns[ScheduleDef.SCHEDULE_HEADER_COL];
        const scheduleType = taskColumns[ScheduleDef.SCHEDULE_TYPE_COL];
        const scheduleId = taskColumns[ScheduleDef.SCHEDULE_ID_COL];
        const alias = taskColumns[ScheduleDef.SCHEDULE_EQT_ALIAS_COL];
        const pointAtt = taskColumns[ScheduleDef.SCHEDULE_EQT_POINT_ATT_COL];
        const targetState = taskColumns[ScheduleDef.SCHEDULE_EQT_TARGET_STATE];
        const scheduleItem = this.scheduleItems.find( r =>
                    r.scheduleType === scheduleType &&
                    r.scheduleId === scheduleId &&
                    r.eqtAlias === alias &&
                    r.eqtPointAtt === pointAtt);
        if (scheduleItem) {
            const subGetFilter = this.scsTscService.getFilter(taskName).subscribe((filter) => {
                console.log('{ScheduleService}', '[getScheduleFilter]', 'filter=', filter);
                const onOffTime = this.getOnOffTime(filter);
                const onOffTimeDisplay = onOffTime;
                if (targetState === 'ON') {
                    scheduleItem.onTime = onOffTime;
                    scheduleItem.onTimeDisplay = onOffTimeDisplay;
                    scheduleItem.filter1 = filter;
                } else if (targetState === 'OFF') {
                    scheduleItem.offTime = onOffTime;
                    scheduleItem.offTimeDisplay = onOffTimeDisplay;
                    scheduleItem.filter2 = filter;
                }
                this.updateScheduleItemOnOffTimeDisplay(scheduleItem);
                subGetFilter.unsubscribe();
            });
        }
    }
    public getScheduleEnable(taskName: string) {
        console.log('{ScheduleService}', '[getScheduleEnable]', 'taskName=', taskName);
        const taskColumns: string[] = taskName.split(',');
        const header = taskColumns[ScheduleDef.SCHEDULE_HEADER_COL];
        const scheduleType = taskColumns[ScheduleDef.SCHEDULE_TYPE_COL];
        const scheduleId = taskColumns[ScheduleDef.SCHEDULE_ID_COL];
        const alias = taskColumns[ScheduleDef.SCHEDULE_EQT_ALIAS_COL];
        const pointAtt = taskColumns[ScheduleDef.SCHEDULE_EQT_POINT_ATT_COL];
        const targetState = taskColumns[ScheduleDef.SCHEDULE_EQT_TARGET_STATE];
        const scheduleItem = this.scheduleItems.find( r =>
                    r.scheduleType === scheduleType &&
                    r.scheduleId === scheduleId &&
                    r.eqtAlias === alias &&
                    r.eqtPointAtt === pointAtt);
        if (scheduleItem) {
            const subGetEnbale = this.scsTscService.getEnableFlag(taskName).subscribe((enableFlag) => {
                console.log('{ScheduleService}', '[getScheduleEnable]', 'enableFlag=', enableFlag);
                if (targetState === 'ON') {
                    scheduleItem.enableFlag1 = +enableFlag;
                } else {
                    scheduleItem.enableFlag2 = +enableFlag;
                }
                this.updateScheduleItemOnOffTimeDisplay(scheduleItem);
                subGetEnbale.unsubscribe();
            });
        }
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
    private updateOneshotOnOffTimeDisplayAll() {
        for (const scheduleItem of this.oneshotScheduleItems) {
            this.updateOneshotOnOffTimeDisplay(scheduleItem);
        }
    }
    private updateOneshotOnOffTimeDisplay(scheduleItem: ScheduleItem) {
        const schedule = this.scheduleIdMap.get(scheduleItem.scheduleId);
        const scheduleRunDaygroup = this.getRunDayGroupId(scheduleItem.scheduleId);
        const scheduleRunNextDaygroup = this.getRunNextDayGroupId(scheduleItem.scheduleId);
        if (scheduleItem.enableFlag1 === 0) {
            scheduleItem.onTimeDisplay = this.inhibitedOnOffTime;
        } else {
            // Change onTimeDisplay to blank if time has passed
            if (scheduleItem.onTime !== this.unavailableOnOffTime && scheduleItem.onTime.length > 0) {
                const hh1 = +scheduleItem.onTime.split(':')[0];
                const mm1 = +scheduleItem.onTime.split(':')[1];
                const taskDaygroup = '' + scheduleItem.filter1.split(' ')[0];
                console.log('{ScheduleService}', '[updateOneshotOnOffTimeDisplay]', 'onTime', scheduleItem.onTime, 'onTimeDisplay', scheduleItem.onTimeDisplay,
                    'hour', hh1, 'minute', mm1, 'schedule daygroup', scheduleRunDaygroup, 'taskDaygroup', taskDaygroup);
                const dg = this.dayGroupIdMap.get(taskDaygroup);
                if (this.isOneshotScheduleStarted() && dg && dg.datesList) {
                    let dateTimeExpired = true;
                    for (const d of dg.datesList) {
                        if (!UtilService.isDateExpired(+d, hh1, mm1)) {
                            dateTimeExpired = false;
                            break;
                        }
                    }
                    if (dateTimeExpired) {
                        scheduleItem.onTimeDisplay = '';
                    } else {
                        scheduleItem.onTimeDisplay = scheduleItem.onTime;

                        this.addOnOffUpdateTimer(scheduleItem, scheduleItem.taskName1, hh1, mm1);
                    }
                } else {
                    if (taskDaygroup === scheduleRunDaygroup) {
                        if (UtilService.isTimeExpired(hh1, mm1)) {
                            scheduleItem.onTimeDisplay = '';
                        } else {
                            scheduleItem.onTimeDisplay = scheduleItem.onTime;

                            this.addOnOffUpdateTimer(scheduleItem, scheduleItem.taskName1, hh1, mm1);
                        }
                    } else if (taskDaygroup === scheduleRunNextDaygroup) {
                        scheduleItem.onTimeDisplay = scheduleItem.onTime;
                    } else {
                        console.warn('{ScheduleService}', '[updateOneshotOnOffTimeDisplay]', 'invalid daygroup found for schedule task', scheduleItem.taskName1);
                    }
                }
            }
        }

        if (scheduleItem.enableFlag2 === 0) {
            scheduleItem.offTimeDisplay = this.inhibitedOnOffTime;
        } else {
            // Change offTimeDisplay to blank if time has passed
            if (scheduleItem.offTime !== this.unavailableOnOffTime && scheduleItem.offTime.length > 0) {
                const hh2 = +scheduleItem.offTime.split(':')[0];
                const mm2 = +scheduleItem.offTime.split(':')[1];
                const taskDaygroup = '' + scheduleItem.filter2.split(' ')[0];
                console.log('{ScheduleService}', '[updateOneshotOnOffTimeDisplay]', 'offTime', scheduleItem.offTime, 'offTimeDisplay',
                    scheduleItem.offTimeDisplay, 'hour', hh2, 'minute', mm2, 'schedule daygroup', scheduleRunDaygroup, 'taskDaygroup', taskDaygroup);
                const dg = this.dayGroupIdMap.get(taskDaygroup);
                if (this.isOneshotScheduleStarted() && dg && dg.datesList) {
                    let dateTimeExpired = true;
                    for (const d of dg.datesList) {
                        if (!UtilService.isDateExpired(+d, hh2, mm2)) {
                            dateTimeExpired = false;
                            break;
                        }
                    }
                    if (dateTimeExpired) {
                        scheduleItem.offTimeDisplay = '';
                    } else {
                        scheduleItem.offTimeDisplay = scheduleItem.offTime;

                        this.addOnOffUpdateTimer(scheduleItem, scheduleItem.taskName2, hh2, mm2);
                    }
                } else {
                    if (taskDaygroup === scheduleRunDaygroup) {
                        if (UtilService.isTimeExpired(hh2, mm2)) {
                            scheduleItem.offTimeDisplay = '';
                        } else {
                            scheduleItem.offTimeDisplay = scheduleItem.offTime;

                            this.addOnOffUpdateTimer(scheduleItem, scheduleItem.taskName2, hh2, mm2);
                        }
                    } else if (taskDaygroup === scheduleRunNextDaygroup) {
                        scheduleItem.offTimeDisplay = scheduleItem.offTime;
                    } else {
                        console.warn('{ScheduleService}', '[updateOneshotOnOffTimeDisplay]', 'invalid daygroup found for schedule task', scheduleItem.taskName2);
                    }
                }
            }
        }
    }
    public getSchedulesByPeriodic(isPeriodic: boolean): Observable<Schedule[]> {
        console.log('{ScheduleService}', '[getSchedulesByPeriodic]', 'isPeriodic', isPeriodic);

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
        this.currentIsPeriodic = isPeriodic;
        if (this.schedules) {
            console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'schedules count=', this.schedules.length);
        } else {
            console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'schedules not defined');
        }

        const schedulesByPeriodic = new Array<Schedule>();
        for (const s of this.schedules) {
            // console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', 'compare periodic', isPeriodic, s.periodic);
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
                    // console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', '*** schedule is pushed to schedulesByPeriodic');
                }
            }
        }
        console.log('{ScheduleService}', '[updateSchedulesByPeriodic]', schedulesByPeriodic.length, ' schedules pushed to schedulesByPeriodic');
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
            const speriodic: boolean = this.scheduleIdMap.get(s.scheduleId).periodic;
            const svisible: boolean = this.scheduleIdMap.get(s.scheduleId).visibility === ScheduleDef.VISIBLE;
            // console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', 'compare periodic', isPeriodic, speriodic);

            if (svisible) {
                if ((isPeriodic && speriodic) || (!isPeriodic && !speriodic)) {
                    if (isPeriodic) {
                        if (s.enableFlag1 === 0) {
                            // console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag1 is 0');
                            s.onTimeDisplay = this.inhibitedOnOffTime;
                        } else {
                            // console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag1 is 1');
                            s.onTimeDisplay = this.getPeriodicOnOffTimeDisplay(s.filter1);
                        }
                        if (s.enableFlag2 === 0) {
                            // console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag2 is 0');
                            s.offTimeDisplay = this.inhibitedOnOffTime;
                        } else {
                            // console.log('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag2 is 1');
                            s.offTimeDisplay = this.getPeriodicOnOffTimeDisplay(s.filter2);
                        }
                    // } else {
                    //     this.updateOneshotOnOffTimeDisplay(s);
                    }
                    scheduleItemsByPeriodic.push(s);
                    // console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', '*** scheduleItem is pushed to scheduleItemsByPeriodic');
                }
            }
        }
        console.log('{ScheduleService}', '[updateScheduleItemsByPeriodic]', scheduleItemsByPeriodic.length, ' scheduleItems pushed to scheduleItemsByPeriodic');
        this.subjScheduleItemsByPeriodic.next(scheduleItemsByPeriodic);
    }
    public updateScheduleItemOnOffTimeDisplay(scheduleItem: ScheduleItem) {
        const speriodic: boolean = this.scheduleIdMap.get(scheduleItem.scheduleId).periodic;

        if (speriodic) {
            if (scheduleItem.enableFlag1 === 0) {
                // console.log('{ScheduleService}', '[updateScheduleItemOnOffTimeDisplay]', 'enableFlag1 is 0');
                scheduleItem.onTimeDisplay = this.inhibitedOnOffTime;
            } else {
                // console.log('{ScheduleService}', '[updateScheduleItemOnOffTimeDisplay]', 'enableFlag1 is 1');
                scheduleItem.onTimeDisplay = this.getPeriodicOnOffTimeDisplay(scheduleItem.filter1);
            }
            if (scheduleItem.enableFlag2 === 0) {
                // console.log('{ScheduleService}', '[updateScheduleItemOnOffTimeDisplay]', 'enableFlag2 is 0');
                scheduleItem.offTimeDisplay = this.inhibitedOnOffTime;
            } else {
                // console.log('{ScheduleService}', '[updateScheduleItemOnOffTimeDisplay]', 'enableFlag2 is 1');
                scheduleItem.offTimeDisplay = this.getPeriodicOnOffTimeDisplay(scheduleItem.filter2);
            }
        } else {
            this.updateOneshotOnOffTimeDisplay(scheduleItem);
        }
    }
    public setClientName(clientName: string) {
        this.clientName = clientName;
    }
    public setFilter(taskName: string, filter: string): Observable<any> {
        console.log('{ScheduleService}', '[setFilter]', filter);
        return this.scsTscService.setFilter(taskName, filter, this.clientName).map(
            res => {
                this.getScheduleFilter(taskName);
                return res;
            }
        )
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
    public enableTask(taskName: string): Observable<any> {
        console.log('{ScheduleService}', '[enableTask]');
        return this.scsTscService.enableTask(taskName, 1, this.clientName).map(
            res => {
                this.getScheduleEnable(taskName);
                return res;
            }
        );
    }
    public disableTask(taskName: string): Observable<any> {
        console.log('{ScheduleService}', '[disableTask]');
        return this.scsTscService.enableTask(taskName, 0, this.clientName).map(
            res => {
                this.getScheduleEnable(taskName);
                return res;
            }
        );
    }

    public startOneshotSchedule(scheduleId) {
        const runDayGroupId = this.getRunDayGroupId(scheduleId);
        const runNextDayGroupId = this.getRunNextDayGroupId(scheduleId);

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

        if (this.tscTimeOffset > 0) {
            this.addOffsetToDateList(curDateList);
            this.addOffsetToDateList(nextDateList);
        }

        // Set dateList to running daygroup to implement start schedule
        const subDates = this.scsTscService.setDates(runDayGroupId, curDateList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[startOneshotSchedule]', 'setDates', res);

                const subDates2 = this.scsTscService.setDates(runNextDayGroupId, nextDateList, this.clientName).subscribe(
                    res2 => {
                        console.log('{ScheduleService}', '[startOneshotSchedule]', 'setDates', res2);

                        // Update running status in schedule task description
                        this.setScheduleRunningDesc(scheduleId, ScheduleDef.STARTED);

                        // Reload day groups to update running schedules
                        this.readDayGroupDates();

                        subDates2.unsubscribe();
                    }
                );
                subDates.unsubscribe();
            }
        );
    }

    public addOffsetToDateList(dateList: string[]) {
        for (let i = 0; i < dateList.length; i++) {
            const timevalue = +dateList[i] + this.tscTimeOffset;
            dateList[i] = timevalue.toString();
        }
    }
    public subtractOffsetFromDateList(dateList: string[]) {
        for (let i = 0; i < dateList.length; i++) {
            const timevalue = +dateList[i] - this.tscTimeOffset;
            dateList[i] = timevalue.toString();
        }
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

                                    // Get next dateList from planning daygroup
                                    if (planNextDayGroupId && runNextDayGroupId) {
                                        const subGetDates2 = this.scsTscService.getDates(planNextDayGroupId).subscribe(
                                            datesList2 => {
                                                console.log('{ScheduleService}', '[startPeriodicSchedules]', 'getDates', datesList2);

                                                // Set dateList to running daygroup to implement start schedule
                                                const subSetDates2 = this.scsTscService.setDates(runNextDayGroupId, datesList2, this.clientName).subscribe(
                                                    res2 => {
                                                        console.log('{ScheduleService}', '[startPeriodicSchedules]', 'setDates', res2);

                                                        // Update running status in schedule task description
                                                        this.setScheduleRunningDesc(s.id, ScheduleDef.STARTED);

                                                        // Reload day groups to update running schedules
                                                        this.readDayGroupDates();

                                                        subSetDates2.unsubscribe();
                                                    }
                                                );
                                                subGetDates2.unsubscribe();
                                            }
                                        );
                                    } else {
                                        console.error('{ScheduleService}', '[startPeriodicSchedules]',
                                            'planNextDayGroupId/runNextDayGroupId not found in config')
                                    }
                                    subSetDates.unsubscribe();
                                }
                            );
                            subGetDates.unsubscribe();
                        }
                    );
                } else {
                    console.error('{ScheduleService}', '[startPeriodicSchedules]', 'planDayGroupId/runDayGroupId not found in config')
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

    public stopSchedule(scheduleId) {
        const runDayGroupId = this.getRunDayGroupId(scheduleId);
        const runNextDayGroupId = this.getRunNextDayGroupId(scheduleId);

        // Set empty dateList to running daygroup to implement stop schedule
        const datesList = new Array<string>();
        const subSetDates = this.scsTscService.setDates(runDayGroupId, datesList, this.clientName).subscribe(
            res => {
                console.log('{ScheduleService}', '[stopSchedule]', 'setDates', res);

                const subSetDates2 = this.scsTscService.setDates(runNextDayGroupId, datesList, this.clientName).subscribe(
                    res2 => {
                        console.log('{ScheduleService}', '[stopSchedule]', 'setDates', res2);

                        // Update running status in schedule task description
                        this.setScheduleRunningDesc(scheduleId, ScheduleDef.STOPPED);

                        // Reload day groups to update running schedules
                        this.readDayGroupDates();

                        subSetDates2.unsubscribe();
                    }
                );
                subSetDates.unsubscribe();
            }
        );
    }

    public setScheduleRunningDesc(scheduleId, runningStatus) {
        console.log('{ScheduleService}', '[setScheduleRunningDesc]', scheduleId, 'runningStatus', runningStatus);
        const schedule = this.scheduleIdMap.get(scheduleId);
        if (schedule && (runningStatus === ScheduleDef.STARTED || runningStatus === ScheduleDef.STOPPED)) {
            schedule.runningStatus = runningStatus;
            const taskDesc = schedule.toString();
            const subSetScheduleDesc = this.scsTscService.setDescription(schedule.taskName, taskDesc, this.clientName).subscribe(
                res => {
                    console.log('{ScheduleService}', '[setScheduleRunningDesc]', res);

                    subSetScheduleDesc.unsubscribe();
                }
            );
        }
    }

    public getDayGroupId(scheduleId, dayGroupType) {
        const schedule = this.scheduleIdMap.get(scheduleId);
        if (schedule && this.dayGroupConfig) {
            // console.log('{ScheduleService}', '[getDayGroupId]', 'scheduleId', scheduleId, 'dayGroupType', dayGroupType, this.dayGroupConfig);

            if (this.dayGroupConfig.get(schedule.scheduleType)) {
                if (this.dayGroupConfig.get(schedule.scheduleType).get(schedule.id)) {
                    if (this.dayGroupConfig.get(schedule.scheduleType)) {
                        return this.dayGroupConfig.get(schedule.scheduleType).get(schedule.id).get(dayGroupType);
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

    public getPlanDayGroupId(scheduleId): string {
        // Add empty string in front of return value to force return value to be string
        return '' + this.getDayGroupId(scheduleId, ScheduleDef.PLAN_DAY_GROUP);
    }

    public getPlanNextDayGroupId(scheduleId): string {
        // Add empty string in front of return value to force return value to be string
        return '' + this.getDayGroupId(scheduleId, ScheduleDef.PLAN_NEXT_DAY_GROUP);
    }

    public getRunDayGroupId(scheduleId): string {
        // Add empty string in front of return value to force return value to be string
        return '' + this.getDayGroupId(scheduleId, ScheduleDef.RUN_DAY_GROUP);
    }

    public getRunNextDayGroupId(scheduleId): string {
        // Add empty string in front of return value to force return value to be string
        return '' + this.getDayGroupId(scheduleId, ScheduleDef.RUN_NEXT_DAY_GROUP);
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
            // this.dayGroupCnt = dayGroupList.length;
            this.dayGroupIdList = new Array<string>();
            for (const dg of dayGroupList) {
                 console.log('{ScheduleService}', '[readDayGroups]', 'names:', dg.names, 'ids:', dg.ids);
                 const daygroup = new DayGroup();
                 daygroup.ids = '' + dg.ids;    // empty string in front to force value to string
                 daygroup.names = dg.names;
                 daygroup.datesList = new Array<string>();
                 this.dayGroupIdList.push(daygroup.ids);
                 this.dayGroupIdMap.set(daygroup.ids, daygroup);
            }
            this.readDayGroupDates();

            const startTime = new Date();
            startTime.setSeconds(startTime.getSeconds() + (60 - startTime.getSeconds()))
            this.startDayGroupUpdateTimer(startTime);
            subDayGroup.unsubscribe();
        })
    }

    public readDayGroupDates() {
        console.log('{ScheduleService}', '[readDayGroupDates]', 'dayGroupIdMap.keys =', this.dayGroupIdMap.keys());
        const sub = Observable.of(this.dayGroupIdList).mergeMap(daygroupIdList => {
            const obsList = new Array<Observable<any>>();
            for (const dgid of daygroupIdList) {
                obsList.push(this.scsTscService.getDates(dgid).map((datesList) => {
                    console.log('{ScheduleService}', '[readDayGroupDates]', 'dgid', dgid, 'datesList =', datesList);
                    const daygroup = this.dayGroupIdMap.get(dgid);
                    if (daygroup) {
                        daygroup.datesList = [...datesList];
                        if (this.tscTimeOffset > 0) {
                            this.subtractOffsetFromDateList(daygroup.datesList);
                        }
                    }
                    return datesList;
                }));
            }
            return Observable.combineLatest(obsList);
        })
        .subscribe(() => {
            console.log('{ScheduleService}', '[readDayGroupDates]', 'on subscribe');
            this.loadNonPeriodicScheduleItems();
            this.updateWeeklySchedules();
            sub.unsubscribe();
        })
    }

    public updateRunningSchedules() {
        console.log('{ScheduleService}', '[updateRunningSchedules]', 'schedules count', this.schedules.length);
        this.runningSchedules = new Array<Schedule>();
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
        const daygroupId: string = this.getRunDayGroupId(schedule.id);
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
            } else {
                console.log('{ScheduleService}', '[isPeriodicScheduleRunning]', 'No daygroup/datesList found for daygroupId', daygroupId);
            }
        }
        return false;
    }

    public isOneshotScheduleRunning(schedule: Schedule): boolean {
        console.log('{ScheduleService}', '[isOneshotScheduleRunning]', 'oneshotTaskToRunCnt', this.oneshotTaskToRunCnt);
        if (this.isOneshotScheduleStarted && this.oneshotTaskToRunCnt > 0) {
            return true;
        } else {
            return false;
        }
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
            if (s.periodic) {
                const daygroupId = this.getPlanDayGroupId(s.id);

                if (daygroupId) {
                    console.log('{ScheduleService}', '[updateWeeklySchedules]', 'schedule', s.id, 'daygroup id', daygroupId);
                    const daygroup = this.dayGroupIdMap.get(daygroupId);

                    if (daygroup && daygroup.datesList) {
                        // console.log('{ScheduleService}', '[updateWeeklySchedules]', 'daygroup', daygroup.ids);
                        for (const d of daygroup.datesList) {
                            // console.log('{ScheduleService}', '[updateWeeklySchedules]', 'date', d);

                            if (UtilService.beyondNextWeek(d)) {
                                console.log('{ScheduleService}', '[updateWeeklySchedules]', 'beyond next week', d);
                                break;
                            }

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
        }

        this.weeklySchedules = tempWeekdaySchedules;

        this.subjWeeklySchedules.next(this.weeklySchedules);
    }

    public setSchedulePlanDates(scheduleId, datesList, nextDatesList) {
        const daygroupId = this.getPlanDayGroupId(scheduleId);
        const nextDaygroupId = this.getPlanNextDayGroupId(scheduleId);

        if (this.tscTimeOffset > 0) {
            this.addOffsetToDateList(datesList);
            this.addOffsetToDateList(nextDatesList);
        }

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

        if (this.tscTimeOffset > 0) {
            this.addOffsetToDateList(datesList);
            this.addOffsetToDateList(nextDatesList);
        }

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

    public addSchedule(scheduleId): Observable<any> {
        console.log('{ScheduleService}', '[addSchedule]', scheduleId);
        const s = this.scheduleIdMap.get(scheduleId);
        if (s) {
            s.visibility = ScheduleDef.VISIBLE;
            if (s.periodic) {
                s.runningStatus = this.periodicStarted ? ScheduleDef.STARTED : ScheduleDef.STOPPED;

                const desc = s.toString();
                return this.scsTscService.setDescription(s.taskName, desc, this.clientName).map(
                    res => {
                        console.log('{ScheduleService}', '[addSchedule]', 'return', res);
                        this.updateSchedulesByPeriodic(true);
                        this.updateScheduleItemsByPeriodic(true);
                    }
                )
            } else {
                console.log('{ScheduleService}', '[addSchedule]', 'Cannot add OneShot schedule');
            }
        }
        return Observable.empty();
    }

    public deleteSchedule(scheduleId): Observable<any> {
        console.log('{ScheduleService}', '[deleteSchedule]', scheduleId);
        const s = this.scheduleIdMap.get(scheduleId);
        if (s) {
            s.visibility = ScheduleDef.INVISIBLE;
            if (s.periodic) {
                const desc = s.toString();
                return this.scsTscService.setDescription(s.taskName, desc, this.clientName).map(
                    res => {
                        console.log('{ScheduleService}', '[deleteSchedule]', 'return', res);
                        this.updateSchedulesByPeriodic(true);
                        this.updateScheduleItemsByPeriodic(true);
                    }
                )
            } else {
                console.log('{ScheduleService}', '[deleteSchedule]', 'Cannot delete OneShot schedule');
            }
        }
        return Observable.empty();
    }

    public isScheduleRunning(scheduleId): boolean {
        if (this.runningSchedules) {
            for (const s of this.runningSchedules) {
                if (s && s.id && s.id === scheduleId) {
                    return true;
                }
            }
        }
        return false;
    }

    public isScheduleAssigned(scheduleId): boolean {
        if (this.weeklySchedules) {
            for (const s of this.weeklySchedules) {
                if (s && s.id && s.id === scheduleId) {
                    return true;
                }
            }
        }
        return false;
    }

    public isScheduleVisible(scheduleId): boolean {
        const schedule = this.scheduleIdMap.get(scheduleId);
        if (schedule) {
            return schedule.visibility === ScheduleDef.VISIBLE;
        }
        return false;
    }

    public loadNonPeriodicScheduleItems() {
        console.log('{ScheduleService}', '[loadNonPeriodicScheduleItems]', 'oneshotTaskNames count =', this.oneshotTaskNames.length);
        this.loadingService.setLoading(true);
        const sub = Observable.of(this.oneshotTaskNames).mergeMap(taskNames => {
            const enabledTaskList = [];
            const obsList = new Array<Observable<any>>();
            for (const taskName of taskNames) {
                obsList.push(this.scsTscService.getEnableFlag(taskName).map((enable) => {
                    if (+enable === 1) {
                        enabledTaskList.push(taskName);
                    }
                    return enable;
                }));
            }
            return Observable.combineLatest(obsList).map(() => enabledTaskList);
        })
        .mergeMap(enabledTaskList => {
            const obsList = new Array<Observable<any>>();
            for (const taskName of enabledTaskList) {
                obsList.push(this.scsTscService.getFilter(taskName).map(filter => filter));
            }
            return Observable.combineLatest(obsList);
        })
        .subscribe(filterList => {
            let taskToRunCnt = 0;
            for (const filter of filterList) {
                const onOffTime = this.getOnOffTime(filter);
                const onOffTimeDisplay = onOffTime;

                const hour = +onOffTime.split(':')[0];
                const minute = +onOffTime.split(':')[1];
                const taskDaygroup: string = filter.split(' ')[0];
                const daygroup = this.dayGroupIdMap.get(taskDaygroup);

                if (daygroup && daygroup.datesList && daygroup.datesList.length > 0) {
                    let dateTime = +daygroup.datesList[0];
                    if (this.tscTimeOffset === 0) {
                        // Handle unix time underflow from tsc server
                        if (dateTime > UtilService.UNIX_TIME_MAX) {
                            dateTime = 0;
                        }
                    } else {
                        dateTime = dateTime - this.tscTimeOffset;
                    }
                    // Check if date/time has expired
                    if (UtilService.isDateExpired(dateTime, hour, minute)) {
                        console.log('{ScheduleService}', '[loadNonPeriodicScheduleItems]', 'task on time has expired');
                    } else {
                        taskToRunCnt++;
                        console.log('{ScheduleService}', '[loadNonPeriodicScheduleItems]', 'task on time scheduled to run at ',
                            onOffTime, taskToRunCnt);
                    }
                }
                console.log('{ScheduleService}', '[loadNonPeriodicScheduleItems]', 'task on time ', onOffTime, 'taskToRunCnt', taskToRunCnt);
            }
            this.oneshotTaskToRunCnt = taskToRunCnt;
            this.updateOneshotOnOffTimeDisplayAll();
            this.updateRunningSchedules();
            this.loadingService.setLoading(false); 
            sub.unsubscribe();
        });
    }

    public clearOnOffTimerMap() {
        console.log('{ScheduleService}', '[clearOnOffTimerMap]');
        this.updateOnOffTimerMap.forEach((subTimer: any, key: string) => {
            subTimer.unsubscribe();
        })
        this.updateOnOffTimerMap.clear();
    }

    public addOnOffUpdateTimer(scheduleItem, taskName, hour, minute) {
        const date = new Date();
        date.setSeconds(0, 0);
        const currentTime = date.getTime();
        const ONE_DAY = 86400000;
        const ONE_MINUTE = 60000;
        date.setHours(hour);
        date.setMinutes(minute);
        if (date.getTime() < currentTime) {
            date.setTime(date.getTime() + ONE_DAY + ONE_MINUTE);
            console.log('{ScheduleService}', '[addOnOffUpdateTimer]', taskName, hour, minute, 'set to next day', date);
        } else {
            date.setTime(date.getTime() + ONE_MINUTE);
            console.log('{ScheduleService}', '[addOnOffUpdateTimer]', taskName, hour, minute, 'set to current day', date);
        }

        const timer = Observable.timer(date);
        const subTimer = timer.subscribe(val => {
            this.updateOneshotOnOffTimeDisplay(scheduleItem);
            subTimer.unsubscribe();
            this.updateOnOffTimerMap.delete(taskName);
            console.log('{ScheduleService}', '[addOnOffUpdateTimer]', 'timer triggered', taskName, date,
                'delete timer - count =', this.updateOnOffTimerMap.size);
        })
        const prevSubTimer = this.updateOnOffTimerMap.get(taskName);
        if (prevSubTimer) {
            prevSubTimer.unsubscribe();
            this.updateOnOffTimerMap.delete(taskName);
        }
        this.updateOnOffTimerMap.set(taskName, subTimer);
        console.log('{ScheduleService}', '[addOnOffUpdateTimer]', 'updateOnOffTimerMap timer - count =', this.updateOnOffTimerMap.size);
    }

    public startRunningStatusUpdateTimer(startDelay: number) {
        const timer = Observable.timer(startDelay, this.runningStatusUpdatePeriod);
        const subTimer = timer.subscribe(val => {
            this.readSchTableRunningStatus();
        })
    }

    public readSchTableRunningStatus() {
        const subGetSchTableRunningStatus = Observable.forkJoin(
            this.schTableTaskNames.map((taskName: any) => {
                return this.scsTscService.getDescription(taskName)
                    .map((desc: any) => {
                        return desc;
                    })
            }))
            .subscribe((data: any[]) => {
                console.log('{ScheduleService}', '[readSchTableRunningStatus]', 'extractSchTableRunningStatus');
                this.extractSchTableRunningStatus(this.schTableTaskNames, data);

                // this.readDayGroupDates();
                this.updateRunningSchedules();

                subGetSchTableRunningStatus.unsubscribe();
            })
    }

    public extractSchTableRunningStatus(taskNames: string[], data: any[]) {
        console.log('{ScheduleService}', '[extractSchTableRunningStatus]', 'begin');
        for (let i = 0; i < taskNames.length; i++) {
            const taskColumns: string[] = taskNames[i].split(',');
            const header = taskColumns[ScheduleDef.SCHEDULE_HEADER_COL];
            const scheduleType = taskColumns[ScheduleDef.SCHEDULE_TYPE_COL];
            const scheduleId = taskColumns[ScheduleDef.SCHEDULE_ID_COL];
            if (header != null && header === ScheduleDef.SCHEDULE_TABLE_HEADER) {
                const schedule = this.schedules.find(s =>
                    s.scheduleType === scheduleType &&
                    s.id === scheduleId);
                if (schedule != null) {
                    const schTableData: string = data[i];
                    const dataColumns = schTableData.split(',');
                    const runningStatus = dataColumns[ScheduleDef.SCHEDULE_RUNNING_STATUE_COL];
                    schedule.runningStatus = runningStatus;
                    console.log('{ScheduleService}', '[extractSchTableRunningStatus]', ' schedule runningStatus updated ',  schedule.runningStatus);
                } else {
                    console.warn('{ScheduleService}', '[extractSchTableRunningStatus]', ' schedule table not found ');
                }
            } else {
                console.error('{ScheduleService}', '[extractSchTableRunningStatus]', 'unrecognized header in taskname', taskNames[i]);
            }
        }
    }

    public startDayGroupUpdateTimer(startTime: Date) {
        const timer = Observable.timer(startTime, this.daygroupUpdatePeriod);
        const subTimer = timer.subscribe(() => {
            this.readDayGroupDates();
        })
    }
}
