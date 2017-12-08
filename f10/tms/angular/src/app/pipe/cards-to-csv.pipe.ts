import { Pipe, PipeTransform } from '@angular/core';
import { Card, Equipment } from '../model/Scenario';

@Pipe({
  name: 'cardsToCsv'
})
export class CardsToCsvPipe implements PipeTransform {

  private static readonly STR_EMPTY = '';
  private static readonly STR_COMMA = ',';
  private static readonly STR_LDRN  = '\r\n';

  transform(cards: Card[], args?: any): string {
    let csv = CardsToCsvPipe.STR_EMPTY;
    cards.forEach( card => {
      let session1 = CardsToCsvPipe.STR_EMPTY;
      session1 += card.name;
      session1 += CardsToCsvPipe.STR_COMMA + card.state;
      session1 += CardsToCsvPipe.STR_COMMA + card.step;
      csv += session1 + CardsToCsvPipe.STR_LDRN;

      card.steps.forEach(step => {
        let session2 = CardsToCsvPipe.STR_EMPTY;
        session2 += step.step;
        session2 += CardsToCsvPipe.STR_COMMA + step.state;
        session2 += CardsToCsvPipe.STR_COMMA + step.delay;

        const equipment: Equipment = step.equipment;

        session2 += CardsToCsvPipe.STR_COMMA + equipment.connAddr;
        session2 += CardsToCsvPipe.STR_COMMA + equipment.univname;
        session2 += CardsToCsvPipe.STR_COMMA + equipment.classId;
        session2 += CardsToCsvPipe.STR_COMMA + equipment.geo;
        session2 += CardsToCsvPipe.STR_COMMA + equipment.func;
        session2 += CardsToCsvPipe.STR_COMMA + equipment.eqplabel;
        session2 += CardsToCsvPipe.STR_COMMA + equipment.pointlabel;
        
        csv += 
              session1
               + CardsToCsvPipe.STR_COMMA + session2
               + CardsToCsvPipe.STR_LDRN;

        equipment.ev.forEach(ev => {
          let session3 = CardsToCsvPipe.STR_EMPTY;
          session3 += ev.name;
          session3 += CardsToCsvPipe.STR_COMMA + ev.value.stop;
          session3 += CardsToCsvPipe.STR_COMMA + ev.value.start;
          csv +=
                 session1 + CardsToCsvPipe.STR_COMMA
               + session2 + CardsToCsvPipe.STR_COMMA
               + session3 + CardsToCsvPipe.STR_LDRN;
        });
      });
    });
    return csv;
  }

}
