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
@Cond(name = "player.blocking", pattern = "player\\.block(ing)?", example = "player.blocking")
public class CondBlocking extends CondBoolean {

    @Override
    public boolean getBoolean(Player player, DataQuest quest, Event event) {
        return player.isBlocking();
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-blocking0");
        } else {
            return TLocale.asString("translate-condition-blocking1");
        }
    }

    @Override
    public String toString() {
        return "CondBlocking{" +
                "negative=" + negative +
                '}';
    }
}
