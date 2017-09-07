import { Component, OnInit, ElementRef } from '@angular/core';
import { Schedule } from '../type/schedule';
import { ActivatedRoute } from '@angular/router'
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { ScheduleItem } from '../type/schedule-item';
import { ConfigService } from '../service/config.service';
import { ScheduleService } from '../service/schedule.service';
import { UtilService } from '../service/util.service';
import { WeekdayDef } from './weekday-def';

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

    public displayAppNavigation = false;

    public manualRefreshEnabled = false;

    public displayCutoffTime = false;

    public displayRunningSchedules = true;

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
        console.log('{schedule-planning}', '[loadConfig]', 'defaultWeeklyConfig=', this.defaultWeeklyConfig);

        this.periodicPlanningDuration = this.configService.config.getIn(['schedule_planning', 'periodic_planning_duration']);
        console.log('{schedule-planning}', '[loadConfig]', 'periodicPlanningDuration=', this.periodicPlanningDuration);

        this.applyPlanToRunningDayGroup = this.configService.config.getIn(['schedule_planning', 'apply_plan_to_running_daygroup']);
        console.log('{schedule-planning}', '[loadConfig]', 'applyPlanToRunningDayGroup=', this.applyPlanToRunningDayGroup);

        this.displayAppNavigation = this.configService.config.getIn(['schedule_planning', 'display_app_navigation']);
        console.log('{schedule-planning}', '[loadConfig]', 'display_app_navigation=', this.displayAppNavigation);

        this.manualRefreshEnabled = this.configService.config.getIn(['schedule_planning', 'manual_refresh_enabled']);
        console.log('{schedule-planning}', '[loadConfig]', 'manual_refresh_enabled=', this.manualRefreshEnabled);

        this.displayCutoffTime = this.configService.config.getIn(['schedule_planning', 'display_cutoff_time']);
        console.log('{schedule-planning}', '[loadConfig]', 'display_cutoff_time=', this.displayCutoffTime);

        this.displayRunningSchedules = this.configService.config.getIn(['schedule_planning', 'display_running_schedules']);
        console.log('{schedule-planning}', '[loadConfig]', 'display_running_schedules=', this.displayRunningSchedules);
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
                    this.sunSchedule = weeklySchedules[WeekdayDef.SUNDAY];
                    this.monSchedule = weeklySchedules[WeekdayDef.MONDAY];
                    this.tueSchedule = weeklySchedules[WeekdayDef.TUESDAY];
                    this.wedSchedule = weeklySchedules[WeekdayDef.WEDNESDAY];
                    this.thuSchedule = weeklySchedules[WeekdayDef.THURSDAY];
                    this.friSchedule = weeklySchedules[WeekdayDef.FRIDAY];
                    this.satSchedule = weeklySchedules[WeekdayDef.SATURDAY];
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
                if (this.periodicStarted && this.applyPlanToRunningDayGroup) {
                    this.scheduleService.setScheduleRunDates(s.id, Array<string>(), Array<string>());
                }
            }
        }

//        if (this.periodicStarted && this.applyPlanToRunningDayGroup) {
            this.scheduleService.loadData();
//        }
        this.planModified = false;
    }

    public getAssignedSchedulesMap() {
        const assignedSchedulesMap = new Map<string, number[]>();
        const wSchedules = Array<Schedule>(7);
        wSchedules[WeekdayDef.SUNDAY] = this.sunSchedule;
        wSchedules[WeekdayDef.MONDAY] = this.monSchedule;
        wSchedules[WeekdayDef.TUESDAY] = this.tueSchedule;
        wSchedules[WeekdayDef.WEDNESDAY] = this.wedSchedule;
        wSchedules[WeekdayDef.THURSDAY] = this.thuSchedule;
        wSchedules[WeekdayDef.FRIDAY] = this.friSchedule;
        wSchedules[WeekdayDef.SATURDAY] = this.satSchedule;

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
            this.monSchedule = this.weeklySchedules[WeekdayDef.MONDAY];
            this.tueSchedule = this.weeklySchedules[WeekdayDef.TUESDAY];
            this.wedSchedule = this.weeklySchedules[WeekdayDef.WEDNESDAY];
            this.thuSchedule = this.weeklySchedules[WeekdayDef.THURSDAY];
            this.friSchedule = this.weeklySchedules[WeekdayDef.FRIDAY];
            this.satSchedule = this.weeklySchedules[WeekdayDef.SATURDAY];
            this.sunSchedule = this.weeklySchedules[WeekdayDef.SUNDAY];
        }
        this.planModified = false;
    }

    public onChange() {
        if ((this.monSchedule && !this.weeklySchedules[WeekdayDef.MONDAY]) || (!this.monSchedule && this.weeklySchedules[WeekdayDef.MONDAY])) {
            this.planModified = true;
        } else if (this.monSchedule && this.weeklySchedules[WeekdayDef.MONDAY] && this.monSchedule.id !== this.weeklySchedules[WeekdayDef.MONDAY].id) {
            this.planModified = true;
        } else if ((this.tueSchedule && !this.weeklySchedules[WeekdayDef.TUESDAY]) || (!this.tueSchedule && this.weeklySchedules[WeekdayDef.TUESDAY])) {
            this.planModified = true;
        } else if (this.tueSchedule && this.weeklySchedules[WeekdayDef.TUESDAY] && this.tueSchedule.id !== this.weeklySchedules[WeekdayDef.TUESDAY].id) {
            this.planModified = true;
        } else if ((this.wedSchedule && !this.weeklySchedules[WeekdayDef.WEDNESDAY]) || (!this.wedSchedule && this.weeklySchedules[WeekdayDef.WEDNESDAY])) {
            this.planModified = true;
        } else if (this.wedSchedule && this.weeklySchedules[WeekdayDef.WEDNESDAY] && this.wedSchedule.id !== this.weeklySchedules[WeekdayDef.WEDNESDAY].id) {
            this.planModified = true;
        } else if ((this.thuSchedule && !this.weeklySchedules[WeekdayDef.THURSDAY]) || (!this.thuSchedule && this.weeklySchedules[WeekdayDef.THURSDAY]))  {
            this.planModified = true;
        } else if (this.thuSchedule && this.weeklySchedules[WeekdayDef.THURSDAY] && this.thuSchedule.id !== this.weeklySchedules[WeekdayDef.THURSDAY].id) {
            this.planModified = true;
        } else if ((this.friSchedule && !this.weeklySchedules[WeekdayDef.FRIDAY]) || (!this.friSchedule && this.weeklySchedules[WeekdayDef.FRIDAY])) {
            this.planModified = true;
        } else if (this.friSchedule && this.weeklySchedules[WeekdayDef.FRIDAY] && this.friSchedule.id !== this.weeklySchedules[WeekdayDef.FRIDAY].id) {
            this.planModified = true;
        } else if ((this.satSchedule && !this.weeklySchedules[WeekdayDef.SATURDAY]) || (!this.satSchedule && this.weeklySchedules[WeekdayDef.SATURDAY])) {
            this.planModified = true;
        } else if (this.satSchedule && this.weeklySchedules[WeekdayDef.SATURDAY] && this.satSchedule.id !== this.weeklySchedules[WeekdayDef.SATURDAY].id) {
            this.planModified = true;
        } else if ((this.sunSchedule && !this.weeklySchedules[WeekdayDef.SUNDAY]) || (!this.sunSchedule && this.weeklySchedules[WeekdayDef.SUNDAY])) {
            this.planModified = true;
        } else if (this.sunSchedule && this.weeklySchedules[WeekdayDef.SUNDAY] && this.sunSchedule.id !== this.weeklySchedules[WeekdayDef.SUNDAY].id) {
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
                        if (s.scheduleType === type && s.id === id) {
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
            this.monSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.MONDAY.toString());
            this.tueSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.TUESDAY.toString());
            this.wedSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.WEDNESDAY.toString());
            this.thuSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.THURSDAY.toString());
            this.friSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.FRIDAY.toString());
            this.satSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.SATURDAY.toString());
            this.sunSchedule = this.getDefaultWeekdaySchedule(WeekdayDef.SUNDAY.toString());
        }
        this.onChange();
    }

    public startPeriodic() {
        console.log('{schedule-planning}', '[startPeriodic]', 'periodicSchedules count =', this.periodicSchedules.length);

        this.scheduleService.startPeriodicSchedules();

        this.periodicStarted = true;
        console.log('{schedule-planning}', '[startPeriodic]', 'periodicStarted', this.periodicStarted);
    }

    public stopPeriodic() {
        console.log('{schedule-planning}', '[stopPeriodic]', 'periodicSchedules count =', this.periodicSchedules.length);

        this.scheduleService.stopPeriodicSchedules();

        this.periodicStarted = false;
        console.log('{schedule-planning}', '[stopPeriodic]', 'periodicStarted', this.periodicStarted);
    }

    public onRefresh() {
        window.location.reload();
    }
}
