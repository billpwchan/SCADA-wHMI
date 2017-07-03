import { Injectable, OnDestroy } from '@angular/core';

import 'rxjs/add/operator/toPromise';
import { Observable, BehaviorSubject } from 'rxjs/Rx';

import { ScsTscService } from './scs-tsc.service';
import { ConfigService } from '../service/config.service';
import { Schedule } from '../type/schedule';
import { ScheduleItem } from '../type/schedule-item';


const SCHEDULE_HEADER_COL: number = 0;
const SCHEDULE_TYPE_COL: number = 1;
const SCHEDULE_ID_COL: number = 2;
const SCHEDULE_FUNC_COL: number = 3;
const SCHEDULE_GEO_COL: number = 4;
const SCHEDULE_EQT_LABEL_COL: number = 5;
const SCHEDULE_EQT_ALIAS_COL: number = 6;
const SCHEDULE_EQT_POINT_ATT_COL: number = 7;
const SCHEDULE_EQT_TARGET_STATE: number = 8;

const SCHEDULE_TABLE_HEADER: string = "sch_table";
const SCHEDULE_TASK_HEADER: string = "sch_task";

const SCHEDULE_TABLE_TYPE_DESC_COL: number = 0;
const SCHEDULE_TABLE_TITLE_COL: number = 1;
const SCHEDULE_RUNNING_STATUE_COL: number = 2;
const SCHEDULE_VISIBILITY_COL: number = 3;
const SCHEDULE_PERIODIC_COL: number = 4;
const SCHEDULE_TITLE_READ_ONLY_COL: number = 5;
const SCHEDULE_TIME_READ_ONLY_COL: number = 6;
const SCHEDULE_EQT_LIST_READ_ONLY_COL: number = 7;


@Injectable()
export class ScheduleService {

    // schedule task cut off time
	private cutoffTime: string;

	// display offset in on/off time
	private displayOffset: boolean = true;

    // array to store all schedule tables
    private schedules: Schedule[] = [];

    private schedulesByType: Schedule[] = [];

    // array to store all schedule tasks
    private scheduleItems: ScheduleItem[] = [];

    private scheduleItemsByType: ScheduleItem[] = [];

    private scheduleTypeFilter: string;

    private subGetTaskNames: any;

    private subGetDescFilterEnable: any;

    private subSetFilter: any;

    private subSetTitle: any;

    private subEnableTask: any;

    private subDisableTask: any;

    private currentScheduleType: string = "";

    private subjSchedulesByType = new BehaviorSubject(this.schedulesByType);

    private subjScheduleItemsByType = new BehaviorSubject(this.scheduleItemsByType);

    constructor(
        private configService: ConfigService, private scsTscService: ScsTscService
    ) { }

    ngOnDestroy() {
        this.subGetTaskNames.unsubscribe();
        this.subGetDescFilterEnable.unsubscribe();
        this.subSetFilter.unsubscribe();
        this.subSetTitle.unsubscribe();
        this.subEnableTask.unsubscribe();
        this.subDisableTask.unsubscribe();
    }

    public load() {

        this.loadConfig();

        this.readTscTasks();
    }

    loadConfig() {
		this.cutoffTime = this.configService.config.getIn(['schedule_table', 'cutoff_time']);
		console.info('{schedule-table}', '[loadConfig]', 'cutoff_time=', this.cutoffTime);
		
		this.displayOffset = this.configService.config.getIn(['schedule_table', 'show_cutoff_offset']);
		console.info('{schedule-table}', '[loadConfig]', 'show_cutoff_offset=', this.displayOffset);
	}

    private readTscTasks() {
        console.debug('{ScheduleService}', '[readTscTasks]', 'begin');

        this.subGetTaskNames = this.scsTscService.getTaskNames().subscribe(
            (taskNames: any[]) => { 
				console.debug('{ScheduleService}', '[readTscTasks]', taskNames);

				this.extractScheduleTasks(taskNames);

                this.subGetDescFilterEnable = this.scsTscService.getDescFilterEnable(taskNames).subscribe(
                    (data: any[]) => {
                        this.extractDescFilterEnable(taskNames, data);

                        this.updateSchedulesByType();

                        this.updateScheduleItemsByType();
                    }
                )
            }
        )
        console.debug('{ScheduleService}', '[readTscTasks]', 'end');
    }

    private extractScheduleTasks(taskNames: string[]) {
		console.debug('{ScheduleService}', '[extractScheduleTasks]', 'begin', 'taskName count=', taskNames.length);
		for (let i=0; i<taskNames.length; i++) {
			let columns: string[] = taskNames[i].split(',');

			let header = columns[SCHEDULE_HEADER_COL];

			if (header != null && header == SCHEDULE_TABLE_HEADER) {

                this.extractScheduleTable(columns);
				
			} else if (header != null && header == SCHEDULE_TASK_HEADER) {

                this.extractScheduleTask(taskNames[i], columns);
            } else {

                console.error('{ScheduleService}', '[extractScheduleTasks]', 'unrecognized header in taskname', taskNames[i]);
            }
		}
        console.debug('{ScheduleService}', '[extractScheduleTasks]', 'end');
	}

    private extractScheduleTable(columns: string[]) {
        console.debug('{ScheduleService}', '[extractScheduleTable]', 'begin');

        let scheduleType = columns[SCHEDULE_TYPE_COL];
        let scheduleId = +columns[SCHEDULE_ID_COL];

        let sch = this.schedules.find( r =>
            r.scheduleType == scheduleType &&
            r.scheduleId == scheduleId);
        if (sch == null) {
            sch = new Schedule;
            sch.scheduleType = scheduleType;
            sch.scheduleId = scheduleId;
            sch.id = scheduleType + scheduleId;

            this.schedules.push(sch);
        }
        console.debug('{ScheduleService}', '[extractScheduleTable]', 'end');
    }

    private extractScheduleTask(taskName: string, columns: string[]) {
        console.debug('{ScheduleService}', '[extractScheduleTask]', 'begin');

		for (let i=0; i<columns.length; i++) {
            // extract fields from task name
            let scheduleType = columns[SCHEDULE_TYPE_COL];
            let scheduleId = +columns[SCHEDULE_ID_COL];
            let eqtAlias = columns[SCHEDULE_EQT_ALIAS_COL];
            let eqtPointAtt = columns[SCHEDULE_EQT_POINT_ATT_COL];            
            let targetState = columns[SCHEDULE_EQT_TARGET_STATE];

            let rowFound = this.scheduleItems.find( r =>  
                r.scheduleType == scheduleType &&
                r.scheduleId == scheduleId &&
                r.eqtAlias == eqtAlias &&
                r.eqtPointAtt == eqtPointAtt);

            if (!rowFound) {
                let key = this.scheduleItems.length;
                let eqtLabel = columns[SCHEDULE_EQT_LABEL_COL];
                let funcCat = +columns[SCHEDULE_FUNC_COL];
                let geoCat = +columns[SCHEDULE_GEO_COL];

                let row = this.createScheduleItem(key, scheduleType, scheduleId, eqtLabel, eqtAlias, eqtPointAtt,
                                                    funcCat, geoCat, targetState, taskName);
                this.scheduleItems.push(row);
            } else {
                if (targetState == 'OFF') {
                    rowFound.taskName2 = taskName;
                } else {
                    rowFound.taskName1 = taskName;
                }
            }
		}
		console.debug('{ScheduleService}', '[extractScheduleTask]', 'end');
    }

    private createScheduleItem(key: number, scheduleType:string, scheduleId:number, eqtLabel:string, eqtAlias:string,
									eqtPointAtt:string, funcCat:number, geoCat:number, targetState:string, taskName:string): ScheduleItem {
		console.debug('{ScheduleService}', '[createScheduleItem]', 'begin');
		let t = new ScheduleItem();

		t.scheduleType = scheduleType;
		t.scheduleId = scheduleId;
        t.scheduleKey = scheduleType + scheduleId;
		t.eqtLabel = eqtLabel;
		t.eqtDescription = "N/A";
		t.eqtAlias = eqtAlias;
		t.eqtPointAtt = eqtPointAtt;
		t.funcCat = funcCat;
		t.geoCat = geoCat;
		t.onTime = "N/A";
		t.offTime = "N/A";
		t.enableFlag1 = 0;
        t.enableFlag2 = 0;
		if (targetState == 'OFF') {
			t.taskName2 = taskName;
		} else {
			t.taskName1 = taskName;
		}

		return t;
	}

    private extractDescFilterEnable(taskNames: string[], data: any[]) {
        console.debug('{ScheduleService}', '[extractDescFilterEnable]', 'begin');

        for (let i=0; i<taskNames.length; i++) {
			let taskColumns: string[] = taskNames[i].split(',');

			let header = taskColumns[SCHEDULE_HEADER_COL];
            let scheduleType = taskColumns[SCHEDULE_TYPE_COL];
            let scheduleId = +taskColumns[SCHEDULE_ID_COL];

			if (header != null && header == SCHEDULE_TABLE_HEADER) {

//                console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule table process ', taskNames[i]);

                let schedule = this.schedules.find(s =>
                    s.scheduleType == scheduleType &&
                    s.scheduleId == scheduleId);
                if (schedule != null) {
                    let schTableData: string = data[3*i];
                    let dataColumns = schTableData.split(',');

                    let scheduleDescription = dataColumns[SCHEDULE_TABLE_TYPE_DESC_COL];
                    let scheduleTitle = dataColumns[SCHEDULE_TABLE_TITLE_COL];
                    let runningStatus = dataColumns[SCHEDULE_RUNNING_STATUE_COL];
                    let visibility = dataColumns[SCHEDULE_VISIBILITY_COL];
                    let periodic = dataColumns[SCHEDULE_PERIODIC_COL];
                    let titleReadOnly = dataColumns[SCHEDULE_TITLE_READ_ONLY_COL];
                    let timeReadOnly = dataColumns[SCHEDULE_TIME_READ_ONLY_COL];
                    let eqtListReadOnly = dataColumns[SCHEDULE_EQT_LIST_READ_ONLY_COL];

                    schedule.scheduleDescription = scheduleDescription;
                    schedule.text = scheduleTitle;
                    schedule.runningStatus = runningStatus;
                    schedule.visibility = visibility;
                    schedule.periodic = (periodic=='true');
                    schedule.titleReadOnly = (titleReadOnly=='true');
                    schedule.timeReadOnly = (timeReadOnly=='true');
                    schedule.eqtListReadOnly = (eqtListReadOnly=='true');
                    schedule.taskName = taskNames[i];

                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule description updated',  schedule.scheduleDescription);
                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule title updated ',  schedule.text);
                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule runningStatus updated ',  schedule.runningStatus);
                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule periodic updated ', schedule.periodic);
                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule titleReadOnly updated ', schedule.titleReadOnly);
                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule timeReadOnly updated ', schedule.timeReadOnly);
                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule eqtListReadOnly updated ', schedule.eqtListReadOnly);

                    console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule table updated ');
                } else {
                    console.warn('{ScheduleService}', '[extractDescFilterEnable]', ' schedule table not found ');
                }
				
			} else if (header != null && header == SCHEDULE_TASK_HEADER) {

//                console.debug('{ScheduleService}', '[extractDescFilterEnable]', ' schedule task process ', taskNames[i]);

                let alias = taskColumns[SCHEDULE_EQT_ALIAS_COL];
                let pointAtt = taskColumns[SCHEDULE_EQT_POINT_ATT_COL];
                let targetState = taskColumns[SCHEDULE_EQT_TARGET_STATE];
                let filter = data[3*i + 1];
                let enableFlag = +data[3*i + 2];

                let scheduleItem = this.scheduleItems.find( r =>
					r.scheduleType == scheduleType &&
					r.scheduleId == scheduleId &&
					r.eqtAlias == alias &&
					r.eqtPointAtt == pointAtt);
				if (scheduleItem != null) {
					scheduleItem.eqtDescription = data[3*i];

                    let onOffTime = this.getOnOffTime(filter);
                    if (enableFlag == 0) {
                        console.debug('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag is 0');
                        onOffTime = "";
                    } else {
                        console.debug('{ScheduleService}', '[extractDescFilterEnable]', 'enableFlag is 1');
                    }

                    if (targetState == 'ON') {
                        scheduleItem.onTime = onOffTime;
                        scheduleItem.filter1 = filter;
                        scheduleItem.enableFlag1 = enableFlag;
                    } else if (targetState == 'OFF') {
                        scheduleItem.offTime = onOffTime;
                        scheduleItem.filter2 = filter;
                        scheduleItem.enableFlag2 = enableFlag;
                    }

					console.debug('{ScheduleService}', '[extractDescFilterEnable]', 'update scheduleItem description,filter,enableFlag ', scheduleItem.eqtDescription, filter, enableFlag);
				} else {
					console.warn('{ScheduleService}', '[extractDescFilterEnable]', 'scheduleItem not found ', scheduleType, scheduleId, alias, pointAtt);
				}
                
            } else {

                console.error('{ScheduleService}', '[extractDescFilterEnable]', 'unrecognized header in taskname', taskNames[i]);
            }
		}

        console.debug('{ScheduleService}', '[extractDescFilterEnable]', 'end');
    }

    private getOnOffTime(filter: string) {
        let dg_time: string[];
        dg_time = filter.split(' ');

        let hh: string = dg_time[4];
        let mm: string = dg_time[5];
        let hr: number = +hh;
        let min: number = +mm;
        let hhmm:string;

        if (hh.length == 1) {
            hh = '0' + hh;
        }
        if (mm.length == 1) {
            mm = '0' + mm;
        }
            
        if (this.showNextDayOffset(hr, min)) {
            hhmm = (hr + 24) + ':' + mm;
        } else {
            hhmm = hh + ':' + mm;
        }

        console.debug('{schedule-table}', '[getOnOffTime]', 'hhmm=', hhmm);

        return hhmm;
    }

    public getScheduleItems(scheduleType: string) : Observable<any> {
        this.updateScheduleItemsByType();

        return this.subjScheduleItemsByType;
    }

    public updateScheduleItemsByType() {
        console.debug('{ScheduleService}', '[updateScheduleItemsByType]', 'scheduleItems count=', this.scheduleItems.length)
        this.scheduleItemsByType = [];
        for (let s of this.scheduleItems) {
            if(s.scheduleType == this.currentScheduleType) {
                this.scheduleItemsByType.push(s);
            }
        }
        this.subjScheduleItemsByType.next(this.scheduleItemsByType);
    }

    public getSchedules(scheduleType: string): Observable<any> {
        this.currentScheduleType = scheduleType;
        this.updateSchedulesByType();

        return this.subjSchedulesByType;
    }

    public updateSchedulesByType() {
        console.debug('{ScheduleService}', '[updateSchedulesByType]', 'schedules count=', this.schedules.length)
        this.schedulesByType = [];
        for (let s of this.schedules) {
            if(s.scheduleType == this.currentScheduleType) {
                this.schedulesByType.push(s);
            }
        }
        this.subjSchedulesByType.next(this.schedulesByType);
    }

    public setFilter(taskName: string, filter: string) {
        if (this.subSetFilter) {
            this.subSetFilter.unsubscribe();
        }
        this.subSetFilter = this.scsTscService.setFilter(taskName, filter).subscribe(res => 
            console.debug('{ScheduleService}', '[setFilter]', res));
    }

    private showNextDayOffset(hr, min) {
		console.debug('{ScheduleService}', '[showNextDayOffset]', 'begin');
		if (this.displayOffset) {
			let cutoffHr = +this.cutoffTime.split(':')[0];
			let cutoffMin = +this.cutoffTime.split(':')[1];

			if (hr < cutoffHr || (hr == cutoffHr && min < cutoffMin)) {
				return true;
			}
		}
		return false;
	}

    public setScheduleTitle(id: string, scheduleTitle: string) {
        console.debug('{ScheduleService}', '[setScheduleTitle]', id, scheduleTitle);
        let schedule = this.schedules.find(s => s.id == id);
        if (schedule) {
            let desc = schedule.toString();
            let taskName = schedule.taskName;

            schedule.text = scheduleTitle;

            if (this.subSetTitle) {
                this.subSetTitle.unsubscribe();
            }

            this.subSetTitle = this.scsTscService.setDescription(taskName, desc).subscribe(res =>
                console.debug('{ScheduleService}', '[setScheduleTitle]', res));
        } else {
            console.warn('{ScheduleService}', '[setScheduleTitle]', 'schedule not found', id);
        }
    }

    public enableTask(taskName: string) {
        this.subEnableTask = this.scsTscService.enableTask(taskName, 1).subscribe(res =>
                console.debug('{ScheduleService}', '[enableTask]', res));
    }

    public disableTask(taskName: string) {
        this.subDisableTask = this.scsTscService.enableTask(taskName, 0).subscribe(res =>
                console.debug('{ScheduleService}', '[disableTask]', res));
    }

    // public updateOnOffTime(taskNames: string[]) {
    //     this.subGetFilters = this.scsTscService.getFilters(taskNames).subscribe(res =>
    //         console.debug('{ScheduleService}', '[disableTask]', res));
    // }
}