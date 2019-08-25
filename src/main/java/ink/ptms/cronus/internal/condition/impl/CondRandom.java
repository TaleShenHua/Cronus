package ink.ptms.cronus.internal.condition.impl;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.Numbers;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 * <p>
 * random > 0.5
 */
@Cond(name = "random", pattern = "random (?<expression>.+)", example = "random [expression]")
public class CondRandom extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return Numbers.getRandom().nextDouble();
    }

    @Override
    public String translate() {
        return TLocale.asString("translate-condition-random", expression.translate());
    }

    @Override
    public String toString() {
        return "CondRandom{" +
                "expression=" + expression +
                '}';
    }
}
