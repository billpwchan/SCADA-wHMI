import { NgModule, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

// @Injectable()

@NgModule({
  imports: [CommonModule],
  declarations: []
})

export class UtilsHttpModule {

  static readonly c = UtilsHttpModule.name;

  httpClientHandlerError(func: string, err: HttpErrorResponse, mgs?: string): void {
    const f = 'httpClientHandlerError';
    console.log(f, 'func', func, 'error', err);
    console.log(f, 'func', func, 'error.error', err.error);
    if (err.error instanceof Error) {
      console.log(func, 'Client-side error occured.');
    } else {
      console.log(func, 'Server-side error occured.');
    }
  }

  httpClientHandlerComplete(func: string, msg?: string): void {
    const f = 'httpClientHandlerComplete';
    console.log(f, 'func', func, 'Complete msg', msg);
  }
 }
