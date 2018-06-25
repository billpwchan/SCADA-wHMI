export class AioSourceUtil {
  readonly c = 'AioSourceUtil';
  getPointLvAttributeValue(result, dbm, attribute): any {
    const f = 'getPointLvAttributeValue';
    const alias = dbm.alias;
    console.log(this.c, f, 'alias', alias);
    const point = dbm.attributes['point'];
    console.log(this.c, f, 'point', point);
    const attributeAlias = alias + point + dbm.attributes[attribute];
    console.log(this.c, f, 'attributeAlias', attributeAlias);
    const ret = result[attributeAlias];
    console.log(this.c, f, 'attributeAlias', attributeAlias, 'ret', ret);
    return ret;
  }
}
