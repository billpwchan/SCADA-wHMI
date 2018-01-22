import { DatatableStep } from './DatatableScenario';
import { Step } from './Scenario';

export class DataScenarioHelper {

  public static readonly c = 'DataScenarioHelper';

  public static isFlagOn(val: number, flag: number): boolean {
    // tslint:disable-next-line:no-bitwise
    return ((val & ( 1 << flag)) !== 0);
  }

  public static setFlagOn(val: number, flag: number): number {
    // tslint:disable-next-line:no-bitwise
    return ( val | ( 1 << flag ) );
  }

  public static setFlagOff(val: number, flag: number): number {
    // tslint:disable-next-line:no-bitwise
    return ( val ^ ( 1 << flag ) );
  }

  public static convertToDatatableStep(step: Step): DatatableStep {
    let dtStep: DatatableStep = null;
    if ( null != step ) {
      dtStep = new DatatableStep(
        String(step.step)
        , String(step.equipment.geo)
        , String(step.equipment.func)
        , step.equipment.eqplabel
        , step.equipment.pointlabel
        , step.equipment.valuelabel
        , String(step.step)
        , new Date()
      );
    }
    return dtStep;
  }

}
