package ink.ptms.cronus.internal.program.function.hook;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionSkillAPI extends Function {

    @Override
    public String getName() {
        return "skillapi";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            Player player = ((QuestProgram) program).getPlayer();
            PlayerData playerData = SkillAPI.getPlayerData(player);
            PlayerClass playerDataMainClass = playerData.getMainClass();
            switch (args[0].toLowerCase()) {
                case "points":
                    return playerData.getAttributePoints();
                case "class":
                    return playerDataMainClass == null ? "null" : playerDataMainClass.getData().getName();
                case "class.group":
                    return playerDataMainClass == null ? "null" : playerDataMainClass.getData().getGroup();
                case "class.parent":
                    return playerDataMainClass == null ? "null" : playerDataMainClass.getData().getParent();
                case "class.prefix":
                    return playerDataMainClass == null ? "null" : playerDataMainClass.getData().getPrefix();
                case "class.mana":
                    return playerDataMainClass == null ? "null" : playerDataMainClass.getData().getManaName();
                case "mana":
                    return playerData.getMana();
                case "mana.max":
                    return playerData.getMaxMana();
                case "health.last":
                    return playerData.getLastHealth();
                case "health":
                    return playerDataMainClass == null ? -1 : playerDataMainClass.getHealth();
                case "level":
                    return playerDataMainClass == null ? -1 : playerDataMainClass.getLevel();
                case "exp":
                    return playerDataMainClass == null ? -1 : playerDataMainClass.getExp();
                case "exp.total":
                    return playerDataMainClass == null ? -1 : playerDataMainClass.getTotalExp();
                case "exp.require":
                case "exp.required":
                    return playerDataMainClass == null ? -1 : playerDataMainClass.getRequiredExp();
            }
        }
        return "<Non-Quest>";
    }
}
