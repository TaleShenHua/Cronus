package ink.ptms.cronus.internal.condition.impl.argument;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.util.Sxpression;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.Numbers;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "function", pattern = "func(tion)? (((?<function1>\\S+) (?<expression>.+))|(?<function2>.+))", example = "function [function] [expression]")
public class CondFunction extends Condition {

    private boolean negative;
    private boolean booleanMode;
    private String function;
    private Sxpression expression;

    @Override
    public void init(Matcher matcher, String text) {
        if (booleanMode = (matcher.group("function1") == null)) {
            negative = text.startsWith("!");
            function = matcher.group("function2");
        } else {
            expression = new Sxpression(matcher.group("expression"));
            function = matcher.group("function1");
        }
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        String v = FunctionParser.parseAll(new QuestProgram(player, quest), function);
        if (booleanMode) {
            return negative != Numbers.getBoolean(v);
        } else {
            return expression.getNumber().isNumber() ? expression.isSelect(NumberConversions.toDouble(v)) : expression.isSelect(v);
        }
    }

    @Override
    public String translate() {
        if (booleanMode) {
            if (!negative) {
                return TLocale.asString("translate-condition-function0", function);
            } else {
                return TLocale.asString("translate-condition-function1", function);
            }
        } else {
            return TLocale.asString("translate-condition-function2", function + " " + expression.translate());
        }
    }

    @Override
    public String toString() {
        return "CondFunction{" +
                "negative=" + negative +
                ", booleanMode=" + booleanMode +
                ", function='" + function + '\'' +
                ", expression=" + expression +
                '}';
    }
}
