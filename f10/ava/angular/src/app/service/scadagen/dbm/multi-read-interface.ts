export enum MultiReadResultType {
  NEXT = 0
  , ERROR
  , COMPLETE
}

export class MultiReadResult {
  public key: string;
  public result: MultiReadResultType;
  public connAddr: string;
  public dbaddress: string[];
  public dbvalue;
}
