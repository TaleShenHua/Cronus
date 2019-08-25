package ink.ptms.cronus.internal.condition.impl.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondString;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.gamemode", pattern = "player\\.gamemode (?<expression>.+)", example = "player.gamemode [expression]")
public class CondGameMode extends CondString {

    @Override
    public String getString(Player player, DataQuest quest, Event event) {
        return player.getGameMode().name();
    }

    @Override
    public String translate() {
        if (expression.getSymbol().startsWith("=")) {
            return TLocale.asString("translate-condition-gamemode0", expression.getNumber().getSource());
        } else {
            return TLocale.asString("translate-condition-gamemode1", expression.getNumber().getSource());
        }
    }

    @Override
    public String toString() {
        return "CondGameMode{" +
                "expression=" + expression +
                '}';
    }
}
