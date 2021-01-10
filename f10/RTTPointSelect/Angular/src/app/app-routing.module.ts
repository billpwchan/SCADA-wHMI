import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';
import { RttAppComponent } from './rtt-app/rtt-app.component';
import { RttComponent } from './rtt/rtt.component';

const routes: Routes = [
  { path: '', redirectTo: 'rtt', pathMatch: 'full' },
  { path: 'rtt', component: RttComponent },
  { path: 'rtt-app', component: RttAppComponent },
  { path: '**', component: NotFoundComponent }
];
@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
