package ink.ptms.cronus.internal.program.effect.impl.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectLoseExp extends Effect {

    @Override
    public String pattern() {
        return "skillapi\\.loseexp";
    }

    @Override
    public String getExample() {
        return "skillapi.loseexp";
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            PlayerData playerData = SkillAPI.getPlayerData(((QuestProgram) program).getPlayer());
            playerData.loseExp();
        }
    }

    @Override
    public String toString() {
        return "EffectLoseExp{}";
    }
}
