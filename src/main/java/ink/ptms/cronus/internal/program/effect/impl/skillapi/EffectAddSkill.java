package ink.ptms.cronus.internal.program.effect.impl.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.skills.Skill;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectAddSkill extends Effect {

    @TInject
    private static TLogger logger;
    private Skill skill;

    @Override
    public String pattern() {
        return "skillapi\\.addskill (?<skill>.+)";
    }

    @Override
    public String getExample() {
        return "skillapi.addskill [skill]";
    }

    @Override
    public void match(Matcher matcher) {
        try {
            skill = SkillAPI.getSkill(matcher.group("skill"));
        } catch (Throwable t) {
            logger.warn("Invalid ManaSource: " + matcher.group("source"));
        }
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram && skill != null) {
            PlayerData playerData = SkillAPI.getPlayerData(((QuestProgram) program).getPlayer());
            playerData.giveSkill(skill);
        }
    }

    @Override
    public String toString() {
        return "EffectAddSkill{" +
                "skill=" + skill +
                '}';
    }
}
