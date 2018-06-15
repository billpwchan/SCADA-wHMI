import { TranslateService } from '@ngx-translate/core';

export class GuiUtil {
  c = 'GuiUtil';
  constructor(
    private translate: TranslateService
  ) { }
  getDisplayMessage(key: string): string {
    const f = 'getDisplayMessage';
    let label = key;
    if (null != key) {
      if (key.length > 0) {
        label = this.translate.instant(key);
      } else {
        label = '-';
      }
    } else {
      label = 'N/A';
    }
    return label;
  }
}
