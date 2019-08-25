package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.UnEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "location")
public class TaskLocation extends UnEvent {

    private Location location;

    public TaskLocation(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EventPeriod event) {
        return location == null || location.inSelect(player.getLocation());
    }

    @Override
    public String toString() {
        return "TaskLocation{" +
                "location=" + location +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
