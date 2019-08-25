package ink.ptms.cronus.internal.condition;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.event.EventCondition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:10
 */
public abstract class Condition {

    abstract public void init(Matcher matcher, String text);

    abstract public boolean check(Player player, DataQuest quest, Event event);

    public boolean check(Player player, DataQuest quest) {
        return check(player, quest, new EventCondition());
    }

    public boolean check(Player player) {
        return check(player, new DataQuest(), new EventCondition());
    }

    public String translate() {
        return null;
    }
}
