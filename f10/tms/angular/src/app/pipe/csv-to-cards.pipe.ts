import { Pipe, PipeTransform } from '@angular/core';
import { Card } from '../model/Scenario';

@Pipe({
  name: 'csvToCards'
})
export class CsvToCardsPipe implements PipeTransform {

  transform(value: string, args?: any): Card[] {
    const cards: Card[] = new Array<Card>();
    return cards;
  }

}
