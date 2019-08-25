package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

import java.util.Calendar;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionDate extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "date";
    }

    @Override
    public Object eval(Program program, String... args) {
        Calendar calendar = Calendar.getInstance();
        switch (args[0].toLowerCase()) {
            case "system":
                return System.currentTimeMillis();
            case "era":
                return calendar.get(Calendar.ERA);
            case "year":
            case "years":
                return calendar.get(Calendar.YEAR);
            case "month":
            case "months":
                return calendar.get(Calendar.MONTH);
            case "week.year":
                return calendar.get(Calendar.WEEK_OF_YEAR);
            case "week.month":
                return calendar.get(Calendar.WEEK_OF_MONTH);
            case "day":
            case "date":
                return calendar.get(Calendar.DATE);
            case "day.month":
                return calendar.get(Calendar.DAY_OF_MONTH);
            case "day.year":
                return calendar.get(Calendar.DAY_OF_YEAR);
            case "day.week":
                return calendar.get(Calendar.DAY_OF_WEEK);
            case "day.week.month":
                return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            case "am":
            case "pm":
                return calendar.get(Calendar.AM_PM);
            case "hour":
            case "hours":
                return calendar.get(Calendar.HOUR);
            case "hour.day":
            case "hours.day":
                return calendar.get(Calendar.HOUR_OF_DAY);
            case "minute":
            case "minutes":
                return calendar.get(Calendar.MINUTE);
            case "second":
            case "seconds":
                return calendar.get(Calendar.SECOND);
            case "millisecond":
            case "milliseconds":
                return calendar.get(Calendar.MILLISECOND);
            case "zone.offset":
                return calendar.get(Calendar.ZONE_OFFSET);
            case "dst.offset":
                return calendar.get(Calendar.DST_OFFSET);
            default:
                return "<invalid>";
        }
    }
}
