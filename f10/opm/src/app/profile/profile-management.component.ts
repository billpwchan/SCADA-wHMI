import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-profile-management',
    templateUrl: './profile-management.component.html'
})

export class ProfileManagementComponent {
    constructor(
        private translate: TranslateService
    ) { }

}
