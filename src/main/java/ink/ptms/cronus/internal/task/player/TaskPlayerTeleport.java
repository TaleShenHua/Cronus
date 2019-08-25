package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.TeleportCause;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_teleport")
public class TaskPlayerTeleport extends Countable<PlayerTeleportEvent> {

    private Location from;
    private Location to;
    private TeleportCause cause;

    public TaskPlayerTeleport(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        from = data.containsKey("from") ? BukkitParser.toLocation(data.get("from")) : null;
        to = data.containsKey("to") ? BukkitParser.toLocation(data.get("to")) : null;
        cause = data.containsKey("cause") ? BukkitParser.toTeleportCause(data.get("cause")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerTeleportEvent event) {
        return (from == null || from.inSelect(event.getFrom())) && (to == null || to.inSelect(event.getFrom())) && (cause == null || cause.isSelect(event.getCause()));
    }

    @Override
    public String toString() {
        return "TaskPlayerTeleport{" +
                "from=" + from +
                ", to=" + to +
                ", cause=" + cause +
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
