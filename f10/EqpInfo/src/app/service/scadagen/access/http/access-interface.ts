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
  public env: string;
  public address;
  public values;
}

export class HttpAccessWriteResult {
  public key: string;
  public method: HttpAccessResultType;
  public env: string;
  public address;
  public values;
}
