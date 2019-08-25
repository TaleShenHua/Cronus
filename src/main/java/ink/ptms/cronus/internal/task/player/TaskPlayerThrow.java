package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_throw")
public class TaskPlayerThrow extends Countable<ProjectileLaunchEvent> {

    private String projectile;

    public TaskPlayerThrow(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        projectile = data.containsKey("projectile") ? String.valueOf(data.get("projectile")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, ProjectileLaunchEvent e) {
        return (projectile == null || entitySelector.isSelect(e.getEntity(), projectile));
    }

    @Override
    public String toString() {
        return "TaskPlayerThrow{" +
                "projectile=" + projectile +
                ", count=" + count +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}
