import { Pipe, PipeTransform } from '@angular/core';
import { Card, Step, Equipment, Execution } from '../../model/Scenario';
import { TokenIndex, ExecutionIndex } from './csv-to-card-settings';
import { DacSimExecType } from '../../service/scs/dac-sim-settings';

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

  readonly c = 'CsvToCardsPipe';

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
    console.log(this.c, f, 'csv', csv);

    const fieldSeparator  = args[0];
    const quoteStrings    = args[1];
    const eolChar         = args[2];

    const cards: Card[] = new Array<Card>();
    const lines = csv.split(eolChar);
    for ( let lineIndex = 0 ; lineIndex < lines.length ; ++lineIndex ) {
      const line = lines[lineIndex];

      // Validation
      const reg = new RegExp(/((?:[^",]|(?:"(?:\\{2}|\\"|[^"])*?"))*)/);

      const valid = reg.test(line);
      console.log(this.c, f, 'valid', valid);
      if ( valid ) {
        // Split Token
        let token = line.split(/((?:[^",]|(?:"(?:\\{2}|\\"|[^"])*?"))*)/);
        console.log(this.c, f, 'token', token);

        token.splice(0, 1);
        console.log(this.c, f, 'token', token);
        token.splice(token.length - 1, 1);
        console.log(this.c, f, 'token', token);

        if ( token.length > 0 ) {

          token = token.filter(e => e !== fieldSeparator);

          if ( token.length > 0 ) {
            for ( let i = 0 ; i < token.length ; ++i ) {
              if ( token[i].length > 2 ) {
                if (quoteStrings === token[i].charAt(0) && quoteStrings === token[i].charAt(token[i].length - 1)) {
                  token[i] = token[i].substr(1, token[i].length - 2);
                }
              }
            }
          }

          console.log(this.c, f, 'token', token);
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
              const eEnvLabel   = token[TokenIndex.EQUIPMENT_ENVLABEL];
              const eUnivname   = token[TokenIndex.EQUIPMENT_UNIVNAME];
              const eClassId    = Number.parseInt(token[TokenIndex.EQUIPMENT_CLASSID]);
              const eGeo        = Number.parseInt(token[TokenIndex.EQUIPMENT_GEO]);
              const eFunc       = Number.parseInt(token[TokenIndex.EQUIPMENT_FUNC]);
              const eEqplabel   = token[TokenIndex.EQUIPMENT_EQPLABEL];
              const ePointlabel = token[TokenIndex.EQUIPMENT_POINTLABEL];
              const eInitLabel  = token[TokenIndex.EQUIPMENT_INITLABEL];
              const eValueLabel = token[TokenIndex.EQUIPMENT_VALUELABEL];
              const eCurrentLabel = token[TokenIndex.EQUIPMENT_CURRENTLABEL];

              let step = this.getStep(card.steps, sStep);

              if ( null === step ) {
                step = new Step(sStep, sState, sDelay, sExecute);
                card.steps.push(step);
              } else {
                // Step Exists
                step.state = sState;
                step.delay = sDelay;
              }

              if ( ! step.equipment ) {
                step.equipment = new Equipment(
                  eConnAddr
                  , eEnvLabel
                  , eUnivname
                  , eClassId
                  , eGeo
                  , eFunc
                  , eEqplabel
                  , ePointlabel
                  , eInitLabel
                  , eValueLabel
                  , eCurrentLabel
                );
              }

              if ( token.length > TokenIndex.EXECUTION_BASE ) {

                if ( ! step.equipment.phases) {
                  step.equipment.phases = [];
                }

                // Execution Session
                const tokenExecs: string[] = token.slice(Number(TokenIndex.EXECUTION_BASE));
                console.log(this.c, f, 'token', token);
                console.log(this.c, f, 'tokenExecs', tokenExecs);
                for ( let i = 0 ; i < Object.keys(DacSimExecType).length ; ++i ) {
                  if ( ! step.equipment.phases[i] ) {
                    step.equipment.phases[i] = [];
                  }
                  const execs = this.getExecutions(tokenExecs, i);
                  execs.forEach ( (exec: Execution) => {
                    step.equipment.phases[i].push(exec);
                  });
                }
              } else {
                console.warn(this.c, f, 'No Execution session found at line[' + lineIndex + ']');
              }
            } else {
              console.warn(this.c, f, 'No Equipment and Execution session found at line[' + lineIndex + ']');
            }
          } else {
            console.warn(this.c, f, 'No Step, Equipment and Execution session found at line[' + lineIndex + ']');
          }
        } else {
          console.warn(this.c, f, 'Token IS ZERO');
        }
      } else {
        console.warn(this.c, f, 'Invalid CSV lineIndex[' + lineIndex + '], line[' + line + ']');
      }
    }
    return cards;
  }

  private getExecutions(token: string[], phaseType: number): Execution[] {
    const f = 'getExecutions';
    console.log(this.c, f);
    const ret = new Array<Execution>();

    const lenOfExecutionIndex = Object.keys(ExecutionIndex).length / 2;
    const executionCount = token.length / lenOfExecutionIndex;
    for ( let i = 0, y = 0 ; i < executionCount ; ++i, y = i * lenOfExecutionIndex ) {
      const execPhase: DacSimExecType = Number.parseInt(token[ y + ExecutionIndex.PHASE_TYPE]);
      if ( execPhase === phaseType ) {
        // Execution Session
        const execType  = Number.parseInt(token[ y + ExecutionIndex.EXEC_TYPE]);
        const execName  = token[ y + ExecutionIndex.EXEC_NAME];
        const execValue = Number.parseFloat(token[ y + ExecutionIndex.EXEC_VALUE]);
        const exec: Execution
                    = new Execution(
                                      execType
                                    , execName
                                    , execValue);
        ret.push(exec);
      }
    }
    return ret;
  }
}
