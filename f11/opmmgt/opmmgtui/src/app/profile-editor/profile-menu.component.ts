import {Component,OnDestroy} from '@angular/core';

import {AgRendererComponent} from 'ag-grid-angular/main';
import {OpmService} from "../opm.service";
import {Router} from "@angular/router";
import {ProfileEditorComponent} from "./profile-editor.component";

@Component({
    selector: 'profile-menu',
    template: `<button *ngIf="profileId!=-1" (click)="delete()" class="btn btn-xs btn-primary">Del</button> <button *ngIf="profileId!=-1"  (click)="edit()" class="btn btn-xs btn-primary">Edit</button>`
})
export class ProfileMenuComponent implements AgRendererComponent, OnDestroy {
    private params:any;

    public profileId:number;

    constructor(private router: Router, private opmService: OpmService) {
      this.profileId = -1;
    }

    agInit(params:any):void {
        this.params = params;
        this.profileId = this.params.data.id;
    }

    private getProfiles() {
        this.opmService.getProfiles()
          .subscribe(res => this.params.api.setRowData(res));
      }
  
    public delete() {
      this.opmService.deleteProfile(this.profileId).subscribe((res) => {
        this.getProfiles();
      });
    }

    public edit() {
      this.router.navigate(['/profiles', this.params.data.id ]);
    }

    ngOnDestroy() {

    }
}
/*
cellRenderer: function(params) {
          var eDiv = document.createElement('div');
          if (params.data.id > 0) {
            eDiv.innerHTML = '<a ng-reflect-router-link="/profiles/delete/' + params.data.id
              + '" ng-reflect-href="/profiles/delete/' + params.data.id
              + '" href="/profiles/delete/' + params.data.id
              + '" class="btn btn-xs btn-primary">Del</a> <a
              href="/profiles/'
              + params.data.id + '" class="btn btn-xs btn-primary">Edit</a>';
          } else {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Add</button>';
          }

          return eDiv;
        }

        */
