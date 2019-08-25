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
@Cond(name = "player.level", pattern = "player\\.level (?<expression>.+)", example = "player.level [expression]")
public class CondLevel extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return player.getLevel();
    }

    @Override
    public String translate() {
        return TLocale.asString("translate-condition-level", expression.translate());
    }

    @Override
    public String toString() {
        return "CondLevel{" +
                "expression=" + expression +
                '}';
    }
}
