package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_command")
public class TaskPlayerCommand extends Countable<PlayerCommandPreprocessEvent> {

    private String command;

    public TaskPlayerCommand(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        command = data.containsKey("command") ? String.valueOf(data.get("command")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerCommandPreprocessEvent e) {
        return (command == null || e.getMessage().startsWith(command));
    }

    @Override
    public String toString() {
        return "TaskPlayerCommand{" +
                "command='" + command + '\'' +
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
