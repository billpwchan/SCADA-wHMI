import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-profile-management-add',
    templateUrl: './profile-management-add.component.html'
})
export class ProfileManagementAddComponent {
    constructor(
        private translateService: TranslateService,
    ) { }
}
