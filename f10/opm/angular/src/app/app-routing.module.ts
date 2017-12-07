import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OperatorManagementComponent } from './operator/operator-management.component';
import { ProfileManagementComponent } from './profile/profile-management.component';

const routes: Routes = [
    { path: '', redirectTo: 'profile-management', pathMatch: 'full'}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})

export class AppRoutingModule {}
