import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
 
import { ScheduleTableComponent }		from './schedule-table/schedule-table.component';
import { SchedulePlanningComponent }	from './schedule-planning/schedule-planning.component';

 
const routes: Routes = [
  { path: '', redirectTo: 'schedule-table/Oneshot', pathMatch: 'full' },
  { path: 'schedule-table', redirectTo: '/schedule-table/Oneshot', pathMatch: 'full' },
  { path: 'schedule-table/:scheduleType',  component: ScheduleTableComponent  },
  { path: 'schedule-planning', component: SchedulePlanningComponent },
];
 
@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}