package ink.ptms.cronus.internal.condition.impl.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondBoolean;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.sneaking", pattern = "player\\.sneak(ing)?", example = "player.sneaking")
public class CondSneaking extends CondBoolean {

    @Override
    public boolean getBoolean(Player player, DataQuest quest, Event event) {
        return player.isSneaking();
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-sneaking0");
        } else {
            return TLocale.asString("translate-condition-sneaking1");
        }
    }

    @Override
    public String toString() {
        return "CondSneaking{" +
                "negative=" + negative +
                '}';
    }
}
