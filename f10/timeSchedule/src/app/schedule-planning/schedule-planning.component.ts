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
    public periodicScheduleOptions = Array<Schedule>();
    public weeklySchedules = Array<Schedule>();
    public runningSchedules = Array<Schedule>();
    public runningSchedulesStr = '';

    public defaultWeeklyConfig: any;
    public periodicPlanningDuration: number;
    public applyPlanToRunningDayGroup: false;

    private subRoute: any;
    private subSchedules: any;
    private subWeeklySchedules: any;
    private subGetRunningSchedules: any;

    public periodicStarted = false;

    public planModified = false;

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

        this.applyPlanToRunningDayGroup = this.configService.config.getIn(['schedule_planning', 'apply_plan_to_running_daygroup']);
        console.log('{schedule-table}', '[loadConfig]', 'applyPlanToRunningDayGroup=', this.applyPlanToRunningDayGroup);
    }

    private loadData() {
        this.subRoute = this.route.queryParams.subscribe(params => {

            const clientName = params['clientName'] || this.configService.config.getIn(['default_client_name']);
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
                this.periodicScheduleOptions = [];
                if (this.periodicSchedules) {
                    const emptySchedule = new Schedule();
                    const noSchedulePlannedStr = 'No schedule planned';
                    const translatedStr = this.translate.instant(noSchedulePlannedStr);
                    if (translatedStr) {
                        emptySchedule.text = translatedStr;
                    } else {
                        emptySchedule.text = noSchedulePlannedStr;
                    }
                    this.periodicScheduleOptions.push(emptySchedule);
                    for (const s of this.periodicSchedules) {
                        this.periodicScheduleOptions.push(s);
                    }
                }

                this.periodicStarted = this.scheduleService.isPeriodicScheduleStarted();
                console.log('{schedule-planning}', '[getPeriodicSchedules]', 'periodicStarted', this.periodicStarted);
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
                    for (const s of schedules) {
                        if (this.runningSchedulesStr && this.runningSchedulesStr.length > 0) {
                            this.runningSchedulesStr = this.runningSchedulesStr + ', ' + s.text;
                        } else {
                            this.runningSchedulesStr = s.text;
                        }
                    }
                } else {
                    const str = 'No schedule is running';
                    const translatedStr = this.translate.instant(str);
                    if (translatedStr) {
                        this.runningSchedulesStr = translatedStr;
                    } else {
                        this.runningSchedulesStr = str;
                    }
                }
            }
        )
    }

    public updateWeeklyPlan() {
        const assignedSchedulesMap = this.getAssignedSchedulesMap();

        for (const s of this.periodicSchedules) {
            if (assignedSchedulesMap.has(s.id)) {
                const currentDate = new Date();
                const nextDate = new Date();
                currentDate.setHours(0, 0, 0, 0);
                nextDate.setTime(nextDate.getTime() + 86400000);
                nextDate.setHours(0, 0, 0, 0);
                const wdayList = assignedSchedulesMap.get(s.id);

                console.log('{schedule-planning}', '[updateWeeklyPlan]', 'current date', currentDate.getFullYear(), currentDate.getMonth() + 1,
                    currentDate.getDate(), 'next date', nextDate.getFullYear(), nextDate.getMonth() + 1, nextDate.getDate());

                const datesList = UtilService.getWeekDatesList(wdayList, currentDate, this.periodicPlanningDuration);
                const nextDatesList = UtilService.getWeekNextDatesList(wdayList, nextDate, this.periodicPlanningDuration);

                if (datesList && datesList.length > 0) {
                    this.scheduleService.setSchedulePlanDates(s.id, datesList, nextDatesList);
                    if (this.periodicStarted && this.applyPlanToRunningDayGroup) {
                        this.scheduleService.setScheduleRunDates(s.id, datesList, nextDatesList);
                    }
                } else {
                    console.error('{schedule-planning}', '[updateWeeklyPlan]', 'Error generating date list for schedule', s.scheduleType, s.id);
                }
            } else {
                this.scheduleService.setSchedulePlanDates(s.id, Array<string>(), Array<string>());
            }
        }

        assignedSchedulesMap.forEach((value: number[], key: string) => {
            const currentDate = new Date();
            const nextDate = new Date();
            currentDate.setHours(0, 0, 0, 0);

            console.log('{schedule-planning}', '[updateWeeklyPlan]', 'current date', currentDate.getFullYear(), currentDate.getMonth() + 1,
                currentDate.getDate(), 'next date', nextDate.getFullYear(), nextDate.getMonth() + 1, nextDate.getDate());

            const datesList = UtilService.getWeekDatesList(value, currentDate, this.periodicPlanningDuration);
            const nextDatesList = UtilService.getWeekNextDatesList(value, currentDate, this.periodicPlanningDuration);

            if (datesList && datesList.length > 0) {
                this.scheduleService.setSchedulePlanDates(key, datesList, nextDatesList);
                if (this.periodicStarted && this.applyPlanToRunningDayGroup) {
                    this.scheduleService.setScheduleRunDates(key, datesList, nextDatesList);
                }
            }
        });
        this.planModified = false;
    }

    public getAssignedSchedulesMap() {
        const assignedSchedulesMap = new Map<string, number[]>();
        const wSchedules = Array<Schedule>(7);
        wSchedules[0] = this.sunSchedule;
        wSchedules[1] = this.monSchedule;
        wSchedules[2] = this.tueSchedule;
        wSchedules[3] = this.wedSchedule;
        wSchedules[4] = this.thuSchedule;
        wSchedules[5] = this.friSchedule;
        wSchedules[6] = this.satSchedule;

        for (let i = 0; i < 7; i++) {
            if (wSchedules[i] && wSchedules[i].id) {
                if (assignedSchedulesMap.has(wSchedules[i].id)) {
                    assignedSchedulesMap.get(wSchedules[i].id).push(i);
                    console.log('{schedule-planning}', '[getAssignedSchedulesMap]', 'assigning weekday', i, 'to', wSchedules[i].id);
                } else {
                    assignedSchedulesMap.set(wSchedules[i].id, [i]);
                    console.log('{schedule-planning}', '[getAssignedSchedulesMap]', 'assigning weekday', i, 'to', wSchedules[i].id);
                }
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
        this.planModified = false;
    }

    public onChange() {
        console.log('{schedule-planning}', '[onChange]');
        console.log('{schedule-planning}', '[onChange]', 'monSchedule', this.monSchedule, 'weeklySchedules[1]', this.weeklySchedules[1]);
        if ((this.monSchedule && !this.weeklySchedules[1]) || (!this.monSchedule && this.weeklySchedules[1])) {
            this.planModified = true;
        } else if (this.monSchedule && this.weeklySchedules[1] && this.monSchedule.id !== this.weeklySchedules[1].id) {
            this.planModified = true;
        } else if ((this.tueSchedule && !this.weeklySchedules[2]) || (!this.tueSchedule && this.weeklySchedules[2])) {
            this.planModified = true;
        } else if (this.tueSchedule && this.weeklySchedules[2] && this.tueSchedule.id !== this.weeklySchedules[2].id) {
            this.planModified = true;
        } else if ((this.wedSchedule && !this.weeklySchedules[3]) || (!this.wedSchedule && this.weeklySchedules[3])) {
            this.planModified = true;
        } else if (this.wedSchedule && this.weeklySchedules[3] && this.wedSchedule.id !== this.weeklySchedules[3].id) {
            this.planModified = true;
        } else if ((this.thuSchedule && !this.weeklySchedules[4]) || (!this.thuSchedule && this.weeklySchedules[4]))  {
            this.planModified = true;
        } else if (this.thuSchedule && this.weeklySchedules[4] && this.thuSchedule.id !== this.weeklySchedules[4].id) {
            this.planModified = true;
        } else if ((this.friSchedule && !this.weeklySchedules[5]) || (!this.friSchedule && this.weeklySchedules[5])) {
            this.planModified = true;
        } else if (this.friSchedule && this.weeklySchedules[5] && this.friSchedule.id !== this.weeklySchedules[5].id) {
            this.planModified = true;
        } else if ((this.satSchedule && !this.weeklySchedules[6]) || (!this.satSchedule && this.weeklySchedules[6])) {
            this.planModified = true;
        } else if (this.satSchedule && this.weeklySchedules[6] && this.satSchedule.id !== this.weeklySchedules[6].id) {
            this.planModified = true;
        } else if (this.sunSchedule && !this.weeklySchedules[0]) {
            this.planModified = true;
        } else if (this.sunSchedule && this.weeklySchedules[0] && this.sunSchedule.id !== this.weeklySchedules[0].id) {
            this.planModified = true;
        } else {
            this.planModified = false;
        }
        console.log('{schedule-planning}', '[onChange]', 'planModified', this.planModified);
    }

    public getDefaultWeekdaySchedule(weekday: string): Schedule {
        if (this.defaultWeeklyConfig.get(weekday)) {
            console.log('{schedule-planning}', '[getDefaultWeekdaySchedule]', weekday, this.defaultWeeklyConfig.get(weekday));
            if (this.defaultWeeklyConfig.get(weekday).get('type') && this.defaultWeeklyConfig.get(weekday).get('id')) {
                console.log('{schedule-planning}', '[getDefaultWeekdaySchedule]', 'periodicSchedules count =', this.periodicSchedules.length);
                const type = this.defaultWeeklyConfig.get(weekday).get('type');
                const id = this.defaultWeeklyConfig.get(weekday).get('id');
                if (type && id) {
                    for (const s of this.periodicSchedules) {
                        if (s.scheduleType === type && s.scheduleId === +id) {
                            if (this.scheduleService.isScheduleVisible(s.id)) {
                                console.log('{schedule-planning}', '[getDefaultWeekdaySchedule]', 'schedule is visible', s.id);
                                return s;
                            } else {
                                console.log('{schedule-planning}', '[getDefaultWeekdaySchedule]', 'schedule is invisible', s.id);
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public defaultsWeeklyPlan() {
        if (this.defaultWeeklyConfig) {
            this.monSchedule = this.getDefaultWeekdaySchedule('1');
            this.tueSchedule = this.getDefaultWeekdaySchedule('2');
            this.wedSchedule = this.getDefaultWeekdaySchedule('3');
            this.thuSchedule = this.getDefaultWeekdaySchedule('4');
            this.friSchedule = this.getDefaultWeekdaySchedule('5');
            this.satSchedule = this.getDefaultWeekdaySchedule('6');
            this.sunSchedule = this.getDefaultWeekdaySchedule('0');
        }
        this.onChange();
    }

    public startPeriodic() {
        console.log('{schedule-table}', '[startPeriodic]', 'periodicSchedules count =', this.periodicSchedules.length);

        this.scheduleService.startPeriodicSchedules();

        this.periodicStarted = true;
        console.log('{schedule-planning}', '[startPeriodic]', 'periodicStarted', this.periodicStarted);
    }

    public stopPeriodic() {
        console.log('{schedule-table}', '[stopPeriodic]', 'periodicSchedules count =', this.periodicSchedules.length);

        this.scheduleService.stopPeriodicSchedules();

        this.periodicStarted = false;
        console.log('{schedule-planning}', '[stopPeriodic]', 'periodicStarted', this.periodicStarted);
    }
}
