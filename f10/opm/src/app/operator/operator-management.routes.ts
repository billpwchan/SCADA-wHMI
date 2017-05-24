import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OperatorManagementComponent } from './operator-management.component';
import { OperatorManagementModifyComponent } from './operator-management-modify.component';
import { OperatorManagementAddComponent } from './operator-management-add.component';
import { OperatorManagementRemoveComponent } from './operator-management-remove.component';
import { OperatorManagementChangePasswordComponent } from './operator-management-change-password.component';

const routes: Routes = [
    { path: 'operator-management', component: OperatorManagementComponent, children: [
        { path: '', redirectTo: 'modify', pathMatch: 'full' },
        { path: 'modify', component: OperatorManagementModifyComponent },
        { path: 'add', component: OperatorManagementAddComponent },
        { path: 'remove', component: OperatorManagementRemoveComponent },
        { path: 'change-password', component: OperatorManagementChangePasswordComponent }
    ]}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})

export class AppRoutingModule {}
