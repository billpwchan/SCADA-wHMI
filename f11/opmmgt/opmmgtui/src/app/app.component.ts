import {Component} from "@angular/core";
import {Router, ActivatedRoute} from "@angular/router";
import {OpmService} from "./opm.service";

@Component({
  selector: 'my-app',
  templateUrl: 'app.component.html',
  providers: [ OpmService ]
})
export class AppComponent {
    constructor(public router: Router,
                private route: ActivatedRoute) {
    }
}
