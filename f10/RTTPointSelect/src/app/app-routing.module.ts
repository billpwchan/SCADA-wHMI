import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RttComponent } from './rtt/rtt.component';
import { PageNotFoundComponent }   from './not-found.component';
const routes: Routes = [
  { path: '', redirectTo: 'rtt', pathMatch: 'full' },
  { path: 'rtt',  component: RttComponent  },
  { path: '**', component: PageNotFoundComponent }
];
@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
