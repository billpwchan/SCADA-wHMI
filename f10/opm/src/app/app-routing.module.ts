import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OperatorManagementComponent } from './operator/operator-management.component';
import { OperatorManagementModifyComponent } from './operator/operator-management-modify.component';
import { OperatorManagementAddComponent } from './operator/operator-management-add.component';
import { OperatorManagementRemoveComponent } from './operator/operator-management-remove.component';

import { ProfileManagementComponent } from './profile/profile-management.component';
import { ProfileManagementModifyComponent } from './profile/profile-management-modify.component';
import { ProfileManagementAddComponent } from './profile/profile-management-add.component';
import { ProfileManagementRemoveComponent } from './profile/profile-management-remove.component';

const routes: Routes = [
    { path: '', redirectTo: '/profile-management/modify', pathMatch: 'full' },
    { path: 'operator-management', component: OperatorManagementComponent, children: [
        { path: '', redirectTo: 'modify', pathMatch: 'full' },
        { path: 'modify', component: OperatorManagementModifyComponent },
        { path: 'add', component: OperatorManagementAddComponent },
        { path: 'remove', component: OperatorManagementRemoveComponent },
    ]},
    { path: 'profile-management', component: ProfileManagementComponent, children: [
        { path: '', redirectTo: 'modify', pathMatch: 'full' },
        { path: 'modify', component: ProfileManagementModifyComponent },
        { path: 'add', component: ProfileManagementAddComponent },
        { path: 'remove', component: ProfileManagementRemoveComponent }
    ]},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {}
