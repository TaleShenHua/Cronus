package ink.ptms.cronus.internal.condition.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Condition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
public abstract class CondBoolean extends Condition {

    protected boolean negative;

    @Override
    public void init(Matcher matcher, String text) {
        negative = text.startsWith("!");
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return negative != getBoolean(player, quest, event);
    }

    abstract public boolean getBoolean(Player player, DataQuest quest, Event event);

}
