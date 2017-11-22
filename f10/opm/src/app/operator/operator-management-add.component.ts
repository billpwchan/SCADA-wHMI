import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-operator-management-add',
    templateUrl: './operator-management-add.component.html'
})
export class OperatorManagementAddComponent {
    constructor(
        private translateService: TranslateService
    ) { }
}
