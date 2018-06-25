import { TranslateService } from '@ngx-translate/core';
import { GuiUtil } from '../../../../../common/gui-util';

export class AioGui {
  c = 'AioGui';
  private cfg;
  constructor(
    private guiUtil: GuiUtil
  ) { }
  setCfg(cfg) {
    this.cfg = cfg;
  }
}
