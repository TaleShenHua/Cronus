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
@Cond(name = "player.sleeping", pattern = "player\\.sleep(ing)?", example = "player.sleeping")
public class CondSleeping extends CondBoolean {

    @Override
    public boolean getBoolean(Player player, DataQuest quest, Event event) {
        return player.isSleeping();
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-sleeping0");
        } else {
            return TLocale.asString("translate-condition-sleeping1");
        }
    }

    @Override
    public String toString() {
        return "CondSleeping{" +
                "negative=" + negative +
                '}';
    }
}
