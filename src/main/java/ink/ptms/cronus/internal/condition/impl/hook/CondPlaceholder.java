package ink.ptms.cronus.internal.condition.impl.hook;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
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
@Cond(name = "placeholder", pattern = "(placeholder|papi) (((?<placeholder1>\\S+) (?<expression>.+))|(?<placeholder2>.+))", example = "placeholder [placeholder] [expression]")
public class CondPlaceholder extends Condition {

    private boolean negative;
    private boolean booleanMode;
    private String placeholder;
    private Sxpression expression;

    @Override
    public void init(Matcher matcher, String text) {
        if (booleanMode = (matcher.group("placeholder1") == null)) {
            negative = text.startsWith("!");
            placeholder = matcher.group("placeholder2");
        } else {
            expression = new Sxpression(matcher.group("expression"));
            placeholder = matcher.group("placeholder1");
        }
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        String v = TLocale.Translate.setPlaceholders(player, placeholder);
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
                return TLocale.asString("translate-condition-placeholder0", placeholder);
            } else {
                return TLocale.asString("translate-condition-placeholder1", placeholder);
            }
        } else {
            return TLocale.asString("translate-condition-placeholder2", placeholder + " " + expression.translate());
        }
    }

    @Override
    public String toString() {
        return "CondPlaceholder{" +
                "negative=" + negative +
                ", booleanMode=" + booleanMode +
                ", placeholder='" + placeholder + '\'' +
                ", expression=" + expression +
                '}';
    }
}
