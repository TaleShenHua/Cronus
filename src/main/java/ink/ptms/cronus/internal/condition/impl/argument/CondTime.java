package ink.ptms.cronus.internal.condition.impl.argument;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.database.data.time.Time;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "time", pattern = "time (?<symbol>>|>=|<|<=|==|=|!=)[ ]?(?<time>.+)", example = "time [symbol] [time]")
public class CondTime extends Condition {

    private String symbol;
    private Time time;

    @Override
    public void init(Matcher matcher, String text) {
        symbol = matcher.group("symbol");
        time = Time.parseNoTime(matcher.group("time"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        switch (symbol) {
            case ">":
                return time.isTimeout();
            case ">=":
                return time.isTimeout() || time.isEquals();
            case "<":
                return !time.isTimeout();
            case "<=":
                return !time.isTimeout() || time.isEquals();
            case "=":
            case "==":
                return time.isEquals();
            case "!=":
                return !time.isEquals();
        }
        return false;
    }

    @Override
    public String translate() {
        switch (symbol) {
            case ">":
                return TLocale.asString("translate-condition-time", TLocale.asString("translate-expression-0"), time.getOrigin());
            case ">=":
                return TLocale.asString("translate-condition-time", TLocale.asString("translate-expression-1"), time.getOrigin());
            case "<":
                return TLocale.asString("translate-condition-time", TLocale.asString("translate-expression-2"), time.getOrigin());
            case "<=":
                return TLocale.asString("translate-condition-time", TLocale.asString("translate-expression-3"), time.getOrigin());
            case "=":
            case "==":
                return TLocale.asString("translate-condition-time", TLocale.asString("translate-expression-4"), time.getOrigin());
            case "!=":
                return TLocale.asString("translate-condition-time", TLocale.asString("translate-expression-5"), time.getOrigin());
        }
        return "?";
    }

    @Override
    public String toString() {
        return "CondTime{" +
                "symbol='" + symbol + '\'' +
                ", time=" + time +
                '}';
    }
}
