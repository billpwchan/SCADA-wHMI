import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {RouterModule, Routes} from "@angular/router";
import { HttpModule, JsonpModule } from '@angular/http';

// ag-grid
import {AgGridModule} from "ag-grid-angular/main";
// application
import {AppComponent} from "./app.component";

import { FunctionEditorComponent } from './function-editor/function-editor.component';
import { LocationEditorComponent } from './location-editor/location-editor.component';
import { ActionEditorComponent } from './action-editor/action-editor.component';
import { ProfileEditorComponent } from './profile-editor/profile-editor.component';
import { MaskEditorComponent } from './mask-editor/mask-editor.component';
import { ProfileDetailEditorComponent } from './profile-detail-editor/profile-detail-editor.component';
import { ProfileDeleteComponent } from './profile-delete/profile-delete.component';
import { ProfileMenuComponent } from './profile-editor/profile-menu.component';

const appRoutes: Routes = [
  {path: 'functions', component: FunctionEditorComponent, data: {title: "Function Definition"}},
  {path: 'locations', component: LocationEditorComponent, data: {title: "Location Definition"}},
  {path: 'actions', component: ActionEditorComponent, data: {title: "Action Definition"}},
  {path: 'profiles', component: ProfileEditorComponent, data: {title: "Profile Definition"}},
  {path: 'masks', component: MaskEditorComponent, data: {title: "Mask Definition"}},
  {path: 'profiles/:id', component: ProfileDetailEditorComponent, data: {title: ""}},
  {path: 'profiles/delete/:id', component: ProfileDeleteComponent, data: {title: ""}},
  {path: '', redirectTo: 'profiles', pathMatch: 'full'}
];

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
      HttpModule,
      JsonpModule,
      RouterModule.forRoot(appRoutes),
      AgGridModule.withComponents(
        [
          FunctionEditorComponent
        ])

    ],
    declarations: [
      AppComponent,
      FunctionEditorComponent,
      LocationEditorComponent,
      ActionEditorComponent,
      ProfileEditorComponent,
      MaskEditorComponent,
      ProfileDetailEditorComponent,
      ProfileDeleteComponent,
      ProfileMenuComponent
  ],
    bootstrap: [AppComponent],
  entryComponents: [ProfileMenuComponent]
})
export class AppModule {
}
