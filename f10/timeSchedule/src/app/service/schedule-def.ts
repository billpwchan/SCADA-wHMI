export class ScheduleDef {
    public static SCHEDULE_TABLE_HEADER = 'sch_s';
    public static SCHEDULE_TASK_HEADER = 'sch_t';
    // sch_table/sch_task columns in tsc task name 
    public static SCHEDULE_HEADER_COL = 0;
    public static SCHEDULE_TYPE_COL = 1;
    public static SCHEDULE_ID_COL = 2;
    // sch_task columns in tsc task name
    public static SCHEDULE_FUNC_COL = 3;
    public static SCHEDULE_GEO_COL = 4;
    public static SCHEDULE_EQT_ALIAS_COL = 5;
    public static SCHEDULE_EQT_POINT_ATT_COL = 6;
    public static SCHEDULE_EQT_TARGET_STATE = 7;
    // sch_table columns in tsc task description
    public static SCHEDULE_TABLE_TYPE_DESC_COL = 0;
    public static SCHEDULE_TABLE_TITLE_COL = 1;
    public static SCHEDULE_RUNNING_STATUE_COL = 2;
    public static SCHEDULE_VISIBILITY_COL = 3;
    public static SCHEDULE_PERIODIC_COL = 4;
    public static SCHEDULE_TITLE_READ_ONLY_COL = 5;
    public static SCHEDULE_TIME_READ_ONLY_COL = 6;
    public static SCHEDULE_EQT_LIST_READ_ONLY_COL = 7;
    // sch_task columns in tsc task description
    public static SCHEDULE_EQT_LABEL_COL = 0;
    public static SCHEDULE_EQT_DESCRIPTION_COL = 1;

    // schedule visibility
    public static VISIBLE = 'visible';
    public static INVISIBLE = 'invisible';

    // schedule status
    public static STARTED = 'started';
    public static STOPPED = 'stopped';

    // daygroup config
    public static PLAN_DAY_GROUP = 'plan_day_group';
    public static PLAN_NEXT_DAY_GROUP = 'plan_next_day_group';
    public static RUN_DAY_GROUP = 'run_day_group';
    public static RUN_NEXT_DAY_GROUP = 'run_next_day_group';
}
