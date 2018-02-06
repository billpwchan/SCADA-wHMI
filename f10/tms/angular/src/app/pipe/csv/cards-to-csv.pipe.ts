import { Pipe, PipeTransform } from '@angular/core';
import { Card, Equipment } from '../../model/Scenario';
import { CsvToCardSettings } from './csv-to-card-settings';
import { DacSimExecType } from '../../service/scs/dac-sim-settings';

/**
 * Pipe to transform Cards to Csv
 *
 * @export
 * @class CardsToCsvPipe
 * @implements {PipeTransform}
 */
@Pipe({
  name: 'cardsToCsv'
})
export class CardsToCsvPipe implements PipeTransform {

  readonly c = 'CardsToCsvPipe';

  transform(cards: Card[], args?: any[]): string {
    const f = 'transform';
    console.log(this.c, f);
    console.log(this.c, f, 'cards', cards);

    const fieldSeparator  = args[0];
    const quoteStrings    = args[1];
    const eolChar         = args[2];

    let csv = CsvToCardSettings.STR_EMPTY;

    // Card Session
    cards.forEach( card => {
      let session1 =  this.createField(CsvToCardSettings.STR_EMPTY, card.name, quoteStrings);
      session1 = this.concatField(session1, card.state, fieldSeparator, quoteStrings);
      session1 = this.concatField(session1, card.step, fieldSeparator, quoteStrings);

      // Step Session
      card.steps.forEach(step => {

        let session2 =  this.createField(CsvToCardSettings.STR_EMPTY, step.step, quoteStrings);
        session2 = this.concatField(session2, step.state, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, step.delay, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, step.execute, fieldSeparator, quoteStrings);

        const equipment: Equipment = step.equipment;

        session2 = this.concatField(session2, equipment.connAddr, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.envlabel, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.univname, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.classId, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.geo, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.func, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.eqplabel, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.pointlabel, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.initlabel, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.valuelabel, fieldSeparator, quoteStrings);
        session2 = this.concatField(session2, equipment.currentlabel, fieldSeparator, quoteStrings);

        // Controls Session
        csv +=  session1
              + fieldSeparator + session2;

        for ( let i = 0 ; i < Object.keys(DacSimExecType).length ; ++i ) {
          equipment.phases[i].forEach(exec => {

            let session3 =  this.createField(CsvToCardSettings.STR_EMPTY, i, quoteStrings);
            session3 = this.concatField(session3, exec.execType, fieldSeparator, quoteStrings);
            session3 = this.concatField(session3, exec.name, fieldSeparator, quoteStrings);
            session3 = this.concatField(session3, exec.value, fieldSeparator, quoteStrings);

            csv += fieldSeparator + session3;
          });
        }
        csv += eolChar;

      });
    });
    return csv;
  }

  private createField(str: string, field: any, quoteStrings: string): string {
    const ret = str + quoteStrings + field + quoteStrings;
    return ret;
  }

  private concatField(str: string, field: any, fieldSeparator: string, quoteStrings: string): string {
    const ret = str + fieldSeparator + quoteStrings + field + quoteStrings;
    return ret;
  }

}
