import { Component, OnInit } from '@angular/core';
import {GridOptions} from "ag-grid/main";
import {OpmService} from "../opm.service";
import {OpmFunction, OpmLocation} from "../model";

@Component({
  selector: 'app-mask-editor',
  templateUrl: './mask-editor.component.html',
  styleUrls: ['./mask-editor.component.css']
})
export class MaskEditorComponent implements OnInit {

  public errorMessage: string;
  public functionNames: string[];
  public locationNames: string[];

  ngOnInit() {
    this.errorMessage = "";
  }

  public gridOptions: GridOptions;

  constructor(private opmService: OpmService) {
    this.gridOptions = <GridOptions>{};
    this.gridOptions.columnDefs = this.createColumnDefs();
    this.getFunctions();
    this.getLocations();
    this.getMasks();
  }

  private createColumnDefs() {
    return [
      {
        headerName: '#', width: 60, suppressSorting: true,
        suppressMenu: true, pinned: true, editable:false, field: "id",
        cellRenderer: function(params) {
          var eDiv = document.createElement('div');
          if (params.data.id > 0) {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Del</button>';
          } else {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Add</button>';
          }
          return eDiv;
        }
      },
      {headerName: "ID", field: "id", width: 60},
      {headerName: "Function", field: "function.name", editable: true, width: 100},
      {headerName: "Location", field: "location.name", editable: true, width: 100},
      {headerName: "Mask1", field: "mask1", editable: true, width: 100},
      {headerName: "Mask2", field: "mask2", editable: true, width: 100},
      {headerName: "Mask3", field: "mask3", editable: true, width: 100},
      {headerName: "Mask4", field: "mask4", editable: true, width: 100}

    ];
  }

  private setColumnDefs() {
    let cdef = [
      {
        headerName: '#', width: 60, suppressSorting: true,
        suppressMenu: true, pinned: true, editable:false, field: "id",
        cellRenderer: function(params) {
          var eDiv = document.createElement('div');
          if (params.data.id > 0) {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Del</button>';
          } else {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Add</button>';
          }
          return eDiv;
        }
      },
      {headerName: "ID", field: "id", width: 60},
      {headerName: "Function", cellEditor: 'select', cellEditorParams:{values:this.functionNames}, field: "function.name",
        editable: true, width: 100},
      {headerName: "Location", cellEditor: 'select', cellEditorParams:{values:this.locationNames}, field: "location.name",
        editable: true, width: 100},
      {headerName: "Mask1", field: "mask1", tooltipField: "location.description", editable: true, width: 100},
      {headerName: "Mask2", field: "mask2", editable: true, width: 100},
      {headerName: "Mask3", field: "mask3", editable: true, width: 100},
      {headerName: "Mask4", field: "mask4", editable: true, width: 100}

    ];

    this.gridOptions.api.setColumnDefs(cdef);
  }

  private getFunctions() {
    this.opmService.getFunctions()
      .subscribe(
        fcts => this.setFunctionNames(fcts),
        error => this.errorMessage = <any>error,
        () => console.log('getFunctions onCompleted'));
  }

  private getLocations() {
    this.opmService.getLocations()
      .subscribe(
        locs => this.setLocationNames(locs),
        error => this.errorMessage = <any>error);
  }

  private getMasks() {
    this.errorMessage = "";
    this.opmService.getMasks()
      .subscribe(
        fcts => this.gridOptions.api.setRowData(fcts),
        error => this.errorMessage = <any>error,
        () => console.log('getMasks onCompleted'));
  }

  public onCellClicked(event): void {
    console.log('onCellClicked: ' + event.rowIndex + ' ' + event.colDef.field);
    this.errorMessage = "";
    if (event.colDef.headerName === '#') {
      if (event.data.id > 0) {
        this.opmService.deleteMask(event.data.id)
          .subscribe(res => this.getMasks(),
            error => this.errorMessage = <any>error,
            () => console.log('deleteMask onCompleted'));
      } else {
        this.opmService.createMask(event.data)
          .subscribe(res => this.getMasks(),
            error => this.errorMessage = <any>error);
      }
    }
  }

  public onRowValueChanged(event): void {
    console.log('onRowValueChanged: ' + event.data);
    this.errorMessage = "";
    if (event.data.id > 0) {
      this.opmService.updateMask(event.data)
        .subscribe(
          res => this.getMasks(),
          error => this.errorMessage = <any>error);
    } else {
      this.opmService.createMask(event.data)
        .subscribe(
          res => this.getMasks(),
          error => this.errorMessage = <any>error);
    }
  }

  private setFunctionNames(fcts: OpmFunction[]) {
    this.functionNames = fcts.map(d => d.name);
    this.setColumnDefs();
  }

  private setLocationNames(locs: OpmLocation[]) {
    this.locationNames = locs.map(d => d.name);
    this.setColumnDefs();
  }
}


