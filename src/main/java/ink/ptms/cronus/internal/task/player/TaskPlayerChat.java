package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_chat")
public class TaskPlayerChat extends Countable<AsyncPlayerChatEvent> {

    private String message;

    public TaskPlayerChat(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        message = data.containsKey("message") ? String.valueOf(data.get("message")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, AsyncPlayerChatEvent e) {
        return (message == null || e.getMessage().contains(message));
    }

    @Override
    public String toString() {
        return "TaskPlayerChat{" +
                "message='" + message + '\'' +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
