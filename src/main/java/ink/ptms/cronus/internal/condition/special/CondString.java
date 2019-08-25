package ink.ptms.cronus.internal.condition.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
public abstract class CondString extends Condition {

    protected Sxpression expression;

    @Override
    public void init(Matcher matcher, String text) {
        expression = new Sxpression(matcher.group("expression"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return expression.isSelect(String.valueOf(getString(player, quest, event)));
    }

    abstract public String getString(Player player, DataQuest quest, Event event);

}
