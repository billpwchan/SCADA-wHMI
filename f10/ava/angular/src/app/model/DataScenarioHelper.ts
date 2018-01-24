import { DatatableStep } from './DatatableScenario';
import { Step } from './Scenario';

export enum AlarmValueType {
  USE_SINGLE_BIT = 0
  , USE_MULIT_BIT
  , USE_VALUE
}

export class DataScenarioHelper {

  public static readonly c = 'DataScenarioHelper';

  public static isSingleValueType(valueType: AlarmValueType) {
    return (AlarmValueType.USE_VALUE === valueType || AlarmValueType.USE_SINGLE_BIT === valueType);
  }

  public static isFlagOn(val: number, flag: number, valueType: AlarmValueType): boolean {
    const f = 'isFlagOn';
    let ret = false;
    switch (valueType) {
      case AlarmValueType.USE_VALUE: {
        ret = ((val !== 0) && (val === flag));
      } break;
      case AlarmValueType.USE_SINGLE_BIT: {
        // tslint:disable-next-line:no-bitwise
        ret = ((val & ( 1 << flag)) !== 0);
      } break;
      case AlarmValueType.USE_MULIT_BIT: {
        // tslint:disable-next-line:no-bitwise
        ret = ((val & ( 1 << flag)) !== 0);
      } break;
    }
    console.log(this.c, f, 'ret[' + ret + '] val[' + val + '] flag[' + flag + '] valueType[' + valueType + ']');
    return ret;
  }

  public static setFlagOn(val: number, flag: number, valueType: AlarmValueType): number {
    const f = 'setFlagOn';
    let ret = 0;
    switch (valueType) {
      case AlarmValueType.USE_VALUE: {
        ret = flag;
      } break;
      case AlarmValueType.USE_SINGLE_BIT: {
        val = 0;
        // tslint:disable-next-line:no-bitwise
        ret = ( val | ( 1 << flag ) );
      } break;
      case AlarmValueType.USE_MULIT_BIT: {
        // tslint:disable-next-line:no-bitwise
        ret = ( val | ( 1 << flag ) );
      } break;
    }
    console.log(this.c, f, 'ret[' + ret + '] val[' + val + '] flag[' + flag + '] valueType[' + valueType + ']');
    return ret;
  }

  public static setFlagOff(val: number, flag: number, valueType: AlarmValueType): number {
    const f = 'setFlagOff';
    let ret = 0;
    switch (valueType) {
      case AlarmValueType.USE_VALUE: {
        ret = 0;
      } break;
      case AlarmValueType.USE_SINGLE_BIT: {
        ret = 0;
      } break;
      case AlarmValueType.USE_MULIT_BIT: {
        // tslint:disable-next-line:no-bitwise
        ret = ( val ^ ( 1 << flag ) );
      } break;
    }
    console.log(this.c, f, 'ret[' + ret + '] val[' + val + '] flag[' + flag + '] valueType[' + valueType + ']');
    return ret;
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
