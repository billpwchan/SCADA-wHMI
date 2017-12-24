import { Pipe, PipeTransform } from '@angular/core';
import { Card, Step, Equipment, Execution, PhaseType } from '../../model/Scenario';
import { TokenIndex } from './csv-to-card-settings';

/**
 * Pipe to transform the CSV to Cards
 *
 * @export
 * @class CsvToCardsPipe
 * @implements {PipeTransform}
 */
@Pipe({
  name: 'csvToCards'
})
export class CsvToCardsPipe implements PipeTransform {

  c: string = CsvToCardsPipe.name;

  private getEV(evs: Execution[], id: string): Execution {
    for ( let i = 0 ; i < evs.length ; ++i ) {
      if ( evs[i].name === id ) {
        return evs[i];
      }
    }
    return null;
  }

  private getStep(steps: Step[], id: number): Step {
    for ( let i = 0 ; i < steps.length ; ++i ) {
      if ( steps[i].step === id ) {
        return steps[i];
      }
    }
    return null;
  }

  private getCard(cards: Card[], id: string): Card {
    for ( let i = 0 ; i < cards.length ; ++i ) {
      if ( cards[i].name === id ) {
        return cards[i];
      }
    }
    return null;
  }

  transform(csv: any, args: any[]): Card[] {

    const f = 'transform';

    console.log(this.c, f);
    // console.log(this.c, f, 'csv', csv);

    const STR_COMMA = args[0];
    const STR_EOL   = args[1];

    const cards: Card[] = new Array<Card>();
    const lines = csv.split(STR_EOL);
      lines.forEach( line => {
        const token = line.split(STR_COMMA);

        if ( token.length > TokenIndex.CARD_STEP ) {
          // Card Session
          const cName   = token[TokenIndex.CARD_NAME];
          const cState  = Number.parseInt(token[TokenIndex.CARD_STATE]);
          const cStep   = Number.parseInt(token[TokenIndex.CARD_STEP]);

          let card = this.getCard(cards, cName);
          if ( null === card ) {
            card = new Card(cName, cState, cStep);
            cards.push(card);
          } else {
            // Card Exists
          }

          if ( token.length > TokenIndex.EQUIPMENT_POINTLABEL ) {
            // Step Session
            const sStep       = Number.parseInt(token[TokenIndex.STEP_STEP]);
            const sState      = Number.parseInt(token[TokenIndex.STEP_STATE]);
            const sDelay      = Number.parseInt(token[TokenIndex.STEP_DELAY]);
            const sExecute    = (token[TokenIndex.STEP_EXECUTE] === 'true');

            // Equipment Session
            const eConnAddr   = token[TokenIndex.EQUIPMENT_CONNADDR];
            const eUnivname   = token[TokenIndex.EQUIPMENT_UNIVNAME];
            const eClassId    = Number.parseInt(token[TokenIndex.EQUIPMENT_CLASSID]);
            const eGeo        = Number.parseInt(token[TokenIndex.EQUIPMENT_GEO]);
            const eFunc       = Number.parseInt(token[TokenIndex.EQUIPMENT_FUNC]);
            const eEqplabel   = token[TokenIndex.EQUIPMENT_EQPLABEL];
            const ePointlabel = token[TokenIndex.EQUIPMENT_POINTLABEL];
            const eValueLabel = token[TokenIndex.EQUIPMENT_VALUELABEL];

            let step = this.getStep(card.steps, sStep);

            const equipment: Equipment = new Equipment(
              eConnAddr
              , eUnivname
              , eClassId
              , eGeo
              , eFunc
              , eEqplabel
              , ePointlabel
              , eValueLabel
            );

            if ( null === step ) {
              step = new Step(sStep, sState, sDelay, sExecute, equipment);
              card.steps.push(step);
            } else {
              // Step Exists
              step.state = sState;
              step.delay = sDelay;
              step.equipment = equipment;
            }

            if ( token.length > TokenIndex.EXEC_VALUE ) {
              // Phase Session
              const execPhase: PhaseType = Number.parseInt(token[TokenIndex.PHASE_TYPE]);

              let phases: Execution[] = [];
              switch (execPhase) {
                case PhaseType.STOP: {
                  phases = step.equipment.phaseStop;
                } break;
                case PhaseType.START: {
                  phases = step.equipment.phaseStart;
                } break;
              }

              // Execution Session
              const execType  = Number.parseInt(token[TokenIndex.EXEC_TYPE]);
              const execName  = token[TokenIndex.EXEC_NAME];
              const execValue = Number.parseFloat(token[TokenIndex.EXEC_VALUE]);

              const exec: Execution = new Execution(
                                                    execType
                                                  , execName
                                                  , execValue);

              phases.push(exec);
            }
          }
        }
      });
    return cards;
  }
}
