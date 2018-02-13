export class AccessInterface {

}

export enum HttpAccessResultType {
  NEXT = 0
  , ERROR
  , COMPLETE
}

export class HttpAccessResult {
  public key: string;
  public method: HttpAccessResultType;
  public connAddr: string;
  public dbAddresses;
  public dbValues;
}
