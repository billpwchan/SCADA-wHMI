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

}
