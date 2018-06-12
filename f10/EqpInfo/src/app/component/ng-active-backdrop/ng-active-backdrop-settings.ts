export class NgActiveBackdropSettings {
    public static readonly STR_NOTIFY_PARENT = 'notifyParent';
    public static readonly STR_NOTIFY_FROM_PARENT = 'notifyFromParent';
}

export class NgActiveBackdropDbmCfg {
    public env: string;
    public alias: string;
    public attributes: string[];
}
export class NgActiveBackdropClassCfg {
    public span: string;
}
export class NgActiveBackdropCfg {
    public class: NgActiveBackdropClassCfg;
    public dbm: NgActiveBackdropDbmCfg;
}
