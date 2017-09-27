import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Observer } from 'rxjs/Observer';
import 'rxjs/add/operator/share';

@Injectable()
export class LoadingService {
    public loading$: Observable<{}>;
    private observer: Observer<string>;

    constructor() {
        console.log('{LoadingService}', '[constructor]');
        this.loading$ = new Observable(
            observer => this.observer = observer).share();
    }

    public setLoading(name) {
        if (this.observer) {
            this.observer.next(name);
        }
    }
}
