package ink.ptms.cronus.internal.program.effect.impl.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectAddAttributePoints extends Effect {

    private int count;

    @Override
    public String pattern() {
        return "skillapi\\.addattributepoint(s)? (?<count>.+)";
    }

    @Override
    public String getExample() {
        return "skillapi.addattributepoints [count]";
    }

    @Override
    public void match(Matcher matcher) {
        count = NumberConversions.toInt(matcher.group("count"));
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            PlayerData playerData = SkillAPI.getPlayerData(((QuestProgram) program).getPlayer());
            playerData.giveAttribPoints(count);
        }
    }

    @Override
    public String toString() {
        return "EffectAddAttributePoints{" +
                "count=" + count +
                '}';
    }
}
