export class AccessInterface {

}

export enum HttpAccessResultType {
  NEXT = 0
  , ERROR
  , COMPLETE
}

export class HttpAccessReadResult {
  public key: string;
  public method: HttpAccessResultType;
  public connAddr: string;
  public dbAddresses;
  public dbValues;
}

export class HttpAccessWriteResult {
  public key: string;
  public method: HttpAccessResultType;
  public connAddr: string;
  public dbAddresses;
  public dbValues;
}
