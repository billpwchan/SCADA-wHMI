import { Component, OnInit, ElementRef } from '@angular/core';
import { Schedule } from '../type/schedule';
import { ActivatedRoute } from '@angular/router'
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { ScheduleItem } from '../type/schedule-item';
import { ConfigService } from '../service/config.service';
import { ScheduleService } from '../service/schedule.service';
import { UtilService } from '../service/util.service';

@Component({
    selector: 'app-sch-planning',
    templateUrl: './schedule-planning.component.html',
    styleUrls: ['./schedule-planning.component.css'],
    providers: []
})
export class SchedulePlanningComponent implements OnInit {
    public monSchedule: Schedule;
    public tueSchedule: Schedule;
    public wedSchedule: Schedule;
    public thuSchedule: Schedule;
    public friSchedule: Schedule;
    public satSchedule: Schedule;
    public sunSchedule: Schedule;

    public periodicSchedules = Array<Schedule>();
    public weeklySchedules = Array<Schedule>();
    public runningSchedules = Array<Schedule>();
    public runningSchedulesStr: string;

    public defaultWeeklyConfig: any;
    public periodicPlanningDuration: number;

    private subRoute: any;
    private subSchedules: any;
    private subWeeklySchedules: any;
    private subGetRunningSchedules: any;

    constructor(
        private configService: ConfigService,
        private route: ActivatedRoute,
        private scheduleService: ScheduleService,
        private translate: TranslateService) {
            translate.onLangChange.subscribe((event: LangChangeEvent) => {
                this.loadTranslations();
            })
    }

    ngOnInit() {
        this.loadTranslations();
        this.loadConfig();
        this.loadData();
    }

    private loadTranslations() {
    }

    private loadConfig() {
        this.defaultWeeklyConfig = this.configService.config.getIn(['schedule_planning', 'weekly_planning']);
        console.log('{schedule-table}', '[loadConfig]', 'defaultWeeklyConfig=', this.defaultWeeklyConfig);

        this.periodicPlanningDuration = this.configService.config.getIn(['schedule_planning', 'periodic_planning_duration']);
        console.log('{schedule-table}', '[loadConfig]', 'periodicPlanningDuration=', this.periodicPlanningDuration);
    }

    private loadData() {
        this.subRoute = this.route.queryParams.subscribe(params => {

            let clientName = params['clientName'] || this.configService.config.getIn(['default_client_name']);
            console.log('{schedule-planning}', '[loadData]', 'clientName =', clientName);

            this.scheduleService.setClientName(clientName);

            this.getPeriodicSchedules();

            this.getWeeklySchedules();

            this.getRunningSchedules();
        })
    }

    public getPeriodicSchedules() {
        this.subSchedules = this.scheduleService.getSchedulesByPeriodic(true).subscribe(
            periodicSchedules => {
                this.periodicSchedules = periodicSchedules;
            }
        )
    }

    public getWeeklySchedules() {
        this.subWeeklySchedules = this.scheduleService.getWeeklySchedules().subscribe(
            weeklySchedules => {
                this.weeklySchedules = weeklySchedules;
                if (weeklySchedules && weeklySchedules.length > 0) {
                    this.sunSchedule = weeklySchedules[0];
                    this.monSchedule = weeklySchedules[1];
                    this.tueSchedule = weeklySchedules[2];
                    this.wedSchedule = weeklySchedules[3];
                    this.thuSchedule = weeklySchedules[4];
                    this.friSchedule = weeklySchedules[5];
                    this.satSchedule = weeklySchedules[6];
                }
            }
        )
    }

    public getRunningSchedules() {
        this.subGetRunningSchedules = this.scheduleService.getRunningSchedules().subscribe(
            schedules => {
                this.runningSchedules = schedules;
                this.runningSchedulesStr = '';
                if (schedules && schedules.length > 0) {
                    for (let s of schedules) {
                        if (this.runningSchedulesStr.length > 0) {
                            this.runningSchedulesStr = this.runningSchedulesStr + ', ' + s.text;
                        } else {
                            this.runningSchedulesStr = s.text;
                        }
                    }
                }
            }
        )
    }

    public updateWeeklyPlan() {
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'mon =', this.monSchedule.text);
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'tue =', this.tueSchedule.text);
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'wed =', this.wedSchedule.text);
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'thu =', this.thuSchedule.text);
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'fri =', this.friSchedule.text);
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'sat =', this.satSchedule.text);
        console.log('{schedule-planning}', '[updateWeeklyPlan]', 'sun =', this.sunSchedule.text);

        let assignedSchedulesMap = this.getAssignedSchedulesMap();

        for (let s of this.periodicSchedules) {
            if (assignedSchedulesMap.has(s.id)) {
                let currentDate = new Date();
                let nextDate = new Date();
                currentDate.setHours(0, 0, 0, 0);
                nextDate.setTime(nextDate.getTime() + 86400000);
                nextDate.setHours(0, 0, 0, 0);
                let wdayList = assignedSchedulesMap.get(s.id);

                console.log('{schedule-planning}', '[updateWeeklyPlan]', 'current date', currentDate.getFullYear(), currentDate.getMonth()+1, currentDate.getDate(),
                'next date', nextDate.getFullYear(), nextDate.getMonth()+1, nextDate.getDate());

                let datesList = UtilService.getWeekDatesList(wdayList, currentDate, this.periodicPlanningDuration);
                let nextDatesList = UtilService.getWeekNextDatesList(wdayList, nextDate, this.periodicPlanningDuration);

                if (datesList && datesList.length > 0) {
                    this.scheduleService.setSchedulePlanDates(s.id, datesList, nextDatesList);
                } else {
                    console.error('{schedule-planning}', '[updateWeeklyPlan]', 'Error generating date list for schedule', s.scheduleType, s.id);
                }
            } else {
                this.scheduleService.setSchedulePlanDates(s.id, Array<string>(), Array<string>());
            }
        }

        assignedSchedulesMap.forEach((value: number[], key: string) => {
            let currentDate = new Date();
            let nextDate = new Date();
            currentDate.setHours(0, 0, 0, 0);

            console.log('{schedule-planning}', '[updateWeeklyPlan]', 'current date', currentDate.getFullYear(), currentDate.getMonth()+1, currentDate.getDate(),
             'next date', nextDate.getFullYear(), nextDate.getMonth()+1, nextDate.getDate());

            let datesList = UtilService.getWeekDatesList(value, currentDate, this.periodicPlanningDuration);
            let nextDatesList = UtilService.getWeekNextDatesList(value, currentDate, this.periodicPlanningDuration);

            if (datesList && datesList.length > 0) {
                this.scheduleService.setSchedulePlanDates(key, datesList, nextDatesList);
            }
        });
    }

    public getAssignedSchedulesMap() {
        let assignedSchedulesMap = new Map<string, Array<number>>();
        let wSchedules = Array<Schedule>(7);
        wSchedules[0] = this.sunSchedule;
        wSchedules[1] = this.monSchedule;
        wSchedules[2] = this.tueSchedule;
        wSchedules[3] = this.wedSchedule;
        wSchedules[4] = this.thuSchedule;
        wSchedules[5] = this.friSchedule;
        wSchedules[6] = this.satSchedule;


        for (let i=0; i<7; i++) {
            if (assignedSchedulesMap.has(wSchedules[i].id)) {
                assignedSchedulesMap.get(wSchedules[i].id).push(i);
                console.log('{schedule-planning}', '[getAssignedSchedulesMap]', 'assigning weekday', i, 'to', wSchedules[i].id);
            } else {
                assignedSchedulesMap.set(wSchedules[i].id, [i]);
                console.log('{schedule-planning}', '[getAssignedSchedulesMap]', 'assigning weekday', i, 'to', wSchedules[i].id);
            }
        }
        console.log('{schedule-planning}', '[getAssignedSchedulesMap]', 'assignedSchedulesMap', assignedSchedulesMap);
        return assignedSchedulesMap;
    }

    public revertWeeklyPlan() {
        if (this.weeklySchedules && this.weeklySchedules.length > 0) {
            this.monSchedule = this.weeklySchedules[1];
            this.tueSchedule = this.weeklySchedules[2];
            this.wedSchedule = this.weeklySchedules[3];
            this.thuSchedule = this.weeklySchedules[4];
            this.friSchedule = this.weeklySchedules[5];
            this.satSchedule = this.weeklySchedules[6];
            this.sunSchedule = this.weeklySchedules[0];
        }
    }

    public getWeekdaySchedule(weekday: string): Schedule {
        if (this.defaultWeeklyConfig.get(weekday)) {
            console.log('{schedule-planning}', '[defaultsWeeklyPlan]', weekday, this.defaultWeeklyConfig.get(weekday));
            if (this.defaultWeeklyConfig.get(weekday).get("type") && this.defaultWeeklyConfig.get(weekday).get("id")) {
                console.log('{schedule-planning}', '[defaultsWeeklyPlan]', 'periodicSchedules count =', this.periodicSchedules.length);
                let type = this.defaultWeeklyConfig.get(weekday).get("type");
                let id = this.defaultWeeklyConfig.get(weekday).get("id");
                for (let s of this.periodicSchedules) {
                    if (s.scheduleType === type && s.scheduleId === +id) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    public defaultsWeeklyPlan() {
        if (this.defaultWeeklyConfig) {
            this.monSchedule = this.getWeekdaySchedule("0");
            this.tueSchedule = this.getWeekdaySchedule("1");
            this.wedSchedule = this.getWeekdaySchedule("2");
            this.thuSchedule = this.getWeekdaySchedule("3");
            this.friSchedule = this.getWeekdaySchedule("4");
            this.satSchedule = this.getWeekdaySchedule("5");
            this.sunSchedule = this.getWeekdaySchedule("6");
        }
    }

    public startPeriodic() {
        console.log('{schedule-table}', '[startPeriodic]', 'periodicSchedules count =', this.periodicSchedules.length);
        for (let s of this.periodicSchedules) {
            this.scheduleService.startPeriodicSchedule(s.id);
        }
    }

    public stopPeriodic() {
        console.log('{schedule-table}', '[stopPeriodic]', 'periodicSchedules count =', this.periodicSchedules.length);
        for (let s of this.periodicSchedules) {
            this.scheduleService.stopSchedule(s.id);
        }
    }
}
