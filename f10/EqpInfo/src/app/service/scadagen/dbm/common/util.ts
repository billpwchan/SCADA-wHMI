export class DbmUtils {
  public joinDbmAlias(... args: string[]): string {
    return args.join('');
  }
  public joinDbmAliasWith(join: string, ... args: string[]): string {
    return args.join(join);
  }
}
