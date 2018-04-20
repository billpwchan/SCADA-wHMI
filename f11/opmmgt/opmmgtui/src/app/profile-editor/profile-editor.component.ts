import { Component, OnInit } from '@angular/core';
import {GridOptions} from "ag-grid/main";
import {OpmService} from "../opm.service";
import {ProfileMenuComponent} from "./profile-menu.component";

@Component({
  selector: 'app-profile-editor',
  templateUrl: './profile-editor.component.html',
  styleUrls: ['./profile-editor.component.css']
})
export class ProfileEditorComponent implements OnInit {

  public errorMessage: string;

  ngOnInit() {
    this.errorMessage = "";
  }

  public gridOptions: GridOptions;

  constructor(private opmService: OpmService) {
    this.gridOptions = <GridOptions>{};
    this.gridOptions.columnDefs = this.createColumnDefs();
    this.getProfiles();
  }

  private createColumnDefs() {
    return [
      {
        headerName: '#', width: 90, suppressSorting: true,
        suppressMenu: true, pinned: true, editable:false, field: "id",
        cellRendererFramework: ProfileMenuComponent

      },
      {headerName: "ID", field: "id", width: 60},
      {headerName: "Name", field: "name", editable: true, width: 100},
      {headerName: "Description", field: "description", editable: true, width: 300}
    ];
  }

  private getProfiles() {
    this.opmService.getProfiles()
      .subscribe(
        locs => this.gridOptions.api.setRowData(locs),
        error => this.errorMessage = <any>error);
  }

  public onCellClicked(event): void {
    console.log('onCellClicked: ' + event.rowIndex + ' ' + event.colDef.field);
    if (event.colDef.headerName === '#') {
      if (event.data.id < 0) {
        this.opmService.createProfile(event.data).subscribe((res) => {
          this.getProfiles();
        },
          error => this.errorMessage = <any>error);
      }
    }
  }

  public onRowValueChanged(event): void {
    console.log('onRowValueChanged: ' + event.data);
    if (event.data.id > 0) {
      this.opmService.updateProfile(event.data).subscribe((res) => {
        this.getProfiles();
      },
        error => this.errorMessage = <any>error);
    } else {
      this.opmService.createProfile(event.data).subscribe((res) => {
        this.getProfiles();
      },
        error => this.errorMessage = <any>error);
    }
  }

  public delete(id: number) {
    this.opmService.deleteProfile(id).subscribe((res) => {
        this.getProfiles();
      },
      error => this.errorMessage = <any>error._body);
  }
}
