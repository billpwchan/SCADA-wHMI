import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router'

import { SelectComponent } from 'ng2-select';
import { DatatableComponent } from '../../../node_modules/@swimlane/ngx-datatable/src/components/datatable.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

import { ScsTscService } from '../service/scs-tsc.service'
import { Schedule } from '../type/schedule';
import { ScheduleItem } from '../type/schedule-item';
import { ConfigService } from '../service/config.service';
import { ScheduleService } from '../service/schedule.service';

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

@Component({
	selector: 'sch-table',
	templateUrl: './schedule-table.component.html',
	styleUrls: ['./schedule-table.component.css']
})
export class ScheduleTableComponent implements OnInit {

	// properties for ng2-select
	private _disabledV:string = '0';
	private disabled:boolean = false;
	private activeItem;

	// properties for ngx-datatable
	private messages = {};

	// subscriptions
	private subRoute: any;
	private subSchedules: any;
	private subScheduleItems: any;

	// scheduleType filter
	private scheduleType: string;

	// stores all schedules of selected schedule type
	private schedules: Schedule[] = [];

	// stores rows filtered by type
	private rows : ScheduleItem[] = [];
	
	// cache copy of rows
	private cachedRows = [];

	@ViewChild(DatatableComponent) table: DatatableComponent;

	// selected schedule
	private selectedSchedule: Schedule;

	// selected row in table
  	private selectedRow = [];

	// flag to display 'Change title' button
	private titleChangeEnabled: boolean = false;

	// place holder to store title typed by operator
	private newScheduleTitle: string;

	// flags to display button to enable/disable task
	private displayEnableOnTime: boolean = false;
	private displayDisableOnTime: boolean = false;
	private displayEnableOffTime: boolean = false;
	private displayDisableOffTime: boolean = false;

	// string to represent unavailable on off time
	private unavailableOnOffTime: string = "NA";

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
		console.info('{schedule-table}', '[loadConfig]', 'translate current lang=', this.translate.currentLang);

		this.unavailableOnOffTime = this.configService.config.getIn(['schedule_table', 'unavailable_on_off_time']);

		console.info('{schedule-table}', '[loadConfig]', 'unavailableOnOffTime=', this.unavailableOnOffTime);
	}

	loadData() {
		this.cleanupSubscriptions();

		this.subRoute = this.route.params.subscribe(params => {
			
			this.scheduleType = params['scheduleType'];
			
			this.subSchedules = this.scheduleService.getSchedules(this.scheduleType).subscribe(schedules => {
		
				this.schedules = schedules;

				if (this.schedules.length > 0) {
					this.activeItem = [this.schedules[0]];

					this.selectedSchedule = schedules[0];

					this.titleChangeEnabled = !this.selectedSchedule.titleReadOnly;

					this.clearSelectedRow();

					this.filterRowsBySchedule();
				}
			})
			console.info('{schedule-table}', '[onInit]', 'schedules', this.schedules);

			this.subScheduleItems = this.scheduleService.getScheduleItems(this.scheduleType).subscribe(scheduleItems => {
				this.rows = scheduleItems;
				this.cachedRows = [...this.rows];

				this.clearSelectedRow();

				this.filterRowsBySchedule();
			});
			console.info('{schedule-table}', '[onInit]', 'scheduleItems', this.rows);
			
		})
	}

	ngOnDestroy() {
		this.cleanupSubscriptions()
	}

	private cleanupSubscriptions() {
		if (this.subRoute) {
			this.subRoute.unsubscribe();
		}
		if (this.subSchedules) {
			this.subSchedules.unsubscribe();
		}
		if (this.subScheduleItems) {
			this.subScheduleItems.unsubscribe();
		}
	}

	// ng2-select callback
	private get disabledV():string {
		return this._disabledV;
	}
	
	// ng2-select callback
	private set disabledV(value:string) {
		this._disabledV = value;
		this.disabled = this._disabledV === '1';
	}
	
	// ng2-select callback
	public selected(event:any):void {
		console.debug('{schedule-table}', '[ng2-select selected]', 'Selected value is: ', event.text);

		const val = event.id;

		console.debug('{schedule-table}', '[ng2-select selected]', 'selectedSchedule', event.id);

		this.selectedSchedule = this.schedules.find(s => s.id == val);

		this.titleChangeEnabled = !this.selectedSchedule.titleReadOnly;

		console.debug('{schedule-table}', '[ng2-select selected]', 'titleReadOnly', this.selectedSchedule.titleReadOnly);

		console.debug('{schedule-table}', '[ng2-select selected]', 'titleChangeEnabled', this.titleChangeEnabled);

		// filter our data
		const temp = this.cachedRows.filter(function(d) {
			console.debug('{schedule-table}', '[ng2-select selected]', 'filtering', d.scheduleKey, val);
			return d.scheduleKey.indexOf(val) !== -1 || !val;
		});

		// update the rows
		this.rows = temp;
		console.trace('{schedule-table}', '[ng2-select selected]', 'filtered rows', temp);
		// Whenever the filter changes, always go back to the first page
		//this.table.offset = 0;
	}
	
	// ng2-select callback
	public removed(value:any):void {
		console.debug('{schedule-table}', '[ng2-select removed]', 'Removed value is: ', value);
	}
	
	// ng2-select callback
	public typed(value:any):void {
		console.debug('{schedule-table}', '[ng2-select typed]', 'New search input: ', value);

		this.newScheduleTitle = value;
	}
	
	// ng2-select callback
	public refreshValue(value:any):void {
		console.debug('{schedule-table}', '[ng2-select typed]', 'refresh value ', value);
	}

	// ngx-datatable callback
	public onSelect(selectedRow) {
    	console.debug('{schedule-table}', '[ngx-datatable onSelect]', 'Select Event', selectedRow, this.selectedRow);

		let scheduleItem: ScheduleItem = this.selectedRow[0];

		if (scheduleItem.onTime != this.unavailableOnOffTime) {
			if (scheduleItem.enableFlag1) {
				this.displayEnableOnTime = false;
				this.displayDisableOnTime = true;
			} else {
				this.displayEnableOnTime = true;
				this.displayDisableOnTime = false;
			}
		} else {
			this.displayEnableOnTime = false;
			this.displayDisableOnTime = false;
		}

		if (scheduleItem.offTime != this.unavailableOnOffTime) {		
			if (scheduleItem.enableFlag2) {
				this.displayEnableOffTime = false;
				this.displayDisableOffTime = true;
			} else {
				this.displayEnableOffTime = true;
				this.displayDisableOffTime = false;
			}
		} else {
			this.displayEnableOffTime = false;
			this.displayDisableOffTime = false;
		}
  	}

	// ngx-datatable callback
	public onActivate(event) {
    	console.debug('{schedule-table}', '[ngx-datatable onActivate]', 'Activate Event', event);
  	}

	// ngx-datatable callback
	updateRowPosition() {
		const ix = this.getSelectedIx();
		const arr = [ ...this.rows ];
		arr[ix - 1] = this.rows[ix];
		arr[ix] = this.rows[ix - 1];
		this.rows = arr;
	}

	// ngx-datatable callback
	getSelectedIx() {
		return this.selected[0]['$$index'];
	}

	// ngx-datatable callback
	// stringComparator(propA, propB) {
	// 	console.debug('String Sorting Comparator', propA, propB);

	// 	// Just a simple sort function comparison
	// 	if (propA.toLowerCase() < propB.toLowerCase()) return -1;
	// 	if (propA.toLowerCase() > propB.toLowerCase()) return 1;
	// }

	// ngx-datatable callback
	// numberComparator(propA, propB) {
	// 	console.debug('Number Sorting Comparator', propA, propB);

	// 	// Just a simple sort function comparison
	// 	if (propA < propB) return -1;
	// 	if (propA > propB) return 1;
	// }

	// click event handler for save button
	updateOnOffTime(newOnTime: string, newOffTime: string) {
		console.debug('{schedule-table}', '[updateOnOffTime]', 'on/off time ', newOnTime, newOffTime);
		if (newOnTime && this.selectedRow.length==1) {
			let scheduleItem: ScheduleItem = this.selectedRow[0];

			console.debug('{schedule-table}', '[updateOnOffTime]', 'scheduleItem=', scheduleItem);

			if (scheduleItem && scheduleItem.taskName1) {

				scheduleItem.onTime = newOnTime;
				let hh = newOnTime.split(':')[0];
				let mm = newOnTime.split(':')[1];

				let dgtime = scheduleItem.filter1.split(' ');
				dgtime[4] = hh;
				dgtime[5] = mm;
				let filter = dgtime.join(' ');

				console.debug('{schedule-table}', '[updateOnOffTime]', 'onTime=', hh, mm, 'filter=', filter);

				this.scheduleService.setFilter(scheduleItem.taskName1, filter);
			}
		}

		if (newOffTime && this.selectedRow.length==1) {
			let scheduleItem: ScheduleItem = this.selectedRow[0];

			if (scheduleItem && scheduleItem.taskName2) {

				scheduleItem.offTime = newOffTime;
				let hh = newOffTime.split(':')[0];
				let mm = newOffTime.split(':')[1];

				let dgtime = scheduleItem.filter2.split(' ');
				dgtime[4] = hh;
				dgtime[5] = mm;
				let filter = dgtime.join(' ');

				console.debug('{schedule-table}', '[updateOnOffTime]', 'offTime=', hh, mm, 'filter=', filter);

				this.scheduleService.setFilter(scheduleItem.taskName2, filter);
			}
		}
	}

	// click event handler for clear button
	private clearOnOffTime() {
	}


	private filterRowsBySchedule() {
		this.rows = [];
		for (let r of this.cachedRows) {
			if (this.selectedSchedule && r.scheduleKey == this.selectedSchedule.id) {
				this.rows.push(r);
			}
		}
	}

	private clearSelectedRow() {
		this.selectedRow = [];
	}

	private setScheduleTitle() {
		console.debug('{schedule-table}', '[setScheduleTitle]', this.newScheduleTitle);	
		if (this.newScheduleTitle) {
			this.selectedSchedule.text = this.newScheduleTitle;
			this.scheduleService.setScheduleTitle(this.selectedSchedule.id, this.newScheduleTitle);

			// rebuild schedules
			let tempSch: Schedule[] = [];
			for (let sch of this.schedules) {
				if (sch.id == this.selectedSchedule.id) {
					sch.text = this.newScheduleTitle;
				}
				tempSch.push(sch);
			}
			this.schedules = [];
			this.schedules = tempSch;

		}
	}

	private setOnTimeEnable() {
		console.debug('{schedule-table}', '[setOnTimeEnable]');

		if (this.selectedRow[0].taskName1) {

			this.scheduleService.enableTask(this.selectedRow[0].taskName1);
		}
	}

	private setOnTimeDisable() {
		console.debug('{schedule-table}', '[setOnTimeDisable]');

		if (this.selectedRow[0].taskName1) {	

			this.scheduleService.disableTask(this.selectedRow[0].taskName1);
		}
	}

	private setOffTimeEnable() {
		console.debug('{schedule-table}', '[setOffTimeEnable]');

		if (this.selectedRow[0].taskName2) {

			this.scheduleService.enableTask(this.selectedRow[0].taskName2);
		}
	}

	private setOffTimeDisable() {
		console.debug('{schedule-table}', '[setOffTimeDisable]');

		if (this.selectedRow[0].taskName2) {

			this.scheduleService.disableTask(this.selectedRow[0].taskName2);
		}
	}
}
