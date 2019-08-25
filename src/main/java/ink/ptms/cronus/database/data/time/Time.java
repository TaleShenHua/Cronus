package ink.ptms.cronus.database.data.time;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.util.Utils;
import org.bukkit.util.NumberConversions;

import java.util.Calendar;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:28
 */
public class Time {

    private TimeType type;
    private int day;
    private int hour;
    private int minute;
    private long time;
    private String origin;

    public Time(String libTime) {
        this(Utils.toTime(libTime));
    }

    public Time(long time) {
        this.type = TimeType.TIME;
        this.time = time;
    }

    public Time(int hour, int minute) {
        this.type = TimeType.DAY;
        this.hour = hour;
        this.minute = minute;
    }

    public Time(TimeType type, int day, int hour, int minute) {
        this.type = type;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Time origin(String origin) {
        this.origin = origin;
        return this;
    }

    public boolean isTimeout(DataQuest dataQuest) {
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            case TIME:
                return dataQuest.getTimeStart() + time < System.currentTimeMillis();
            default:
                return isTimeout();
        }
    }

    public boolean isTimeout() {
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            case DAY: {
                long timeout = (hour * 60L) + (minute);
                long current = (calendar.get(Calendar.HOUR_OF_DAY) * 60L) + calendar.get(Calendar.MINUTE);
                return current > timeout;
            }
            case WEEK: {
                long timeout = (day * 60L * 24L) + (hour * 60L) + (minute);
                long current = (calendar.get(Calendar.DAY_OF_WEEK) * 60L * 24L) + (calendar.get(Calendar.HOUR_OF_DAY) * 60L) + calendar.get(Calendar.MINUTE);
                return current > timeout;
            }
            case MONTH: {
                long timeout = (day * 60L * 24L) + (hour * 60L) + (minute);
                long current = (calendar.get(Calendar.DAY_OF_MONTH) * 60L * 24L) + (calendar.get(Calendar.HOUR_OF_DAY) * 60L) + calendar.get(Calendar.MINUTE);
                return current > timeout;
            }
            default:
                return false;
        }
    }

    public boolean isEquals() {
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            case DAY: {
                long timeout = (hour * 60L) + (minute);
                long current = (calendar.get(Calendar.HOUR_OF_DAY) * 60L) + calendar.get(Calendar.MINUTE);
                return current == timeout;
            }
            case WEEK: {
                long timeout = (day * 60L * 24L) + (hour * 60L) + (minute);
                long current = (calendar.get(Calendar.DAY_OF_WEEK) * 60L * 24L) + (calendar.get(Calendar.HOUR_OF_DAY) * 60L) + calendar.get(Calendar.MINUTE);
                return current == timeout;
            }
            case MONTH: {
                long timeout = (day * 60L * 24L) + (hour * 60L) + (minute);
                long current = (calendar.get(Calendar.DAY_OF_MONTH) * 60L * 24L) + (calendar.get(Calendar.HOUR_OF_DAY) * 60L) + calendar.get(Calendar.MINUTE);
                return current == timeout;
            }
            default:
                return false;
        }
    }

    public static Time parse(String in) {
        if (in == null) {
            return null;
        }
        in = in.toLowerCase();
        if (in.equalsIgnoreCase("never") || in.equals("-1")) {
            return null;
        } else if (in.startsWith("day:")) {
            String[] v = in.substring("day:".length()).split(":");
            return new Time(NumberConversions.toInt(v[0]), NumberConversions.toInt(v.length > 1 ? v[1] : 0)).origin(in);
        } else if (in.startsWith("week:")) {
            String[] v = in.substring("week:".length()).split(":");
            return new Time(TimeType.WEEK, NumberConversions.toInt(v[0]), NumberConversions.toInt(v.length > 1 ? v[1] : 0), NumberConversions.toInt(v.length > 2 ? v[2] : 0)).origin(in);
        } else if (in.startsWith("month:")) {
            String[] v = in.substring("month:".length()).split(":");
            return new Time(TimeType.MONTH, NumberConversions.toInt(v[0]), NumberConversions.toInt(v.length > 1 ? v[1] : 0), NumberConversions.toInt(v.length > 1 ? v[2] : 0)).origin(in);
        } else {
            return new Time(in).origin(in);
        }
    }

    public static Time parseNoTime(String in) {
        if (in == null) {
            return null;
        }
        in = in.toLowerCase();
        if (in.startsWith("week:")) {
            String[] v = in.substring("week:".length()).split(":");
            return new Time(TimeType.WEEK, NumberConversions.toInt(v[0]), NumberConversions.toInt(v.length > 1 ? v[1] : 0), NumberConversions.toInt(v.length > 2 ? v[2] : 0)).origin(in);
        } else if (in.startsWith("month:")) {
            String[] v = in.substring("month:".length()).split(":");
            return new Time(TimeType.MONTH, NumberConversions.toInt(v[0]), NumberConversions.toInt(v.length > 1 ? v[1] : 0), NumberConversions.toInt(v.length > 1 ? v[2] : 0)).origin(in);
        } else {
            String[] v = in.split(":");
            return new Time(NumberConversions.toInt(v[0]), NumberConversions.toInt(v.length > 1 ? v[1] : 0)).origin(in);
        }
    }

    public TimeType getType() {
        return type;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public long getTime() {
        return time;
    }

    public String getOrigin() {
        return origin;
    }
}
