import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProfileManagementComponent } from './profile-management.component';
import { ProfileManagementModifyComponent } from './profile-management-modify.component';
import { ProfileManagementAddComponent } from './profile-management-add.component';
import { ProfileManagementRemoveComponent } from './profile-management-remove.component';

const routes: Routes = [
    { path: 'profile-management', component: ProfileManagementComponent, children: [
        { path: '', redirectTo: 'modify', pathMatch: 'full' },
        { path: 'modify', component: ProfileManagementModifyComponent },
        { path: 'add', component: ProfileManagementAddComponent },
        { path: 'remove', component: ProfileManagementRemoveComponent }
    ]}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})

export class AppRoutingModule {}
