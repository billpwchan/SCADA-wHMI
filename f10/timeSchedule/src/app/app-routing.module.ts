import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScheduleTableComponent } from './schedule-table/schedule-table.component';
import { SchedulePlanningComponent } from './schedule-planning/schedule-planning.component';
import { PageNotFoundComponent } from './not-found.component';
const routes: Routes = [
  { path: '', redirectTo: 'schedule-table', pathMatch: 'full' },
  { path: 'schedule-table',  component: ScheduleTableComponent  },
  { path: 'schedule-planning', component: SchedulePlanningComponent },
  { path: '**', component: PageNotFoundComponent }
];
@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
