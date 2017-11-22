import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router'
import { SelectComponent } from 'ng2-select';
import { DatatableComponent } from '../../../node_modules/@swimlane/ngx-datatable/src/components/datatable.component';
import { sortRows } from '../../../node_modules/@swimlane/ngx-datatable/src/utils';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { Schedule } from '../type/schedule';
import { ScheduleItem, ScheduleItemFilter } from '../type/schedule-item';
import { ConfigService } from '../service/config.service';
import { ScheduleService } from '../service/schedule.service';
import { UtilService } from '../service/util.service';
@Component({
    selector: 'app-sch-table',
    templateUrl: './schedule-table.component.html',
    styleUrls: ['./schedule-table.component.css']
})
export class ScheduleTableComponent implements OnInit, OnDestroy {
    // properties for ng2-select
    public _disabledV = '0';
    public selectScheduleDisabled = false;
    public activeItem;
    // properties for ngx-datatable
    public messages = {};
    public defaultSorts = [];
    public pageSize = 5;
    public pageOffset = 0;

    // subscriptions
    private subRoute: any;
    private subSchedules: any;
    private subScheduleItems: any;
    // display periodic
    public displayPeriodicSchedules = false;
    // stores schedules returned from ScheduleService
    public schedules: Schedule[] = [];
    // stores scheduleItems filtered by type
    public scheduleItems: ScheduleItem[] = [];
    // cache copy of scheduleItems
    private cachedSchItems: ScheduleItem[] = [];
    private tempSchItems: ScheduleItem[] = [];
    @ViewChild('scheduleTable') dataTable: DatatableComponent;
    // selected schedule
    public selectedSchedule: Schedule;
    // selected row in table
    public selectedRow = [];
    public selectedScheduleItem: ScheduleItem;
    public selectedItemDisplay = '';
    public selectedOnTime = '';
    public selectedOffTime = '';
    // flag to display 'Add' button
    public addScheduleEnabled = false;
    // flag to display 'Delete' button
    public deleteScheduleEnabled = false;
    // flag to display 'Rename' button
    public renameScheduleEnabled = false;
    // userdefined schedules visible count
    public visibleUserDefinedCnt = 0;
    public maxUserDefinedScheduleCount = 0;

    public unusedSchedules = [];
    public newSchedule: Schedule;

    // place holder to store title typed by operator
    public newScheduleTitle: string;
    // flags to display button to enable/disable task
    public displayEnableOnTime = false;
    public displayDisableOnTime = false;
    public displayEnableOffTime = false;
    public displayDisableOffTime = false;
    public addScheduleClicked = false;
    public renameScheduleClicked = false;

    public pendingOnTimeIsEnabled = false;
    public pendingOffTimeIsEnabled = false;

    public onTimeNotAvailable = true;
    public offTimeNotAvailable = true;

    public newOnTime = '';
    public newOffTime = '';

    public inputIsModified = false;
    public newOnTimeValid = true;
    public newOffTimeValid = true;
    public inputTimeValid = true;

    public newScheduleTitleValid = true;
    public newScheduleTitleModified = false;

    public maxTitleLength = 40;

    // string to represent unavailable task
    private unavailableOnOffTime = 'N/A';
    // string to represent inhibited time
    private inhibitedOnOffTime = '';
    // schedule task cut off time
    public cutoffTime: string;

    // ScheduleItem filter list including online/offline filters
    private schItemFilters: ScheduleItemFilter [];

    // offline filter config
    private offlineFilters: Map<string, string>;

    // offline sort config
    private offlineSort: any;

    private sortEvent: any;
    private sortColumns = [ {prop: 'geoCat'}, {prop: 'funcCat'}];

    public runningSchedules = Array<Schedule>();
    public runningSchedulesStr = '';

    private subGetRunningSchedules: any;

    public oneshotStarted = false;

    public periodicStarted = false;

    public displayAppNavigation = false;

    public manualRefreshEnabled = false;

    public displayCutoffTime = true;

    public displayCutoffTimePeriodic = true;

    public displayCutoffTimeNonPeriodic = false;

    public displayRunningSchedules = true;

    public displaySpinner = true;

    public equipmentTaskSeparator = '-';

    public geocatTranslationPrefix = 'geocat';

    public funcatTranslationPrefix = 'funcat';

    public displayOtherTypesInRunningSchedules = true;

    public disableSchedulePlanning = false;

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
    loadTranslations() {
        this.messages['emptyMessage'] = this.translate.instant('No item to display');
        this.messages['selectedMessage'] = this.translate.instant('selected');
        this.messages['totalMessage'] = this.translate.instant('total');
    }
    loadConfig() {
        console.log('{schedule-table}', '[loadConfig]', 'translate current lang=', this.translate.currentLang);

        this.disableSchedulePlanning = this.configService.config.getIn(['disable_periodic_schedule_planning']);

        this.cutoffTime = this.configService.config.getIn(['schedule_table', 'cutoff_time']);
        console.log('{schedule-table}', '[loadConfig]', 'cutoff_time=', this.cutoffTime);

        this.unavailableOnOffTime = this.configService.config.getIn(['schedule_table', 'unavailable_on_off_time']);
        console.log('{schedule-table}', '[loadConfig]', 'unavailable_on_off_time=', this.unavailableOnOffTime);

        this.inhibitedOnOffTime = this.configService.config.getIn(['schedule_table', 'inhibited_on_off_time']);
        console.log('{schedule-table}', '[loadConfig]', 'inhibited_on_off_time=', this.inhibitedOnOffTime);

        this.maxUserDefinedScheduleCount = this.configService.config.getIn(['schedule_table', 'max_userdefined_schedule_count']);
        console.log('{schedule-table}', '[loadConfig]', 'max-userdefined-schedule-count=', this.maxUserDefinedScheduleCount);

        this.offlineFilters = this.configService.config.getIn(['schedule_table', 'filter']);
        console.log('{schedule-table}', '[loadConfig]', 'filter=', this.offlineFilters);

        this.offlineSort = this.configService.config.getIn(['schedule_table', 'sort']);
        console.log('{schedule-table}', '[loadConfig]', 'sort=', this.offlineSort);

        this.maxTitleLength = this.configService.config.getIn(['schedule_table', 'max_title_length']);
        console.log('{schedule-table}', '[loadConfig]', 'maxTitleLength =', this.maxTitleLength);

        this.displayAppNavigation = this.configService.config.getIn(['schedule_table', 'display_app_navigation']);
        console.log('{schedule-table}', '[loadConfig]', 'display_app_navigation=', this.displayAppNavigation);

        this.manualRefreshEnabled = this.configService.config.getIn(['schedule_table', 'manual_refresh_enabled']);
        console.log('{schedule-table}', '[loadConfig]', 'manual_refresh_enabled=', this.manualRefreshEnabled);

        this.displayCutoffTimePeriodic = this.configService.config.getIn(['schedule_table', 'display_cutoff_time_periodic']);
        console.log('{schedule-table}', '[loadConfig]', 'display_cutoff_time_periodic=', this.displayCutoffTimePeriodic);

        this.displayCutoffTimeNonPeriodic = this.configService.config.getIn(['schedule_table', 'display_cutoff_time_non_periodic']);
        console.log('{schedule-table}', '[loadConfig]', 'display_cutoff_time_non_periodic=', this.displayCutoffTimeNonPeriodic);

        this.displayRunningSchedules = this.configService.config.getIn(['schedule_table', 'display_running_schedules']);
        console.log('{schedule-table}', '[loadConfig]', 'display_running_schedules=', this.displayRunningSchedules);

        this.equipmentTaskSeparator = this.configService.config.getIn(['schedule_table', 'equipment_task_string_separator']);
        console.log('{schedule-table}', '[loadConfig]', 'equipment_task_string_separator=', this.equipmentTaskSeparator);

        this.geocatTranslationPrefix = this.configService.config.getIn(['schedule_table', 'geocat_translation_prefix']);
        console.log('{schedule-table}', '[loadConfig]', 'geocat_translation_prefix=', this.geocatTranslationPrefix);

        this.funcatTranslationPrefix = this.configService.config.getIn(['schedule_table', 'funcat_translation_prefix']);
        console.log('{schedule-table}', '[loadConfig]', 'funcat_translation_prefix=', this.funcatTranslationPrefix);

        this.pageSize = this.configService.config.getIn(['schedule_table', 'page_size']);
        console.log('{schedule-table}', '[loadConfig]', 'page_size=', this.pageSize);

        this.displaySpinner = this.configService.config.getIn(['schedule_table', 'display_spinner']);
        console.log('{schedule-table}', '[loadConfig]', 'display_spinner=', this.displaySpinner);

        this.displayOtherTypesInRunningSchedules = this.configService.config.getIn(['schedule_table', 'display_other_types_in_running_schedules']);
        console.log('{schedule-table}', '[loadConfig]', 'display_other_types_in_running_schedules=', this.displayOtherTypesInRunningSchedules);
    }
    loadData() {
        if (this.subRoute) {
            this.subRoute.unsubscribe();
        }
        this.subRoute = this.route.queryParams.subscribe(params => {
            // this.cleanupScheduleSubscriptions();
            const p = params['periodic'];
            if (p === 'true') {
                this.displayPeriodicSchedules = true;
                this.displayCutoffTime = this.displayCutoffTimePeriodic;
            } else {
                this.displayPeriodicSchedules = false;
                this.displayCutoffTime = this.displayCutoffTimeNonPeriodic;
            }
            console.log('{schedule-table}', '[loadData]', 'periodic =', this.displayPeriodicSchedules);

            const clientName = params['clientName'] || this.configService.config.getIn(['default_client_name']);
            console.log('{schedule-table}', '[loadData]', 'clientName =', clientName);

            this.scheduleService.setClientName(clientName);

            const runtimeFilters = params['filter'];
            console.log('{schedule-table}', '[loadData]', 'runtimeFilters =', runtimeFilters);

            const runtimeSort = params['sort'];
            console.log('{schedule-table}', '[loadData]', 'runtimeSort =', runtimeSort);

            if (!this.subSchedules) {
                this.subSchedules = this.scheduleService.getSchedulesByPeriodic(this.displayPeriodicSchedules).subscribe(schedules => {
                    console.log('{schedule-table}', '[loadData]', 'scheduleService return', schedules.length, 'schedules');
                    this.schedules = schedules;
                    if (schedules && schedules.length > 0) {
                        // display first schedule as active and selected schedules
                        this.activeItem = [this.schedules[0]];
                        this.selectedSchedule = schedules[0];

                        this.visibleUserDefinedCnt = 0;
                        for (const s of schedules) {
                            if (!s.titleReadOnly && s.visibility) {
                                this.visibleUserDefinedCnt++;
                            }
                        }
                        this.oneshotStarted = this.scheduleService.isOneshotScheduleStarted();
                        this.periodicStarted = this.scheduleService.isPeriodicScheduleStarted();
                        this.updateAddDeleteRenameScheduleButton();
                        this.cancelRenameSchedule();
                    } else {
                        console.log('{schedule-table}', '[loadData]', 'getSchedulesByPeriodic return 0 schedule');
                    }
                })
            } else {
                this.scheduleService.updateSchedulesByPeriodic(this.displayPeriodicSchedules);
            }

            if (!this.subScheduleItems) {
                this.subScheduleItems = this.scheduleService.getScheduleItemsByPeriodic(this.displayPeriodicSchedules).subscribe(scheduleItems => {
                    console.log('{schedule-table}', '[loadData]', 'scheduleService return', scheduleItems.length, 'scheduleItems');
                    this.clearSelectedRow();
                    this.clearSchItemFilters();

                    this.setDefaultSort(runtimeSort);

                    this.addOfflineFilters();

                    this.addRuntimeFilters(runtimeFilters);

                    this.cachedSchItems = this.filterSchItems(scheduleItems);

                    if (this.selectedSchedule) {
                        const schItems = new Array<ScheduleItem>();
                        // filter selected Schedule
                        for (const schItem of this.cachedSchItems) {
                            if (schItem.scheduleId === this.selectedSchedule.id) {
                                schItems.push(schItem);
                            }
                        }
                        this.scheduleItems = [...schItems];
                    } else {
                        this.scheduleItems = [...this.cachedSchItems];
                    }

                    this.dataTable.offset = 0;
                    this.pageOffset = 0;

                    if (this.sortEvent) {
                        const sorted = sortRows(this.scheduleItems, this.sortColumns, this.sortEvent);

                        this.tempSchItems = [...sorted];
                    } else {
                        this.tempSchItems = this.scheduleItems;
                    }

                    this.getScheduleContentPage();
                });
            } else {
                this.scheduleService.updateScheduleItemsByPeriodic(this.displayPeriodicSchedules);
            }

            this.getRunningSchedules();
        })
    }
    ngOnDestroy() {

    }
    // private cleanupScheduleSubscriptions() {
    //     if (this.subSchedules) {
    //         this.subSchedules.unsubscribe();
    //     }
    //     if (this.subScheduleItems) {
    //         this.subScheduleItems.unsubscribe();
    //     }
    //     if (this.subGetRunningSchedules) {
    //         this.subGetRunningSchedules.unsubscribe();
    //     }
    // }
    // ng2-select callback
    private get disabledV(): string {
        return this._disabledV;
    }
    // ng2-select callback
    private set disabledV(value: string) {
        this._disabledV = value;
        this.selectScheduleDisabled = this._disabledV === '1';
    }
    // ng2-select callback
    public selected(event: any): void {
        console.log('{schedule-table}', '[ng2-select selected]', 'Selected value is: ', event.text);
        const val = event.id;
        console.log('{schedule-table}', '[ng2-select selected]', 'selectedSchedule', event.id);
        this.selectedSchedule = this.schedules.find(s => s.id === val);
        this.updateAddDeleteRenameScheduleButton();
        this.cancelRenameSchedule();
        console.log('{schedule-table}', '[ng2-select selected]', 'titleReadOnly', this.selectedSchedule.titleReadOnly);
        console.log('{schedule-table}', '[ng2-select selected]', 'renameScheduleEnabled', this.renameScheduleEnabled);
        // filter our data
        const temp = this.cachedSchItems.filter(function(d) {
            console.log('{schedule-table}', '[ng2-select selected]', 'filtering', d.scheduleId, val);
            return d.scheduleId.indexOf(val) !== -1 || !val;
        });
        // update the rows
        this.scheduleItems = temp;

        console.log('{schedule-table}', '[ng2-select selected]', 'filtered rows', temp);
        // Whenever the filter changes, always go back to the first page
        this.dataTable.offset = 0;
        this.pageOffset = 0;

        // clear equipment selection
        this.clearSelectedRow();

        if (this.sortEvent) {
            const sorted = sortRows(temp, this.sortColumns, this.sortEvent);

            this.tempSchItems = [...sorted];
        } else {
            this.tempSchItems = temp;
        }

        // Read content from tsc server
        this.getScheduleContentPage();
    }
    // ng2-select callback
    public removed(value: any): void {
        console.log('{schedule-table}', '[ng2-select removed]', 'Removed value is: ', value);
    }
    // ng2-select callback
    public typed(value: any): void {
        console.log('{schedule-table}', '[ng2-select typed]', 'New search input: ', value);
    }
    // ng2-select callback
    public refreshValue(value: any): void {
        console.log('{schedule-table}', '[ng2-select typed]', 'refresh value ', value);
    }
    // ngx-datatable callback
    public onSelect(selectedRow) {
        console.log('{schedule-table}', '[ngx-datatable onSelect]', 'Select Event', selectedRow, this.selectedRow);
        const scheduleItem: ScheduleItem = this.selectedRow[0];
        this.selectedItemDisplay = this.translate.instant(this.geocatTranslationPrefix + scheduleItem.geoCat) + this.equipmentTaskSeparator +
                                    this.translate.instant(this.funcatTranslationPrefix + scheduleItem.funcCat) + this.equipmentTaskSeparator +
                                    this.translate.instant(scheduleItem.eqtLabel)  + this.equipmentTaskSeparator +
                                    this.translate.instant(scheduleItem.eqtDescription);
        this.resetModified();
    }
    // ngx-datatable callback
    public onActivate(event) {
        console.log('{schedule-table}', '[ngx-datatable onActivate]', 'Activate Event', event);
    }
    // ngx-datatable callback
    // public updateRowPosition() {
    //     const ix = this.getSelectedIx();
    //     const arr = [ ...this.scheduleItems ];
    //     arr[ix - 1] = this.scheduleItems[ix];
    //     arr[ix] = this.scheduleItems[ix - 1];
    //     this.scheduleItems = arr;
    // }
    // ngx-datatable callback
    // public getSelectedIx() {
    //     return this.selected[0]['$$index'];
    // }
    // ngx-datatable callback
    public onPage(event: any) {
        console.log('{schedule-table}', '[ngx-datatable onPage]', 'Page Event', event);
        if (event) {
            this.pageSize = event.pageSize;
            this.pageOffset = event.offset;

            this.getScheduleContentPage();
        }
    }
    // ngx-datatable callback
    public onSort(event: any) {
        console.log('{schedule-table}', '[ngx-datatable onSort]', 'Sort Event', event);

        this.sortEvent = event.sorts;

        const val = sortRows(this.tempSchItems, this.sortColumns, this.sortEvent);

        this.tempSchItems = [...val];

        this.pageOffset = 0;

        this.getScheduleContentPage();
    }

    public getScheduleContentPage() {
        console.log('{schedule-table}', '[getScheduleContentPage]', 'page=', this.pageOffset, 'size=', this.pageSize);
        this.scheduleService.clearOnOffTimerMap();
        const startIdx = this.pageOffset * this.pageSize;
        for (let rowIdx = startIdx; rowIdx < (startIdx + this.pageSize) && rowIdx < this.tempSchItems.length; rowIdx++) {
            this.getScheduleItemContent(rowIdx);
        }
    }

    public getScheduleItemContent(row: number) {
        console.log('{schedule-table}', '[getScheduleItemContent]', 'row=', row, 'index=', this.scheduleItems.indexOf(this.tempSchItems[row]));

        if (this.tempSchItems && this.tempSchItems[row]) {
            if (this.tempSchItems[row].taskName1) {
                this.scheduleService.getScheduleDescFilterEnable(this.tempSchItems[row].taskName1);
            }
            if (this.tempSchItems[row].taskName2) {
                this.scheduleService.getScheduleDescFilterEnable(this.tempSchItems[row].taskName2);
            }
        }
    }

    private clearSchItemFilters() {
        this.schItemFilters = [];
    }
    private addSchItemsFilter(attributeName: string, filterPattern: string) {
        const scheduleFilter = new ScheduleItemFilter();
        scheduleFilter.attributeName = attributeName;
        scheduleFilter.matchPattern = new RegExp(filterPattern);
        this.schItemFilters.push(scheduleFilter);
         console.log('{schedule-table}', '[addSchItemsFilter]', 'this.schItemFilters.length', this.schItemFilters.length);
    }
    private addOfflineFilters() {
        console.log('{schedule-table}', '[addOfflineFilters]');
        if (this.offlineFilters) {
            console.log('{schedule-table}', '[addOfflineFilters]', 'this.offlineFilters', this.offlineFilters);
            this.offlineFilters.forEach((value: string, key: string) => {
                console.log('{schedule-table}', '[addOfflineFilters]', 'filter', key, value);
                const filter = new ScheduleItemFilter();
                filter.attributeName = key;
                filter.matchPattern = new RegExp(value);
                this.schItemFilters.push(filter);
                console.log('{schedule-table}', '[addOfflineFilters]', 'this.schItemFilters.length', this.schItemFilters.length);
            })
        } else {
            console.log('{schedule-table}', '[addOfflineFilters]', 'this.offlineFilters is null');
        }
    }
    private addRuntimeFilters(runtimeFilters) {
        console.log('{schedule-table}', '[addRuntimeFilters]');
        if (runtimeFilters) {
            console.log('{schedule-table}', '[addRuntimeFilters]', 'runtimeFilters', runtimeFilters);
            const obj = JSON.parse(runtimeFilters)
            Object.keys(obj).forEach(key => {
                console.log('{schedule-table}', '[addRuntimeFilters]', 'key', key, 'value', obj[key]);
                const filter = new ScheduleItemFilter();
                filter.attributeName = key;
                filter.matchPattern = new RegExp(obj[key]);
                this.schItemFilters.push(filter);
                console.log('{schedule-table}', '[addRuntimeFilters]', 'this.schItemFilters.length', this.schItemFilters.length);
            })
        } else {
            console.log('{schedule-table}', '[addRuntimeFilters]', 'runtimeFilters is null');
        }
    }

    private filterSchItems(scheduleItems): ScheduleItem[] {
        let filteredScheduleItems = Array<ScheduleItem>();
        if (this.schItemFilters) {
            console.log('{schedule-table}', '[filterSchItems]', 'this.schItemFilters.length', this.schItemFilters.length);
            for (const r of scheduleItems) {
                let matching = true;
                const filterCnt = 0;
                for (const f of this.schItemFilters) {
                    console.log('{schedule-table}', '[filterSchItems]', 'filter', f.attributeName, f.matchPattern);
                    const attribute: string = r[f.attributeName];
                    if (attribute) {
                        if (!attribute.toString().match(f.matchPattern)) {
                            matching = false;
                            console.log('{schedule-table}', '[filterSchItems]', 'attribute', attribute, 'not matching', f.matchPattern);
                            break;
                        } else {
                            console.log('{schedule-table}', '[filterSchItems]', 'attribute', attribute, 'matching', f.matchPattern);
                        }
                    } else {
                        console.log('{schedule-table}', '[filterSchItems]', 'attribute not defined', f.attributeName);
                        continue;
                    }
                }
                if (matching) {
                    filteredScheduleItems.push(r);
                }
            }
        } else {
            filteredScheduleItems = scheduleItems;
        }
        return filteredScheduleItems;
    }

    private setDefaultSort(runtimeSort) {
        console.log('{schedule-table}', '[setDefaultSort]');
        if (!this.defaultSorts || this.defaultSorts.length === 0) {
            if (runtimeSort) {
                console.log('{schedule-table}', '[setDefaultSort]', 'runtimeSort', runtimeSort);
                const obj = JSON.parse(runtimeSort);
                if (Array.isArray(obj)) {
                    this.defaultSorts = obj;
                    this.sortEvent = obj;
                } else {
                    this.defaultSorts = [obj];
                    this.sortEvent = [obj];
                }
                console.log('{schedule-table}', '[setDefaultSort]', 'defaultSorts', this.defaultSorts);
            } else if (this.offlineSort.get('prop') && this.offlineSort.get('dir')) {
                console.log('{schedule-table}', '[setDefaultSort]', 'runtimeSort is null. offlineSort', this.offlineSort);

                const obj = new Object();
                obj['prop'] = this.offlineSort.get('prop');
                obj['dir'] = this.offlineSort.get('dir');
                this.defaultSorts = [obj];
                this.sortEvent = [obj];

                console.log('{schedule-table}', '[setDefaultSort]', 'defaultSorts', this.defaultSorts);
            } else {
                console.log('{schedule-table}', '[setDefaultSort]', 'runtimeSort and offlineSort are not set. No default sort is applied');
                console.log('{schedule-table}', '[setDefaultSort]', 'this.dataTable.sorts', this.dataTable.sorts);
                console.log('{schedule-table}', '[setDefaultSort]', 'this.sortEvent', this.sortEvent);
            }
        } else {
            console.log('{schedule-table}', '[setDefaultSort]', 'defaultSorts already set.', this.defaultSorts[0]);
            if (this.dataTable.sorts && this.dataTable.sorts.length > 0) {
                console.log('{schedule-table}', '[setDefaultSort]', 'this.dataTable.sorts', this.dataTable.sorts[0]);
                this.sortEvent = this.dataTable.sorts;
            } else {
                this.sortEvent = this.defaultSorts;
            }
        }
    }
    private clearSelectedRow() {
        this.selectedRow = [];
        this.selectedScheduleItem = null;
        this.selectedItemDisplay = '';
        this.resetModified();
    }
    public addSchedule() {
        console.log('{schedule-table}', '[addSchedule]');
        this.addScheduleClicked = true;
        this.unusedSchedules = this.scheduleService.getUnusedSchedules();
        if (this.unusedSchedules && this.unusedSchedules.length > 0) {
            this.newSchedule = this.unusedSchedules[0];
        }
        this.disableScheduleAction();
    }
    public disableScheduleAction() {
        this.addScheduleEnabled = false;
        this.deleteScheduleEnabled = false;
        this.renameScheduleEnabled = false;
        this.selectScheduleDisabled = true;
    }
    public applyAddSchedule() {
        console.log('{schedule-table}', '[applyAddSchedule]');
        this.addScheduleClicked = false;

        if (this.newSchedule) {
            const subAddSch = this.scheduleService.addSchedule(this.newSchedule.id).subscribe(
                res => {
                    // set new schedule as selected schedule
                    this.activeItem = [this.newSchedule];
                    // send selected event to data table to update schedule items
                    const selectEvent: any = {};
                    selectEvent.id = this.newSchedule.id;
                    selectEvent.text = this.newSchedule.text;
                    this.selected(selectEvent);

                    subAddSch.unsubscribe();
                }
            )
        }
        this.updateAddDeleteRenameScheduleButton();
    }
    public cancelAddSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]');
        this.addScheduleClicked = false;
        this.updateAddDeleteRenameScheduleButton();
    }
    public deleteSchedule() {
        console.log('{schedule-table}', '[deleteSchedule]');
        this.disableScheduleAction();
        if (this.selectedSchedule.periodic && !this.selectedSchedule.titleReadOnly) {
            const subDelSch = this.scheduleService.deleteSchedule(this.selectedSchedule.id).subscribe(
                res => {
                    if (this.schedules[0]) {
                        // set first schedule as selected schedule
                        this.activeItem = [this.schedules[0]];
                        // send selected event to data table to update schedule items
                        const selectEvent: any = {};
                        selectEvent.id = this.schedules[0].id;
                        selectEvent.text = this.schedules[0].text;
                        this.selected(selectEvent);
                    } else {
                        // clear selected schedule;
                        this.activeItem = null;
                        // send null event to data table to clear schedule items
                        const selectEvent: any = {};
                        selectEvent.id = null;
                        selectEvent.text = null;
                        this.selected(selectEvent);
                    }
                    subDelSch.unsubscribe();
                }
            )
        }
    }
    public renameSchedule() {
        console.log('{schedule-table}', '[renameSchedule] clicked');
        this.renameScheduleClicked = true;
        this.disableScheduleAction();
        this.newScheduleTitle = this.selectedSchedule.text;
    }
    public applyRenameSchedule() {
        console.log('{schedule-table}', '[applyRenameSchedule]', this.newScheduleTitle);
        this.renameScheduleClicked = false;

        if (this.newScheduleTitle && this.newScheduleTitle.length > 0) {
            this.selectedSchedule.text = this.newScheduleTitle;
            this.scheduleService.setScheduleTitle(this.selectedSchedule.id, this.newScheduleTitle);
            // rebuild schedules
            const tempSch: Schedule[] = [];
            for (const sch of this.schedules) {
                if (sch.id === this.selectedSchedule.id) {
                    sch.text = this.newScheduleTitle;
                    this.activeItem = [sch];
                }
                tempSch.push(sch);
            }
            this.schedules = [];
            this.schedules = tempSch;
        }
        this.newScheduleTitle = '';
        this.updateAddDeleteRenameScheduleButton();
    }
    public updateAddDeleteRenameScheduleButton() {
        console.log('{schedule-table}', '[updateAddDeleteRenameScheduleButton]', ' visibleUserDefinedCnt', this.visibleUserDefinedCnt);
        this.addScheduleEnabled = this.visibleUserDefinedCnt < this.maxUserDefinedScheduleCount;
        const isAssigned = this.scheduleService.isScheduleAssigned(this.selectedSchedule.id);
        const isRunning = this.scheduleService.isScheduleRunning(this.selectedSchedule.id);

        console.log('{schedule-table}', '[updateAddDeleteRenameScheduleButton]', 'isAssigned', isAssigned);
        console.log('{schedule-table}', '[updateAddDeleteRenameScheduleButton]', 'isRunning', isRunning);
        this.deleteScheduleEnabled = !isRunning && !isAssigned && !this.selectedSchedule.titleReadOnly && (this.visibleUserDefinedCnt > 0);
        this.renameScheduleEnabled = !this.selectedSchedule.titleReadOnly;
        this.selectScheduleDisabled = false;
        this.newScheduleTitleModified = false;
    }
    public cancelRenameSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]', this.newScheduleTitle);
        this.renameScheduleClicked = false;
        this.newScheduleTitle = '';
        this.updateAddDeleteRenameScheduleButton();
    }
    public startOneShot() {
        console.log('{schedule-table}', '[startOneShot]');
        this.scheduleService.startOneshotSchedule(this.selectedSchedule.id);
        this.oneshotStarted = true;
    }
    public stopOneShot() {
        console.log('{schedule-table}', '[stopOneShot]');
        this.scheduleService.stopSchedule(this.selectedSchedule.id);
        this.oneshotStarted = false;
    }

    public startPeriodic() {
        this.scheduleService.startDefaultPeriodicSchedules();
        this.periodicStarted = true;
        console.log('{schedule-table}', '[startPeriodic]', 'periodicStarted', this.periodicStarted);
    }

    public stopPeriodic() {
        // this.clearPeriodicWeeklySchedules();
        this.scheduleService.stopPeriodicSchedules();
        this.periodicStarted = false;
        console.log('{schedule-table}', '[stopPeriodic]', 'periodicStarted', this.periodicStarted);
    }

    public updateOnTimeValue(newOnTimeValue) {
        console.log('{schedule-table}', '[updateOnTimeValue]', newOnTimeValue);
        this.newOnTime = newOnTimeValue;
        this.checkInputModified();
        this.checkInputTimeIsValid();
    }
    public updateOffTimeValue(newOffTimeValue) {
        console.log('{schedule-table}', '[updateOffTimeValue]', newOffTimeValue);
        this.newOffTime = newOffTimeValue;
        this.checkInputModified();
        this.checkInputTimeIsValid();
    }

    public updateOnTimeIsEnabled(onTimeIsEnabled) {
        console.log('{schedule-table}', '[updateOnTimeIsEnabled]', onTimeIsEnabled);
        this.pendingOnTimeIsEnabled = onTimeIsEnabled;
        this.checkInputModified();
    }
    public updateOffTimeIsEnabled(offTimeIsEnabled) {
        console.log('{schedule-table}', '[updateOffTimeIsEnabled]', offTimeIsEnabled);
        this.pendingOffTimeIsEnabled = offTimeIsEnabled;
        this.checkInputModified();
    }
    public checkInputModified() {
        if (this.pendingOnTimeIsEnabled && !this.selectedScheduleItem.enableFlag1) {
            this.inputIsModified = true;
        } else if (!this.pendingOnTimeIsEnabled && this.selectedScheduleItem.enableFlag1) {
            this.inputIsModified = true;
        } else if (this.pendingOffTimeIsEnabled && !this.selectedScheduleItem.enableFlag2) {
            this.inputIsModified = true;
        } else if (!this.pendingOffTimeIsEnabled && this.selectedScheduleItem.enableFlag2) {
            this.inputIsModified = true;
        } else if (this.newOnTime !== this.selectedScheduleItem.onTime) {
            this.inputIsModified = true;
        } else if (this.newOffTime !== this.selectedScheduleItem.offTime) {
            this.inputIsModified = true;
        } else {
            this.inputIsModified = false;
        }
        console.log('{schedule-table}', '[checkInputModified]', 'inputIsModified', this.inputIsModified);
    }
    public checkInputTimeIsValid() {
        const regexp = /^(\d\d:\d\d)$/;
        if (this.newOnTime !== this.selectedScheduleItem.onTime && !this.newOnTime.match(regexp)) {
            this.newOnTimeValid = false;
        } else {
            this.newOnTimeValid = true;
        }
        if (this.newOffTime !== this.selectedScheduleItem.offTime && !this.newOffTime.match(regexp)) {
            this.newOffTimeValid = false;
        } else {
            this.newOffTimeValid = true;
        }
        if (this.newOnTimeValid && this.newOffTimeValid) {
            this.inputTimeValid = true;
        } else {
            this.inputTimeValid = false;
        }
        console.log('{schedule-table}', '[checkInputTimeIsValid]', 'inputTimeValid', this.inputTimeValid,
            'newOnTime', this.newOnTime, 'newOffTime', this.newOffTime);
    }
    // click event handler for save button
    public saveModified() {
        const selectedItem: ScheduleItem = this.selectedRow[0];
        let checkRunDates = false;
        if (this.pendingOnTimeIsEnabled && !selectedItem.enableFlag1) {
            checkRunDates = true;
            const subEnableTask = this.scheduleService.enableTask(this.selectedRow[0].taskName1).subscribe(
                res => {
                    console.log('{schedule-table}', '[saveModified]', 'enableTask returned', res);
                    subEnableTask.unsubscribe();
                }
            );
        } else if (!this.pendingOnTimeIsEnabled && selectedItem.enableFlag1) {
            const subDisableTask = this.scheduleService.disableTask(this.selectedRow[0].taskName1).subscribe(
                res => {
                    console.log('{schedule-table}', '[saveModified]', 'disableTask returned', res);
                    subDisableTask.unsubscribe();
                }
            );
        }

        if (this.pendingOffTimeIsEnabled && !selectedItem.enableFlag2) {
            checkRunDates = true;
            const subEnableTask = this.scheduleService.enableTask(this.selectedRow[0].taskName2).subscribe(
                res => {
                    console.log('{schedule-table}', '[saveModified]', 'enableTask returned', res);
                    subEnableTask.unsubscribe();
                }
            );
        } else if (!this.pendingOffTimeIsEnabled && selectedItem.enableFlag2) {
            const subDisableTask = this.scheduleService.disableTask(this.selectedRow[0].taskName2).subscribe(
                res => {
                    console.log('{schedule-table}', '[saveModified]', 'disableTask returned', res);
                    subDisableTask.unsubscribe();
                }
            );
        }

        if (this.newOnTime !== selectedItem.onTime) {
            if (this.newOnTime && this.selectedRow.length === 1 && this.newOnTime !== selectedItem.onTime) {
                checkRunDates = true;
                this.scheduleService.setScheduleItemOnTime(this.selectedSchedule.id, selectedItem, this.newOnTime).subscribe(
                    res => {
                        console.log('{schedule-table}', '[saveModified]', 'setFilter OnTime for returned', res);
                    }
                )
            }
        }

        if (this.newOffTime !== selectedItem.offTime) {
            if (this.newOffTime && this.selectedRow.length === 1 && this.newOffTime !== selectedItem.offTime) {
                checkRunDates = true;
                this.scheduleService.setScheduleItemOffTime(this.selectedSchedule.id, selectedItem, this.newOffTime).subscribe(
                    res => {
                        console.log('{schedule-table}', '[saveModified]', 'setFilter OffTime returned', res);
                    }
                )
            }
        }

        // update dates in oneshot schedule run day group
        if (!this.displayPeriodicSchedules && this.oneshotStarted && checkRunDates) {
            this.scheduleService.addCurrentDayToOneshotSchedules();
        }
    }

    public resetModified() {
        if (this.selectedRow && this.selectedRow[0]) {
            const scheduleItem: ScheduleItem = this.selectedRow[0];
            this.selectedScheduleItem = this.selectedRow[0];

            if (scheduleItem.onTime !== this.unavailableOnOffTime) {
                this.onTimeNotAvailable = false;
            } else {
                this.onTimeNotAvailable = true;
            }
            if (scheduleItem.enableFlag1) {
                this.pendingOnTimeIsEnabled = true;
            } else {
                this.pendingOnTimeIsEnabled = false;
            }
            this.selectedOnTime = scheduleItem.onTime;
            this.newOnTime = scheduleItem.onTime;
            if (scheduleItem.offTime !== this.unavailableOnOffTime) {
                this.offTimeNotAvailable = false;
            } else {
                this.offTimeNotAvailable = true;
            }
            if (scheduleItem.enableFlag2) {
                this.pendingOffTimeIsEnabled = true;
            } else {
                this.pendingOffTimeIsEnabled = false;
            }

            this.selectedOffTime = scheduleItem.offTime;
            this.newOffTime = scheduleItem.offTime;
        }
        this.inputIsModified = false;
        this.newOnTimeValid = true;
        this.newOffTimeValid = true;
        this.inputTimeValid = true;
    }

    public updateNewScheduleTitle(newScheduleTitle) {
        console.log('{schedule-table}', '[updateNewScheduleTitle]', newScheduleTitle);

        if (newScheduleTitle && newScheduleTitle.length > 0) {
            if (newScheduleTitle !== this.selectedSchedule.text) {
                this.newScheduleTitleModified = true;
            } else {
                this.newScheduleTitleModified = false;
            }
            let found = false;
            for (const s of this.schedules) {
                if (s.id !== this.selectedSchedule.id && s.text === newScheduleTitle) {
                    found = true;
                    break;
                }
            }
            if (found) {
                this.newScheduleTitleValid = false;
            } else {
                this.newScheduleTitleValid = true;
            }
        } else {
            this.newScheduleTitleValid = false;
        }
    }

    public getRunningSchedules() {
        console.log('{schedule-table}', '[getRunningSchedules]');
        if (!this.subGetRunningSchedules) {
            this.subGetRunningSchedules = this.scheduleService.getRunningSchedules().subscribe(
                schedules => {
                    console.log('{schedule-table}', '[getRunningSchedules] return', schedules);
                    if (schedules) {
                        this.runningSchedules = schedules;
                        let cnt = 0;
                        if (schedules.length > 0) {
                            for (const s of schedules) {
                                if (!this.displayOtherTypesInRunningSchedules) {
                                    console.log('{schedule-table}', '[getRunningSchedules]', 'this.displayPeriodicSchedules && !s.periodic',
                                        this.displayPeriodicSchedules && !s.periodic);
                                        console.log('{schedule-table}', '[getRunningSchedules]', '!this.displayPeriodicSchedules && s.periodic',
                                        !this.displayPeriodicSchedules && s.periodic);
                                    if ((this.displayPeriodicSchedules && !s.periodic) ||
                                        (!this.displayPeriodicSchedules && s.periodic)) {
                                            continue;
                                        }
                                }
                                if (cnt > 0) {
                                    this.runningSchedulesStr = this.runningSchedulesStr + ', ' + s.text;
                                } else {
                                    this.runningSchedulesStr = s.text;
                                }
                                cnt++;
                                console.log('{schedule-table}', '[getRunningSchedules] runningSchedulesStr', this.runningSchedulesStr);
                            }
                        }

                        if (cnt < 1) {
                            const str = 'No schedule is running';
                            const translatedStr = this.translate.instant(str);
                            if (translatedStr) {
                                this.runningSchedulesStr = translatedStr;
                            } else {
                                this.runningSchedulesStr = str;
                            }
                        }
                    }
                }
            )
        } else {
            this.scheduleService.updateRunningSchedules();
        }
    }

    public onRefresh() {
        window.location.reload();
    }
}

