import { ScheduleItem } from './type/schedule-item'

// export const MockScheduleItemList: ScheduleItem[] = [
//   { scheduleId: 'oneShot', scheduleType:'oneshot', eqtName: 'eqt1',  eqtLabel: 'schedule-item 1 label', geoCat: 1, funcCat: 11, onTime: "10:00", offTime: "10:13"},
//   { scheduleId: 'oneShot', scheduleType:'oneshot', eqtName: 'eqt2',  eqtLabel: 'schedule-item 2 label', geoCat: 2, funcCat: 12, onTime: "10:00", offTime: "10:10"},
//   { scheduleId: 'oneShot', scheduleType:'oneshot', eqtName: 'eqt3',  eqtLabel: 'schedule-item 3 label', geoCat: 3, funcCat: 13, onTime: "10:03", offTime: "10:15"},
//   { scheduleId: 'oneShot', scheduleType:'oneshot', eqtName: 'eqt4',  eqtLabel: 'schedule-item 4 label', geoCat: 4, funcCat: 14, onTime: "10:04", offTime: "10:08"},
//   { scheduleId: 'oneShot', scheduleType:'oneshot', eqtName: 'eqt5',  eqtLabel: 'schedule-item 5 label', geoCat: 5, funcCat: 15, onTime: "10:05", offTime: "11:13"},
//   { scheduleId: 'oneShot', scheduleType:'oneshot', eqtName: 'eqt6',  eqtLabel: 'schedule-item 6 label', geoCat: 6, funcCat: 16, onTime: "9:00", offTime: "9:13"},
//   { scheduleId: 'preDefined1', scheduleType:'predefined', eqtName: 'eqt7',  eqtLabel: 'schedule-item 7 label', geoCat: 7, funcCat: 17, onTime: "9:01", offTime: "9:13"},
//   { scheduleId: 'preDefined1', scheduleType:'predefined', eqtName: 'eqt8',  eqtLabel: 'schedule-item 8 label', geoCat: 8, funcCat: 18, onTime: "8:13", offTime: "10:13"},
//   { scheduleId: 'preDefined2', scheduleType:'predefined', eqtName: 'eqt9',  eqtLabel: 'schedule-item 9 label', geoCat: 9, funcCat: 19, onTime: "8:13", offTime: "11:13"},
//   { scheduleId: 'preDefined2', scheduleType:'predefined', eqtName: 'eqt10',  eqtLabel: 'schedule-item 10 label', geoCat: 10, funcCat: 20, onTime: "10:13", offTime: "11:13"},
//   { scheduleId: 'preDefined2', scheduleType:'predefined', eqtName: 'eqt11',  eqtLabel: 'schedule-item 11 label', geoCat: 1, funcCat: 11, onTime: "10:13", offTime: "11:13"},
//   { scheduleId: 'preDefined2', scheduleType:'predefined', eqtName: 'eqt12',  eqtLabel: 'schedule-item 12 label', geoCat: 2, funcCat: 12, onTime: "10:13", offTime: "11:13"},
//   { scheduleId: 'userDefined1', scheduleType:'userdefined', eqtName: 'eqt13',  eqtLabel: 'schedule-item 13 label', geoCat: 3, funcCat: 13, onTime: "10:13", offTime: "11:13"},
//   { scheduleId: 'userDefined1', scheduleType:'userdefined', eqtName: 'eqt14',  eqtLabel: 'schedule-item 14 label', geoCat: 4, funcCat: 14, onTime: "11:13", offTime: "12:13"},
//   { scheduleId: 'userDefined1', scheduleType:'userdefined', eqtName: 'eqt15',  eqtLabel: 'schedule-item 15 label', geoCat: 5, funcCat: 15, onTime: "12:13", offTime: "13:13"},
//   { scheduleId: 'userDefined1', scheduleType:'userdefined', eqtName: 'eqt16',  eqtLabel: 'schedule-item 16 label', geoCat: 6, funcCat: 16, onTime: "10:13", offTime: "10:13"},
//   { scheduleId: 'userDefined2', scheduleType:'userdefined', eqtName: 'eqt17',  eqtLabel: 'schedule-item 17 label', geoCat: 7, funcCat: 17, onTime: "9:13", offTime: "10:13"},
//   { scheduleId: 'userDefined2', scheduleType:'userdefined', eqtName: 'eqt18',  eqtLabel: 'schedule-item 18 label', geoCat: 8, funcCat: 18, onTime: "8:13", offTime: "9:13"},
//   { scheduleId: 'userDefined2', scheduleType:'userdefined', eqtName: 'eqt19',  eqtLabel: 'schedule-item 19 label', geoCat: 9, funcCat: 19, onTime: "10:11", offTime: "10:13"},
//   { scheduleId: 'userDefined2', scheduleType:'userdefined', eqtName: 'eqt20',  eqtLabel: 'Equipment 20 label', geoCat: 10, funcCat: 20, onTime: "10:13", offTime: "10:13"},
// ];
