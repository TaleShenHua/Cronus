package ink.ptms.cronus.internal.program.effect.impl.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectCastSkill extends Effect {

    private String skill;

    @Override
    public String pattern() {
        return "skillapi\\.cast (?<skill>.+)";
    }

    @Override
    public String getExample() {
        return "skillapi.cast [skill]";
    }

    @Override
    public void match(Matcher matcher) {
        skill = matcher.group("skill");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram && skill != null) {
            PlayerData playerData = SkillAPI.getPlayerData(((QuestProgram) program).getPlayer());
            playerData.cast(skill);
        }
    }

    @Override
    public String toString() {
        return "EffectCastSkill{" +
                "skill='" + skill + '\'' +
                '}';
    }
}
