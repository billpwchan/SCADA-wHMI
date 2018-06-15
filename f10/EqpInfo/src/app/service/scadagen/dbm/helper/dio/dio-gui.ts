import { TranslateService } from '@ngx-translate/core';
import { GuiUtil } from '../../../../../common/gui-util';

export class DioGui {
  c = 'DioGui';
  private cfg;
  constructor(
    private guiUtil: GuiUtil
  ) { }
  setCfg(cfg) {
    this.cfg = cfg;
  }
  getDioValueTableWithTranslate(valueTable: any): any {
    const f = 'getDioValueTableWithTranslate';
    const selectValues = [];
    const labels = valueTable['label'];
    console.log(this.c, f, 'labels.length', labels.length);
    for (let i = 0; i < labels.length; ++i) {
      const label = labels[i];
      if (null != label && label.length > 0) {
        const selectValue = [];
        selectValue['name'] = valueTable['name'][i];
        selectValue['label'] = label;
        selectValue['value'] = valueTable['value'][i];
        selectValue['id'] = i;
        selectValue['translation'] = this.guiUtil.getDisplayMessage(label);
        selectValues.push(selectValue);
        console.log(this.c, f, 'selectValue', selectValue);
      }
    }
    console.log(this.c, f, 'selectValues', selectValues);
    return selectValues;
  }

  getDovAddress(dynaimicData, dovAddress, id): string {
    const f = 'getDovAddress';
    let alias: string;
console.log(this.c, f, 'dynaimicData', dynaimicData);
console.log(this.c, f, 'dovAddress', dovAddress);
    console.log(this.c, f, 'this.cfg', this.cfg);
    if (null != dynaimicData) {
      alias = dovAddress['.initCondGL'][id];
    }
    return alias;
  }

  getDovAddressValue(dynaimicData, dovAddress, id): boolean {
    const f = 'getDovAddressValue';
console.log(this.c, f, 'dynaimicData', dynaimicData);
console.log(this.c, f, 'event', event);
console.log(this.c, f, 'dovAddress', dovAddress);
    let disabled = true;
    console.log(this.c, f, 'this.cfg', this.cfg);
    if (null != id) {
      const alias = this.getDovAddress(dynaimicData, dovAddress, id);
      console.log(this.c, f, 'alias', alias);
      const cond = dynaimicData[alias];
      console.log(this.c, f, 'cond', cond);
      disabled = (0 === cond);
    }
    console.log(this.c, f, 'enabled', disabled);
    return disabled;
  }

}
