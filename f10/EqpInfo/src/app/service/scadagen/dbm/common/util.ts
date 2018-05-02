export class DbmUtils {
  public joinDbmAlias(... args: string[]): string {
    return args.join(':');
  }
}
