package ink.ptms.cronus.internal.condition.impl.argument;

import ink.ptms.cronus.database.data.DataQuest;
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
@Cond(name = "effect", pattern = "effect\\.(?<effect>.+)", example = "effect.[effect]")
public class CondEffect extends Condition {

    private boolean negative;
    private String effect;

    @Override
    public void init(Matcher matcher, String text) {
        negative = text.startsWith("!");
        effect = String.valueOf(matcher.group("effect"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return negative != player.getActivePotionEffects().stream().anyMatch(e -> e.getType().getName().equalsIgnoreCase(effect));
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-effect0", effect);
        } else {
            return TLocale.asString("translate-condition-effect1", effect);
        }
    }

    @Override
    public String toString() {
        return "CondEffect{" +
                "negative=" + negative +
                ", effect='" + effect + '\'' +
                '}';
    }
}
