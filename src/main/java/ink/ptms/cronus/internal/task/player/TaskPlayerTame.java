package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTameEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_tame")
public class TaskPlayerTame extends Countable<EntityTameEvent> {

    private String entity;

    public TaskPlayerTame(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        entity = data.containsKey("entity") ? String.valueOf(data.get("entity")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityTameEvent e) {
        return (entity == null || entitySelector.isSelect(e.getEntity(), entity));
    }

    @Override
    public String toString() {
        return "TaskPlayerTame{" +
                "entity=" + entity +
                ", count=" + count +
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
