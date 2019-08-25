package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_portal_enter")
public class TaskPlayerPortalEnter extends Countable<EntityPortalEnterEvent> {

    private Location location;

    public TaskPlayerPortalEnter(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityPortalEnterEvent event) {
        return location == null || location.inSelect(event.getLocation());
    }

    @Override
    public String toString() {
        return "TaskPlayerPortalEnter{" +
                "location=" + location +
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
