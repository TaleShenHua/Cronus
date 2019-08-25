package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_level")
public class TaskPlayerLevel extends Uncountable<PlayerLevelChangeEvent> {

    private Sxpression level;

    public TaskPlayerLevel(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        level = data.containsKey("level") ? new Sxpression(data.get("level")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerLevelChangeEvent e) {
        return level == null || level.isSelect(getCount(player, dataQuest, e));
    }

    @Override
    public double getCount(Player player, DataQuest dataQuest, PlayerLevelChangeEvent e) {
        return e.getNewLevel() - e.getOldLevel();
    }

    @Override
    public String toString() {
        return "TaskPlayerLevel{" +
                "level=" + level +
                ", total=" + total +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", status='" + status + '\'' +
                ", action=" + action +
                '}';
    }
}
