export class UtilService {
    public static UNIX_TIME_MAX = 2147483647;

    public static isCurrentDate(d: string): boolean {
        const seconds = +d;
        const testDate = new Date(seconds * 1000);
        const currentDate = new Date();

        if (testDate.getFullYear() === currentDate.getFullYear() &&
            testDate.getMonth() === currentDate.getMonth() &&
            testDate.getDate() === currentDate.getDate()) {
            return true;
        }
        return false;
    }

    public static isYesterday(d: string): boolean {
        const seconds = +d;
        const testDate = new Date(seconds * 1000);
        const yesterday = new Date();
        yesterday.setTime(yesterday.getTime() - 86400000);

        if (testDate.getFullYear() === yesterday.getFullYear() &&
            testDate.getMonth() === yesterday.getMonth() &&
            testDate.getDate() === yesterday.getDate()) {
            return true;
        }
        return false;
    }

    public static isDateExpired(dateTime: number, hour: number, minute: number): boolean {
        const seconds = +dateTime;
        const testDate = new Date(seconds * 1000);
        testDate.setHours(hour, minute, 0, 0);
        const currentDate = new Date();
        currentDate.setSeconds(0, 0);

        console.log('{UtilService}', '[isDateExpired]', 'testDate', testDate, testDate.getTime(), 'currentDate', currentDate, currentDate.getTime(),
            testDate.getTime() <= currentDate.getTime());
        return testDate.getTime() <= currentDate.getTime();
    }

    public static includesComingDayOfWeek(d: string, dayofweek: number): boolean {
        const seconds = +d;
        const testDate = new Date(seconds * 1000);
        const weekDate = new Date();

        weekDate.setDate(weekDate.getDate() + (weekDate.getDay() > dayofweek ? (7 - weekDate.getDay() + dayofweek) : (dayofweek - weekDate.getDay())));
        console.log('{UtilService}', '[includesComingDayOfWeek]', 'testDate', testDate, 'dayofweek', dayofweek, 'weekDate', weekDate);

        if (testDate.getFullYear() === weekDate.getFullYear() &&
            testDate.getMonth() === weekDate.getMonth() &&
            testDate.getDate() === weekDate.getDate()) {
            return true;
        }
        return false;
    }

    public static beyondNextWeek(d: string): boolean {
        const seconds = +d;
        const testDate = new Date(seconds * 1000);
        testDate.setHours(0, 0, 0, 0);

        const nextWeekDate = new Date();
        nextWeekDate.setHours(0, 0, 0, 0);
        nextWeekDate.setTime(nextWeekDate.getTime() + (7 * 86400000));

        if (testDate.getTime() > nextWeekDate.getTime()) {
            console.log('{UtilService}', '[beyondNextWeek]', 'testDate', testDate, 'nextWeekDate', nextWeekDate, 'return false');
            return true;
        } else {
            console.log('{UtilService}', '[beyondNextWeek]', 'testDate', testDate, 'nextWeekDate', nextWeekDate, 'return true');
            return false;
        }
    }

    public static getWeekDatesList(weekdays: number[], startDate: Date, duration: number): string[] {
        const datesList = [];
        console.log('{UtilService}', '[getWeekDatesList]', 'days in week', weekdays, 'startDate',
            startDate.getFullYear(), startDate.getMonth() + 1, startDate.getDate());

        const d = new Date(startDate);
        const startTime = d.getTime();
        for (let n = 0; n < duration; n++) {
            d.setTime(startTime + (n * 86400000));
            console.log('{UtilService}', '[getWeekDatesList]', 'n', n, 'date',
                d.getFullYear(), d.getMonth() + 1, d.getDate(), 'weekday', d.getDay());

            const weekday = d.getDay();
            if (weekdays.indexOf(weekday) > -1) {
                const dateStr = (d.getTime() / 1000).toString();
                datesList.push(dateStr);
                console.log('{UtilService}', '[getWeekDatesList]', 'pushed date to datesList',
                    d.getFullYear(), d.getMonth() + 1, d.getDate(), 'weekday', d.getDay());
            }
        }

        return datesList;
    }

    public static getWeekNextDatesList(weekdays: number[], startDate: Date, duration: number): string[] {
        const datesList = [];
        console.log('{UtilService}', '[getWeekNextDatesList]', 'days in week', weekdays,
            'startDate', startDate.getFullYear(), startDate.getMonth() + 1, startDate.getDate());

        const d = new Date(startDate);
        const startTime = d.getTime();
        for (let n = 0; n < duration; n++) {
            d.setTime(startTime + (n * 86400000));
            console.log('{UtilService}', '[getWeekNextDatesList]', 'n', n, 'date',
                d.getFullYear(), d.getMonth() + 1, d.getDate(), 'weekday', d.getDay());

            const weekday = d.getDay();
            if (weekdays.indexOf(weekday) > -1) {
                const dateStr = ((d.getTime() / 1000) + 86400).toString();    // find next day string
                datesList.push(dateStr);
                console.log('{UtilService}', '[getWeekNextDatesList]', 'pushed date to datesList',
                    d.getFullYear(), d.getMonth() + 1, d.getDate(), 'weekday', d.getDay());
            }
        }

        return datesList;
    }

    public static isTimeExpired(hour: number, minute: number): boolean {
        const currentDate = new Date();
        if (hour < currentDate.getHours() || (hour === currentDate.getHours() && minute <= currentDate.getMinutes())) {
            console.log('{UtilService}', '[isTimeExpired]', 'hour', hour, 'minute', minute, 'return true');
            return true;
        }
        console.log('{UtilService}', '[isTimeExpired]', 'hour', hour, 'minute', minute, 'return false');
        return false;
    }
}
