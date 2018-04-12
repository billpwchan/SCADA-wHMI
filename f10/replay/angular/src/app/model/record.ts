export enum RecordType {
  SNAPSHOT
  , INCREMENT
}

export class Record {
  public static SNAPSHOT_FILE_PREFIX = 'S_';
  public static INCREMENT_FILE_PREFIX = 'I_';

  public fileName: string;
  public fileDate: number;
  public fileDateStr: string;
  public fileType: RecordType;
}
