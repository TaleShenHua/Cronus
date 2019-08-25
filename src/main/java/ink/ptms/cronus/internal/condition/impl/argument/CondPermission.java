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
@Cond(name = "permission", pattern = "permission\\.(?<permission>.+)", example = "permission.[content]")
public class CondPermission extends Condition {

    private boolean negative;
    private String permission;

    @Override
    public void init(Matcher matcher, String text) {
        negative = text.startsWith("!");
        permission = String.valueOf(matcher.group("permission"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return negative != player.hasPermission(permission);
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-permission0", permission);
        } else {
            return TLocale.asString("translate-condition-permission1", permission);
        }
    }

    @Override
    public String toString() {
        return "CondPermission{" +
                "negative=" + negative +
                ", permission='" + permission + '\'' +
                '}';
    }
}
