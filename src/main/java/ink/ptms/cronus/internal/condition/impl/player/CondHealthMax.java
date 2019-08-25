package ink.ptms.cronus.internal.condition.impl.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.health.max", pattern = "player\\.health\\.max (?<expression>.+)", example = "player.health.max [expression]")
public class CondHealthMax extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return player.getHealth();
    }

    @Override
    public String translate() {
        return TLocale.asString("translate-condition-health-max", expression.translate());
    }

    @Override
    public String toString() {
        return "CondHealthMax{" +
                "expression=" + expression +
                '}';
    }
}
