import { Pipe, PipeTransform } from '@angular/core';
import { Card, Equipment, PhaseType } from '../../model/Scenario';
import { CsvToCardSettings } from './csv-to-card-settings';

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

  c: string = CardsToCsvPipe.name;

  transform(cards: Card[], args?: any[]): string {

    const f = 'transform';

    console.log(this.c, f);
    // console.log(this.c, f, 'cards', cards);

    const STR_COMMA = args[0];
    const STR_EOL   = args[1];

    let csv = CsvToCardSettings.STR_EMPTY;
    cards.forEach( card => {
      let session1 = CsvToCardSettings.STR_EMPTY;
      session1 += card.name;
      session1 += STR_COMMA + card.state;
      session1 += STR_COMMA + card.step;
      csv += session1 + STR_EOL;

      card.steps.forEach(step => {
        let session2 = CsvToCardSettings.STR_EMPTY;
        session2 += step.step;
        session2 += STR_COMMA + step.state;
        session2 += STR_COMMA + step.delay;
        session2 += STR_COMMA + step.execute;

        const equipment: Equipment = step.equipment;

        session2 += STR_COMMA + equipment.connAddr;
        session2 += STR_COMMA + equipment.univname;
        session2 += STR_COMMA + equipment.classId;
        session2 += STR_COMMA + equipment.geo;
        session2 += STR_COMMA + equipment.func;
        session2 += STR_COMMA + equipment.eqplabel;
        session2 += STR_COMMA + equipment.pointlabel;
        session2 += STR_COMMA + equipment.valuelabel;

        csv +=
              session1
               + STR_COMMA + session2
               + STR_EOL;

        equipment.phaseStop.forEach(exec => {

          let session3 = CsvToCardSettings.STR_EMPTY;
          session3 += PhaseType.STOP;
          session3 += STR_COMMA + exec.execType;
          session3 += STR_COMMA + exec.name;
          session3 += STR_COMMA + exec.value;
          csv +=
                  session1 + STR_COMMA
                + session2 + STR_COMMA
                + session3 + STR_EOL;
        });

        equipment.phaseStart.forEach(exec => {
          let session3 = CsvToCardSettings.STR_EMPTY;
          session3 += PhaseType.START;
          session3 += STR_COMMA + exec.execType;
          session3 += STR_COMMA + exec.name;
          session3 += STR_COMMA + exec.value;
          csv +=
                  session1 + STR_COMMA
                + session2 + STR_COMMA
                + session3 + STR_EOL;
        });
      });
    });
    return csv;
  }

}
