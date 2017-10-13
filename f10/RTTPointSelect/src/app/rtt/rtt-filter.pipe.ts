import { Pipe, PipeTransform } from '@angular/core';

import { Olsdata } from './Olsdata';

@Pipe({
  name: 'olsfilter',
  pure: false
})
export class RttFilterPipe implements PipeTransform {
  transform(items: Olsdata[], filter: Olsdata): Olsdata[] {
    if (!items || !filter) {
      return items;
    }
    // filter items array, items which match and return true will be kept, false will be filtered out
    return items.filter((item: Olsdata) => this.applyFilter(item, filter));
  }

  /**
   * Perform the filtering.
   *
   * @param {Olsdata} ols The Olsdata to compare to the filter.
   * @param {Olsdata} filter The filter to apply.
   * @return {boolean} True if Olsdata satisfies filters, false if not.
   */
  applyFilter(ols: Olsdata, filter: Olsdata): boolean {
    for (const field in filter) {
      if (filter[field]) {
        if (typeof filter[field] === 'string') {
          if (ols[field].toLowerCase().indexOf(filter[field].toLowerCase()) === -1) {
            return false;
          }
        } else if (typeof filter[field] === 'number') {
          if (ols[field] !== filter[field]) {
            return false;
          }
        }
      }
    }
    return true;
  }
}
