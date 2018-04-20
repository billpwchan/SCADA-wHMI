
export class OpmObject {
  id: number;
  name: string;
  description: string;

  constructor(info: any) {

    if (info == null) {
      this.id = -1;
      this.name = "";
      this.description = "";
    } else {
      this.id = info.id;
      this.name = info.name;
      this.description = info.description;
    }
  }
}

export class OpmFunction extends OpmObject {

  category: number;
  family: string;

  constructor(info: any) {
    super(info);

    if (info == null) {
      this.category = 0;
      this.family = "";
    } else {
      this.category = info.category;
      this.family = info.family;
    }
  }
}

export class OpmLocation extends OpmObject {

  category: number;

  constructor(info: any) {
    super(info);

    if (info == null) {
      this.category = 0;
    } else {
      this.category = info.category;
    }
  }
}

export class OpmAction extends OpmObject {

  category: number;
  abbrev: string;

  constructor(info: any) {
    super(info);

    if (info == null) {
      this.category = 0;
      this.abbrev = "";
    } else {
      this.category = info.category;
      this.abbrev = info.abbrev;
    }
  }
}

export class OpmMask {

  id: number;
  mask1: string;
  mask2: string;
  mask3: string;
  mask4: string;
  function: OpmFunction;
  location: OpmLocation;

  constructor(info: any) {
    if (info == null) {
      this.id = -1;
      this.mask1 = "";
      this.mask2 = "";
      this.mask3 = "";
      this.mask4 = "";
      this.function = new OpmFunction(null);
      this.location = new OpmLocation(null);
    } else {
      this.id = info.id;
      this.mask1 = info.mask1;
      this.mask2 = info.mask2;
      this.mask3 = info.mask3;
      this.mask4 = info.mask4;
      this.function = new OpmFunction(null);
      if (info.function != null) {
        this.function.name = info.function.name;
      }
      this.location = new OpmLocation(null);
      if (info.location != null) {
        this.location.name = info.location.name;
      }
    }
  }
}

export class OpmProfile extends OpmObject {

  masks: OpmMask[];

  constructor(info: any) {
    super(info);
    if (info != null) {
      this.masks = info.masks.map(d => {return new OpmMask(d);});
    }
  }
}
