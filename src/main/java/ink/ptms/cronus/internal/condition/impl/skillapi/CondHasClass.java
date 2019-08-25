package ink.ptms.cronus.internal.condition.impl.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondBoolean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-07-29 18:49
 */
@Cond(name = "skillapi.hasClass", pattern = "skillapi\\.hasClass (?<class>.+)", example = "skillapi.hasSkill [class]")
public class CondHasClass extends CondBoolean {

    private String className;

    @Override
    public void init(Matcher matcher, String text) {
        super.init(matcher, text);
        className = matcher.group("class");
    }

    @Override
    public boolean getBoolean(Player player, DataQuest quest, Event event) {
        PlayerData playerData = SkillAPI.getPlayerData(player);
        return playerData.hasClass(className);
    }

    @Override
    public String translate() {
        return "Has Class " + className;
    }

    @Override
    public String toString() {
        return "CondHasClass{" +
                "className='" + className + '\'' +
                ", negative=" + negative +
                '}';
    }
}
