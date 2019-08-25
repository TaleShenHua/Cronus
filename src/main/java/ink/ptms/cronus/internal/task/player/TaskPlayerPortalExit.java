package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPortalExitEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_portal_exit")
public class TaskPlayerPortalExit extends Countable<EntityPortalExitEvent> {

    private Location from;
    private Location to;

    public TaskPlayerPortalExit(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        from = data.containsKey("from") ? BukkitParser.toLocation(data.get("from")) : null;
        to = data.containsKey("to") ? BukkitParser.toLocation(data.get("to")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityPortalExitEvent event) {
        return (from == null || from.inSelect(event.getFrom())) && (to == null || to.inSelect(event.getFrom()));
    }

    @Override
    public String toString() {
        return "TaskPlayerPortalExit{" +
                "from=" + from +
                ", to=" + to +
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
