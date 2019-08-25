package ink.ptms.cronus.internal.condition.impl.quest;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondBoolean;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "quest.completed", pattern = "quest\\.complete(d)? (?<quest>.+)", example = "quest.completed [quest]")
public class CondQuestCompleted extends CondBoolean {

    private String quest;

    @Override
    public void init(Matcher matcher, String text) {
        negative = text.startsWith("!");
        quest = matcher.group("quest");
    }

    @Override
    public boolean getBoolean(Player player, DataQuest quest, Event event) {
        return CronusAPI.getData(player).getQuestCompleted().containsKey(quest);
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-quest-completed0", quest);
        } else {
            return TLocale.asString("translate-condition-quest-completed1", quest);
        }
    }

    @Override
    public String toString() {
        return "CondQuestCompleted{" +
                "quest='" + quest + '\'' +
                ", negative=" + negative +
                '}';
    }
}
