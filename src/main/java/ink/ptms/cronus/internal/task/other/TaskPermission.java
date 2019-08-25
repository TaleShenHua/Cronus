package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.task.special.UnEvent;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "permission")
public class TaskPermission extends UnEvent {

    private String permission;

    public TaskPermission(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        permission = String.valueOf(data.getOrDefault("permission", "*"));
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EventPeriod event) {
        return player.hasPermission(permission);
    }

    @Override
    public String toString() {
        return "TaskPermission{" +
                "permission='" + permission + '\'' +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
